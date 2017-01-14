package com.li.videoapplication.data.download;


import com.li.videoapplication.data.database.FileDownloaderEntity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 事件：加载下载文件
 */
public class FileDownloaderResponseObject extends BaseResponseEntity {

    private String fileType;

    private List<FileDownloaderEntity> data;

    public FileDownloaderResponseObject(boolean result, String msg, List<FileDownloaderEntity> data, String fileType) {
        this.fileType = fileType;
        this.data = data;
        setResult(result);
        setMsg(msg);
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<FileDownloaderEntity> getData() {
        return data;
    }

    public void setData(List<FileDownloaderEntity> data) {
        this.data = data;
    }
}
