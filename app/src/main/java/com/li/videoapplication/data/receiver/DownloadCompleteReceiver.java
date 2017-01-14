package com.li.videoapplication.data.receiver;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.li.videoapplication.framework.BaseReceiver;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.utils.ApkUtil;

/**
 * 广播：监听下载完成
 */
public class DownloadCompleteReceiver extends BaseReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            Log.d(tag, "onReceive: ----- ACTION_DOWNLOAD_COMPLETE -----");
            /**当系统下载完成，就提示安装*/
            ApkUtil.installAPK(context, intent);

        } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {
            Log.d(tag, "onReceive: ----- ACTION_NOTIFICATION_CLICKED -----");
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            //点击通知栏取消下载
            manager.remove(ids);
            ToastHelper.s("下载已取消");
        }
    }
}
