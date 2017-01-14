package com.li.videoapplication.mvp.match.view;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.MatchRecordEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.adapter.MatchRecordAdapter;
import com.li.videoapplication.mvp.match.MatchContract.IMatchPresenter;
import com.li.videoapplication.mvp.match.MatchContract.IMatchRecordView;
import com.li.videoapplication.mvp.match.presenter.MatchPresenter;
import com.li.videoapplication.views.LoadMoreViewAccent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 活动：历史战绩
 */
public class MatchRecordActivity extends TBaseAppCompatActivity implements IMatchRecordView,
        RequestLoadMoreListener, OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private int page = 1;
    private int page_count;
    private IMatchPresenter presenter;
    private MatchRecordAdapter adapter;


    @Override
    protected int getContentView() {
        return R.layout.activity_matchrecord;
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
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initAdapter() {
        presenter = MatchPresenter.getInstance();
        presenter.setMatchRecordView(this);

        List<Match> datas = new ArrayList<>();
        adapter = new MatchRecordAdapter(datas);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setOnLoadMoreListener(this);
        adapter.setLoadMoreView(new LoadMoreViewAccent());
        View emptyView = getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyText.setText("快去参加赛事赢取丰厚奖励吧~");
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        super.loadData();
        presenter.getHistoricalRecord(getMember_id(), page);
    }

    @OnClick(R.id.tb_back)
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
        }
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
    public void hideProgress() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        adapter.loadMoreComplete();//加载完成
    }

    /**
     * 回调：历史战绩
     */
    @Override
    public void refreshMatchRecordData(MatchRecordEntity data) {
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
