package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 *
 */

public class SwitchChoiceGameEvent extends BaseEntity{

    private int index;

    private String typeId;

    public SwitchChoiceGameEvent(int index, String typeId) {
        this.index = index;
        this.typeId = typeId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
