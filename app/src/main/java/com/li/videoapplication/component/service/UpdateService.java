package com.li.videoapplication.component.service;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseIntentService;
import com.li.videoapplication.tools.ToastHelper;

import java.io.File;

/**
 * 通知栏更新下载apk
 */
public class UpdateService extends BaseIntentService {

    private static final String TAG = UpdateService.class.getSimpleName();
    /**
     * 系统下载管理器
     */
    private DownloadManager dm;
    /**
     * 系统下载器分配的唯一下载任务id，可以通过这个id查询或者处理下载任务
     */


    private Update update;

    /**
     * 启动服务
     */
    public static void startUpdateService(Update update) {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra("update", update);
        try {
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ToastHelper.s("正在下载...");
    }

    public UpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        update = (Update) intent.getSerializableExtra("update");
        /**开始下载*/
        startDownload();
    }

    private void startDownload() {
        Log.d(TAG, "startDownload: ");
        String path = Environment.DIRECTORY_DOWNLOADS + File.separator +
                "SYSJ_" + update.getVersion_str() + "_" + update.getBuild() + ".apk";
        boolean deleteFile = FileUtil.deleteFile(path);
        Log.d(TAG, "startDownload: path = " + path);
        Log.d(TAG, "startDownload: deleteFile = " + deleteFile);

        /**获得系统下载器*/
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        /**设置下载地址*/
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(update.getUpdate_url()));
        //显示在下载界面，即下载后的文件在下载管理里显示
        request.setVisibleInDownloadsUi(true);
        /**设置下载文件的类型*/
        request.setMimeType("application/vnd.android.package-archive");
        /**设置下载存放的文件夹和文件名字  SYSJ_V2.1.2_20161212.apk*/
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                "SYSJ_" + update.getVersion_str() + "_" + update.getBuild() + ".apk");
        /**设置下载时或者下载完成时，通知栏是否显示*/
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("手游视界" + update.getVersion_str());
        /**执行下载，并返回任务唯一id*/
        dm.enqueue(request);
    }
}
