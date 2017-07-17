package com.ifeimo.screenrecordlib.util;

import com.ifeimo.screenrecordlib.RecordingManager;

import java.io.File;

public class StorageUtil {

    public static final String FFMPEG_V2SH = "ffmpeg_v2sh";
    public static final String FM_OLD_CORE = "fmOldCore";
    public static final String FM_NEW_CORE = "fmNewCore";
    public static final String FM_TEST_CORE = "fmTestCore";
    public static final String BUSYBOX = "busybox";

    public static String getSrceenRecordFloder() {
        return RecordingManager.getInstance().context().getFilesDir().getPath();
    }

    // -------------------------------------------------------------------------------

    /**
     * 录屏路径（4.4以下）
     */
    public static String getFfmpegV2sh() {
        return getSrceenRecordFloder() + File.separator + FFMPEG_V2SH;
    }

    /**
     * 录屏路径（4.4以下）
     */
    public static boolean exitFfmpegV2sh() {
        File file = null;
        try {
            file = new File(getFfmpegV2sh());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null && file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------------

    /**
     * 录屏旧核心路径（4.4以上）
     */
    public static String getFmOldCore() {
        return getSrceenRecordFloder() + File.separator + FM_OLD_CORE;
    }

    /**
     * 录屏旧核心路径（4.4以上）
     */
    public static boolean exitFmOldCore() {
        File file = null;
        try {
            file = new File(getFmOldCore());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null && file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------------

    /**
     * 录屏新核心路径（4.4以上）
     */
    public static String getFmNewCore() {
        return getSrceenRecordFloder() + File.separator + FM_NEW_CORE;
    }

    /**
     * 录屏新核心路径（4.4以上）
     */
    public static boolean exitFmNewCore() {
        File file = null;
        try {
            file = new File(getFmNewCore());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null && file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------------

    /**
     * 录屏旧核心路径（4.4以上）（测试）
     */
    public static String getFmTestCore() {
        return getSrceenRecordFloder() + File.separator + FM_TEST_CORE;
    }

    // -------------------------------------------------------------------------------

    public static String getBusybox() {
        return getSrceenRecordFloder() + File.separator + BUSYBOX;
    }

    public static boolean exitBusybox() {
        File file = null;
        try {
            file = new File(getBusybox());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null && file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }
}
