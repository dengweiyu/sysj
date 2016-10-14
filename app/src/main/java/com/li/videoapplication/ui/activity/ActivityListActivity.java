package com.li.videoapplication.ui.activity;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.GetMatchList201Entity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.ActivityAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动：活动列表
 */
public class ActivityListActivity extends TBaseActivity implements PullToRefreshBase.OnRefreshListener2<ListView> {

    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private ActivityAdapter adapter;
    private List<Match> data;
    private int page = 1;
    private int page_count;

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
    }

    @Override
    public void initView() {
        super.initView();
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh);

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        listView = pullToRefreshListView.getRefreshableView();

        listView.addFooterView(newEmptyView(2));

        data = new ArrayList<>();
        adapter = new ActivityAdapter(this, data);
        listView.setAdapter(adapter);

        pullToRefreshListView.setOnRefreshListener(this);
    }

    @Override
    public void loadData() {
        super.loadData();
        onPullDownToRefresh(pullToRefreshListView);
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengAnalyticsHelper.onEvent(this,UmengAnalyticsHelper.DISCOVER,"热门活动");
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 1;
        DataManager.getMatchList201(page);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        PullToRefreshHepler.setLastUpdatedLabel(refreshView);
        PullToRefreshHepler.onRefreshCompleteDelayed(getHandler(), pullToRefreshListView,
                PullToRefreshActivity.TIME_REFRESH_SHORT);
        if (page <= page_count)
            // 活动列表
            DataManager.getMatchList201(page);
    }

    /**
     * 回调：赛事列表
     */
    public void onEventMainThread(GetMatchList201Entity event) {

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
                }
            }
            pullToRefreshListView.onRefreshComplete();
        }
    }
}
