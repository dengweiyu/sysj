package com.li.videoapplication.data.cache;

import android.util.Log;

/**
 * 功能：缓存弹幕库数据
 */
public class DanmukuCache {

	public static final String TAG = DanmukuCache.class.getSimpleName();

	public static void save(String video_id, String xml) {
		CacheManager.save(video_id, xml);
		Log.d(TAG, "save/video_id=" + video_id);
		Log.d(TAG, "save/xml=" + xml);
	}

	public static String get(String video_id) {
		String xml = CacheManager.get(video_id);
		Log.d(TAG, "save/video_id=" + video_id);
		Log.d(TAG, "save/xml=" + xml);
		return xml;
	}
}
