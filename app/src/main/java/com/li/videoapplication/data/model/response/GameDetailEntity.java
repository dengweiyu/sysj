package com.li.videoapplication.data.model.response;


import com.li.videoapplication.data.model.entity.FGame;
import com.li.videoapplication.framework.BaseResponseEntity;

@SuppressWarnings("serial")
public class GameDetailEntity extends BaseResponseEntity {

    private FGame data;

    public FGame getData() {
        return data;
    }

    public void setData(FGame data) {
        this.data = data;
    }
}
