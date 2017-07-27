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
    private float original_price;
    private boolean isDiscount;
    private float sign_price;
    private String topMsg;

    public String getTopMsg() {
        return topMsg;
    }

    public void setTopMsg(String topMsg) {
        this.topMsg = topMsg;
    }

    public float getSign_price() {
        return sign_price;
    }

    public void setSign_price(float sign_price) {
        this.sign_price = sign_price;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public void setDiscount(boolean discount) {
        isDiscount = discount;
    }

    public float getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(float original_price) {
        this.original_price = original_price;
    }

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
