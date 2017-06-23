package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 服务器时间
 */

public class ServiceTimeEntity extends BaseResponseEntity {

    /**
     * data : []
     * timestamp : 1497074470
     */

    private long timestamp;
    private List<?> data;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
