package com.li.videoapplication.framework;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 基本活动：注册/接受广播
 */
public abstract class BaseSlidingActivity extends SlidingFragmentActivity {

	protected final String action = this.getClass().getName();
	protected final String tag = this.getClass().getSimpleName();

	protected Handler handler = new Handler(Looper.getMainLooper());
	protected LayoutInflater inflater;
	protected FragmentManager manager;
	protected Resources resources;
	protected ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(tag, "onCreate");
		super.onCreate(savedInstanceState);

		inflater = LayoutInflater.from(this);
		manager = getSupportFragmentManager();
		resources = getResources();

		actionBar = getActionBar();
	}

	public void onResume() {
		Log.d(tag, "onResume");
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		Log.d(tag, "onPause");
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onDestroy() {
		Log.d(tag, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.d(tag, "onNewIntent");
		super.onNewIntent(intent);
	}

	protected void setActionBar(View view) {
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(view, params);
	}
}