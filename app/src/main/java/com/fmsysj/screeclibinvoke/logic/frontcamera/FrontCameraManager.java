package com.fmsysj.screeclibinvoke.logic.frontcamera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.li.videoapplication.framework.AppManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 前置摄像头管理
 */
public class FrontCameraManager implements FrontCameraView.OnScreenRotation {

	public static final String TAG = FrontCameraManager.class.getSimpleName();

	private int HORIZONTAL = LinearLayout.HORIZONTAL;
	private int VERTICAL = LinearLayout.VERTICAL;

	private AtomicInteger orientation= new AtomicInteger();

	private static FrontCameraManager manager;

	public static FrontCameraManager getInstance() {
		if (manager == null) {
			synchronized (FrontCameraManager.class) {
				manager = new FrontCameraManager();
			}
		}
		return manager;
	}

	public boolean isOpen() {
		return isOpen.get();
	}

	private AtomicBoolean isOpen = new AtomicBoolean(false);
	private WindowManager windowManager;
	private Context context;
	private LayoutParams paramsContent;
	private FrontCameraView cameraView;

	// 屏幕尺寸
	private int displayWidth;
	private int displayHeight;

	private FrontCameraManager() {
		super();
		Log.d(TAG, "FrontFrontCameraManager: // -------------------------------------------------------------");
		context = AppManager.getInstance().getContext();
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		displayWidth = windowManager.getDefaultDisplay().getWidth();
		displayHeight = windowManager.getDefaultDisplay().getHeight();

		if (displayHeight > displayWidth){
			orientation.set(LinearLayout.VERTICAL);
		}else {
			orientation.set(LinearLayout.HORIZONTAL);
		}
	}

	private void printParams() {
		if (paramsContent != null) {
			Log.d(TAG, "printParams: x=" + paramsContent.x);
			Log.d(TAG, "printParams: y=" + paramsContent.y);
		}
		Log.d(TAG, "printParams: displayWidth=" + displayWidth);
		Log.d(TAG, "printParams: displayHeight=" + displayHeight);
	}

	/**
	 * 切换横屏时
	 */
	public void toLandscape() {
		Log.d(TAG, "toLandscape: // -------------------------------------------------------------");
		printParams();

		int tempHeight = displayHeight;
		int tempWidth = displayWidth;
		displayWidth = windowManager.getDefaultDisplay().getWidth();
		displayHeight = windowManager.getDefaultDisplay().getHeight();
		if(tempWidth < tempHeight && isOpen()){
			orientation.set(HORIZONTAL);
			dismiss();
			cameraView = null;
			show();
		}
		// updateCurrentView();
		printParams();
	}

	/**
	 * 切换竖屏时
	 */
	public void toPortrait() {
		Log.d(TAG, "toPortrait: // -------------------------------------------------------------");
		printParams();

		int tempHeight = displayHeight;
		int tempWidth = displayWidth;

		displayWidth = windowManager.getDefaultDisplay().getWidth();
		displayHeight = windowManager.getDefaultDisplay().getHeight();
		if(tempWidth > tempHeight && isOpen()){
			orientation.set(VERTICAL);
			dismiss();
			cameraView = null;
			show();
		}
		// updateCurrentView();
		printParams();

	}

	/**
	 * 更新默认窗口
	 */
	public void updateCurrentView() {
		Log.d(TAG, "updateCurrentView: // -------------------------------------------------------------");
		printParams();
		if (cameraView != null) {
			windowManager.updateViewLayout(cameraView, paramsContent);
		}
	}

	/**
	 * 显示画中画界
	 */
	public void show() {
		Log.d(TAG, "show: // -------------------------------------------------------------");
		if (getContentView() != null &&
				getContentView().getParent() == null) {
			cameraView.measureScreen();
			windowManager.addView(getContentView(), paramsContent);
			isOpen.set(true);
			printParams();
		}
	}

	/**
	 * 获取画中画界面
	 */
	public FrontCameraView getContentView() {
		Log.d(TAG, "getContentView: // -------------------------------------------------------------");
		if (cameraView == null) {
			cameraView = new FrontCameraView(this);
		}
		if (paramsContent == null) {
			paramsContent = new LayoutParams();

			paramsContent.type = LayoutParams.TYPE_TOAST;
			paramsContent.format = PixelFormat.RGBA_8888;
			paramsContent.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL |
					LayoutParams.FLAG_NOT_FOCUSABLE;
			paramsContent.gravity = Gravity.LEFT |
					Gravity.TOP;


			paramsContent.x = (displayWidth - cameraView.mWidth);
			paramsContent.y = 0;
		}
		paramsContent.width = cameraView.mWidth;
		paramsContent.height = cameraView.mHeight;

		printParams();
		return cameraView;
	}

	/**
	 * 移动
	 */
	public void move(View view, int delatX, int deltaY) {
		Log.d(TAG, "move: // -------------------------------------------------------------");
		Log.d(TAG, "move: delatX=" + delatX);
		Log.d(TAG, "move: deltaY=" + deltaY);
		if (view == cameraView) {
			paramsContent.x += delatX;
			paramsContent.y += deltaY;
			windowManager.updateViewLayout(view, paramsContent);
			printParams();
		}
	}

	/**
	 * 关闭
	 */
	public void dismiss() {
		Log.d(TAG, "dismiss: // -------------------------------------------------------------");
		printParams();
		if (cameraView != null) {
			windowManager.removeView(cameraView);
			cameraView.close();
			cameraView = null;
			isOpen.set(false);
		}
	}

	/**
	 * 更新界面
	 */
	public boolean isUpdate() {
		Log.d(TAG, "isUpdate: // -------------------------------------------------------------");
		if (cameraView == null) {
			return false;
		}
		return true;
	}

	@Override
	public int getRotation() {
		return windowManager.getDefaultDisplay().getRotation();
	}

	@Override
	public int getHeight() {
		return displayHeight;
	}

	@Override
	public int getWidth() {
		return displayWidth;
	}

	@Override
	public WindowManager getWindowManager() {
		return windowManager;
	}

	@Override
	public int getOrientation() {
		return orientation.get();
	}
}
