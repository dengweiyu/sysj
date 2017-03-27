package com.li.videoapplication.data.download;

import java.io.File;

public interface ReqProgressCallBack{

    /**
     * 下载进度
     * @param total
     * @param current
     */
    void onProgressUpdate(long total, long current);

    /**
     * 下载成功
     */
    void onDownLoadSuccess(File file);

    /**
     * 下载失败
     */
    void onDownLoadFail(String error);
}
