package com.li.videoapplication.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.GetMatchList201Entity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.mvp.adapter.ActivityListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 活动：活动列表
 */
public class ActivityListActivity extends TBaseActivity implements OnRefreshListener,
        RequestLoadMoreListener ,View.OnClickListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ActivityListAdapter adapter;
    private int page = 1;
    private int page_count;

    /**
     * 跳转：活动详情
     */
    private void startActivityDetailActivity(Match record) {
        ActivityManeger.startActivityDetailActivityNewTask(this, record.getMatch_id());
        if (record.getStatus().equals("进行中")) {
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "热门活动-进行中");
        } else if (record.getStatus().equals("已结束")) {
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "热门活动-已结束");
        }
    }

    /**
     * 跳转：我的活动
     */
    private void startMyActivityListActivity() {
        ActivityManeger.startMyActivityListActivity(this);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_activity;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle("活动");
        abMyActivity.setVisibility(View.VISIBLE);
        abMyActivity.setOnClickListener(this);
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
        List<Match> data = new ArrayList<>();
        adapter = new ActivityListAdapter(data);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setOnLoadMoreListener(this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        super.loadData();
        DataManager.getMatchList201(page);
    }

    private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int pos) {
                Match record = (Match) adapter.getItem(pos);
                startActivityDetailActivity(record);
            }
        });

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int pos) {
                Match record = (Match) adapter.getItem(pos);
                switch (view.getId()) {
                    case R.id.activity_reward:
                        WebActivity.startWebActivity(ActivityListActivity.this, record.getReward_url());
                        break;
                }
            }
        });
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
                    // 数据全部加载完毕
                    adapter.loadMoreEnd();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "热门活动");
    }

    /**
     * 回调：赛事列表
     */
    public void onEventMainThread(GetMatchList201Entity event) {
        if (event != null && event.isResult()) {
            page_count = event.getData().getPage_count();
            if (event.getData().getList().size() > 0) {
                if (page == 1) {
                    adapter.setNewData(event.getData().getList());
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    // 如果有下一页则调用addData，不需要把下一页数据add到list里面，直接新的数据给adapter即可。
                    adapter.addData(event.getData().getList());
                }
                ++page;
            }
        }
        adapter.loadMoreComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ab_myactivity:
                startMyActivityListActivity();
                break;
        }
    }
}
