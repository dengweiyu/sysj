package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 未读消息红点事件
 */

public class UnReadMessageEvent extends BaseEntity {
    private int count;


    public UnReadMessageEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
