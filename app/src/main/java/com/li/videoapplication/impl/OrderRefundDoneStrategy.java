package com.li.videoapplication.impl;

import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;

/**
 * 订单状态：11  订单退款完成
 */

public class OrderRefundDoneStrategy extends OrderStrategy {
    public OrderRefundDoneStrategy(PlayWithOrderDetailEntity orderDetail, int role) {
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
            right.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickLeftButton() {
        chat();
    }

    @Override
    public void onClickRightButton() {
        createOrder();
    }
}
