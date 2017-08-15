package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * 订单生成时间
 */

public class OrderTimeEntity extends BaseResponseEntity {


    /**
     * code : 10000
     * startTime : 1500364214
     * endTime : 1500368100
     */

    private long startTime;
    private long endTime;
    private long intervalTime;
    private long nowTime;
    private long secondTime;

    public long getSecondTime() {
        return secondTime;
    }

    public void setSecondTime(long secondTime) {
        this.secondTime = secondTime;
    }

    public long getNowTime() {
        return nowTime;
    }

    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
