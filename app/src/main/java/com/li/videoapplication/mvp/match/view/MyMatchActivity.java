package com.li.videoapplication.mvp.match.view;

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
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.EventsList214Entity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.adapter.MatchListAdapter;
import com.li.videoapplication.mvp.match.MatchContract.IMatchPresenter;
import com.li.videoapplication.mvp.match.MatchContract.IMyMatchListView;
import com.li.videoapplication.mvp.match.presenter.MatchPresenter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 活动：我的赛事
 */
public class MyMatchActivity extends TBaseAppCompatActivity implements IMyMatchListView,
        RequestLoadMoreListener, OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private MatchListAdapter adapter;
    private int page = 1;
    private int page_count;
    private IMatchPresenter presenter;

    /**
     * 跳转：游戏赛事详情
     */
    private void startActivityDetailGameMatch(String event_id) {
        ActivityManager.startGameMatchDetailActivity(this, event_id);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "侧栏我的赛事-我的赛事页面，点击任何一个赛事 ");
    }

    /**
     * 跳转：活动详情
     */
    private void startActivityDetailActivity208(String match_id) {
        ActivityManager.startActivityDetailActivity(this, match_id);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "侧栏我的赛事-进入活动");
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "侧栏我的赛事-我的赛事页面，点击任何一个赛事 ");
    }

    @Override
    public int getContentView() {
        return R.layout.activity_mymatch;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();
        initRecyclerView();
        initAdapter();
        addOnClickListener();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        View emptyView = getLayoutInflater().inflate(R.layout.emptyview,
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
    public void loadData() {
        super.loadData();
        presenter.getMyEventsList(page,getMember_id());
    }

    @OnClick(R.id.goback)
    public void goback() {
        finish();
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadData();
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (page <= page_count) {
                    loadData();
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

    /**
     * 回调：我的赛事列表
     */
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
}
