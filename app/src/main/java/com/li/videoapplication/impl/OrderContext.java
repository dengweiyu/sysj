package com.li.videoapplication.impl;

import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;
import com.li.videoapplication.interfaces.IOrderStrategy;
import com.li.videoapplication.interfaces.IShowDialogListener;
import com.li.videoapplication.interfaces.IStrategyController;
import com.li.videoapplication.ui.activity.CreatePlayWithOrderActivity;
import com.li.videoapplication.ui.activity.PlayWithOrderDetailActivity;


/**
 *订单状态不能本地维护 因此没用状态机
 */

public class OrderContext implements IStrategyController ,IOrderStrategy  {
    private IOrderStrategy mStrategy;
    private IShowDialogListener mShowDialogListener;
    public void setOrderState(PlayWithOrderDetailEntity orderEntity, int role){
        mStrategy = getStrategy(orderEntity,role);
    }

    @Override
    public IOrderStrategy getStrategy(PlayWithOrderDetailEntity orderEntity,int role) {
        IOrderStrategy strategy = null;
        if (role == PlayWithOrderDetailActivity.ROLE_TOURIST){
            return strategy;
        }
        if (orderEntity != null){
            switch (orderEntity.getData().getStatusX()){
                case "0":           //待支付

                    strategy = new OrderPayingStrategy(orderEntity,role);
                    break;
                case "1":           //支付失败
                    strategy = new OrderPayFailStrategy(orderEntity,role);
                    break;
                case "2":           //待接单   不处于抢单模式下
                    if (orderEntity.getCoach() != null){
                        strategy = new OrderTakingStrategy(orderEntity,role);
                    }
                    break;
                case "3":           //陪练开始
                    strategy = new OrderPlayingStrategy(orderEntity,role);
                    break;
                case "4":           //教练已提交结果 待用户确认
                    strategy = new OrderHasResultStrategy(orderEntity,role);
                    break;
                case "5":           //完成订单
                    strategy = new OrderDoneStrategy(orderEntity,role);
                    break;
                case "10":          //退款申请中
                    strategy = new OrderRefundingStrategy(orderEntity,role);
                    break;
                case "11":          //退款完成
                    strategy = new OrderRefundDoneStrategy(orderEntity,role);
                    break;

                case "14":         //派单失败
                    strategy = new AutoSendOderFailed(orderEntity,role);
                    break;
                default:            //12:拒绝退款 13：订单被取消

                    break;
            }
        }


        return strategy;
    }

    @Override
    public void renderLeftButton(TextView left) {
        if (mStrategy == null){
            left.setVisibility(View.GONE);
            return;
        }
        mStrategy.renderLeftButton(left);
    }

    @Override
    public void renderRightButton(TextView right) {
        if (mStrategy == null){
            right.setVisibility(View.GONE);
            return;
        }
        mStrategy.renderRightButton(right);
    }

    @Override
    public void onClickLeftButton() {
        if (mStrategy == null){
            return;
        }
        mStrategy.onClickLeftButton();
    }

    @Override
    public void onClickRightButton() {
        if (mStrategy == null){
            return;
        }
        mStrategy.onClickRightButton();
    }

    public void setShowDialogListener(IShowDialogListener showDialogListener) {
        mShowDialogListener = showDialogListener;
        if (mStrategy != null && mStrategy instanceof OrderStrategy){
            ((OrderStrategy)mStrategy).setLoadingDialogListener(mShowDialogListener);
        }
    }
}
