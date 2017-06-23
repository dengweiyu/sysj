package com.li.videoapplication.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.LoginEntity;
import com.li.videoapplication.data.model.response.MsgRequestNewEntity;
import com.li.videoapplication.data.model.response.VerifyCodeNewEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.ShareSDKLoginHelper;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.utils.AuthCodeUtil;
import com.li.videoapplication.utils.CountDownTimerUtils;
import com.li.videoapplication.utils.PatternUtil;
import com.li.videoapplication.utils.StringUtil;

/**
 * 活动：登录
 */
public class LoginActivity extends TBaseActivity implements OnClickListener,
        OnCheckedChangeListener, TextWatcher {

    private EditText phone;
    private EditText code;
    private TextView get;
    private TextView submit;
    private String mobilePhone;
    private boolean isCountDowning;
    private ArrayAdapter<String> fmLoginAdapter;

    private ShareSDKLoginHelper loginHelper = new ShareSDKLoginHelper();

    private String getPhoneText() {
        return phone.getText().toString().trim();
    }

    private String getCodeText() {
        return code.getText().toString().trim();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        loginHelper.initSDK(this);

        setSystemBarBackgroundWhite();
        setAbTitle(R.string.login_title);
    }

    @Override
    public void initView() {
        super.initView();
        if (AppConstant.isFeiMo) initFMLogin();
        phone = (EditText) findViewById(R.id.login_phone);
        code = (EditText) findViewById(R.id.login_code);
        get = (TextView) findViewById(R.id.login_get);
        submit = (TextView) findViewById(R.id.login_submit);
        CheckBox deal = (CheckBox) findViewById(R.id.login_deal);

        get.setOnClickListener(this);
        submit.setOnClickListener(this);

        findViewById(R.id.login_qq).setOnClickListener(this);
        findViewById(R.id.login_wx).setOnClickListener(this);
        findViewById(R.id.login_wb).setOnClickListener(this);

        deal.setOnCheckedChangeListener(this);
        deal.setChecked(true);

        onCheckedChanged(deal, deal.isChecked());

        phone.setText("");
        phone.addTextChangedListener(this);
        code.addTextChangedListener(this);
        submit.setFocusable(false);
        submit.setClickable(false);
        get.setFocusable(false);
        get.setClickable(false);
    }

    private void initFMLogin() {
        Spinner fm_login = (Spinner) findViewById(R.id.fm_login);
        fm_login.setVisibility(View.VISIBLE);
        String[] fmLoginData = getResources().getStringArray(R.array.fm_login);

        fmLoginAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fmLoginData);
        fmLoginAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fm_login.setAdapter(fmLoginAdapter);
        fm_login.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //当item被选中时，会调用此方法
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fmID = fmLoginAdapter.getItem(position);
                Log.d(tag, "onItemSelected: pos = " + position + " : " + fmID);
                if (position != 0) {
                    showProgressDialog(LoadingDialog.LOHIN);
                    // 飞磨快速登录
                    DataManager.loginFm(fmID);
                }
            }

            /**
             * 当数据项的值设置为空时，就会调用此方法，通过调用adapter.clear()方法清空数据，并且刷新界面
             * 时，会调用次方法
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(tag, "onNothingSelected: ");
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (getPhoneText().length() == 11) {
            if (!isCountDowning) {
                get.setBackgroundResource(R.drawable.dialog_registermobile_red);
                get.setFocusable(true);
                get.setClickable(true);
            }
            if (getCodeText().length() == 4) {
                submit.setBackgroundResource(R.drawable.dialog_registermobile_red);
                submit.setFocusable(true);
                submit.setClickable(true);
            } else {
                submit.setBackgroundResource(R.drawable.login_pink);
                submit.setFocusable(false);
                submit.setClickable(false);
            }
        } else {
            get.setBackgroundResource(R.drawable.login_pink);
            get.setFocusable(false);
            get.setClickable(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            submit.setFocusable(true);
            submit.setClickable(true);
            if (getPhoneText().length() == 11 && getCodeText().length() == 4) {
                submit.setBackgroundResource(R.drawable.dialog_registermobile_red);
            } else {
                submit.setBackgroundResource(R.drawable.login_pink);
            }
        } else {
            submit.setFocusable(false);
            submit.setClickable(false);
            submit.setBackgroundResource(R.drawable.login_submit_gray);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_get:
                msgRequestNew();
                break;

            case R.id.login_submit:
                verifyCodeNew();
                break;

            case R.id.login_qq:
                animationHelper.startAnimationShake(v);
                loginHelper.qq();
                break;

            case R.id.login_wx:
                animationHelper.startAnimationShake(v);
                loginHelper.wx();
                break;

            case R.id.login_wb:
                animationHelper.startAnimationShake(v);
                loginHelper.wb();
                break;

            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void msgRequestNew() {
        if (StringUtil.isNull(getPhoneText())) {
            showToastShort("手机号码不能为空");
            animationHelper.startAnimationShake(phone);
            return;
        }
        if (!PatternUtil.isMatchMobile(getPhoneText())) {
            showToastShort("请输入正确的手机号");
            animationHelper.startAnimationShake(code);
            return;
        }
        // 加密手机号
        String encode = AuthCodeUtil.authcodeEncode(getPhoneText(), AuthCodeUtil.APP_KEY);
        Log.d(tag, "encode: " + encode);
        // 获取验证码
        DataManager.msgRequestNew(encode);
    }

    /**
     * 提交手机和验证码
     */
    private void verifyCodeNew() {
        if (StringUtil.isNull(getPhoneText())) {
            showToastShort("手机号码不能为空");
            animationHelper.startAnimationShake(phone);
            return;
        }
        if (!PatternUtil.isMatchMobile(getPhoneText())) {
            showToastShort("填入的不是手机号码");
            animationHelper.startAnimationShake(phone);
            return;
        }
        if (StringUtil.isNull(getCodeText())) {
            showToastShort("验证码不能为空");
            animationHelper.startAnimationShake(code);
            return;
        }

        // 提交手机和验证码
        DataManager.verifyCodeNew(getPhoneText(), getCodeText());
        mobilePhone = getPhoneText();
        showProgressDialog(LoadingDialog.LOHIN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginHelper.stopSDK(this);
    }

    /**
     * 回调:取验证码
     */
    public void onEventMainThread(MsgRequestNewEntity event) {
        if (event != null) {
            showToastShort(event.getMsg());
            if (event.isResult()) {
                new CountDownTimerUtils(get, 60000, 1000,
                        R.drawable.dialog_registermobile_gray, R.drawable.dialog_registermobile_red,
                        new CountDownTimerUtils.ICountDownFinish() {
                            @Override
                            public void onFinish() {
                                phone.addTextChangedListener(LoginActivity.this);
                                isCountDowning = false;
                            }
                        }).start();
                isCountDowning = true;
                phone.removeTextChangedListener(this);
            }
        }
    }

    /**
     * 回调:提交手机和验证码
     */
    public void onEventMainThread(VerifyCodeNewEntity event) {
        if (event != null) {
            if (event.isResult()) {
                // 登录
                DataManager.login(mobilePhone);
            } else {
                dismissProgressDialog();
                showToastShort(event.getMsg());
            }
        }
    }

    /**
     * 回调:登录
     */
    public void onEventMainThread(LoginEntity event) {
        if (event != null) {
            dismissProgressDialog();
            if (event.isResult()) {
                showToastShort("登录成功");
                DataManager.userProfilePersonalInformation(getMember_id(), getMember_id());
                UITask.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        AppAccount.login();
                    }
                }, 400);
                finish();
            } else {
                showToastShort(event.getMsg());
            }
        }
    }
}
