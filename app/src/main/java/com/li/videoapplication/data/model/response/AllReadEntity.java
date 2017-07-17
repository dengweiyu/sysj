package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

@SuppressWarnings("serial")
public class AllReadEntity extends BaseResponseEntity {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AllReadEntity(String type) {
        this.type = type;
    }
}
