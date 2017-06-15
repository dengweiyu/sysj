package com.li.videoapplication.tools;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.li.videoapplication.utils.UUIDUtil;

import java.io.File;
/**
 * 功能：相册，照相
 *
 */
public class PhotoHelper {
	
	public static final String TAG = PhotoHelper.class.getSimpleName();

	public static final int REQUESTCODE_PHOTO_TAKE = 11;

	public static final int REQUESTCODE_PHOTO_PICK = 22;

	private String path;

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
	/**
	 * 功能：照相
	 */
	public void takePhoto(Activity activity) {

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
			File destDir = new File(dir);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			File file = new File(dir, "photo" + UUIDUtil.getUUID() + ".jpg");
			String path = file.getAbsolutePath();
			Log.i(TAG, "path=" + path);
			setPath(path);
			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//保存到指定地址
			try {
				activity.startActivityForResult(intent, REQUESTCODE_PHOTO_TAKE);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
				ToastHelper.s("摄像头尚未准备好");
			}
		} else {
			ToastHelper.s("SDCard尚未准备好");
			return;
		}
	}

	/**
	 * 功能：相册
	 */
	public void pickPhoto(Activity activity) {
		
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			Intent intent;
			if (Build.VERSION.SDK_INT < 19) {
				intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
			} else {
				intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			}
			try {
				activity.startActivityForResult(intent, REQUESTCODE_PHOTO_PICK);
			} catch (Exception e) {
				e.printStackTrace();
				ToastHelper.s("无法打开相册");
			}
		} else {
			ToastHelper.s("暂无外部存储");
			return;
		}
	}
}
