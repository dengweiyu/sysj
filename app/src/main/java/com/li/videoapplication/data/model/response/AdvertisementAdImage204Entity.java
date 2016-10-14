package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Advertisement;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class AdvertisementAdImage204Entity extends BaseResponseEntity {

    private List<Advertisement> data;

    private int changetime;

    public List<Advertisement> getData() {
        return data;
    }

    public void setData(List<Advertisement> data) {
        this.data = data;
    }

    public int getChangetime() {
        return changetime;
    }

    public void setChangetime(int changetime) {
        this.changetime = changetime;
    }
}
