package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * 陪玩订单价格
 */

public class PlayWithOrderPriceEntity  extends BaseResponseEntity{

    /**
     * price : 10
     */

    private float price;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
