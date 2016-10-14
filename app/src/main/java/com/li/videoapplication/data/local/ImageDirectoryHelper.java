package com.li.videoapplication.data.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;

import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.network.LightTask;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.utils.StringUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 功能：加载图片文件夹
 */
public class ImageDirectoryHelper {

    protected final String tag = this.getClass().getSimpleName();
    protected final String action = this.getClass().getName();

    private Context context;

    private List<ImageDirectoryEntity> directorys;
    private boolean result;
    private String msg;

    /**
     * 回调
     */
    private void postDirectorys(boolean result, String msg, List<ImageDirectoryEntity> data) {
        EventManager.postImageDirectoryResponseObject(result, msg, data);
    }

    /**
     * 消息处理
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message message) {
            postDirectorys(result, msg, directorys);
            super.handleMessage(message);
        }
    };

    public ImageDirectoryHelper() {
        super();

        directorys = new ArrayList<>();
        context = AppManager.getInstance().getContext();
        result = false;
    }

    /**
     * 新线程加载图片文件夹
     */
    public void loadImageDirectorys() {

        LightTask.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                scanDirectorys();
            }
        });
    }

    /**
     * 扫描图片文件夹
     */
    private void scanDirectorys() {

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            msg = "当前存储卡不可用";
            handler.sendEmptyMessage(0);
            return;
        }
        directorys.clear();
        Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(mImgUri, null, MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
        // 存储文件父路径,防止重复遍历
        Set<String> directorys = new HashSet<>();
        while (cursor.moveToNext()) {
            // 获取图片路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            // 图片父路径
            File parentFile = new File(path).getParentFile();
            if (parentFile == null) {
                continue;
            }
            String dirPath = parentFile.getAbsolutePath();
            ImageDirectoryEntity entity;
            // 如果文件集已经存有这个父路径就停止当次循环
            if (directorys.contains(dirPath)) {
                continue;
            } else {
                directorys.add(dirPath);
                entity = new ImageDirectoryEntity();
                entity.setPath(dirPath);
            }
            // 返回文件夹中图片数量
            if (parentFile.list() == null) {
                continue;
            }
            List<String> fileNames = getDirectoryImage(dirPath);
            entity.setFileNames(fileNames);
            this.directorys.add(entity);
            // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                cursor.close();
            }
        }
        msg = "加载图片文件夹完成";
        result = true;
        handler.sendEmptyMessage(0);
    }

    /**
     * 扫描文件夹下的我文件
     */
    public synchronized final static List<String> getDirectoryImage(String parentPath) {

        if (StringUtil.isNull(parentPath))
            return null;
        File parentFile = null;
        try {
            parentFile = new File(parentPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (parentFile == null || !parentFile.isDirectory())
            return null;
        String[] filePaths = parentFile.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                    return true;
                }
                return false;
            }
        });
        if (filePaths != null)
            return Arrays.asList(filePaths);
        else
            return null;
    }
}
