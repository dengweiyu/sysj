package com.li.videoapplication.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.li.videoapplication.framework.AppManager;

import java.util.Map;
import java.util.Set;
/**
 * 功能：系统默认（Android默认的）
 */
public class DefaultPreferences {

    protected final String action = this.getClass().getName();

    protected final String tag = this.getClass().getSimpleName();

    private SharedPreferences sp;

    private DefaultPreferences() {
        super();
        Context context = AppManager.getInstance().getContext();
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static DefaultPreferences instance;

    public static DefaultPreferences getInstance() {
        if (instance == null) {
            synchronized (DefaultPreferences.class) {
                if (instance == null) {
                    instance = new DefaultPreferences();
                }
            }
        }
        return instance;
    }

    public void putString(String key, String value) {
        Log.d(tag, "put/key=" + key);
        Log.d(tag, "put/value=" + value);
        sp.edit().putString(key, value).commit();
    }

    public void putStringSet(String key, Set<String> values) {
        Log.d(tag, "put/key=" + key);
        Log.d(tag, "put/values=" + values);
        sp.edit().putStringSet(key, values).commit();
    }

    public void putInt(String key, int value) {
        Log.d(tag, "put/key=" + key);
        Log.d(tag, "put/value=" + value);
        sp.edit().putInt(key, value).commit();
    }

    public void putLong(String key, long value) {
        Log.d(tag, "put/key=" + key);
        Log.d(tag, "put/value=" + value);
        sp.edit().putLong(key, value).commit();
    }

    public void putFloat(String key, float value) {
        Log.d(tag, "put/key=" + key);
        Log.d(tag, "put/value=" + value);
        sp.edit().putFloat(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        Log.d(tag, "put/key=" + key);
        Log.d(tag, "put/value=" + value);
        sp.edit().putBoolean(key, value).commit();
    }

    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    public String getString(String key, String defValue) {
        Log.d(tag, "get/key=" + key);
        Log.d(tag, "get/defValue=" + defValue);
        return sp.getString(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        Log.d(tag, "get/key=" + key);
        Log.d(tag, "get/defValues=" + defValues);
        return sp.getStringSet(key, defValues);
    }

    public int getInt(String key, int defValue) {
        Log.d(tag, "get/key=" + key);
        Log.d(tag, "get/defValue=" + defValue);
        return sp.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        Log.d(tag, "get/key=" + key);
        Log.d(tag, "get/defValue=" + defValue);
        return sp.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        Log.d(tag, "get/key=" + key);
        Log.d(tag, "get/defValue=" + defValue);
        return sp.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        Log.d(tag, "get/key=" + key);
        Log.d(tag, "get/defValue=" + defValue);
        return sp.getBoolean(key, defValue);
    }

    public void remove(String key) {
        Log.d(tag, "remove/key=" + key);
        sp.edit().remove(key).commit();
    }

    public void clear() {
        Log.d(tag, "remove/clear");
        sp.edit().clear().commit();
    }

    public boolean contains(String key) {
        Log.d(tag, "contains/key=" + key);
        return sp.contains(key);
    }
}
