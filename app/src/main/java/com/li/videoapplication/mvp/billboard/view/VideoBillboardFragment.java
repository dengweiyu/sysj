package com.li.videoapplication.mvp.billboard.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.data.model.response.VideoRankingEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.VideoBillboardAdapter;
import com.li.videoapplication.mvp.billboard.BillboardContract.IBillboardPresenter;
import com.li.videoapplication.mvp.billboard.BillboardContract.IVideoBillboardView;
import com.li.videoapplication.mvp.billboard.presenter.BillboardPresenter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 碎片：视频榜--点赞榜，评论榜，观看榜
 */
public class VideoBillboardFragment extends TBaseFragment implements IVideoBillboardView, OnRefreshListener,
        RequestLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    public static final int VIDEOBILLBOARD_LIKE = 1;// 点赞榜
    public static final int VIDEOBILLBOARD_COMMENT = 2;// 评论榜
    public static final int VIDEOBILLBOARD_CLICK = 3;// 观看榜

    private IBillboardPresenter presenter;
    public BillboardActivity activity;
    public int billboard;
    private VideoBillboardAdapter adapter;
    private int page = 1;
    private int page_count = 50;//最多50页

    public static VideoBillboardFragment newInstance(int billboard) {
        VideoBillboardFragment fragment = new VideoBillboardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab", billboard);
        fragment.setArguments(bundle);
        return fragment;
    }

    public int getBillboard() {
        if (billboard == 0) {
            try {
                billboard = getArguments().getInt("tab");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (billboard == 0) {
                billboard = VIDEOBILLBOARD_LIKE;
            }
        }
        return billboard;
    }

    @Override
    protected int getCreateView() {
        return R.layout.refresh_recyclerview;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.activity = (BillboardActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getBillboard() == VIDEOBILLBOARD_COMMENT) {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "视频榜-评论榜");
            } else if (getBillboard() == VIDEOBILLBOARD_CLICK) {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "视频榜-观看榜");
            }
        }
    }


    @Override
    protected void initContentView(View view) {

        initRecyclerView();

        initAdapter();

        onRefresh();

        addOnClickListener();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initAdapter() {
        presenter = new BillboardPresenter();
        presenter.setVideoBillboardView(this);

        List<VideoImage> data = new ArrayList<>();
        adapter = new VideoBillboardAdapter(data);
        adapter.setTab(getBillboard());
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setOnLoadMoreListener(this);

        recyclerView.setAdapter(adapter);
    }

    private void addOnClickListener() {

    }

    private void loadData() {
        if (getBillboard() == VIDEOBILLBOARD_LIKE) {
            // 视频榜--点赞榜
            presenter.loadVideoBillboardRankingData(getMember_id(), "like", page);
        } else if (getBillboard() == VIDEOBILLBOARD_COMMENT) {
            // 视频榜--评论榜
            presenter.loadVideoBillboardRankingData(getMember_id(), "comment", page);
        } else if (getBillboard() == VIDEOBILLBOARD_CLICK) {
            // 视频榜--观看榜
            presenter.loadVideoBillboardRankingData(getMember_id(), "click", page);
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
     * 视频榜--点赞榜,评论榜,观看榜
     */
    @Override
    public void refreshVideoBillboardRankingData(VideoRankingEntity data) {
        if (data.getList().size() > 0) {
            if (page == 1) {
                adapter.setNewData(data.getList());
            } else {
                adapter.addData(data.getList());
            }
            ++page;
        }
    }

    /**
     * 回调：视频收藏
     */
    public void onEventMainThread(VideoCollect2Entity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }
    }

    /**
     * 回调：视频点赞
     */
    public void onEventMainThread(VideoFlower2Entity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }
    }
}
