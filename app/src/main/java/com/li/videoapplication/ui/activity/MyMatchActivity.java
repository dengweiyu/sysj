package com.li.videoapplication.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.MyEventsList204Entity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.ui.adapter.MyMatchListAdapter;

/**
 * 活动：我的赛事
 */

public class MyMatchActivity extends PullToRefreshActivity<Match> {

    private MyMatchListAdapter adapter;
    private View nodata;

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_mymatch;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle("我的赛事");
    }

    @Override
    public void initView() {
        super.initView();
        nodata = findViewById(R.id.nodata_root);
        TextView nodata_text = (TextView)findViewById(R.id.nodata_text);
        nodata_text.setText("您还没有参加过赛事喔~");
        adapter = new MyMatchListAdapter(this, data);
        setAdapter(adapter);
    }

    @Override
    public void loadData() {
        super.loadData();
        onPullDownToRefresh(pullToRefreshListView);
    }


    @Override
    public void onRefresh() {
        DataManager.myEventsList204(PreferencesHepler.getInstance().getMember_id(), page);
    }

    @Override
    public void onLoadMore() {
        DataManager.myEventsList204(PreferencesHepler.getInstance().getMember_id(), page);
    }

    public void onEventMainThread(MyEventsList204Entity event) {
        if (event != null) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {
                    nodata.setVisibility(View.GONE);
                    if (page == 1) {
                        data.clear();
                    }
                    data.addAll(event.getData().getList());
                    adapter.notifyDataSetChanged();
                    ++page;
                } else {//加载更多（不处理）或刷新获取到数据为[]（清空数据）
                    if (page == 1) {
                        data.clear();
                        adapter.notifyDataSetChanged();
                        nodata.setVisibility(View.VISIBLE);
                    }
                }
                isRefreshing = false;
                refreshComplete();
            } else {
                nodata.setVisibility(View.VISIBLE);
            }
        }
    }
}
