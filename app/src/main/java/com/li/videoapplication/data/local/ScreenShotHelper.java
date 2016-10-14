package com.li.videoapplication.data.local;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.network.LightTask;
import com.li.videoapplication.data.preferences.DefaultPreferences;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：加载截图
 */
public class ScreenShotHelper {

    protected final String tag = this.getClass().getSimpleName();
    protected final String action = this.getClass().getName();

    private Context context;

    private List<ScreenShotEntity> data;
    private boolean result;
    private String msg;

    /**
     * 回调
     */
    private void postEvent(boolean result, String msg, List<ScreenShotEntity> data) {
        EventManager.postScreenShotResponseObject(result, msg, data);
    }

    /**
     * 消息处理
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message message) {
            postEvent(result, msg, data);
            super.handleMessage(message);
        }
    };

    public ScreenShotHelper() {
        super();

        data = new ArrayList<>();
        context = AppManager.getInstance().getContext();
        result = false;
    }

    /**
     * 新线程加载截图
     */
    public void loadScreenShots() {

        LightTask.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                loadPicture();
                result = true;
                msg = "加载截图完成";
                handler.sendEmptyMessage(0);
            }
        });
    }

    /**
     * 加载截图
     */
    private List<ScreenShotEntity> loadPicture() {
        data.clear();
        File file = SYSJStorageUtil.getInnerSysjPicture();
        Log.i(tag, "file=" + file);
        if (file != null) {
            scanPicture(file);
        }
        file = SYSJStorageUtil.getOuterSysjPicture();
        Log.i(tag, "path=" + file);
        if (file != null) {
            scanPicture(file);
        }
        Log.i(tag, "data=" + data);
        // 对图片根据修改时间进行排序
        Comparators.sortScreenShot(data);
        return data;
    }

    /**
     * 扫描截图
     */
    private void scanPicture(File file) {
        // 图片路径
        String path;
        // 图片最后修改时间
        long lastModified;
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] files = file.listFiles();
        String fileName;
        ScreenShotEntity entity;
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory()) {
                fileName = files[i].getName();
                Log.i(tag, "fileName=" + fileName);
                if (fileName.trim().toLowerCase().endsWith(".png")) {
                    entity = new ScreenShotEntity();
                    path = file.getAbsolutePath() + File.separator + fileName;
                    Log.i(tag, "path=" + path);
                    lastModified = getLastModified(fileName);
                    Log.i(tag, "lastModified=" + lastModified);
                    if (lastModified != 0) {
                        entity.setLastModified(lastModified);
                    } else {
                        lastModified = file.lastModified();
                        saveLastModified(fileName, lastModified);
                        entity.setLastModified(lastModified);
                    }
                    entity.setPath(path);
                    // 去掉后后缀名
                    fileName = FileUtil.getFileName(fileName);
                    entity.setDisplayName(fileName);
                    data.add(entity);
                }
            }
        }
    }

    /**
     * 保存截图最后修改时间
     */
    public synchronized final static void saveLastModified(String fileName, long lastModified) {
        DefaultPreferences.getInstance().putLong(fileName, lastModified);
    }

    /**
     * 获取截图最后修改时间
     */
    public synchronized final static long getLastModified(String fileName) {
        return DefaultPreferences.getInstance().getLong(fileName, 0);
    }

    /**
     * 移除截图最后修改时间
     */
    public synchronized final static void removeLastModified(String fileName) {
        DefaultPreferences.getInstance().remove(fileName);
    }

    /**
     * 截图重命名
     */
    public static void renameScreenShot(File file, String fileName, String newFileName) {
        // 获得视频的父路径
        final String parentFile = file.getParentFile().getPath() + File.separator;
        File newFile = new File(parentFile + newFileName + ".png");
        // 文件重命名
        file.renameTo(newFile);
        ToastHelper.s("修改成功");
        long fielCreatTime = getLastModified(fileName + ".png");
        // 新增
        saveLastModified(newFileName + ".png", fielCreatTime);
        // 移除
        removeLastModified(fileName + ".png");
        // 加载截图
        DataManager.LOCAL.loadScreenShots();
    }
}
