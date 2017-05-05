package com.li.videoapplication.data.download;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.data.database.FileDownloaderManager;
import com.li.videoapplication.data.local.FileUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.utils.ApkUtil;
import com.li.videoapplication.utils.URLUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载任务执行类
 */
public class DownLoader {

    public String TAG;

    private FileDownloaderEntity entity;
    private String fileUrl;
    private long fileSize = 0;//文件总大小
    private long downFileSize = 0;//已经下载的文件的大小
    private int downloadTimes = 0;//当前尝试请求的次数
    private static final int MAX_DOWNLOAD_TIMES = 3;//失败重新请求次数
    private boolean isDownloading = false;// 当前任务的状态
    private DownLoadTask downLoadTask;
    private List<DownLoadListener> downLoadListeners = new ArrayList<>();

    public DownLoader(FileDownloaderEntity entity){
        Log.d(TAG, "###################################################################");
        Log.d(TAG, "###################################################################");
        Log.d(TAG, "###################################################################");
        Log.d(TAG, "###################################################################");

        TAG = this.toString();

        this.entity = entity;
        this.fileSize = entity.getFileSize();
        this.downFileSize = entity.getDownloadSize();
        this.fileUrl = entity.getFileUrl();
    }

    public String getFileUrl(){
        return fileUrl;
    }

    /**
     * 当前任务进行的状态
     */
    public synchronized boolean isDownLoading(){
        return isDownloading;
    }

    /**
     * 设置当前任务进行的状态
     */
    public synchronized void setDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }

    /**
     * 保存下载信息至数据库
     */
    private void saveEntity(){
        Log.i(TAG, "saveEntity::downFileSize" + downFileSize);
        entity.setDownloadSize(downFileSize);
        entity.setDownloading(isDownLoading());
        FileDownloaderManager.saveOrUdateByFileUrl(entity);
    }

    /**
     * 获取当前任务对象
     */
    public FileDownloaderEntity getEntity(){
        Log.d(TAG, "getEntity: //--------------------------------------------------");
        Log.d(TAG, "getEntity: isDownloading=" + isDownLoading());
        Log.d(TAG, "getEntity: downFileSize=" + downFileSize);
        Log.d(TAG, "getEntity: fileSize=" + fileSize);
        Log.d(TAG, "getEntity: progress=" + entity.getProgress());

        entity.setDownloadSize(downFileSize);
        entity.setDownloading(isDownLoading());
        return entity;
    }

    /**
     * 开始
     */
    public void start(){
        if(downLoadTask == null) {
            Log.d(TAG, "start: //--------------------------------------------------");
            Log.d(TAG, "start: ");
            //3为线程池大小 大于2 则加入队列里了
            if (DownLoadExecutor.getExecutor().getActiveCount() >= 3){
                handler.sendEmptyMessage(TASK_ADD_QUEUE);
            }

            handler.sendEmptyMessage(TASK_PRESTART);
            downLoadTask = new DownLoadTask();
            DownLoadExecutor.execute(downLoadTask);
            handler.sendEmptyMessage(TASK_START);

        }
    }

    /**
     * 停止
     */
    public void stop(){
        if(downLoadTask != null) {
            Log.d(TAG, "stop: //--------------------------------------------------");
            downLoadTask.stop();
            downLoadTask = null;
        }
    }

    /**
     * 销毁
     */
    public void destroy(){
        if(downLoadTask != null) {
            Log.d(TAG, "destroy: //--------------------------------------------------");
            downLoadTask.stop();
            downLoadTask = null;
        }
        // 删除
        FileDownloaderManager.deleteByFileUrl(fileUrl);
        File tmpFile = SYSJStorageUtil.createTmpApkPath(fileUrl);
        if (tmpFile != null) {
            FileUtil.deleteFile(tmpFile.getPath());
        }
    }

    /**
     * 增加监听
     */
    public void addDownLoadListeners(DownLoadListener listener){
        if (listener == null)
            return;
        if (!downLoadListeners.contains(listener)) {
            downLoadListeners.add(listener);
        }
    }

    /**
     * 增加监听组
     */
    public void addAllDownLoadListeners(List<DownLoadListener> listeners){
        if (listeners == null && listeners.size() > 0)
            return;
        downLoadListeners.addAll(listeners);
    }

    /**
     * 移除监听
     */
    public void removeDownLoadListeners(DownLoadListener listener){
        if (listener == null)
            return;
        if (downLoadListeners.contains(listener)) {
            downLoadListeners.remove(listener);
        }
    }

    /**
     * 清除所有监听
     */
    public void clearDownLoadListeners(){
        downLoadListeners.clear();
    }

    /**
     * 文件下载任务
     */
    public class DownLoadTask implements Runnable {

        private boolean isDownloading;
        private URL mURL;
        private HttpURLConnection httpURLConnection;
        private InputStream inputStream;
        private RandomAccessFile randomAccessFile;
        private int progress = -1;

        public DownLoadTask(){
            super();
        }

        @Override
        public void run() {
            Log.d(TAG, "run: //--------------------------------------------------");
            Log.d(TAG, "run: fileUrl=" + fileUrl);
            if (fileUrl == null ||
                    !URLUtil.isURL(fileUrl)) {
                error();
                return;
            }
            setDownloading(true);
            downloadTimes = 0;
            while(downloadTimes < MAX_DOWNLOAD_TIMES){ // 做3次请求的尝试
                try {
                    if(downFileSize == fileSize
                            && fileSize > 0){
                        Log.d(TAG, "run: 1");
                        file();
                        return;
                    }
                    mURL = new URL(fileUrl);
                    if (mURL != null) {
                        httpURLConnection = (HttpURLConnection) mURL.openConnection();
                        httpURLConnection.setConnectTimeout(5000);
                        httpURLConnection.setReadTimeout(10000);
                        httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
                        Log.d(TAG, "run: 2");
                        File tmpFile;
                        File apkFile;

                        tmpFile = SYSJStorageUtil.createTmpApkPath(fileUrl);
                        if(downFileSize == 0 ||
                                tmpFile == null || !tmpFile.exists()){// 第一次下载，初始化
                            Log.d(TAG, "run: 3");

                            fileSize = 0;
                            downFileSize = 0;

                            tmpFile = SYSJStorageUtil.createTmpApkPath(fileUrl);
                            long contentLength = httpURLConnection.getContentLength();
                            Log.d(TAG, "run: contentLength=" + contentLength);
                            if(contentLength > 0 && tmpFile != null) {
                                Log.d(TAG, "run: 4");
                                randomAccessFile = new RandomAccessFile(tmpFile, "rwd");
                                randomAccessFile.setLength(contentLength);
                                fileSize = contentLength;
                                Log.d(TAG, "run: fileSize=" + fileSize);
                                entity.setFileSize(fileSize);
                                if(isDownLoading()){
                                    saveEntity();
                                }
                            } else {
                                Log.d(TAG, "run: 5");
                                error();
                            }
                        } else {// 继续下载
                            Log.d(TAG, "run: 6");
                            randomAccessFile = new RandomAccessFile(tmpFile, "rwd");
                            randomAccessFile.seek(downFileSize);
                            httpURLConnection.setRequestProperty("Range", "bytes=" + downFileSize + "-");
                        }
                        inputStream = httpURLConnection.getInputStream();
                        if (inputStream != null) {
                            Log.d(TAG, "run: 7");
                            byte[] buffer = new byte[1024 * 4];
                            int length;
                            while((length = inputStream.read(buffer)) != -1 && isDownLoading()){
                                randomAccessFile.write(buffer, 0, length);
                                downFileSize += length;
//                                Log.d(TAG, "run: length=" + length);
                                int nowProgress = (int)((100 * downFileSize) / fileSize);
                                if(nowProgress > progress){
                                    progress = nowProgress;
                                    progress();
                                }
                            }
                        } else {
                            Log.d(TAG, "run: 8");
                            error();
                        }
                        //下载完了
                        if(downFileSize == fileSize && fileSize > 0) {
                            Log.d(TAG, "run: 9");
                            file();
                        } else {
                            Log.d(TAG, "run: 10");
                            error();
                        }
                    } else {
                        error();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: 11");
                    error();
                } finally {
                    Log.d(TAG, "run: 12");
                    if(httpURLConnection != null)
                        try {
                            httpURLConnection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    if(inputStream != null)
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    if(randomAccessFile != null)
                        try {
                            randomAccessFile.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        }

        private void progress() {
            Log.d(TAG, "progress: //--------------------------------------------------");
            setDownloading(true);
            Log.d(TAG, "progress: isDownloading=" + isDownLoading());
            handler.sendEmptyMessage(TASK_PROGESS);
        }

        private void file() {
            Log.d(TAG, "file: //--------------------------------------------------");
            File tmpFile;
            File apkFile;
            Log.d(TAG, "file: fileSize=" + fileSize);
            Log.d(TAG, "file: downFileSize=" + downFileSize);
            tmpFile = SYSJStorageUtil.createTmpApkPath(fileUrl);
            apkFile = SYSJStorageUtil.createApkPath(fileUrl);
            boolean flag = false;
            try {
                // 移动文件
                flag = FileUtil.moveFile(tmpFile.getPath(), apkFile.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(flag) {
                String packageName = ApkUtil.getPackageName(apkFile.getPath());
                entity.setPackageName(packageName);
                Log.d(TAG, "file: packageName=" + packageName);
                success();
            } else {
                error();
            }
        }

        private void success() {
            Log.d(TAG, "success: //--------------------------------------------------");
            try {
                FileUtil.deleteFile(SYSJStorageUtil.createTmpApkPath(fileUrl).getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            setDownloading(false);
            fileSize = 0;
            downFileSize = 0;
            saveEntity();
            downloadTimes = MAX_DOWNLOAD_TIMES;
            downLoadTask = null;
            handler.sendEmptyMessage(TASK_SUCCESS);
        }

        private void error() {
            Log.d(TAG, "error: //--------------------------------------------------");
            setDownloading(false);
            downloadTimes = MAX_DOWNLOAD_TIMES;
            if(downFileSize > 0 && fileSize > 0){
                saveEntity();
            }
            downLoadTask = null;
            handler.sendEmptyMessage(TASK_ERROR);
        }

        public void stop(){
            Log.d(TAG, "stop: //--------------------------------------------------");
            setDownloading(false);
            downloadTimes = MAX_DOWNLOAD_TIMES;
            if(downFileSize > 0 && fileSize > 0){
                saveEntity();
            }
            handler.sendEmptyMessage(TASK_STOP);
        }
    }

    private final int TASK_START = 0;
    private final int TASK_STOP = 1;
    private final int TASK_PROGESS = 2;
    private final int TASK_ERROR = 3;
    private final int TASK_SUCCESS = 4;
    private final int TASK_PRESTART = 5;
    private final int TASK_ADD_QUEUE = -1;

    /**
     * 消息处理
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {

        private long index = 0;

        @Override
        public void handleMessage(Message msg) {
            ++ index;
            Log.d(TAG, "handleMessage: index=" + index);
            if(msg.what == TASK_START){ // 开始下载
                onStart();
            } else if(msg.what == TASK_STOP){ // 停止下载
                onStop();
            } else if(msg.what == TASK_PROGESS){ // 改变进程
                onProgress();
            } else if(msg.what == TASK_ERROR){ // 下载出错
                onError();
            } else if(msg.what == TASK_SUCCESS){ // 下载完成
                onSuccess();
            } else if(msg.what == TASK_PRESTART){ // 开始下载
                preStart();
            }else if (msg.what == TASK_ADD_QUEUE){//加入队列等待执行
                addQueue();
            }
        }

        /**
         * 通知监听器，任务已开始下载
         */
        private void onStart() {
            for (DownLoadListener listener : downLoadListeners) {
                listener.onStart(getEntity());
            }
        }

        /**
         * 通知监听器，任务已停止
         */
        private void onStop() {
            for (DownLoadListener listener : downLoadListeners) {
                listener.onStop(getEntity());
            }
        }

        /**
         * 通知监听器，任务进度
         */
        private void onProgress() {
            for (DownLoadListener listener : downLoadListeners) {
                listener.onProgress(getEntity());
            }
        }

        /**
         * 通知监听器，任务异常，并进入停止状态
         */
        private void onError() {
            for (DownLoadListener listener : downLoadListeners) {
                listener.onError(getEntity());
            }
        }

        /**
         * 通知监听器，任务成功执行完毕
         */
        private void preStart() {
            for (DownLoadListener listener : downLoadListeners) {
                listener.preStart(getEntity());
            }
        }

        /**
         * 通知监听器，任务成功执行完毕
         */
        private void onSuccess() {
            for (DownLoadListener listener : downLoadListeners) {
                listener.onSuccess(getEntity());
            }
        }

        /**
         * 通知监听器，任务加入队列中等待
         */
        private void addQueue(){
            for (DownLoadListener listener : downLoadListeners) {
                listener.addQueue(getEntity());
            }
        }
    };
}