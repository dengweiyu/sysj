package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 事件：上传视频完成
 */
@SuppressWarnings("serial")
public class VideoUploadCompleteEvent extends BaseEntity {

    private String video_id;

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
