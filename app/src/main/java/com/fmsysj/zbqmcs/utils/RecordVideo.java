package com.fmsysj.zbqmcs.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import com.fmsysj.zbqmcs.activity.ScreenRecord50;
import com.fmsysj.zbqmcs.activity.ScreenRecordActivity;
import com.fmsysj.zbqmcs.floatview.FloatView2;
import com.fmsysj.zbqmcs.floatview.FloatViewManager;
import com.fmsysj.zbqmcs.frontcamera.FrontCameraService;
import com.fmsysj.zbqmcs.record.Recorder44;
import com.fmsysj.zbqmcs.floatview.FloatViewService;
import com.fmsysj.zbqmcs.service.ScreenRECService;
import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.io.File;

/**
 * 录屏相关类
 * 
 * @author WYX
 * 
 */
public class RecordVideo {

	Context mContext;

	/** 是否开始录屏 */
	public static boolean isStart = false;
	/** 是否停止录屏 */
	public static boolean isStop = true;
	public static boolean useFloatView = false;
	/** 是否处于录屏之中 */
	public static boolean isRecordering = false;
	public static boolean recordering = false;
	public static Handler mHandler;
	// 视频模式（高清，标准）
	String videoMode;
	private SharedPreferences sp;

	public RecordVideo(Context context) {

		mContext = context;
		sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		mHandler = new Handler() {
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 1: {
					ExApplication.isCompositionVideo = true;
                    ToastHelper.s("视频合成中,请稍候...");
				}
					break;
				case 2: {
					ExApplication.isCompositionVideo = false;
                    ToastHelper.s("视频已合成,可点击通知栏进行预览");
					if (ExApplication.islaterCloaseApp) {
						exitProgrames();
					}
					// 视频合成路径
					String path = (String) msg.obj;
					// 截取视频路径中的视频名称
					String name = path.substring(path.lastIndexOf("/") + 1);
                    String fileName = name.split("\\.mp4")[0];
                    VideoCaptureManager.save(path,
                            VideoCaptureEntity.VIDEO_SOURCE_REC,
                            VideoCaptureEntity.VIDEO_STATION_LOCAL);
                    nofity(path, mContext);
				}
					break;
				case 3: {

					ExApplication.isCompositionVideo = false;
					String content;
					if (ExApplication.getAndroidSDKVersion() >= 21) {
						content = "非常抱歉,您的视频合成失败了";
					} else {
						content = "非常抱歉,您的视频合成失败了，请确保手机真正root成功后重试";
					}
                    ToastHelper.s(content);
					if (ExApplication.islaterCloaseApp == true) {
						exitProgrames();
					}
				}
					break;
				}
			}

			/**
			 * 退出录屏大师后台服务
			 */
			private void exitProgrames() {

				// 取消所有的通知
				NotificationManager mNotificationManager;
				mNotificationManager = (NotificationManager) AppManager.getInstance().getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.cancelAll();

				// 终止录制服务
				AppManager.getInstance().getApplication().stopService(new Intent(AppManager.getInstance().getApplication(), ScreenRECService.class));

				// 终止浮窗服务
				AppManager.getInstance().getApplication().stopService(new Intent(AppManager.getInstance().getApplication(), FloatViewService.class));

				android.os.Process.killProcess(android.os.Process.myPid());

			}
		};
	}

	public void stardRecordVideo() {

		// if (ExApplication.floatCameraClose == true) {

		if (ScreenRecordActivity.SDKVesion < 19) {
			// 这里同时也兼应用第一次录屏时设置清晰度（根据android版本不同设置不同默认配置，4.4以下是标清，4.4以上是超清）
			videoMode = sp.getString("quality_of_video", ExApplication.SQuality);

			// 将2.0.1.1及之前版本的清晰度修改成最新版本
			if (videoMode.equals("高清")) {
				videoMode = "0";
			} else if (videoMode.equals("标准")) {
				videoMode = "1";
			} else if (videoMode.equals("流畅")) {
				videoMode = "0";
			}
			// 将当前清晰度配置赋给全局变量
			ExApplication.videoQuality = videoMode;

			// 开始录屏
			StartRecordForVesionOther();

		}

		else if (ScreenRecordActivity.SDKVesion >= 19 && ScreenRecordActivity.SDKVesion < 21) {
			videoMode = sp.getString("quality_of_video", ExApplication.HQuality);

			// 将2.0.1.1及之前版本的清晰度修改成最新版本
			if (videoMode.equals("高清")) {
				videoMode = "0";
			} else if (videoMode.equals("标准")) {
				videoMode = "1";
			} else if (videoMode.equals("流畅")) {
				videoMode = "0";
			}

			ExApplication.videoQuality = videoMode;
			StartRecordForVesion19();

		} else {
			videoMode = sp.getString("quality_of_video", ExApplication.HQuality);

			// 将2.0.1.1及之前版本的清晰度修改成最新版本
			if (videoMode.equals("高清")) {
				videoMode = "0";
			} else if (videoMode.equals("标准")) {
				videoMode = "1";
			} else if (videoMode.equals("流畅")) {
				videoMode = "0";
			}

			ExApplication.videoQuality = videoMode;
			ExApplication.mConfiguration = ScreenRecordActivity.getConfiguration();

            ScreenRecord50.showNewTask();

		}
		// 如果打开了设置页中的触摸选项，录屏时显示触摸点
		boolean isShowTouch = sp.getBoolean("show_touch_view", false);
		if (isShowTouch) {
			android.provider.Settings.System.putInt(mContext.getContentResolver(), "show_touches", 1);
		}
		// 如果打开了前置摄像头选项
		boolean isfloatCamera = sp.getBoolean("show_front_camera", false);
		if (isfloatCamera) {
			// 开启画中画
			Intent intent = new Intent(mContext, FrontCameraService.class);
			mContext.startService(intent);
			ExApplication.floatCameraClose = false;

		}
		FloatView2.doubleclick = false;
	}

    /**
	 * 4.4版本开始录制方法
	 */
	public void StartRecordForVesion19() {

		if (isStart == false && isStop == true) {

			sp = PreferenceManager.getDefaultSharedPreferences(mContext);
			if (sp.getBoolean("record_sound", true)) {
				Recorder44.isRecordAudio = true;

			} else {
				Recorder44.isRecordAudio = false;
			}

			isRecordering = true;
			recordering = true;
			isStart = true;
			isStop = false;
			ScreenRecordActivity.endRecord = false;

			Recorder44.StartRecordVideo(videoMode, mContext);

		}
	}

	/**
	 * 4.4版本停止录制方法
	 */
	public void StopRecordForVesion19() {
		// SettingShowTouchesController.setShowTouches(context, false);
		isRecordering = false;
		isStop = true;
		isStart = false;

		FloatView2.min = 0; // 时间清零
		FloatView2.sec = 0;

		Recorder44.StopRecordVideo();
	}

	/**
	 * 4.4以下系统开始录制方法
	 */
	public void StartRecordForVesionOther() {
		if (isStart == false && isStop == true) {
			isStart = true;
			isStop = false;
			isRecordering = true;
			ScreenRecordActivity.endRecord = false;
			// btStart.setImageDrawable(getResources().getDrawable(MResource.getIdByName(context,"drawable","bt_stop_down")));
			useFloatView = true;
			// 录制结果对用户进行通知
//			ScreenRECService.h.sendEmptyMessage(4);
            ScreenRECService.sendEmptyMessage(4);
			// isLayoutBackClick = true;

		}

	}

	/**
	 * 4.4以下系统停止录制方法
	 */
	public void StopRecordForVesionOther() {

		isStop = true;
		isStart = false;
		useFloatView = false;
		isRecordering = false;

		// MainActivity.endRecord = true;
		FloatView2.min = 0; // 时间清零
		FloatView2.sec = 0;

		// 录制结果对用户进行通知
//		ScreenRECService.h.sendEmptyMessage(4);
        ScreenRECService.sendEmptyMessage(4);
		// isLayoutBackClick = true;

	}

	/**
	 * 录制成功通知栏
	 * 
	 * @param fileName
	 *            视频保存路径
	 */
	@SuppressLint("NewApi")
	public static void nofity(String fileName, Context mContext) {

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(ns);
		int icon = R.drawable.icon;
		CharSequence tickerText = "视频合成完成,来这里预览你的作品~";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Context context = mContext;
		CharSequence contentTitle = "手游视界";
		CharSequence contentText = "点击预览视频";
		File file = new File(fileName);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndTypeAndNormalize(uri, "video/mp4");
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		mNotificationManager.notify(0, notification);

	}

	/**
	 * 录制状态通知栏
	 * 
	 * @param content
	 * @param mContext
	 */
	@SuppressWarnings("deprecation")
	public static void RecordStateNofity(String content, Context mContext) {

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(ns);
		int icon = R.drawable.icon;
		CharSequence tickerText = content;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Context context = mContext;
		CharSequence contentTitle = "手游视界";
		CharSequence contentText = content;

		Intent intent = new Intent();

		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		mNotificationManager.notify(0, notification);

	}
}
