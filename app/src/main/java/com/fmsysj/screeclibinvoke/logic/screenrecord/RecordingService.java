package com.fmsysj.screeclibinvoke.logic.screenrecord;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.fmsysj.screeclibinvoke.data.model.configuration.RecordingSetting;
import com.fmsysj.screeclibinvoke.data.observe.ObserveManager;
import com.fmsysj.screeclibinvoke.logic.floatview.FloatViewManager;
import com.fmsysj.screeclibinvoke.logic.frontcamera.FrontCameraManager;
import com.fmsysj.screeclibinvoke.logic.notification.RecordingNotificationManager;
import com.fmsysj.screeclibinvoke.logic.videoedit.ShakeHelper;
import com.fmsysj.screeclibinvoke.logic.videoedit.SoundHelper;
import com.fmsysj.screeclibinvoke.logic.videoedit.TouchHelper;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.ifeimo.screenrecordlib.constant.Configuration;
import com.ifeimo.screenrecordlib.listener.RecordingListener;
import com.ifeimo.screenrecordlib.util.Utils;
import com.li.videoapplication.R;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.data.database.VideoCaptureManager;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseService;
import com.li.videoapplication.tools.IntentHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.AppUtil;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务：录屏服务
 */
public class RecordingService extends BaseService implements
        ShakeHelper.OnShakeListener,
        RecordingListener {

    public static final String TAG = RecordingService.class.getSimpleName();

    public static final String MSG = "msg";
    public static final String FLAG = "flag";

    public static final int SHOW_FLOAT_VIEW = 1001;
    public static final int SHOW_FLOAT_VIEW_2 = 1002;
    public static final int SHOW_FLOAT_CONTENT_VIEW = 1003;
    public static final int SHOW_FLOAT_CONTENT_VIEW_2 = 1004;
    public static final int REMOVE_FLOAT_CURRENT_VIEW = 1005;
    public static final int TOOGLE_FLOAT_VIEW = 1006;
    public static final int DESTROY_FLOAT_VIEW = 1007;

    public static final int OPEN_FRONT_CAMERA = 2001;
    public static final int CLOSE_FRONT_CAMERA = 2002;

    public static final int START_SCREEN_CAPTURE = 4001;
    public static final int START_SCREEN_RECORD = 4002;
    public static final int STOP_SCREEN_RECORD = 4003;
    public static final int PAUSE_SCREEN_RECORD = 4004;
    public static final int RESUME_SCREEN_RECORD = 4005;

    public static final int OPEN_SHAKE = 5001;
    public static final int CLOSE_SHAKE = 5002;

    /*****************************  摇晃 ***************************/
    /*****************************  摇晃 ***************************/
    /*****************************  摇晃 ***************************/
    /*****************************  摇晃 ***************************/

    /**
     * 打开摇晃
     */
    public static void openShake() {
        startRecordingService(OPEN_SHAKE);
    }

    /**
     * 关闭摇晃
     */
    public static void closeShake() {
        startRecordingService(CLOSE_SHAKE);
    }

    /*****************************  浮窗 ***************************/
    /*****************************  浮窗 ***************************/
    /*****************************  浮窗 ***************************/
    /*****************************  浮窗 ***************************/

    /**
     * 隐藏与显示悬浮窗
     */
    public static void toogleFloatView(boolean flag) {
        startRecordingService(TOOGLE_FLOAT_VIEW, flag);
    }

    /**
     * 销毁浮窗
     */
    public static void destroyFloatView() {
        startRecordingService(DESTROY_FLOAT_VIEW);
    }

    /**
     * 显示浮窗1
     */
    public static void showFloatView() {
        startRecordingService(SHOW_FLOAT_VIEW);
    }

    /**
     * 显示浮窗2
     */
    public static void showFloatView2() {
        startRecordingService(SHOW_FLOAT_VIEW_2);
    }

    /**
     * 显示浮窗详情1
     */
    public static void showFloatContentView() {
        startRecordingService(SHOW_FLOAT_CONTENT_VIEW);
    }

    /**
     * 显示浮窗详情2
     */
    public static void showFloatContentView2() {
        startRecordingService(SHOW_FLOAT_CONTENT_VIEW_2);
    }

    /**
     * 移除默认浮窗
     */
    public static void removeFloatCurrentView() {
        startRecordingService(REMOVE_FLOAT_CURRENT_VIEW);
    }

    /***************************** 前置摄像头 ***************************/
    /***************************** 前置摄像头 ***************************/
    /***************************** 前置摄像头 ***************************/
    /***************************** 前置摄像头 ***************************/

    /**
     * 打开前置摄像头
     */
    public static void openFrontCamera() {
        startRecordingService(OPEN_FRONT_CAMERA);
    }

    /**
     * 关闭前置摄像头
     */
    public static void closeFrontCamera() {
        startRecordingService(CLOSE_FRONT_CAMERA);
    }

    /***************************** 录屏 ***************************/
    /***************************** 录屏 ***************************/
    /***************************** 录屏 ***************************/
    /***************************** 录屏 ***************************/

    /**
     * 开始录屏
     */
    public static void startScreenCapture() {
        startRecordingService(START_SCREEN_CAPTURE);
    }

    /**
     * 截屏
     */
    public static void startScreenRecord() {
        startRecordingService(START_SCREEN_RECORD);
    }

    /**
     * 停止录屏
     */
    public static void stopScreenRecord() {
        startRecordingService(STOP_SCREEN_RECORD);
    }

    /**
     * 暂停录屏
     */
    public static void pauseScreenRecord() {
        startRecordingService(PAUSE_SCREEN_RECORD);
    }

    /**
     * 继续录屏
     */
    public static void resumeScreenRecord() {
        startRecordingService(RESUME_SCREEN_RECORD);
    }

    /***************************** 启动/停止服务 ***************************/
    /***************************** 启动/停止服务 ***************************/
    /***************************** 启动/停止服务 ***************************/
    /***************************** 启动/停止服务 ***************************/

    /**
     * 启动录屏服务
     */
    public static void startRecordingService() {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent();
        intent.setClass(context, RecordingService.class);
        context.startService(intent);
    }

    /**
     * 启动录屏服务
     */
    private static void startRecordingService(int msg) {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent();
        if (msg != 0)
            intent.putExtra(MSG, msg);
        intent.setClass(context, RecordingService.class);
        context.startService(intent);
    }

    /**
     * 启动录屏服务
     */
    private static void startRecordingService(int msg, boolean flag) {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent();
        if (msg != 0)
            intent.putExtra(MSG, msg);
        intent.putExtra(FLAG, flag);
        intent.setClass(context, RecordingService.class);
        context.startService(intent);
    }

    /**
     * 停止录屏服务
     */
    public static void stopRecordingService() {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent();
        intent.setClass(context, RecordingService.class);
        context.stopService(intent);
    }

    /***************************** 线程 ***************************/
    /***************************** 线程 ***************************/
    /***************************** 线程 ***************************/
    /*****************************
     * 线程
     ***************************/

    private Handler h = new Handler(Looper.getMainLooper());

    private void _removeHandlers() {
        if (h != null)
            h.removeCallbacksAndMessages(null);
    }

    private String looperThreadName = "LoopThread-RecordingService";
    private HandlerThread mThread, nThread;
    // private Handler mHandler, nHandler;
    private Handler mHandler = new Handler(Looper.getMainLooper()), nHandler = new Handler(Looper.getMainLooper());

    private void _initLooperThread() {
        Log.d(TAG, "_initLooperThread: // -----------------------------------------------------------");
/*
        // TODO: 2016/6/29  此处不要提高线程优先级，否则容易引起严重卡顿
		if (mThread == null) {
			mThread = new HandlerThread(looperThreadName, Process.THREAD_PRIORITY_LOWEST);
			mThread.start();
			mHandler = new Handler(mThread.getLooper());
		}

		// TODO: 2016/6/29 此处不要提高线程优先级，否则容易引起严重卡顿
		if (nThread == null) {
			nThread = new HandlerThread(looperThreadName, Process.THREAD_PRIORITY_LOWEST);
			nThread.start();
			nHandler = new Handler(nThread.getLooper());
		}*/
    }

    private void _destroyLooperThread() {
        Log.d(TAG, "_destroyLooperThread: // -----------------------------------------------------------");
        if (nThread != null)
            if (nThread.isAlive())
                try {
                    nThread.quit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        if (mThread != null)
            if (mThread.isAlive())
                try {
                    mThread.quit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    private int index;

    private void newThread(Runnable r) {
        if (r != null) {
            new Thread(r, "newThread-RecordingService-" + (++index)).start();
        }
    }

    /***************************** 生命周期 ***************************/
    /***************************** 生命周期 ***************************/
    /***************************** 生命周期 ***************************/
    /*****************************
     * 生命周期
     ***************************/

    private ShakeHelper helper;
    private long lastShakeMillis = System.currentTimeMillis();

    @Override
    public void onShake() {
        Log.d(TAG, "onShake: // -----------------------------------------------------------");
        if (PreferencesHepler.getInstance().getRecordingSetting().isShakeRecording()) {
            // 如果两次摇晃时间过短，不作处理，防止重复操作
            if (System.currentTimeMillis() - lastShakeMillis <= 5000) {
                return;
            }
            lastShakeMillis = System.currentTimeMillis();
            // 应用处于前台且非录屏状态，不作处理
            if (!RecordingManager.getInstance().isRecording() &&
                    AppUtil.isAppOnForeground(AppManager.getInstance().getContext()))
                return;
            // 显示浮窗，或者是无浮窗模式
            if (FloatViewManager.getInstance().isShow() ||
                    !PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws()) {
                if (!RecordingManager.getInstance().isRecording()) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && !Utils.root()) {
                        ToastHelper.s(R.string.root_content);
                        return;
                    }
                    _startScreenRecord();
                    Log.d(TAG, "onShake: true");
                } else {
                    _stopScreenRecord();
                    Log.d(TAG, "onShake: true");
                }
            }
        }
    }

    private void _startForeground() {
        Log.d(TAG, "_startForeground: // -----------------------------------------------------------");
        startForeground(RecordingNotificationManager.NOTIFICATION_RECORDING,
                RecordingNotificationManager.getInstance().notification);
    }

    private void _stopForeground() {
        Log.d(TAG, "_stopForeground: // -----------------------------------------------------------");
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "**************************************************************************");
        Log.d(TAG, "**************************************************************************");
        Log.d(TAG, "**************************************************************************");
        Log.d(TAG, "**************************************************************************");
        _initLooperThread();

        helper = new ShakeHelper(this);
        helper.setOnShakeListener(this);

        if (PreferencesHepler.getInstance().getRecordingSetting().isShakeRecording() == true) {
            _openShake();
        } else {
            _closeShake();
        }

        _showNotification();

        RecordingManager.getInstance().listener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int msg = intent.getIntExtra(MSG, 0);
            boolean flag = intent.getBooleanExtra(FLAG, true);
            if (msg == DESTROY_FLOAT_VIEW) {
                _destroyFloatView();
            } else if (msg == TOOGLE_FLOAT_VIEW) {
                _toogleFloatView(flag);
            } else if (msg == SHOW_FLOAT_VIEW) {
                _showFloatView();
            } else if (msg == SHOW_FLOAT_VIEW_2) {
                _showFloatView2();
            } else if (msg == SHOW_FLOAT_CONTENT_VIEW) {
                _showFloatContentView();
            } else if (msg == SHOW_FLOAT_CONTENT_VIEW_2) {
                _showFloatContentView2();
            } else if (msg == REMOVE_FLOAT_CURRENT_VIEW) {
                _removeFloatCurrentView();
            } else if (msg == OPEN_FRONT_CAMERA) {
                _openFrontCamera();
            } else if (msg == CLOSE_FRONT_CAMERA) {
                _closeFrontCamera();
            } else if (msg == START_SCREEN_CAPTURE) {
                _startScreenCapture();
            } else if (msg == START_SCREEN_RECORD) {
                if (RecordingManager.getInstance().isRecording() == false) {
                    _startScreenRecord();
                }
            } else if (msg == STOP_SCREEN_RECORD) {
                if (RecordingManager.getInstance().isRecording() == true) {
                    _stopScreenRecord();
                }
            } else if (msg == PAUSE_SCREEN_RECORD) {
                _pauseScreenRecord();
            } else if (msg == RESUME_SCREEN_RECORD) {
                _resumeScreenRecord();
            } else if (msg == OPEN_SHAKE) {
                _openShake();
            } else if (msg == CLOSE_SHAKE) {
                _closeShake();
            }
        }
        _startForeground();
        return START_NOT_STICKY;
    }

    public void onDestroy() {
        RecordingManager.getInstance().listener(null);
        _closeShake();
        if (RecordingManager.getInstance().isRecording() == true) {
            _stopScreenRecord();
        }
        _closeFrontCamera();
        _removeFloatCurrentView();
        _stopForeground();
        _destroyNotification();
        _destroyFloatView();
        super.onDestroy();

        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                _removeHandlers();
                _destroyLooperThread();
            }
        }, 1200);
    }

    private int orientation;

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(tag, "onConfigurationChanged: newConfig=" + newConfig);
        orientation = resources.getConfiguration().orientation;
        Log.d(TAG, "onConfigurationChanged: orientation=" + orientation);
        if (orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    FloatViewManager.getInstance().isLandscape = false;
                    FloatViewManager.getInstance().toPortrait();
                    FrontCameraManager.getInstance().toPortrait();
                }
            });
        } else if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    FloatViewManager.getInstance().isLandscape = true;
                    FloatViewManager.getInstance().toLandscape();
                    FrontCameraManager.getInstance().toLandscape();
                }
            });
        }
    }

    /*****************************  浮窗 ***************************/
    /*****************************  浮窗 ***************************/
    /*****************************  浮窗 ***************************/
    /*****************************  浮窗 ***************************/

    /**
     * 隐藏与显示悬浮窗
     */
    public void _toogleFloatView(final boolean flag) {
        Log.d(TAG, "_showFloatView: // -----------------------------------------------------------");
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                FloatViewManager.getInstance().toogleView(flag);
            }
        });
    }

    /**
     * 显示浮窗1
     */
    public void _showFloatView() {
        Log.d(TAG, "_showFloatView: // -----------------------------------------------------------");
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                FloatViewManager.getInstance().showView();
            }
        });
    }

    /**
     * 显示浮窗2
     */
    public void _showFloatView2() {
        Log.d(TAG, "_showFloatView2: // -----------------------------------------------------------");
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                FloatViewManager.getInstance().showView2();
            }
        });
    }

    /**
     * 显示浮窗详情1
     */
    public void _showFloatContentView() {
        Log.d(TAG, "_showFloatContentView: // -----------------------------------------------------------");
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                FloatViewManager.getInstance().showContentView();
            }
        });
    }

    /**
     * 显示浮窗详情2
     */
    public void _showFloatContentView2() {
        Log.d(TAG, "_showFloatContentView2: // -----------------------------------------------------------");
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                FloatViewManager.getInstance().showContentView2();
            }
        });
    }

    /**
     * 移除默认浮窗
     */
    public void _removeFloatCurrentView() {
        Log.d(TAG, "_removeFloatCurrentView: // -----------------------------------------------------------");
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                FloatViewManager.getInstance().removeCurrentView();
            }
        });
    }

    /**
     * 销毁浮窗
     */
    public void _destroyFloatView() {
        Log.d(TAG, "_destroyFloatView: // -----------------------------------------------------------");
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                FloatViewManager.destruction();
            }
        });
    }

    /***************************** 前置摄像头 ***************************/
    /***************************** 前置摄像头 ***************************/
    /***************************** 前置摄像头 ***************************/
    /***************************** 前置摄像头 ***************************/

    /**
     * 打开前置摄像头
     */
    public void _openFrontCamera() {
        Log.d(TAG, "_openFrontCamera: // -----------------------------------------------------------");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // 打开前置摄像头
                FrontCameraManager.getInstance().show();

                h.post(new Runnable() {
                    @Override
                    public void run() {

                        ToastHelper.s(R.string.recording_frontcamera_open);
                    }
                });
            }
        }, 300);

        nHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // 发布前置摄像头事件
                ObserveManager.getInstance().notifyFrontCameraObservable();
            }
        }, 300 + 600);
    }

    /**
     * 关闭前置摄像头
     */
    public void _closeFrontCamera() {
        Log.d(TAG, "_closeFrontCamera: // -----------------------------------------------------------");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // 关闭前置摄像头
                FrontCameraManager.getInstance().dismiss();
            }
        }, 300);

        nHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // 发布前置摄像头事件
                ObserveManager.getInstance().notifyFrontCameraObservable();
            }
        }, 300 + 600);
    }

    /***************************** 计时 ***************************/
    /***************************** 计时 ***************************/
    /***************************** 计时 ***************************/
    /*****************************
     * 计时
     ***************************/

    private AtomicInteger single = new AtomicInteger(0);

    @Override
    public void onRecordingTiming() {
        h.post(new Runnable() {
            @Override
            public void run() {

                if (RecordingManager.getInstance().isRecording()) {
                    // 发布录屏事件（录屏中）
                    ObserveManager.getInstance().notifyRecordingObservable();
                    /*
					if (single.get() % 2 == 0) {
						doTiming();
					}
					single.incrementAndGet();*/
                }
            }
        });
    }

    private void doTiming() {
        long secs = RecordingManager.getInstance().secs();
        if (secs != 0) {
            if (RecordingManager.getInstance().isRecording() &&
                    !AppUtil.isAppOnForeground(AppManager.getInstance().getContext())) {// 录屏中，应用在后台
                if (secs % 60 == 0) {// 60s执行一次
                    // 销毁所有资源（服务，调度器，下载器）
//					Utils_Framework.destroyAllResouce();
                    Log.d(TAG, "doTiming: destroyAllResouce...");

                    String time = RecordingManager.getInstance().time();
                    Log.d(TAG, "doTiming: time=" + time);
                }
                if (secs % 60 == 0) {// 60s执行一次
                    // 移除任务栈
//					Utils_Framework.finishApp();
                    Log.d(TAG, "doTiming: finishApp...");

                    String time = RecordingManager.getInstance().time();
                    Log.d(TAG, "doTiming: time=" + time);
                }
            }
			/*
			if (secs % 60 == 0) {// 60s执行一次
				//垃圾回收
				System.gc();
				Log.d(TAG, "doTiming: gc");

				String time = RecordingManager.getInstance().time();
				Log.d(TAG, "doTiming: time=" + time);
			}*/
        }
    }

    /***************************** 录屏 ***************************/
    /***************************** 录屏 ***************************/
    /***************************** 录屏 ***************************/
    /***************************** 录屏 ***************************/

    /**
     * 截屏
     */
    private void _startScreenCapture() {
        Log.d(TAG, "_startScreenCapture: // -----------------------------------------------------------");

        newThread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final File file = SYSJStorageUtil.createPicturePath();
                if (file == null)
                    return;
                // 截屏
                RecordingManager.getInstance().startScreenCapture(file.getPath());
                // 播放截屏声效
                SoundHelper.playCapture();

                nHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // 发布截屏事件
                        EventManager.postScreenShotEvent();
                    }
                }, 2800);

                nHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // 发布截屏事件
                        EventManager.postScreenShotEvent();
                    }
                }, 12800);
            }
        });
    }

    @Override
    public void onCapturingCompleted() {
        Log.d(TAG, "onCapturingCompleted: // -----------------------------------------------------------");
        if (!RecordingManager.getInstance().isRecording()) {
            ToastHelper.s(R.string.capturing_success);
        }
    }

    private String tmpPath;

    /**
     * 开始录屏
     */
    private void _startScreenRecord() {
        _showNotification();
        Log.d(TAG, "_startScreenRecord: // -----------------------------------------------------------");

        if (Build.VERSION.SDK_INT >= 21) {
            if (!AppUtil.isAppOnForeground(AppManager.getInstance().getContext())) {
                // 移除任务栈
//                Utils_Framework.finishTask();
            }
        } else {
            if (AppUtil.isAppBackground()) {
                // 移除任务栈
//				Utils_Framework.finishTask();
            }
        }

        newThread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final File tmpFile = SYSJStorageUtil.createTmpRecPath();
                if (tmpFile == null)
                    return;
                tmpPath = tmpFile.getPath();
                RecordingSetting setting = PreferencesHepler.getInstance().getRecordingSetting();
                Configuration.Quality quality;
                switch (setting.getQuality()) {
                    case RecordingSetting.QUALITY_LOW:
                        quality = Configuration.Quality.STANDARD;
                        break;
                    case RecordingSetting.QUALITY_STANDARD:
                        quality = Configuration.Quality.STANDARD;
                        break;
                    case RecordingSetting.QUALITY_HIGH:
                        quality = Configuration.Quality.HIGH;
                        break;
                    case RecordingSetting.QUALITY_ULTRA_HIGH:
                        quality = Configuration.Quality.ULTRAHIGH;
                        break;
                    default:
                        quality = Configuration.Quality.HIGH;
                        break;
                }

                boolean lanscape = false;
                if (setting.getLandscape() == 1) {
                    lanscape = true;
                }

                final Configuration configuration = new Configuration.Builder()
                        .audio(setting.isSoundRecording())
                        .landscape(lanscape)
                        .quality(quality)
                        .build();

                // 开始录屏
                RecordingManager.getInstance().startScreenRecord(tmpPath, configuration);

                if (PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws()) {
                } else {
                }

                if (PreferencesHepler.getInstance().getRecordingSetting().isShakeRecording()) {
                }
            }
        });
    }

    @Override
    public void onRecordingStarted() {
        Log.d(TAG, "onRecordingStarted: // -----------------------------------------------------------");

        if (PreferencesHepler.getInstance().getRecordingSetting().isTouchPosition()) {
            // 设置触摸显示
            TouchHelper.toogle(true);
        } else {
            TouchHelper.toogle(false);
        }

        if (!AppUtil.isAppOnForeground(AppManager.getInstance().getContext())) {// 应用处于后台

            // 销毁所有资源（服务，调度器，下载器）
            //Utils_Data.destroyAllResouce();

            System.gc();
        }

        nHandler.post(new Runnable() {
            @Override
            public void run() {

                // 发布录屏事件（开始，停止，暂停，继续）
                ObserveManager.getInstance().notifyRecording2Observable();
            }
        });
    }

    @Override
    public void onRecordingStarted2() {
        Log.d(TAG, "onRecordingStarted2: // -----------------------------------------------------------");
        if (!AppUtil.isAppOnForeground(AppManager.getInstance().getContext())) {
            // 移除任务栈
//			Utils_Framework.finishApp();
        }

        System.gc();
    }

    /**
     * 停止录屏
     */
    private void _stopScreenRecord() {
        Log.d(TAG, "_stopScreenRecord: // -----------------------------------------------------------");

        // 停止录屏
        RecordingManager.getInstance().stopScreenRecord();
    }

    @Override
    public void onRecordingStoped() {
        Log.d(TAG, "onRecordingStoped: // -----------------------------------------------------------");
        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },500);
        newThread(new Runnable() {
            @Override
            public void run() {

                // 通知系统扫描媒体文件
                IntentHelper.scanFile();
                // 关闭触摸显示
                TouchHelper.toogle(false);

                FloatViewManager.getInstance().isFloatingWindiws = PreferencesHepler.getInstance().getRecordingSetting().isFloatingWindiws();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        // 关闭前置摄像头
                        FrontCameraManager.getInstance().dismiss();
                    }
                });

                nHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        // 发布录屏事件（开始，停止，暂停，继续）
                        ObserveManager.getInstance().notifyRecording2Observable();
                    }
                });

                // --------------------------------------------------------------------------

                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // --------------------------------------------------------------------------

                final File tmpFile = new File(tmpPath);
                if (tmpFile == null) {
                    h.post(new Runnable() {
                        public void run() {
                            ToastHelper.s(R.string.recording_failure);
                        }
                    });
                    return;
                }


                final File file = SYSJStorageUtil.createRecPath();
                String path = null;
                try {
                    path = file.getPath();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                boolean flag = false;
                try {
                    flag = FileUtil.moveFile(tmpPath, path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (flag) {
                    tmpPath = null;
                    // 保存进数据库
                    if (FileUtil.isFile(path)) {
                        VideoCaptureManager.save(path,
                                VideoCaptureEntity.VIDEO_SOURCE_REC,
                                VideoCaptureEntity.VIDEO_STATION_LOCAL);
                    }

                    h.post(new Runnable() {
                        @Override
                        public void run() {

                            ToastHelper.s(R.string.recording_success);
                        }
                    });

                    if (PreferencesHepler.getInstance().getRecordingSetting().isRecordedJump()) {
                        final String p = path;

                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (PreferencesHepler.getInstance().isLogin()) {
                                    //录屏后跳转分享
                                    VideoCaptureEntity videoCaptureEntity = VideoCaptureManager.findByPath(p);

                                    ActivityManager.startVideoShareActivity210NewTask(AppManager.getInstance().getContext(),
                                            videoCaptureEntity);
                                }

                            }
                        }, 1200);
                    }

                } else {
                    h.post(new Runnable() {
                        public void run() {
                            ToastHelper.s(R.string.recording_failure);
                        }
                    });
                }

                // --------------------------------------------------------------------------

                nHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        // 发布录屏事件（开始，停止，暂停，继续）
                        ObserveManager.getInstance().notifyRecording2Observable();
                    }
                });

                nHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // 发布录屏事件（完成）
                        EventManager.postVideoCaptureEvent();
                    }
                }, 1600);
            }
        });
    }

    @Override
    public void onRecordingCompleted() {
        Log.d(TAG, "onRecordingCompleted: // -----------------------------------------------------------");

        nHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // 发布录屏事件（开始，停止，暂停，继续）
                ObserveManager.getInstance().notifyRecording2Observable();
            }
        }, 800);
    }

    /**
     * 暂停录屏
     */
    private void _pauseScreenRecord() {
        Log.d(TAG, "_pauseScreenRecord: // -----------------------------------------------------------");

        newThread(new Runnable() {
            @Override
            public void run() {

                // 暂停录屏
                RecordingManager.getInstance().pauseScreenRecord();

                nHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // 发布录屏事件（开始，停止，暂停，继续）
                        ObserveManager.getInstance().notifyRecording2Observable();
                    }
                }, 600);

            }
        });
    }

    @Override
    public void onRecordingPaused() {
        Log.d(TAG, "onRecordingPaused: // -----------------------------------------------------------");
    }

    /**
     * 继续录屏
     */
    private void _resumeScreenRecord() {
        Log.d(TAG, "_resumeScreenRecord: // -----------------------------------------------------------");

        newThread(new Runnable() {
            @Override
            public void run() {

                // 继续录屏
                RecordingManager.getInstance().resumeScreenRecord();

                nHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // 发布录屏事件（开始，停止，暂停，继续）
                        ObserveManager.getInstance().notifyRecording2Observable();
                    }
                }, 600);
            }
        });
    }

    @Override
    public void onRecordingResumed() {
        Log.d(TAG, "onRecordingResumed: // -----------------------------------------------------------");
    }

    /***************************** 通知 ***************************/
    /***************************** 通知 ***************************/
    /***************************** 通知 ***************************/
    /*****************************
     * 通知
     ***************************/

    private void _showNotification() {
        Log.d(TAG, "_showNotification: // -----------------------------------------------------------");
        RecordingNotificationManager.getInstance().refreshRemoteViews();
        RecordingNotificationManager.getInstance().refreshRemoteViews2();
        RecordingNotificationManager.getInstance().showNotification();
    }

    private void _destroyNotification() {
        Log.d(TAG, "_destroyNotification: // -----------------------------------------------------------");
        RecordingNotificationManager.destruction();
    }

    /***************************** 摇晃 ***************************/
    /***************************** 摇晃 ***************************/
    /***************************** 摇晃 ***************************/
    /*****************************
     * 摇晃
     ***************************/

    private void _openShake() {
        if (helper != null) {
            Log.d(TAG, "_openShake: // -----------------------------------------------------------");
            helper.resume();
        }
    }

    private void _closeShake() {
        if (helper != null) {
            Log.d(TAG, "_closeShake: // -----------------------------------------------------------");
            helper.pause();
        }
    }
}