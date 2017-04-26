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

import com.fmsysj.screeclibinvoke.ui.activity.ScreenRecordActivity;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.ifeimo.screenrecordlib.Utils;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.ToastHelper;
import com.fmsysj.screeclibinvoke.data.observe.ObserveManager;
import com.fmsysj.screeclibinvoke.data.observe.listener.FrontCameraObservable;
import com.fmsysj.screeclibinvoke.data.observe.listener.Recording2Observable;
import com.fmsysj.screeclibinvoke.logic.frontcamera.FrontCameraManager;
import com.fmsysj.screeclibinvoke.logic.screenrecord.RecordingService;
import com.li.videoapplication.ui.ActivityManeger;

/**
 * 视图：浮窗详情
 */
public class FloatContentView extends RelativeLayout implements
		OnClickListener,
		Recording2Observable,
		FrontCameraObservable{

	public static final String TAG = FloatContentView.class.getSimpleName();

	private FrameLayout root;
	public RelativeLayout container;

	private LinearLayout top;
	private LinearLayout bottom;
	private LinearLayout left;
	private LinearLayout right;
	private ImageView rightIcon;
	private TextView rightText;

	public int width;
	public int height;

	private Handler handler;
	private ContentAnimation animation;
	private Context context;
	private LayoutInflater inflater;

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		ObserveManager.getInstance().addRecording2Observable(this);
		ObserveManager.getInstance().addFrontCameraObservable(this);
		handler.post(new Runnable() {
			@Override
			public void run() {

				refreshFrontCamera();
			}
		});
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		ObserveManager.getInstance().removeRecording2Observable(this);
		ObserveManager.getInstance().removeFrontCameraObservable(this);
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
					RecordingService.showFloatView2();
				else
					RecordingService.showFloatView();
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

	public FloatContentView() {
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
		inflater.inflate(R.layout.view_floatcontentview, this);

		root = (FrameLayout) findViewById(R.id.root);
		container = (RelativeLayout) findViewById(R.id.container);

		top = (LinearLayout) findViewById(R.id.floatview_top);
		bottom = (LinearLayout) findViewById(R.id.floatview_bottom);
		left = (LinearLayout) findViewById(R.id.floatview_left);
		right = (LinearLayout) findViewById(R.id.floatview_right);

		rightIcon = (ImageView) findViewById(R.id.floatview_right_icon);
		rightText = (TextView) findViewById(R.id.floatview_right_text);

		root.setOnClickListener(this);
		top.setOnClickListener(this);
		bottom.setOnClickListener(this);
		left.setOnClickListener(this);
		right.setOnClickListener(this);

		ViewGroup.LayoutParams params = container.getLayoutParams();
		width = params.width;
		height = params.height;
	}

	private void initState() {
		animation = new ContentAnimation(container, root);
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
			rightIcon.setImageResource(R.drawable.floatview_frontcamera_black);
			rightText.setText(R.string.floatview_frontcamera_close);
		} else {// 前置摄像头关闭
			rightIcon.setImageResource(R.drawable.floatview_frontcamera_gray);
			rightText.setText(R.string.floatview_frontcamera_open);
		}
	}

	@Override
	public void onClick(View v) {
		Log.d(TAG, "onClick: // -----------------------------------------");
		Log.d(TAG, "onClick: thread=" + Thread.currentThread());
		setEnabledDelayed(v);
		if (v == top) {// 录制
			startScreenRecord();
		} else if (v == left) {// 截屏
			startScreenCapture();
		} else if (v == root) {// 关闭
			close();
		} else if (v == bottom) {// 主页
			main();
		} else if (v == right) {// 主播
			toogleFrontCamera();
		}
	}

	/**
	 * 关闭
	 */
	private void close() {

		// 展开浮窗1
		FloatViewManager.getInstance().showView();
	}

	/**
	 * 主页
	 */
	private void main() {
		// 主页
		ScreenRecordActivity.startScreenRecordActivity(getContext());

		// 展开浮窗1
		FloatViewManager.getInstance().showView();
	}

	/**
	 * 开始录屏
	 */
	private void startScreenRecord() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && !Utils.root()) {
			ToastHelper.s(R.string.root_content);
			return;
		}

		// 开始录屏
		RecordingService.startScreenRecord();

		// 显示浮窗2
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

		// 展开浮窗1
		FloatViewManager.getInstance().showView();
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

		// 展开浮窗1
		FloatViewManager.getInstance().showView();
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