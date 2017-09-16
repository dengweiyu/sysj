package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 *
 */

public class GoodAndStartEvent extends BaseEntity {
    public final static int TYPE_GOOD = 1;
    public final static int TYPE_START = 2;


    private int type;
    private String videoId;
    private boolean isPositive;

    public GoodAndStartEvent(int type, String videoId, boolean isPositive) {
        this.type = type;
        this.videoId = videoId;
        this.isPositive = isPositive;
    }

    public GoodAndStartEvent() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public void setPositive(boolean positive) {
        isPositive = positive;
    }
}
