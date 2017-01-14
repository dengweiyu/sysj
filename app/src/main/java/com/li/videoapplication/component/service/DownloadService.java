package com.li.videoapplication.component.service;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseIntentService;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.StringUtil;

import java.io.File;

/**
 * 通知栏更新下载apk
 *
 * 参数：download_url(xxxxx.apk), title
 */
public class DownloadService extends BaseIntentService {

    private static final String TAG = DownloadService.class.getSimpleName();
    /**
     * 系统下载管理器
     */
    private DownloadManager dm;

    private Download download;

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

    public DownloadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        download = (Download) intent.getSerializableExtra("download");
        /**开始下载*/
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

        /**获得系统下载器*/
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        /**设置下载地址*/
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(download.getDownload_url()));
        //显示在下载界面，即下载后的文件在下载管理里显示
        request.setVisibleInDownloadsUi(true);
        /**设置下载文件的类型*/
        request.setMimeType("application/vnd.android.package-archive");
        /**设置下载存放的文件夹和文件名字  SYSJ_xxx.apk*/
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                "SYSJ_" + StringUtil.getFileNameWithExt(download.getDownload_url()));
        /**设置下载时或者下载完成时，通知栏是否显示*/
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(download.getTitle());
        /**执行下载，并返回任务唯一id*/
        dm.enqueue(request);
    }
}
