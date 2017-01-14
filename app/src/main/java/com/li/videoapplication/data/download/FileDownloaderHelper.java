package com.li.videoapplication.data.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.data.database.FileDownloaderManager;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.framework.AppManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 功能：加载下载文件
 */
public class FileDownloaderHelper {

    protected final String tag = this.getClass().getSimpleName();
    protected final String action = this.getClass().getName();

    private Context context;

    private String fileType;
    private List<FileDownloaderEntity> data;
    private boolean result;
    private String msg;

    /**
     * 回调
     */
    private void postEvent(boolean result, String msg, List<FileDownloaderEntity> data, String fileType) {
        EventManager.postFileDownloaderResponseObject(result, msg, data, fileType);
    }

    /**
     * 消息处理
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message message) {
            postEvent(result, msg, data, fileType);
            super.handleMessage(message);
        }
    };

    public FileDownloaderHelper() {
        super();

        data = new ArrayList<>();
        context = AppManager.getInstance().getContext();
        result = false;
    }

    /**
     * 初始化下载文件（ 飞磨）
     */
    public static void initFeimo() {
        RequestExecutor.start(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    // -----------------------------------------------------------------------------------------

    /**
     * 新线程加载下载文件（广告）
     */
    public void loadAdvertisement() {
        fileType = FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT;
        RequestExecutor.start(new Runnable() {
            @Override
            public void run() {

                _loadAdvertisement();
                result = true;
                msg = "加载下载文件完成";
                handler.sendEmptyMessage(0);

                Log.d(tag, "run: result=" + result);
                Log.d(tag, "run: data=" + data);
            }
        });
    }

    /**
     * 加载下载文件（广告）
     */
    private void _loadAdvertisement() {
        data.clear();
        List<FileDownloaderEntity> list = FileDownloaderManager.findAll();
        if (list != null && list.size() > 0) {
            Iterator<FileDownloaderEntity> iterator = list.iterator();
            while (iterator.hasNext()) {
                FileDownloaderEntity entity = iterator.next();
                if (entity != null && entity.getFileType().equals(FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT)) {
                    data.add(entity);
                }
            }
        }
    }

    /**
     * 新线程加载下载文件（ 飞磨）
     */
    public void loadFeimo() {
        fileType = FileDownloaderEntity.FILE_TYPE_FEIMO;
        RequestExecutor.start(new Runnable() {
            @Override
            public void run() {

                _loadFeimo();
                result = true;
                msg = "加载下载文件完成";
                handler.sendEmptyMessage(0);

                Log.d(tag, "run: result=" + result);
                Log.d(tag, "run: data=" + data);
            }
        });
    }

    /**
     * 加载下载文件（ 飞磨）
     */
    private void _loadFeimo() {
        data.clear();
        List<FileDownloaderEntity> list = FileDownloaderManager.findAll();
        if (list != null && list.size() > 0) {
            Iterator<FileDownloaderEntity> iterator = list.iterator();
            while (iterator.hasNext()) {
                FileDownloaderEntity entity = iterator.next();
                if (entity != null && entity.getFileType().equals(FileDownloaderEntity.FILE_TYPE_FEIMO)) {
                    data.add(entity);
                }
            }
        }
    }
}
