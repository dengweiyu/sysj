package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 立即支付
 */

public class PayNowEvent extends BaseEntity {
    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public PayNowEvent(int page) {
        this.page = page;
    }
}
