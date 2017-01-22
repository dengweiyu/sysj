package com.li.videoapplication.ui.dialog;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.LoginEntity;
import com.li.videoapplication.data.model.response.MsgRequestNewEntity;
import com.li.videoapplication.data.model.response.VerifyCodeNewEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.BaseDialog;
import com.li.videoapplication.framework.BaseOverShootDialog;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.ShareSDKLoginHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.activity.LoginActivity;
import com.li.videoapplication.utils.AuthCodeUtil;
import com.li.videoapplication.utils.CountDownTimerUtils;
import com.li.videoapplication.utils.PatternUtil;
import com.li.videoapplication.utils.StringUtil;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import io.rong.eventbus.EventBus;

/**
 * 弹框：登陆
 */
public class LogInDialog extends BaseOverShootDialog implements View.OnClickListener {

    private TextInputLayout code_inputll;
    private AppCompatEditText phone, code;
    private TextView getCode, login;
    private Activity activity;
    private ShareSDKLoginHelper loginHelper;
    private LoadingDialog pd;

    public LogInDialog(Context context) {
        super(context, R.style.MyDialog);

        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

        setCanceledOnTouchOutside(false);

        loginHelper = new ShareSDKLoginHelper();
        loginHelper.initSDK(context);
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_login;
    }

    @Override
    protected void afterContentView(Context context) {
        super.afterContentView(context);

        code_inputll = (TextInputLayout) findViewById(R.id.login_codeinputll);
        phone = (AppCompatEditText) findViewById(R.id.login_phone);
        code = (AppCompatEditText) findViewById(R.id.login_code);
        getCode = (TextView) findViewById(R.id.login_getcode);
        login = (TextView) findViewById(R.id.login_login);

        getCode.setOnClickListener(this);
        login.setOnClickListener(this);
        findViewById(R.id.login_qq).setOnClickListener(this);
        findViewById(R.id.login_wx).setOnClickListener(this);
        findViewById(R.id.login_wb).setOnClickListener(this);
        findViewById(R.id.login_close).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_close:
                dismiss();
                break;

            case R.id.login_getcode:
                msgRequestNew();
                break;

            case R.id.login_login:
                verifyCodeNew();
                break;

            case R.id.login_qq:
                loginHelper.qq();
                break;

            case R.id.login_wx:
                loginHelper.wx();
                break;

            case R.id.login_wb:
                loginHelper.wb();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void msgRequestNew() {
        if (StringUtil.isNull(getPhoneText())) {
            code_inputll.setError("手机号码不能为空");
            return;
        }
        if (!PatternUtil.isMatchMobile(getPhoneText())) {
            code_inputll.setError("请输入正确的手机号");
            return;
        }
        // 加密手机号
        String encode = AuthCodeUtil.authcodeEncode(getPhoneText(), AuthCodeUtil.APP_KEY);
        // 获取验证码
        DataManager.msgRequestNew(encode);
    }

    /**
     * 提交手机和验证码
     */
    private void verifyCodeNew() {
        if (StringUtil.isNull(getPhoneText())) {
            code_inputll.setError("手机号码不能为空");
            return;
        }
        if (!PatternUtil.isMatchMobile(getPhoneText())) {
            code_inputll.setError("请输入正确的手机号");
            return;
        }
        if (StringUtil.isNull(getCodeText())) {
            code_inputll.setError("验证码不能为空");
            return;
        }
        // 提交手机和验证码
        DataManager.verifyCodeNew(getPhoneText(), getCodeText());
        showProgressDialog();
    }

    private String getPhoneText() {
        return phone.getText().toString().trim();
    }

    private String getCodeText() {
        return code.getText().toString().trim();
    }

    private void showProgressDialog() {
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
            pd = null;
        }
        pd = new LoadingDialog(activity, R.style.MyDialog);
        pd.setProgressText("登录中...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        //10秒后还未登陆成功则取消对话框
        TimeHelper.runAfter(new TimeHelper.RunAfter() {
            @Override
            public void runAfter() {
                if (!PreferencesHepler.getInstance().isLogin()){
                    dismissProgressDialog();
                }
            }
        }, 1000 * 10);
    }

    private void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    /**
     * 回调:取验证码
     */
    public void onEventMainThread(MsgRequestNewEntity event) {
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
                DataManager.login(getPhoneText());// 登录
            } else {
                code_inputll.setError(event.getMsg());
                dismissProgressDialog();
                showToastShort("登录失败！");
            }
        }
    }

    /**
     * 回调:登录
     */
    public void onEventMainThread(LoginEntity event) {
        dismissProgressDialog();
        if (event != null && event.isResult()) {
            // 成功
            showToastShort("登录成功");
            String member_id = PreferencesHepler.getInstance().getMember_id();
            DataManager.userProfilePersonalInformation(member_id, member_id);
            UITask.postDelayed(new Runnable() {

                @Override
                public void run() {
                    AppAccount.login();
                }
            }, 400);
            dismiss();
            return;
        }
        showToastShort("登录失败！");
    }
}
