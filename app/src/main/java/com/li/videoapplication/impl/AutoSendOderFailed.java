package com.li.videoapplication.impl;

import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;

/**
 * 订单状态：14 自动派单失败
 */

public class AutoSendOderFailed extends OrderStrategy {
    public AutoSendOderFailed(PlayWithOrderDetailEntity orderDetail, int role) {
        super(orderDetail, role);
    }

    @Override
    public void renderLeftButton(TextView left) {
        left.setVisibility(View.GONE);
    }

    @Override
    public void renderRightButton(TextView right) {
        if (isOwner()){
            setText(right,"再来一单",true);
        }
    }

    @Override
    public void onClickLeftButton() {

    }

    @Override
    public void onClickRightButton() {
        createGrabOrder();
    }
}
