package com.li.videoapplication.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.MatchVideoList201Entity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.ui.adapter.GroupDetailVideoAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

/**
 * 活动：参赛视频
 *
 */
public class ActivityVideoActivity extends PullToRefreshActivity<VideoImage> implements View.OnClickListener {

    /**
     * 跳转：获奖名单
     */
    private void startWebActivity(String url) {
        WebActivity.startWebActivity(this, url);
    }

    private Match item;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        item = (Match) getIntent().getSerializableExtra("item");
        if (item == null) {
            finish();
        }
    }

    private View headerView;
    private ImageView goback, pic;
    private TextView reward;

    private GroupDetailVideoAdapter adapter;

    @Override
    public int getContentView() {
        return R.layout.activity_activityvideo;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
        setSystemBar(false);
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        actionBar.hide();
    }

    @Override
    public void initView() {
        super.initView();

        setModeEnd();
        addHeaderView(getHeaderView());
        adapter = new GroupDetailVideoAdapter(this, data);
        setAdapter(adapter);
    }

    @Override
    public void loadData() {
        super.loadData();

        onPullDownToRefresh(pullToRefreshListView);
    }

    public View getHeaderView() {
        if (headerView == null) {
            headerView = inflater.inflate(R.layout.header_activityvideo, null);
            pic = (ImageView) headerView.findViewById(R.id.activity_pic);
            goback = (ImageView) headerView.findViewById(R.id.activity_goback);
            reward = (TextView) headerView.findViewById(R.id.activityvideo_reward);
            goback.setOnClickListener(this);
            reward.setOnClickListener(this);
            setListViewLayoutParams(headerView, 234);

        }
        return headerView;
    }

    private void refreshHeaderView(Match item) {

        if (item != null) {
            setImageViewImageNet(pic, item.getFlag());

            if (item.getStatus().equals("已结束") &&
                    !StringUtil.isNull(item.getReward_url()) &&
                    URLUtil.isURL(item.getReward_url())) {// 获奖名单
                reward.setVisibility(View.VISIBLE);
            } else {
                reward.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        case R.id.activity_goback:
            onBackPressed();
            break;

        case R.id.activityvideo_reward:
            if (item.getStatus().equals("已结束") &&
                    !StringUtil.isNull(item.getReward_url()) &&
                    URLUtil.isURL(item.getReward_url())) {// 获奖名单
                startWebActivity(item.getReward_url());
            }
            break;
        }
    }

    @Override
    public void onRefresh() {

        // 赛事列表
        DataManager.matchVideoList201(item.getMatch_id(), getMember_id(), page);
    }

    @Override
    public void onLoadMore() {

        // 赛事列表
        DataManager.matchVideoList201(item.getMatch_id(), getMember_id(), page);
    }

    /**
     * 回调：赛事列表
     */
    public void onEventMainThread(MatchVideoList201Entity event) {
        refreshData(event);
        if (event.getData().getList().size() > 0) {
            if (page == 1) {
                data.clear();
            }
            data.addAll(event.getData().getList());
            adapter.notifyDataSetChanged();
            ++ page;
        }
        isRefreshing = false;
        refreshComplete();
    }

    private void refreshData(MatchVideoList201Entity event) {
        item.setTitle(event.getData().getTitle());
        item.setStarttime(event.getData().getStarttime());
        item.setEndtime(event.getData().getEndtime());
        item.setFlag(event.getData().getFlag());
        refreshHeaderView(item);
    }
}
