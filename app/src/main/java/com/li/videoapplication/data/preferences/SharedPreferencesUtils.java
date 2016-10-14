package com.li.videoapplication.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtils {

	public static SharedPreferences getMinJieKaiFaPreferences(Context context) {
		return context.getSharedPreferences("com_fmscreenrecord.properties", Context.MODE_PRIVATE);
	}

	public static void setPreference(Context context, String key, String value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
		editor = null;
	}

	public static String getPreference(Context context, String key) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String value = preferences.getString(key, "");
		return value;
	}
	public static boolean getPreferenceBoolean(Context context, String key) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean value = preferences.getBoolean(key, false);
		return value;
	}
	public static boolean getPreferenceboolean(Context context, String key) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean value = preferences.getBoolean(key, false);
		return value;
	}
}