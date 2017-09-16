package com.li.videoapplication.tools;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.li.videoapplication.R;
import com.li.videoapplication.component.service.DownloadService;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.utils.NetUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * 写入文件管理类
 */
public class DownloadHelper {

    private static final String TAG = "DownloadHelper";

    public static void downloadFile(final Context context, final Download download) {
        if (!NetUtil.isConnect()) {
            ToastHelper.s(R.string.net_disable);
        } else if (NetUtil.isWIFI()) {
            // 开始, 继续下载
            DownloadService.startDownloadService(download);
        } else {
            // 文件下载
            DialogManager.showFileDownloaderDialog(context,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 开始, 继续下载
                            DownloadService.startDownloadService(download);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // WIFI下载
                            context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
        }
    }

    public static boolean writeResponseBody2Disk(ResponseBody body, String downloadName) {
        String path = SYSJStorageUtil.getSysjDownload() + File.separator + downloadName;
        Log.i(TAG, "startToWrite.path:" + path);

        File futureFile = new File(path);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        long fileSize = body.contentLength();
        Log.d(TAG, "fileSize:" + fileSize);

        try {
            try {
                byte[] fileReader = new byte[1024 * 1024];
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }

    }
}
