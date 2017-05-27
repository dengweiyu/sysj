package com.li.videoapplication.mvp.billboard.view;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.GroupAttentionGroupEntity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.PlayerRankingCurrencyEntity;
import com.li.videoapplication.data.model.response.PlayerRankingEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.animation.RecyclerViewAnim;
import com.li.videoapplication.mvp.billboard.BillboardContract.IBillboardPresenter;
import com.li.videoapplication.mvp.billboard.BillboardContract.IPlayerBillboardView;
import com.li.videoapplication.mvp.billboard.presenter.BillboardPresenter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.mvp.adapter.PlayerBillboardAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 碎片：主播榜--粉丝榜，财富榜，视频榜
 */
public class PlayerBillboardFragment extends TBaseFragment implements IPlayerBillboardView, OnRefreshListener,
        RequestLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    public static final int PLAYERBILLBOARD_FANS = 1;
    public static final int PLAYERBILLBOARD_CURRENCY = 2;
    public static final int PLAYERBILLBOARD_VIDEO = 3;

    private IBillboardPresenter presenter;

    private View mHeaderView;
    private TextView rank;
    private RelativeLayout container;
    private String myRanking;

    private PlayerBillboardAdapter adapter;

    private int page = 1;
    private int page_count = 50;//最多50页

    public static PlayerBillboardFragment newInstance(int tab) {
        PlayerBillboardFragment fragment = new PlayerBillboardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab", tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    public int tab;

    public int getTab() {
        if (tab == 0) {
            try {
                tab = getArguments().getInt("tab");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tab == 0) {
                tab = PLAYERBILLBOARD_FANS;
            }
        }
        return tab;
    }

    /**
     * 跳转：玩家动态
     */
    private void startDynamicActivity(Member member) {
        ActivityManager.startPlayerDynamicActivity(getActivity(), member);
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "主播榜-个人中心");
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getTab() == PLAYERBILLBOARD_CURRENCY) {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "主播榜-磨豆榜");
            } else if (getTab() == PLAYERBILLBOARD_VIDEO) {
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "主播榜-视频榜");
            }
        }
    }

    @Override
    protected void initContentView(View view) {
        initRecyclerView();

        initAdapter();

        onRefresh();

        addOnClickListener();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initAdapter() {
        presenter = new BillboardPresenter();
        presenter.setPlayerBillboardView(this);

        List<Member> data = new ArrayList<>();
        adapter = new PlayerBillboardAdapter(data, getTab());
        adapter.openLoadAnimation(new RecyclerViewAnim());
        adapter.setOnLoadMoreListener(this);

        adapter.addHeaderView(getHeaderView());
        recyclerView.setAdapter(adapter);
    }

    public View getHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = getActivity().getLayoutInflater().inflate(R.layout.header_playerbillboard, null);
            rank = (TextView) mHeaderView.findViewById(R.id.playerbillboard_rank);
            container = (RelativeLayout) mHeaderView.findViewById(R.id.container);
            setTextViewText(rank, "");
            container.setVisibility(View.GONE);
            mHeaderView.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return mHeaderView;
    }

    private void addOnClickListener() {
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                Member member = (Member) adapter.getItem(position);
                startDynamicActivity(member);
            }
        });
    }

    private void loadData() {
        if (getTab() == PLAYERBILLBOARD_FANS) {
            // 主播榜--粉丝榜
            presenter.loadRankingFansData(getMember_id(), page);
        } else if (getTab() == PLAYERBILLBOARD_CURRENCY) {
            // 主播榜--磨豆榜
            presenter.loadRankingCurrencyData(getMember_id(), page);
        } else if (getTab() == PLAYERBILLBOARD_VIDEO) {
            // 主播榜--视频榜
            presenter.loadRankingVideoData(getMember_id(), page);
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
     * 回调：主播榜--粉丝榜
     */
    @Override
    public void refreshRankingFansData(PlayerRankingEntity data) {
        if (getTab() == PLAYERBILLBOARD_FANS) {
            refreshData(data);
        }
    }

    /**
     * 回调：主播榜--磨豆榜
     */
    @Override
    public void refreshRankingCurrencyData(PlayerRankingCurrencyEntity data) {
        if (getTab() == PLAYERBILLBOARD_CURRENCY) {
            if (data.getData().size() > 0) {
                if (isLogin()) myRanking = data.getMyRanking();
                refreshHeaderView();
                if (page == 1) {
                    adapter.setNewData(data.getData());
                } else {
                    adapter.addData(data.getData());
                }
                ++page;
            }
        }
    }

    /**
     * 回调：主播榜--视频榜
     */
    @Override
    public void refreshRankingVideoData(PlayerRankingEntity data) {
        if (getTab() == PLAYERBILLBOARD_VIDEO) {
            refreshData(data);
        }
    }

    /**
     * 回调：主播榜--玩家关注
     */
    @Override
    public void refreshPlayerAttention(MemberAttention201Entity data) {
        Log.d(tag, "主播榜--玩家关注: "+data.getMsg());
    }

    private void refreshData(PlayerRankingEntity data) {
        if (data.getList().size() > 0) {
            Log.d(tag, "refreshData: data.getMyRanking() == "+ data.getMyRanking());
            if (isLogin()) myRanking = data.getMyRanking();
            refreshHeaderView();
            if (page == 1) {
                adapter.setNewData(data.getList());
            } else {
                adapter.addData(data.getList());
            }
            ++page;
        }
    }

    /**
     * 回调：关注
     */
    public void onEventMainThread(GroupAttentionGroupEntity event) {
        //关注发生改变 刷新列表
        onRefresh();
    }

    private void refreshHeaderView() {
        if (!StringUtil.isNull(myRanking)) {
            container.setVisibility(View.VISIBLE);
            setTextViewText(rank, "您当前的排名是：" + myRanking + " 快去发布视频提高排名吧~");
            return;
        }
        setTextViewText(rank, "");
        container.setVisibility(View.GONE);
    }
}
