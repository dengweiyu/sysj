package com.li.videoapplication.data.cache;

import java.io.Serializable;

public class CacheItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 存储的键
	 */
	private final String key;

	/**
	 * 字符串
	 */
	private String data;

	public CacheItem(final String key, final String data) {
		this.key = key;
		this.data = data;
	}

	public String getKey() {
		return key;
	}

	public String getData() {
		return data;
	}
}