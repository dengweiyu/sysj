package com.li.videoapplication.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.li.videoapplication.R;
import com.li.videoapplication.data.network.LightTask;
import com.li.videoapplication.data.qrcode.camera.CameraManager;
import com.li.videoapplication.data.qrcode.decode.DecodeThread;
import com.li.videoapplication.data.qrcode.utils.BeepManager;
import com.li.videoapplication.data.qrcode.utils.CaptureActivityHandler;
import com.li.videoapplication.data.qrcode.utils.InactivityTimer;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.PhotoHelper;
import com.li.videoapplication.utils.URLUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Hashtable;

/**
 * 活动：二维码扫描
 */
public class ScanQRCodeActivity extends TBaseActivity implements OnClickListener, SurfaceHolder.Callback {

	private static final String TAG = ScanQRCodeActivity.class.getSimpleName();

	private PhotoHelper photoHelper = new PhotoHelper();

	private LinearLayout photo, light;
	private ImageView lightIcon;
	private TextView lightText;

	private CameraManager cameraManager;
	private CaptureActivityHandler captureActivityHandler;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;

	private SurfaceView scanPreview;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;

	@Override
	public int getContentView() {
		return R.layout.activity_scanqrcode;
	}

	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void beforeOnCreate() {
		super.beforeOnCreate();

		setSystemBar(false);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// 		WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();

		actionBar.hide();
	}

	@Override
	public void initView() {

		photo = (LinearLayout) findViewById(R.id.scanqrcode_photo);
		light = (LinearLayout) findViewById(R.id.scanqrcode_light);
		lightText = (TextView) findViewById(R.id.scanqrcode_light_text);
		lightIcon = (ImageView) findViewById(R.id.scanqrcode_light_icon);

		photo.setOnClickListener(this);
		light.setOnClickListener(this);

		scanPreview = (SurfaceView) findViewById(R.id.surfaceview);
		scanContainer = (RelativeLayout) findViewById(R.id.scanqrcode_container);
		scanCropView = (RelativeLayout) findViewById(R.id.scanqrcode_center);
		scanLine = (ImageView) findViewById(R.id.scanqrcode_line);
	}

	@Override
	public void loadData() {

		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f);
		animation.setDuration(4500);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		scanLine.startAnimation(animation);
	}

	@Override
	public void onResume() {
		super.onResume();
		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to startActivityVideoActivity the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());
		captureActivityHandler = null;
		if (isHasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(scanPreview.getHolder());
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			scanPreview.getHolder().addCallback(this);
		}
		inactivityTimer.onResume();
	}

	@Override
	public void onPause() {
		if (captureActivityHandler != null) {
			captureActivityHandler.quitSynchronously();
			captureActivityHandler = null;
		}
		inactivityTimer.onPause();
		beepManager.close();
		cameraManager.closeDriver();
		if (!isHasSurface) {
			scanPreview.getHolder().removeCallback(this);
		}
		if (cameraManager != null
				&& lightText.getText().equals(resources.getString(R.string.scanqrcode_light_off))) {
			cameraManager.turnLightOff();
			lightText.setText(R.string.scanqrcode_light_on);
			lightIcon.setImageResource(R.drawable.scanqrcode_light_transparent);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.scanqrcode_photo:
			photoHelper.pickPhoto(this);
			break;

		case R.id.scanqrcode_light:
			toogleLight();
			break;
		}
	}

	private void toogleLight() {
		if (cameraManager != null) {
			if (lightText.getText().equals(resources.getString(R.string.scanqrcode_light_on))) {
				cameraManager.turnLightOn();
				lightText.setText(R.string.scanqrcode_light_off);
				lightIcon.setImageResource(R.drawable.scanqrcode_light_white);
			} else {
				cameraManager.turnLightOff();
				lightText.setText(R.string.scanqrcode_light_on);
				lightIcon.setImageResource(R.drawable.scanqrcode_light_transparent);
			}
		}
	}

	private Rect mCropRect;
	private boolean isHasSurface = false;

	public Handler getHandler() {
		return captureActivityHandler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!isHasSurface) {
			isHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isHasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	/**
	 *
	 * A valid barcode has been found, so give an indication of success and startActivityVideoActivity
	 * the results.
	 *
	 * @param rawResult The contents of the barcode.
	 * @param bundle    The extras
	 */
	public void handleDecode(Result rawResult, Bundle bundle) {
		inactivityTimer.onActivity();
		beepManager.playBeepSoundAndVibrate();

		int width = mCropRect.width();
		int height = mCropRect.height();
		Log.i(tag, "width=" + width);
		Log.i(tag, "height=" + height);

		String result = rawResult.getText();
		Log.i(tag, "result=" + result);

		if (URLUtil.isURL(result)) {
			//WebActivity.startWebActivity(this, result);
			Uri uri = Uri.parse(result);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			finish();
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (captureActivityHandler == null) {
				captureActivityHandler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
			}
			initCrop();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		// camera error
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage("Camera error");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (captureActivityHandler != null) {
			captureActivityHandler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	public Rect getCropRect() {
		return mCropRect;
	}

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = cameraManager.getCameraResolution().y;
		int cameraHeight = cameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 回调：相册
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(tag, "requestCode=" + requestCode + "/resultCode=" + resultCode + "/data=" + data);

		if (resultCode == Activity.RESULT_CANCELED) {
			return;
		}
		if (requestCode == PhotoHelper.REQUESTCODE_PHOTO_TAKE) {
			String path = photoHelper.getPath();
			if (path != null)
				updatePhoto(path);
		}
		if (requestCode == PhotoHelper.REQUESTCODE_PHOTO_PICK) {
			if (data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					Cursor cursor = getContentResolver().query(uri, null, null, null, null);
					if (null == cursor || !cursor.moveToFirst()) {
						showToastShort("没有找到您想要的图片");
						return;
					}
					int columnIndex = cursor.getColumnIndex("_data");
					String path = cursor.getString(columnIndex);
					if (path != null) {
						File file = new File(path);
						if (file == null || !file.exists()) {
							showToastShort("没有找到您想要的图片");
							return;
						}
					}
					cursor.close();
					updatePhoto(path);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updatePhoto(final String originPath) {

		LightTask.post(new Runnable() {

			private String msg;

			@Override
			public void run() {
				Result result = scanningImage(originPath);
				Log.i(tag, "result=" + result);
				if (result == null) {
					msg = "该图片识别不出二维码";
					showToast();
				} else {
					// 数据返回
					String str = recode(result.toString());
					Log.i(tag, "result=" + str);

					if (URLUtil.isURL(str)) {
						Uri uri = Uri.parse(str);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
						finish();
					}
				}
			}

			private void showToast() {
				if(msg == null)
					return;
				post(new Runnable() {
					@Override
					public void run() {
						showToastShort(msg);
					}
				});
			}
		});
	}

	private Bitmap bitmap;

	protected Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		bitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		bitmap = BitmapFactory.decodeFile(path, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int[] pixels = new int[w * h];
		bitmap.getPixels(pixels, 0, w, 0, 0, w, h);

		Hashtable<DecodeHintType, String> hints = new Hashtable<>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

		RGBLuminanceSource source = new RGBLuminanceSource(w, h, pixels);
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(binaryBitmap, hints);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String recode(String result) {
		String formart = "";
		try {
			boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
					.canEncode(result);
			if (ISO) {
				formart = new String(result.getBytes("ISO-8859-1"), "GB2312");
				Log.i("1234      ISO8859-1", formart);
			} else {
				formart = result;
				Log.i("1234      stringExtra", result);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return formart;
	}
}
