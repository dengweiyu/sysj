package com.li.videoapplication.impl;

import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;

/**
 * 订单状态：2 待接单
 */

public class OrderTakingStrategy extends OrderStrategy {
    public OrderTakingStrategy(PlayWithOrderDetailEntity orderDetail, int role) {
        super(orderDetail, role);
    }

    @Override
    public void renderLeftButton(TextView left) {
        setText(left,"私聊",false);
    }

    @Override
    public void renderRightButton(TextView right) {
        if (isOwner()){
            right.setVisibility(View.GONE);
        }else {
            setText(right,"确认接单",true);
        }

    }

    @Override
    public void onClickLeftButton() {
        chat();
    }

    @Override
    public void onClickRightButton() {
        confirmTakeOrder();
    }
}
