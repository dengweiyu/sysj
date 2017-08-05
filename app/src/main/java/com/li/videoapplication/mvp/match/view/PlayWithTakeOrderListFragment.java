package com.li.videoapplication.mvp.match.view;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.NetworkError;
import com.li.videoapplication.data.model.event.RefreshOrderDetailEvent;
import com.li.videoapplication.data.model.response.CoachSignEntity;
import com.li.videoapplication.data.model.response.PlayWithTakeOrderEntity;
import com.li.videoapplication.data.network.RequestUrl;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.WebActivityJS;
import com.li.videoapplication.ui.adapter.PlayWithTakeOrderAdapter;
import com.li.videoapplication.ui.view.SimpleItemDecoration;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 陪玩接单列表
 */

public class PlayWithTakeOrderListFragment extends TBaseFragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView mList;

    private SwipeRefreshLayout mRefresh;

    private PlayWithTakeOrderAdapter mAdapter;

    private List<PlayWithTakeOrderEntity.DataBean> mData;

    private TextView mSign;
    private TextView mTip;
    private  View mEmptyView;

    private int mPage = 1;

    private static int mStatus = 3;     //1=>在线  2=>游戏中 3=>离线
    @Override
    protected int getCreateView() {
        return R.layout.fragment_play_with_take_order;
    }

    @Override
    protected void initContentView(View view) {
        mEmptyView = view.findViewById(R.id.rl_order_list_empty);
        view.findViewById(R.id.ll_order_list_empty).setOnClickListener(this);
        TextView tip = (TextView) view.findViewById(R.id.tv_order_tip);
        TextView tipRed = (TextView) view.findViewById(R.id.tv_order_tip_red);

        Member member  = getUser();
        if (member.isCoach()){
            tip.setText("还木有单子呢，要抓紧努力哟~");
            tipRed.setVisibility(View.GONE);
            view.findViewById(R.id.iv_arrow_red_one).setVisibility(View.GONE);
            view.findViewById(R.id.iv_arrow_red_two).setVisibility(View.GONE);
        }else {
            tip.setText("成为教练才能接单，赶紧");
            tipRed.setText("去申请");
            tipRed.setVisibility(View.VISIBLE);
            view.findViewById(R.id.iv_arrow_red_one).setVisibility(View.VISIBLE);
            view.findViewById(R.id.iv_arrow_red_two).setVisibility(View.VISIBLE);
        }


        mRefresh = (SwipeRefreshLayout)view.findViewById(R.id.srl_take_order);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mList = (RecyclerView)view.findViewById(R.id.rv_play_with_take_order);
        mData = new ArrayList<>();
        //接单列表都是自己的订单 因此角色的教练
        mAdapter = new PlayWithTakeOrderAdapter(getMember_id(),"",getMember_id(),mData);
        mAdapter.setEnableLoadMore(false);


        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(10),false,false,true,false));


        mList.setAdapter(mAdapter);

        mTip = (TextView)view.findViewById(R.id.tv_sign_tip);
        mSign = (TextView)view.findViewById(R.id.tv_user_sign);
        refreshStatus();
        mSign.setOnClickListener(this);
        if (getUser().isCoach()){
            view.findViewById(R.id.ll_take_order_top).setVisibility(View.VISIBLE);
            view.findViewById(R.id.v_divider_top).setVisibility(View.VISIBLE);
        }else {
            view.findViewById(R.id.ll_take_order_top).setVisibility(View.GONE);
            view.findViewById(R.id.v_divider_top).setVisibility(View.GONE);
        }

        mRefresh.setRefreshing(true);
        DataManager.getPlayWithTakeOrder(getMember_id(),mPage);
    }

    private void refreshStatus(){
        if (mStatus == 3){
            mSign.setText("签到");
            mSign.setBackground(new ColorDrawable(getResources().getColor(R.color.ab_backdround_red)));
            mSign.setTextColor(getContext().getResources().getColor(R.color.white));
            mTip.setText("亲爱的陪练宝宝，赶紧签到确认上线吧~");
        }else {
            mSign.setText("签退");
            mSign.setBackgroundResource(R.drawable.red_border);
            mSign.setTextColor(getContext().getResources().getColor(R.color.ab_backdround_red));
            mTip.setText("亲爱的陪练宝宝，有事离开记得签退哦~");
        }

    }


    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        DataManager.getPlayWithTakeOrder(getMember_id(),mPage);
    }

    @Override
    public void onLoadMoreRequested() {
        DataManager.getPlayWithTakeOrder(getMember_id(),mPage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_user_sign:
                if (mStatus == 3){
                    DataManager.coachSign(getMember_id(),1);
                }else {
                    DataManager.coachSign(getMember_id(),3);
                }
                break;
            case R.id.ll_order_list_empty:
                //打开web页
                WebActivityJS.startWebActivityJS(getContext(),"https://sapp.17sysj.com/Sysj222/Coach/apply","申请成为陪练","");
                break;
        }
    }

    /**
     *接单列表
     */
    public void onEventMainThread(PlayWithTakeOrderEntity entity){
        if (entity != null && entity.isResult()){
            //更新教练状态
            mStatus = entity.getCoachStatus();
            refreshStatus();
            if (mPage == 1){
                mData.clear();
            }

            if (entity.getPage_count() > mPage){
                mAdapter.setEnableLoadMore(true);
                mAdapter.setOnLoadMoreListener(this);
            }else {
                mAdapter.setEnableLoadMore(false);
                mAdapter.setOnLoadMoreListener(null);
            }
            mData = entity.getData();
            mAdapter.setNewData(mData);
            mPage++;
            if (mData.size() == 0){
                mEmptyView.setVisibility(View.VISIBLE);
            }else {
                mEmptyView.setVisibility(View.GONE);
            }
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mRefresh.setRefreshing(false);
    }

    /**
     *签到或者签退
     */
    public void onEventMainThread(CoachSignEntity entity){
        if (entity != null && entity.isResult()){
            if (mStatus == 3){
                mStatus = 1;
                ToastHelper.s("签到成功啦~");
            }else {
                mStatus = 3;
                ToastHelper.s("已签退~");
            }
        }
        refreshStatus();
    }

    /**
     *需要更新订单状态
     */
    public void onEventMainThread(RefreshOrderDetailEvent event){
        //更新订单状态
        if (mData != null){
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getOrder_id().equals(event.getOrderId())){
                    if (!mData.get(i).getStatusX().equals(event.getStatus())){
                        mData.get(i).setStatusX(event.getStatus());
                        mData.get(i).setStatusText(event.getStatusText());
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }

    /**
     * 网络错误
     */
    public void onEventMainThread(NetworkError error){
        if (error.getUrl().equals(RequestUrl.getInstance().getPlayWithPlaceOrder())){
            mRefresh.setRefreshing(false);
        }
    }
}
