package com.li.videoapplication.data.model.event;

import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.framework.BaseEntity;

import java.util.List;

/**
 * 事件：上传赛事结果图片
 */
@SuppressWarnings("serial")
public class UploadMatchPicEvent extends BaseEntity {

    private List<ScreenShotEntity> data;

    public List<ScreenShotEntity> getData() {
        return data;
    }

    public void setData(List<ScreenShotEntity> data) {
        this.data = data;
    }
}
