package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 分享本地视频成功
 */

public class SharedSuccessEvent extends BaseEntity {
    private String channel;

    public SharedSuccessEvent(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
