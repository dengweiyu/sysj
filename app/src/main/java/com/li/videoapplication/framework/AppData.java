package com.li.videoapplication.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：储存应用的数据
 *
 */
public class AppData {

    protected final String action = this.getClass().getName();

    protected final String tag = this.getClass().getSimpleName();

	private AppData() {
		super();
	}

	private static AppData instance;

	/* 单一实例 */
	public static AppData getInstance() {
		if (null == instance) {
			instance = new AppData();
		}
		return instance;
	}

    /**
     * 集合
     */
	private Map<String, Object> map;

	private Map<String, Object> getMap() {
		if (map == null) {
			map = new HashMap<>();
		}
		return map;
	}

	public boolean put(String key, Object value) {
		getMap().put(key, value);
		return true;
	}

	public boolean remove(String key) {
		getMap().remove(key);
		return true;
	}

	public boolean containsKey(String key) {
		return getMap().containsKey(key);
	}

	public boolean containsValue(Object value) {
		return getMap().containsValue(value);
	}
}
