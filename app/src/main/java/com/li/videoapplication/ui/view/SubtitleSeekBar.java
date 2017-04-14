package com.li.videoapplication.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.li.videoapplication.R;
import com.li.videoapplication.tools.SRTHelper;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;
import com.li.videoapplication.ui.fragment.SubtitleFragment;
import com.li.videoapplication.ui.srt.SRT;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.Iterator;

public class SubtitleSeekBar extends ImageView {

	public final static String TAG = SubtitleSeekBar.class.getSimpleName();

	private Paint transparentPaint = new Paint();
	/**左右滑块 文本*/
	private Paint yellowPinPaint = new Paint(), whitePinPaint = new Paint();
	/**左右箭头 文本*/
	private	Paint yellowArrowPaint = new Paint();
	/**遮罩*/
	private Paint pinAndArrowPaint = new Paint();
	private Paint srtPaint = new Paint();
	private Paint editPaint = new Paint();

	/** 左右滑块 */
	private Bitmap leftPin, rightPin;
	/** 左右箭头 */
	private Bitmap leftArrow, rightArrow;

	/** 左右箭头 X坐标 */
	private int leftArrowX, rightArrowX;
	/** 左右滑块 X坐标 */
	private int leftPinX, rightPinX;
	/** 左右滑块 高度 */
	private int leftPinY, rightPinY;
	/** 左右滑块最小间距 */
	private int minDistance;

	/**最小时间间隔（毫秒）*/
	private long minTime = 1 * 1000;
	/** 最大时间（毫秒）*/
	private long maxTime = 120 * 1000;
	/** 左右滑块时间（毫秒） */
	private long leftTime = 0, rightTime = maxTime;
	/** 每秒移动坐标距离（pix/seconds） */
	private float speed;

	/** 哪个滑块进行滑动 */
	private int slidingPin = -1;
	/** 滑块 宽度 */
	private int pinWidth, pinHeight;

	/**
	 * 是否显示微调按钮
	 * -1:不显示
	 * 0:显示左边微调按钮
	 * 1:显示右边微调按钮
	 */
	private int leftOrRight = -1;

	private Callback callback;
	private VideoEditorActivity2 activity;
	private SubtitleFragment fragment;

	public SubtitleSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SubtitleSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SubtitleSeekBar(Context context) {
		super(context);
		init(context);
	}

	public void init(VideoEditorActivity2 activity, SubtitleFragment fragment, Callback callback) {
		this.activity = activity;
		this.fragment = fragment;
		this.callback = callback;
	}

	private void init(Context context) {
		leftPin = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_leftpin_blue), 0);
		rightPin = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_rightpin_blue), 0);

		leftArrow = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_leftarrow_yellow), 1);
		rightArrow = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_rightarrow_yellow), 1);

		whitePinPaint.setColor(EditorSeekBar.COLOR_WHITE);
		whitePinPaint.setTextSize(EditorSeekBar.DIMEN_TEXT_PIN);
		whitePinPaint.setTypeface(Typeface.SANS_SERIF);
		whitePinPaint.setAntiAlias(true);

		yellowPinPaint.setColor(EditorSeekBar.COLOR_YELLOW);
		yellowPinPaint.setTextSize(EditorSeekBar.DIMEN_TEXT_PIN);
		yellowPinPaint.setTypeface(Typeface.SANS_SERIF);
		yellowPinPaint.setAntiAlias(true);

		yellowArrowPaint.setColor(EditorSeekBar.COLOR_YELLOW);
		yellowArrowPaint.setTextSize(EditorSeekBar.DIMEN_TEXT_ARROW);
		yellowArrowPaint.setTypeface(Typeface.SANS_SERIF);
		yellowArrowPaint.setAntiAlias(true);

		srtPaint.setAntiAlias(true);
		srtPaint.setColor(EditorSeekBar.COLOR_BLUE);
		srtPaint.setStyle(Paint.Style.FILL);
		srtPaint.setAlpha((int) (255f * 0.8f));

		editPaint.setAntiAlias(true);
		editPaint.setColor(EditorSeekBar.COLOR_BLUE);
		editPaint.setStyle(Paint.Style.FILL);
		editPaint.setAlpha((int) (255f * 0.4f));

		pinWidth = leftPin.getWidth();
		pinHeight = leftPin.getHeight();
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
				scale = (float) EditorSeekBar.DIMEN_PIN_HEIGHT/ (float) orginal;
				matrix.postScale(scale, scale);
				break;
			case 01:// 左右箭头
				scale = (float) EditorSeekBar.DIMEN_ARROW_HEIGHT/ (float) orginal;
				matrix.postScale(scale, scale);
				break;
		}
		Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return b;
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		Log.d(TAG, "onWindowFocusChanged: ");
		Log.d(TAG, "onWindowFocusChanged: hasWindowFocus=" + hasWindowFocus);
		logParams();

		rightPinY = getHeight();
		rightPinY = leftPinY;
		rightPinY = leftPin.getHeight();
		pinWidth = leftPin.getWidth();
		pinHeight = leftPin.getHeight();

		if (leftPinX == 0 || rightPinX == 0) {
			leftPinX = EditorSeekBar.DIMEN_PADDING;
			rightPinX = Math.abs(getWidth() - EditorSeekBar.DIMEN_PADDING);
		}

		setMaxTime(maxTime);
		setLeftTime(leftTime);
		setRightTime(rightTime);
		setMinTime(minTime);
		notifyChange();
	}

	public void cancelOnClick() {
		Log.d(TAG, "cancelOnClick: ");
		Log.d(TAG, "cancelOnClick: slidingPin=" + slidingPin);
		isOnClick = false;
		slidingPin = -1;
		leftOrRight = -1;
		// 滑块恢复默认样式
		leftPin = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_leftpin_blue), 0);
		rightPin = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_rightpin_blue), 0);
		Log.d(TAG, "cancelOnClick: isOnClick=" + isOnClick);
		Log.d(TAG, "cancelOnClick: slidingPin=" + slidingPin);
		Log.d(TAG, "cancelOnClick: leftOrRight=" + leftOrRight);
		notifyChange();
	}

	private int _leftPinX, _rightPinX;

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(TAG, "onDraw: ");
		Log.d(TAG, "onDraw: slidingPin=" + slidingPin);
		Log.d(TAG, "onDraw: leftOrRight=" + leftOrRight);

		_leftPinX = leftPinX - pinWidth;
		_rightPinX = rightPinX;

		// 编辑图片
		if (activity.isAddingSubtitle == true) {// 编辑状态

			// 编辑字幕阴影
			Rect rect = new Rect(leftPinX, EditorSeekBar.DIMEN_IMAGE_TOP, rightPinX, EditorSeekBar.DIMEN_IMAGE_BOTTOM);
			canvas.drawRect(rect, editPaint);

			if (leftTime != rightTime) {
				// 左右滑块
				canvas.drawBitmap(leftPin, _leftPinX, EditorSeekBar.DIMEN_PIN_TOP, pinAndArrowPaint);
				canvas.drawBitmap(rightPin, _rightPinX, EditorSeekBar.DIMEN_PIN_TOP, pinAndArrowPaint);
			}

			int textWidth;

			// 滑块文字
			String leftPinText = getPinText((int) leftTime, true);
			String rightPinText = getPinText((int) rightTime, true);
			// 字体宽度
			textWidth = (int) whitePinPaint.measureText(leftPinText);
			// 绘制底部左右端进度时间
			if ((rightPinX - pinWidth) - (leftPinX - textWidth / 2 + textWidth) <= 10) {// 两端靠近时时间框拉开距离
				if ((leftOrRight == 0)) {// 左边
					Log.d(TAG, "onDraw: 11");
					canvas.drawText(leftPinText, _leftPinX - textWidth / 2,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, yellowPinPaint);
					canvas.drawText(rightPinText, _rightPinX + textWidth / 4,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, whitePinPaint);
				} else if (leftOrRight == 1) {// 右边
					Log.d(TAG, "onDraw: 12");
					canvas.drawText(leftPinText, _leftPinX - textWidth / 2,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, whitePinPaint);
					canvas.drawText(rightPinText, _rightPinX + textWidth / 4,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, yellowPinPaint);
				} else {
					Log.d(TAG, "onDraw: 12.5");
					canvas.drawText(leftPinText, _leftPinX - textWidth / 2,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, whitePinPaint);
					canvas.drawText(rightPinText, _rightPinX + textWidth / 4,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, whitePinPaint);
				}
            } else {
				if ((leftOrRight == 0)) {// 左边
					Log.d(TAG, "onDraw: 13");
					canvas.drawText(leftPinText, _leftPinX - textWidth / 4,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, yellowPinPaint);
					canvas.drawText(rightPinText, _rightPinX - textWidth / 4,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, whitePinPaint);
				} else if (leftOrRight == 1) {// 右边
					Log.d(TAG, "onDraw: 14");
					canvas.drawText(leftPinText, _leftPinX - textWidth / 4,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, whitePinPaint);
					canvas.drawText(rightPinText, _rightPinX - textWidth / 4,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, yellowPinPaint);
				} else {
					Log.d(TAG, "onDraw: 14.5");
					canvas.drawText(leftPinText, _leftPinX - textWidth / 4,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, whitePinPaint);
					canvas.drawText(rightPinText, _rightPinX - textWidth / 4,
							EditorSeekBar.DIMEN_TEXT_PIN_BOTTOM, whitePinPaint);
				}
            }

			// 回调
			if (callback != null) {
                if (slidingPin == EditorSeekBar.SLIDING_LEFTPIN) {
                    callback.updateLeftTime(leftTime);
                }
                if (slidingPin == EditorSeekBar.SLIDING_RIGHTPIN) {
                    callback.updateRightTime(rightTime);
                }
            }

			if (leftOrRight == 0) {// 左边
				Log.d(TAG, "onDraw: 21");
				// 微调左箭头
                leftArrowX = _leftPinX - ScreenUtil.dp2px(15);
                rightArrowX = _leftPinX + ScreenUtil.dp2px(20);

                // 左右箭头
                canvas.drawBitmap(leftArrow, leftArrowX, EditorSeekBar.DIMEN_ARROW_TOP, pinAndArrowPaint);
                canvas.drawBitmap(rightArrow, rightArrowX, EditorSeekBar.DIMEN_ARROW_TOP, pinAndArrowPaint);

                // 左箭头文字
                canvas.drawText("-1s", leftArrowX, EditorSeekBar.DIMEN_TEXT_ARROW_BOTTOM, yellowArrowPaint);
                canvas.drawText("+1s", rightArrowX, EditorSeekBar.DIMEN_TEXT_ARROW_BOTTOM, yellowArrowPaint);

                // 左滑块
                updateLeftPin(1);

            } else if (leftOrRight == 1) {// 右边
				Log.d(TAG, "onDraw: 22");
                // 微调右箭头
                leftArrowX = _rightPinX - ScreenUtil.dp2px(30);
                rightArrowX = _rightPinX + ScreenUtil.dp2px(10);

                // 左右箭头
                canvas.drawBitmap(leftArrow, leftArrowX, EditorSeekBar.DIMEN_ARROW_TOP, pinAndArrowPaint);
                canvas.drawBitmap(rightArrow, rightArrowX, EditorSeekBar.DIMEN_ARROW_TOP, pinAndArrowPaint);

                // 右箭头文字
                canvas.drawText("-1s", leftArrowX, EditorSeekBar.DIMEN_TEXT_ARROW_BOTTOM, yellowArrowPaint);
                canvas.drawText("+1s", rightArrowX, EditorSeekBar.DIMEN_TEXT_ARROW_BOTTOM, yellowArrowPaint);

                // 右滑块
                updateRightPin(1);
            }
		}

		// 字幕阴影
		Rect rect;
		long left, right;
		int i = 0;
		if (fragment != null &&
				fragment.info != null &&
				fragment.info.size() > 0) {
			Iterator<SRT> iterator = fragment.info.iterator();
			while (iterator.hasNext()) {
				++ i;
				Log.d(TAG, "onDraw: i=" + i);
				SRT srt = iterator.next();
				if (srt != null &&  fragment.srt == srt) {

				} else {
					left = calculateDistanceX(SRTHelper.toMILLISECOND(srt.startTime));
					right = calculateDistanceX(SRTHelper.toMILLISECOND(srt.endTime));
					Log.d(TAG, "onDraw: left=" + left);
					Log.d(TAG, "onDraw: right=" + right);

					rect = new Rect((int) left, EditorSeekBar.DIMEN_IMAGE_TOP, (int) right, EditorSeekBar.DIMEN_IMAGE_BOTTOM);
					canvas.drawRect(rect, srtPaint);
				}
			}
		}

		logParams();
	}

	private void logParams() {
		Log.d(TAG, "-------------------------------------------------------------------");
		Log.d(TAG, "logParams: leftPinX=" + leftPinX);
		Log.d(TAG, "logParams: leftTime=" + leftTime);
		Log.d(TAG, "logParams: rightPinX=" + rightPinX);
		Log.d(TAG, "logParams: rightTime=" + rightTime);
		Log.d(TAG, "logParams: maxTime=" + maxTime);
		Log.d(TAG, "logParams: speed=" + speed);
		Log.d(TAG, "logParams: minTime=" + minTime);
		Log.d(TAG, "logParams: pinWidth=" + pinWidth);
		Log.d(TAG, "logParams: top=" + getTop());
		Log.d(TAG, "logParams: bottom=" + getBottom());
		Log.d(TAG, "logParams: left=" + getLeft());
		Log.d(TAG, "logParams: right=" + getRight());
		Log.d(TAG, "logParams: width=" + getWidth());
		Log.d(TAG, "logParams: height=" + getHeight());
	}

	public String getPinText(int time, boolean flag) {
		int minutes = (time / (60 * 1000));
		int seconds = time / 1000 % 60;
		String result = flag && minutes < 10 ? "0" : "";
		result += minutes + ":";
		if (seconds < 10) {
			result += "0" + seconds;
		} else {
			result += seconds;
		}
		return result;
	}

	/**
	 * 是否点击
	 */
	public boolean isOnClick = false;
	private boolean isEditableDown = false;
	private int downX, downY;
	private int moveX, moveY;
	private int upX, upY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG, "-------------------------------------------------------------------");
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = (int) event.getX();
				downY = (int) event.getY();
				Log.d(TAG, "onTouchEvent: ACTION_DOWN");
				Log.d(TAG, "onTouchEvent: downX=" + downX);
				Log.d(TAG, "onTouchEvent: downY=" + downY);
				if (activity.isAddingSubtitle == true) {// 编辑状态
					Log.d(TAG, "onTouchEvent: ACTION_DOWN/11");
					if (isOnClick == false) {// 移动
						// 判断按压点位置，和绘制左右哪一个滑块
						if (Math.abs(rightPinX - downX) <= ScreenUtil.dp2px(25) &&
                                downY >= ScreenUtil.dp2px(60) &&
                                downY <= ScreenUtil.dp2px(200)) {// 右滑块
                            slidingPin = EditorSeekBar.SLIDING_RIGHTPIN;
                            Log.d(TAG, "onTouchEvent: right");
                        } else if ((Math.abs(leftPinX - downX) <= ScreenUtil.dp2px(25) &&
                                downY >= ScreenUtil.dp2px(60) &&
                                downY <= ScreenUtil.dp2px(200))) {// 左滑块
                            slidingPin = EditorSeekBar.SLIDING_LEFTPIN;
                            Log.d(TAG, "onTouchEvent: left");
                        }
						// 秒数微调
						if (adjustMinDistance(downX, downY) == 0) {
                            if (leftOrRight == -1 || leftOrRight == 1) {
                                if (leftOrRight == 1) {
                                    updateRightPin(0);
                                }
                                leftOrRight = 0;
                                updateLeftPin(1);
                            } else if (leftOrRight == 0) {
                                leftOrRight = -1;
                                updateLeftPin(0);
                            }
                        } else if (adjustMinDistance(downX, downY) == 1) {
                            if (leftOrRight == 1) {
                                leftOrRight = -1;
                                updateRightPin(0);
                            } else if (leftOrRight == -1 || leftOrRight == 0) {
                                if (leftOrRight == 0) {
                                    updateLeftPin(0);
                                }
                                leftOrRight = 1;
                                updateRightPin(1);
                            }
                        }
					}
				} else if (activity.isAddingSubtitle == false){// 非编辑状态，点击字幕
					Log.d(TAG, "onTouchEvent: ACTION_DOWN/12");
					if (downY >= EditorSeekBar.DIMEN_ARROW_TOP &&
							downY <= EditorSeekBar.DIMEN_IMAGE_BOTTOM) {
						if (fragment != null &&
								fragment.info != null &&
								fragment.info.size() > 0) {
							Iterator<SRT> iterator = fragment.info.iterator();
							while (iterator.hasNext()) {
								SRT srt = iterator.next();
								long startTime = SRTHelper.toMILLISECOND(srt.startTime);
								long endTime = SRTHelper.toMILLISECOND(srt.endTime);
								long startDistance = calculateDistanceX(startTime);
								long endDistance = calculateDistanceX(endTime);
								Log.d(TAG, "onTouchEvent: startTime=" + startTime);
								Log.d(TAG, "onTouchEvent: endTime=" + endTime);
								Log.d(TAG, "onTouchEvent: startDistance=" + startDistance);
								Log.d(TAG, "onTouchEvent: endDistance=" + endDistance);
								if (downX >= startDistance && downX <= endDistance) {
									fragment.srt = srt;
									isEditableDown = true;

									// setLeftTime(startTime);
									// setRightTime(endTime);
									setTime(startTime, endTime);

									// 回调
									if (callback != null) {
										callback.updateLeftTime(startTime);
										callback.updateRightTime(endTime);
									}
									fragment.setEditable();
								}
							}
						}
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				moveX = (int) event.getX();
				moveY = (int) event.getY();
				Log.d(TAG, "onTouchEvent: ACTION_MOVE");
				Log.d(TAG, "onTouchEvent: moveX=" + moveX);
				Log.d(TAG, "onTouchEvent: moveY=" + moveY);
				if (activity.isAddingSubtitle == true &&
						isEditableDown == false) {
					Log.d(TAG, "onTouchEvent: ACTION_MOVE/11");
					Log.d(TAG, "onTouchEvent: isOnClick=" + isOnClick);
					if (isOnClick == false) {// 移动
						if (Math.abs(rightPinX - downX) <= ScreenUtil.dp2px(25) &&
                                downY >= ScreenUtil.dp2px(60) &&
                                downY <= ScreenUtil.dp2px(200)) {
                            slidingPin = EditorSeekBar.SLIDING_RIGHTPIN;
                            Log.d(TAG, "onTouchEvent: right");
                        } else if ((Math.abs(leftPinX - downX) <= ScreenUtil.dp2px(25) &&
                                downY >= ScreenUtil.dp2px(60) &&
                                downY <= ScreenUtil.dp2px(200))) {
                            slidingPin = EditorSeekBar.SLIDING_LEFTPIN;
                            Log.d(TAG, "onTouchEvent: left");
                        }

						if (slidingPin == EditorSeekBar.SLIDING_LEFTPIN &&
                                moveX <= rightPinX - minDistance &&
                                moveX >= EditorSeekBar.DIMEN_PADDING) {// 左滑块
                            leftPinX = moveX;
                            calculateTime();
                            Log.d(TAG, "onTouchEvent: leftPinX=" + leftPinX);
                        } else if (slidingPin == EditorSeekBar.SLIDING_RIGHTPIN &&
                                moveX >= leftPinX + minDistance &&
                                moveX <= getWidth() - EditorSeekBar.DIMEN_PADDING) {// 右滑块
                            rightPinX = moveX;
                            calculateTime();
                            Log.d(TAG, "onTouchEvent: rightPinX=" + rightPinX);
                        }
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				upX = (int) event.getX();
				upY = (int) event.getY();
				Log.d(TAG, "onTouchEvent: ACTION_UP");
				Log.d(TAG, "onTouchEvent: upX=" + upX);
				Log.d(TAG, "onTouchEvent: upY=" + upY);
				if (activity.isAddingSubtitle &&
						isEditableDown == false) {
					if (Math.abs(upX - downX) < 5) {// 同一点
						if (isOnClick == true) {// 点击状态
							Log.d(TAG, "onTouchEvent: ACTION_UP/40");
							Log.d(TAG, "onTouchEvent: slidingPin=" + slidingPin);
							if (slidingPin == EditorSeekBar.SLIDING_RIGHTPIN &&
									downX > rightPinX - ScreenUtil.dp2px(30) &&
									downX < rightPinX + ScreenUtil.dp2px(30) + pinWidth &&
									downY > EditorSeekBar.DIMEN_PIN_TOP &&
									downY < EditorSeekBar.DIMEN_PIN_BOTTOM + ScreenUtil.dp2px(10)) {// 右滑块
                                Log.d(TAG, "onTouchEvent: ACTION_UP/41");
                                isOnClick = false;
                            }

							if (slidingPin == EditorSeekBar.SLIDING_LEFTPIN &&
									downX > leftPinX - ScreenUtil.dp2px(30) &&
									downX < leftPinX + ScreenUtil.dp2px(30) + pinWidth &&
									downY > EditorSeekBar.DIMEN_PIN_TOP &&
									downY < EditorSeekBar.DIMEN_PIN_BOTTOM + ScreenUtil.dp2px(10)) {// 左滑块
                                Log.d(TAG, "onTouchEvent: ACTION_UP/42");
                                isOnClick = false;
                            }
						} else {// 移动状态
							Log.d(TAG, "onTouchEvent: ACTION_UP/43");
							isOnClick = true;
						}
				    }
					Log.d(TAG, "onTouchEvent: isOnClick=" + isOnClick);
					if (isOnClick == true) {// 点击操作
						Log.d(TAG, "onTouchEvent: ACTION_UP/11");
                        if (leftOrRight == 1) {// 右滑块
							Log.d(TAG, "onTouchEvent: ACTION_UP/11.1");
							// 秒数微调
                            if ((downX <= (leftArrowX + ScreenUtil.dp2px(20)))
                                    && (Math.abs(downY - ScreenUtil.dp2px(40)) <= ScreenUtil.dp2px(30))) {// 左箭头
                                if ((rightTime - leftTime) > minTime) {// 滑块间距必须大于最小间距
									if ((rightPinX - speed) <= leftPinX) {// 右滑块坐标须恒大于左滑块
										return true;
									}
									if (rightTime > leftTime) {
										if (overMinDistance())
											rightPinX = (int) (rightPinX - speed);
										rightTime = rightTime - 1000;
										if (rightTime < leftTime + minTime) {
											rightTime = leftTime + minTime;
										}
										Log.d(TAG, "onTouchEvent: ACTION_UP/-1");
									}
								}
                            } else if (downX - rightArrowX <= ScreenUtil.dp2px(50) &&
                                    downX - rightArrowX >= 0 && downY < ScreenUtil.dp2px(35)) {// 右箭头
								if (rightTime < maxTime) {
									rightPinX = (int) (rightPinX + speed);
									rightTime = rightTime + 1000;
									if (rightTime > maxTime) {
										rightTime = maxTime;
									}
									Log.d(TAG, "onTouchEvent: ACTION_UP/+1");
								}
							}
							setRightTime(rightTime);
                        } else if (leftOrRight == 0) {// 左滑块
							Log.d(TAG, "onTouchEvent: ACTION_UP/11.2");
                            // 秒数微调
                            if ((downX <= (leftArrowX + ScreenUtil.dp2px(20)))
                                    && (Math.abs(downY - ScreenUtil.dp2px(40)) <= ScreenUtil.dp2px(30))) {// 左箭头
								if (leftTime > 0) {
									leftPinX = (int) (leftPinX - speed);
									leftTime = leftTime - 1000;
									if (leftTime < 0) {
										leftTime = 0;
									}
									Log.d(TAG, "onTouchEvent: ACTION_UP/-1");
								}
							} else if (downX - rightArrowX <= ScreenUtil.dp2px(50) &&
                                    downX - rightArrowX >= 0 &&
                                    Math.abs(downY - ScreenUtil.dp2px(40)) <= ScreenUtil.dp2px(20)) {// 右箭头
                                if ((leftPinX + speed) >= rightPinX) {// 右滑块坐标须恒大于左滑块
									return true;
								}
								if ((rightTime - leftTime) > minTime) { // 滑块间距必须大于最小间距
									if (leftTime < rightTime) {
										if (overMinDistance())
											leftPinX = (int) (leftPinX + speed);
										leftTime = leftTime + 1000;
										if (leftTime > rightTime - minTime) {
											leftTime = rightTime - minTime;
										}
										Log.d(TAG, "onTouchEvent: ACTION_UP/+1");
									}
								}
                            }
                        }
						setLeftTime(leftTime);
                    } else {// 移动操作
						Log.d(TAG, "onTouchEvent: ACTION_UP/12");
						isOnClick = false;
						leftOrRight = -1;
						// 滑块恢复默认样式
						leftPin = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_leftpin_blue), 0);
						rightPin = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_rightpin_blue), 0);
					}

					downX = 0;
					downY = 0;
					moveX = 0;
					moveY = 0;
					upX = 0;
					upY = 0;
				}
				isEditableDown = false;
				break;
		}
		notifyChange();
		return true;
	}

	/**
	 * 判断点击是否唤起微调按钮
	 * return
	 * 		0:左边微调按钮
	 * 		1:右边微调按钮
	 */
	public int adjustMinDistance(int x, int y) {
		if (y <= ScreenUtil.dp2px(60) ||
				y >= ScreenUtil.dp2px(200))
			return -1;
		if (Math.abs(rightPinX - x) < Math.abs(leftPinX - x)) {
			if (Math.abs(rightPinX - x) <= ScreenUtil.dp2px(50))
				return 1;
		} else {
			if (Math.abs(leftPinX - x) <= ScreenUtil.dp2px(50))
				return 0;
		}
		return -1;
	}

	/**
	 * 判断左右滑块之间的距离是否大于最小间距
	 */
	private boolean overMinDistance() {
		if ((rightPinX - leftPinX) > minDistance)
			return true;
		return false;
	}

	/**
	 * 设置右边滑块
	 */
	private void updateRightPin(int i) {
		if (i == 0) {
			rightPin = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_rightpin_blue), 0);
		} else if (i == 1) {
			rightPin = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_rightpin_yellow), 0);
		}
	}

	/**
	 * 设置左边滑块
	 */
	private void updateLeftPin(int i) {
		if (i == 0) {
			leftPin = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_leftpin_blue), 0);
		} else if (i == 1) {
			leftPin = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.videoeditor_leftpin_yellow), 0);
		}
	}

	public void notifyChange() {
		if (leftPinX < EditorSeekBar.DIMEN_PADDING)
			leftPinX = EditorSeekBar.DIMEN_PADDING;
		if (rightPinX < EditorSeekBar.DIMEN_PADDING)
			rightPinX = EditorSeekBar.DIMEN_PADDING;
		if (leftPinX > Math.abs(getWidth() - EditorSeekBar.DIMEN_PADDING))
			leftPinX = Math.abs(getWidth() - EditorSeekBar.DIMEN_PADDING);
		if (rightPinX > Math.abs(getWidth() - EditorSeekBar.DIMEN_PADDING))
			rightPinX = Math.abs(getWidth() - EditorSeekBar.DIMEN_PADDING);
		invalidate();
	}

	/**
	 * 根据距离计算时间
	 */
	public void calculateTime() {
		if (isOnClick)
			return;
		leftTime = (maxTime * (leftPinX - EditorSeekBar.DIMEN_PADDING)) / (getWidth() - 2 * EditorSeekBar.DIMEN_PADDING);
		rightTime = (maxTime * (rightPinX - EditorSeekBar.DIMEN_PADDING)) / (getWidth() - 2 * EditorSeekBar.DIMEN_PADDING);
		if (leftTime < 0) {
			leftTime = 0;
		}
		if (rightTime < 0) {
			rightTime = 0;
		}
	}

	/**
	 * 根据时间计算距离
	 */
	private int calculateDistance(long time) {
		Log.d(TAG, "calculateDistance: ");
		Log.d(TAG, "calculateDistance: time=" + time);
		Log.d(TAG, "calculateDistance: maxTime=" + maxTime);
		Log.d(TAG, "calculateDistance: width=" + getWidth());
		return (int) ((getWidth() - 2d * EditorSeekBar.DIMEN_PADDING) * time / maxTime);
	}

	/**
	 * 根据时间计算距离
	 */
	private int calculateDistanceX(long time) {
		Log.d(TAG, "calculateDistanceX: ");
		Log.d(TAG, "calculateDistanceX: time=" + time);
		Log.d(TAG, "calculateDistanceX: maxTime=" + maxTime);
		Log.d(TAG, "calculateDistanceX: width=" + getWidth());
		return (int) (((getWidth() - 2d * EditorSeekBar.DIMEN_PADDING) / maxTime) * time) + EditorSeekBar.DIMEN_PADDING;
	}

	/**
	 * 设置左边时间
	 */
	public void setLeftTime(long leftTime) {
		Log.d(TAG, "setLeftTime: ---------------------------------------------------");
		this.leftTime = leftTime;
		if (leftTime <= rightTime - minTime) {
			leftPinX = calculateDistanceX(leftTime);
		} else {
			leftPinX = EditorSeekBar.DIMEN_PADDING;
		}
		Log.d(TAG, "setLeftTime: leftTime=" + leftTime);
		Log.d(TAG, "setLeftTime: leftPinX=" + leftPinX);
		notifyChange();
	}

	/**
	 * 设置右边时间
	 */
	public void setRightTime(long rightTime) {
		Log.d(TAG, "setRightTime: ---------------------------------------------------");
		this.rightTime = rightTime;
		if (rightTime >= leftTime + minTime) {
			rightPinX = calculateDistanceX(rightTime);
		} else {
			rightPinX = Math.abs(getWidth() - EditorSeekBar.DIMEN_PADDING);
		}
		Log.d(TAG, "setRightTime: rightTime=" + rightTime);
		Log.d(TAG, "setRightTime: rightPinX=" + rightPinX);
		notifyChange();
	}

	/**
	 * 设置时间
	 */
	public void setTime(long leftTime, long rightTime) {
		Log.d(TAG, "setTime: ---------------------------------------------------");
		this.leftTime = leftTime;
		this.rightTime = rightTime;
		if (rightTime - leftTime > minTime) {
			leftPinX = calculateDistanceX(leftTime);
			rightPinX = calculateDistanceX(rightTime);
		}
		Log.d(TAG, "setTime: leftTime=" + leftTime);
		Log.d(TAG, "setTime: rightTime=" + rightTime);
		Log.d(TAG, "setTime: leftPinX=" + leftPinX);
		Log.d(TAG, "setTime: rightPinX=" + rightPinX);
		notifyChange();
	}

	/**
	 * 设置最大时间
	 */
	public void setMaxTime(long maxTime) {
		Log.d(TAG, "setMaxTime: ---------------------------------------------------");
		this.maxTime = maxTime;
		speed = (getWidth() - EditorSeekBar.DIMEN_PADDING * 2) * 1000 / (float) maxTime;
		Log.d(TAG, "setMaxTime: speed=" + speed);
		if (speed < 1) {
			speed = 1;
		}
		Log.d(TAG, "setMaxTime: maxTime=" + maxTime);
		Log.d(TAG, "setMaxTime: speed=" + speed);
		notifyChange();
	}

	/**
	 * 设置最小时间间隔，最小距离
	 */
	public void setMinTime(long minTime) {
		Log.d(TAG, "setMinTime: ---------------------------------------------------");
		this.minTime = minTime;
		Log.d(TAG, "setMinTime: minTime=" + minTime);
		minDistance = calculateDistance(minTime);
		Log.d(TAG, "setMinTime: minDistance=" + minDistance);
		if (minDistance < ScreenUtil.dp2px(2)) {
			minDistance = ScreenUtil.dp2px(2);
		}
		Log.d(TAG, "setMinTime: minDistance=" + minDistance);
		notifyChange();
	}

	public interface Callback {

		void updateLeftTime(long leftTime);

		void updateRightTime(long rightTime);
	}
}