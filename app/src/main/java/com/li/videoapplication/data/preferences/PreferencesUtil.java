package com.li.videoapplication.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 功能：保存数据柑工具类
 */
public class PreferencesUtil {

    protected final String action = this.getClass().getName();

    protected final String tag = this.getClass().getSimpleName();
	
	private SharedPreferences sp;
	
	public PreferencesUtil(Context c, String name) {
		sp = c.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	/**
	 * 功能：保存String
	 */
	public void saveString(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	/**
	 * 功能：取出String
	 */
	public String getString(String key, String... defValue) {
		if (defValue.length > 0)
			return sp.getString(key, defValue[0]);
		else
			return sp.getString(key, "");
	}

	/**
	 * 功能：保存int
	 */
	public void saveInt(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	/**
	 * 功能：取出int
	 */
	public int getInt(String key, int defValue) {
		return sp.getInt(key, defValue);

	}

	/**
	 * 功能：保存boolean
	 */
	public void saveBoolean(String key, Boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}

	/**
	 * 功能：取出boolean
	 */
	public Boolean getBoolean(String key, Boolean... defValue) {
		if (defValue.length > 0)
			return sp.getBoolean(key, defValue[0]);
		else
			return sp.getBoolean(key, false);
	}

	/**
	 * 功能：保存long
	 */
	public void saveLong(String key, long value) {
		sp.edit().putLong(key, value).commit();
	}

	/**
	 * 功能：取出long
	 */
	public long getLong(String key, long defValue) {
		return sp.getLong(key, defValue);

	}

	/**
	 * 功能：移除数据
	 */
	public boolean remove(String key) {
		return sp.edit().remove(key).commit();
	}

	/**
	 * 功能：清除所有数据
	 */
	public boolean clear() {
		return sp.edit().clear().commit();
	}
}
