package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 搜索结果
 */

public class SearchResultEvent extends BaseEntity {
    private int position;
    private boolean isResult;

    public SearchResultEvent(int position, boolean isResult) {
        this.position = position;
        this.isResult = isResult;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isResult() {
        return isResult;
    }

    public void setResult(boolean result) {
        isResult = result;
    }
}
