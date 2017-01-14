package com.li.videoapplication.ui.activity;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.data.model.response.PackageList203Entity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.GiftAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动：礼包列表
 */
public class GiftListActivity extends TBaseActivity implements OnRefreshListener2<ListView>,View.OnClickListener {

    private PullToRefreshListView pullToRefreshListView;
    private GiftAdapter adapter;
    private List<Gift> data;
    private int page = 1;
    private int page_count;

    /**
     * 跳转：我的礼包
     */
    private void startMyGiftListActivity() {
        ActivityManeger.startMyGiftListActivity(this);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_gift;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle("礼包");
        abMyGift.setVisibility(View.VISIBLE);
        abMyGift.setOnClickListener(this);
    }

    @Override
    public void initView() {
        super.initView();
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        ListView listView = pullToRefreshListView.getRefreshableView();

        data = new ArrayList<>();
        adapter = new GiftAdapter(this, data);
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
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "热门礼包");
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 1;
        // 礼包列表
        DataManager.packageList203(getMember_id(), page);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        PullToRefreshHepler.setLastUpdatedLabel(refreshView);
        PullToRefreshHepler.onRefreshCompleteDelayed(getHandler(), pullToRefreshListView,
                PullToRefreshActivity.TIME_REFRESH_SHORT);
        if (page <= page_count)
            // 礼包列表
            DataManager.packageList203(getMember_id(), page);
    }

    /**
     * 回调：礼包列表203
     */
    public void onEventMainThread(PackageList203Entity event) {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ab_mygift:
                startMyGiftListActivity();
                break;
        }
    }
}
