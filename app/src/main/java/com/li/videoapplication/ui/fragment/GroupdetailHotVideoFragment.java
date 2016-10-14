package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.View;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.GroupHotDataListEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.ui.adapter.GroupDetailVideoRecyclerAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：最热视频
 */
public class GroupdetailHotVideoFragment extends TBaseFragment implements PullLoadMoreRecyclerView.PullLoadMoreListener,
        AppBarLayout.OnOffsetChangedListener{

    public PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private List<VideoImage> hotVideoData;
    private GroupDetailVideoRecyclerAdapter adapter;
    private GroupDetailActivity activity;
    private int page = 1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (GroupDetailActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈-精彩视频");
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.pager_groupdetail_newvideo;
    }

    @Override
    protected void initContentView(View view) {
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
        //设置线性布局
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);//滑动监听（刷新/加载更多）

        hotVideoData = new ArrayList<>();
        adapter = new GroupDetailVideoRecyclerAdapter(getActivity(), hotVideoData, this);
        mPullLoadMoreRecyclerView.setAdapter(adapter);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onRefresh() {
        page = 1;
        onLoadMore();
    }

    @Override
    public void onLoadMore() {
        // 圈子视频列表（最热）
        if (activity != null && activity.group_id != null)
            DataManager.groupHotDataList(activity.group_id, getMember_id(), page);
    }

    /**
     * 回调：圈子视频列表（最热）
     */
    public void onEventMainThread(GroupHotDataListEntity event) {

        if (event != null) {
            if (event.isResult()) {
                if (page == 1) {
                    hotVideoData.clear();
                }
                hotVideoData.addAll(event.getData().getList());
                Log.d(tag, "hotVideoData=" + hotVideoData);
                adapter.notifyDataSetChanged();
                if (event.getData().getList().size() > 0) {
                    ++page;
                }
            }
            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        mPullLoadMoreRecyclerView.setPullRefreshEnable(i==0);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        activity.appBarLayout.removeOnOffsetChangedListener(this);
    }


}
