package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * Created by y on 2018/4/19.
 */

public class VideoPlayDurationEntity extends BaseResponseEntity {

    private List<String> erData;

    public List<String> getErData() {
        return erData;
    }

    public void setErData(List<String> erData) {
        this.erData = erData;
    }
}
