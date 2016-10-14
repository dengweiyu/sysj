package com.li.videoapplication.data.cache;

import android.util.Log;

import com.li.videoapplication.data.network.Utils;
import com.li.videoapplication.utils.StringUtil;

import java.util.Map;

/**
 * 功能：缓存网络访问数据
 */
public class RequestCache {

	public static final String TAG = RequestCache.class.getSimpleName();
	
	public static void save(String url, Map<String, Object> params, String resultString) {
		if (StringUtil.isNull(url)) {
			throw new NullPointerException();
		}
		url = Utils.getNewUrl(url, params);
		CacheManager.save(url, resultString);
		Log.d(TAG, "save/url=" + url);
		Log.d(TAG, "save/resultString=" + resultString);
	}
	
	public static String get(String url, Map<String, Object> params) {
		if (StringUtil.isNull(url)) {
			throw new NullPointerException();
		}
		url = Utils.getNewUrl(url, params);
		String resultString = CacheManager.get(url);
		Log.d(TAG, "get/url=" + url);
		Log.d(TAG, "get/resultString=" + resultString);
		return resultString;
	}
}
