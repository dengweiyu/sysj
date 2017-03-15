package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 事件：云端视频申请推荐位
 */
public class CloudVideoRecommendEvent extends BaseEntity {

    private String video_id;

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
