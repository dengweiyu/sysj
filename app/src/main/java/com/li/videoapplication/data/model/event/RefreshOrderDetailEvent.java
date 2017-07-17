package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 订单状态需要刷新
 */

public class RefreshOrderDetailEvent extends BaseEntity {

    private String orderId;
    public String status;
    public String statusText;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public RefreshOrderDetailEvent(String orderId, String status, String statusText) {
        this.orderId = orderId;
        this.status = status;
        this.statusText = statusText;
    }
}
