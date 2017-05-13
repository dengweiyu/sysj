package com.li.videoapplication.data.database;

import android.os.Environment;

import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.local.FileOperateUtil;

import java.io.File;

public class Utils {

    /**
     * 初始化数据库
     */
    public static void scanRecToDatabase() {
        // 外置SD卡
        File outer = SYSJStorageUtil.getOuterSysjRec();
        // 内置SD卡
        File inner = SYSJStorageUtil.getInnerSysjRec();
        if (outer != null) {
            scanFiles(new File(inner.getPath() + File.separator), new File(outer.getPath() + File.separator));
        } else {
            scanFiles(new File(inner.getPath() + File.separator), null);
        }

        // SD卡
        String floder = FileOperateUtil.getVideoFolder();
        if (floder != null){
            scanFiles(new File(floder + File.separator), null);
        }
    }

    /**
     * 初始化数据库
     */
    private static void scanFiles(final File inner, final File outer) {

        int lengths = 0;
        int innerLength = 0;
        int outerLength = 0;

        File[] innerFiles = null;
        if (inner != null) {
            innerFiles = inner.listFiles();
            if (innerFiles != null) {
                innerLength = innerFiles.length;
            }
        }
        File[] outerFiles = null;
        if (outer != null) {
            outerFiles = outer.listFiles();
            if (outerFiles == null){
                outerLength = 0;
            }else {
                outerLength = outerFiles.length;
            }

        }
        lengths = innerLength + outerLength;
        if (lengths <= 0) {
            return;
        }
        if (innerFiles != null && innerFiles.length > 0) {
            // 内置SD卡
            for (int i = 0; i < innerLength; i++) {
                batchInsert(innerFiles[i]);
            }
        }
        if (outerFiles != null && outerFiles.length > 0) {
            // 外置SD卡
            for (int i = 0; i < outerLength; i++) {
                batchInsert(outerFiles[i]);
            }
        }
    }

    /**
     * 插入数据库并同时进行UI更新进度条
     */
    public static void batchInsert(File file) {
        if (file.isFile()) {
            // 文件是MP4文件且大于512
            if ((file.getName().endsWith("mp4") && !file.getName().equals(".mp4") && FileUtil.getFileSize(file) > 512)
                    || (file.getName().endsWith("3gp") && !file.getName().equals(".3gp") && FileUtil.getFileSize(file) > 512)) {
                // 查询该文件是否已经在数据库之中
                if (VideoCaptureManager.findByPath(file.getPath()) == null) {
                    String filePath = file.getPath();
                    VideoCaptureManager.save(filePath,
                            VideoCaptureEntity.VIDEO_SOURCE_REC,
                            VideoCaptureEntity.VIDEO_STATION_LOCAL);
                }
            }
        }
    }

    /**
     * 将数据库拷贝到SD卡
     */
    public synchronized final static void copyDatabaseToSDCard() {
        String oldPath = "data/data/com.li.videoapplication/databases/" + "VIDEO.db";
        String newPath = Environment.getExternalStorageDirectory() + File.separator + "VIDEO.db";
        FileUtil.copyFileToFile(oldPath, newPath);
    }

    /**
     * 删除以前的数据库
     */
    public synchronized final static void deleteDatabase() {
        String oldPath = "data/data/com.li.videoapplication/databases/" + "VIDEO.db";
        String newPath = Environment.getExternalStorageDirectory() + File.separator + "VIDEO.db";
        FileUtil.deleteFile(oldPath);
        FileUtil.deleteFile(newPath);
    }
}
