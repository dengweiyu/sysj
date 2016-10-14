package com.li.videoapplication.framework;

import android.app.Service;
import android.util.Log;

import com.google.gson.Gson;

import io.rong.eventbus.EventBus;

/**
 * 基本服务
 */
public abstract class BaseService extends Service {
	
	protected final String action = this.getClass().getName();
	protected final String tag = this.getClass().getSimpleName();
    protected Gson gson = new Gson();
    
    @Override
    public void onCreate() {
    	super.onCreate();
		Log.d(tag, "onCreate");

        EventBus.getDefault().register(this);
        AppManager.getInstance().addService(this);
    }
    
    @Override
    public void onDestroy() {
        Log.d(tag, "onDestroy");
        AppManager.getInstance().removeService(this);
    	super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    public void onEventAsync(Object event) {
//		Log.d(tag, "event=" + event.toString());
    }
}
