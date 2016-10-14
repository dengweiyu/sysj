package com.li.videoapplication.framework;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;

import io.rong.eventbus.EventBus;


/**
 * 基本碎片
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) 
public abstract class BaseFragment extends Fragment {
	
	public final String action = this.getClass().getName();
	public final String tag = this.getClass().getSimpleName();
	
	protected Handler handler = new Handler(Looper.getMainLooper());
	protected Gson gson = new Gson();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(tag, "onCreate");
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}
	
	@Override
	public void onDestroy() {
		Log.d(tag, "onDestroy");
		EventBus.getDefault().unregister(this);
		try {
			handler.removeCallbacksAndMessages(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	public void onEventBackgroundThread(Object event) {
		// Log.d(tag, "event=" + event.toString());
	}
}
