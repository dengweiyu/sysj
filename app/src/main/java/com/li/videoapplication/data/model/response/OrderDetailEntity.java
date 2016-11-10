package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.framework.BaseResponseEntity;

public class OrderDetailEntity extends BaseResponseEntity {

    private Currency data;

    public Currency getData() {
        return data;
    }

    public void setData(Currency data) {
        this.data = data;
    }
}
