package com.li.videoapplication.impl;

import android.widget.TextView;

import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;

/**
 * 订单状态：10  申请退款中
 */

public class OrderRefundingStrategy extends OrderStrategy {
    public OrderRefundingStrategy(PlayWithOrderDetailEntity orderDetail, int role) {
        super(orderDetail, role);
    }

    @Override
    public void renderLeftButton(TextView left) {
        setText(left,"私聊",false);
    }

    @Override
    public void renderRightButton(TextView right) {
        if (isOwner()){
            setText(right,"再来一单",true);
        }else {
            setText(right,"确认退款",true);
        }
    }

    @Override
    public void onClickLeftButton() {
        chat();
    }

    @Override
    public void onClickRightButton() {
        if (isOwner()){
            createOrder();
        }else {
            confirmRefund();
        }
    }
}
