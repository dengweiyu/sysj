package com.li.videoapplication.data.qiniu.storage;

import com.li.videoapplication.data.qiniu.http.CancellationHandler;

/**
 * 定义用户取消数据或文件上传的信号
 * 用户取消上传时，必须实现的接口
 */
public interface UpCancellationSignal extends CancellationHandler {

}
