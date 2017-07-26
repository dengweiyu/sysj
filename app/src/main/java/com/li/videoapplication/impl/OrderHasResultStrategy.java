package com.li.videoapplication.impl;

import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;

/**
 * 订单状态：4  教练已提交结果等待用户确认
 */

public class OrderHasResultStrategy extends OrderStrategy {
    public OrderHasResultStrategy(PlayWithOrderDetailEntity orderDetail, int role) {
        super(orderDetail, role);
    }

    @Override
    public void renderLeftButton(TextView left) {
        if (isOwner()){
            setText(left,"确认完成",true);
        }else {
            setText(left,"私聊",false);
        }

    }

    @Override
    public void renderRightButton(TextView right) {
        if (isOwner()){
            setText(right,"再来一单",false);
        }else {
            right.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClickLeftButton() {
        if (isOwner()){
            userConfirmResult();
        }else {
            chat();
        }

    }

    @Override
    public void onClickRightButton() {
        createOrder();
    }
}
