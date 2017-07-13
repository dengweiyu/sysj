package com.li.videoapplication.ui.activity;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.data.model.response.UpdateVersionAboutEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.utils.AppUtil;

/**
 * 活动：关于
 */
public class AboutActivity extends TBaseActivity implements OnClickListener {

    private LinearLayout newFunction;
	private LinearLayout website;
	private LinearLayout wx;
	private LinearLayout qq;
	private LinearLayout teamwork;
	private LinearLayout talents;
	private LinearLayout privacy;
	private LinearLayout feedback;
	private TextView version;

	@Override
	public int getContentView() {
		return R.layout.activity_about;
	}

	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();

		setSystemBarBackgroundWhite();
		setAbTitle(R.string.about_title);
	}

	@Override
	public void initView() {
		super.initView();

		newFunction = (LinearLayout) findViewById(R.id.about_newFunction);
		website = (LinearLayout) findViewById(R.id.about_website);
		wx = (LinearLayout) findViewById(R.id.about_wx);
		qq = (LinearLayout) findViewById(R.id.about_qq);
		teamwork = (LinearLayout) findViewById(R.id.about_teamwork);
		talents = (LinearLayout) findViewById(R.id.about_talents);
		privacy = (LinearLayout) findViewById(R.id.about_privacy);
		feedback = (LinearLayout) findViewById(R.id.about_feedback);

		version = (TextView) findViewById(R.id.about_version);

		newFunction.setOnClickListener(this);
		website.setOnClickListener(this);
		wx.setOnClickListener(this);
		qq.setOnClickListener(this);
		teamwork.setOnClickListener(this);
		talents.setOnClickListener(this);
		privacy.setOnClickListener(this);
		feedback.setOnClickListener(this);

		String version = AppUtil.getVersionName(this);
		if (version != null)
			this.version.setText(version);
		else
			this.version.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.about_newFunction:// 新功能介绍
			newFunction();
			break;

		case R.id.about_website:
			website();
			break;

		case R.id.about_wx:
			wx();
			break;

		case R.id.about_qq:
			qq();
			break;

		case R.id.about_teamwork:
			teamwork();
			break;

		case R.id.about_talents:
			talents();
			break;

		case R.id.about_privacy:
			ActivityManager.startPrivacyActivity(this);
			break;

		case R.id.about_feedback:
			feedback();
			break;
		}
	}

	/**
	 * 新功能介绍
	 */
	private void newFunction() {
		Update update = PreferencesHepler.getInstance().getUpdate();
		if (update != null) {
			newFunction(update);
		} else {
			// 版本更新
			DataManager.updateVersionAbout();
		}
	}
	
	private void newFunction(Update update) {

		String changelog = update.getChange_log();
		String[] changeArray = changelog.split(";");
		changelog = "";
		for (int i = 0; i < changeArray.length; i++) {
			if (i != changeArray.length) {
				changelog += changeArray[i] + "\n";
			} else {
				changelog += changeArray[i];
			}
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("新功能介绍");
		builder.setMessage(changelog);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	
	}

	/**
	 * 官方网站
	 */
	private void website() {

		Uri uri = Uri.parse(getString(R.string.about_website_content));
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
		try {
			this.startActivity(intent);
		} catch (Exception e) {
			IntentHelper.startActivityActionView(this,"http://www.17sysj.com");
		}
	}

	/**
	 * 微信公众号
	 */
	@SuppressWarnings("deprecation")
	private void wx() {

		// 将微信公众号复制进粘贴板
		ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		mClipboardManager.setText("fmsysj");

		new AlertDialog.Builder(this).setMessage("\n公众号“fmsysj”已复制，您可以在微信中直接粘贴搜索。\n").setPositiveButton("前往微信", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				try {
					Intent intent = new Intent();
					ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
					intent.setAction(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setComponent(cmp);
					startActivityForResult(intent, 0);
				} catch (Exception e) {
					showToastShort("打开失败，请确保您手机有安装微信客户端");
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();
	}

	/**
	 * 玩家QQ群
	 */
	/****************
	 *
	 * 发起添加群流程。群号：手游视界活动群(251575665) 的 key 为： uyxdSCiWyI0eHzS5bmbs95pP-LT3OrwQ
	 * 调用 joinQQGroup(uyxdSCiWyI0eHzS5bmbs95pP-LT3OrwQ) 即可发起手Q客户端申请加群 手游视界活动群(251575665)
	 *  key 由官网生成的key
	 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
	 ******************/
	private boolean qq() {

		String key = "uyxdSCiWyI0eHzS5bmbs95pP-LT3OrwQ";
		String qq = getString(R.string.about_qq_content);
		Intent intent = new Intent();
		intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
		// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		try {
			startActivity(intent);
			return true;
		} catch (Exception e) {
			showToastShort("未安装手Q或安装的版本不支持");
			return false;
		}
	}

	/**
	 * 合作邮箱
	 */
	private void teamwork() {
		String address = resources.getString(R.string.about_teamwork_content);
		Uri uri = Uri.parse("mailto:" + address);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		// intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
		// intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
		// intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
		startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
	}
	
	/**
	 * 人才招聘
	 */
	private void talents() {
		String address = resources.getString(R.string.about_talents_content);
		Uri uri = Uri.parse("mailto:" + address);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
	}

    /**
	 * 用户反馈
	 */
	private void feedback() {
//		FeedbackAgent agent = new FeedbackAgent(this);
//		agent.startFeedbackActivity();
	//	FeedbackAPI.openFeedbackActivity();

		ActivityManager.startFeedbackActivity(this);
	}

	/**
	 * 回调：版本更新
	 * 
	 * @param event
	 */
	public void onEventMainThread(UpdateVersionAboutEntity event) {

		if (event != null) {
			if (event.isResult()) {
				Update update = event.getData().get(0);
				if (update != null) {
					newFunction(update);
				}
			}
		}
	}
}
