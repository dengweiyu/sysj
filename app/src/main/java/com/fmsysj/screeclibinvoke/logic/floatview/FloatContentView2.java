package com.fmsysj.screeclibinvoke.logic.floatview;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifeimo.screenrecordlib.RecordingManager;
import com.ifeimo.screenrecordlib.Utils;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.toast.ToastHelper;
import com.fmsysj.screeclibinvoke.data.observe.ObserveManager;
import com.fmsysj.screeclibinvoke.data.observe.listener.FrontCameraObservable;
import com.fmsysj.screeclibinvoke.data.observe.listener.Recording2Observable;
import com.fmsysj.screeclibinvoke.data.observe.listener.RecordingObservable;
import com.fmsysj.screeclibinvoke.logic.frontcamera.FrontCameraManager;
import com.fmsysj.screeclibinvoke.logic.screenrecord.RecordingService;

/**
 * 视图：浮窗详情2
 */
public class FloatContentView2 extends RelativeLayout implements
		OnClickListener,
		RecordingObservable,
		Recording2Observable,
		FrontCameraObservable{

	public static final String TAG = FloatContentView2.class.getSimpleName();

	private FrameLayout root;
	public RelativeLayout container;

	private LinearLayout leftTop;
	private LinearLayout rightTop;
	private LinearLayout rightBottom;
	private LinearLayout leftBottom;
	private LinearLayout top;

	private ImageView topIcon;
	private TextView topText;
	private ImageView rightTopIcon;
	private TextView rightTopText;

	public int width;
	public int height;

	private ContentAnimation2 animation;

	private Context context;
	private LayoutInflater inflater;
	private Handler handler;

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		ObserveManager.getInstance().addRecordingObservable(this);
		ObserveManager.getInstance().addRecording2Observable(this);
		ObserveManager.getInstance().addFrontCameraObservable(this);
		handler.post(new Runnable() {
			@Override
			public void run() {

				refreshFrontCamera();
				refreshRecording();
			}
		});
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		ObserveManager.getInstance().removeRecordingObservable(this);
		ObserveManager.getInstance().removeRecording2Observable(this);
		ObserveManager.getInstance().removeFrontCameraObservable(this);
	}

	/**
	 * 回调：发布录屏事件（录屏中）
	 */
	@Override
	public void onRecording() {
		handler.post(new Runnable() {
			@Override
			public void run() {

				refreshRecording();
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

	/**
	 * 回调：前置摄像头
	 */
	@Override
	public void onCamera() {
		handler.post(new Runnable() {
			@Override
			public void run() {

				refreshFrontCamera();
			}
		});
	}

	public FloatContentView2() {
		super(AppManager.getInstance().getContext(), null);

		initialize();
	}

	private void initialize() {
		context = AppManager.getInstance().getContext();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		handler = new Handler(Looper.myLooper());

		initView();
		initState();
	}

	private void initView() {
		inflater.inflate(R.layout.view_floatcontentview2, this);

		root = (FrameLayout) findViewById(R.id.root);
		container = (RelativeLayout) findViewById(R.id.container);

		top = (LinearLayout) findViewById(R.id.floatview_top);
		leftTop = (LinearLayout) findViewById(R.id.floatview_leftTop);
		rightTop = (LinearLayout) findViewById(R.id.floatview_rightTop);
		rightBottom = (LinearLayout) findViewById(R.id.floatview_rightBottom);
		leftBottom = (LinearLayout) findViewById(R.id.floatview_leftBottom);

		topIcon = (ImageView) findViewById(R.id.floatview_top_icon);
		topText = (TextView) findViewById(R.id.floatview_top_text);
		rightTopIcon = (ImageView) findViewById(R.id.floatview_rightTop_icon);
		rightTopText = (TextView) findViewById(R.id.floatview_rightTop_text);

		ViewGroup.LayoutParams params = container.getLayoutParams();
		width = params.width;
		height = params.height;

		root.setOnClickListener(this);
		top.setOnClickListener(this);
		leftTop.setOnClickListener(this);
		rightTop.setOnClickListener(this);
		leftBottom.setOnClickListener(this);
		rightBottom.setOnClickListener(this);
	}

	private void initState() {
		animation = new ContentAnimation2(container, root);
	}

	/**
	 * 开始浮窗动画
	 */
	public void startAnimation(Animationable animationable) {
		// 开始浮窗动画
		animation.startAnimation(animationable);
	}

	/**
	 * 设置右上角按键
	 */
	private void refreshFrontCamera() {
		if (FrontCameraManager.getInstance().isOpen()) {// 前置摄像头打开
			rightTopIcon.setImageResource(R.drawable.floatview_frontcamera_black);
			rightTopText.setText(R.string.floatview_frontcamera_close);
		} else {// 前置摄像头关闭
			rightTopIcon.setImageResource(R.drawable.floatview_frontcamera_gray);
			rightTopText.setText(R.string.floatview_frontcamera_open);
		}
	}

	private int iconMark, textMark;

	/**
	 * 设置顶部按键
	 */
	private void refreshRecording() {
		Log.d(TAG, "refreshRecording: // -----------------------------------------");
		if (RecordingManager.getInstance().isRecording()) {
			if (RecordingManager.getInstance().isPausing()) {// 录屏中，暂停中
				if (iconMark != 1) {
					topIcon.setImageResource(R.drawable.floatview_screenrecord_resume);
					iconMark = 1;
					Log.d(TAG, "refreshRecording: iconMark=" + iconMark);
				}
				if (textMark != 1) {
					topText.setText(R.string.floatview_screenrecord_resume);
					textMark = 1;
					Log.d(TAG, "refreshRecording: textMark=" + textMark);
				}
			} else {// 录屏中
				if (iconMark != 2) {
					topIcon.setImageResource(R.drawable.floatview_screenrecord_pause);
					iconMark = 2;
					Log.d(TAG, "refreshRecording: iconMark=" + iconMark);
				}
				if (textMark != 2) {
					topText.setText(R.string.floatview_screenrecord_pause);
					textMark = 2;
					Log.d(TAG, "refreshRecording: textMark=" + textMark);
				}
			}
		} else {// 不在录屏中
			if (iconMark != 1) {
				topIcon.setImageResource(R.drawable.floatview_screenrecord_resume);
				iconMark = 1;
				Log.d(TAG, "refreshRecording: iconMark=" + iconMark);
			}
			if (textMark != 3) {
				topText.setText(R.string.floatview_screenrecord_start);
				textMark = 3;
				Log.d(TAG, "refreshRecording: textMark=" + textMark);
			}
		}
	}

	@Override
	public void onClick(View v) {
		Log.d(TAG, "onClick: // -----------------------------------------");
		Log.d(TAG, "onClick: thread=" + Thread.currentThread());
		setEnabledDelayed(v);
		if (v == root) {// 关闭
			close();
		} else if (v == leftTop) {// 停止
			stopScreenRecord();
		} else if (v == leftBottom) {// 截屏
			startScreenCapture();
		} else if (v == rightBottom) {// 主页
			main();
		} else if (v == rightTop) {// 主播
			toogleFrontCamera();
		} else if (v == top) {// 暂停
			toogleScreenRecord();
		}
	}

	/**
	 * 关闭
	 */
	private void close() {
		FloatViewManager.getInstance().showView2();
	}

	/**
	 * 主页
	 */
	private void main() {
		// 主页
		ActivityManeger.startMainActivityNewTask();
		// 展开浮窗1
		FloatViewManager.getInstance().showView2();
	}

	/**
	 * 前置摄像头
	 */
	private void toogleFrontCamera() {
		if (FrontCameraManager.getInstance().isOpen()) {// 前置摄像头打开
			RecordingService.closeFrontCamera();
		} else {// 前置摄像头关闭
			RecordingService.openFrontCamera();
		}

		// 展开浮窗2
		FloatViewManager.getInstance().showView2();
	}

	/**
	 * 暂停，继续录屏
	 */
	private void toogleScreenRecord() {
		if (RecordingManager.getInstance().isRecording()) {
			if (RecordingManager.getInstance().isPausing()) {// 录屏中，暂停中
				// 继续录屏
				RecordingService.resumeScreenRecord();
			} else {// 录屏中
				// 暂停录屏
				RecordingService.pauseScreenRecord();
			}

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					refreshRecording();
				}
			}, 300);
		}

		// 展开浮窗2
		FloatViewManager.getInstance().showView2();
	}

	/**
	 * 停止录屏
	 */
	private void stopScreenRecord() {

		// 停止录屏
		RecordingService.stopScreenRecord();

		// 展开浮窗1
		// FloatViewManager.getInstance().showView();
	}

	/**
	 * 截屏
	 */
	private void startScreenCapture() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && !Utils.root()) {
			ToastHelper.s(R.string.root_content);
			return;
		}

		// 截屏
		RecordingService.startScreenCapture();

		// 展开浮窗2
		FloatViewManager.getInstance().showView2();
	}

	private void setEnabledDelayed(final View view) {
		if (view != null) {
			view.setEnabled(false);
			view.setClickable(false);
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					view.setEnabled(true);
					view.setClickable(true);
				}
			}, 1200);
		}
	}
}