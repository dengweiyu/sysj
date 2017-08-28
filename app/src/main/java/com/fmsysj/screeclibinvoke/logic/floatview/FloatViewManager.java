package com.fmsysj.screeclibinvoke.logic.floatview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;


/**
 * 浮窗管理
 */

public class FloatViewManager {


	public static final String TAG = FloatViewManager.class.getSimpleName();

	private static FloatViewManager instance;

	public static FloatViewManager getInstance() {
		if (instance == null) {
			synchronized (FloatViewManager.class) {
				instance = new FloatViewManager();
			}
		}
		return instance;
	}

	/**
	 * 销毁
	 */
	public static void destruction() {
		if (instance != null)
			instance.destroyView();
		instance = null;
	}

	// -------------------------------------------------------------------------------------

	private WindowManager windowManager;
	private Context context;
	private LayoutParams params;
	private LayoutParams paramsContent;

	private View currentView;
	private FloatView floatView;
	private FloatView2 floatView2;
	private FloatContentView floatContentView;
	private FloatContentView2 floatContentView2;

	public volatile boolean isFloatingWindiws = true;

	private void printParams() {
		if (params != null) {
			Log.d(TAG, "printParams: x=" + params.x);
			Log.d(TAG, "printParams: y=" + params.y);
		}
		Log.d(TAG, "printParams: moveX=" + moveX);
		Log.d(TAG, "printParams: moveY=" + moveY);
		Log.d(TAG, "printParams: displayWidth=" + displayWidth);
		Log.d(TAG, "printParams: displayHeight=" + displayHeight);
	}

	/**
	 * 切换横屏时浮窗位置
	 */
	public void toLandscape() {
		Log.d(TAG, "toLandscape: // -------------------------------------------------------------");
		printParams();

		displayWidth = windowManager.getDefaultDisplay().getWidth();
		displayHeight = windowManager.getDefaultDisplay().getHeight();

//		params.x = params.y;
//		params.y = displayWidth - params.x;

		if (params != null) {
			moveX = params.x;
			moveY = params.y;
		}

		updateCurrentView();
		printParams();
	}

	/**
	 * 切换竖屏时浮窗位置
	 */
	public void toPortrait() {
		Log.d(TAG, "toPortrait: // -------------------------------------------------------------");
		printParams();

		displayWidth = windowManager.getDefaultDisplay().getWidth();
		displayHeight = windowManager.getDefaultDisplay().getHeight();

//		params.x = displayWidth - params.y;
//		params.y = params.x;

		if (params != null) {
			moveX = params.x;
			moveY = params.y;
		}

		updateCurrentView();
		printParams();
	}

	/**
	 * 更新默认窗口
	 */
	public void updateCurrentView() {
		Log.d(TAG, "updateCurrentView: // -------------------------------------------------------------");
		printParams();
		if (currentView != null) {
			if (currentView == floatView ||
					currentView == floatView2) {
				windowManager.updateViewLayout(currentView, params);
			}
		}
	}

	/**
	 * 是否横屏
	 */
	public boolean isLandscape = false;

	public boolean isShow() {
		if (currentView != null)
			return true;
		return false;
	}

	public int displayWidth;
	public int displayHeight;
	public int floatWidth;
	public int moveX = -1;
	public int moveY = -1;

	private FloatViewManager() {
		super();
		Log.d(TAG, "FloatViewManager: // -------------------------------------------------------------");
		context = AppManager.getInstance().getContext();
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		displayWidth = windowManager.getDefaultDisplay().getWidth();
		displayHeight = windowManager.getDefaultDisplay().getHeight();

		isFloatingWindiws = PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws();

		printParams();
	}

	/**
	 * 浮窗1
	 */
	private FloatView getView() {
		if (floatView == null) {
			Log.d(TAG, "getView: // -------------------------------------------------------------");
			floatView = new FloatView();
		}
		if (params == null) {
			params = new LayoutParams();
			// TYPE_PHONE
			// TYPE_SYSTEM_ALERT
			// TYPE_TOAST

			// 4.3以下版本不能用TYPE_TOAST作为悬浮窗属性
			if (Build.VERSION.SDK_INT <= 18 ||Build.VERSION.SDK_INT >= 23){
				params.type = LayoutParams.TYPE_SYSTEM_ALERT;
			} else {
				params.type = LayoutParams.TYPE_TOAST;
			}
			params.format = PixelFormat.RGBA_8888;
			params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
			params.gravity = Gravity.LEFT | Gravity.TOP;
			params.width = floatView.width;
			params.height = floatView.height;
			if (isLandscape) {// 横屏
				if (moveX != -1 && moveY != -1) {
					params.x =  moveY;
					params.y =  moveX;
				} else {
					params.x = displayHeight - floatView.width;
					params.y = displayWidth / 2;
				}
			} else {// 竖屏
				if (moveX != -1 && moveY != -1) {
					params.x = moveX;
					params.y = moveY;
				} else {
					params.x = displayWidth - floatView.width;
					params.y = displayHeight / 2;
				}
			}
		}
		printParams();
		return floatView;
	}

	/**
	 * 浮窗2
	 */
	private FloatView2 getView2() {
		if (floatView2 == null) {
			Log.d(TAG, "getView2: // -------------------------------------------------------------");
			floatView2 = new FloatView2();
		}
		if (params == null) {
			params = new LayoutParams();
			// 4.3以下版本不能用TYPE_TOAST作为悬浮窗属性
			if (Build.VERSION.SDK_INT <= 18 || Build.VERSION.SDK_INT >= 23) {
				params.type = LayoutParams.TYPE_SYSTEM_ALERT;
			} else {
				params.type = LayoutParams.TYPE_TOAST;
			}
			params.format = PixelFormat.RGBA_8888;
			params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
			params.gravity = Gravity.LEFT | Gravity.TOP;
			params.width = floatView2.width;
			params.height = floatView2.height;
			if (isLandscape) {// 横屏
				if (moveX != -1 && moveY != -1) {
					params.x =  moveY;
					params.y =  moveX;
				} else {
					params.x = displayHeight - floatView2.width;
					params.y = displayWidth / 2;
				}
			} else {// 竖屏
				if (moveX != -1 && moveY != -1) {
					params.x = moveX;
					params.y = moveY;
				} else {
					params.x = displayWidth - floatView2.width;
					params.y = displayHeight / 2;
				}
			}
		}
		printParams();
		return floatView2;
	}

	/**
	 * 浮窗详情1
	 */
	private FloatContentView getContentView() {
		if (floatContentView == null) {
			Log.d(TAG, "getContentView: // -------------------------------------------------------------");
			floatContentView = new FloatContentView();
		}
		if (paramsContent == null) {
			paramsContent = new LayoutParams();
			// 4.3以下版本不能用TYPE_TOAST作为悬浮窗属性
			if (Build.VERSION.SDK_INT <= 18 || Build.VERSION.SDK_INT >= 23) {
				paramsContent.type = LayoutParams.TYPE_SYSTEM_ALERT;
			} else {
				paramsContent.type = LayoutParams.TYPE_TOAST;
			}
			paramsContent.format = PixelFormat.RGBA_8888;
			paramsContent.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
			paramsContent.gravity = Gravity.CENTER;
			paramsContent.width = ViewGroup.LayoutParams.MATCH_PARENT;
			paramsContent.height = ViewGroup.LayoutParams.MATCH_PARENT;
		}
		return floatContentView;
	}

	/**
	 * 浮窗详情2
	 */
	private FloatContentView2 getContentView2() {
		if (floatContentView2 == null) {
			Log.d(TAG, "getContentView2: // -------------------------------------------------------------");
			floatContentView2 = new FloatContentView2();
		}
		if (paramsContent == null) {
			paramsContent = new LayoutParams();
			// 4.3以下版本不能用TYPE_TOAST作为悬浮窗属性
			if (Build.VERSION.SDK_INT <= 18 || Build.VERSION.SDK_INT >= 23) {
				paramsContent.type = LayoutParams.TYPE_SYSTEM_ALERT;
			} else {
				paramsContent.type = LayoutParams.TYPE_TOAST;
			}
			paramsContent.format = PixelFormat.RGBA_8888;
			paramsContent.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
			paramsContent.gravity = Gravity.CENTER;
			paramsContent.width = ViewGroup.LayoutParams.MATCH_PARENT;
			paramsContent.height = ViewGroup.LayoutParams.MATCH_PARENT;
		}
		printParams();
		return floatContentView2;
	}

	/**
	 * 展开浮窗1
	 */
	public synchronized void showView() {
		Log.d(TAG, "showView: // -------------------------------------------------------------");
		printParams();
		if (currentView != null &&
				floatView != null &&
				currentView == floatView)
			return;
		closeAnimation(new Animationable() {
			@Override
			public void animate() {
				Log.d(TAG, "animate: thread=" + Thread.currentThread());
				removeCurrentView();
				if (getView() != null && getView().getParent() == null) {
					toogleView(isFloatingWindiws);
					windowManager.addView(getView(), params);
					currentView = getView();
				}
			}
		});
	}

	/**
	 * 展开浮窗2
	 */
	public synchronized void showView2() {
		Log.d(TAG, "showView2: // -------------------------------------------------------------");
		printParams();
		if (currentView != null &&
				floatView2 != null &&
				currentView == floatView2)
			return;
		closeAnimation(new Animationable() {
			@Override
			public void animate() {
				Log.d(TAG, "animate: thread=" + Thread.currentThread());
				removeCurrentView();
				if (getView2() != null && getView2().getParent() == null) {
					toogleView(isFloatingWindiws);
					windowManager.addView(getView2(), params);
					currentView = getView2();
				}
			}
		});
	}

	/**
	 * 展开浮窗详情1
	 */
	public void showContentView() {
		Log.d(TAG, "showContentView: // -------------------------------------------------------------");
		if (currentView != null &&
				floatContentView != null &&
				currentView == floatContentView)
			return;
		closeAnimation(new Animationable() {
			@Override
			public void animate() {
				Log.d(TAG, "animate: thread=" + Thread.currentThread());
				removeCurrentView();
				if (getContentView() != null && getContentView().getParent() == null) {
					windowManager.addView(getContentView(), paramsContent);
					currentView = getContentView();
					openAnimation();
				}
			}
		});
	}

	/**
	 * 展开浮窗详情2
	 */
	public void showContentView2() {
		Log.d(TAG, "showContentView2: // -------------------------------------------------------------");
		printParams();
		if (currentView != null &&
				floatContentView2 != null &&
				currentView == floatContentView2)
			return;
		closeAnimation(new Animationable() {
			@Override
			public void animate() {
				Log.d(TAG, "animate: thread=" + Thread.currentThread());
				removeCurrentView();
				if (getContentView2() != null && getContentView2().getParent() == null) {
					windowManager.addView(getContentView2(), paramsContent);
					currentView = getContentView2();
					openAnimation();
				}
			}
		});
	}

	/**
	 * 关闭动画
	 */
	private void closeAnimation(Animationable animationable) {
		Log.d(TAG, "closeAnimation: // -------------------------------------------------------------");
		printParams();
		if (floatContentView != null &&
				currentView == floatContentView) {
			floatContentView.startAnimation(animationable);
			return;
		}
		if (floatContentView2 != null &&
				currentView == floatContentView2) {
			floatContentView2.startAnimation(animationable);
			return;
		}
		if (animationable != null)
			animationable.animate();
	}

	/**
	 * 移除默认窗口
	 */
	public void removeCurrentView() {
		Log.d(TAG, "removeCurrentView: // -------------------------------------------------------------");
		printParams();
		if (currentView != null) {
			windowManager.removeView(currentView);
			currentView = null;
		}
	}

	public void move(View view, int delatX, int deltaY) {
		Log.d(TAG, "move: // -------------------------------------------------------------");
		printParams();
		if (view == floatView ||
				view == floatView2) {
			params.x += delatX;
			params.y += deltaY;
			try {
				windowManager.updateViewLayout(view, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			printParams();
		}
	}

	/**
	 * 隐藏与显示悬浮窗
	 */
	public void toogleView(boolean flag) {
		Log.d(TAG, "toogleView: // -------------------------------------------------------------");
		Log.d(TAG, "toogleView: flag=" + flag);
		printParams();
		if (instance != null) {
			if (floatView != null) {
				if (flag)
					floatView.showView();
				else
					floatView.hideView();
			}
			if (floatView2 != null) {
				if (flag)
					floatView2.showView();
				else
					floatView2.hideView();
			}
		}
	}

	/**
	 * 打开动画
	 */
	public void openAnimation() {
		Log.d(TAG, "openAnimation: // -------------------------------------------------------------");
		printParams();
		if (currentView != null &&
				floatContentView != null &&
				currentView == floatContentView) {
			floatContentView.startAnimation(new Animationable() {
				@Override
				public void animate() {

				}
			});
		} else if (currentView != null &&
				floatContentView2 != null &&
				currentView == floatContentView2) {
			floatContentView2.startAnimation(new Animationable() {
				@Override
				public void animate() {

				}
			});
		}
	}

	/**
	 * 销毁资源
	 */
	public void destroyView() {
		Log.d(TAG, "destroyView: // -------------------------------------------------------------");
		printParams();
		removeCurrentView();
	}
}