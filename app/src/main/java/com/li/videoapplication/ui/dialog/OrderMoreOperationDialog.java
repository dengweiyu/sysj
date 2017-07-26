package com.li.videoapplication.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.PopupWindowCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.CustomerInfoEntity;
import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.PlayWithOrderDetailActivity;
import com.li.videoapplication.utils.ScreenUtil;

import io.rong.eventbus.EventBus;

/**
 * 退款 , 客服
 */

public class OrderMoreOperationDialog extends PopupWindow implements View.OnClickListener{
    private Context mContext;
    private View mRootView;
    private View mAnchor;
    private PlayWithOrderDetailEntity mOrderDetail;

    private  CustomerInfoEntity mCustomerEntity;
    private Member mUser;
    private int mRole;
    public OrderMoreOperationDialog(Context context, Member member,View anchor, PlayWithOrderDetailEntity entity,int role) {
        super();
        mContext = context;
        mAnchor = anchor;
        mOrderDetail = entity;
        mRole = role;
        mUser = member;
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_more_operation,null);
        mRootView.findViewById(R.id.tv_refund_apply).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_chat_with_customer).setOnClickListener(this);
        setTouchable(true);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        setWidth(mRootView.getMeasuredWidth());
        setHeight(mRootView.getMeasuredHeight());
        setContentView(mRootView);

    }

    public void show(){
        int offsetX = -mRootView.getMeasuredWidth()*2/3;

        //Y偏移
        int offsetY = -ScreenUtil.dp2px(5);
        showAsDropDown(mAnchor,offsetX,offsetY, Gravity.BOTTOM);
    }

        @Override
        public void onClick(View v) {

            System.out.println("AAA:"+mRole+"  "+mOrderDetail.getData().getStatusX());
            switch (v.getId()){
                case R.id.tv_refund_apply:
                    if (mOrderDetail == null){
                        ToastHelper.s("订单获取失败哦~");
                        return;
                    }
                    switch (mOrderDetail.getData().getStatusX()){
                        case "0":
                            ToastHelper.s("该订单尚未支付哦~");
                            return;

                        case "1":
                            ToastHelper.s("该订单支付失败哦~");
                            return;
                        case "5":
                            ToastHelper.s("您无法对该订单进行退款了哦~如有需要请联系客服");
                            return;
                        case "10":
                            ToastHelper.s("该订单退款中哦~");
                            return;
                        case "11":
                            ToastHelper.s("该订单已退款哦~7个工作日内金额原路退还");
                            return;
                    }
                    if (mRole != PlayWithOrderDetailActivity.ROLE_OWNER){

                        if ("2".equals(mOrderDetail.getData().getStatusX())){
                            ToastHelper.s("接单后才能退款哦~");
                            return;
                        }else if ("3".equals(mOrderDetail.getData().getStatusX())){
                            //接单了 允许教练退款

                        }else {
                            ToastHelper.s("您无法对该订单进行退款了哦~如有需要请联系客服");
                            return;
                        }
                    }else {
                        int count = 0;
                        try {
                            Integer.parseInt(mOrderDetail.getData().getEvaluate_counter());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        //已经评价了 无法退款
                        if (count > 0){
                            ToastHelper.s("您无法对该订单进行退款了哦~如有需要请联系客服");
                            return;
                        }
                    }
                    ActivityManager.startRefundApplyActivity(mContext,mOrderDetail,"");
                    break;
                case R.id.tv_chat_with_customer:
                    if (mOrderDetail == null){
                        ToastHelper.s("未获取订单详情哦~");
                        return;
                    }
                    chatWithCustomer(mUser);
                    break;
            }
        }

    /**
     * 客服
     */
    private void chatWithCustomer(Member user){
        if (mCustomerEntity == null){
            ToastHelper.s("获取客服信息失败~请稍后重试");
            return;
        }

        if (!Proxy.getConnectManager().isConnect()) {
            FeiMoIMHelper.Login(user.getMember_id(), user.getNickname(), user.getAvatar());
        }

        if (mCustomerEntity.getData().getMember_id().equals(user.getId())){
            ToastHelper.s("不能和自己聊天哦~");
            return;
        }

        IMSdk.createChat(mContext,mCustomerEntity.getData().getMember_id(),mCustomerEntity.getData().getMember_name(),mCustomerEntity.getData().getIcon(),ChatActivity.SHOW_FAST_REPLY);
    }


    public void setCustomerEntity(CustomerInfoEntity customerEntity) {
        mCustomerEntity = customerEntity;
    }

    public void setOrderDetail(PlayWithOrderDetailEntity orderDetail) {
        mOrderDetail = orderDetail;
    }
}
