package com.li.videoapplication.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.utils.ScreenUtil;

public class CoverSeekBar extends ImageView {

	public static final String TAG = CoverSeekBar.class.getSimpleName();

	/**滑块高度, 顶部, 底部*/
	private static final int DIMEN_PIN_HEIGHT = ScreenUtil.dp2px(70);
	private static final int DIMEN_PIN_TOP = ScreenUtil.dp2px(21);
	private static final int DIMEN_PIN_BOTTOM = DIMEN_PIN_TOP + DIMEN_PIN_HEIGHT;

	/**滑块*/
	private Paint pinPaint = new Paint();

	/** 滑块 */
	private Bitmap centerPin;
	/** 滑块 X坐标 */
	private int centerPinX;
	/** 滑块 高度 */
	private int centerPinY;

	/**最大时间（毫秒）*/
	private long maxTime = 120 * 1000;
	/**滑块时间（毫秒）*/
	private long centerTime = 0;
	/** 每秒秒移动坐标距离（pix/seconds） */
	private float speed;

	/** 滑块 宽度 */
	private int pinWidth;

	/** 是否是点击 */
	public boolean isOnTouch = false;

	/** 是否是点击 */
	public boolean isFirstOnTouch = false;

	private Callback callback;

	public CoverSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CoverSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CoverSeekBar(Context context) {
		super(context);
		init(context);
	}

	public void init(Callback callback) {
		this.callback = callback;
	}

	private void init(Context context) {
		centerPin = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_centerpin), 0);
		pinWidth = centerPin.getWidth();
	}

	/**
	 * 缩放Bitmap
	 */
	private Bitmap scaleBitmap(Bitmap bitmap, int witch) {
		int orginal = bitmap.getHeight();
		float scale;
		Matrix matrix = new Matrix();
		switch (witch) {
			case 0:// 左右滑块
				scale = (float) DIMEN_PIN_HEIGHT / (float) orginal;
				matrix.postScale(scale, scale);
				break;
		}
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return bmp;
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);

		centerPinY = getHeight();
		pinWidth = centerPin.getWidth();

		if (centerPinX == 0) {
			centerPinX = 1 + EditorSeekBar.DIMEN_PADDING;
		}
		
		setMaxTime(maxTime);
		setCenterTime(centerTime);
		notifyChange();

		logParams();
	}

	private int _centerPinX;

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		_centerPinX = centerPinX - pinWidth / 2;

		// 滑块
		canvas.drawBitmap(centerPin, _centerPinX, DIMEN_PIN_TOP, pinPaint);

		// 回调
		if (callback != null) {
			callback.updateCenterTime(centerTime);
		}

		logParams();
	}

	private void logParams() {
		Log.d(TAG, "-------------------------------------------------------------------");
		Log.d(TAG, "logParams: centerPinX=" + centerPinX);
		Log.d(TAG, "logParams: centerTime=" + centerTime);
		Log.d(TAG, "logParams: maxTime=" + maxTime);
		Log.d(TAG, "logParams: pinWidth=" + pinWidth);
	}

	private int moveX, moveY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		boolean isActionUp = false;

		int preX = (int) event.getX();
		int preY = (int) event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				moveX = (int) event.getX();
				break;
			case MotionEvent.ACTION_MOVE:
				moveY = (int) event.getX();
				if (Math.abs(moveY - moveX) > 5) {
					isFirstOnTouch = true;
					isOnTouch = true;
				}
				if (isOnTouch) {
					centerPinX = preX;
					calculateTime();
				}
				break;
			case MotionEvent.ACTION_UP:
				isActionUp = true;
				if (isOnTouch) {
					isOnTouch = false;
				}
				break;
		}
		notifyChange();
		// 回调
		if (callback != null && isActionUp == true) {
			callback.afterCenterTime(centerTime);
		}
		return true;
	}

	public void notifyChange() {
		Log.d(TAG, "--------------------------------------------------------------------");
		if (centerPinX < EditorSeekBar.DIMEN_PADDING)
			centerPinX = EditorSeekBar.DIMEN_PADDING;
		if (centerPinX > Math.abs(getWidth() - EditorSeekBar.DIMEN_PADDING))
			centerPinX = Math.abs(getWidth() - EditorSeekBar.DIMEN_PADDING);
		invalidate();
		Log.d(TAG, "notifyChange: getWidth()=" + getWidth());
		Log.d(TAG, "notifyChange: DIMEN_PADDING=" + EditorSeekBar.DIMEN_PADDING);
		Log.d(TAG, "notifyChange: centerPinX=" + centerPinX);
	}

	/**
	 * 根据距离计算时间
	 */
	private void calculateTime() {
		Log.d(TAG, "--------------------------------------------------------------------");
		centerTime = (maxTime * (centerPinX - EditorSeekBar.DIMEN_PADDING)) / (getWidth() - 2 * EditorSeekBar.DIMEN_PADDING);
		if (centerTime < 0) {
			centerTime = 0;
		}
	}

	/**
	 * 设置时间
	 */
	public void setCenterTime(long centerTime) {
		Log.d(TAG, "--------------------------------------------------------------------");
		this.centerTime = centerTime;
		if (centerTime <= maxTime && centerTime >= 0) {
			centerPinX = (int) (((getWidth() - 2d * EditorSeekBar.DIMEN_PADDING) * centerTime / maxTime)) + EditorSeekBar.DIMEN_PADDING;
		} else {
			centerPinX = EditorSeekBar.DIMEN_PADDING + 1;
		}
		Log.d(TAG, "setCenterTime: centerTime=" + centerTime);
		Log.d(TAG, "setCenterTime: centerPinX=" + centerPinX);
		notifyChange();
	}

	/**
	 * 设置最大时间
	 */
	public void setMaxTime(long maxTime) {
		Log.d(TAG, "--------------------------------------------------------------------");
		this.maxTime = maxTime;
		float width = (getWidth() - (EditorSeekBar.DIMEN_PADDING * 2)) * 1000;
		speed = width / (float) maxTime;
		if (speed < 1) {
			speed = 1;
		}
		Log.d(TAG, "setMaxTime: maxTime=" + maxTime);
		Log.d(TAG, "setMaxTime: speed=" + speed);
		notifyChange();
	}

	public interface Callback {

		void updateCenterTime(long centerTime);

		void afterCenterTime(long centerTime);
	}
}