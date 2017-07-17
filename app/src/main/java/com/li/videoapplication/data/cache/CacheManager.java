package com.li.videoapplication.data.cache;

import android.util.Log;

import com.li.videoapplication.data.local.SYSJStorageUtil;

import java.io.File;

/**
 * 功能：缓存管理器
 */
public class CacheManager {

	protected final String tag = this.getClass().getSimpleName();
	protected final String action = this.getClass().getName();

	/** SDcard 最小空间，如果小于10M，不会再向里面写入任何数据 */
	public static final long SDCARD_MINSIZE = 1024 * 1024 * 10;

	private static CacheManager instance;

	public static CacheManager getInstance() {
		if (instance == null) {
			synchronized (CacheManager.class) {
				if (instance == null) {
					instance = new CacheManager();
				}
			}
		}
		return instance;
	}

	private CacheManager() {
		super();
	}

	public static void save(final String key, final String data) {
		CacheManager.getInstance().putFileCache(key, data);
	}

	public static String get(final String key) {
		return CacheManager.getInstance().getFileCache(key);
	}

	/**
	 * 从文件缓存中取出缓存
	 */
	public String getFileCache(final String key) {
		Log.i(tag, "key=" + key);
		String md5Key = BaseUtils.getMd5(key);
		Log.i(tag, "md5Key=" + md5Key);
		if (contains(md5Key)) {
			final CacheItem item = getFromCache(md5Key);
			if (item != null) {
				Log.i(tag, "data=" + item.getData());
				return item.getData();
			}
		}
		return null;
	}

	/**
	 * 缓存到文件
	 */
	public void putFileCache(final String key, final String data) {
		Log.i(tag, "key=" + key);
		String md5Key = BaseUtils.getMd5(key);
		Log.i(tag, "md5Key=" + md5Key);
		Log.i(tag, "data=" + data);
		final CacheItem item = new CacheItem(md5Key, data);
		putToCache(item);
	}


	/**
	 * 查询是否有对应的缓存文件
	 */
	public boolean contains(final String key) {
		final File file = SYSJStorageUtil.createCachePath(key);
		return file.exists();
	}

	/**
	 * 将CacheItem从磁盘读取出来
	 */
	public synchronized CacheItem getFromCache(final String key) {
		CacheItem item = null;
		File file = SYSJStorageUtil.createCachePath(key);
		Object o = BaseUtils.restoreObject(file.getPath());
		if (o != null) {
			item = (CacheItem) o;
		}
		return item;
	}

	/**
	 * 将CacheItem缓存到磁盘
	 *
	 * @return 是否缓存，True：缓存成功，False：不能缓存
	 */
	public synchronized boolean putToCache(final CacheItem item) {
		if (BaseUtils.getSdcardSize() > SDCARD_MINSIZE) {
			File file = SYSJStorageUtil.createCachePath(item.getKey());
			BaseUtils.saveObject(file.getPath(), item);
			return true;
		}
		return false;
	}

	/**
	 * sdcard已经挂载并且空间不小于10M，可以写入文件;小于10M时，清除缓存
	 */
	public void initCache() {
		if (BaseUtils.sdcardMounted()) {
			if (BaseUtils.getSdcardSize() < SDCARD_MINSIZE) {
				clearAllData();
			}
		}
	}

	/**
	 * 清除缓存文件
	 */
	public void clearAllData() {
		File file;
		File[] files;
		if (BaseUtils.sdcardMounted()) {
			file = SYSJStorageUtil.getSysjCache();
			if (file != null) {
				files = file.listFiles();
				if (files != null) {
					for (final File f : files) {
						f.delete();
					}
				}
			}
		}
	}
}
