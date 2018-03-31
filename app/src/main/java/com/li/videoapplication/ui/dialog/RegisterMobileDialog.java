package com.li.videoapplication.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.PhoneRequestMsgEntity;
import com.li.videoapplication.data.model.response.UserProfileFinishMemberInfoEntity;
import com.li.videoapplication.data.model.response.VerifyCodeNewEntity;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.AnimationHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.AuthCodeUtil;
import com.li.videoapplication.utils.CountDownTimerUtils;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.PatternUtil;
import com.li.videoapplication.utils.StringUtil;

import io.rong.eventbus.EventBus;

/**
 * 弹框：编辑手机号码验证
 */
public class RegisterMobileDialog extends BaseDialog implements View.OnClickListener {

    private AnimationHelper animationHelper;
    private Activity activity;
    private TextView getCode;
    private EditText mobile, code;
    private Member newMember;

    public RegisterMobileDialog(Context context) {
        super(context);

        animationHelper = new AnimationHelper();
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        newMember = (Member) getUser().clone();

        mobile = (EditText) findViewById(R.id.dialog_registermobile_mobile);
        code = (EditText) findViewById(R.id.dialog_registermobile_code);
        getCode = (TextView) findViewById(R.id.dialog_registermobile_getcode);
        TextView title = (TextView) findViewById(R.id.registermobile_title);

        getCode.setOnClickListener(this);
        findViewById(R.id.dialog_registermobile_verify).setOnClickListener(this);

        if (!StringUtil.isNull(newMember.getMobile())) {
            mobile.setText(newMember.getMobile());
            //设置新光标所在的位置
            Editable e = mobile.getText();
            Selection.setSelection(e, e.length());
            title.setText("更改手机号码");
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_registermobile;
    }

    public void showKeyboard() {
        if (mobile != null) {
            InputUtil.showKeyboard(mobile);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    private String getMobileText() {
        if (mobile.getText() != null)
            return mobile.getText().toString();
        return "";
    }

    private String getCodeText() {
        if (code.getText() != null)
            return code.getText().toString();
        return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_registermobile_getcode:
                // 获取验证码
                msgRequestNew();
                break;

            case R.id.dialog_registermobile_verify:
                verifyCodeNew();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void msgRequestNew() {
        if (StringUtil.isNull(getMobileText())) {
            showToastShort("手机号码不能为空");
            animationHelper.startAnimationShake(mobile);
            return;
        }
        if (!PatternUtil.isMatchMobile(getMobileText())) {
            showToastShort("请输入正确的手机号");
            animationHelper.startAnimationShake(code);
            return;
        }
        // 加密手机号
        String encode = AuthCodeUtil.authcodeEncode(getMobileText(), AuthCodeUtil.APP_KEY);
        // 获取验证码
        DataManager.phoneRequestMsg(encode);
    }

    /**
     * 提交手机和验证码
     */
    private void verifyCodeNew() {
        if (StringUtil.isNull(getMobileText())) {
            showToastShort("手机号码不能为空");
            animationHelper.startAnimationShake(mobile);
            return;
        }
        if (!PatternUtil.isMatchMobile(getMobileText())) {
            showToastShort("请输入正确的手机号");
            animationHelper.startAnimationShake(mobile);
            return;
        }
        if (StringUtil.isNull(getCodeText())) {
            showToastShort("验证码不能为空");
            animationHelper.startAnimationShake(code);
            return;
        }

        // 提交手机和验证码
        DataManager.verifyCodeNew(getMobileText(), getCodeText());
    }

    /**
     * 回调:取验证码
     */
    public void onEventMainThread(PhoneRequestMsgEntity event) {

        if (event != null) {
            showToastShort(event.getMsg());
            if (event.isResult()) {// 成功
                CountDownTimerUtils countDownTimer = new CountDownTimerUtils(getCode, 60000, 1000,
                        R.drawable.dialog_registermobile_gray, R.drawable.dialog_registermobile_red);
                countDownTimer.start();
            }
        }
    }

    /**
     * 回调:提交手机和验证码
     */
    public void onEventMainThread(VerifyCodeNewEntity event) {

        if (event != null) {
            if (event.isResult()) {// 验证成功
                newMember.setMobile(getMobileText());
                // 编辑个人资料
                DataManager.userProfileFinishMemberInfo(newMember);
            } else {
                ToastHelper.s(event.getMsg().toString());
            }
        }
    }

    /**
     * 回调:编辑个人资料
     */
    public void onEventMainThread(UserProfileFinishMemberInfoEntity event) {
        if (event != null) {
            ToastHelper.s(event.getMsg());
            if (event.isResult()) dismiss();
        }
    }

    @Override
    public void dismiss() {
        try {
            InputUtil.closeKeyboard(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.dismiss();
    }
}
