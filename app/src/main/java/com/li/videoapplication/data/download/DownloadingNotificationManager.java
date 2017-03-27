package com.li.videoapplication.data.download;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.li.videoapplication.R;
import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.network.RequestUtil;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.ApkUtil;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.ThreadUtil;
import com.li.videoapplication.utils.URLUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务：下载通知栏
 */
public class DownloadingNotificationManager {

    private static final String TAG = DownloadingNotificationManager.class.getSimpleName();

    private static DownloadingNotificationManager instance;

    public static DownloadingNotificationManager getInstance() {
        if (instance == null)
            synchronized (DownloadingNotificationManager.class) {
                if (instance == null)
                    instance = new DownloadingNotificationManager();
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

    public final static String ACTION_BUTTON = "com.li.videoapplication.DownloadingNotificationManager";
    public final static String EXTRA_URL = "extraUrl";
    public final static String EXTRA_BUTTON = "extraButton";

    /**
     * logo
     */
    public final static int EXTRA_BUTTON_LOGO = 1;

    /**
     * 进度数字
     */
    public final static int EXTRA_BUTTON_TEXT = 2;

    /**
     * 根布局
     */
    public final static int EXTRA_BUTTON_ROOT = 10;

    private Context context;
    private Resources resources;
    private NotificationManager manager;
    private Map<String, RemoteViews> remoteViewses = new HashMap<>();
    private Map<String, Notification> notifications = new HashMap<>();
    private Map<String, FileDownloaderEntity> entities = new HashMap<>();
    private String packageName;
    private Receiver receiver;
    private Handler handler;

    private DownloadingNotificationManager() {
        super();

        context = AppManager.getInstance().getContext();
        resources = context.getResources();
        packageName = context.getPackageName();
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        handler = new Handler(Looper.getMainLooper());

        registerReceiver();
    }

    /**
     * 销毁通知栏
     */
    private void destroy() {
        cancelAllNotification();
        unregisterReceiver();
        closeNotification();
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
    }

    // -------------------------------------------------------------------------------------

    private long count = 0;

    /**
     * 更新通知栏
     */
    public void updateNotification(String fileUrl, FileDownloaderEntity entity) {
        if (fileUrl == null)
            return;
        if (entity == null)
            return;
        Log.d(TAG, "updateNotification: // ----------------------------------------------------");
        ++ count;
        Log.d(TAG, "updateNotification: count=" + count);
        entities.put(fileUrl, entity);
        RemoteViews remoteViews = remoteViewses.get(fileUrl);
        Notification notification = notifications.get(fileUrl);
        if (remoteViews == null || notification == null) {
            showNotification(fileUrl, entity);
        } else {
            refreshRemoteVews(remoteViews, entity);
        }
        notification = notifications.get(fileUrl);
        notifyNotification(findId(fileUrl), notification);
    }

    private void showNotification(String fileUrl, FileDownloaderEntity entity) {
        if (!URLUtil.isURL(fileUrl))
            return;
        Log.d(TAG, "showNotification: // ----------------------------------------------------");
        RemoteViews remoteViews = new RemoteViews(packageName, R.layout.view_downloadingnotification);

        int size = ScreenUtil.dp2px(5);
        remoteViews.setViewPadding(R.id.root, 0, size, 0, size);

        Intent intent = new Intent();
        intent.setAction(ACTION_BUTTON);
        PendingIntent p;
        // logo
        intent.putExtra(EXTRA_URL, fileUrl);
        intent.putExtra(EXTRA_BUTTON, EXTRA_BUTTON_LOGO);
        p = PendingIntent.getBroadcast(context, EXTRA_BUTTON_LOGO, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.downloadingnotification_logo, p);
        // 进度数字
        intent.putExtra(EXTRA_URL, fileUrl);
        intent.putExtra(EXTRA_BUTTON, EXTRA_BUTTON_TEXT);
        p = PendingIntent.getBroadcast(context, EXTRA_BUTTON_TEXT, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.downloadingnotification_text, p);
        // 根布局
        intent.putExtra(EXTRA_URL, fileUrl);
        intent.putExtra(EXTRA_BUTTON, EXTRA_BUTTON_ROOT);
        p = PendingIntent.getBroadcast(context, EXTRA_BUTTON_ROOT, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.root, p);

        Intent i = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, Notification.FLAG_ONGOING_EVENT);

        refreshRemoteVews(remoteViews, entity);

        Notification notification = new NotificationCompat.Builder(context)
                .setContent(remoteViews)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setSmallIcon(R.drawable.logo_round)
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        remoteViewses.put(fileUrl, remoteViews);
        notifications.put(fileUrl, notification);
    }

    private void notifyNotification(int id, Notification notification) {
        if (notification == null)
            return;
        Log.d(TAG, "notifyNotification: // ----------------------------------------------------");
        try {
            manager.notify(id, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshRemoteVews(RemoteViews remoteViews, FileDownloaderEntity entity) {
        if (remoteViews == null)
            return;
        if (entity == null)
            return;
        Log.d(TAG, "refreshRemoteVews: // ----------------------------------------------------");
        refreshLogo(remoteViews, entity);
        if (entity.getDownloadSize() > 0) {// 正在下载
            remoteViews.setTextViewText(R.id.downloadingnotification_title, "正在下载" + entity.getApp_name());
        } else {
            remoteViews.setTextViewText(R.id.downloadingnotification_title, "准备下载" + entity.getApp_name());
        }
        remoteViews.setTextViewText(R.id.downloadingnotification_content, entity.getApp_intro());
        if (entity.isDownloading()) {// 下载中-->暂停
            Log.d(TAG, "refreshRemoteVews: isDownloading");
            remoteViews.setTextViewText(R.id.downloadingnotification_title, "正在下载" + entity.getApp_name());
            remoteViews.setTextViewText(R.id.downloadingnotification_text, entity.getProgress() + "%");
        } else if (entity.isPausing()) {// 暂停-->继续
            Log.d(TAG, "refreshRemoteVews: isPausing");
            remoteViews.setTextViewText(R.id.downloadingnotification_title, entity.getApp_name() + "暂停中");
            remoteViews.setTextViewText(R.id.downloadingnotification_text, string(R.string.applicationdownload_resume));
        } else if (entity.isInstalled()) {// 已安装-->打开
            Log.d(TAG, "refreshRemoteVews: isInstalled");
            remoteViews.setTextViewText(R.id.downloadingnotification_title, entity.getApp_name() + "安装完成");
            remoteViews.setTextViewText(R.id.downloadingnotification_text, string(R.string.applicationdownload_open));
        } else if (entity.isDownloaded()) {// 已下载-->安装
            Log.d(TAG, "setRight: isDownloaded");
            remoteViews.setTextViewText(R.id.downloadingnotification_title, entity.getApp_name() + "下载完成");
            remoteViews.setTextViewText(R.id.downloadingnotification_text, string(R.string.applicationdownload_install));
        } else {// -->下载
            Log.d(TAG, "setRight: download");
            remoteViews.setTextViewText(R.id.downloadingnotification_title, "准备下载" + entity.getApp_name());
            remoteViews.setTextViewText(R.id.downloadingnotification_text, string(R.string.applicationdownload_download));
        }
    }

    private long time = 0;

    private void refreshLogo(final RemoteViews remoteViews, final FileDownloaderEntity entity) {
        Log.d(TAG, "refreshLogo: // ----------------------------------------------------");
        if (time <= 1) {
            remoteViews.setImageViewResource(R.id.downloadingnotification_logo, R.drawable.logo_round);
            ThreadUtil.start(new Runnable() {
                @Override
                public void run() {
                    final Bitmap b = RequestUtil.getBitmap(entity.getFlag());
                    if (b != null) {
                        handler.post(new Runnable() {
                            public void run() {
                                remoteViews.setImageViewBitmap(R.id.downloadingnotification_logo, b);
                                ++ time;
                                Log.d(TAG, "refreshLogo: time=" + time);
                            }
                        });
                    }
                }
            });
        }
    }

    private String string(int res) {
        return resources.getString(res);
    }

    private int findId(String fileUrl) {
        if (fileUrl == null)
            return -1;
        FileDownloaderEntity entity = entities.get(fileUrl);
        if (entity == null)
            return -1;
        Log.d(TAG, "findId: " + entity.getId());
        return entity.getId();
    }

    /**
     * 清除通知栏
     */
    public void cancelNotification(String fileUrl) {
        if (manager != null) {
            Log.d(TAG, "cancel: fileUrl=" + fileUrl);
            try {
                manager.cancel(findId(fileUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清除通知栏
     */
    private void cancelNotification(int id) {
        if (manager != null) {
            Log.d(TAG, "cancel: id=" + id);
            manager.cancel(id);
        }
    }

    /**
     * 清除所有通知栏
     */
    public void cancelAllNotification() {
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
            collapse = statusBarManager.getClass().getMethod("collapsePanels");
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
            String fileUrl = intent.getStringExtra(EXTRA_URL);
            FileDownloaderEntity entity = entities.get(fileUrl);
            int id = intent.getIntExtra(EXTRA_BUTTON, 0);
            Notification notification = notifications.get(fileUrl);
            if (entity == null)
                return;
            if (notification == null)
                return;
            Log.d(TAG, "onReceive: // ----------------------------------------------------");
            Log.d(TAG, "onReceive: action=" + action);
            Log.d(TAG, "onReceive: fileUrl=" + fileUrl);
            Log.d(TAG, "onReceive: notification=" + notification);
            Log.d(TAG, "onReceive: id=" + id);
            if (action.equals(ACTION_BUTTON)) {
                switch (id) {

                    case EXTRA_BUTTON_LOGO:// logo

                        break;

                    case EXTRA_BUTTON_TEXT:// 进度数字
                        File apkFile = SYSJStorageUtil.createApkPath(fileUrl);
                        if (apkFile == null)
                            return;
                        if (ApkUtil.isAvilible(ApkUtil.getPackageName(apkFile.getPath()))) {// 应用已安装
                            // 启动应用
                            ApkUtil.launchApp(context, ApkUtil.getPackageName(apkFile.getPath()));
                            // 广告点击统计（16——打开游戏）
//                            DataManager.advertisementAdClick204_16(entity.getAd_id());
                        } else if (apkFile.exists()) {// 文件已存在
                            // 安装应用
                            ApkUtil.installApp(context, apkFile.getPath());
                            // 广告点击统计（15——启动安装）
//                            DataManager.advertisementAdClick204_15(entity.getAd_id());
                        } else {
//                            ActivityManager.startMainActivityNewTask(-1, 0);
                            ActivityManeger.startDownloadManagerActivity(context);
                        }
                        break;

                    case EXTRA_BUTTON_ROOT:// 根布局
                        // 主页（哪一个页卡）
//                        ActivityManager.startMainActivityNewTask(-1, 0);
                        ActivityManeger.startDownloadManagerActivity(context);
                        break;
                }
                closeNotification();
            }
        }
    }
}
