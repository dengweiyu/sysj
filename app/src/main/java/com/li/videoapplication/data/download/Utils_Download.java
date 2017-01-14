package com.li.videoapplication.data.download;


import com.li.videoapplication.data.database.FileDownloaderEntity;

import java.util.Iterator;
import java.util.List;

public class Utils_Download {

    private static final String TAG = Utils_Download.class.getSimpleName();

    /**
     * 根据fileUrl获取任务
     */
    public static FileDownloaderEntity getEntity(String fileUrl, List<FileDownloaderEntity> list) {
        if (list == null || list.size() == 0)
            return null;
        if (fileUrl == null)
            return null;
        Iterator<FileDownloaderEntity> iterator = list.iterator();
        while (iterator.hasNext()) {
            FileDownloaderEntity entity = iterator.next();
            if (entity != null &&
                    entity.getFileUrl() != null &&
                    entity.getFileUrl().equals(fileUrl)) {
                return entity;
            }
        }
        return null;
    }
}
