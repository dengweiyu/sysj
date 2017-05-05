package com.li.videoapplication.data.database;

import android.util.Log;


import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.framework.BaseEntity;
import com.li.videoapplication.utils.ApkUtil;
import com.li.videoapplication.utils.StringUtil;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.File;

/**
 * 实体：下载文件
 */
@Table(name = FileDownloaderEntity.TABLE_NAME)
public class FileDownloaderEntity extends BaseEntity {

    public final static String FILE_TYPE_ADVERTISEMENT = "advertisement";
    public final static String FILE_TYPE_FEIMO = "feimo";

    public final static String TABLE_NAME = "file_downloader";

    public final static String ID = "id";

    public final static String FILE_TYPE = "fileType";

    public final static String FILE_URL = "fileUrl";
    public final static String FILE_PATH = "filePath";
    public final static String FILE_NAME = "fileName";
    public final static String FILE_SIZE = "fileSize";
    public final static String DOWNLOAD_SIZE = "downloadSize";
    public final static String PACKAGE_NAME = "packageName";

    public final static String APP_ID = "app_id";
    public final static String TYPE_ID = "type_id";
    public final static String AD_ID = "ad_id";
    public final static String GAME_ID = "game_id";
    public final static String APP_NAME = "app_name";
    public final static String FLAG = "flag";
    public final static String SIZE_NUM = "size_num";
    public final static String SIZE_TEXT = "size_text";
    public final static String PLAY_NUM = "play_num";
    public final static String PLAY_TEXT = "play_text";
    public final static String APP_INTRO = "app_intro";
    public final static String DISPLAY = "display";
    public final static String A_DOWNLOAD_URL = "a_download_url";
    public final static String I_DOWNLOAD_URL = "i_download_url";
    public final static String MARK = "mark";

    public final static int AD_LOCATION_ID_1 = 1;
    public final static int AD_LOCATION_ID_2 = 2;
    public final static int AD_LOCATION_ID_3 = 3;
    public final static int AD_LOCATION_ID_4 = 4;
    public final static String AD_LOCATION_ID = "ad_location_id";

    @Column(name = ID, isId = true, autoGen = true)
    private int id;

    @Column(name = FILE_TYPE)
    private String fileType;
    @Column(name = FILE_URL)
    private String fileUrl;
    @Column(name = FILE_PATH)
    private String filePath;
    @Column(name = FILE_NAME)
    private String fileName;
    @Column(name = FILE_SIZE)
    private long fileSize = -1;
    @Column(name = DOWNLOAD_SIZE)
    private long downloadSize = -1;
    @Column(name = PACKAGE_NAME)
    private String packageName = "";

    @Column(name = APP_ID)
    private long app_id;
    @Column(name = TYPE_ID)
    private long type_id;
    @Column(name = AD_ID)
    private String ad_id;
    @Column(name = GAME_ID)
    private String game_id;
    @Column(name = APP_NAME)
    private String app_name;
    @Column(name = FLAG)
    private String flag;
    @Column(name = SIZE_NUM)
    private String size_num;

    @Column(name = SIZE_TEXT)
    private String size_text;
    @Column(name = PLAY_NUM)
    private String play_num;
    @Column(name = PLAY_TEXT)
    private String play_text;
    @Column(name = APP_INTRO)
    private String app_intro;
    @Column(name = DISPLAY)
    private String display;
    @Column(name = A_DOWNLOAD_URL)
    private String a_download_url;
    @Column(name = I_DOWNLOAD_URL)
    private String i_download_url;

    @Column(name = AD_LOCATION_ID)
    private int ad_location_id;

    @Column(name = MARK)
    private String mark;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePath() {
        File apkFile = SYSJStorageUtil.createApkPath(fileUrl);
        if (apkFile != null && apkFile.exists())
            filePath = apkFile.getPath();
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPackageName() {
        if (StringUtil.isNull(packageName)) {
            packageName = ApkUtil.getPackageName(getFilePath());
            Log.d(tag, "getPackageName: packageName=" + packageName);
        }
        return packageName;
    }

    public void setPackageName(String packageName) {
        Log.d(tag, "setPackageName: ");
        this.packageName = packageName;
    }

    public String getI_download_url() {
        return i_download_url;
    }

    public void setI_download_url(String i_download_url) {
        this.i_download_url = i_download_url;
    }

    public long getApp_id() {
        return app_id;
    }

    public void setApp_id(long app_id) {
        this.app_id = app_id;
    }

    public long getType_id() {
        return type_id;
    }

    public void setType_id(long type_id) {
        this.type_id = type_id;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSize_num() {
        return size_num;
    }

    public void setSize_num(String size_num) {
        this.size_num = size_num;
    }

    public String getSize_text() {
        return size_text;
    }

    public void setSize_text(String size_text) {
        this.size_text = size_text;
    }

    public String getPlay_num() {
        return play_num;
    }

    public void setPlay_num(String play_num) {
        this.play_num = play_num;
    }

    public String getPlay_text() {
        return play_text;
    }

    public void setPlay_text(String play_text) {
        this.play_text = play_text;
    }

    public String getApp_intro() {
        return app_intro;
    }

    public void setApp_intro(String app_intro) {
        this.app_intro = app_intro;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getA_download_url() {
        return a_download_url;
    }

    public void setA_download_url(String a_download_url) {
        this.a_download_url = a_download_url;
    }

    public int getProgress() {
        if (fileSize == 0){
            return 0;
        }else{
            return ((int)(100 * downloadSize / fileSize));
        }
    }

    private boolean isDownloading;

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }

    public boolean isPausing() {
        if (isDownloading == false &&
                fileSize > 0 &&
                downloadSize > 0 &&
                downloadSize <= fileSize)
            return true;
        return false;
    }

    public boolean isDownloaded() {
        File apkFile = SYSJStorageUtil.createApkPath(fileUrl);
        if (apkFile != null && apkFile.exists()) {
            Log.d(tag, "isDownloaded: true");
            return true;
        }
        Log.d(tag, "isDownloaded: false");
        return false;
    }

    public boolean isInstalled() {
        if (getPackageName() != null) {
            if (ApkUtil.isAvilible(getPackageName())) {
                Log.d(tag, "isInstalled: true");
                return true;
            }
        }
        Log.d(tag, "isInstalled: false");
        return false;
    }

    public int getAd_location_id() {
        return ad_location_id;
    }

    public void setAd_location_id(int ad_location_id) {
        this.ad_location_id = ad_location_id;
    }
}
