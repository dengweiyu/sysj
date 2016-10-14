package com.li.videoapplication.data.model.event;

import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.framework.BaseEntity;

/**
 * 组件间的通讯事件：分享
 */
@SuppressWarnings("serial")
public class Share2VideoShareEvent extends BaseEntity {

    private String shareChannel;

    public String getShareChannel() {
        return shareChannel;
    }

    public void setShareChannel(String shareChannel) {
        this.shareChannel = shareChannel;
    }
}
