package com.li.videoapplication.mvp.match.view;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.MemberMatchPKEntity204;
import com.li.videoapplication.data.model.response.MemberPKListEntity204;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.MyMatchProcessActivity;
import com.li.videoapplication.mvp.adapter.MyFinishedMatchProcessAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 碎片：我的赛程（已结束）
 */
public class MyFinishedMatchFragment extends TBaseFragment implements OnRefreshListener {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MyMatchProcessActivity activity;
    private MyFinishedMatchProcessAdapter adapter;
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//该fragment处于最前台交互状态
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "我的赛程-已结束");
        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    protected int getCreateView() {
        return R.layout.refresh_recyclerview;
    }

    @Override
    protected void initContentView(View view) {
        initRecyclerView();
        initAdapter();
        onRefresh();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initAdapter() {
        List<Match> data = new ArrayList<>();
        adapter = new MyFinishedMatchProcessAdapter(data);
        adapter.openLoadAnimation();
        if (activity != null && activity.event_id != null) {
            adapter.setEventID(activity.event_id);
        }
        recyclerView.setAdapter(adapter);
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        TextView emptyViewText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyViewText.setText("暂无已结束赛程");
        adapter.setEmptyView(emptyView);
    }

    @Override
    public void onRefresh() {
        if (activity != null && activity.event_id != null) {
            DataManager.getMemberPKList204(getMember_id(), activity.event_id);
        }
    }

    /**
     * 回调：我的赛程（已结束）
     */
    public void onEventMainThread(MemberPKListEntity204 event) {

        if (event != null) {
            if (event.isResult()) {
                if (activity != null && activity.mViewPager != null && !isOnGoing) {
                    //已结束有数据时，进行中无数据-->跳转至已结束页面
                    activity.mViewPager.setCurrentItem(1);
                }
                adapter.setNewData(event.getData());
            }
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
            adapter.loadMoreEnd();
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
