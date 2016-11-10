package com.li.videoapplication.data.local;

import android.content.Context;
import android.util.Log;

import com.li.videoapplication.framework.AppManager;

import java.io.File;

/**
 * 手游视界2.0.3本地存储路径
 */
public class SYSJStorageUtil {

    public final static String TAG = SYSJStorageUtil.class.getSimpleName();


    /************************  以下为手游视界 视频/图片/录音文件  **************************/

    /**
     * 生成临时视频文件（V2.0.8版本）
     *
     * [存储卡]/LuPingDaShi/Tmp/2016-03-07_12_15_12.mp4
     */
    public static File createTmpRecPath() {
        Log.d(TAG, "-----------------------------------[createTmpRecPath]--------------------------------------");
        String path = getSysjTmp().getPath() + File.separator + StorageUtil.createRecName();
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createTmpRecPath: " + file);
        return file;
    }


    /************************  以下为手游视界2.0.3 目录  **************************/

    /**
     * [内置SD卡]/sysj
     */
    public static File getInnerSysj() {
        String inner = StorageUtil.getInner();
        String floder = inner + File.separator + Contants.SYSJ;
        File file = null;
        try {
            file = new File(floder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null) {
            if (file.exists() == false) {
                file = new File(inner + File.separator + Contants.SYSJ);
                file.mkdirs();
            }
        }
        Log.d(TAG, "getInnerSysj: " + file);
        return file;
    }

    /**
     * [外置SD卡]/sysj
     */
    public static File getOuterSysj() {
        Context context = AppManager.getInstance().getContext();
        String outer = StorageUtil.getOuter();
        File floder = null;
        if (StorageUtil.isOuterExit()) {
            // [外置SD卡]/sysj
            floder = new File(outer + File.separator + Contants.SYSJ);
            if (floder.exists() == true) {
                // [外置SD卡]/sysj/test/test
                floder = new File(outer + File.separator + Contants._SYSJ_TEST);
                if (floder.mkdirs() == false) {
                    // [外置SD卡]/Android/data/com.li.videoapplication/sysj
                    floder = new File(outer + File.separator + Contants._SYSJ);
                    if (!floder.exists()) {
                        context.getExternalFilesDir(null);
                        floder.mkdirs();
                    }
                } else {
                    floder.delete();
                    // [外置SD卡]/sysj
                    File file = new File(outer + File.separator + Contants.SYSJ);
                    Log.i(TAG, "getOuterSysj: " + file);
                    return file;
                }
            } else {
                // [外置SD卡]/sysj/test/test
                floder = new File(outer + File.separator + Contants._SYSJ_TEST);
                boolean b = floder.mkdirs();
                if (!b) {
                    // [外置SD卡]/sysj
                    floder = new File(outer + File.separator + Contants._SYSJ);
                    if (floder.exists() == false) {
                        context.getExternalFilesDir(null);
                        floder.mkdirs();
                    }
                } else {
                    floder.delete();
                    // [外置SD卡]/sysj
                    File file = new File(outer + File.separator + Contants.SYSJ);
                    Log.i(TAG, "getOuterSysj: " + file);
                    return file;
                }
            }
            if (floder.exists()) {
                Log.i(TAG, "getOuterSysj: " + floder);
                return floder;
            }
        }
        Log.d(TAG, "getOuterSysj: " + floder);
        return null;
    }

    /**
     * [默认]/sysj
     */
    public static File getDefaultSysj() {
        Context context = AppManager.getInstance().getContext();
        File f = context.getExternalFilesDir(null);
        if (f == null)
            return null;
        String floder = f.getAbsolutePath() + File.separator + Contants.SYSJ;
        File file = null;
        try {
            file = new File(floder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        Log.d(TAG, "getDefaultSysj: " + file);
        return file;
    }

    /**
     * [存储卡]/sysj
     */
    public static File getSysj() {
        File floder = getOuterSysj();
        if (floder == null) {
            floder = getInnerSysj();
            Log.d(TAG, "getSysj: 1");
        }
        if (floder == null) {
            floder = getDefaultSysj();
            Log.d(TAG, "getSysj: 2");
        }
        Log.i(TAG, "getSysj: " + floder);
        return floder;
    }

    /************************  以下为手游视界2.0.3 视频/图片文件夹  **************************/

    /**
     * [存储卡]/sysj/Picture
     */
    public static File getSysjPicture() {
        File file = getSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.PICTURE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getSysjPicture: " + floder);
        return floder;
    }

    /**
     * [内置SD卡]/sysj/Picture
     */
    public static File getInnerSysjPicture() {
        File file = getInnerSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.PICTURE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getInnerSysjPicture: " + floder);
        return floder;
    }

    /**
     * [外置SD卡]/sysj/Picture
     */
    public static File getOuterSysjPicture() {
        File file = getOuterSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.PICTURE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getOuterSysjPicture: " + floder);
        return floder;
    }

    /**
     * [存储卡]/sysj/Rec
     */
    public static File getSysjRec() {
        File file = getSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.REC;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getSysjRec: " + floder);
        return floder;
    }

    /**
     * [内置SD卡]/sysj/Rec
     */
    public static File getInnerSysjRec() {
        File file = getInnerSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.REC;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getInnerSysjRec: " + floder);
        return floder;
    }

    /**
     * [外置SD卡]/sysj/Rec
     */
    public static File getOuterSysjRec() {
        File file = getOuterSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.REC;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getOuterSysjRec: " + floder);
        return floder;
    }

    /**
     * [存储卡]/sysj/tmp
     */
    public static File getSysjTmp() {
        File file = getSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.TMP;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getSysjTmp: " + floder);
        return floder;
    }

    /**
     * [存储卡]/sysj/Cache
     */
    public static File getSysjCache() {
        File file = getSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.CACHE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getSysjCache: " + floder);
        return floder;
    }

    /**
     * [存储卡]/sysj/filecache
     */
    public static File getSysjFilecache() {
        File file = getSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.FILECACHE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getSysjFilecache: " + floder);
        return floder;
    }

    /**
     * [存储卡]/sysj/uploadvideo
     */
    public static File getSysjUploadvideo() {
        File file = getSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.UPLOADVIDEO;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getSysjUploadvideo: " + floder);
        return floder;
    }

    /**
     * [存储卡]/sysj/uploadimage
     */
    public static File getSysjUploadimage() {
        File file = getSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.UPLOADIMAGE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getSysjUploadimage: " + floder);
        return floder;
    }

    /************************  以下为手游视界2.0.3以前（录屏大师）视频/图片文件  **************************/

    /**
     * 生成录屏文件
     *
     * [存储卡]/sysj/Rec/2016-03-07_12_15_12.mp4
     */
    public static File createRecPath() {
        String path = SYSJStorageUtil.getSysjRec().getPath() + File.separator + StorageUtil.createRecName();
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createRecPath: " + file);
        return file;
    }

    /**
     * 生成录屏文件（V2.0.8版本）
     *
     * [存储卡]/LuPingDaShi/Rec/xxxx.mp4
     */
    public static File createRecPath(String fileName) {
        Log.d(TAG, "-----------------------------------[createRecPath]--------------------------------------");
        Log.d(TAG, "createRecPath: fileName=" + fileName);
        fileName = fileName + ".mp4";
        Log.d(TAG, "createRecPath: fileName=" + fileName);
        String path = getSysjRec().getPath() + File.separator + fileName;
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createRecPath: " + file);
        return file;
    }

    /**
     * 生成截图文件
     *
     * [存储卡]/sysj/Picture/Pic20160317121512.png
     */
    public static File createPicturePath() {
        String path = SYSJStorageUtil.getSysjPicture().getPath() + File.separator + StorageUtil.createPictureName();
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createPicturePath: " + file);
        return file;
    }

    /**
     * 生成缓存文件
     *
     * [存储卡]/sysj/Cache/xxxxxxxxxx
     */
    public static File createCachePath(String fileName) {
        String path = SYSJStorageUtil.getSysjCache().getPath() + File.separator + StorageUtil.createCacheName(fileName);
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createCachePath: " + file);
        return file;
    }

    /**
     * 生成文件缓存文件
     *
     * [存储卡]/sysj/Cache/xxxxxxxxxx.xxx
     */
    public static File createFilecachePath(String url) {
        String path = SYSJStorageUtil.getSysjFilecache().getPath() + File.separator + StorageUtil.createFilecacheName(url);
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createFilecachePath: " + file);
        return file;
    }

    /**
     * 生成封面文件（V2.0.8版本）
     *
     * [存储卡]/sysj/Cover/91859a7fccfdcb6320a8ba886727eb80.png
     */
    public static File createCoverPath(String filePath) {
        Log.d(TAG, "-----------------------------------[createCoverPath]--------------------------------------");
        if (StorageUtil.createFilePathName(filePath, ".png") == null)
            return null;
        String path = getSysjCover().getPath() + File.separator + StorageUtil.createFilePathName(filePath, ".png");
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createCoverPath: " + file);
        return file;
    }

    /**
     * 生成字幕文件（V2.1.0版本）
     *
     * [存储卡]/LuPingDaShi/Subtitle/91859a7fccfdcb6320a8ba886727eb80.srt
     */
    public static File createSubtitlePath(String filePath) {
        Log.d(TAG, "-----------------------------------[createSubtitlePath]--------------------------------------");
        if (StorageUtil.createFilePathName(filePath, ".srt") == null)
            return null;
        String path = getSysjSubtitle().getPath() + File.separator + StorageUtil.createFilePathName(filePath, ".srt");
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createSubtitlePath: " + file);
        return file;
    }

    /**
     * [存储卡]/LuPingDaShi/Subtitle（V2.1.0版本）
     */
    public static File getSysjSubtitle() {
        Log.d(TAG, "-----------------------------------[getSysjSubtitle]--------------------------------------");
        File file = getSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.SUBTITLE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getLpdsSubtitle: " + floder);
        return floder;
    }

    /**
     * [存储卡]/LuPingDaShi/Cover（V2.0.8版本）
     */
    public static File getSysjCover() {
        Log.d(TAG, "-----------------------------------[getSysjCover]--------------------------------------");
        File file = getSysj();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.COVER;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getLpdsCover: " + floder);
        return floder;
    }
}
