package com.li.videoapplication.data.local;

import android.content.Context;
import android.util.Log;

import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.framework.AppManager;

import java.io.File;

/**
 * 手游视界2.0.3以前（录屏大师）本地存储路径
 */
public class LPDSStorageUtil {

    public final static String TAG = LPDSStorageUtil.class.getSimpleName();

    /************************  以下为录屏大师目录  **************************/

    /**
     * [内置SD卡]/LuPingDaShi
     */
    public static File getInnerLpds() {
        String inSdcardDir = StorageUtil.getInner();
        String supperPath = inSdcardDir + File.separator + Contants.SUPPERLULU;
        String lpdsPath = inSdcardDir + File.separator + Contants.LUPINGDASHI;
        File supperDir = new File(supperPath);
        // 判断是否存在SupperLulu文件夹
        if (supperDir.exists()) {
            // 如果存在则重命名为LuPingDaShi
            File lpdsDir = new File(lpdsPath);
            supperDir.renameTo(lpdsDir);
            return lpdsDir;
        } else {// 不存在则新建LuPingDaShi文件夹
            File lpdsDir = new File(lpdsPath);
            if (lpdsDir.exists() == false) {
                lpdsDir = new File(inSdcardDir + File.separator + Contants.LUPINGDASHI);
                boolean isSuccess = lpdsDir.mkdirs();
                if (isSuccess){
                    return lpdsDir;
                }
            }
        }
        return null;
    }

    /**
     * [外置SD卡]/LuPingDaShi
     */
    public static File getOuterLpds() {
        Context context = AppManager.getInstance().getContext();
        String outer = StorageUtil.getOuter();
        File lpdsDir = null;
        if (StorageUtil.isOuterExit()) {
            // [外置SD卡]/LuPingDaShi
            lpdsDir = new File(outer + File.separator + Contants.LUPINGDASHI);
            if (lpdsDir.exists() == true) {
                // [外置SD卡]/LuPingDaShi/test/test
                lpdsDir = new File(outer + File.separator + Contants._LUPINGDASHI_TEST);
                if (lpdsDir.mkdirs() == false) {
                    // [外置SD卡]/Android/data/com.li.videoapplication/LuPingDaShi
                    lpdsDir = new File(outer + File.separator + Contants._LUPINGDASHI);
                    if (!lpdsDir.exists()) {
                        context.getExternalFilesDir(null);
                        lpdsDir.mkdirs();
                    }
                } else {
                    lpdsDir.delete();
                    // [外置SD卡]/LuPingDaShi
                    File file = new File(outer + File.separator + Contants.LUPINGDASHI);
                    Log.i(TAG, "getOuterLpds: " + file);
                    return file;
                }
            } else {
                // [外置SD卡]/LuPingDaShi/test/test
                lpdsDir = new File(outer + File.separator + Contants._LUPINGDASHI_TEST);
                boolean b = lpdsDir.mkdirs();
                if (!b) {
                    // [外置SD卡]/LuPingDaShi
                    lpdsDir = new File(outer + File.separator + Contants._LUPINGDASHI);
                    if (lpdsDir.exists() == false) {
                        context.getExternalFilesDir(null);
                        lpdsDir.mkdirs();
                    }
                } else {
                    lpdsDir.delete();
                    // [外置SD卡]/LuPingDaShi
                    File file = new File(outer + File.separator + Contants.LUPINGDASHI);
                    Log.i(TAG, "getOuterLpds: " + file);
                    return file;
                }
            }
            if (lpdsDir.exists()) {
                Log.i(TAG, "getOuterLpds: " + lpdsDir);
                return lpdsDir;
            }
        }
        Log.d(TAG, "getOuterSysj=" + lpdsDir);
        return null;
    }

    /**
     * [存储卡]/LuPingDaShi
     */
    public static File getLpds() {
        File file = getOuterLpds();
        if (file == null) {
            file = getInnerLpds();
        }
        if (file == null) {
            Context context = AppManager.getInstance().getContext();
            file = context.getExternalFilesDir(null);
        }
        Log.i(TAG, "getLpds: " + file);
        return file;
    }

    /**
     * [存储卡]/LuPingDaShi/Cover（V2.0.8版本）
     */
    public static File getLpdsCover() {
//        Log.d(TAG, "-----------------------------------[getLpdsCover]--------------------------------------");
        File file = getLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Constants.COVER;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
//        Log.d(TAG, "getLpdsCover: " + floder);
        return floder;
    }

    /**
     * [存储卡]/LuPingDaShi/Subtitle（V2.0.8版本）
     */
    public static File getLpdsSubtitle() {
//        Log.d(TAG, "-----------------------------------[getLpdsSubtitle]--------------------------------------");
        File file = getLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Constants.SUBTITLE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
//        Log.d(TAG, "getLpdsSubtitle: " + floder);
        return floder;
    }

    /************************  以下为录屏大师 视频/图片文件夹  **************************/

    /**
     * [存储卡]/LuPingDaShi/Picture
     */
    public static File getLpdsPicture() {
        File file = getLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.PICTURE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getLpdsPicture: " + floder);
        return floder;
    }

    /**
     * [内置SD卡]/LuPingDaShi/Picture
     */
    public static File getInnerLpdsPicture() {
        File file = getInnerLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.PICTURE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getInnerLpdsPicture: " + floder);
        return floder;
    }

    /**
     * [外置SD卡]/LuPingDaShi/Picture
     */
    public static File getOuterLpdsPicture() {
        File file = getOuterLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.PICTURE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getOuterLpdsPicture: " + floder);
        return floder;
    }

    /**
     * [内置SD卡]/LuPingDaShi/Rec
     */
    public static File getLpdsRec() {
        return getLpds();
    }


    /**
     * [内置SD卡]/LuPingDaShi/Rec
     */
    public static File getInnerLpdsRec() {
        return getInnerLpds();
    }

    /**
     * [外置SD卡]/LuPingDaShi/Rec
     */
    public static File getOuterLpdsRec() {
        return getOuterLpds();
    }

    /**
     * [内置SD卡]/LuPingDaShi/Rec（V2.0.7版本）
     */
    public static File getLpdsRec207() {
        File file = getLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.REC;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getLpdsRec207: " + floder);
        return floder;
    }

    /**
     * [内置SD卡]/LuPingDaShi/Rec（V2.0.7版本）
     */
    public static File getInnerLpdsRec207() {
        File file = getInnerLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.REC;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getInnerLpdsRec207: " + floder);
        return floder;
    }

    /**
     * [外置SD卡]/LuPingDaShi/Rec（V2.0.7版本）
     */
    public static File getOuterLpdsRec207() {
        File file = getOuterLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.REC;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getOuterLpdsRec207: " + floder);
        return floder;
    }

    /**
     * [存储卡]/LuPingDaShi/Cache（V2.0.7版本）
     */
    public static File getLpdsCache() {
        File file = getLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.CACHE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getLpdsCache: " + floder);
        return floder;
    }

    /**
     * [存储卡]/LuPingDaShi/filecache
     */
    public static File getLpdsFilecache() {
        File file = getLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.FILECACHE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getLpdsFilecache: " + floder);
        return floder;
    }

    /**
     * [存储卡]/LuPingDaShi/uploadvideo（V2.0.7版本）
     */
    public static File getLpdsUploadvideo() {
        File file = getLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.UPLOADVIDEO;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getLpdsUploadvideo: " + floder);
        return floder;
    }

    /**
     * [存储卡]/LuPingDaShi/uploadimage（V2.0.7版本）
     */
    public static File getLpdsUploadimage() {
        File file = getLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Contants.UPLOADIMAGE;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
        Log.d(TAG, "getLpdsUploadimage: " + floder);
        return floder;
    }

    /************************  以下为录屏大师 视频/图片文件  **************************/

    /**
     * 生成录屏文件（V2.0.7版本）
     *
     * [存储卡]/LuPingDaShi/Rec/2016-03-07_12_15_12.mp4
     */
    public static File createRecPath() {
        String path = getLpdsRec207().getPath() + File.separator + StorageUtil.createRecName();
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
     * 生成截图文件（V2.0.7版本）
     *
     * [存储卡]/LuPingDaShi/Picture/Pic20160317121512.png
     */
    public static File createPicturePath() {
        String path = getLpdsPicture().getPath() + File.separator + StorageUtil.createPictureName();
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
     * 生成缓存文件（V2.0.7版本）
     *
     * [存储卡]/LuPingDaShi/Cache/xxxxxxxxxx
     */
    public static File createCachePath(String fileName) {
        String path = getLpdsCache().getPath() + File.separator + StorageUtil.createCacheName(fileName);
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
     * 生成文件缓存文件（V2.0.7版本）
     *
     * [存储卡]/LuPingDaShi/Cache/xxxxxxxxxx.xxx
     */
    public static File createFilecachePath(String url) {
        String path = getLpdsFilecache().getPath() + File.separator + StorageUtil.createFilecacheName(url);
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
     * [存储卡]/LuPingDaShi/Cover/91859a7fccfdcb6320a8ba886727eb80.png
     */
    public static File createCoverPath(String filePath) {
//        Log.d(TAG, "-----------------------------------[createCoverPath]--------------------------------------");
        if (StorageUtil.createFilePathName(filePath, ".png") == null)
            return null;
        String path = getLpdsCover().getPath() + File.separator + StorageUtil.createFilePathName(filePath, ".png");
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d(TAG, "createCoverPath: " + file);
        return file;
    }


    /**
     * 生成字幕文件（V2.0.8版本）
     *
     * [存储卡]/LuPingDaShi/Subtitle/91859a7fccfdcb6320a8ba886727eb80.srt
     */
    public static File createSubtitlePath(String filePath) {
//        Log.d(TAG, "-----------------------------------[createSubtitlePath]--------------------------------------");
        if (StorageUtil.createFilePathName(filePath, ".srt") == null)
            return null;
        String path = getLpdsSubtitle().getPath() + File.separator + StorageUtil.createFilePathName(filePath, ".srt");
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d(TAG, "createSubtitlePath: " + file);
        return file;
    }

    /**
     * [存储卡]/LuPingDaShi/tmp（V2.0.8版本）
     */
    public static File getLpdsTmp() {
//        Log.d(TAG, "-----------------------------------[getLpdsTmp]--------------------------------------");
        File file = getLpds();
        File floder = null;
        if (file != null) {
            String path = file.getPath() + File.separator + Constants.TMP;
            floder = new File(path);
            if (!floder.exists()) {
                floder.mkdirs();
            }
        }
//        Log.d(TAG, "getLpdsTmp: " + floder);
        return floder;
    }

    /**
     * 生成临时视频文件（V2.0.8版本）
     *
     * [存储卡]/LuPingDaShi/Tmp/2016-03-07_12_15_12.mp4
     */
    public static File createTmpRecPath() {
//        Log.d(TAG, "-----------------------------------[createTmpRecPath]--------------------------------------");
        String path = getLpdsTmp().getPath() + File.separator + StorageUtil.createRecName();
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d(TAG, "createTmpRecPath: " + file);
        return file;
    }

    /**
     * 生成临时字幕文件（V2.0.8版本）
     *
     * [存储卡]/LuPingDaShi/Tmp/2016-03-07_12_15_12.srt
     */
    public static File createTmpSubtitlePath() {
//        Log.d(TAG, "-----------------------------------[createTmpSubtitlePath]--------------------------------------");
        String path = getLpdsTmp().getPath() + File.separator + StorageUtil.createFilePathName(".srt");
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d(TAG, "createTmpSubtitlePath: " + file);
        return file;
    }
}
