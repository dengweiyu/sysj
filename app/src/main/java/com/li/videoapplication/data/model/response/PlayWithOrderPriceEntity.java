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
    private String orderNumber;
    private String promotionMsg;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPromotionMsg() {
        return promotionMsg;
    }

    public void setPromotionMsg(String promotionMsg) {
        this.promotionMsg = promotionMsg;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
