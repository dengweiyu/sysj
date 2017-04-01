package com.li.videoapplication.data.local;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.format.Formatter;
import android.util.Log;

import com.li.videoapplication.data.cache.CreateMD5;
import com.li.videoapplication.framework.AppManager;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 手游视界本地存储目录
 */
public class StorageUtil {

    public final static String TAG = StorageUtil.class.getSimpleName();

    public static boolean isOuterExit() {
        boolean flag = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        Log.d(TAG, "isOuterExit: " + flag);
        return flag;
    }

    /**
     * [内置SD卡]
     */
    public static String getInner() {
        String floder = "/mnt/sdcard";
        Log.d(TAG, "getInner: " + floder);
        return floder;
    }

    /**
     * [外置SD卡]
     */
    public static String getOuter() {
        Context context = AppManager.getInstance().getContext();
        String floder = null;
        StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        // 获取sdcard的路径：外置和内置
        String[] paths = null;
        try {
            paths = (String[]) sm.getClass().getMethod("getVolumePaths",  new Class[]{}).invoke(sm, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果数组的长度大于一，则说明有一个以上储存空间，即有外置SD卡
        if (paths != null && paths.length > 1) {
            // 外置SD卡通常是第二个
            String dir = paths[0];
            floder = paths[1];
        }
        Log.d(TAG, "getOuter: " + floder);
        return floder;
    }

    /******************************/

    /**
     * 生成录屏文件名（.mp4）
     *
     * @return 2016-03-07_12_15_12.mp4
     */
    public static String createRecName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String fileName = format.format(new Date());
        Log.d(TAG, "fileName: " + fileName);
        fileName = fileName + ".mp4";
        Log.d(TAG, "fileName: " + fileName);
        return fileName;
    }

    /**
     * 生成截屏文件名（.png）
     *
     * @return Pic20160317121512.png
     */
    public static String createPictureName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmsss");
        String fileName = format.format(new Date());
        Log.d(TAG, "fileName: " + fileName);
        fileName = "Pic" + fileName + ".png";
        Log.d(TAG, "fileName: " + fileName);
        return fileName;
    }

    /**
     * 生成错误日志路径
     */
    public static String createLogName(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmsss");
        String fileName = format.format(new Date());
        Log.d(TAG, "fileName: " + fileName);
        return getInner()+"/sysj/tmp/"+fileName;
    }

    /**
     * 生成缓存文件名
     *
     * @return xxxxxxxxxx
     */
    public static String createCacheName(String fileName) {
        Log.d(TAG, "fileName: " + fileName);
        return fileName;
    }

    /**
     * 生成文件缓存文件名
     *
     * @return xxxxxxxxxx
     */
    public static String createFilecacheName(String url) {
        String extensions = null;
        String[] s = url.split("\\.");
        if (s != null && s.length >= 2) {
            extensions = s[s.length - 1];
            if (!extensions.endsWith("."));{
                extensions = "." + extensions;
            }
        }
        Log.d(TAG, "extensions: " + extensions);
        String name = CreateMD5.getMd5(url);
        if (extensions != null) {
            name = name + extensions;
        }
        Log.d(TAG, "name: " + name);
        return name;
    }

    /******************************/


    /**
     * 获得Sysj目录下总内存 ----> /Sysj
     */
    public static String getSysjTotalSize() {
        return getTotalSize(SYSJStorageUtil.getSysj());
    }

    /**
     * 获得文件总内存
     */
    public static String getTotalSize(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        return getTotalSize(file.getPath());
    }

    /**
     * 获得文件可用内存
     */
    public static String getAvailableSize(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        return getAvailableSize(file.getPath());
    }

    /**
     * 获得文件总内存
     */
    public static String getTotalSize(String path) {
        Log.d(TAG, "getTotalSize: // -----------------------------------------------");
        Context context = AppManager.getInstance().getContext();
        if (context == null) {
            return null;
        }
        if (path == null) {
            return null;
        }
        Log.d(TAG, "getTotalSize: path=" + path);
        StatFs stat = null;
        try {
            stat = new StatFs(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long blockSize = 0;
        long totalBlocks = 0;
        if (stat != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
                totalBlocks = stat.getBlockCountLong();
            } else {
                blockSize = stat.getBlockSize();
                totalBlocks = stat.getBlockCount();
            }
        }
        String s = Formatter.formatFileSize(context, blockSize * totalBlocks);
        Log.d(TAG, "getTotalSize: s=" + s);
        return s;
    }

    // 获取android当前可用内存大小
    public static String getAvailMemory() {
        Log.d(TAG, "getAvailMemory: // -----------------------------------------------");
        Context context = AppManager.getInstance().getContext();
        if (context == null) {
            return null;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        String s = Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        Log.d(TAG, "getAvailMemory: s=" + s);
        return s;
    }

    /**
     * 获得文件可用内存
     */
    public static String getAvailableSize(String path) {
        Log.d(TAG, "getAvailableSize: // -----------------------------------------------");
        Context context = AppManager.getInstance().getContext();
        if (context == null) {
            return null;
        }
        if (path == null) {
            return null;
        }
        Log.d(TAG, "getAvailableSize: path=" + path);
        StatFs stat = null;
        try {
            stat = new StatFs(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long blockSize = 0;
        long availableBlocks = 0;
        if (stat != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
                availableBlocks = stat.getAvailableBlocksLong();
            } else {
                blockSize = stat.getBlockSize();
                availableBlocks = stat.getAvailableBlocks();
            }
        }
        String s = Formatter.formatFileSize(context, blockSize * availableBlocks);
        Log.d(TAG, "getAvailableSize: s=" + s);
        return s;
    }


    /**
     * 获得LuPingDaShi目录下总大小
     */
    public static long getLpdsTotalSize_long() {
        File file = LPDSStorageUtil.getLpds();
        if (file == null) {
            // 获取默认机身内存
            file = Environment.getDataDirectory();
        }
        StatFs stat = new StatFs(file.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * 获得LuPingDaShi目录下可用大小
     */
    public static long getLpdsAvailableSize_long() {
        File file = LPDSStorageUtil.getLpds();
        if (file == null) {
            // 获取默认机身内存
            file = Environment.getDataDirectory();
        }
        StatFs stat = new StatFs(file.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 获得Sysj目录下可用内存 ----> /Sysj
     */
    public static String getSysjAvailableSize() {
        return getAvailableSize(SYSJStorageUtil.getSysj());
    }

    /**
     * 获得Sysj目录下文件大小
     */
    public static String getSysjSize() {
        File recFile = SYSJStorageUtil.getSysjRec();
        File picFile = SYSJStorageUtil.getSysjPicture();
        long recLength = 0;
        long picLength = 0;
        if (recFile != null) {
            recLength = FileUtil._getFileSize(recFile);
        }
        if (picFile != null) {
            picLength = FileUtil._getFileSize(picFile);
        }
        Log.d(TAG, "getSysjSize: recFile=" + recFile);
        Log.d(TAG, "getSysjSize: picFile=" + picFile);
        Log.d(TAG, "getSysjSize: recLength=" + recLength);
        Log.d(TAG, "getSysjSize: picLength=" + picLength);
        DecimalFormat format = new DecimalFormat("0.##");
        float size = ((float) (recLength + picLength)) / 1024 / 1024;
        String s = format.format(size) + "M";
        Log.d(TAG, "getSysjSize: s=" + s);
        return s;
    }

    /**
     * 生成文件对应的文件名
     *
     * @param filePath /mnt/sdcard/Rec/2016-03-07_12_15_12.mp4
     * @param extensions .srt
     * @return xxxxxxxxxx.xxx
     */
    public static String createFilePathName(String filePath, String extensions) {
        Log.d(TAG, "-----------------------------------[createFilePathName]--------------------------------------");
        if (filePath == null ||  filePath.length() == 0)
            return null;
        if (extensions == null ||  extensions.length() == 0)
            return null;
        if (!extensions.startsWith(".")){
            extensions = "." + extensions;
        }
        String name = CreateMD5.getMd5(filePath) + extensions;
        Log.d(TAG, "createFilePathName: " + name);
        return name;
    }

    /**
     * 生成【方法追踪】文件名
     *
     * @return ScreenRecordMaster-MainApplication-onCreate-2016-03-07_12-15-12.mp4
     */
    public static String createMethodTracingName(String className, String methodName) {
        Log.d(TAG, "-----------------------------------[createMethodTracingName]--------------------------------------");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String date = format.format(new Date());
        StringBuffer buffer = new StringBuffer("VideoApplication-");
        if (className != null) {
            buffer.append(className + "-");
        }
        if (methodName != null) {
            buffer.append(methodName + "-");
        }
        buffer.append(date);
        Log.d(TAG, "createMethodTracingName: " + buffer);
        return buffer.toString();
    }
}
