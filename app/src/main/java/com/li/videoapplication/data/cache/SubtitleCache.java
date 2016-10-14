package com.li.videoapplication.data.cache;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 功能：缓存字幕数据
 */
public class SubtitleCache {

	public static final String TAG = SubtitleCache.class.getSimpleName();
	
	public static void save(String url, InputStream is) {
		Log.d(TAG, "save/url=" + url);
		if (url == null)
			return;
		if (is == null)
			return;
		String subtitle = null;
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(is, Charset.defaultCharset());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (reader != null) {
			BufferedReader bf = new BufferedReader(reader);
			StringBuffer buffer = new StringBuffer();
			String line;
			try {
				while ((line = bf.readLine()) != null) {
					buffer.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			subtitle = buffer.toString();
		}
		if (subtitle != null) {
			CacheManager.save(url, subtitle);
			Log.d(TAG, "save/subtitle=" + subtitle);
		}
	}

	public static String get(String url) {
		Log.d(TAG, "save/url=" + url);
		String subtitle = CacheManager.get(url);
		Log.d(TAG, "save/subtitle=" + subtitle);
		return subtitle;
	}
}
