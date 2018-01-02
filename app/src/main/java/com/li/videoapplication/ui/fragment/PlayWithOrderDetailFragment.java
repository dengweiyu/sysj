package com.li.videoapplication.ui.fragment;


import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.response.PlayWithOrderDetailEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.ui.activity.PlayWithOrderDetailActivity;
import com.li.videoapplication.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.filetransfer.Call;

/**
 * 陪练订单详情
 */

public class PlayWithOrderDetailFragment extends TBaseFragment implements View.OnClickListener {

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


            final ImageView icon = (ImageView)view.findViewById(R.id.civ_coach_detail_icon);

            if (mIsShowCoach && mOrderEntity.getCoach() != null){
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
            View choiceAgain = view.findViewById(R.id.tv_choice_again);
            ImageView statusIcon = (ImageView)view.findViewById(R.id.iv_order_status);
            choiceAgain.setVisibility(View.GONE);
            TextView gameName = (TextView)view.findViewById(R.id.tv_coach_game_name);
            gameName.setVisibility(View.VISIBLE);
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


                    if (PlayWithOrderDetailActivity.getRole(getMember_id(),mOrderEntity.getUser().getMember_id(),mOrderEntity.getCoach().getMember_id()) == PlayWithOrderDetailActivity.ROLE_OWNER){
                        choiceAgain.setVisibility(View.VISIBLE);
                        choiceAgain.setOnClickListener(this);
                    }

                    break;
            }

            TextView nickName = (TextView)view.findViewById(R.id.tv_coach_detail_nick_name);
            TextView score = (TextView)view.findViewById(R.id.tv_coach_detail_score);
            TextView status = (TextView)view.findViewById(R.id.tv_order_status);
            status.setText(data.getStatusText());
            RatingBar ratingBar = (RatingBar)view.findViewById(R.id.rb_coach_detail_score);


            if (mOrderEntity.getCoach() != null) {
                score.setText(mOrderEntity.getCoach().getScore()+"分");
                nickName.setText(mOrderEntity.getCoach().getNickname());
                try {
                    ratingBar.setRating(Float.parseFloat(mOrderEntity.getCoach().getScore()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                priceTotal.setText(Html.fromHtml("总价："+TextUtil.toColor(data.getPrice_total()+"魔币","#fc3c2e")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            TextView startTime = (TextView)view.findViewById(R.id.tv_game_start_time);
            try {
                //抢单模式下 不显示开始时间
                if ("2".equals(data.getOrder_mode())){
                    startTime.setVisibility(View.GONE);
                }else {
                    startTime.setVisibility(View.VISIBLE);
                    startTime.setText(Html.fromHtml("开始时间："+TextUtil.toColor(TimeHelper.getWholeTimeFormat(data.getStart_time()),"#b8b8b8")));
                }

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
                nickName.setText(mOrderEntity.getUser().getNickname());
            }else {
                ratingBar.setVisibility(View.VISIBLE);
                score.setVisibility(View.VISIBLE);
                placeNum.setVisibility(View.GONE);

                if (mOrderEntity.getCoach() != null) {
                    nickName.setText(mOrderEntity.getCoach().getNickname());
                }

            }
        }

        refreshViewByType();
    }

    /**
     *
     */
    private void refreshViewByType(){
        if (mOrderEntity == null || mRootView == null){
            return;
        }
        TextView duration = (TextView) mRootView.findViewById(R.id.tv_game_duration);

        switch (mOrderEntity.getData().getTraining_type_id()){
            case "1":               //王者荣耀
                mRootView.findViewById(R.id.ll_game_base_info).setVisibility(View.VISIBLE);
                mRootView.findViewById(R.id.tv_game_rank).setVisibility(View.GONE);
                duration.setVisibility(View.GONE);
                break;
            case "2":               //吃鸡类
                //
                mRootView.findViewById(R.id.ll_game_base_info).setVisibility(View.GONE);
                mRootView.findViewById(R.id.tv_game_rank).setVisibility(View.GONE);

                duration.setVisibility(View.VISIBLE);
                duration.setText("陪练时长："+mOrderEntity.getData().getInning()+"小时");
                break;
        }
    }

    public void addViewGone(int id){
        mGoneView.add(id);
    }


    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_choice_again:
                PlayWithOrderDetailActivity detailActivity;
                if (getActivity() instanceof PlayWithOrderDetailActivity){
                    detailActivity = (PlayWithOrderDetailActivity)getActivity();
                    detailActivity.choiceAgain(mRootView.findViewById(R.id.rv_coach_info_header));
                }
                break;
        }
    }
}
