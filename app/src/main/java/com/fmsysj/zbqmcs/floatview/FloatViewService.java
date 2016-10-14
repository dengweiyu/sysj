package com.fmsysj.zbqmcs.floatview;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.fmsysj.zbqmcs.activity.ScreenRecordActivity;
import com.fmsysj.zbqmcs.record.Recorder44;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.fmsysj.zbqmcs.utils.VideoEditor;
import com.li.videoapplication.R;
import com.li.videoapplication.data.local.StorageUtil;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 服务：悬浮窗
 */
public class FloatViewService extends BaseService {

    /**
     * 发送广播
     * @param what
     */
    public static void sendEmptyMessage(int what) {
        FloatViewService service = (FloatViewService) AppManager.getInstance().getService(FloatViewService.class);
        if (service != null && service.h != null)
            service.h.sendEmptyMessage(what);
    }

    /**
     * 发送广播
     * @param msg
     */
    public static void sendMessage(Message msg) {
        FloatViewService service = (FloatViewService) AppManager.getInstance().getService(FloatViewService.class);
        if (service != null && service.h != null)
            service.h.sendMessage(msg);
    }

    /**
     * 消息处理
     */
    public Handler h = new Handler() {
        @SuppressLint("SdCardPath")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    FloatContentView.h.sendEmptyMessage(1);
                    FloatContentView.sendEmptyMessage(1);
                    break;

                case 2:
                    ScreenRecordActivity.showNewTask();
                    break;

                case 3:
//                    FloatContentView2.h.sendEmptyMessage(1);
                    FloatContentView2.sendEmptyMessage(1);
                    break;

                case 4:
                    // 给FloatContentView发送消息停止录屏
                    //FloatContentView.h.sendEmptyMessage(2);
                    FloatContentView.sendEmptyMessage(2);
                    break;
            }
        }
    };

    public static int times = 0;
	public static int recordCnt = 1; // 录制次数

    private ScheduledExecutorService threadPool;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();

        showNotification();
    }

    private void showNotification() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, FloatViewService.class), 0);

        // 浮窗的服务捆绑到状态栏中，相当于把服务从后台间接移到前台，提高应用的稳定性，防止被系统杀死
        Notification notification = new Notification.Builder(this)
                .setContentIntent(contentIntent).setTicker("启动录制服务")
                .setContentTitle("手游视界").setSmallIcon(R.drawable.fm_float_ico)
                .setWhen(System.currentTimeMillis()).setAutoCancel(false)
                .getNotification();
        startForeground(1, notification);
    }

	public void DelAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				DelAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (threadPool == null) {
			threadPool = Executors.newScheduledThreadPool(1);
			threadPool.scheduleAtFixedRate(command, 0, 1, TimeUnit.SECONDS);
		}

		// 开启悬浮框
        FloatViewManager.getInstance().showContent();
        return START_REDELIVER_INTENT;
    }

	/**
	 * 循环计时
	 */
	private Runnable command = new Runnable() {
		@Override
		public void run() {

			if (RecordVideo.isStop == false && RecordVideo.isStart == true) {
				if (ExApplication.pauseRecVideo == false) {
					times++;
				}

			} else if (RecordVideo.isStart == false
					&& RecordVideo.isStop == true) {
				times = 0;
				recordCnt = 1;
			}
		}
	};

	public void onDestroy() {
		super.onDestroy();

//		 if (threadPool != null) {
//		     threadPool.shutdown();
//		     threadPool = null;
//		 }
	}

	/**
	 * 判断当前界面是否是桌面
	 */
	private boolean isHome() {
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return getHomes().contains(rti.get(0).topActivity.getPackageName());
	}

	/**
	 * 获得属于桌面的应用的应用包名称 （系统原装应用、Go桌面等等） 这些应用都会包含："android.intent.category.Home"；
	 * 所以只要找出所有的声明为Home的activity的"android.intent.action.MAIN"所在的包名就可以了！
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	private List<String> getHomes() {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		switch (newConfig.orientation) {
		case Configuration.ORIENTATION_LANDSCAPE: {
			FloatViewManager.isLandscape = true;
            FloatViewManager.getInstance().changeOrientation(1);
		}
			break;

		case Configuration.ORIENTATION_PORTRAIT: {
			FloatViewManager.isLandscape = false;
            FloatViewManager.getInstance().changeOrientation(0);
		}
			break;
		}
	}

	public static void muxVideoAndAudio(final String[] fileList,
			final String audioName) {
		new Thread() {

			public void run() {

				if (fileList.length > 1) {
					RecordVideo.mHandler.sendEmptyMessage(1);

					// String[] fileList = Recorder44.fileList.toArray(new
					// String[Recorder44.fileList.size()]);
					String appendVideo = VideoEditor.appendVideo(fileList);
					if (appendVideo != null) {
						if (Recorder44.isRecordAudio) {

							String videoFile = VideoEditor
									.MuxVideoAndAudio(appendVideo, audioName);
							if (videoFile != null) {

								Message msg = new Message();
								msg.obj = videoFile;
								msg.what = 2;
								RecordVideo.mHandler.sendMessage(msg);
								// mHandler.sendEmptyMessage(2);

							} else {
								RecordVideo.mHandler.sendEmptyMessage(3);

							}

							// endRecord(videoFile);

						} else {
							// mHandler.sendEmptyMessage(2);
							// 视频已合成
							// endRecord(appendVideo);
							Message msg = new Message();
							msg.obj = appendVideo;
							msg.what = 2;
							RecordVideo.mHandler.sendMessage(msg);
						}
					} else {
						RecordVideo.mHandler.sendEmptyMessage(3);
						// 合成失败2;
					}
					// 删除源文件
					File file;
					for (String x : fileList) //
					{
						file = new File(x);
						if (file.exists()) {
							file.delete();
						}
					}

				} else {
					// String[] fileList = Recorder44.fileList.toArray(new
					// String[Recorder44.fileList.size()]);

					if (Recorder44.isRecordAudio) {
						RecordVideo.mHandler.sendEmptyMessage(1);
						String videoFile = VideoEditor.MuxVideoAndAudio(
								fileList[0], audioName);
						if (videoFile != null) {
							// 视频已合成;

							Message msg = new Message();
							msg.obj = videoFile;
							msg.what = 2;
							RecordVideo.mHandler.sendMessage(msg);

						} else {
							RecordVideo.mHandler.sendEmptyMessage(3);
							// 合成失败3
						}

						// endRecord(videoFile);
					} else {
						// mHandler.sendEmptyMessage(2);
						// 视频已合成
						Message msg = new Message();
						msg.obj = fileList[0];
						msg.what = 2;
						RecordVideo.mHandler.sendMessage(msg);

					}
				}

			}
		}.start();
	}

	public boolean endRecord(String videoFile) {
		ScreenRecordActivity.videofilename  = StorageUtil.createRecName();
		String newName = ScreenRecordActivity.path_dir + File.separator + ScreenRecordActivity.videofilename;
		new File(videoFile).renameTo(new File(newName));
		ScreenRecordActivity.endRecord = true;
		return true;
	}

	// 通知栏通知

	/**
	 * 
	 * @param fileName
	 *            视频保存路径
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void nofity(String fileName) {

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.icon;
		CharSequence tickerText = "视频合成完成,来这里预览你的作品~";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Context context = getApplicationContext();
		CharSequence contentTitle = "录屏大师";
		CharSequence contentText = "点击预览视频";
		File file = new File(fileName);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndTypeAndNormalize(uri, "video/mp4");
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		mNotificationManager.notify(0, notification);

	}
}