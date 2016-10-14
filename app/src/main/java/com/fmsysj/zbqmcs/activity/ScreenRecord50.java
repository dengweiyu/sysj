package com.fmsysj.zbqmcs.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fmsysj.zbqmcs.encoder.MediaAudioEncoder;
import com.fmsysj.zbqmcs.encoder.MediaEncoder;
import com.fmsysj.zbqmcs.encoder.MediaMuxerWrapper;
import com.fmsysj.zbqmcs.encoder.MediaScreenEncoder;
import com.fmsysj.zbqmcs.floatview.FloatView2;
import com.fmsysj.zbqmcs.floatview.FloatViewManager;
import com.fmsysj.zbqmcs.record.Recorder44;
import com.fmsysj.zbqmcs.record.Settings;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.li.videoapplication.R;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.framework.AppManager;

import java.io.File;
import java.io.IOException;

/**
 * 5.0录制activity
 * 
 * @author WYX
 * 
 */
@SuppressLint("NewApi")
public class ScreenRecord50 extends Activity {

    /**
     * 跳转
     */
    public static final synchronized void showNewTask() {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, ScreenRecord50.class);
        context.startActivity(intent);
    }

    /**
     * 跳转
     */
    public static final synchronized void showNewTask(boolean shake) {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, ScreenRecord50.class);
        intent.putExtra("shake", shake);
        context.startActivity(intent);
    }

	private static final int REQUEST_CODE = 1;
	private MediaProjectionManager mMediaProjectionManager;

	public static boolean startRecoing = false;
	final int width = 1280;
	final int height = 720;
	private static Context mContext;
	private static SharedPreferences sp;

	public static MediaMuxerWrapper mMuxer;
	private static File file;
	/***
	 * 是否通过摇晃启动此页面
	 */
	boolean shake = false;
	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 判断文件
				int fileSize = (int) FileUtil.getFileSize(file);
				if (fileSize > 512) {
					Message mMsg = new Message();
					// 视频文件路径
					mMsg.obj = file.getPath();
					mMsg.what = 2;
					// 发送消息准备吐司通知用户录制成功
					RecordVideo.mHandler.sendMessage(mMsg);

				} else {
					RecordVideo.mHandler.sendEmptyMessage(3);
				}
				break;

			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fm_screenrecord50_activity);
		if (ExApplication.mConfiguration == true) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		mContext = ScreenRecord50.this;
		shake = getIntent().getBooleanExtra("shake", false);
		mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

		sp = PreferenceManager.getDefaultSharedPreferences(mContext);
	}

	@Override
	// 将开始录屏提示放在onResume中，确保每一次打开activity都能调用到
	protected void onResume() {
		super.onResume();
		startRecord();
	}

	public void startRecord() {

		if (startRecoing == false) {
			// 弹出谷歌提示框提示用户是否允许录屏
			Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
			startActivityForResult(captureIntent, REQUEST_CODE);

		}

	}

	public static void stopRecord() {
		if (mMuxer != null) {
			mMuxer.stopRecording();
			mMuxer = null;
		}
		handler.sendEmptyMessageDelayed(1, 1500);

		RecordVideo.isStop = true;
		RecordVideo.isStart = false;
		RecordVideo.useFloatView = false;
		RecordVideo.isRecordering = false;
		startRecoing = false;
		// MainActivity.endRecord = true;
		FloatView2.min = 0; // 时间清零
		FloatView2.sec = 0;
		// 如果开启了悬浮窗录制模式，返回悬浮窗1
		if (!sp.getBoolean("no_float_view_record", false)) {
            FloatViewManager.getInstance().BackToView1();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 开始录屏返回-1，取消录屏返回0
		if (resultCode == -1) {

			RecordVideo.isStart = true;
			RecordVideo.isStop = false;
			RecordVideo.isRecordering = true;
			RecordVideo.recordering = true;
			ScreenRecordActivity.endRecord = false;
			// 如果开启了悬浮窗录制模式，启动悬浮窗
			if (!sp.getBoolean("no_float_view_record", false)) {
                FloatViewManager.getInstance().showContent();
                FloatViewManager.getInstance().removeFirst();

//				FloatContentView.windowManager.BackToView2();
                FloatViewManager.getInstance().BackToView2();
			}
			startRecoing = true;

			try {
				file = SYSJStorageUtil.createRecPath();
				String videoFileName = file.getAbsolutePath();

				mMuxer = new MediaMuxerWrapper(videoFileName);
			} catch (IOException e) {

				e.printStackTrace();
			}

			MediaProjection mediaProjection = mMediaProjectionManager
					.getMediaProjection(resultCode, data);

			if (mediaProjection == null) {
				Log.e("@@", "media projection is null");
				return;
			}

			// 如果是摇晃录屏，则根据当前屏幕方向来录屏
			if (shake) {

				setvideoQualityForShake(ExApplication.videoQuality);
			} else {// 根据当前配置来录屏
				setvideoQuality(ExApplication.videoQuality);

			}

			if (sp.getBoolean("record_sound", true)) {
				Recorder44.isRecordAudio = true;

			} else {
				Recorder44.isRecordAudio = false;
			}

			// 录制声音
			if (Recorder44.isRecordAudio) {
				new MediaAudioEncoder(mMuxer, mMediaEncoderListener);
			}

			new MediaScreenEncoder(mMuxer, mMediaEncoderListener,
					Settings.width, Settings.height, 1, mediaProjection,
					Settings.BITRATE);

			try {
				mMuxer.prepare();
			} catch (IOException e) {
				e.printStackTrace();
			}

			mMuxer.startRecording();
			moveTaskToBack(true);
			this.finish();
		} else {
			// 如果开启了悬浮窗录制模式，启动悬浮窗
			if (!sp.getBoolean("no_float_view_record", false)) {
				// 弹回第一个浮窗
//				FloatContentView.windowManager.BackToView1();
                FloatViewManager.getInstance().BackToView1();
				RecordVideo.isStop = true;
				RecordVideo.isStart = false;
			}
			ExApplication.resultCode50 = resultCode;
			this.finish();
		}
	}

	/**
	 * callback methods from encoder
	 */
	private final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {
		@Override
		public void onPrepared(final MediaEncoder encoder) {
		}

		@Override
		public void onStopped(final MediaEncoder encoder) {

		}
	};

	// 根据清晰度设置分辨率
	private void setvideoQuality(String videoQuality) {
		if (videoQuality.equals(ExApplication.HQuality)) {
			Settings.BITRATE = 2000000;
			// 根据sp配置或者横竖屏设定分辨率
			if (sp.getBoolean("horizontalRecord", true)) {// 横屏
				Settings.width = 1280;
				Settings.height = 720;

			} else {// 竖屏
				Settings.width = 720;
				Settings.height = 1280;

			}

		} else if (videoQuality.equals(ExApplication.SQuality)) {
			Settings.BITRATE = 1200000;
			// 根据sp配置或者横竖屏设定分辨率
			if (sp.getBoolean("horizontalRecord", true)) {
				Settings.width = 720;
				Settings.height = 480;

			} else {
				Settings.width = 480;
				Settings.height = 720;

			}

		}
		/**
		 * 此配置已经作废
		 * 
		 */
		else if (videoQuality.equals("流畅")) {
			if (ScreenRecordActivity.getConfiguration()) {
				Settings.width = 640;
				Settings.height = 360;

			} else {
				Settings.width = 360;
				Settings.height = 640;

			}

		}

	}

	// 根据清晰度设置分辨率
	private void setvideoQualityForShake(String videoQuality) {
		if (videoQuality.equals(ExApplication.HQuality)) {
			Settings.BITRATE = 2000000;
			// 根据横竖屏来判断录屏方向
			if (ScreenRecordActivity.getConfiguration()) {// 横屏
				Settings.width = 1280;
				Settings.height = 720;

			} else {// 竖屏
				Settings.width = 720;
				Settings.height = 1280;

			}

		} else if (videoQuality.equals(ExApplication.SQuality)) {
			Settings.BITRATE = 1200000;
			// 根据横竖屏来判断录屏方向
			if (ScreenRecordActivity.getConfiguration()) {
				Settings.width = 720;
				Settings.height = 480;

			} else {
				Settings.width = 480;
				Settings.height = 720;

			}

		}
		/**
		 * 此配置已经作废
		 * 
		 */
		else if (videoQuality.equals("流畅")) {
			if (ScreenRecordActivity.getConfiguration()) {
				Settings.width = 640;
				Settings.height = 360;

			} else {
				Settings.width = 360;
				Settings.height = 640;

			}

		}

	}

	protected void onDestroy() {
		super.onDestroy();
		// if (mRecorder != null) {
		// mRecorder.quit();
		// mRecorder = null;
		// }
	}
}
