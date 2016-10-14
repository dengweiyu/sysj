package com.li.videoapplication.data.local;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 事件：加载图片文件夹
 */
public class ImageDirectoryResponseObject extends BaseResponseEntity {

    private List<ImageDirectoryEntity> directorys;

    public ImageDirectoryResponseObject(boolean result, String msg, List<ImageDirectoryEntity> data) {
        this.directorys = data;
        setResult(result);
        setMsg(msg);
    }

    public List<ImageDirectoryEntity> getDirectorys() {
        return directorys;
    }

    public void setDirectorys(List<ImageDirectoryEntity> directorys) {
        this.directorys = directorys;
    }
}
