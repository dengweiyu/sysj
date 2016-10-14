package com.fmsysj.zbqmcs.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;

import com.fmsysj.zbqmcs.activity.ScreenRecordActivity;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseService;

/**
 * 服务：首页两个按钮的计时服务
 */
public class PanelTimeService extends BaseService {

	private int which;
    private TimerTask task; // 定时任务
    private int msgWhat = 0;
    private int times = 0;
    private String HMSTimes;
    private Timer timer;
    private Message msg;
    private TimeBinder binder;

    @Override
	public IBinder onBind(Intent intent) {
		if (binder == null) {
			binder = new TimeBinder();
		}
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		ExApplication.stopTimeService = false;

        showNotification();
	}

    private void showNotification() {
        Intent intent = new Intent(this, PanelTimeService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // 浮窗的服务捆绑到状态栏中，相当于把服务从后台间接移到前台，提高应用的稳定性，防止被系统杀死
        Notification notification = new Notification.Builder(
                AppManager.getInstance().getContext()).setContentIntent(pendingIntent)
                .setTicker("启动录制服务").setContentTitle("手游视界")
                .setSmallIcon(R.drawable.fm_float_ico)
                .setWhen(System.currentTimeMillis()).setAutoCancel(false)
                .getNotification();
        startForeground(1, notification);
    }

    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			which = intent.getIntExtra("which", 0);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public class TimeBinder extends Binder {
		/**
		 * 开始计时
		 */
		public void startTime() {
			times = 0;
			HMSTimes = null;
			new Thread(time).start();

		}

		/**
		 * 暂停计时
		 */
		public void pauseTime() {
			if (timer != null) {
				timer.cancel();
				timer.purge();
				timer = null;
			}
		}

		/**
		 * 停止计时
		 */
		public void stopTime() {

			if (timer != null) {
				timer.cancel();
				timer.purge();
				timer = null;
			}

			times = 0;
			HMSTimes = null;
		}

		/**
		 * 继续计时
		 */
		public void goonTime() {
			new Thread(time).start();
		}

		private Runnable time = new Runnable() {

			public void run() {

				if (which == 0) {
					msgWhat = 5;
				} else if (which == 1) {
					msgWhat = 6;
				}
				// 开始计时
				task = new TimerTask() {
					@Override
					public void run() {
						if (ExApplication.stopTimeService) {

							if (timer != null) {
								timer.cancel();
								timer.purge();
								timer = null;
							}
//							ScreenRecordActivity.handler.sendEmptyMessage(9);
                            ScreenRecordActivity.sendEmptyMessage(9);
						} else {
							times++;
							msg = new Message();
							msg.what = msgWhat;

							HMSTimes = ExApplication.getTransformTime(times
									+ "");
							msg.obj = HMSTimes;
//							ScreenRecordActivity.handler.sendMessage(msg);
                            ScreenRecordActivity.sendMessage(msg);
						}
					}
				};
				// 初始化计时器
				if (timer != null) {
					timer.cancel();
					timer.purge();
					timer = null;
				}
				timer = new Timer();
				timer.schedule(task, 1000, 1000); // 启动定时器,时间为1s
			}
		};
	}

	public void onDestroy() {

		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;

		} else {
			timer = new Timer();
			timer.cancel();
			timer.purge();
			timer = null;
		}
		times = 0;
		HMSTimes = null;
	};

}
