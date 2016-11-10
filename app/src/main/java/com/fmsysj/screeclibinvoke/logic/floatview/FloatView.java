package com.fmsysj.screeclibinvoke.logic.floatview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.fmsysj.screeclibinvoke.data.observe.ObserveManager;
import com.fmsysj.screeclibinvoke.data.observe.listener.Recording2Observable;
import com.fmsysj.screeclibinvoke.data.observe.listener.RecordingObservable;

/**
 * 视图：浮窗1
 */
public class FloatView extends FrameLayout implements
		RecordingObservable,
		Recording2Observable {

	public static final String TAG = FloatView.class.getSimpleName();

	private ImageView icon;
	private FrameLayout root;
	private RelativeLayout container;

	public int width;
	public int height;

	private int downX;
	private int downY;
	public int downXFirst;
	public int downYFirst;

	private int displayWidth;
	private int displayHeight;

	private int moveX;
	private int moveY;
	private boolean isMove = false;

	private Context context;
	private LayoutInflater inflater;
	private Handler handler;

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		ObserveManager.getInstance().addRecordingObservable(this);
		ObserveManager.getInstance().addRecording2Observable(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		ObserveManager.getInstance().removeRecordingObservable(this);
		ObserveManager.getInstance().removeRecording2Observable(this);
	}

	/**
	 * 回调：发布录屏事件（录屏中）
	 */
	@Override
	public void onRecording() {
		handler.post(new Runnable() {
			@Override
			public void run() {

				if (RecordingManager.getInstance().isRecording())
					FloatViewManager.getInstance().showView2();
				else
					FloatViewManager.getInstance().showView();
			}
		});
	}

	/**
	 * 回调：发布录屏事件（开始，停止，暂停，继续）
	 */
	@Override
	public void onRecording2() {
		handler.post(new Runnable() {
			@Override
			public void run() {

				if (RecordingManager.getInstance().isRecording())
					FloatViewManager.getInstance().showView2();
				else
					FloatViewManager.getInstance().showView();
			}
		});
	}

	public FloatView() {
		super(AppManager.getInstance().getContext(), null);
		Log.d(TAG, "FloatView: thread=" + Thread.currentThread());

		context = AppManager.getInstance().getContext();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		handler = new Handler(Looper.myLooper());

		inflater.inflate(R.layout.view_floatview, this);
		root = (FrameLayout) findViewById(R.id.root);
		container = (RelativeLayout) findViewById(R.id.container);
		icon = (ImageView) findViewById(R.id.floatview_icon);

		width = container.getLayoutParams().width;
		height = container.getLayoutParams().height;

		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		displayWidth = manager.getDefaultDisplay().getWidth();
		displayHeight = manager.getDefaultDisplay().getHeight();

		FloatViewManager.getInstance().move(this, displayWidth, displayHeight / 2);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG, "onTouchEvent: thread=" + Thread.currentThread());
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				icon.setImageResource(R.drawable.floatview_icon_close);
				downX = (int) event.getRawX();
				downY = (int) event.getRawY();
				downXFirst = downX;
				downYFirst = downY;
				isMove = false;
				break;

			case MotionEvent.ACTION_UP:
				// 停止移动时，将控件当前坐标重新赋值给全局变量
				int[] location = new int[2];
				icon.getLocationOnScreen(location);
				if (location[0] == 0 && location[1] == 0) {
					FloatViewManager.getInstance().moveX = (int) event.getRawX();
					FloatViewManager.getInstance().moveY = (int) event.getRawY();
				} else {
					FloatViewManager.getInstance().moveX = location[0];
					FloatViewManager.getInstance().moveY = location[1];
				}
				FloatViewManager.getInstance().floatWidth = container.getWidth();
				if (!isMove) {
					onClick();
				}
				// 重新设置图片
				icon.setImageResource(R.drawable.floatview_icon_open);
				break;

			case MotionEvent.ACTION_MOVE:
				moveX = (int) event.getRawX();
				moveY = (int) event.getRawY();
				// 消除手指点击误差
				if (((moveX - downXFirst) > -15 && (moveX - downXFirst) < 15) && ((moveY - downYFirst) > -15 && (moveY - downYFirst) < 15)) {
					isMove = false;
				} else {
					FloatViewManager.getInstance().move(this, moveX - downX, moveY - downY);
					isMove = true;
				}
				downX = moveX;
				downY = moveY;
				break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 点击
	 */
	private void onClick() {
		Log.d(TAG, "onClick: // -----------------------------------------");
		Log.d(TAG, "onClick: thread=" + Thread.currentThread());
		FloatViewManager.getInstance().showContentView();
	}

	public void showView() {
		Log.d(TAG, "showView: // -----------------------------------------");
		if (getVisibility() != VISIBLE)
			setVisibility(VISIBLE);
	}

	public void hideView() {
		Log.d(TAG, "hideView: // -----------------------------------------");
		if (getVisibility() == VISIBLE)
			setVisibility(GONE);
	}
}