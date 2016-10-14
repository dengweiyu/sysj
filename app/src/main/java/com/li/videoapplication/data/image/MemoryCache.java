package com.li.videoapplication.data.image;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


import android.graphics.Bitmap;
import android.util.Log;

/**
 * 内存缓存
 */
public class MemoryCache {

	private static final String TAG = MemoryCache.class.getSimpleName();

	// 放入缓存时是个同步操作
	// true:将按照最近使用次数由少到多排列，即LRU,这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
	private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	// 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
	private long size = 0;
	// 缓存只能占用的最大堆内存
	private long limit = 20* 1000* 1000;
	// 单例模式
	private static MemoryCache instance;

	public static MemoryCache getInstance() {
		if (instance == null)
			synchronized (MemoryCache.class) {
				if (instance == null)
					instance = new MemoryCache();
			}
		return instance;
	}

	public MemoryCache() {
		// 用 25% 的堆内存
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}

	public void setLimit(long limit) {
		this.limit = limit;
		Log.i(TAG, "limit=" + this.limit / 1024. / 1024. + "MB");
	}

	public Bitmap get(String id) {
		try {
			if (!cache.containsKey(id))
				return null;
			return cache.get(id);
		} catch (NullPointerException ex) {
			return null;
		}
	}

	public void put(String id, Bitmap bitmap) {
		try {
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	/**
	 * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
	 */
	private void checkSize() {
		if (size > limit) {
			// 先遍历最近最少使用的元素
			Iterator<Entry<String, Bitmap>> iterator = cache.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Bitmap> entry = iterator.next();
				size -= getSizeInBytes(entry.getValue());
				iterator.remove();
				if (size <= limit)
					break;
			}
		}
	}

	/**
	 * 清空图片缓存
	 */
	public void clear() {
		if (cache != null) {
			cache.clear();
		}
	}

	/**
	 * 销毁
	 */
	public void destroy() {
		if (cache != null) {
			cache.clear();
			cache = null;
		}
		instance = null;
	}

	/**
	 * 图片占用的内存
	 */
	long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}