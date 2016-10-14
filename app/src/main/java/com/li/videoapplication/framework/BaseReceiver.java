package com.li.videoapplication.framework;

import android.content.BroadcastReceiver;

/**
 * 基本广播接收器
 */
public abstract class BaseReceiver extends BroadcastReceiver {
	
	public final String action = this.getClass().getName();
	
	public final String tag = this.getClass().getSimpleName();
}
