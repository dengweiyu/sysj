package com.li.videoapplication.utils;

import android.util.Log;

import java.util.UUID;

/**
 * 功能：UUID工具
 */
public class
		UUIDUtil {
	
	private static final String TAG = UUIDUtil.class.getSimpleName();
	
	/**
	 * 功能：UUID
	 * 
	 * @return
	 */
	public static String currentUUID() {
		UUID mUUID = UUID.randomUUID();
		String b = mUUID.toString();
		Log.i(TAG, "UUID=" + b);
		return b;
	}

	/**
	 *
	 * 功能：32位的UUID
	 *
	 * @return
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		// 得到对象产生的ID
		String a = uuid.toString();
		a = a.replaceAll("-", "");
		Log.i(TAG, "UUID=" + a);
		return a;
	}
}
