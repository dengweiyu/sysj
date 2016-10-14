package com.li.videoapplication.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.li.videoapplication.R;
import com.li.videoapplication.data.network.RequestService;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.NormalPreferences;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.fragment.BannerFragment;
import com.li.videoapplication.ui.fragment.SplashFragment;
import com.li.videoapplication.ui.fragment.WelcomeFragment;
import com.li.videoapplication.ui.service.AppStartService;
import com.umeng.analytics.MobclickAgent;

/**
 * 活动：启动
 */
public class AppstartActivity extends TBaseActivity {

	@Override
	public int getContentView() {
		return R.layout.activity_appstart;
	}

	@Override
	public void beforeOnCreate() {
		super.beforeOnCreate();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setSystemBar(false);
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();

		MobclickAgent.setDebugMode(true);

		try {
			RequestService.startRequestService();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			AppStartService.startAppStartService();
		} catch (Exception e) {
			e.printStackTrace();
		}

		MainActivity activity = AppManager.getInstance().getMainActivity();
		if (activity != null) {
			ActivityManeger.startMainActivityBottom(this);
		} else {
			if (NormalPreferences.getInstance().getBoolean(Constants.APPSTART_ACTIVITY_FIRSTSETUP, true)) {
				// 启动-欢迎
				replaceFragment(new WelcomeFragment());
				NormalPreferences.getInstance().putBoolean(Constants.APPSTART_ACTIVITY_FIRSTSETUP, false);
			} else {
				// 启动商标
				replaceFragment(new SplashFragment());
				UITask.postDelayed(new Runnable() {

					@Override
					public void run() {
						// 图片广告
						replaceFragment(new BannerFragment());
					}
				}, 1500);
			}
		}
	}

	public void replaceFragment(Fragment target) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container, target).commit();
	}
}
