package com.li.videoapplication.data.database;

import android.util.Log;

import com.li.videoapplication.utils.URLUtil;

import org.xutils.ex.DbException;

import java.util.Iterator;
import java.util.List;

/**
 * 实体：下载文件数据库
 */
public class FileDownloaderManager {

    public final static String TAG = FileDownloaderManager.class.getSimpleName();

    /**
     * 查找全部
     */
    public static List<FileDownloaderEntity> findAll() {
        try {
            return xUtilsDb.DB
                    .selector(FileDownloaderEntity.class)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存
     *
     * @return -1：失败 1：成功
     */
    public static int save(FileDownloaderEntity oldEntity) {
        if (oldEntity == null)
            return - 1;
        if (oldEntity.getFileUrl() == null ||
                !URLUtil.isURL(oldEntity.getFileUrl()))
            return - 1;
        if (oldEntity.getGame_id() == null)
            return - 1;

        try {
            xUtilsDb.DB.save(oldEntity);
            Log.d(TAG, "save: 1");
            return 1;
        } catch (DbException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * 保存或更新根据（game_id）
     *
     * @return -1：失败 1：成功
     */
    public static int saveOrUdateByGameId(FileDownloaderEntity oldEntity) {
        if (oldEntity == null)
            return - 1;
        if (oldEntity.getFileUrl() == null ||
                !URLUtil.isURL(oldEntity.getFileUrl()))
            return - 1;
        if (oldEntity.getGame_id() == null)
            return - 1;

        FileDownloaderEntity newEntity = findByGameId(oldEntity.getGame_id());
        if (newEntity == null) {// 插入
            Log.d(TAG, "saveOrUdateByGameId: 1");
            return save(oldEntity);
        } else {// 更新

            if (oldEntity.getFileUrl() == null ||
                    !oldEntity.getFileUrl().equals(newEntity.getFileUrl())) {

                Log.d(TAG, "saveOrUdateByGameId: 2");
                newEntity.setFileUrl(oldEntity.getFileUrl());
                newEntity.setFileSize(0);
                newEntity.setDownloadSize(0);
            }

            newEntity.setAd_location_id(oldEntity.getAd_location_id());

            newEntity.setFilePath(oldEntity.getFilePath());
            newEntity.setFileName(oldEntity.getFileName());
            newEntity.setFileType(oldEntity.getFileType());

            newEntity.setApp_id(oldEntity.getApp_id());
            newEntity.setApp_intro(oldEntity.getApp_intro());
            newEntity.setApp_name(oldEntity.getApp_name());
            newEntity.setDisplay(oldEntity.getDisplay());
            newEntity.setGame_id(oldEntity.getGame_id());
            newEntity.setAd_id(oldEntity.getAd_id());
            newEntity.setI_download_url(oldEntity.getI_download_url());
            newEntity.setFlag(oldEntity.getFlag());
            newEntity.setPlay_num(oldEntity.getPlay_num());
            newEntity.setPlay_text(oldEntity.getPlay_text());
            newEntity.setSize_num(oldEntity. getSize_num());
            newEntity.setSize_text(oldEntity.getSize_text());
            newEntity.setType_id(oldEntity.getType_id());
            try {
                xUtilsDb.DB.update(newEntity,
                        new String[]{ FileDownloaderEntity.FILE_URL,
                                FileDownloaderEntity.FILE_PATH,
                                FileDownloaderEntity.FILE_NAME,
                                FileDownloaderEntity.FILE_SIZE,
                                FileDownloaderEntity.DOWNLOAD_SIZE,
                                FileDownloaderEntity.FILE_TYPE,
                                FileDownloaderEntity.FILE_TYPE,

                                FileDownloaderEntity.APP_ID,
                                FileDownloaderEntity.APP_INTRO,
                                FileDownloaderEntity.APP_NAME,
                                FileDownloaderEntity.DISPLAY,
                                FileDownloaderEntity.AD_ID,
                                FileDownloaderEntity.GAME_ID,
                                FileDownloaderEntity.I_DOWNLOAD_URL,
                                FileDownloaderEntity.FLAG,
                                FileDownloaderEntity.PLAY_NUM,
                                FileDownloaderEntity.PLAY_TEXT,
                                FileDownloaderEntity.SIZE_NUM,
                                FileDownloaderEntity.SIZE_TEXT,
                                FileDownloaderEntity.TYPE_ID,

                                FileDownloaderEntity.AD_LOCATION_ID});
                Log.d(TAG, "saveOrUdateByGameId: 2");
                return 1;
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 保存或更新根据（ad_location_id）
     *
     * @return -1：失败 1：成功
     */
    public static int saveOrUdateByAdLocationId(FileDownloaderEntity oldEntity) {
        if (oldEntity == null)
            return - 1;
        if (oldEntity.getFileUrl() == null ||
                !URLUtil.isURL(oldEntity.getFileUrl()))
            return - 1;
        if (oldEntity.getGame_id() == null)
            return - 1;

        FileDownloaderEntity newEntity = findByAdLocationId(oldEntity.getAd_location_id());
        if (newEntity == null) {// 插入
            Log.d(TAG, "saveOrUdateByAdLocationId: 1");
            return save(oldEntity);
        } else {// 更新

            if (oldEntity.getFileUrl() == null ||
                    !oldEntity.getFileUrl().equals(newEntity.getFileUrl())) {

                Log.d(TAG, "saveOrUdateByAdLocationId: 2");
                newEntity.setFileUrl(oldEntity.getFileUrl());
                newEntity.setFileSize(0);
                newEntity.setDownloadSize(0);
            }

            newEntity.setAd_location_id(oldEntity.getAd_location_id());

            newEntity.setFilePath(oldEntity.getFilePath());
            newEntity.setFileName(oldEntity.getFileName());
            newEntity.setFileType(oldEntity.getFileType());

            newEntity.setApp_id(oldEntity.getApp_id());
            newEntity.setApp_intro(oldEntity.getApp_intro());
            newEntity.setApp_name(oldEntity.getApp_name());
            newEntity.setDisplay(oldEntity.getDisplay());
            newEntity.setGame_id(oldEntity.getGame_id());
            newEntity.setAd_id(oldEntity.getAd_id());
            newEntity.setI_download_url(oldEntity.getI_download_url());
            newEntity.setFlag(oldEntity.getFlag());
            newEntity.setPlay_num(oldEntity.getPlay_num());
            newEntity.setPlay_text(oldEntity.getPlay_text());
            newEntity.setSize_num(oldEntity. getSize_num());
            newEntity.setSize_text(oldEntity.getSize_text());
            newEntity.setType_id(oldEntity.getType_id());
            try {
                xUtilsDb.DB.update(newEntity,
                        new String[]{ FileDownloaderEntity.FILE_URL,
                                FileDownloaderEntity.FILE_PATH,
                                FileDownloaderEntity.FILE_NAME,
                                FileDownloaderEntity.FILE_SIZE,
                                FileDownloaderEntity.DOWNLOAD_SIZE,
                                FileDownloaderEntity.FILE_TYPE,
                                FileDownloaderEntity.FILE_TYPE,

                                FileDownloaderEntity.APP_ID,
                                FileDownloaderEntity.APP_INTRO,
                                FileDownloaderEntity.APP_NAME,
                                FileDownloaderEntity.DISPLAY,
                                FileDownloaderEntity.AD_ID,
                                FileDownloaderEntity.GAME_ID,
                                FileDownloaderEntity.I_DOWNLOAD_URL,
                                FileDownloaderEntity.FLAG,
                                FileDownloaderEntity.PLAY_NUM,
                                FileDownloaderEntity.PLAY_TEXT,
                                FileDownloaderEntity.SIZE_NUM,
                                FileDownloaderEntity.SIZE_TEXT,
                                FileDownloaderEntity.TYPE_ID,

                                FileDownloaderEntity.AD_LOCATION_ID});
                Log.d(TAG, "saveOrUdateByAdLocationId: 2");
                return 1;
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 保存或更新（fileUrl）
     *
     * @return -1：失败 1：成功
     */
    public static int saveOrUdateByFileUrl(FileDownloaderEntity oldEntity) {
        if (oldEntity == null)
            return - 1;
        if (oldEntity.getFileUrl() == null ||
                !URLUtil.isURL(oldEntity.getFileUrl()))
            return - 1;
        if (oldEntity.getGame_id() == null)
            return - 1;

        FileDownloaderEntity newEntity = findByFileUrl(oldEntity.getFileUrl());
        if (newEntity == null) {// 插入
            Log.d(TAG, "saveOrUdateByFileUrl: 1");
            return save(oldEntity);
        } else {// 更新
            newEntity.setAd_location_id(oldEntity.getAd_location_id());

            newEntity.setFileUrl(oldEntity.getFileUrl());
            newEntity.setFilePath(oldEntity.getFilePath());
            newEntity.setFileName(oldEntity.getFileName());
            newEntity.setFileSize(oldEntity.getFileSize());
            newEntity.setDownloadSize(oldEntity.getDownloadSize());
            newEntity.setFileType(oldEntity.getFileType());

            newEntity.setApp_id(oldEntity.getApp_id());
            newEntity.setApp_intro(oldEntity.getApp_intro());
            newEntity.setApp_name(oldEntity.getApp_name());
            newEntity.setDisplay(oldEntity.getDisplay());
            newEntity.setGame_id(oldEntity.getGame_id());
            newEntity.setAd_id(oldEntity.getAd_id());
            newEntity.setI_download_url(oldEntity.getI_download_url());
            newEntity.setFlag(oldEntity.getFlag());
            newEntity.setPlay_num(oldEntity.getPlay_num());
            newEntity.setPlay_text(oldEntity.getPlay_text());
            newEntity.setSize_num(oldEntity. getSize_num());
            newEntity.setSize_text(oldEntity.getSize_text());
            newEntity.setType_id(oldEntity.getType_id());
            try {
                xUtilsDb.DB.update(newEntity,
                        new String[]{ FileDownloaderEntity.FILE_URL,
                                FileDownloaderEntity.FILE_PATH,
                                FileDownloaderEntity.FILE_NAME,
                                FileDownloaderEntity.FILE_SIZE,
                                FileDownloaderEntity.DOWNLOAD_SIZE,
                                FileDownloaderEntity.FILE_TYPE,
                                FileDownloaderEntity.FILE_TYPE,

                                FileDownloaderEntity.APP_ID,
                                FileDownloaderEntity.APP_INTRO,
                                FileDownloaderEntity.APP_NAME,
                                FileDownloaderEntity.DISPLAY,
                                FileDownloaderEntity.AD_ID,
                                FileDownloaderEntity.GAME_ID,
                                FileDownloaderEntity.I_DOWNLOAD_URL,
                                FileDownloaderEntity.FLAG,
                                FileDownloaderEntity.PLAY_NUM,
                                FileDownloaderEntity.PLAY_TEXT,
                                FileDownloaderEntity.SIZE_NUM,
                                FileDownloaderEntity.SIZE_TEXT,
                                FileDownloaderEntity.TYPE_ID,

                                FileDownloaderEntity.AD_LOCATION_ID});
                Log.d(TAG, "saveOrUdateByFileUrl: 2");
                return 1;
            } catch (DbException ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 查找（fileUrl）
     */
    public static FileDownloaderEntity findByFileUrl(String fileUrl) {
        try {
            return xUtilsDb.DB
                    .selector(FileDownloaderEntity.class)
                    .where(FileDownloaderEntity.FILE_URL, "=", fileUrl)
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查找（ad_location_id）
     */
    public static FileDownloaderEntity findByAdLocationId(int ad_location_id) {
        try {
            return xUtilsDb.DB
                    .selector(FileDownloaderEntity.class)
                    .where(FileDownloaderEntity.AD_LOCATION_ID, "=", ad_location_id)
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查找（game_id）
     */
    public static FileDownloaderEntity findByGameId(String game_id) {
        try {
            return xUtilsDb.DB
                    .selector(FileDownloaderEntity.class)
                    .where(FileDownloaderEntity.GAME_ID, "=", game_id)
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查找
     */
    public static List<FileDownloaderEntity> findAllAdvertisement() {
        try {
            return xUtilsDb.DB
                    .selector(FileDownloaderEntity.class)
                    .where(FileDownloaderEntity.FILE_TYPE, "=", FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除所有广告（game_id）
     */
    public static void deleteAllAdvertisement(String game_id) {
        List<FileDownloaderEntity> list = findAllAdvertisement();
        Log.d(TAG, "deleteAllAdvertisement: list=" + list);
        Log.d(TAG, "deleteAllAdvertisement: game_id=" + game_id);
        if (list != null && list.size() > 0) {
            for (FileDownloaderEntity entity : list) {
                String id = entity.getGame_id();
                if (game_id == null || !game_id.equals(id)) {
                    deleteByFileUrl(entity.getFileUrl());
                }
            }
        }
    }

    /**
     * 删除（fileUrl）
     */
    public static boolean deleteByFileUrl(String fileUrl) {
        FileDownloaderEntity entity = findByFileUrl(fileUrl);
        if (entity != null)
            try {
                xUtilsDb.DB.delete(entity);
                Log.d(TAG, "deleteByFileUrl: true");
                return true;
            } catch (DbException e) {
                e.printStackTrace();
            }
        return false;
    }

    /**
     * 删除（ad_location_id）
     */
    public static boolean deleteByAdLocationId(int ad_location_id) {
        FileDownloaderEntity entity = findByAdLocationId(ad_location_id);
        if (entity != null)
            try {
                xUtilsDb.DB.delete(entity);
                Log.d(TAG, "deleteByAdLocationId: true");
                return true;
            } catch (DbException e) {
                e.printStackTrace();
            }
        return false;
    }

    /**
     * 删除（game_id）
     */
    public static boolean deleteByGameId(String game_id) {
        FileDownloaderEntity entity = findByGameId(game_id);
        if (entity != null)
            try {
                xUtilsDb.DB.delete(entity);
                Log.d(TAG, "deleteByGameId: true");
                return true;
            } catch (DbException e) {
                e.printStackTrace();
            }
        return false;
    }

    /**
     * 删除（FileType）
     */
    public static boolean deleteByFileTypeFeiMo(){
        List<FileDownloaderEntity> entities = findAllFileTypeFeiMo();
        if (entities != null){
            try {
                for (FileDownloaderEntity entity : entities){
                    xUtilsDb.DB.delete(entity);
                }
                return true;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 查找
     */
    public static List<FileDownloaderEntity> findAllFileTypeFeiMo(){
        try {
            return xUtilsDb.DB
                    .selector(FileDownloaderEntity.class)
                    .where(FileDownloaderEntity.FILE_TYPE, "=", FileDownloaderEntity.FILE_TYPE_FEIMO)
                    .findAll();
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
