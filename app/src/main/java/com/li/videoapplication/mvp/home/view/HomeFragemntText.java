package com.li.videoapplication.mvp.home.view;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.HomeMultipleAdapterNew;

import java.util.ArrayList;
import java.util.List;


/**
 * 做一个测试，这个类可以删除
 */
import butterknife.BindView;

public  class HomeFragemntText extends TBaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private HomeMultipleAdapterNew mAdapter;
    private List<HomeModuleEntity.ADataBean> mData;
    private int currentPage;
    private int lastPage;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_home_column;
    }

    @Override
    protected void initContentView(View view) {
        mData=new ArrayList<>();
        mAdapter= new HomeMultipleAdapterNew(mData);

        setRecyclerView();
        setSwipeRefreshLayout();

    }



    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    private void setSwipeRefreshLayout() {
            mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
    private void setRecyclerView() {
        if (mRecyclerView.getLayoutManager() == null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }



    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMoreRequested() {

    }
}