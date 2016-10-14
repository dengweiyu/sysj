package com.li.videoapplication.utils;

import android.util.Log;

public class LogHelper {
	
	protected final String name = this.getClass().getName();
	
	protected final String simpleName = this.getClass().getSimpleName();
	
	protected final String tag = simpleName;
	
	protected final String action = name;
	
	public static void e(String tag, String msg) {
		Log.e(tag, msg);
	}
	
	public static void d(String tag, String msg) {
		Log.d(tag, msg);
	}
	
	public static void w(String tag, String msg) {
		Log.w(tag, msg);
	}
	
	public static void v(String tag, String msg) {
		Log.v(tag, msg);
	}
	
	public static void i(String tag, String msg) {
		Log.i(tag, msg);
	}
}
