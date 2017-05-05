package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 广场内层ViewPager滚动事件
 */

public class SquareScrollEntity extends BaseEntity {

    private String gameId;

    public SquareScrollEntity() {
    }

    public SquareScrollEntity(String gameId, int position) {
        this.gameId = gameId;
        this.position = position;
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
