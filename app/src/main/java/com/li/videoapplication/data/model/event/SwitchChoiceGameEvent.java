package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 *
 */

public class SwitchChoiceGameEvent extends BaseEntity{

    private int index;

    public SwitchChoiceGameEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
