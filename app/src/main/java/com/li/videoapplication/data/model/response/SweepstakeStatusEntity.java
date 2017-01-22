package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;


public class SweepstakeStatusEntity extends BaseResponseEntity {

    private String cost;
    private String url;

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
