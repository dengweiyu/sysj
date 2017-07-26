package com.li.videoapplication.impl;

import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;

/**
 * 订单状态：1  支付失败
 */

public class OrderPayFailStrategy extends OrderStrategy {
    public OrderPayFailStrategy(PlayWithOrderDetailEntity orderDetail, int role) {
        super(orderDetail, role);
    }

    @Override
    public void renderLeftButton(TextView left) {
        setText(left,"支付失败",false);
    }

    @Override
    public void renderRightButton(TextView right) {
        if (isOwner()){
            setText(right,"再来一单",true);
        }else {
            right.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickLeftButton() {
        //do nothing
    }

    @Override
    public void onClickRightButton() {
        createOrder();
    }
}
