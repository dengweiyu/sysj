package com.li.videoapplication.ui.adapter;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.PlayWithTakeOrderEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.activity.PlayWithOrderDetailActivity;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.List;

/**
 * 陪练接单
 */

public class PlayWithTakeOrderAdapter extends BaseQuickAdapter<PlayWithTakeOrderEntity.DataBean,BaseViewHolder> {
    private String mMemberId;
    private String mOwnerId;
    private String mCoachId;
    public PlayWithTakeOrderAdapter(String memberId,String ownerId,String coachId, List<PlayWithTakeOrderEntity.DataBean> data) {
        super(R.layout.play_with_take_order_item, data);
        mMemberId = memberId;
    }

    @Override
    protected void convert(BaseViewHolder holder,final PlayWithTakeOrderEntity.DataBean data) {
        holder.setText(R.id.tv_user_nick_name,data.getNickname())
                .setText(R.id.tv_user_place_order_num,"下单数："+data.getOrderTotal());
        try {
            holder.setText(R.id.tv_order_create_time, TimeHelper.getWholeTimeFormat(data.getAdd_time()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.getView(R.id.tv_chat_with_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatWithCustomer(data.getUser_id(),data.getNickname(),data.getAvatar());
            }
        });

        final ImageView icon = (ImageView)holder.getView(R.id.civ_user_icon);
        GlideHelper.displayImage(mContext,data.getAvatar(),icon);

        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                GlideHelper.displayImage(mContext,data.getAvatar(),icon);
            }
        },500);

        TextView server = (TextView)holder.getView(R.id.tv_game_server);
        server.setText(data.getGameArea());

        TextView mode = (TextView)holder.getView(R.id.tv_game_mode);
        mode.setText(data.getGameMode());

        TextView count = (TextView)holder.getView(R.id.tv_game_count);
        count.setText(data.getInning()+"局");

        TextView status = (TextView)holder.getView(R.id.tv_order_status);
        status.setText(data.getStatusText());
        status.setTextColor(mContext.getResources().getColor(R.color.ab_backdround_red));

        TextView rank = (TextView)holder.getView(R.id.tv_game_rank);
        try {
            rank.setText(Html.fromHtml("TA的段位："+ TextUtil.toColor(data.getTrainingLevel()+"","#b8b8b8")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView priceTotal = (TextView)holder.getView(R.id.tv_game_price_total);
        try {
            priceTotal.setText(Html.fromHtml("总价："+TextUtil.toColor(data.getPrice_total()+"","#fc3c2e")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView startTime = (TextView)holder.getView(R.id.tv_game_start_time);
        try {
            startTime.setText(Html.fromHtml("开始时间："+TextUtil.toColor(TimeHelper.getWholeTimeFormat(data.getStart_time()),"#b8b8b8")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView createTime = (TextView)holder.getView(R.id.tv_game_end_time);
        try {
            createTime.setText(Html.fromHtml("下单时间："+TextUtil.toColor(TimeHelper.getWholeTimeFormat(data.getAdd_time()),"#b8b8b8")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView orderNumber = (TextView)holder.getView(R.id.tv_game_order_number);
        try {
            orderNumber.setText(Html.fromHtml("订单号："+TextUtil.toColor(data.getOrderNum(),"#b8b8b8")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        View take = holder.getView(R.id.tv_take_order);
        View textGo = holder.getView(R.id.tv_go_detail);
        View layoutGo = holder.getView(R.id.rl_go_detail);
        View imageGo = holder.getView(R.id.iv_go_detail);
        switch (data.getStatusX()){
            case "0":                   //待支付
                take.setVisibility(View.GONE);
                textGo.setVisibility(View.VISIBLE);
                imageGo.setVisibility(View.VISIBLE);
                break;
            case "1":                   //支付失败
                take.setVisibility(View.GONE);
                textGo.setVisibility(View.VISIBLE);
                imageGo.setVisibility(View.VISIBLE);
                break;
            case "2":                   //待接单
                take.setVisibility(View.VISIBLE);
                textGo.setVisibility(View.GONE);
                imageGo.setVisibility(View.GONE);
                break;
            case "3":                   //陪练中
                take.setVisibility(View.GONE);
                textGo.setVisibility(View.VISIBLE);
                imageGo.setVisibility(View.VISIBLE);
                break;

            case "4":
                take.setVisibility(View.GONE);
                textGo.setVisibility(View.VISIBLE);
                imageGo.setVisibility(View.VISIBLE);
                status.setTextColor(mContext.getResources().getColor(R.color.lpds_blue));
                break;

            case "5":                   //陪练完成
                take.setVisibility(View.GONE);
                textGo.setVisibility(View.VISIBLE);
                imageGo.setVisibility(View.VISIBLE);
                status.setTextColor(mContext.getResources().getColor(R.color.lpds_blue));
                break;
            case "10":                  //退款申请
                take.setVisibility(View.GONE);
                textGo.setVisibility(View.VISIBLE);
                imageGo.setVisibility(View.VISIBLE);
                break;
            case "11":                  //退款完成
                take.setVisibility(View.GONE);
                textGo.setVisibility(View.VISIBLE);
                imageGo.setVisibility(View.VISIBLE);
                break;
        }

        if ("2".equals(data.getStatusX())){

            take.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityManager.startPlayWithOrderDetailActivity(mContext,
                            data.getOrder_id(),
                            PlayWithOrderDetailActivity.ROLE_COACH,false);
                }
            });
            layoutGo.setOnClickListener(null);
        }else {
            layoutGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityManager.startPlayWithOrderDetailActivity(mContext,
                            data.getOrder_id(),
                            PlayWithOrderDetailActivity.ROLE_COACH,false);
                }
            });
            take.setOnClickListener(null);
        }
    }

    public String getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(String ownerId) {
        mOwnerId = ownerId;
    }

    public String getCoachId() {
        return mCoachId;
    }

    public void setCoachId(String coachId) {
        mCoachId = coachId;
    }

    /**
     * 聊天
     */
    private void chatWithCustomer(String memberId,String nickName,String avatar){

        if (memberId == null){
            ToastHelper.s("对不起，暂时无法与客户聊天~");
            return;
        }

        if (StringUtil.isNull(mMemberId)){
            DialogManager.showLogInDialog(mContext);
            return;
        }

        if (memberId.equals(mMemberId)){
            ToastHelper.s("不能和自己聊天哦~");
            return;
        }


        IMSdk.createChat(mContext,memberId,nickName,avatar, ChatActivity.SHOW_FAST_REPLY,null);
    }
}
