package com.li.videoapplication.framework;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.data.network.RequestManager;

import io.rong.eventbus.EventBus;

/**
 * 基本活动
 */
public abstract class BaseActivity extends FragmentActivity implements IBaseActivity {

    protected final String action = this.getClass().getName();
    protected final String tag = this.getClass().getSimpleName();
	
	private Handler handler = new Handler(Looper.getMainLooper());
	public RequestManager requestManager = new RequestManager();

	public Handler getHandler() {
		return handler;
	}

	@Override
	public void refreshIntent() {
		Log.d(tag, "refreshIntent");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(tag, "onCreate");
		beforeOnCreate();
		super.onCreate(savedInstanceState);
		refreshIntent();
		afterOnCreate();
		initView();
		loadData();
	}

	@Override
	public void beforeOnCreate() {
		Log.d(tag, "beforeOnCreate");
	}

	@Override
	public void afterOnCreate() {
		Log.d(tag, "afterOnCreate");
		AppManager.getInstance().addActivity(this);
		EventBus.getDefault().register(this);
	}

	@Override
	public void initView() {
		Log.d(tag, "initView");
	}

	@Override
	public void loadData() {
		Log.d(tag, "loadHomeData");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(tag, "onStart: ");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(tag, "onRestart: ");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(tag, "onResume: ");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(tag, "onPause: ");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(tag, "onStop: ");
	}

	@Override
    protected void onDestroy() {
		Log.d(tag, "onDestroy");
		if (handler != null)
			try {
				handler.removeCallbacksAndMessages(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		if (requestManager != null) {
			requestManager.cancelAllTask();
			requestManager.clearAllTask();
		}
		EventBus.getDefault().unregister(this);
		AppManager.getInstance().removeActivity(this);
        super.onDestroy();
		ImageLoaderHelper.clearMemoryCache();
    }

	public void onEventAsync(Object event) {
		// Log.d(tag, "event=" + event.toString());
	}

	public void post(Runnable r) {
		if (handler == null)
			return;
		if (r == null)
			return;
		handler.post(r);
	}

	public void postAtTime(Runnable r, long uptimeMillis) {
		if (handler == null)
			return;
		if (r == null)
			return;
		handler.postAtTime(r, uptimeMillis);
	}

	public void postAtTime(Runnable r, Object token, long uptimeMillis) {
		if (handler == null)
			return;
		if (r == null)
			return;
		handler.postAtTime(r, token, uptimeMillis);
	}

	public void postDelayed(Runnable r, long delayMillis) {
		if (handler == null)
			return;
		if (r == null)
			return;
		handler.postDelayed(r, delayMillis);
	}

	public void postAtFrontOfQueue(Runnable r) {
		if (handler == null)
			return;
		if (r == null)
			return;
		handler.postAtFrontOfQueue(r);
	}
}