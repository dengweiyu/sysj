package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.MemberMatchPKEntity204;
import com.li.videoapplication.data.model.response.MemberPKListEntity204;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.activity.MyMatchProcessActivity;
import com.li.videoapplication.ui.adapter.MyFinishedMatchProcessAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：我的赛程（已结束）
 */
public class MyFinishedMatchFragment extends TBaseFragment implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private MyMatchProcessActivity activity;
    private List<Match> data;
    private MyFinishedMatchProcessAdapter adapter;
    private CoordinatorLayout container;
    private View nodata;
    private boolean isOnGoing = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (MyMatchProcessActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_myfinishedmatch;
    }

    @Override
    protected void initContentView(View view) {
        nodata = view.findViewById(R.id.nodata_root);
        initRecyclerView(view);
        onRefresh();
    }

    //fragment处于当前交互状态
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//该fragment处于最前台交互状态
            //刷新数据
            onRefresh();
        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    private void initRecyclerView(View view) {
        container = (CoordinatorLayout) view.findViewById(R.id.myfinish_container);
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
        //设置线性布局
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setRefreshing(true);
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);//滑动监听（刷新/加载更多）

        data = new ArrayList<>();
        adapter = new MyFinishedMatchProcessAdapter(data);
        mPullLoadMoreRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        if (activity != null && activity.event_id != null) {
            DataManager.getMemberPKList204(getMember_id(), activity.event_id);
        }
    }

    @Override
    public void onLoadMore() {
        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
        Snackbar.make(container, "无更多赛程", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 回调：我的赛程（已结束）
     */
    public void onEventMainThread(MemberPKListEntity204 event) {

        if (event != null) {
            if (event.isResult()) {
                if (event.getData().size() > 0) {
                    if (activity != null && activity.mViewPager != null && !isOnGoing) {
                        //已结束有数据时，进行中无数据-->跳转至已结束页面
                        activity.mViewPager.setCurrentItem(1);
                    }
                    nodata.setVisibility(View.GONE);
                    mPullLoadMoreRecyclerView.setVisibility(View.VISIBLE);

                    data.clear();
                    data.addAll(event.getData());
                    adapter.notifyDataSetChanged();
                } else {
                    nodata.setVisibility(View.VISIBLE);
                }
            } else {
                nodata.setVisibility(View.VISIBLE);
                mPullLoadMoreRecyclerView.setVisibility(View.GONE);
            }
            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();//关闭加载进度圈
        }
    }

    /**
     * 回调：进行中赛事
     */
    public void onEventMainThread(MemberMatchPKEntity204 event) {

        if (event != null) {
            isOnGoing = event.isResult();
        }
    }
}
