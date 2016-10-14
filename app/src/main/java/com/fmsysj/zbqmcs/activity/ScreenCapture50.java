package com.fmsysj.zbqmcs.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.fmsysj.zbqmcs.floatview.FloatViewManager;
import com.li.videoapplication.R;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 5.0截屏
 * 
 * @author WYX
 * 
 */
@SuppressLint("NewApi")
public class ScreenCapture50 extends Activity {

    /**
     * 跳转
     */
    public static final synchronized void showNewTask() {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, ScreenCapture50.class);
        context.startActivity(intent);
    }

	private MediaProjectionManager mProjectionManager;
	private Handler mHandler;
    private Handler handler;
	private static MediaProjection MEDIA_PROJECTION;

	private ImageReader mImageReader;
	private static String STORE_DIRECTORY;
	private int mWidth;
	private int mHeight;
	private int resultCode;
	private Intent data;
	private static int IMAGES_PRODUCED;
	private static final int REQUEST_CODE = 100;
	private static final String TAG = ScreenCapture50.class.getSimpleName();
	private Context mContext;

	Bitmap bmp = null;
	File storeDirectory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fm_screenrecord50_activity);
		mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				mHandler = new Handler();
				Looper.loop();
			}
		}.start();

		mContext = ScreenCapture50.this;
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {

                FloatViewManager.getInstance().back();

				MEDIA_PROJECTION = mProjectionManager.getMediaProjection(resultCode, data);

				if (MEDIA_PROJECTION != null) {

					DisplayMetrics metrics = getResources().getDisplayMetrics();
					int density = metrics.densityDpi;
					int flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY
							| DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
					Display display = getWindowManager().getDefaultDisplay();
					Point size = new Point();
					display.getSize(size);
					mWidth = size.x;
					mHeight = size.y;

					mImageReader = ImageReader.newInstance(mWidth, mHeight,
							PixelFormat.RGBA_8888, 2);
					MEDIA_PROJECTION.createVirtualDisplay("screencap", mWidth,
							mHeight, density, flags, mImageReader.getSurface(),
							null, null);

					mImageReader.setOnImageAvailableListener(
							new ImageAvailableListener(), null);
					finish();
					stopProjection();
				}
			};
		};
		startProjection();
	}

	private class ImageAvailableListener implements
			ImageReader.OnImageAvailableListener {
		@Override
		public void onImageAvailable(ImageReader reader) {

			new Thread() {
				Image image = null;
				FileOutputStream fos = null;
				Bitmap bitmap = null;

				public void run() {
					try {
						image = mImageReader.acquireLatestImage();
						if (image != null) {
							ToastHelper.s("截屏成功！");
							Image.Plane[] planes = image.getPlanes();
							ByteBuffer buffer = planes[0].getBuffer();
							// 两个相邻像素之间距离字节单位 4
							int pixelStride = planes[0].getPixelStride();
							// 2944
							int rowStride = planes[0].getRowStride();
							// 64
							int rowPadding = rowStride - pixelStride * mWidth;

							int offset = 0;
							bitmap = Bitmap.createBitmap(mWidth, mHeight,
									Bitmap.Config.ARGB_8888);
							int pixel;
							for (int i = 0; i < mHeight; ++i) {
								for (int j = 0; j < mWidth; ++j) {
									pixel = 0;
									pixel |= (buffer.get(offset) & 0xff) << 16; // R
									pixel |= (buffer.get(offset + 1) & 0xff) << 8; // G
									pixel |= (buffer.get(offset + 2) & 0xff); // B
									pixel |= (buffer.get(offset + 3) & 0xff) << 24; // A
									bitmap.setPixel(j, i, pixel);
									offset += pixelStride;
								}
								offset += rowPadding;
							}
							try {
								fos = new FileOutputStream(SYSJStorageUtil.createPicturePath());
								bitmap.compress(CompressFormat.PNG, 100, fos);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}

							IMAGES_PRODUCED++;
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
							bitmap.recycle();
						}

						if (image != null) {
							image.close();
						}
					}
				};
			}.start();

		}
	}

	private void stopProjection() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (MEDIA_PROJECTION != null)
					MEDIA_PROJECTION.stop();
			}
		});
		onDestroy();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == -1) {
			this.resultCode = resultCode;
			this.data = data;
			moveTaskToBack(true);
			handler.sendEmptyMessageDelayed(0, 1000);
		} else {
            FloatViewManager.getInstance().back();
            ToastHelper.s("截屏失败!");

			finish();
			stopProjection();
		}
	}

	private void startProjection() {
		startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
	}
}
