package com.li.videoapplication.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.animation.RecyclerViewAnim;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.VideoRewardRankEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.adapter.VideoRewardAdapter;
import com.li.videoapplication.ui.view.SimpleItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频榜
 */

public class VideoRewardFragment extends TBaseFragment implements SwipeRefreshLayout.OnRefreshListener ,BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView mVideoList;
    private VideoRewardAdapter mAdapter;
    private List<VideoRewardRankEntity.ADataBean.IncludeBean> mData;
    private int mPage = 1;
    private SwipeRefreshLayout mRefresh;
    @Override
    protected int getCreateView() {
        return R.layout.fragment_video_reward;
    }

    @Override
    protected void initContentView(View view) {

        mRefresh = (SwipeRefreshLayout)view.findViewById(R.id.srl_video_reward);
        mRefresh.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mRefresh.setOnRefreshListener(this);

        mVideoList = (RecyclerView)view.findViewById(R.id.rv_video_reward);
        mVideoList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mData = new ArrayList<>();
        mAdapter = new VideoRewardAdapter(mData);
        mAdapter.openLoadAnimation(new RecyclerViewAnim());
        mVideoList.setAdapter(mAdapter);
        mRefresh.setRefreshing(true);
        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        mPage++;
        loadData();
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

    @Override
    public void onRefresh() {
        mPage = 1;
        loadData();
    }

    private void loadData(){
        DataManager.getSendRewardRank(getMember_id(),mPage,SendRewardRankFragment.getTypeByIndex(SendRewardRankFragment.TYPE_VIDEO));
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }


    public void onEventMainThread(VideoRewardRankEntity entity){
        if (entity.isResult()){
            if (mPage == 1){
                mData.clear();
            }

            enableLoadMore(entity.getAData().getPage_count() > mPage);

            mData.addAll(entity.getAData().getInclude());
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();

        }

        mRefresh.setRefreshing(false);
    }
}
