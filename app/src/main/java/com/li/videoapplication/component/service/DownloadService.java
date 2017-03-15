package com.li.videoapplication.component.service;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseIntentService;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 通知栏更新下载apk
 *
 * 参数：download_url(xxxxx.apk), title
 */
public class DownloadService extends BaseIntentService {

    private static final String TAG = DownloadService.class.getSimpleName();
    public static final int HANDLE_DOWNLOAD = 0x001;
    /**
     * 系统下载管理器
     */
    private DownloadManager dm;
    private Download download;
    private long downloadId;//下载任务ID
    private DownloadChangeObserver downloadObserver;//下载进度监听
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 启动服务
     */
    public static void startDownloadService(Download download) {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra("download", download);
        try {
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ToastHelper.s("正在下载...");
    }

    public Handler downLoadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            Log.d(TAG, "handleMessage: msg = " + msg);
            if (HANDLE_DOWNLOAD == msg.what) {
                //被除数可以为0，除数必须大于0
                if (msg.arg1 >= 0 && msg.arg2 > 0) {
                    for (DownloadService.OnProgressListener listener : listeners) {
                        listener.onDownloadProgress(msg.arg1 / (float) msg.arg2);
                    }
                    //下载完成:已下载大小=总大小
                    if (msg.arg1 == msg.arg2) {
                        unregisterContentObserver();
                        close();
                    }
                }
            }
            super.handleMessage(msg);
        }
    };

    public interface OnProgressListener {
        /**
         * 下载进度
         *
         * @param fraction 已下载/总大小
         */
        void onDownloadProgress(float fraction);
    }

    /**
     * 回调
     */
    private static List<DownloadService.OnProgressListener> listeners = new ArrayList<>();

    public static void addListeners(DownloadService.OnProgressListener listener) {
        synchronized (DownloadService.class) {
            if (listener != null && !listeners.contains(listener))
                listeners.add(listener);
        }
    }

    public static void removeListeners(DownloadService.OnProgressListener listener) {
        synchronized (DownloadService.class) {
            if (listener != null)
                listeners.remove(listener);
        }
    }

    public DownloadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        download = (Download) intent.getSerializableExtra("download");
        /*开始下载*/
        startDownload();
    }

    private void startDownload() {
        Log.d(TAG, "startDownload: ");
        //DOWNLOAD/SYSJ_xxx.apk
        String path = Environment.DIRECTORY_DOWNLOADS + File.separator +
                "SYSJ_" + StringUtil.getFileNameWithExt(download.getDownload_url());
        boolean deleteFile = FileUtil.deleteFile(path);
        Log.d(TAG, "startDownload: path = " + path);
        Log.d(TAG, "startDownload: deleteFile = " + deleteFile);

        /*获得系统下载器*/
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        /*获得下载监听器*/
        downloadObserver = new DownloadChangeObserver();
        //注册下载监听
        registerContentObserver();

        /*设置下载地址*/
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(download.getDownload_url()));
        //显示在下载界面，即下载后的文件在下载管理里显示
        request.setVisibleInDownloadsUi(true);
        /*设置下载文件的类型*/
        request.setMimeType("application/vnd.android.package-archive");
        /*设置下载存放的文件夹和文件名字  SYSJ_xxx.apk*/
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                "SYSJ_" + StringUtil.getFileNameWithExt(download.getDownload_url()));
        /*设置下载时或者下载完成时，通知栏是否显示*/
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(download.getTitle());
        /*执行下载，并返回任务唯一id*/
        downloadId = dm.enqueue(request);
    }

    /**
     * 监听下载进度
     */
    private class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver() {
            super(downLoadHandler);
            Log.d(TAG, "DownloadChangeObserver: ");
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        /**
         * 当所监听的Uri发生改变时，就会回调此方法
         * 为了提高性能，在这里开启定时任务，每1秒去查询数据大小并发送到handle中更新UI。
         *
         * @param selfChange 此值意义不大, 一般情况下该回调值false
         */
        @Override
        public void onChange(boolean selfChange) {
            Log.d(TAG, "onChange: ");
            scheduledExecutorService.scheduleAtFixedRate(progressRunnable, 0, 1, TimeUnit.SECONDS);
        }
    }

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    /**
     * 发送Handler消息更新进度和状态
     */
    private void updateProgress() {
        Log.d(TAG, "updateProgress: ");
        int[] bytesAndStatus = getBytesAndStatus(downloadId);
        downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD,
                bytesAndStatus[0], bytesAndStatus[1], bytesAndStatus[2]));
    }

    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     *
     * @param downloadId id
     * @return int[已经下载文件大小,总大小,下载状态]
     */
    private int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{
                -1, -1, 0
        };
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = dm.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //下载状态
                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 下载任务服务销毁");
    }

    /**
     * 注册ContentObserver
     */
    private void registerContentObserver() {
        /* observer download change **/
        if (downloadObserver != null) {
            getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"),
                    false, downloadObserver);
        }
    }

    /**
     * 注销ContentObserver
     */
    private void unregisterContentObserver() {
        if (downloadObserver != null) {
            getContentResolver().unregisterContentObserver(downloadObserver);
        }
    }

    /**
     * 关闭定时器，线程等操作
     */
    private void close() {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }

        if (downLoadHandler != null) {
            downLoadHandler.removeCallbacksAndMessages(null);
        }
    }
}
