package com.li.videoapplication.mvp.match.view;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.EventsPKListEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.match.MatchContract.IMatchPresenter;
import com.li.videoapplication.mvp.match.MatchContract.IMatchProcessView;
import com.li.videoapplication.mvp.match.presenter.MatchPresenter;
import com.li.videoapplication.mvp.match.view.GameMatchDetailActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.MatchProcessAdapter;
import com.li.videoapplication.ui.adapter.MatchProcessHeaderAdapter;
import com.li.videoapplication.utils.ClickUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.LoadMoreViewAccent;
import com.li.videoapplication.views.LoadMoreViewWhite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 碎片：赛程
 */
public class GameMatchProcessFragment extends TBaseFragment implements IMatchProcessView,
        OnRefreshListener, RequestLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private IMatchPresenter presenter;
    private GameMatchDetailActivity activity;
    private List<Match> data;
    private List<Match> headerData;
    private MatchProcessHeaderAdapter headeradapter;
    private MatchProcessAdapter adapter;
    private RecyclerView headerRecyclerView;
    public int is_last;
    public int currentHeaderPos = -1;
    private int page = 1;
    private int page_count = 1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (GameMatchDetailActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //fragment处于当前交互状态
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//该fragment处于最前台交互状态
            //刷新赛事详情数据
            onRefresh();
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "赛程");
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_gamematch_process;
    }

    @Override
    protected void initContentView(View view) {
        initHeaderListView(view);
        initRecyclerView();
        initAdapter();
        onRefresh();
        addOnClickListener();
    }

    private void initHeaderListView(View view) {
        headerRecyclerView = (RecyclerView) view.findViewById(R.id.process_horizontallist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        headerRecyclerView.setLayoutManager(linearLayoutManager);
        OverScrollDecoratorHelper.setUpOverScroll(headerRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);

        headerData = new ArrayList<>();
        headeradapter = new MatchProcessHeaderAdapter(headerData);
        headeradapter.receiveFragment(this);
        headerRecyclerView.setAdapter(headeradapter);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initAdapter() {
        presenter = MatchPresenter.getInstance();
        presenter.setMatchProcessView(this);

        data = new ArrayList<>();
        adapter = new MatchProcessAdapter(this, data);
        adapter.openLoadAnimation();
        adapter.setOnLoadMoreListener(this);
        adapter.setLoadMoreView(new LoadMoreViewAccent());

        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyText.setText("该赛程未发布");
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);
    }

    public void setHeaderClick() {
        Log.d(tag, "setHeaderClick: " + currentHeaderPos);
        if (ClickUtil.canClick() && currentHeaderPos != -1) {
            page = 1;
            page_count = 1;//复位
            onLoadMoreRequested();
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        if (activity != null && activity.event_id != null) {
            presenter.getEventsSchedule(activity.event_id);
        }
    }

    //加载更多
    @Override
    public void onLoadMoreRequested() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(tag, "onLoadMore: page == " + page + ", page count == " + page_count);
                if (page <= page_count) {
                    if (currentHeaderPos != -1) {//点击后处于某页的加载更多
                        presenter.getEventsPKList(headerData.get(currentHeaderPos).getSchedule_id(), page, getMember_id());
                    } else {//未点击，默认显示最后一页的加载更多
                        presenter.getEventsPKList(headerData.get(headerData.size() - 1).getSchedule_id(), page, getMember_id());
                    }
                } else {
                    hideProgress();
                    // 没有更多数据
                    adapter.loadMoreEnd();
                }
            }
        });
    }

    private void addOnClickListener() {
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int pos) {
                Match record = (Match) adapter.getItem(pos);
                switch (view.getId()) {
                    case R.id.red_icon:
                        if (RongIM.getInstance() != null &&
                                !StringUtil.isNull(record.getA_member_id()) &&
                                !StringUtil.isNull(record.getA_name()) &&
                                !record.getA_member_id().equals(getMember_id())) {

                            ActivityManeger.startConversationActivity(getActivity(),
                                    record.getA_member_id(), record.getA_name(), false);
                            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "对战表-头像");
                        }
                        break;
                    case R.id.blue_icon:
                        if (RongIM.getInstance() != null &&
                                !StringUtil.isNull(record.getB_member_id()) &&
                                !StringUtil.isNull(record.getB_name())) {

                            if (record.getA_member_id().equals(getMember_id())) {

                                ActivityManeger.startConversationActivity(getActivity(),
                                        record.getB_member_id(), record.getB_name(), true, record.getB_qq());
                            } else {
                                ActivityManeger.startConversationActivity(getActivity(),
                                        record.getB_member_id(), record.getB_name(), false);
                            }
                            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "对战表-头像");
                        }
                        break;
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
     * 回调：赛程时间表
     */
    @Override
    public void refreshEventsScheduleData(List<Match> data) {
        if (data.size() > 0) {
            headerRecyclerView.setVisibility(View.VISIBLE);
            headerData.clear();
            headerData.addAll(data);
            currentHeaderPos = -1;
            headeradapter.notifyDataSetChanged();
            headerRecyclerView.smoothScrollToPosition(headerData.size() - 1);
            onLoadMoreRequested();
        } else {
            headerRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * 回调：赛程PK列表
     */
    @Override
    public void refreshEventsPKListData(EventsPKListEntity data) {
        Log.d(tag, "赛程PK列表: " + data);
        is_last = data.getIs_last();
        page_count = data.getPage_count();

        if (page == 1) {
            adapter.setNewData(data.getList());
        } else {
            adapter.addData(data.getList());
        }
        if (data.getList().size() > 0)
            ++page;
        headeradapter.notifyDataSetChanged();
    }
}
