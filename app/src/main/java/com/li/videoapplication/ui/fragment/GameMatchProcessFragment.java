package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.EventsPKList204Entity;
import com.li.videoapplication.data.model.response.EventsSchedule204Entity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.GameMatchDetailActivity;
import com.li.videoapplication.ui.adapter.MatchProcessAdapter;
import com.li.videoapplication.ui.adapter.MatchProcessHeaderAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 碎片：赛程
 */
public class GameMatchProcessFragment extends TBaseFragment implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private GameMatchDetailActivity activity;
    public PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private int page = 1;
    private List<Match> data;
    private List<Match> headerData;
    private MatchProcessHeaderAdapter headeradapter;
    private MatchProcessAdapter adapter;
    public int currentHeaderPos = -1;
    private View nodata;
    private TextView nodataText;
    private RecyclerView headerRecyclerView;
    private boolean isRefreshing;
    public int is_last;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (GameMatchDetailActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //fragment处于当前交互状态
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//该fragment处于最前台交互状态
            //刷新赛事详情数据
            onRefresh();
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "赛程");
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_gamematch_process;
    }

    @Override
    protected void initContentView(View view) {
        initHeaderListView(view);
        initRecyclerView(view);
        onRefresh();
    }

    private void initHeaderListView(View view) {
        headerRecyclerView = (RecyclerView) view.findViewById(R.id.process_horizontallist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        headerRecyclerView.setLayoutManager(linearLayoutManager);
        OverScrollDecoratorHelper.setUpOverScroll(headerRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);

        headerData = new ArrayList<>();
        headeradapter = new MatchProcessHeaderAdapter(getActivity(), headerData);
        headeradapter.receiveFragment(this);
        headerRecyclerView.setAdapter(headeradapter);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    private void initRecyclerView(View view) {
        nodata = view.findViewById(R.id.nodata_root);
        nodataText = (TextView) view.findViewById(R.id.nodata_text);
        nodataText.setText("暂无对战列表");
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pulltorefresh_gamematchprocess);
        //设置线性布局
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setRefreshing(true);
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);

        data = new ArrayList<>();
        adapter = new MatchProcessAdapter(this, data);
        mPullLoadMoreRecyclerView.setAdapter(adapter);
    }

    public void setHeaderClick() {
        if (currentHeaderPos != -1) {
            if (headerData.get(currentHeaderPos).getPk_status().equals("1")) {//1-->已发布
                page = 1;
                onLoadMore();
                mPullLoadMoreRecyclerView.setRefreshing(true);
            } else {
                data.clear();
                adapter.notifyDataSetChanged();
                headeradapter.notifyDataSetChanged();

                nodata.setVisibility(View.VISIBLE);
                nodataText.setText("该赛程未发布");
            }
        }
    }

//    public boolean isLastMatch() {
//        if (currentHeaderPos != -1) {
//            return headerData.get(currentHeaderPos).getIs_last().equals("1");
//        } else {
//            return headerData.get(headerData.size() - 1).getIs_last().equals("1");
//        }
//    }

    @Override
    public void onRefresh() {
        page = 1;
        if (activity != null && activity.event_id != null) {
            DataManager.eventsSchedule204(activity.event_id);
        }
    }

    @Override
    public void onLoadMore() {
        if (!isRefreshing) {
            if (currentHeaderPos != -1) {//点击后处于某页的加载更多
                DataManager.eventsPKList210(headerData.get(currentHeaderPos).getSchedule_id(), page, getMember_id());
            } else {//未点击，默认显示最后一页的加载更多
                DataManager.eventsPKList210(headerData.get(headerData.size() - 1).getSchedule_id(), page, getMember_id());
            }
            isRefreshing = true;
        }
    }

    /**
     * 回调：赛程时间表
     */
    public void onEventMainThread(EventsSchedule204Entity event) {

        if (event != null) {
            if (event.isResult()) {
                if (event.getData().size() > 0) {
                    headerRecyclerView.setVisibility(View.VISIBLE);
                    nodata.setVisibility(View.GONE);
                    headerData.clear();
                    headerData.addAll(event.getData());
                    currentHeaderPos = -1;
                    if (event.getData().get(headerData.size() - 1).getPk_status().equals("1")) {//1-->已发布
                        nodata.setVisibility(View.GONE);
                        onLoadMore();
                    } else {//0-->未发布
                        nodata.setVisibility(View.VISIBLE);
                        nodataText.setText("该赛程未发布");
                        mPullLoadMoreRecyclerView.setRefreshing(false);
                    }
                    headeradapter.notifyDataSetChanged();
                    headerRecyclerView.smoothScrollToPosition(headerData.size() - 1);
                } else {
                    headerRecyclerView.setVisibility(View.INVISIBLE);
                    nodata.setVisibility(View.VISIBLE);
                    nodataText.setText("暂无对战列表");
                }
            }
            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
        }
    }

    /**
     * 回调：赛程PK列表
     */
    public void onEventMainThread(EventsPKList204Entity event) {

        if (event != null) {
            if (event.isResult()) {
                is_last = event.getData().getIs_last();

                if (event.getData().getList().size() > 0) {
                    mPullLoadMoreRecyclerView.setVisibility(View.VISIBLE);
                    nodata.setVisibility(View.GONE);
                    if (page == 1) {
                        data.clear();
                    }
                    data.addAll(event.getData().getList());
                    adapter.notifyDataSetChanged();
                    headeradapter.notifyDataSetChanged();
                    ++page;
                } else {
                    if (page == 1) {
                        data.clear();
                        adapter.notifyDataSetChanged();
                        headeradapter.notifyDataSetChanged();
                        nodata.setVisibility(View.VISIBLE);
                        mPullLoadMoreRecyclerView.setVisibility(View.INVISIBLE);
                        nodataText.setText("暂无可用的对战列表");
                    }
                }
            }
            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
        }
        isRefreshing = false;
    }
}
