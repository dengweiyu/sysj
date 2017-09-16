package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.FansList203AttentionEntity;
import com.li.videoapplication.data.model.response.FansList203FansEntity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseChildFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.ui.activity.MyPlayerActivity;
import com.li.videoapplication.ui.adapter.SearchMemberAdapter;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：我的粉丝，我的关注
 */
public class MyPlayerFragment extends TBaseChildFragment implements OnRefreshListener2<ListView> {

    private MyPlayerActivity activity;

    public static MyPlayerFragment newInstance(int page) {
        MyPlayerFragment fragment = new MyPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page", page);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int tab;

    private void refreshIntent() {
        try {
            tab = getArguments().getInt("page", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private SearchMemberAdapter adapter;
    private List<Member> data;
    private int page_count = 1;
    private int page = 1;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (MyPlayerActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_searchmember;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return pullToRefreshListView;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initContentView(View view) {

        refreshIntent();

        initListView(view);
        refreshListView();
    }

    private void initListView(View view) {

        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        listView = pullToRefreshListView.getRefreshableView();

        data = new ArrayList<>();
        if (tab == MyPlayerActivity.PAGE_MYFANS)
            adapter = new SearchMemberAdapter(getActivity(), SearchMemberAdapter.PAGE_MYFANS, data,null);
        if (tab == MyPlayerActivity.PAGE_MYFOCUS)
            adapter = new SearchMemberAdapter(getActivity(), SearchMemberAdapter.PAGE_MYFOCUS, data,null);
        listView.setAdapter(adapter);

        pullToRefreshListView.setOnRefreshListener(this);
    }

    public void refreshListView() {

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                onPullDownToRefresh(pullToRefreshListView);
            }
        }, AppConstant.TIME.SEARCH_MEMBER);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 1;
        onPullUpToRefresh(refreshView);
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        PullToRefreshHepler.setLastUpdatedLabel(refreshView);
        onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);
        if (page < page_count) {
            loadData();
        }
    }

    private void loadData(){
        if (activity != null && !StringUtil.isNull(activity.member_id)) {
            if (tab == MyPlayerActivity.PAGE_MYFANS) {
                // 粉丝列表
                DataManager.fansList203Fans(activity.member_id,page);
            } else if (tab == MyPlayerActivity.PAGE_MYFOCUS) {
                // 关注列表
                DataManager.fansList203Attention(activity.member_id,page);
            }
        }
    }

    /**
     * 回调：粉丝列表
     */
    public void onEventMainThread(FansList203FansEntity event) {

        if (tab == MyPlayerActivity.PAGE_MYFANS) {
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
            onRefreshComplete();
        }
    }

    /**
     * 回调：粉丝列表
     */
    public void onEventMainThread(FansList203AttentionEntity event) {

        if (tab == MyPlayerActivity.PAGE_MYFOCUS) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {
                    if (page == 1) {
                        data.clear();
                    }
                    data.addAll(event.getData().getList());
                    adapter.notifyDataSetChanged();
                    ++page;
                }
            }
            onRefreshComplete();
        }
    }

    /**
     * 回调：玩家关注
     */
    public void onEventMainThread(MemberAttention201Entity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }
    }
}
