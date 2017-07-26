package com.li.videoapplication.impl;

import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;

/**
 * 订单状态：3 订单进行中
 */

public class OrderPlayingStrategy extends OrderStrategy {
    public OrderPlayingStrategy(PlayWithOrderDetailEntity orderDetail, int role) {
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
            setText(right,"提交结果",true);
        }

    }

    @Override
    public void onClickLeftButton() {
        chat();
    }

    @Override
    public void onClickRightButton() {
        commitOrderResult();
    }
}
