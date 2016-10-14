package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Advertisement;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class AdvertisementAdLocation204Entity extends BaseResponseEntity {

    private List<Advertisement> data;

    public List<Advertisement> getData() {
        return data;
    }

    public void setData(List<Advertisement> data) {
        this.data = data;
    }
}
