package com.li.videoapplication.data.network;

/**
 * Http网络访问方法
 */
public interface AbsRequestMethod {

    /**
     * Http执行网络请求
     */
    ResponseObject execute(RequestObject requestObject) throws Exception;

    /**
     * Get方法
     */
    void doGet();

    /**
     * Post方法
     */
    void doPost();

    /**
     * 上传文件
     */
    void uploadFile();

    /**
     * 下載文件
     */
    void downloadFile();

    /**
     * 取消请求
     */
    boolean cancel();
}
