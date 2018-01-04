package com.li.videoapplication.impl;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.event.SwitchChoiceGameEvent;
import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.interfaces.IOrderStrategy;
import com.li.videoapplication.interfaces.IShowDialogListener;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.CreatePlayWithOrderActivity;
import com.li.videoapplication.ui.activity.PlayWithOrderDetailActivity;
import com.li.videoapplication.ui.dialog.ConfirmPlayWithDialog;
import com.li.videoapplication.utils.StringUtil;

import io.rong.eventbus.EventBus;

/**
 *
 */

public abstract class OrderStrategy implements IOrderStrategy {
    protected PlayWithOrderDetailEntity mOrderDetail;
    protected int mRole;

    public OrderStrategy(PlayWithOrderDetailEntity orderDetail, int role) {
        mOrderDetail = orderDetail;
        mRole = role;
    }

    //是否为用户
    protected boolean isOwner(){
        return mRole == PlayWithOrderDetailActivity.ROLE_OWNER;
    }

    //私聊
    protected void chat(){
        Member user = PreferencesHepler.getInstance().getUserProfilePersonalInformation();
        if (user == null || StringUtil.isNull(user.getId())){
            DialogManager.showLogInDialog(AppManager.getInstance().currentActivity());
            return;
        }

        if (!Proxy.getConnectManager().isConnect()) {
            FeiMoIMHelper.Login(user.getId(), user.getNickname(), user.getAvatar());
        }

        String memberId;
        String nickName;
        String avatar;
        if (isOwner()){
            memberId = mOrderDetail.getCoach().getMember_id();
            nickName = mOrderDetail.getCoach().getNickname();
            avatar = mOrderDetail.getCoach().getAvatar();
        }else {
            memberId = mOrderDetail.getUser().getMember_id();
            nickName = mOrderDetail.getUser().getNickname();
            avatar = mOrderDetail.getUser().getAvatar();
        }

        if (user.getId().equals(memberId)){
            ToastHelper.s("不能和自己聊天哦~");
            return;
        }

        String qq = null;
        if (isOwner()){
            qq = mOrderDetail.getCoach().getQq();
        }

        IMSdk.createChat(
                AppManager.getInstance().currentActivity(),
                memberId,
                nickName,
                avatar,
                ChatActivity.SHOW_FAST_REPLY,qq);
    }

    protected void setText(TextView text,String content,boolean isPositive){
        text.setVisibility(View.VISIBLE);
        text.setText(content);
        if (isPositive){
            text.setBackgroundResource(R.drawable.background_press_red);
            text.setTextColor(Color.WHITE);
        }else {
            text.setBackgroundResource(R.drawable.background_press);
            text.setTextColor(Color.parseColor("#575757"));
        }
    }

    //立即支付
    protected void payNow(){
        Member user = PreferencesHepler.getInstance().getUserProfilePersonalInformation();
        float coin = 0;
        float priceTotal = 0;

        try {
            coin = Float.parseFloat((user.getCoin()));
            priceTotal = Float.parseFloat(mOrderDetail.getData().getPrice_total());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ConfirmPlayWithDialog(AppManager.getInstance().currentActivity(),priceTotal,coin, CreatePlayWithOrderActivity.PAGE_ORDER_DETAIL).show();
    }

    //再来一单
    protected void createOrder(){
        int orderMode = mOrderDetail.getData().getOrder_mode().equals("2")?CreatePlayWithOrderActivity.MODE_ORDER_GRAB:CreatePlayWithOrderActivity.MODE_ORDER_NORMAL;

        ActivityManager.startCreatePlayWithOrderActivity(
                AppManager.getInstance().currentActivity(),
                orderMode,
                mOrderDetail.getCoach().getMember_id(),
                mOrderDetail.getCoach().getNickname(),
                mOrderDetail.getCoach().getAvatar(),
                mOrderDetail.getCoach().getQq(),
                mOrderDetail.getData().getTraining_type_id(),
                mOrderDetail.getData().getGameName());

/*        if (orderMode == CreatePlayWithOrderActivity.MODE_ORDER_AGAIN){                                                      //抢单模式
            Intent intent = new Intent();
            intent.setClass(AppManager.getInstance().currentActivity(), CreatePlayWithOrderActivity.class);
            intent.putExtra("order_mode",CreatePlayWithOrderActivity.MODE_ORDER_AGAIN);
            intent.putExtra("coach_bean",mOrderDetail.getCoach());
            AppManager.getInstance().currentActivity().startActivity(intent);
        }else {                                                     //普通模式
            ActivityManager.startCreatePlayWithOrderActivity(
                    AppManager.getInstance().currentActivity(),
                    mOrderDetail.getCoach().getMember_id(),
                    mOrderDetail.getCoach().getNickname(),
                    mOrderDetail.getCoach().getAvatar(),
                    mOrderDetail.getCoach().getQq());
        }*/

    }

    //抢单模式
    protected void createGrabOrder(){
        ActivityManager.startCreatePlayWithOrderActivity(AppManager.getInstance().currentActivity(),CreatePlayWithOrderActivity.MODE_ORDER_GRAB,null,null,null,null,mOrderDetail.getData().getTraining_type_id(),mOrderDetail.getData().getGameName());
    }

    //吃鸡游戏的再来一单会跳转到陪练列表
    protected void choiceNewCoach(){
        //启动页面
        ActivityManager.startMainActivity(AppManager.getInstance().currentActivity(),3,0);
        //传递新的类型
        EventBus.getDefault().post(new SwitchChoiceGameEvent(-1,mOrderDetail.getData().getTraining_type_id()));
    }

    //确认接单
    protected void confirmTakeOrder(){
        Member user = PreferencesHepler.getInstance().getUserProfilePersonalInformation();
        DataManager.confirmTakeOrder(user.getId(),mOrderDetail.getData().getOrder_id());
        invokeListener();
    }

    //提交结果
    protected void commitOrderResult(){
        ActivityManager.startConfirmOrderDoneActivity(
                AppManager.getInstance().currentActivity(),
                mOrderDetail.getCoach().getNickname(),
                mOrderDetail.getCoach().getAvatar(),
                mOrderDetail.getData().getOrder_id(),
                mOrderDetail.getData().getInning(),
                mOrderDetail.getUser().getOrderCount(),
                mOrderDetail.getData().getTraining_type_id());
    }

    //用户确认教练提交的游戏结果
    protected void userConfirmResult(){
        Member user = PreferencesHepler.getInstance().getUserProfilePersonalInformation();
        DataManager.confirmOrderDone(user.getId(),mOrderDetail.getData().getOrder_id());
        invokeListener();
    }

    //评价
    protected void commentOrder(){
        ActivityManager.startPlayWithOrderCommentActivity(
                AppManager.getInstance().currentActivity(),
                mOrderDetail.getCoach().getMember_id(),
                mOrderDetail.getCoach().getNickname(),
                mOrderDetail.getCoach().getAvatar(),
                mOrderDetail.getCoach().getScore(),
                mOrderDetail.getData().getOrder_id());
    }

    //确认退款
    protected void confirmRefund(){
        Member user = PreferencesHepler.getInstance().getUserProfilePersonalInformation();
        DataManager.coachConfirmRefund(user.getId(),mOrderDetail.getData().getOrder_id());
        invokeListener();
    }

    private IShowDialogListener mShowDialogListener;
    //显示加载进度条
   public void setLoadingDialogListener(IShowDialogListener listener){
       mShowDialogListener = listener;
   }

   private void invokeListener(){
       if (mShowDialogListener != null){
           mShowDialogListener.onShowDialog(true);
       }
   }

}
