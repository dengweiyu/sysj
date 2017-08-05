package com.li.videoapplication.data.local;

import java.io.File;

public class Contants {


    public static final String SUPPERLULU = "SupperLulu";

    /**
     * 录屏大师目录
     */
    public static final String LUPINGDASHI = "LuPingDaShi";

    /**
     * 手游视界目录
     */
    public static final String SYSJ = "sysj";

    /**
     * 下载应用
     */
    public static final String APK = "apk";

    /**
     * 临时下载应用
     */
    public static final String TMP_APK = "tmpApk";

    /**
     * 手游世界包名
     */
    public static final String PACKAGENAME_SYSJ = "com.li.videoapplication";

    /**
     * 录屏大师包名
     */
    public static final String PACKAGENAME_LUPINGDASHI = "com.screeclibinvoke";

    // []/Android/data/com.li.videoapplication/sysj
    public static final String ANDROID = "Android";
    public static final String DATA = "data";

    public static final String _SYSJ = ANDROID + File.separator +
            DATA + File.separator +
            PACKAGENAME_SYSJ + File.separator +
            SYSJ;

    public static final String _LUPINGDASHI = ANDROID + File.separator +
            DATA + File.separator +
            PACKAGENAME_LUPINGDASHI + File.separator +
            LUPINGDASHI;

    public static final String _SYSJ_TEST = SYSJ + File.separator +
            "test" + File.separator +
            "test";

    public static final String _LUPINGDASHI_TEST = LUPINGDASHI + File.separator +
            "test" + File.separator +
            "test";

    /**
     * 截图
     */
    public static final String PICTURE = "Picture";

    /**
     * 录制的视频，剪輯的視頻
     */
    public static final String REC = "Rec";

    /**
     * 剪辑的视频
     */
    public static final String TMP = "tmp";

    /**
     * 封面
     */
    public static final String COVER = "Cover";

    /**
     * 字幕
     */
    public static final String SUBTITLE = "Subtitle";

    /**
     * 缓存
     */
    public static final String CACHE = "cache";

    /**
     * 下载
     */
    public static final String DOWNLOAD = "download";

    /**
     * 文件缓存
     */
    public static final String FILECACHE = "filecache";

    /**
     * 上传的视频缓存
     */
    public static final String UPLOADVIDEO = "uploadvideo";

    /**
     * 上传的图片缓存
     */
    public static final String UPLOADIMAGE = "uploadimage";


}