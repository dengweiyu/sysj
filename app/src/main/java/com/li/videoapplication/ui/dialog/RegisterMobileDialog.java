package com.li.videoapplication.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.MsgRequestNewEntity;
import com.li.videoapplication.data.model.response.PhoneRequestMsgEntity;
import com.li.videoapplication.data.model.response.VerifyCodeNewEntity;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.tools.AnimationHelper;
import com.li.videoapplication.ui.toast.ToastHelper;
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
    private MobileCallback mobileCallback;
    private TextView getCode;
    private EditText mobile, code;

    public RegisterMobileDialog(Context context, String oldMobile, MobileCallback mobileCallback) {
        super(context);

        this.mobileCallback = mobileCallback;
        animationHelper = new AnimationHelper();
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

        mobile = (EditText) findViewById(R.id.dialog_registermobile_mobile);
        code = (EditText) findViewById(R.id.dialog_registermobile_code);
        getCode = (TextView) findViewById(R.id.dialog_registermobile_getcode);

        getCode.setOnClickListener(this);
        findViewById(R.id.dialog_registermobile_verify).setOnClickListener(this);

        if (!StringUtil.isNull(oldMobile))
            mobile.setText(oldMobile);

        mobile.requestFocus();
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_registermobile;
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
                // FIXME: 2016/8/27
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
        if (!StringUtil.isMobileNumber(getMobileText())) {
            showToastShort("请输入正确的手机号");
            animationHelper.startAnimationShake(code);
            return;
        }
        // 获取验证码
        DataManager.phoneRequestMsg(getMobileText());

        CountDownTimerUtils countDownTimer = new CountDownTimerUtils(getCode, 60000, 1000,
                R.drawable.dialog_registermobile_gray, R.drawable.dialog_registermobile_red);
        countDownTimer.start();
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

        // FIXME: 2016/8/27
        // 提交手机和验证码
        DataManager.verifyCodeNew(getMobileText(), getCodeText());


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

    public interface MobileCallback {
        void onMobileCallback(DialogInterface dialog, String mobileNum);
    }

    /**
     * 回调:取验证码
     */
    public void onEventMainThread(PhoneRequestMsgEntity event) {

        if (event != null) {
            boolean result = event.isResult();
            String msg = event.getMsg();
            if (result) {// 成功
                showToastShort(msg);
            }
        }
    }

    /**
     * 回调:提交手机和验证码
     */
    public void onEventMainThread(VerifyCodeNewEntity event) {

        if (event != null) {
            if (event.isResult()) {// 验证成功
                if (mobileCallback != null)
                    mobileCallback.onMobileCallback(this, getMobileText());
            } else {
                ToastHelper.s("验证码错误");
            }
        }
    }
}
