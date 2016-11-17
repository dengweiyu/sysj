package com.li.videoapplication.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.EventsList211Entity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.GameMatchAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：热门游戏赛事
 */
public class GameMatchFragment extends TBaseFragment implements OnRefreshListener, RequestLoadMoreListener {

    private GameMatchAdapter adapter;
    private List<Match> data;

    private int page = 1;
    private int page_count;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    /**
     * 跳转：游戏赛事详情
     */
    private void startActivityDetailGameMatch(String event_id) {
        ActivityManeger.startGameMatchDetailActivity(getContext(), event_id);
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MATCH, "进入赛事");
    }

    /**
     * 跳转：活动详情
     */
    private void startActivityDetailActivity208(String match_id) {
        ActivityManeger.startActivityDetailActivity208(getContext(), match_id);
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MATCH, "赛事列表-进入活动");
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_gamematch;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    protected void initContentView(View view) {
        initListView(view);
        onRefresh();
    }

    private void initListView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.gamematch_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.gamematch_swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        data = new ArrayList<>();
        adapter = new GameMatchAdapter(data);
        adapter.openLoadAnimation();
        adapter.setOnLoadMoreListener(this);

        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyText.setText("敬请期待更多赛事");
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);

        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int pos) {
                Match record = (Match) adapter.getItem(pos);
                if (record.getType_id().equals("3")) { //活动
                    startActivityDetailActivity208(record.getMatch_id());
                } else { //赛事
                    startActivityDetailGameMatch(record.getEvent_id());
                }
            }
        });
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        page = 1;
        // 赛事列表
        DataManager.getEventsList211(page);
    }

    //加载更多
    @Override
    public void onLoadMoreRequested() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (page <= page_count) {
                    // 赛事列表
                    DataManager.getEventsList211(page);
                } else {
                    // 数据全部加载完毕就调用 loadComplete
                    adapter.loadComplete();
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "进入赛事列表页面次数");
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "进入赛事列表页面次数");
        }
    }

    /**
     * 回调：赛事列表
     */
    public void onEventMainThread(EventsList211Entity event) {

        if (event != null && event.isResult()) {
            page_count = event.getData().getPage_count();
            adapter.openLoadMore(page_count);

            if (event.getData().getList().size() > 0) {
                if (page == 1) {
                    data.clear();
                    data.addAll(event.getData().getList());
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    // 如果有下一页则调用addData，不需要把下一页数据add到list里面，直接新的数据给adapter即可。
                    adapter.addData(event.getData().getList());
                }
                ++page;
            }
        }
    }
}
