package com.li.videoapplication.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.LoginEntity;
import com.li.videoapplication.data.model.response.MsgRequestNewEntity;
import com.li.videoapplication.data.model.response.VerifyCodeNewEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.ShareSDKLoginHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.dialog.LoadingDialog;
import com.li.videoapplication.utils.PatternUtil;
import com.li.videoapplication.utils.StringUtil;
/**
 * 活动：登录
 */
public class LoginActivity extends TBaseActivity implements OnClickListener, OnCheckedChangeListener {

	/**
	 * 跳转：个人资料
	 */
	public void startMyPersonalInfoActivity() {
		ActivityManeger.startMyPersonalInfoActivity(this);
	}

	private EditText phone;
	private EditText code;
	private TextView get;
	private TextView submit;
	private CheckBox deal;
	private RelativeLayout qq;
	private RelativeLayout wx;
	private RelativeLayout wb;

	private boolean isFromChat = false;
	private String event_id;

	private TimeCount mTimeCount;

	private String mobilePhone;
	
	private ShareSDKLoginHelper loginHelper = new ShareSDKLoginHelper();

	private String getPhoneText() {
		return phone.getText().toString().trim();
	}

	private String getCodeText() {
		return code.getText().toString().trim();
	}

	@Override
	public void refreshIntent() {
		super.refreshIntent();
		try {
			isFromChat = getIntent().getBooleanExtra("isFromChat", false);
			event_id = getIntent().getStringExtra("event_id");
		} catch (Exception e) {
			e.printStackTrace();
		}
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

		phone = (EditText) findViewById(R.id.login_phone);
		code = (EditText) findViewById(R.id.login_code);
		get = (TextView) findViewById(R.id.login_get);
		submit = (TextView) findViewById(R.id.login_submit);
		deal = (CheckBox) findViewById(R.id.login_deal);
		qq = (RelativeLayout) findViewById(R.id.login_qq);
		wx = (RelativeLayout) findViewById(R.id.login_wx);
		wb = (RelativeLayout) findViewById(R.id.login_wb);

		get.setOnClickListener(this);
		submit.setOnClickListener(this);
		qq.setOnClickListener(this);
		wx.setOnClickListener(this);
		wb.setOnClickListener(this);

		deal.setOnCheckedChangeListener(this);
		deal.setChecked(true);

		onCheckedChanged(deal, deal.isChecked());

		phone.setText("");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		loginHelper.stopSDK(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (isChecked) {
			submit.setFocusable(true);
			submit.setClickable(true);
			submit.setBackgroundResource(R.drawable.login_submit_btn);
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
			get.setBackgroundResource(R.drawable.login_get_btn);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
			get.setText("重新发送(" + millisUntilFinished / 1000 + ")");
			get.setFocusable(false);
			get.setClickable(false);
			get.setBackgroundResource(R.drawable.login_get_pressed);
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
		// 获取验证码
		DataManager.msgRequestNew(getPhoneText());
		mTimeCount = new TimeCount(60000, 1000);
		mTimeCount.start();
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

	/**
	 * 回调:取验证码
	 */
	public void onEventMainThread(MsgRequestNewEntity event) {

		if (event != null) {
			boolean result = event.isResult();
			String msg = event.getMsg();
			if (result) {// 成功
				showToastShort(msg);
			}
		} else {
			// showToastShort("验证码发送失败！请重新发送");
		}
	}

	/**
	 * 回调:提交手机和验证码
	 */
	public void onEventMainThread(VerifyCodeNewEntity event) {

		if (event != null) {
			boolean result = event.isResult();
			String msg = event.getMsg();
			if (result) {// 成功
				// showToastShort(msg);
				// 登录
				DataManager.login(mobilePhone);
				return;
			}
		}
		if (mTimeCount != null) {
			mTimeCount.onFinish();
		}
		dismissProgressDialog();
		showToastShort("登录失败！");
	}

	/**
	 * 回调:登录
	 */
	public void onEventMainThread(LoginEntity event) {

		if (event != null) {
			if (event.isResult()) {// 成功
				showToastShort("登录成功");

				DataManager.userProfilePersonalInformation(getMember_id(), getMember_id());
				if (isFromChat) {
					//赛事详情
					AppManager.getInstance().removeActivity(GameMatchDetailActivity.class);
					ActivityManeger.startGameMatchDetailActivityNewTask(this, event_id);
				}/*else {
					// 个人资料
					startMyPersonalInfoActivity();
				}*/
				UITask.postDelayed(new Runnable() {

					@Override
					public void run() {
						AppAccount.login();
					}
				}, 400);
				finish();
				return;
			}
		}
		dismissProgressDialog();
		showToastShort("登录失败！");
	}
}
