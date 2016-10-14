package com.li.videoapplication.framework;


import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * 基本碎片
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) 
public abstract class TBaseChildFragment extends TBaseFragment {

	protected FragmentManager childManager;

	protected FragmentManager manager;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		childManager =  getChildFragmentManager();
		manager = ((FragmentActivity) activity).getSupportFragmentManager();
	}
}
