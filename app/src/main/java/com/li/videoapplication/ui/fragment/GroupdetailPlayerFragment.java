package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.View;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.GroupGamerListEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.GroupDetailActivity;
import com.li.videoapplication.ui.adapter.GroupDetailPlayerAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：玩家
 */
public class GroupdetailPlayerFragment extends TBaseFragment
        implements PullLoadMoreRecyclerView.PullLoadMoreListener, AppBarLayout.OnOffsetChangedListener {

    public PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private List<Member> playerData;
    private GroupDetailPlayerAdapter adapter;
    private int page = 1;
    private GroupDetailActivity activity;

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
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈-玩家");
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.pager_groupdetail_newvideo;
    }

    @Override
    protected void initContentView(View view) {
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);

        //设置线性布局
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);

        playerData = new ArrayList<>();
        adapter = new GroupDetailPlayerAdapter(getActivity(), playerData);
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
        // 圈子玩家列表
        if (activity != null && activity.group_id != null)
            DataManager.groupGamerList(activity.group_id, getMember_id(), page);
    }

    /**
     * 回调：圈子玩家列表
     */
    public void onEventMainThread(GroupGamerListEntity event) {

        if (event != null) {
            if (event.isResult()) {
                if (page == 1) {
                    playerData.clear();
                }
                playerData.addAll(event.getData().getList());
                Log.d(tag, "dData=" + playerData);
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
        mPullLoadMoreRecyclerView.setPullRefreshEnable(i == 0);
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
