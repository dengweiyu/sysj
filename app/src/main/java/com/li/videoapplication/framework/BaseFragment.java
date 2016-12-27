package com.li.videoapplication.framework;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import io.rong.eventbus.EventBus;
import rx.Subscription;


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
	public void onAttach(Context context) {
		Log.d(tag, "onAttach: ");
		super.onAttach(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(tag, "onCreate");
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d(tag, "onCreateView: ");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		Log.d(tag, "onActivityCreated: ");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		Log.d(tag, "onStart: ");
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.d(tag, "onResume: ");
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.d(tag, "onPause: ");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.d(tag, "onStop: ");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		Log.d(tag, "onDestroyView: ");
		super.onDestroyView();
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

	@Override
	public void onDetach() {
		Log.d(tag, "onDetach: ");
		super.onDetach();
	}

	public void onEventBackgroundThread(Object event) {
		// Log.d(tag, "event=" + event.toString());
	}
}
