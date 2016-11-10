package com.fmsysj.screeclibinvoke.logic.floatview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.R;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.fmsysj.screeclibinvoke.data.observe.ObserveManager;
import com.fmsysj.screeclibinvoke.data.observe.listener.Recording2Observable;
import com.fmsysj.screeclibinvoke.data.observe.listener.RecordingObservable;
import com.fmsysj.screeclibinvoke.ui.view.FlickerView2;

/**
 * 视图：浮窗2
 */
public class FloatView2 extends FrameLayout implements
		RecordingObservable,
		Recording2Observable {

	public static final String TAG = FloatView2.class.getSimpleName();

	public RelativeLayout container;
	private FrameLayout root;
	public ImageView icon;
	public TextView text;
	private FlickerView2 flickerView;

	public int width;
	public int height;
	private int downX;
	private int downY;
	public int downXFirst;
	public int downYFirst;
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
		handler.post(new Runnable() {
			@Override
			public void run() {

				refreshContentView();
			}
		});
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

				refreshContentView();
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

				refreshContentView();
				if (RecordingManager.getInstance().isRecording())
					FloatViewManager.getInstance().showView2();
				else
					FloatViewManager.getInstance().showView();
			}
		});
	}

	public FloatView2() {
		super(AppManager.getInstance().getContext(), null);

		initialize();
		initView();

		if (PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws()) {
			// 隐藏与显示悬浮窗
			showView();
		} else {
			hideView();
		}
	}

	private void initialize() {
		context = AppManager.getInstance().getContext();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		handler = new Handler(Looper.myLooper());
	}

	private void initView() {
		inflater.inflate(R.layout.view_floatview2, this);
		root = (FrameLayout) findViewById(R.id.root);
		container = (RelativeLayout) findViewById(R.id.container);
		icon = (ImageView) findViewById(R.id.floatview_icon);
		text = (TextView) findViewById(R.id.floatview_text);
		flickerView = (FlickerView2) findViewById(R.id.floatview_point);

		width = container.getLayoutParams().width;
		height = container.getLayoutParams().height;

		text.setText("00:00");
		container.setAlpha(1f);
		flickerView.hideView();
	}

	private int iconMark;

	private void refreshContentView() {
		// Log.d(TAG, "refreshContentView: // -----------------------------------------");
		if (RecordingManager.getInstance().isRecording()) {
			if (RecordingManager.getInstance().isPausing()) {// 录屏暂停中
				if (iconMark != 1) {
					icon.setImageResource(R.drawable.floatview_icon_pause);
					flickerView.hideView();
					iconMark = 1;
					Log.d(TAG, "refreshContentView: iconMark=" + iconMark);
				}
			} else {// 录屏中
				text.setText(RecordingManager.getInstance().time());
				if (iconMark != 2) {
					icon.setImageResource(R.drawable.floatview_icon);
					flickerView.flickView();
					iconMark = 2;
					Log.d(TAG, "refreshContentView: iconMark=" + iconMark);
				}
			}
		} else {// 不在录屏中
			text.setText("00:00");
			if (iconMark != 3) {
				icon.setImageResource(R.drawable.floatview_icon);
				flickerView.hideView();
				iconMark = 3;
				Log.d(TAG, "refreshContentView: iconMark=" + iconMark);
			}
			FloatViewManager.getInstance().showView();
		}
	}

	// 防止点击两下导致崩溃
	public boolean doubleClick = false;

	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = (int) event.getRawX();
				downY = (int) event.getRawY();
				downXFirst = downX;
				downYFirst = downY;
				isMove = false;
				break;

			case MotionEvent.ACTION_UP:
				// 停止移动时，将控件当前坐标重新赋值给全局变量
				int[] location = new int[2];
				container.getLocationOnScreen(location);
				FloatViewManager.getInstance().floatWidth = container.getWidth();
				FloatViewManager.getInstance().moveX = location[0];
				FloatViewManager.getInstance().moveY = location[1];
				if (!isMove && doubleClick == false) {
					onClick();
				}
				break;

			case MotionEvent.ACTION_MOVE:
				moveX = (int) event.getRawX();
				moveY = (int) event.getRawY();
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
	 *  点击
	 */
	private void onClick() {
		Log.d(TAG, "onClick: // -----------------------------------------");
		Log.d(TAG, "onClick: thread=" + Thread.currentThread());
		// 展开浮窗详情2
		FloatViewManager.getInstance().showContentView2();
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