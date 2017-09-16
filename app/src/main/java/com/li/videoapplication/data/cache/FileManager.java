package com.li.videoapplication.data.cache;

import android.util.Log;

import com.li.videoapplication.data.local.SYSJStorageUtil;

import java.io.File;
import java.io.InputStream;

/**
 * 功能：文件缓存管理器
 */
public class FileManager {

    protected final String tag = this.getClass().getSimpleName();
    protected final String action = this.getClass().getName();

    private static FileManager instance;

    public static FileManager getInstance() {
        if (instance == null) {
            synchronized (FileManager.class) {
                if (instance == null) {
                    instance = new FileManager();
                }
            }
        }
        return instance;
    }

    public static void   saveStream(String url, InputStream is) {
        getInstance().saveFile(url, is);
    }

    public static InputStream getStream(String url) {
        return getInstance().getFile(url);
    }

    /**
     * 取出文件
     */
    public InputStream getFile(String url) {
        Log.d(tag, "url=" + url);
        File file = SYSJStorageUtil.createFilecachePath(url);
        if (file == null)
            return null;
        if (!file.exists())
            return null;
        return BaseUtils.restoreStream(file.getPath());
    }

    /**
     * 保存文件
     */
    public void saveFile(String url, InputStream is) {
        Log.d(tag, "saveFile: ");
        Log.i(tag, "url=" + url);
        if (url == null)
            return;
        if (is == null)
            return;
        File file = SYSJStorageUtil.createFilecachePath(url);
        if (file == null)
            return;
        String path = new String(file.getPath());
        if (file.exists())
            try {
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        BaseUtils.saveStream(path, is);
    }
}
