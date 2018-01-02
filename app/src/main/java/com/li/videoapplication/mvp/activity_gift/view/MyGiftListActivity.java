package com.li.videoapplication.mvp.activity_gift.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.data.model.response.MyPackageEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IGiftPresenter;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IMyGiftView;
import com.li.videoapplication.mvp.activity_gift.presenter.GiftPresenter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.mvp.adapter.MyGiftAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 活动：我的礼包列表
 */
public class MyGiftListActivity extends TBaseAppCompatActivity implements IMyGiftView,
        RequestLoadMoreListener, OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private IGiftPresenter presenter;
    private MyGiftAdapter adapter;
    private int page = 1;
    private int page_count;

    /**
     * 跳转：礼包详情
     */
    private void startGiftDetailActivity(Gift item) {
        ActivityManager.startGiftDetailActivity(this, item.getId());
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "我的礼包");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_myactivitylist;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
        initRecyclerView();
        initAdapter();
        addOnClickListener();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText("我的礼包");
    }

    @OnClick(R.id.tb_back)
    public void goback() {
        finish();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initAdapter() {
        presenter = GiftPresenter.getInstance();
        presenter.setMyGiftView(this);

        List<Gift> data = new ArrayList<>();
        adapter = new MyGiftAdapter(data);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setOnLoadMoreListener(this);

        View emptyView = getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyText.setText("您还没有领取过礼包喔~~");
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        super.loadData();
        presenter.getMyGiftList(getMember_id(), page);
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

    private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Gift record = (Gift) adapter.getItem(position);
                startGiftDetailActivity(record);
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
     * 回调：我的礼包列表
     */
    @Override
    public void refreshMyGiftListData(MyPackageEntity data) {
        page_count = data.getPage_count();
        if (data.getList().size() > 0) {
            if (page == 1) {
                adapter.setNewData(data.getList());
            } else {
                adapter.addData(data.getList());
            }
            ++page;
        }
    }
}
