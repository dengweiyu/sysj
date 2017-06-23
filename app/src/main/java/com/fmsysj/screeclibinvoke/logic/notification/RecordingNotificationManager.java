package com.fmsysj.screeclibinvoke.logic.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.fmsysj.screeclibinvoke.ui.activity.ScreenRecordActivity;
import com.ifeimo.screenrecordlib.RecordingManager;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.ScreenUtil;
import com.fmsysj.screeclibinvoke.data.observe.ObserveManager;
import com.fmsysj.screeclibinvoke.data.observe.listener.FrontCameraObservable;
import com.fmsysj.screeclibinvoke.data.observe.listener.Recording2Observable;
import com.fmsysj.screeclibinvoke.logic.frontcamera.FrontCameraManager;
import com.fmsysj.screeclibinvoke.logic.screenrecord.RecordingService;

import java.lang.reflect.Method;

/**
 * 服务：录屏通知栏
 */
public class RecordingNotificationManager implements
        Recording2Observable,
        FrontCameraObservable{

    private static final String TAG = RecordingNotificationManager.class.getSimpleName();

    private static RecordingNotificationManager instance;

    public static RecordingNotificationManager getInstance() {
        if (instance == null)
            synchronized (RecordingNotificationManager.class) {
                if (instance == null)
                    instance = new RecordingNotificationManager();
            }
        return instance;
    }

    /**
     * 销毁通知栏
     */
    public static void destruction() {
        if (instance != null)
            instance.destroy();
        instance = null;
    }

    // -------------------------------------------------------------------------------------

    public final static String ACTION_BUTTON = "com.screeclibinvoke.RecordingNotificationManager";
    public final static String EXTRA_BUTTON = "extraButton";
    /** 返回主页 ID */
    public final static int EXTRA_BUTTON_MAIN = 1;
    /** 前置摄像头 ID */
    public final static int EXTRA_BUTTON_FRONTCAMERA = 2;
    /** 截图ID */
    public final static int EXTRA_BUTTON_SCREENCAPTURE = 3;
    /** 录屏ID */
    public final static int EXTRA_BUTTON_START = 4;
    /** 暂停 */
    public final static int EXTRA_BUTTON_STOP = 5;

    public static final int NOTIFICATION_RECORDING = 200;

    private Context context;
    private NotificationManager manager;
    private RemoteViews remoteViews;
    public Notification notification;
    private String packageName;
    private Receiver receiver;
    private Handler handler;

    private RecordingNotificationManager() {
        super();
        ObserveManager.getInstance().addRecording2Observable(this);
        ObserveManager.getInstance().addFrontCameraObservable(this);

        context = AppManager.getInstance().getContext();
        packageName = context.getPackageName();
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        handler = new Handler(Looper.myLooper());

        initRemoteViews();
        refreshRemoteViews();
        refreshRemoteViews2();
        registerReceiver();
    }

    /**
     * 销毁通知栏
     */
    private void destroy() {
        cancel(NOTIFICATION_RECORDING);
        ObserveManager.getInstance().removeRecording2Observable(this);
        ObserveManager.getInstance().removeFrontCameraObservable(this);
        unregisterReceiver();
        closeNotification();
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
        instance = null;
    }

    // -------------------------------------------------------------------------------------

    private void initRemoteViews() {
        remoteViews = new RemoteViews(packageName, R.layout.view_recordingnotification);
        if (Build.VERSION.SDK_INT >= 16) {
            int size = ScreenUtil.dp2px(5);
            remoteViews.setViewPadding(R.id.root, 0, size, 0, size);
        }

        Intent intent = new Intent();
        intent.setAction(ACTION_BUTTON);
        PendingIntent p;
        // 主页
        intent.putExtra(EXTRA_BUTTON, EXTRA_BUTTON_MAIN);
        p = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.recordingnotification_main, p);
        // 前置摄像头
        intent.putExtra(EXTRA_BUTTON, EXTRA_BUTTON_FRONTCAMERA);
        p = PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.recordingnotification_frontcamera, p);
        // 截屏
        intent.putExtra(EXTRA_BUTTON, EXTRA_BUTTON_SCREENCAPTURE);
        p = PendingIntent.getBroadcast(context, 3, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.recordingnotification_screencapture, p);
        // 开始，停止
        intent.putExtra(EXTRA_BUTTON, EXTRA_BUTTON_START);
        p = PendingIntent.getBroadcast(context, 4, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.recordingnotification_start, p);
        // 暂停，继续
        intent.putExtra(EXTRA_BUTTON, EXTRA_BUTTON_STOP);
        p = PendingIntent.getBroadcast(context, 5, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.recordingnotification_stop, p);
    }

    /**
     * 回调：发布录屏事件（开始，停止，暂停，继续）
     */
    @Override
    public void onRecording2() {
        handler.post(new Runnable() {
            @Override
            public void run() {

                refreshRemoteViews();
                updateNotification();
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

                refreshRemoteViews2();
                updateNotification();
            }
        });
    }

    private int recordMark, frontCameraMark;

    public void refreshRemoteViews() {
        Log.d(TAG, "refreshRemoteViews: ");
        if (remoteViews == null)
            return;
        if (RecordingManager.getInstance().isRecording()) {
            if (RecordingManager.getInstance().isPausing()) {// 录屏暂停中
                if (recordMark != 1) {
                    remoteViews.setViewVisibility(R.id.recordingnotification_stop,
                            android.view.View.VISIBLE);
                    remoteViews.setImageViewResource(R.id.recordingnotification_start,
                            R.drawable.recordingnotification_resume);
                    recordMark = 1;
                    Log.d(TAG, "refreshRemoteViews: recordMark=" + recordMark);
                }
            } else {// 录屏中
                if (recordMark != 2) {
                    remoteViews.setViewVisibility(R.id.recordingnotification_stop,
                            android.view.View.VISIBLE);
                    remoteViews.setImageViewResource(R.id.recordingnotification_start,
                            R.drawable.recordingnotification_pause);
                    recordMark = 2;
                    Log.d(TAG, "refreshRemoteViews: recordMark=" + recordMark);
                }
            }
        } else {// 不在录屏中
            if (recordMark != 3) {
                remoteViews.setViewVisibility(R.id.recordingnotification_stop,
                        android.view.View.GONE);
                remoteViews.setImageViewResource(R.id.recordingnotification_start,
                        R.drawable.recordingnotification_start);
                recordMark = 3;
                Log.d(TAG, "refreshRemoteViews: recordMark=" + recordMark);
            }
        }
    }

    public void refreshRemoteViews2() {
        Log.d(TAG, "refreshRemoteViews2: ");
        if (remoteViews == null)
            return;
        if (FrontCameraManager.getInstance().isOpen()) {// 前置摄像头打开
            if (frontCameraMark != 1) {
                remoteViews.setImageViewResource(R.id.recordingnotification_frontcamera,
                        R.drawable.floatview_frontcamera_black);
                frontCameraMark = 1;
                Log.d(TAG, "refreshRemoteViews2: frontCameraMark=" + frontCameraMark);
            }
        } else {// 前置摄像头关闭
            if (frontCameraMark != 2) {
                remoteViews.setImageViewResource(R.id.recordingnotification_frontcamera,
                        R.drawable.floatview_frontcamera_gray);
                frontCameraMark = 2;
                Log.d(TAG, "refreshRemoteViews2: frontCameraMark=" + frontCameraMark);
            }
        }
    }

    public void showNotification() {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(), Notification.FLAG_ONGOING_EVENT);

        notification = new NotificationCompat.Builder(context)
                .setContent(remoteViews)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setSmallIcon(R.drawable.logo_round)
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        manager.notify(NOTIFICATION_RECORDING, notification);

    }

    public void updateNotification() {
        manager.notify(NOTIFICATION_RECORDING, notification);
    }

    /**
     * 清除通知栏
     */
    public void cancel(int id) {
        if (manager != null) {
            Log.d(TAG, "cancel: id=" + id);
            manager.cancel(id);
        }
    }

    /**
     * 清除通知栏
     */
    public void cancelAll() {
        if (manager != null) {
            manager.cancelAll();
        }
    }

    /**
     * 收起通知栏
     */
    private void closeNotification() {
        try {
            Object statusBarManager = context.getSystemService("statusbar");
            Method collapse;
            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_BUTTON);
        context.registerReceiver(receiver, filter);
    }

    /**
     * 反注册广播
     */
    private void unregisterReceiver() {
        if (receiver != null)
            try {
                context.unregisterReceiver(receiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
    }

    /**
     * 监听按钮广播
     */
    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int id = intent.getIntExtra(EXTRA_BUTTON, 0);
            if (action.equals(ACTION_BUTTON)) {
                switch (id) {

                    case EXTRA_BUTTON_MAIN:// 主页
                        main(context);
                        break;

                    case EXTRA_BUTTON_FRONTCAMERA:// 前置摄像头
                        toogleFrontCamera();
                        break;

                    case EXTRA_BUTTON_SCREENCAPTURE:// 截屏
                        startScreenCapture();
                        break;

                    case EXTRA_BUTTON_START:// 开始，暂停，继续
                        toogleScreenRecord();
                        break;

                    case EXTRA_BUTTON_STOP:// 停止
                        stopScreenRecord();
                        break;
                }
                closeNotification();
            }
        }
    }

    /**
     * 主页
     */
    private void main(Context context) {
        // 主页
        ScreenRecordActivity.startScreenRecordActivity(context);
    }

    /**
     * 前置摄像头
     */
    private void toogleFrontCamera() {
        Log.d(TAG, "toogleFrontCamera: ");
        if (FrontCameraManager.getInstance().isOpen()) {// 前置摄像头打开
            RecordingService.closeFrontCamera();
        } else {// 前置摄像头关闭
            RecordingService.openFrontCamera();
        }
    }

    /**
     * 开始，暂停，继续录屏
     */
    private void toogleScreenRecord() {
        if (RecordingManager.getInstance().isRecording()) {
            if (RecordingManager.getInstance().isPausing()) {// 录屏暂停中
                // 继续录屏
                RecordingService.resumeScreenRecord();
            } else {// 录屏中
                // 暂停录屏
                RecordingService.pauseScreenRecord();
            }
        } else {// 不在录屏中
            // 开始录屏
            RecordingService.startScreenRecord();
        }
    }

    /**
     * 停止录屏
     */
    private void stopScreenRecord() {
        if (RecordingManager.getInstance().isRecording()) {// 录屏中
            // 停止录屏
            RecordingService.stopScreenRecord();
        }
    }

    /**
     * 截屏
     */
    private void startScreenCapture() {
        // 截屏
        RecordingService.startScreenCapture();
    }
}
