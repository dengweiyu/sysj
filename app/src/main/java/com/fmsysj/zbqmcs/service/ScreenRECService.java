package com.fmsysj.zbqmcs.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.fmsysj.zbqmcs.activity.ScreenRecord50;
import com.fmsysj.zbqmcs.activity.ScreenRecordActivity;
import com.fmsysj.zbqmcs.floatview.FloatView2;
import com.fmsysj.zbqmcs.floatview.FloatViewManager;
import com.fmsysj.zbqmcs.floatview.FloatViewService;
import com.fmsysj.zbqmcs.frontcamera.CameraView;
import com.fmsysj.zbqmcs.frontcamera.FrontCameraService;
import com.fmsysj.zbqmcs.record.NativeProcessRunner;
import com.fmsysj.zbqmcs.record.RecordAction;
import com.fmsysj.zbqmcs.record.ScreenCoreHandler;
import com.fmsysj.zbqmcs.record.Settings;
import com.fmsysj.zbqmcs.record.ShakeListeners;
import com.fmsysj.zbqmcs.utils.EnumUtils;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.local.StorageUtil;
import com.li.videoapplication.data.preferences.SharedPreferencesUtils;
import com.li.videoapplication.data.local.FileUtil;
import com.fmsysj.zbqmcs.utils.RUtils;
import com.fmsysj.zbqmcs.utils.RecordVideo;
import com.fmsysj.zbqmcs.utils.ViewUtils;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseService;
import com.li.videoapplication.ui.activity.SettingActivity;

import java.io.File;

/**
 * 服务：录制服务
 */
public class ScreenRECService extends BaseService implements ShakeListeners.OnShakeListener {

	// 视频模式（高清，标准，流畅）
    private String videoMode;
	private SharedPreferences sp;
    private RecordVideo recordVideo;

    /**
     * 发送广播
     * @param what
     */
    public static void sendEmptyMessage(int what) {
        ScreenRECService service = (ScreenRECService) AppManager.getInstance().getService(ScreenRECService.class);
        if (service != null && service.h != null)
            service.h.sendEmptyMessage(what);
    }

    /**
     * 消息处理
     */
    public Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 4: // 开始或停止录制
                if (!isProgress) {
                    sendBroadcastRecordAction2();
                } else {
                    sendBroadcastRecordAction2();
                }
				break;
			case 5: // 回调设置界面
                //startSettingActivity();

				break;
			case 6: // 回调设置界面
                ScreenRecordActivity.showNewTask();
				break;

			case 7: {// 开声音
				SharedPreferences.Editor editor = sound_settings.edit();
				editor.putBoolean("sound_setting", true);
				editor.commit();
			}
				break;

			case 8: {// 关声音
				SharedPreferences.Editor editor = sound_settings.edit();
				editor.putBoolean("sound_setting", false);
				editor.commit();
			}
				break;

			case 9:
                startFloatViewService();
				break;

			case 10:
                //sendBroadcastRecordAction();
				break;
			}

		}
	};

    private void sendBroadcastRecordAction2() {
        Intent intent = new Intent(RecordAction.ACTION);
        intent.putExtra("action", "screen_shot");
        intent.putExtra("hideStatusPanel", true);
        mContextScreenService.sendBroadcast(intent);
    }

    private void startSettingActivity() {
        Intent intent2 = new Intent();
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.setClass(mContextScreenService, SettingActivity.class);
        mContextScreenService.startActivity(intent2);
    }

    private void sendBroadcastRecordAction() {

        Intent intent = new Intent(RecordAction.ACTION);
        intent.putExtra("action", "open_record_file");
        //intent.putExtra("out_file", MainActivity.path_dir+ "/"+ MainActivity.videofilename);
        mContextScreenService.sendBroadcast(RecordAction.getActionIntent("open_record_file_action"));
    }

    private void startFloatViewService() {
        Intent intent = new Intent();
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(mContextScreenService, FloatViewService.class);
        mContextScreenService.startService(intent);
    }

    private static String PREFS_NAME = "sound_setting";

	public static SharedPreferences sound_settings;

	NativeProcessRunner mNativeProcessRunner;
	long screen_shot_last_time = 0;
	Thread thread;
	long lastShake = System.currentTimeMillis();
	private boolean isProgress = false;
	// private SoundPool pool;
	// private int soundID;
	// private float streamVolume;
	private static String outFile;

	public static Context mContextScreenService;

	private boolean yaohuangjieping = true;

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getStringExtra("action");
			if ("show_screenshot_notification".equals(action)) {
				// showScreenshotNotification();
			} else if ("start_yaohuangjieping_service".equals(action)) {
				start_yaohuangjieping_service();
			} else if ("end_yaohuangjieping_service".equals(action)) {
				end_yaohuangjieping_service();
			} else if ("start_record_no_alert".equals(action)) {
				start_record_no_alert();
			} else if ("cancel_record_no_alert".equals(action)) {
				cancel_record_no_alert();
			} else if ("open_record_file_action".equals(action)) {
				open_record_file_action();
			} else if ("open_record_file_without_alert".equals(action)) {
				final String fileName = intent.getStringExtra("file_path");
				open_record_file_without_alert(fileName);
			} else if ("share_record_file_to_friend".equals(action)) {
				share_record_file_to_friend(intent);
			} else if ("open_record_file_without_alert_preview".equals(action)) {
				final String fileName = intent.getStringExtra("file_path");
				open_record_file_without_alert_preview(fileName);
			} else if ("open_record_file_without_alert_download_mx_player".equals(action)) {

			} else if ("open_record_file_without_alert_dont_show_again".equals(action)) {

			} else if ("vote_comment_btn_action".equals(action)) {

			} else if ("alert_no_auth_get_pro_version".equals(action)) {
				alert_no_auth_get_pro_version();
			} else if ("screen_shot".equals(action)) {
				if (intent.getBooleanExtra("hideStatusPanel", false)) {
					ViewUtils.hideStatusPanel(ScreenRECService.this);
				}
				if ((System.currentTimeMillis() - screen_shot_last_time) > 3000) {

					if (!isProgress) {

						start_record_no_alert();

						RecordVideo.isStart = true;
						RecordVideo.isStop = false;

					} else {

						isProgress = false;
						RecordVideo.isStart = false;
						RecordVideo.isStop = true;
						RecordVideo.isRecordering = false;
						FloatView2.min = 0; // 时间清零
						FloatView2.sec = 0;

						endScreenRecord();
						ScreenRecordActivity.endRecord = true;
					}
				}
			} else if ("open_record_file".equals(action)) {
				String fileName = intent.getStringExtra("path");
				open_record_file_without_alert(fileName);
			}
		}
	};

	private void cancel_record_no_alert() {
		ScreenCoreHandler.uninstall(ScreenRECService.this);
		isProgress = false;
	}

	public void start_record_no_alert() {
		final Handler handler = new Handler() {
			private boolean flash = false;

			@Override
			public void handleMessage(Message msg) {
				int icon;
				icon = getRDrawableID("icon");
				if (msg.what == 1) {
					msg.what = 0;

					if (!flash) {
						icon = getRDrawableID("icon");
						flash = true;
					} else {
						icon = getRDrawableID("recording_2");
						flash = false;
					}
				}

				if (isProgress) {
					sendEmptyMessageDelayed(1, 1000);
				} else {
					// mNotificationManager.cancel(getRXmlID("screenshot_setting"));
				}
			}
		};
		showTransparentToastInUI();
		handler.sendEmptyMessageDelayed(0, 800);
		if (PreferenceManager.getDefaultSharedPreferences(ScreenRECService.this).getBoolean("play_screenshot_sound", true)) {

			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					_record_screen_now();
				}
			}.sendEmptyMessageDelayed(0, 700);
		} else {
			_record_screen_now();
		}
	}

	private void showTransparentToast(Context context) {
		Toast result = new Toast(context);
		TextView tv = new TextView(context);
		tv.setText("");
		result.setView(tv);
		result.setDuration(Toast.LENGTH_LONG);
		result.show();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter(RecordAction.ACTION);

		registerReceiver(broadcastReceiver, filter);

		new ShakeListeners(this).setOnShakeListener(this);

		mContextScreenService = ScreenRECService.this;

		sound_settings = getSharedPreferences(PREFS_NAME, 0);

		sp = PreferenceManager.getDefaultSharedPreferences(this);

		boolean hide_notify = sp.getBoolean("hide_notify", false);

		if (sp.getBoolean("yahangjieping", false)) {
			start_yaohuangjieping_service();
		} else {
			end_yaohuangjieping_service();
		}

		DisplayMetrics dm = mContextScreenService.getResources().getDisplayMetrics();
		Settings.width = (int) (dm.widthPixels);
		Settings.height = (int) (dm.heightPixels);

	}

	void startRecord() {

		mNativeProcessRunner = new NativeProcessRunner();
		mNativeProcessRunner.setExecutable(ScreenCoreHandler.install(this));
		mNativeProcessRunner.initialize();

		mNativeProcessRunner.setOnReadyListener(new NativeProcessRunner.OnReadyListener() {
            @Override
            public void onReady() {

                outFile = getOutputFile().getAbsolutePath();
                mNativeProcessRunner.start(outFile);
            }

            @Override
            public void onFinished() {
                ScreenCoreHandler.uninstall(ScreenRECService.this);

            }
        });
	}

	// 水印

	void stopRecording() {
		// FloatView2.removeWater();
		this.mNativeProcessRunner.stop();
		this.mNativeProcessRunner.destroy();
	}

	private void _record_screen_now() {
		isProgress = true;
		configSettings();

		startRecord();
		// alertNoAuth();
	}

	// -----录制参数配置-----！！！
	private void configSettings() {
		Context context = ScreenRECService.this;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		// 是否录制声音
		if (sp.getBoolean("record_sound", false)) {
			Settings.audioSource = Settings.AudioSource.MIC;
		} else {
			Settings.audioSource = Settings.AudioSource.MUTE;
		}

		int rotation = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
		switch (rotation) {
		case Surface.ROTATION_0:
			Settings.rotation = EnumUtils.ordinalOf(Settings.Rotation.class, Integer.valueOf("1"));
			break;
		case Surface.ROTATION_90:
			Settings.rotation = EnumUtils.ordinalOf(Settings.Rotation.class, Integer.valueOf("0"));
			break;
		case Surface.ROTATION_180:
			Settings.rotation = EnumUtils.ordinalOf(Settings.Rotation.class, Integer.valueOf("3"));
			break;
		case Surface.ROTATION_270:
			Settings.rotation = EnumUtils.ordinalOf(Settings.Rotation.class, Integer.valueOf("2"));
			break;
		}

		if (sp.getBoolean("video_draw", false)) {
			Settings.cpuGpu = Settings.CpuGpu.GPU;
		} else {
			Settings.cpuGpu = Settings.CpuGpu.CPU;
		}

		// 图像格式
		Settings.colorFix = false;// sp.getBoolean("color_fix", false);
		// 触摸反馈
		Settings.showTouches = sp.getBoolean("show_touches", false);

		if (sp.getBoolean("use_debug", false)) {

			// 屏幕大小
			String size = sp.getString("save_format", getRString("fm_orig_size"));
			if (getRString("fm_orig_size").equals(size)) // 原始大小
			{
				DisplayMetrics dm = context.getResources().getDisplayMetrics();
				Settings.width = dm.widthPixels;
				Settings.height = dm.heightPixels;
			} else // 自定义大小
			{
				String[] ss = size.split("x"); // 以乘号分割
				Settings.width = Integer.valueOf(ss[0]);
				Settings.height = Integer.valueOf(ss[1]);
			}

			// 帧率
			Settings.fps = sp.getInt("video_fps", 15);

			// 编码
			Settings.encoder = EnumUtils.ordinalOf(Settings.VideoEncoder.class, Integer.valueOf(sp.getString("video_encoder", "0")));
			// 视频音频质量
			Settings.videoQuality = EnumUtils.ordinalOf(Settings.VideoQuality.class, Integer.valueOf(sp.getString("video_quality", "0")));
			Settings.audioQuality = EnumUtils.ordinalOf(Settings.AudioQuality.class, Integer.valueOf(sp.getString("audio_quality", "0")));
		}

		else {

			// 编码
			Settings.encoder = EnumUtils.ordinalOf(Settings.VideoEncoder.class, Integer.valueOf("0"));

			String mode = sp.getString("quality_of_video", ExApplication.SQuality);

			if (mode.equals("流畅")) {
				/*
				 * Settings.fps = 12; DisplayMetrics dm =
				 * context.getResources().getDisplayMetrics(); Settings.width =
				 * (int)((dm.widthPixels)/2.5); Settings.height =
				 * (int)((dm.heightPixels)/2.5);
				 * 
				 * Settings.videoQuality =
				 * EnumUtils.ordinalOf(Settings.VideoQuality.class,
				 * Integer.valueOf("0")); Settings.audioQuality =
				 * EnumUtils.ordinalOf(Settings.AudioQuality.class,
				 * Integer.valueOf("1"));
				 */
				Settings.fps = 12;

				DisplayMetrics dm = context.getResources().getDisplayMetrics();
				Settings.width = (int) (dm.widthPixels / 2);
				Settings.height = (int) (dm.heightPixels / 2);

				Settings.videoQuality = EnumUtils.ordinalOf(Settings.VideoQuality.class, Integer.valueOf("2"));
				Settings.audioQuality = EnumUtils.ordinalOf(Settings.AudioQuality.class, Integer.valueOf("2"));
			} else if (mode.equals(ExApplication.SQuality)) {
				/*
				 * Settings.fps = 15;
				 * 
				 * DisplayMetrics dm =
				 * context.getResources().getDisplayMetrics(); Settings.width =
				 * (int)(dm.widthPixels/2); Settings.height =
				 * (int)(dm.heightPixels/2);
				 * 
				 * Settings.videoQuality =
				 * EnumUtils.ordinalOf(Settings.VideoQuality.class,
				 * Integer.valueOf("2")); Settings.audioQuality =
				 * EnumUtils.ordinalOf(Settings.AudioQuality.class,
				 * Integer.valueOf("2"));
				 */
				Settings.fps = 15;
				DisplayMetrics dm = context.getResources().getDisplayMetrics();
				Settings.width = (int) (dm.widthPixels / 1.7);
				Settings.height = (int) (dm.heightPixels / 1.7);

				Settings.videoQuality = EnumUtils.ordinalOf(Settings.VideoQuality.class, Integer.valueOf("3"));
				Settings.audioQuality = EnumUtils.ordinalOf(Settings.AudioQuality.class, Integer.valueOf("2"));

			} else if (mode.equals(ExApplication.HQuality)) {
				Settings.fps = 15;
				DisplayMetrics dm = context.getResources().getDisplayMetrics();
				Settings.width = (int) (dm.widthPixels / 1.4);
				Settings.height = (int) (dm.heightPixels / 1.4);

				Settings.videoQuality = EnumUtils.ordinalOf(Settings.VideoQuality.class, Integer.valueOf("3"));
				Settings.audioQuality = EnumUtils.ordinalOf(Settings.AudioQuality.class, Integer.valueOf("2"));
			}

		}

	}

	private void showTransparentToastInUI() {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
//				handler.sendEmptyMessage(0);
                h.postAtFrontOfQueue(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < 50; i++) {
                            showTransparentToast(ScreenRECService.this);
                        }
                    }
                });
			}
		});
		thread.setPriority(1);
		thread.setDaemon(true);
		thread.start();

	}

	private void endScreenRecord() {
		if (null != thread) {
			thread.interrupt();
		}
		thread = null;
		stopRecording();

		/*
		 * Intent itt = new Intent(); itt.putExtra("out_file", outFile);
		 * itt.setClass(ScreenRECService.this, EndRecordAlertDialog.class);
		 * itt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(itt);
		 */

		Intent intent = new Intent(RecordAction.ACTION);
		intent.putExtra("action", "open_record_file");
		intent.putExtra("out_file", outFile);
		sendBroadcast(RecordAction.getActionIntent("open_record_file_action"));

	}

	// 视频合成成功与否的吐司提示
	private void open_record_file_action() {

		if (outFile != null) {
			int fileSize = (int) FileUtil.getFileSize(new File(outFile));
			if (fileSize > 512) {

				Message msg = new Message();
				// 视频文件路径
				msg.obj = outFile;
				msg.what = 2;
				// 发送消息准备吐司通知用户录制成功
				RecordVideo.mHandler.sendMessage(msg);
			} else {

				// 发送消息准备吐司通知用户录制失败
				RecordVideo.mHandler.sendEmptyMessage(3);

			}
		}

	}

	private void openFile1(String fileName) {
		if (null != fileName && new File(fileName).exists()) {
			openFile(new File(fileName));
		}
		NotificationManager mNotificationManager = (NotificationManager) AppManager.getInstance().getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(getRDrawableID("icon"));
		restartService();
	}

	private void share_record_file_to_friend(Intent itt) {
		String fileName = itt.getStringExtra("file_path");
		Intent intent = createShareIntent(new File(fileName));
		intent = Intent.createChooser(intent, getRString("fm_slelct_to_share"));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		NotificationManager mNotificationManager = (NotificationManager) AppManager.getInstance().getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(getRDrawableID("icon"));
		restartService();
	}

	private void open_record_file_without_alert_preview(String fileName) {
		openFile1(fileName);
	}

	private void open_record_file_without_alert_dont_show_again() {
		final SharedPreferences sp = SharedPreferencesUtils.getMinJieKaiFaPreferences(ScreenRECService.this);
		sp.edit().putBoolean("do_not_show_cant_play_dialog", true).commit();
	}

	private void open_record_file_without_alert(final String fileName) {

		openFile1(fileName);
		// }
		// voteAndComment();
	}

	private Intent createShareIntent(File file) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/html");
		Uri uri = Uri.fromFile(file);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		shareIntent.putExtra(Intent.EXTRA_EMAIL, RUtils.getRString("fm_share_to_email_title"));
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, RUtils.getRString("fm_share_to_email_title"));
		shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(RUtils.getRString("fm_share_def_content")));
		return shareIntent;
	}

	private void restartService() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0: {
					// UtilService.stopService(ExApplication.Get());
					break;
				}
				case 1: {
					// UtilService.startService(ExApplication.Get());
					break;
				}
				case 2: {
					check_show_notify_icon();
					break;
				}
				}
			}
		};
		handler.sendEmptyMessageDelayed(0, 100);
		handler.sendEmptyMessageDelayed(1, 500);
		handler.sendEmptyMessageDelayed(2, 700);
	}

	private void check_show_notify_icon() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		boolean hide_notify = sp.getBoolean("hide_notify", false);
		if (!hide_notify) {
			// showScreenshotNotification();
		} else {
			NotificationManager mNotificationManager = (NotificationManager) AppManager.getInstance().getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel(getRXmlID("screenshot_setting"));
		}
	}

	/**
	 * 摇晃监听
	 * */
	public void onShake() {
		if (yaohuangjieping) {

			// 如果两次摇晃时间过短，不作处理，防止重复操作
			if (System.currentTimeMillis() - lastShake <= 5000) {

				return;
			}
			lastShake = System.currentTimeMillis();
			sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			boolean sfv = sp.getBoolean("no_float_view_record", false);
			if (ScreenRecordActivity.startFloatService == true || sfv) {

				if ((RecordVideo.isStart == false && RecordVideo.isStop == true)) {

					// 显示通知栏
					RecordVideo.RecordStateNofity("录屏大师开始录屏", getApplicationContext());
					if (recordVideo == null) {
						recordVideo = new RecordVideo(getApplicationContext());
					}

					if (ScreenRecordActivity.getAndroidSDKVersion() == 19) {
						// 这里同时也兼应用第一次录屏时设置清晰度（根据android版本不同设置不同默认配置，4.4以下是标准，4.4以上是高清）
						videoMode = sp.getString("quality_of_video", ExApplication.HQuality);

						// 打开其他配置
						openOthreConfigure();

						recordVideo.stardRecordVideo();

					} else if (ScreenRecordActivity.SDKVesion < 19) {
						// 这里同时也兼应用第一次录屏时设置清晰度（根据android版本不同设置不同默认配置，4.4以下是标准，4.4以上是高清）
						videoMode = sp.getString("quality_of_video", ExApplication.SQuality);
						// 打开其他配置
						openOthreConfigure();
						on_shake_screenshot_action();

					} else if (ScreenRecordActivity.SDKVesion >= 21) {
						// 这里同时也兼应用第一次录屏时设置清晰度（根据android版本不同设置不同默认配置，4.4以下是标准，4.4以上是高清）
						videoMode = sp.getString("quality_of_video", ExApplication.HQuality);
						// 打开其他配置
						openOthreConfigure();
						ExApplication.mConfiguration = ScreenRecordActivity.getConfiguration();

                        ScreenRecord50.showNewTask(true);
					}
					if (!sfv) {// 如果开启了悬浮窗选项
						// 显示悬浮窗
                        FloatViewManager.getInstance().Show2AndRemove1();
					} else {
						// 修改首页样式
						if (sp.getBoolean("horizontalRecord", true)) {
//							ScreenRecordActivity.handler.sendEmptyMessage(7);
                            ScreenRecordActivity.sendEmptyMessage(7);
						} else {
//							ScreenRecordActivity.handler.sendEmptyMessage(8);
                            ScreenRecordActivity.sendEmptyMessage(8);
						}
					}
					// FloatView1.handler.sendEmptyMessage(5);
				} else if (RecordVideo.isStop == false && RecordVideo.isStart == true) {

					RecordVideo.RecordStateNofity("录屏大师停止录屏", getApplicationContext());
					RecordVideo.isStart = false;
					RecordVideo.isStop = true;
					RecordVideo.isRecordering = false;

					if (ScreenRecordActivity.SDKVesion == 19) {
						if (recordVideo == null) {
							recordVideo = new RecordVideo(getApplicationContext());
						}
						recordVideo.StopRecordForVesion19();
						if (sfv) {// 如果开启无浮窗模式录制
//							ScreenRecordActivity.handler.sendEmptyMessage(9);
                            ScreenRecordActivity.sendEmptyMessage(9);
						} else {
                            FloatViewManager.getInstance().Show1AndRemove2();
						}
					} else if (ScreenRecordActivity.SDKVesion < 19) {
						recordVideo.StopRecordForVesionOther();
						if (sfv) {// 如果开启无浮窗模式录制
//							ScreenRecordActivity.handler.sendEmptyMessage(9);
                            ScreenRecordActivity.sendEmptyMessage(9);

						} else {
                            FloatViewManager.getInstance().Show1AndRemove2();
						}
					} else if (ScreenRecordActivity.SDKVesion >= 21) {
						if (sfv) {// 如果开启无浮窗模式录制
//							ScreenRecordActivity.handler.sendEmptyMessage(9);
                            ScreenRecordActivity.sendEmptyMessage(9);
						}
						// 停止录屏，返回浮窗
						ScreenRecord50.stopRecord();
						ScreenRecord50.startRecoing = false;
					}

					// FloatView2.handler.sendEmptyMessage(5);
					// 关闭触摸显示
					android.provider.Settings.System.putInt(getApplication().getContentResolver(), "show_touches", 0);
					// 关闭画中画
					if (ExApplication.floatCameraClose == false) {
						CameraView.closeFloatView();

					}
					// 关闭水印
					// FloatView2.removeWater();
				}
			}
		}
	}

    // 录屏时打开其他配置
	private void openOthreConfigure() {
		FloatView2.doubleclick = false;
		// 将当前清晰度配置赋给全局变量
		ExApplication.videoQuality = videoMode;

		// 如果打开了设置页中的触摸选项，录屏时显示触摸点
		boolean isShowTouch = sp.getBoolean("show_touch_view", false);
		if (isShowTouch) {

			android.provider.Settings.System.putInt(getApplication().getContentResolver(), "show_touches", 1);
		}
		// 如果打开了前置摄像头选项
		boolean isfloatCamera = sp.getBoolean("show_front_camera", false);
		if (isfloatCamera) {
			// 开启画中画
			Intent intent = new Intent(mContextScreenService, FrontCameraService.class);
			mContextScreenService.startService(intent);
			ExApplication.floatCameraClose = false;

		}

	}

	private void on_shake_screenshot_action() {
		Intent intent = new Intent(RecordAction.ACTION);
		intent.putExtra("action", "screen_shot");
		sendBroadcast(intent);
	}

	private void start_yaohuangjieping_service() {
		yaohuangjieping = true;
	}

	private void end_yaohuangjieping_service() {
		yaohuangjieping = false;
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	private void hideScreenshotNotification() {
		NotificationManager mNotificationManager = (NotificationManager) AppManager.getInstance().getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(getRXmlID("screenshot_setting"));
	}

	private void openFile(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(file);
		String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
		intent.setDataAndType(uri, type == null ? "*/*" : type);
		Intent it = Intent.createChooser(intent, getRString("fm_open_using"));
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(it);
	}

	private File getOutputFile() {
		File localFile = new File(getImageStoreDir());
		if ((!localFile.exists()) && (!localFile.mkdirs())) {
			Log.w("RecorderService", "mkdirs failed " + localFile.getAbsolutePath());
		}

		ScreenRecordActivity.videofilename = StorageUtil.createRecName();
		return new File(localFile, ScreenRecordActivity.videofilename);
	}

	private String getImageStoreDir() {
		SharedPreferences sp = SharedPreferencesUtils.getMinJieKaiFaPreferences(this);
		String path = sp.getString("image_store_dir", SYSJStorageUtil.getSysj().getAbsolutePath()).trim();
		if (path.endsWith("/"))
			return path;
		return path + "/";
	}

	private void alert_no_auth_get_pro_version() {
	}

	private String getRString(String name) {
		return RUtils.getRString(name);
	}

	private int getRID(String name) {
		return RUtils.getRID(name);
	}

	private int getRLayout(String name) {
		return RUtils.getRLayoutID(name);
	}

	private int getRDrawableID(String name) {
		return RUtils.getRDrawableID(name);
	}

	private int getRXmlID(String name) {
		return RUtils.getRXmlID(name);
	}

	private int getRRawID(String name) {
		return RUtils.getRRawID(name);
	}

	private void floatViewControl() {

	}

}