package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.MyMatchList201Entity;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.adapter.ActivityAdapter;

/**
 * 碎片：我的活动
 */
public class MyActivityFragment extends TBaseFragment implements OnRefreshListener2<ListView> {

    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private ActivityAdapter adapter;
    private List<Match> data;

    private int page = 1;
    private View nodata;
    private int page_count;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_myactivity;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return mPullToRefreshListView;
    }

    @Override
    protected void initContentView(View view) {
        nodata = view.findViewById(R.id.nodata_root);
        TextView nodata_text = (TextView) view.findViewById(R.id.nodata_text);
        nodata_text.setText("您还没有参加过活动喔~");
        initListView(view);

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                onPullDownToRefresh(mPullToRefreshListView);
            }
        }, AppConstant.TIME.MYACTIVITY_FRAGMENT);
    }

    private void initListView(View view) {

        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);

        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        mListView = mPullToRefreshListView.getRefreshableView();

        mListView.addHeaderView(newEmptyView(2));
        mListView.addFooterView(newEmptyView(2));

        data = new ArrayList<Match>();
        adapter = new ActivityAdapter(getActivity(), data);
        mListView.setAdapter(adapter);

        mPullToRefreshListView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        page = 1;
        // 我的赛事列表
        DataManager.myMatchList201(getMember_id(), page);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);
        if (page <= page_count)
            // 我的赛事列表
            DataManager.myMatchList201(getMember_id(), page);
    }

    /**
     * 回调:我的赛事列表
     */
    public void onEventMainThread(MyMatchList201Entity event) {
        if (event != null) {
            if (event.isResult()) {
                page_count = event.getData().getPage_count();
                if (event.getData().getList().size() > 0) {
                    nodata.setVisibility(View.GONE);
                    if (page == 1) {
                        data.clear();
                    }
                    data.addAll(event.getData().getList());
                    adapter.notifyDataSetChanged();
                    ++page;
                } else {
                    nodata.setVisibility(View.VISIBLE);
                }
            } else {
                nodata.setVisibility(View.VISIBLE);
            }
            onRefreshComplete();
        }
    }

    /**
     * 事件：登录
     */
    public void onEventMainThread(LoginEvent event) {

        onPullDownToRefresh(mPullToRefreshListView);
    }

    /**
     * 事件：注销
     */
    public void onEventMainThread(LogoutEvent event) {

        onPullDownToRefresh(mPullToRefreshListView);
    }
}
