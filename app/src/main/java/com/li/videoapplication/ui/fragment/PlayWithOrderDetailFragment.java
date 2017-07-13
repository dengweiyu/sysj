package com.li.videoapplication.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;
import com.li.videoapplication.data.model.response.PlayWithTakeOrderEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 陪练订单详情
 */

public class PlayWithOrderDetailFragment extends TBaseFragment {

    private PlayWithOrderDetailEntity  mOrderEntity;

    private View mRootView;

    private List<Integer> mGoneView = new ArrayList<>();

    private  boolean mIsShowCoach = true;
    public static PlayWithOrderDetailFragment newInstance(PlayWithOrderDetailEntity entity,boolean isShowCoach){
        Bundle bundle = new Bundle();
        bundle.putSerializable("order_entity",entity);
        bundle.putBoolean("is_show_coach",isShowCoach);
        PlayWithOrderDetailFragment fragment = new PlayWithOrderDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_play_with_order_detail;
    }

    @Override
    protected void initContentView(View view) {
        Bundle bundle = getArguments();
        mRootView = view;

        for (Integer id:
             mGoneView) {
            View v  = mRootView.findViewById(id);
            if (v != null) {
                v.setVisibility(View.GONE);
            }
        }

        if (bundle != null){
            mOrderEntity = (PlayWithOrderDetailEntity)bundle.getSerializable("order_entity");
            mIsShowCoach = bundle.getBoolean("is_show_coach",true);
        }
        if (mOrderEntity != null){
            final PlayWithOrderDetailEntity.DataBean data = mOrderEntity.getData();
            view.findViewById(R.id.ll_coach_info_header_game_type).setVisibility(View.GONE);
            view.findViewById(R.id.iv_coach_info_go).setVisibility(View.GONE);

            final ImageView icon = (ImageView)view.findViewById(R.id.civ_coach_detail_icon);

            if (mIsShowCoach){
                GlideHelper.displayImage(getContext(),mOrderEntity.getCoach().getAvatar(),icon);

                UITask.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GlideHelper.displayImage(getContext(),mOrderEntity.getCoach().getAvatar(),icon);
                    }
                },1000);
            }else {
                GlideHelper.displayImage(getContext(),mOrderEntity.getUser().getAvatar(),icon);

                UITask.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GlideHelper.displayImage(getContext(),mOrderEntity.getUser().getAvatar(),icon);
                    }
                },1000);
            }



            TextView createOrder = (TextView) view.findViewById(R.id.tv_order_creating);
            TextView playingOrder = (TextView) view.findViewById(R.id.tv_order_playing);
            TextView doneOrder = (TextView) view.findViewById(R.id.tv_order_done);

            ImageView statusIcon = (ImageView)view.findViewById(R.id.iv_order_status);

            TextView gameName = (TextView)view.findViewById(R.id.tv_game_name);
            gameName.setText("陪练游戏："+mOrderEntity.getData().getGameName());

            switch (data.getStatusX()){
                case "0":
                case "1":
                    statusIcon.setImageResource(R.drawable.order_creating);
                    break;
                case "2":
                    createOrder.setText("已下单");
                    playingOrder.setText("等待陪练确认");
                    createOrder.setTextColor(getResources().getColor(R.color.textcolor_french_gray));
                    playingOrder.setTextColor(getResources().getColor(R.color.ab_backdround_red));
                    if (mIsShowCoach){
                        statusIcon.setImageResource(R.drawable.order_playing);
                    }else {
                        createOrder.setText("接单");
                        doneOrder.setText("完成接单");
                        playingOrder.setVisibility(View.GONE);
                        statusIcon.setImageResource(R.drawable.order_taking);
                    }

                    break;
                case "3":
                    createOrder.setText("已下单");
                    playingOrder.setText("陪练开始");
                    createOrder.setTextColor(getResources().getColor(R.color.textcolor_french_gray));
                    playingOrder.setTextColor(getResources().getColor(R.color.ab_backdround_red));
                    if (mIsShowCoach){
                        statusIcon.setImageResource(R.drawable.order_playing);
                    }else {
                        createOrder.setText("接单");
                        doneOrder.setText("完成接单");
                        playingOrder.setVisibility(View.GONE);
                        statusIcon.setImageResource(R.drawable.order_coach_done);
                    }

                    break;
                case "4":
                    createOrder.setText("已下单");
                    playingOrder.setText("陪练开始");
                    doneOrder.setText("待确认");
                    createOrder.setTextColor(getResources().getColor(R.color.textcolor_french_gray));
                    playingOrder.setTextColor(getResources().getColor(R.color.textcolor_french_gray));
                    doneOrder.setTextColor(getResources().getColor(R.color.ab_backdround_red));
                    statusIcon.setImageResource(R.drawable.order_done);

                    break;
                case "5":
                case "10":
                case "11":
                    createOrder.setText("已下单");
                    playingOrder.setText("陪练已结束");
                    doneOrder.setText("完成陪练");

                    createOrder.setTextColor(getResources().getColor(R.color.textcolor_french_gray));
                    playingOrder.setTextColor(getResources().getColor(R.color.textcolor_french_gray));
                    doneOrder.setTextColor(getResources().getColor(R.color.ab_backdround_red));
                    statusIcon.setImageResource(R.drawable.order_done);

                    break;
            }

            TextView nickName = (TextView)view.findViewById(R.id.tv_coach_detail_nick_name);
            nickName.setText(mOrderEntity.getCoach().getNickname());

            TextView score = (TextView)view.findViewById(R.id.tv_coach_detail_score);
            score.setText(mOrderEntity.getCoach().getScore()+"分");

            TextView status = (TextView)view.findViewById(R.id.tv_order_status);
            status.setText(data.getStatusText());

            RatingBar ratingBar = (RatingBar)view.findViewById(R.id.rb_coach_detail_score);

            try {
                ratingBar.setRating(Float.parseFloat(mOrderEntity.getCoach().getScore()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            TextView server = (TextView)view.findViewById(R.id.tv_game_server);
            server.setText(data.getGameArea());

            TextView mode = (TextView)view.findViewById(R.id.tv_game_mode);
            mode.setText(data.getGameMode());

            TextView count = (TextView)view.findViewById(R.id.tv_game_count);
            count.setText(data.getInning()+"局");

            TextView rank = (TextView)view.findViewById(R.id.tv_game_rank);
            try {
                rank.setText(Html.fromHtml("TA的段位："+TextUtil.toColor(data.getGrade()+"","#b8b8b8")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            TextView priceTotal = (TextView)view.findViewById(R.id.tv_game_price_total);
            try {
                priceTotal.setText(Html.fromHtml("总价："+TextUtil.toColor(data.getPrice_total()+"","#fc3c2e")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            TextView startTime = (TextView)view.findViewById(R.id.tv_game_start_time);
            try {
                startTime.setText(Html.fromHtml("开始时间："+TextUtil.toColor(TimeHelper.getWholeTimeFormat(data.getStart_time()),"#b8b8b8")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            TextView createTime = (TextView)view.findViewById(R.id.tv_game_end_time);
            try {
                createTime.setText(Html.fromHtml("下单时间："+TextUtil.toColor(TimeHelper.getWholeTimeFormat(data.getAdd_time()),"#b8b8b8")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            TextView orderNumber = (TextView)view.findViewById(R.id.tv_game_order_number);
            try {
                orderNumber.setText(Html.fromHtml("订单号："+TextUtil.toColor(data.getOrderNum(),"#b8b8b8")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            TextView placeNum = (TextView)view.findViewById(R.id.tv_place_order_num);
            if (!mIsShowCoach){
                ratingBar.setVisibility(View.GONE);
                score.setVisibility(View.GONE);
                placeNum.setText("下单数："+mOrderEntity.getUser().getOrderCount());
                placeNum.setVisibility(View.VISIBLE);
            }else {
                ratingBar.setVisibility(View.VISIBLE);
                score.setVisibility(View.VISIBLE);
                placeNum.setVisibility(View.GONE);
            }
        }
    }

    public void addViewGone(int id){

        mGoneView.add(id);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }
}
