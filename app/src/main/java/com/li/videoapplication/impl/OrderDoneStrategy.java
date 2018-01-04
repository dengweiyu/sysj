package com.li.videoapplication.impl;

import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;

/**
 * 订单状态：5  订单结束 待评价
 */

public class OrderDoneStrategy extends OrderStrategy {

    private int mCommentCount = 0;
    public OrderDoneStrategy(PlayWithOrderDetailEntity orderDetail, int role) {
        super(orderDetail, role);
        try {
            mCommentCount = Integer.parseInt(mOrderDetail.getData().getEvaluate_counter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renderLeftButton(TextView left) {
        if (isOwner()){
            if (mCommentCount == 0){
                setText(left,"评价",false);
            }else if (mCommentCount == 1){
                setText(left,"修改评价",false);
            }else {
                left.setVisibility(View.GONE);
            }
        }else {
            setText(left,"私聊",false);
        }
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
        if (isOwner()){
            commentOrder();
        }else {
            chat();
        }
    }

    @Override
    public void onClickRightButton() {

        if ("2".equals(mOrderDetail.getData().getTraining_type_id())){
            choiceNewCoach();
        }else {
            createOrder();
        }
    }
}
