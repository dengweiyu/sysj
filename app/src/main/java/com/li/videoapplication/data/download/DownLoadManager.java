
package com.li.videoapplication.data.download;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.li.videoapplication.data.DownloadingNotificationManager;
import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.data.database.FileDownloaderManager;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.utils.ApkUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 下载文件管理类
 */
public class DownLoadManager {

    public static final String TAG = DownLoadManager.class.getSimpleName();

    private static DownLoadManager instance;

    /**
     * 初始化下载器
     */
    public static DownLoadManager getInstance() {
        if (instance == null)
            synchronized (DownLoadManager.class) {
                if (instance == null)
                    instance = new DownLoadManager();
            }
        return instance;
    }

    /**
     * 销毁下载器
     */
    public static void destruction() {
        if (instance != null)
            instance.destroy();
        instance = null;
    }

    // -----------------------------------------------------------------------------

    private List<DownLoadListener> downLoadListeners = new ArrayList<>();
    private List<DownLoader> downLoaders = new ArrayList<>();

    /**
     * 初始化下载器
     */
    private DownLoadManager() {
        super();

        Log.d(TAG, "###################################################################");
        Log.d(TAG, "###################################################################");
        Log.d(TAG, "###################################################################");
        Log.d(TAG, "###################################################################");

        // 从数据库恢复下载任务
        List<FileDownloaderEntity> list = FileDownloaderManager.findAll();
        Log.d(TAG, "DownLoadManager: list=" + list);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                FileDownloaderEntity entity = list.get(i);
                DownLoader downLoader = new DownLoader(entity);
                downLoader.addAllDownLoadListeners(downLoadListeners);
                downLoaders.add(downLoader);
            }
        }

        // 销毁通知栏
        DownloadingNotificationManager.destruction();
        // 增加所有任务一个监听
        addAllDownLoadListener(downLoadListener);
    }

    /**
     * 销毁下载器
     */
    private void destroy() {

        // 销毁通知栏
        DownloadingNotificationManager.destruction();
        // 停止所有任务
        stopAllDownLoader();
        // 清除所有任务所有监听
        clearDownLoadListener();
    }

    // -----------------------------------------------------------------------------

    /**
     * 下载文件监听
     */
    private DownLoadListener downLoadListener = new DownLoadListener() {

        @Override
        public void preStart(FileDownloaderEntity entity) {
            Log.d(TAG, "preStart: // ----------------------------------------------");
            Log.d(TAG, "preStart: entity=" + entity);
            if (entity != null) {
                Log.d(TAG, "preStart: downloadSize=" + entity.getDownloadSize());
                File tmpFile = SYSJStorageUtil.createTmpApkPath(entity.getFileUrl());
                if (tmpFile == null || !tmpFile.exists()) {
                    Log.d(TAG, "preStart: true");
                }
            }
        }

        @Override
        public void onStart(FileDownloaderEntity entity) {
            Log.d(TAG, "onStart: // ----------------------------------------------");
            Log.d(TAG, "onStart: entity=" + entity);
            if (entity != null &&
                    entity.getFileType() != null &&
                    entity.getFileType().equals(FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT)) {
                // 更新通知栏
                DownloadingNotificationManager.getInstance().updateNotification(entity.getFileUrl(), entity);
            }
        }

        @Override
        public void onProgress(FileDownloaderEntity entity) {
            Log.d(TAG, "onProgress: // ----------------------------------------------");
            Log.d(TAG, "onProgress: entity=" + entity);
            if (entity != null &&
                    entity.getFileType() != null &&
                    entity.getFileType().equals(FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT)) {
                // 更新通知栏
                DownloadingNotificationManager.getInstance().updateNotification(entity.getFileUrl(), entity);
            }
        }

        @Override
        public void onStop(FileDownloaderEntity entity) {
            Log.d(TAG, "onStop: // ----------------------------------------------");
            Log.d(TAG, "onStop: entity=" + entity);
            if (entity != null &&
                    entity.getFileType() != null &&
                    entity.getFileType().equals(FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT)) {
                // 更新通知栏
                DownloadingNotificationManager.getInstance().updateNotification(entity.getFileUrl(), entity);
            }
        }

        @Override
        public void onSuccess(FileDownloaderEntity entity) {
            Log.d(TAG, "onSuccess: // ----------------------------------------------");
            Log.d(TAG, "onSuccess: entity=" + entity);
            if (entity != null &&
                    entity.getFileType() != null &&
                    entity.getFileType().equals(FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT)) {
                // 更新通知栏
                DownloadingNotificationManager.getInstance().updateNotification(entity.getFileUrl(), entity);

                Activity activity = AppManager.getInstance().getActivity(MainActivity.class);
                Context context = AppManager.getInstance().getContext();
                File apkFile = SYSJStorageUtil.createApkPath(entity.getFileUrl());
                if (apkFile != null) {
                    // 广告点击统计（13——下载成功）
//                    DataManager.advertisementAdClick204_13(entity.getAd_id());
                    if (activity != null) {
                        // 安装应用
                        ApkUtil.installApp(activity, apkFile.getPath());
                        Log.d(TAG, "onSuccess: activity=" + activity);
                    }
                    if (context != null) {
                        // 安装应用
                        ApkUtil.installApp(context, apkFile.getPath());
                        Log.d(TAG, "onSuccess: context=" + context);
                        // 广告点击统计（15——启动安装）
//                        DataManager.advertisementAdClick204_15(entity.getAd_id());
                    }
                }
            }
        }

        @Override
        public void onError(FileDownloaderEntity entity) {
            Log.d(TAG, "onError: // ----------------------------------------------");
            Log.d(TAG, "onError: entity=" + entity);
            if (entity != null &&
                    entity.getFileType() != null &&
                    entity.getFileType().equals(FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT)) {
                // 更新通知栏
                DownloadingNotificationManager.getInstance().updateNotification(entity.getFileUrl(), entity);
                // 广告点击统计（14——下载失败）
//                DataManager.advertisementAdClick204_14(entity.getAd_id());
            }
        }
    };

    // -----------------------------------------------------------------------------

    /**
     * 更新所有任务（从数据库更新到下载器）
     */
    public void updateFromDatabase() {
        Log.d(TAG, "updateFromDatabase: // --------------------------------------------------");
        Log.d(TAG, "updateFromDatabase: downLoaders=" + downLoaders);
        List<FileDownloaderEntity> list = FileDownloaderManager.findAll();
        if (list != null && list.size() > 0) {
            for (FileDownloaderEntity dbEntity : list) {// 迭代数据库下载应用信息
                FileDownloaderEntity oldEntity = getEntity(dbEntity.getFileUrl());
                if (oldEntity == null) {
                    DownLoader downLoader = new DownLoader(dbEntity);
                    downLoader.addAllDownLoadListeners(downLoadListeners);
                    downLoaders.add(downLoader);
                } else {
                    // oldEntity.setFileSize(0);
                    // oldEntity.setDownloadSize(0);
                    // oldEntity.setFileUrl(dbEntity.getFileUrl());
                    oldEntity.setFileType(dbEntity.getFileType());

                    oldEntity.setA_download_url(dbEntity.getA_download_url());
                    oldEntity.setApp_id(dbEntity.getApp_id());
                    oldEntity.setApp_intro(dbEntity.getApp_intro());
                    oldEntity.setApp_name(dbEntity.getApp_name());
                    oldEntity.setDisplay(dbEntity.getDisplay());
                    oldEntity.setGame_id(dbEntity.getGame_id());
                    oldEntity.setI_download_url(dbEntity.getI_download_url());
                    oldEntity.setFlag(dbEntity.getFlag());
                    oldEntity.setPlay_num(dbEntity.getPlay_num());
                    oldEntity.setPlay_text(dbEntity.getPlay_text());
                    oldEntity.setSize_num(dbEntity. getSize_num());
                    oldEntity.setSize_text(dbEntity.getSize_text());
                    oldEntity.setType_id(dbEntity.getType_id());
                }
            }
        }
        List<FileDownloaderEntity> entities = getAllAdvertisement();
        if (entities != null && entities.size() > 0) {// 迭代下载器广告下载应用信息
            for (FileDownloaderEntity entity : entities) {
                FileDownloaderEntity dbEntity = FileDownloaderManager.findByFileUrl(entity.getFileUrl());
                if (dbEntity == null) {
                    deleteDownLoader(entity.getFileUrl());
                }
            }
        }
    }

    /**
     * 更新所有任务（从下载器更新到适配器）
     */
    public void updateTaskToView(List<FileDownloaderEntity> viewList, List<FileDownloaderEntity> downloadList) {
        Log.d(TAG, "updateTaskToView: // -----------------------------------------------------------");
        Log.d(TAG, "updateTaskToView: oldList" + viewList);
        Log.d(TAG, "updateTaskToView: downloadList" + downloadList);
        if (downloadList == null || downloadList.size() == 0)
            return;

        for (FileDownloaderEntity downloadEntity : downloadList) {// 迭代下载器下载应用信息
            FileDownloaderEntity viewEntity = Utils_Download.getEntity(downloadEntity.getFileUrl(), viewList);
            if (viewEntity == null) {
                viewList.add(downloadEntity);
            } else {
                viewEntity.setA_download_url(downloadEntity.getA_download_url());
                viewEntity.setApp_id(downloadEntity.getApp_id());
                viewEntity.setApp_intro(downloadEntity.getApp_intro());
                viewEntity.setApp_name(downloadEntity.getApp_name());
                viewEntity.setDisplay(downloadEntity.getDisplay());
                viewEntity.setGame_id(downloadEntity.getGame_id());
                viewEntity.setI_download_url(downloadEntity.getI_download_url());
                viewEntity.setFlag(downloadEntity.getFlag());
                viewEntity.setPlay_num(downloadEntity.getPlay_num());
                viewEntity.setPlay_text(downloadEntity.getPlay_text());
                viewEntity.setSize_num(downloadEntity.getSize_num());
                viewEntity.setSize_text(downloadEntity.getSize_text());
                viewEntity.setType_id(downloadEntity.getType_id());
            }
        }
        if (viewList != null && viewList.size() > 0) {// 迭代适配器下载应用信息
            Iterator<FileDownloaderEntity> it = viewList.iterator();
            while (it.hasNext()) {
                FileDownloaderEntity entity = it.next();
                FileDownloaderEntity downloadEntity = getEntity(entity.getFileUrl());
                if (downloadEntity == null) {
                    it.remove();
                }
            }
        }
        Log.d(TAG, "updateTaskToView: viewList" + viewList);
    }

    // -----------------------------------------------------------------------------

    /**
     * 增加任务
     * 
     * @param fileUrl 请求下载的路径
     * @return -1 : 已存在任务列表 ， 1 ： 添加进任务列表
     */
    public int addDownLoader(String fileUrl) {
        Log.d(TAG, "addDownLoader: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downloader = downLoaders.get(i);
            if (downloader.getFileUrl().equals(fileUrl)) {
                return -1;
            }
        }

        File tmp = SYSJStorageUtil.createTmpApkPath(fileUrl);
        if (tmp != null) {
            FileUtil.deleteFile(tmp.getPath());
        }
        File apk = SYSJStorageUtil.createApkPath(fileUrl);
        if (apk != null) {
            FileUtil.deleteFile(apk.getPath());
        }

        FileDownloaderEntity entity = new FileDownloaderEntity();
        entity.setFileUrl(fileUrl);
        entity.setDownloadSize(0);
        entity.setFileSize(0);
        DownLoader downLoader = new DownLoader(entity);
        downLoader.addAllDownLoadListeners(downLoadListeners);
        downLoader.start();
        downLoaders.add(downLoader);
        return 1;
    }

    /**
     * 删除任务
     */
    public void deleteDownLoader(String fileUrl) {
        Log.d(TAG, "deleteDownLoader: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downLoader = downLoaders.get(i);
            if (downLoader.getFileUrl().equals(fileUrl)) {
                downLoader.clearDownLoadListeners();
                downLoader.destroy();
                downLoaders.remove(downLoader);
                break;
            }
        }
    }

    /**
     * 获取所有任务
     */
    public List<FileDownloaderEntity> getAllDownLoader() {
        Log.d(TAG, "getAllDownLoader: // --------------------------------------------------");
        List<FileDownloaderEntity> entities = new ArrayList<>();
        for (int i = 0; i < downLoaders.size(); i++) {
            entities.add(downLoaders.get(i).getEntity());
        }
        return entities;
    }

    /**
     * 获取所有任务（广告）
     */
    public List<FileDownloaderEntity> getAllAdvertisement() {
        Log.d(TAG, "getAllAdvertisement: // --------------------------------------------------");
        List<FileDownloaderEntity> entities = new ArrayList<>();
        for (int i = 0; i < downLoaders.size(); i++) {
            FileDownloaderEntity entity = downLoaders.get(i).getEntity();
            if (entity != null &&
                    entity.getFileType() != null &&
                    entity.getFileType().equals(FileDownloaderEntity.FILE_TYPE_ADVERTISEMENT)) {
                entities.add(entity);
            }
        }
        return entities;
    }

    /**
     * 获取所有任务（飞磨）
     */
    public List<FileDownloaderEntity> getAllFeimo() {
        Log.d(TAG, "getAllFeimo: // --------------------------------------------------");
        List<FileDownloaderEntity> entities = new ArrayList<>();
        for (int i = 0; i < downLoaders.size(); i++) {
            FileDownloaderEntity entity = downLoaders.get(i).getEntity();
            if (entity != null &&
                    entity.getFileType() != null &&
                    entity.getFileType().equals(FileDownloaderEntity.FILE_TYPE_FEIMO)) {
                entities.add(entity);
            }
        }
        return entities;
    }

    /**
     * 开始任务
     */
    public void startDownLoader(String fileUrl) {
        Log.d(TAG, "startDownLoader: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downLoader = downLoaders.get(i);
            Log.d(TAG, "startDownLoader: entity" + downLoader.getEntity());
            if (downLoader.getFileUrl().equals(fileUrl)) {
                if (downLoader.getEntity() != null && downLoader.getEntity().getDownloadSize() < 512) {
                    Log.d(TAG, "startDownLoader: downloadSize" + downLoader.getEntity().getDownloadSize());
                    // 广告点击统计（12——开始下载）
//                    DataManager.advertisementAdClick204_12(downLoader.getEntity().getAd_id());
                    Log.d(TAG, "startDownLoader: 12");
                }
                downLoader.start();
                break;
            }
        }
    }

    /**
     * 停止任务
     */
    public void stopDownLoader(String fileUrl) {
        Log.d(TAG, "stopDownLoader: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downLoader = downLoaders.get(i);
            if (downLoader.getFileUrl().equals(fileUrl)) {
                downLoader.stop();
                break;
            }
        }
    }

    /**
     * 开始所有任务
     */
    public void startAllDownLoader() {
        Log.d(TAG, "startAllDownLoader: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downLoader = downLoaders.get(i);
            downLoader.start();
        }
    }

    /**
     * 停止所有任务
     */
    public void stopAllDownLoader() {
        Log.d(TAG, "stopAllDownLoader: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downLoader = downLoaders.get(i);
            downLoader.stop();
        }
    }

    // -----------------------------------------------------------------------------

    /**
     * 增加某一个任务监听
     */
    public void addDownLoadListener(String fileUrl, DownLoadListener listener) {
        Log.d(TAG, "addDownLoadListeners: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downLoader = downLoaders.get(i);
            if (downLoader.getFileUrl().equals(fileUrl)) {
                downLoader.addDownLoadListeners(listener);
                break;
            }
        }
    }

    /**
     * 移除某一个任务一个监听
     */
    public void removeDownLoadListener(String fileUrl, DownLoadListener listener) {
        Log.d(TAG, "removeDownLoadListener: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downLoader = downLoaders.get(i);
            if (downLoader.getFileUrl().equals(fileUrl)) {
                downLoader.removeDownLoadListeners(listener);
                break;
            }
        }
    }

    /**
     * 清除某一个任务所有监听
     */
    public void clearDownLoadListener(String fileUrl) {
        Log.d(TAG, "clearDownLoadListener: // --------------------------------------------------");
        DownLoader downLoader = getDownloader(fileUrl);
        if (downLoader != null) {
            downLoader.clearDownLoadListeners();
        }
    }

    // -----------------------------------------------------------------------------

    /**
     * 增加所有任务一个监听
     */
    public void addAllDownLoadListener(DownLoadListener downLoadListener) {
        Log.d(TAG, "addAllDownLoadListener: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downLoader = downLoaders.get(i);
            downLoader.addDownLoadListeners(downLoadListener);
        }
    }

    /**
     * 重置所有任务所有监听
     */
    public void resetAllDownLoadListener() {
        Log.d(TAG, "resetAllDownLoadListener: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downLoader = downLoaders.get(i);
            downLoader.addAllDownLoadListeners(downLoadListeners);
        }
        addAllDownLoadListener(downLoadListener);
    }

    /**
     * 移除所有任务一个监听
     */
    public void removeAllDownLoadListener(DownLoadListener listener) {
        Log.d(TAG, "removeAllDownLoadListener: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downLoader = downLoaders.get(i);
            downLoader.removeDownLoadListeners(listener);
        }
    }

    /**
     * 清除所有任务所有监听
     */
    public void clearDownLoadListener() {
        Log.d(TAG, "removeAllDownLoadListener: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downLoader = downLoaders.get(i);
            downLoader.clearDownLoadListeners();
        }
    }

    // -----------------------------------------------------------------------------

    /**
     * 任务是否正在下载
     */
    public boolean isDownLoading(String fileUrl) {
        Log.d(TAG, "isDownLoading: // --------------------------------------------------");
        DownLoader downLoader = getDownloader(fileUrl);
        if (downLoader != null) {
            return downLoader.isDownLoading();
        }
        return false;
    }

    /**
     * 获取下载器
     */
    public DownLoader getDownloader(String fileUrl) {
        Log.d(TAG, "getDownloader: // --------------------------------------------------");
        for (int i = 0; i < downLoaders.size(); i++) {
            DownLoader downloader = downLoaders.get(i);
            if (fileUrl != null && fileUrl.equals(downloader.getFileUrl())) {
                return downloader;
            }
        }
        return null;
    }

    /**
     * 获取任务
     */
    public FileDownloaderEntity getEntity(String fileUrl) {
        Log.d(TAG, "getEntity: // --------------------------------------------------");
        DownLoader downLoader = getDownloader(fileUrl);
        if (downLoader != null && downLoader.getEntity() != null) {
            return downLoader.getEntity();
        }
        return null;
    }
}
