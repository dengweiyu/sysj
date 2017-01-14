package com.li.videoapplication.data.download;


import com.li.videoapplication.data.database.FileDownloaderEntity;

/**
 * 下载文件监听
 */
public interface DownLoadListener {

    /**
     * 开始下载文件
     * @param entity 下载任务对象
     */
    void preStart(FileDownloaderEntity entity);

    /**
     * 开始下载文件
     * @param entity 下载任务对象
     */
    void onStart(FileDownloaderEntity entity);
    
    /**
     * 文件下载进度情况
     * @param entity 下载任务对象
     */
    void onProgress(FileDownloaderEntity entity);
    
    /**
     * 停止下载完毕
     * @param entity 下载任务对象
     */
    void onStop(FileDownloaderEntity entity);
    
    /**
     * 文件下载失败
     * @param entity 下载任务对象
     */
    void onError(FileDownloaderEntity entity);
    
    
    /**
     * 文件下载成功
     * @param entity 下载任务对象
     */
    void onSuccess(FileDownloaderEntity entity);
}
