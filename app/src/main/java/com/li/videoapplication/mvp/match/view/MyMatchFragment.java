package com.li.videoapplication.mvp.match.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.EventsList214Entity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.MatchListAdapter;
import com.li.videoapplication.mvp.match.MatchContract;
import com.li.videoapplication.mvp.match.presenter.MatchPresenter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的赛事
 */

public class MyMatchFragment extends TBaseFragment implements MatchContract.IMyMatchListView,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private MatchListAdapter adapter;
    private int page = 1;
    private int page_count;
    private MatchContract.IMatchPresenter presenter;
    /**
     * 跳转：游戏赛事详情
     */
    private void startActivityDetailGameMatch(String event_id) {
        ActivityManager.startGameMatchDetailActivity(getContext(), event_id);
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.SLIDER, "侧栏我的赛事-我的赛事页面，点击任何一个赛事 ");
    }

    /**
     * 跳转：活动详情
     */
    private void startActivityDetailActivity208(String match_id) {
        ActivityManager.startActivityDetailActivity(getContext(), match_id);
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MATCH, "侧栏我的赛事-进入活动");
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.SLIDER, "侧栏我的赛事-我的赛事页面，点击任何一个赛事 ");
    }

    @Override
    protected int getCreateView() {
        return R.layout.activity_mymatch;
    }

    @Override
    protected void initContentView(View view) {
        initRecyclerView();
        initAdapter();
        addOnClickListener();

        presenter.getMyEventsList(page,getMember_id());
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initAdapter() {
        presenter = MatchPresenter.getInstance();
        presenter.setMyMatchListView(this);

        List<Match> datas = new ArrayList<>();
        adapter = new MatchListAdapter(datas);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setOnLoadMoreListener(this);

        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyText.setText("您还没参加过赛事喔~");
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);
    }

    private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Match item = (Match) adapter.getItem(position);

                if (item.getType_id().equals("3")) { //活动
                    startActivityDetailActivity208(item.getMatch_id());
                } else { //赛事
                    startActivityDetailGameMatch(item.getEvent_id());
                }
            }

        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        presenter.getMyEventsList(page,getMember_id());
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (page <= page_count) {
                    presenter.getMyEventsList(page,getMember_id());
                } else {
                    // 没有更多数据
                    adapter.loadMoreEnd();
                }
            }
        });
    }

    @Override
    public void hideProgress() {
        if (swipeRefreshLayout.isRefreshing())
        swipeRefreshLayout.setRefreshing(false);

        adapter.loadMoreComplete();//加载完成

    }

    @Override
    public void refreshMyMatchListData(EventsList214Entity data) {
        page_count = data.getPage_count();
        if (page == 1) {
            adapter.setNewData(data.getList());
        } else {
            // 如果有下一页则调用addData，不需要把下一页数据add到list里面，直接新的数据给adapter即可。
            adapter.addData(data.getList());
        }
        ++page;
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }
}
