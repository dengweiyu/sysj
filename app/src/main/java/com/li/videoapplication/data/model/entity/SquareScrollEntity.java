package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 广场内层ViewPager滚动事件
 */

public class SquareScrollEntity extends BaseEntity {

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
