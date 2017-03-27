package com.li.videoapplication.mvp.match.view;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.EventsList214Entity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.adapter.MatchListAdapter;
import com.li.videoapplication.mvp.match.MatchContract.IGroupMatchListView;
import com.li.videoapplication.mvp.match.presenter.MatchPresenter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 活动：圈子-赛事列表
 */
public class GroupMatchListActivity extends TBaseAppCompatActivity implements IGroupMatchListView,
        RequestLoadMoreListener, OnRefreshListener {

    @BindView(R.id.groupmatchlist_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.groupmatchlist_swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private MatchListAdapter adapter;
    private int page = 1;
    private int page_count;
    private String game_id;
    private MatchPresenter presenter;

    /**
     * 跳转：游戏赛事详情
     */
    private void startActivityDetailGameMatch(String event_id) {
        ActivityManeger.startGameMatchDetailActivity(this, event_id);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈-赛事-赛事详情");
    }

    /**
     * 跳转：活动详情
     */
    private void startActivityDetailActivity208(String match_id) {
        ActivityManeger.startActivityDetailActivity(this, match_id);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "游戏圈赛事列表-进入活动");
    }

    /**
     * 跳转：赛事结果
     */
    private void startMatchResultActivity(String event_id) {
        ActivityManeger.startMatchResultActivity(this, event_id);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_groupmatchlist;
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            game_id = getIntent().getStringExtra("game_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isNull(game_id)) finish();
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundColor(Color.WHITE);
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
        presenter.setGroupMatchListView(this);

        List<Match> datas = new ArrayList<>();
        adapter = new MatchListAdapter(datas);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setOnLoadMoreListener(this);

        View emptyView = getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        super.loadData();
        presenter.getGroupEventsList(page, game_id);
    }

    private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int pos) {
                Match item = (Match) adapter.getItem(pos);

                if (item.getType_id().equals("3")) { //活动
                    startActivityDetailActivity208(item.getMatch_id());
                } else { //赛事
                    startActivityDetailGameMatch(item.getEvent_id());
                }
            }
        });

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int pos) {
                Match record = (Match) adapter.getItem(pos);
                switch (view.getId()) {
                    case R.id.match_result:
                        startMatchResultActivity(record.getEvent_id());
                        break;
                }
            }
        });
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        page = 1;
        loadData();
    }

    //加载更多
    @Override
    public void onLoadMoreRequested() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (page <= page_count) {
                    loadData();
                } else {
                    // 数据全部加载完毕
                    adapter.loadMoreEnd();
                }
            }
        });
    }

    @OnClick(R.id.goback)
    public void goback() {
        finish();
    }


    @Override
    public void hideProgress() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        adapter.loadMoreComplete();//加载完成
    }

    /**
     * 回调：圈子赛事列表
     */
    @Override
    public void refreshGroupMatchListData(EventsList214Entity data) {
        page_count = data.getPage_count();
        if (data.getList().size() > 0) {
            if (page == 1) {
                adapter.setNewData(data.getList());
            } else {
                // 如果有下一页则调用addData，不需要把下一页数据add到list里面，直接新的数据给adapter即可。
                adapter.addData(data.getList());
            }
            ++page;
        }
    }
}
