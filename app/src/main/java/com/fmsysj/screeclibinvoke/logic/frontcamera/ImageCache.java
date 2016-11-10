package com.fmsysj.screeclibinvoke.logic.frontcamera;

import android.graphics.Bitmap;

import java.util.HashMap;

public class ImageCache {

	private static final HashMap<String, Bitmap> MAP = new HashMap<>();

	public static void put(String key, Bitmap bmp) {
		MAP.put(key, bmp);
	}

	public static Bitmap get(String key) {
		return MAP.get(key);
	}

	public static void clear() {
		MAP.clear();
	}
}
