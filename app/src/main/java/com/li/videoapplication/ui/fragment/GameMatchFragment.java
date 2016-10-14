package com.li.videoapplication.ui.fragment;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.EventsList204Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.GameMatchAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：热门游戏赛事
 */
public class GameMatchFragment extends TBaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView> {

    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private GameMatchAdapter adapter;
    private List<Match> data;

    private int page = 1;
    private View nodata;
    private int page_count;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_gamematch;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return pullToRefreshListView;
    }

    @Override
    protected void initContentView(View view) {
        nodata = view.findViewById(R.id.nodata_root);
        initListView(view);

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                onPullDownToRefresh(pullToRefreshListView);
            }
        }, AppConstant.TIME.WELFARE_ACTIVITY);
    }

    private void initListView(View view) {

        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        listView = pullToRefreshListView.getRefreshableView();

        listView.addFooterView(newEmptyView(2));

        data = new ArrayList<>();
        adapter = new GameMatchAdapter(getActivity(), data);
        listView.setAdapter(adapter);

        pullToRefreshListView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 1;
        // 赛事列表
        DataManager.getEventsList204(page);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        PullToRefreshHepler.setLastUpdatedLabel(refreshView);
        onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);
        if (page <= page_count) {
            // 赛事列表
            DataManager.getEventsList204(page);
        }
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

    /**
     * 回调：赛事列表
     */
    public void onEventMainThread(EventsList204Entity event) {

        if (event != null) {
            if (event.isResult()) {
                page_count = event.getData().getPage_count();
                if (event.getData().getList().size() > 0) {
                    if (page == 1) {
                        data.clear();
                    }
                    data.addAll(event.getData().getList());
                    adapter.notifyDataSetChanged();
                    ++page;
                } else {
                    if (page == 1)
                        nodata.setVisibility(View.VISIBLE);
                }
            }
            onRefreshComplete();
        }
    }
}
