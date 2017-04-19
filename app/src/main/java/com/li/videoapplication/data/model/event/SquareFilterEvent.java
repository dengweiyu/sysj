package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 玩家广场 游戏选择事件
 */

public class SquareFilterEvent extends BaseEntity {
    private int position;

    public SquareFilterEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
