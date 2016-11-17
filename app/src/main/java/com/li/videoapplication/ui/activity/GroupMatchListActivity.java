package com.li.videoapplication.ui.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.GroupEventsList211Entity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.GroupMatchListAdapter;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动：圈子-赛事列表
 */
public class GroupMatchListActivity extends TBaseAppCompatActivity implements View.OnClickListener,
        RequestLoadMoreListener, OnRefreshListener {

    private List<Match> datas;
    private GroupMatchListAdapter adapter;
    private int page = 1;
    private int page_count;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String game_id;

    /**
     * 跳转：游戏赛事详情
     */
    private void startActivityDetailGameMatch(String event_id) {
        ActivityManeger.startGameMatchDetailActivity(this, event_id);
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
        findViewById(R.id.goback).setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.groupmatchlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.groupmatchlist_swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        datas = new ArrayList<>();
        adapter = new GroupMatchListAdapter(datas);
        adapter.openLoadAnimation();
        adapter.setOnLoadMoreListener(this);
        View emptyView = getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);

        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int pos) {
                Match item = (Match) adapter.getItem(pos);
                startActivityDetailGameMatch(item.getEvent_id());
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();
        DataManager.getGroupEventsList211(game_id, page);
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
                    DataManager.getGroupEventsList211(game_id, page);
                } else {
                    // 数据全部加载完毕就调用 loadComplete
                    adapter.loadComplete();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goback:
                finish();
                break;
        }
    }

    /**
     * 回调：圈子赛事列表
     */
    public void onEventMainThread(GroupEventsList211Entity event) {

        if (event != null && event.isResult()) {
            page_count = event.getData().getPage_count();
            adapter.openLoadMore(page_count);

            if (event.getData().getList().size() > 0) {
                if (page == 1) {
                    datas.clear();
                    datas.addAll(event.getData().getList());
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
