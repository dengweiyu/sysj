package com.li.videoapplication.mvp.match.view;

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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.event.MatchListFliterEvent;
import com.li.videoapplication.data.model.response.EventsList214Entity;
import com.li.videoapplication.data.model.response.GameCateEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.MatchListAdapter;
import com.li.videoapplication.mvp.match.MatchContract.IMatchListView;
import com.li.videoapplication.mvp.match.MatchContract.IMatchPresenter;
import com.li.videoapplication.mvp.match.presenter.MatchPresenter;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.dialog.MatchFliterDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 碎片：游戏赛事列表
 */
public class GameMatchFragment extends TBaseFragment implements IMatchListView,
        OnRefreshListener, RequestLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.matchlist_topbar_left)
    TextView tv_topbar_left;
    @BindView(R.id.matchlist_topbar_mid)
    TextView tv_topbar_mid;

    private int page = 1;
    private int page_count;
    private IMatchPresenter presenter;
    private MatchListAdapter adapter;
    private String match_type = "1,2";//默认选中 1:官方赛,2:邀请赛
    private String game_id = "0";//默认选中 0:全部游戏
    private MatchFliterDialog matchFliterDialog;
    private GameCateEntity gameCateData;

    /**
     * 跳转：游戏赛事详情
     */
    private void startGameMatchDetailActivity(String event_id) {
        ActivityManeger.startGameMatchDetailActivity(getContext(), event_id);
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MATCH, "进入赛事");
    }

    /**
     * 跳转：活动详情
     */
    private void startActivityDetailActivity208(String match_id) {
        ActivityManeger.startActivityDetailActivity208(getContext(), match_id);
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MATCH, "赛事列表-进入活动");
    }

    /**
     * 跳转：赛事结果
     */
    private void startMatchResultActivity(String event_id) {
        ActivityManeger.startMatchResultActivity(getContext(), event_id);
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_matchlist;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    protected void initContentView(View view) {
        initRecyclerView();

        initAdapter();

        loadData();

        addOnClickListener();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initAdapter() {
        presenter = MatchPresenter.getInstance();
        presenter.setMatchListView(this);

        List<Match> data = new ArrayList<>();
        adapter = new MatchListAdapter(data);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setOnLoadMoreListener(this);

        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        emptyText.setText("敬请期待更多赛事");
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);
    }

    private void addOnClickListener() {

        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int pos) {
                Match record = (Match) adapter.getItem(pos);
                if (record.getType_id().equals("3")) { //活动
                    startActivityDetailActivity208(record.getMatch_id());
                } else { //赛事
                    startGameMatchDetailActivity(record.getEvent_id());
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

    @OnClick(R.id.matchlist_fliterview)
    void onClick() {
        if (matchFliterDialog != null) {
            matchFliterDialog.show();
        } else {
            if (gameCateData != null)
                matchFliterDialog = DialogManager.showMatchFliterDialog(getContext(), gameCateData);
        }
    }

    private void loadData() {
        // 赛事列表
        presenter.getEventsList(page, match_type, game_id);
        //游戏类型
        presenter.getGameCate();
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        page = 1;
        // 赛事列表
        presenter.getEventsList(page, match_type, game_id);
    }

    //加载更多
    @Override
    public void onLoadMoreRequested() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (page <= page_count) {
                    // 赛事列表
                    presenter.getEventsList(page, match_type, game_id);
                } else {
                    // 没有更多数据
                    adapter.loadMoreEnd();
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "进入赛事列表页面次数");
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MATCH, "进入赛事列表页面次数");
        }
    }

    @Override
    public void hideProgress() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        adapter.loadMoreComplete();//加载完成
    }

    /**
     * 回调：赛事列表
     */
    @Override
    public void refreshMatchListData(EventsList214Entity data) {
        page_count = data.getPage_count();
        if (page == 1) {
            adapter.setNewData(data.getList());
        } else {
            // 如果有下一页则调用addData，不需要把下一页数据add到list里面，直接新的数据给adapter即可。
            adapter.addData(data.getList());
        }
        ++page;
    }

    /**
     * 回调：赛事筛选
     */
    @Override
    public void refreshGameCateData(GameCateEntity data) {
        gameCateData = data;
        // 全部游戏 | 官方赛,邀请赛
        String topbar_mid_text = data.getGameData().get(0).getName()
                + " | " + data.getEventsData().get(0).getName() + ", "
                + data.getEventsData().get(1).getName();
        tv_topbar_mid.setText(topbar_mid_text);
    }

    /**
     * 事件回调：筛选
     */
    public void onEventMainThread(MatchListFliterEvent event) {
        Log.d(tag, "筛选: " + event);
        refreshTopbar(event);
        match_type = event.getMatch_type();
        game_id = event.getGameIds();
        onRefresh();
    }

    private void refreshTopbar(MatchListFliterEvent event) {
        tv_topbar_left.setText("已选择：");
        String topbar_mid_text = event.getGameNames() + event.getMatch_type_names();
        tv_topbar_mid.setText(topbar_mid_text);
    }
}
