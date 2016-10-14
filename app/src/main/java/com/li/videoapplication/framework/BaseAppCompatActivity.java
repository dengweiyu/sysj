package com.li.videoapplication.framework;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.data.network.RequestManager;
import com.li.videoapplication.utils.LogHelper;
import com.umeng.message.PushAgent;

import io.rong.eventbus.EventBus;


/**
 * 基本活动 5.0
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements IBaseActivity {

    protected final String action = this.getClass().getName();
    protected final String tag = this.getClass().getSimpleName();
	
	private Handler handler = new Handler(Looper.getMainLooper());
	public RequestManager requestManager = new RequestManager();

	public Handler getHandler() {
		return handler;
	}

	@Override
	public void refreshIntent() {
		LogHelper.d(tag, "refreshIntent");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogHelper.d(tag, "onCreate");
		beforeOnCreate();
		super.onCreate(savedInstanceState);
		refreshIntent();
		afterOnCreate();
		initView();
		loadData();
		PushAgent.getInstance(this).onAppStart();
	}

	@Override
	public void beforeOnCreate() {
		LogHelper.d(tag, "beforeOnCreate");
	}

	@Override
	public void afterOnCreate() {
		LogHelper.d(tag, "afterOnCreate");
		AppManager.getInstance().addActivity(this);
		EventBus.getDefault().register(this);
		PushAgent.getInstance(this).onAppStart();
	}

	@Override
	public void initView() {
		LogHelper.d(tag, "initView");
	}

	@Override
	public void loadData() {
		LogHelper.d(tag, "loadData");
	}

	@Override
    protected void onDestroy() {
		LogHelper.d(tag, "onDestroy");
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