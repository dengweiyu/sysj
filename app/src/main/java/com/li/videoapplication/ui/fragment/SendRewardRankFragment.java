package com.li.videoapplication.ui.fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.animation.RecyclerViewAnim;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.SendRewardRankEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.adapter.SendRewardRankAdapter;
import com.li.videoapplication.ui.view.SimpleItemDecoration;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *  土豪榜/人气榜
 */
public class SendRewardRankFragment extends TBaseFragment implements SwipeRefreshLayout.OnRefreshListener ,BaseQuickAdapter.RequestLoadMoreListener {
    //
    public final static int TYPE_SEND = 1;
    public final static int TYPE_RECEIVED = 2;
    public final static int TYPE_VIDEO = 3;
    private int mPage = 1;
    private int mType;
    private TextView mMyRankMsg;
    private RecyclerView mRankList;
    private SendRewardRankAdapter mAdapter;
    private SwipeRefreshLayout mRefresh;
    private List<SendRewardRankEntity.ADataBean.IncludeBean> mData;
    public static SendRewardRankFragment newInstance(int type){
        SendRewardRankFragment fragment = new SendRewardRankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_send_reward;
    }

    @Override
    protected void initContentView(View view) {
        Bundle bundle = getArguments();
        //
        if (bundle != null){
            mType = bundle.getInt("type",TYPE_SEND);
            loadData();
        }

        mRefresh = (SwipeRefreshLayout)view.findViewById(R.id.srl_send_reward);
        mRefresh.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mRefresh.setOnRefreshListener(this);

        mRankList = (RecyclerView)view.findViewById(R.id.rv_send_reward_list);
        mRankList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRankList.addItemDecoration(new SimpleItemDecoration(getActivity(),false,false,false,true));
        mData = new ArrayList<>();
        mAdapter = new SendRewardRankAdapter(mData,mType);
        mAdapter.openLoadAnimation(new RecyclerViewAnim());
        mRefresh.setRefreshing(true);
        mRankList.setAdapter(mAdapter);


    }

    @Override
    public void onLoadMoreRequested() {
        mPage++;
        loadData();
    }

    private View getHeaderView(){
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.header_playerbillboard, null);
        mMyRankMsg =  (TextView)root.findViewById(R.id.playerbillboard_rank);
        return root;
    }

    private void loadData(){
        DataManager.getSendRewardRank(getMember_id(),mPage,getTypeByIndex(mType));
    }

    private synchronized void enableLoadMore(boolean isEnable){
        if (isEnable){
            mAdapter.setOnLoadMoreListener(this);
            mAdapter.setEnableLoadMore(true);
        }else {

            mAdapter.setEnableLoadMore(false);
            //移除listener一定要在 EnableLoadMore后面。。具体可看源码
            mAdapter.setOnLoadMoreListener(null);
        }
    }

    public static String getTypeByIndex(int index){
        String type="sendGiftRanking";

        switch (index){
            case TYPE_SEND:
                type="sendGiftRanking";
                break;
            case TYPE_RECEIVED:
                type="receivedGiftRanking";
                break;
            case TYPE_VIDEO:
                type="videoGiftRanking";
                break;
        }
        return type;
    }


    @Override
    public void onRefresh() {
        mPage = 1;
        loadData();
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    public void onEventMainThread(SendRewardRankEntity entity){
        mRefresh.setRefreshing(false);
        mAdapter.loadMoreComplete();
        if (entity.isResult()){
            //
            if (!getTypeByIndex(mType).equals(entity.getType())){
                return;
            }

            //
            if (mPage == 1){
                mData.clear();
            }
            mData.addAll(entity.getAData().getInclude());

            enableLoadMore(entity.getAData().getPage_count() > mPage);

            if (entity.getAData().getPosition() == 0){
                if (mMyRankMsg != null){
                    mMyRankMsg.setText("");
                }
            }else {
                if (mMyRankMsg == null){
                    mAdapter.addHeaderView(getHeaderView());
                }

                if (mType == TYPE_SEND){
                    mMyRankMsg.setText(Html.fromHtml("您当前在打赏土豪榜排名为："+ TextUtil.toColor(entity.getAData().getPosition()+"","#fc3c2e")));
                    if (entity.getAData().getPosition() > 1000){
                        mMyRankMsg.setText(Html.fromHtml("您当前的排名是：第"+ TextUtil.toColor("1000","#fc3c2e")+"名之外，赶紧给关注的主播打赏礼物吧~"));
                    }

                }else {
                    mMyRankMsg.setText(Html.fromHtml("您当前在打赏人气榜排名为："+ TextUtil.toColor(entity.getAData().getPosition()+"","#fc3c2e")));
                    if(entity.getAData().getPosition() > 1000){
                        mMyRankMsg.setText(Html.fromHtml("您当前的排名是：第"+ TextUtil.toColor("1000","#fc3c2e")+"名之外，赶紧发布精彩视频提高排名吧~"));
                    }
                }
            }

            mAdapter.notifyDataSetChanged();

        }else {

        }
    }
}
