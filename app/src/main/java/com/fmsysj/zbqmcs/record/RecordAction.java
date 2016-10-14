package com.fmsysj.zbqmcs.record;


/**
 *广播数据 
 * @author lin
 * Create：2014-12
 */


import android.content.Intent;

public class RecordAction {
	public static final String ACTION = "com.fmscreenrecorder.ACTION";

	public static Intent getActionIntent(String action) {
		Intent intent = new Intent(ACTION);
		intent.putExtra("action", action);
		return intent;
	}
}
