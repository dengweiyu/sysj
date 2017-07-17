package com.ifeimo.screenrecordlib.record.record50;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.ifeimo.screenrecordlib.RecordingManager;
import com.ifeimo.screenrecordlib.util.BackgroundStartActivityUtil;
import com.ifeimo.screenrecordlib.util.TaskUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 活动：5.0截屏（优化图片处理）b
 */
@SuppressLint("NewApi")
@TargetApi(21)
public class ScreenCaptureActivity extends Activity {
	private static final String TAG = ScreenCaptureActivity.class.getSimpleName();

	private static BackgroundStartActivityUtil util;

	public static void startCapture(final Context context, String path) {
		util = new BackgroundStartActivityUtil(new BackgroundStartActivityUtil.ActivityCreateListener() {
			@Override
			public void createSuccess() {

			}

			@Override
			public void createFail() {
				Toast.makeText(context, "你的手机还没有开启后台弹出界面权限，开启权限后才能正常使用该功能",
						Toast.LENGTH_LONG).show();
			}
		});

		Intent intent = new Intent();
		intent.setClass(context, ScreenCaptureActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK );
		intent.putExtra("path", path);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	private String path;
	private MediaProjectionManager manager;

	private static final int REQUEST_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (util != null)
			util.init();

		try {
			path = getIntent().getStringExtra("path");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (path == null) {
			TaskUtil.clearTaskAndAffinity(this);
		}

		manager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
		startCapture();
	}

	@Override
	protected void onDestroy() {
		if (handler != null)
			handler.removeCallbacksAndMessages(null);
		util = null;
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(TAG, "onConfigurationChanged: newConfig=" + newConfig);
	}

	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------

	private void startCapture() {
		Intent intent = manager.createScreenCaptureIntent();
		try {
			startActivityForResult(intent, REQUEST_CODE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stopCapture() {
		/*
		if (virtualDisplay != null) {
			virtualDisplay.release();
		}
		if (imageReader != null) {
			imageReader.close();
		}*/
		if (projection != null) {
			projection.stop();
		}
	}

	private Handler handler = new Handler(Looper.getMainLooper());
	private MediaProjection projection;
	private ImageReader imageReader;
	private VirtualDisplay virtualDisplay;
	private int width;
	private int height;

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		Log.d(TAG, "onActivityResult: requestCode=" + requestCode);
		Log.d(TAG, "onActivityResult: resultCode=" + resultCode);
		Log.d(TAG, "onActivityResult: data=" + data);
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

			moveTaskToBack(true);

			new Thread(new Runnable() {
				@Override
				public void run() {

					Looper.prepare();

					projection = manager.getMediaProjection(resultCode, data);

					DisplayMetrics metrics = getResources().getDisplayMetrics();
					int density = metrics.densityDpi;
					int flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY |
							DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
					Display display = getWindowManager().getDefaultDisplay();
					Point size = new Point();
					display.getSize(size);
					width = size.x;
					height = size.y;

					imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
					imageReader.setOnImageAvailableListener(onImageAvailableListener, null);
					virtualDisplay = projection.createVirtualDisplay("screencap",
							width,
							height,
							density,
							flags,
							imageReader.getSurface(),
							null,
							null);

					handler.post(new Runnable() {
						@Override
						public void run() {

							stopCapture();
						}
					});

					Looper.loop();
				}
			}, "newThread-ScreenCaptureActivity").start();

			TaskUtil.clearTaskAndAffinity(this);
		} else {
			// 截屏失败!
			stopCapture();
			TaskUtil.clearTaskAndAffinity(this);
		}
	}

	private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {

		private Image image;
		private FileOutputStream fos;
		private Bitmap bitmap, targetbitmap;

		@Override
		public void onImageAvailable(ImageReader reader) {
			long startMills = System.currentTimeMillis();
			try {
				image = imageReader.acquireLatestImage();
				if (image != null) {
					Image.Plane[] planes = image.getPlanes();
					ByteBuffer buffer = planes[0].getBuffer();
					int pixelStride = planes[0].getPixelStride();
					int rowStride = planes[0].getRowStride();
					int rowPadding = rowStride - pixelStride * width;

					bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
					bitmap.copyPixelsFromBuffer(buffer);
					targetbitmap = Bitmap.createBitmap(bitmap, 0, 0,width, height);

					long middleMills = System.currentTimeMillis();
					fos = new FileOutputStream(path);
					targetbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					long endMills = System.currentTimeMillis();

					// Log.d(TAG, "onImageAvailable: startMills=" + startMills);
					// Log.d(TAG, "onImageAvailable: middleMills=" + middleMills);
					// Log.d(TAG, "onImageAvailable: endMills=" + endMills);
					Log.d(TAG, "onImageAvailable: difTime=" + (middleMills - startMills));
					Log.d(TAG, "saveBitmpToStorage: difTime=" + (endMills - middleMills));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
				if (bitmap != null) {
					try {
						bitmap.recycle();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (targetbitmap != null) {
					try {
						targetbitmap.recycle();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
						image != null) {
					try {
						image.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				handler.post(new Runnable() {
					@Override
					public void run() {

						stopCapture();
						TaskUtil.clearTaskAndAffinity(ScreenCaptureActivity.this);
					}
				});

				RecordingManager.getInstance().onCapturingCompleted();

				try {
					Looper.myLooper().quit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
}
