package com.li.videoapplication.data.receiver;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;

import com.li.videoapplication.framework.BaseReceiver;
import com.li.videoapplication.utils.ApkUtil;

/**
 * 广播：监听下载完成
 */
public class DownloadCompleteReceiver extends BaseReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            ApkUtil.openFile(intent,context);
        }
    }
}
