package com.li.videoapplication.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.PhoneRequestMsgEntity;
import com.li.videoapplication.data.model.response.VerifyCodeNewEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.AnimationHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.AuthCodeUtil;
import com.li.videoapplication.utils.StringUtil;

import io.rong.eventbus.EventBus;


/**
 * 绑定手机号码
 */

public class UploadVideoPhoneDialog extends BaseDialog implements View.OnClickListener{

    private EditText code, phone;
    private TextView get, submit;
    private ImageView cancel;
    private LoadingDialog mLoadingDialog;
    private String getPhoneText() {
        if (phone.getText() != null)
            return phone.getText().toString().trim();
        else
            return "";
    }

    private String getCodeText() {
        if (code.getText() != null)
            return code.getText().toString().trim();
        else
            return "";
    }

    public interface Callback {
        void onCall(String phone);
    }

    private Activity activity;
    private Callback callback;
    private AnimationHelper animationHelper = new AnimationHelper();

    @Override
    protected int getContentView() {
        return R.layout.dialog_upload_video_phone;
    }

    public UploadVideoPhoneDialog(Activity activity, Callback callback) {
        super(activity);
        this.activity = activity;
        this.callback = callback;

        mLoadingDialog = new LoadingDialog(activity);

        get = (TextView) findViewById(R.id.phone_get_upload_video);
        submit = (TextView) findViewById(R.id.phone_submit_upload_video);

        code = (EditText) findViewById(R.id.phone_code_upload_video);
        phone = (EditText) findViewById(R.id.phone_upload_video);

        cancel = (ImageView) findViewById(R.id.cancel_upload_video);

        get.setOnClickListener(this);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);

        setCanceledOnTouchOutside(true);

        EventBus.getDefault().register(this);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.phone_get_upload_video:
                msgRequestNew();
                break;

            case R.id.phone_submit_upload_video:
                verifyCodeNew();
                break;

            case R.id.cancel_upload_video:
                dismiss();
                break;
        }
    }

    private TimeCount mTimeCount;
    private String mobilePhone;

    /**
     * 获取验证码
     */
    private void msgRequestNew() {
        if (StringUtil.isNull(getPhoneText())) {
            ToastHelper.s("手机号码不能为空");
            animationHelper.startShake7(phone);
            return;
        }
        if (!StringUtil.isMatchMobile(getPhoneText())) {
            ToastHelper.s("填入的不是手机号码");
            animationHelper.startShake7(phone);
            return;
        }
        // 获取验证码
        String str = AuthCodeUtil.authcodeEncode(getPhoneText(), AuthCodeUtil.APP_KEY);
        Log.i(tag, "phone encode:" + str);
        DataManager.phoneRequestMsg(str);
        mTimeCount = new TimeCount(120000L, 1000L);
        mTimeCount.start();
    }

    /**
     * 获取验证码倒计时
     */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            get.setFocusable(true);
            get.setClickable(true);
            get.setText("获取验证码");
            get.setBackgroundResource(R.drawable.upload_video_phone_bg);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            get.setText("重新发送(" + millisUntilFinished / 1000 + ")");
            get.setFocusable(false);
            get.setClickable(false);
            get.setBackgroundResource(R.drawable.phone_get_gray);
        }
    }

    /**
     * 提交手机和验证码
     */
    private void verifyCodeNew() {
        if (StringUtil.isNull(getPhoneText())) {
            ToastHelper.s("手机号码不能为空");
            animationHelper.startShake7(phone);
            return;
        }
        if (!StringUtil.isMatchMobile(getPhoneText())) {
            ToastHelper.s("填入的不是手机号码");
            animationHelper.startShake7(phone);
            return;
        }
        if (StringUtil.isNull(getCodeText())) {
            ToastHelper.s("验证码不能为空");
            animationHelper.startShake7(code);
            return;
        }

        // 提交手机和验证码
        mLoadingDialog.show();
        DataManager.verifyCodeNew(getPhoneText(), getCodeText());
        mobilePhone = getPhoneText();

        setCancelable(false);


    }

    /**
     * 回调:取验证码
     */
    public void onEventMainThread(PhoneRequestMsgEntity event) {

        if (event.isResult()) {

        } else {
            mTimeCount.onFinish();
            mTimeCount.cancel();
        }
        ToastHelper.s(event.getMsg());
    }

    /**
     * 回调:提交手机和验证码
     */
    public void onEventMainThread(VerifyCodeNewEntity event) {
        mLoadingDialog.dismiss();
        if (event.isResult()) {// 成功
            dismiss();
            if (callback != null)
                callback.onCall(mobilePhone);
        } else {
            ToastHelper.s("验证失败");

            setCancelable(true);

            if (mTimeCount != null) {
                mTimeCount.onFinish();
            }
        }
    }
}
