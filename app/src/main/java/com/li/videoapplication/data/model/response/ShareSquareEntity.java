package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.ShareSquare;

import com.li.videoapplication.framework.BaseResponseEntity;

public class ShareSquareEntity extends BaseResponseEntity {

    private ShareSquare data;

    public ShareSquare getData() {
        return data;
    }

    public void setData(ShareSquare data) {
        this.data = data;
    }
}
