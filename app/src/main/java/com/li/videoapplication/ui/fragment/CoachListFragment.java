package com.li.videoapplication.ui.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.NetworkError;
import com.li.videoapplication.data.model.response.CoachListEntity;
import com.li.videoapplication.data.model.response.CoachStatusEntity;
import com.li.videoapplication.data.model.response.PackageInfo203Entity;
import com.li.videoapplication.data.network.RequestUrl;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.adapter.CoachLisAdapter;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.utils.ScreenUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * 教练列表
 */

public class CoachListFragment extends TBaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {
    private final long TICK_DELAY = 30000;
    private SwipeRefreshLayout mRefresh;
    private RecyclerView mList;
    private CoachLisAdapter mAdapter;
    private List<CoachListEntity.DataBean.IncludeBean> mData;
    private int mPage = 1;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_coach_list;
    }

    @Override
    protected void initContentView(View view) {
        mList = (RecyclerView) view.findViewById(R.id.rv_coach_list);
        mRefresh = (SwipeRefreshLayout)view.findViewById(R.id.srl_coach_refresh_layout);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mData = new ArrayList<>();
        mAdapter = new CoachLisAdapter(getActivity(),mData);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(10),true,true,true,false));

        mList.setAdapter(mAdapter);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setEnableLoadMore(false);
        mRefresh.setRefreshing(true);

        mList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (mData != null){
                    String memberId = mData.get(i).getMember_id();
                    if (memberId != null){
                        ActivityManager.startCoachDetailActivity(getActivity(),memberId,mData.get(i).getNickname(),mData.get(i).getAvatar());
                    }
                }
            }
        });

        loadData(mPage);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }




    private void loadData(int page){
        DataManager.getCoachList(page);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadData(mPage);
    }

    /**
     * 是否可见
     */
    public void onUserVisibleHint(boolean isVisibleToUser){
        if (isVisibleToUser){
            if (mData != null && mData.size() > 0){
                mTickHandler.post(mTickStatusTask);
            }
        }else {
            if (mTickHandler != null){
                mTickHandler.removeCallbacks(mTickStatusTask);
            }
        }

        System.out.println("UserVisible:"+isVisibleToUser);
    }


    @Override
    public void onLoadMoreRequested() {
        loadData(mPage);
    }

    final Handler mTickHandler = new Handler();


    /**
     * 更新教练状态任务
     */
    final Runnable mTickStatusTask = new Runnable() {
        @Override
        public void run() {
            DataManager.getCoachStatus();
            //30秒
            mTickHandler.postDelayed(this,TICK_DELAY);
        }
    };

    /**
     *教练列表
     */
    public void onEventMainThread(CoachListEntity entity){
        if (entity != null && entity.isResult() && entity.getData() != null){
            refreshData(entity.getData().getInclude());

            if (entity.getData().getPage_count() > mPage){
                mAdapter.setEnableLoadMore(true);
                mAdapter.setOnLoadMoreListener(this);
            }else {
                mAdapter.setEnableLoadMore(false);
                mAdapter.setOnLoadMoreListener(null);
            }
        }else {
            ToastHelper.s("暂无陪练大神哦~");
        }
        mRefresh.setRefreshing(false);
    }


    private void refreshData(List<CoachListEntity.DataBean.IncludeBean> data){
        if (mData == null){
            mData = new ArrayList<>();
        }
        if (mPage == 1){
            mData.clear();
        }

        mData.addAll(Lists.newArrayList(Iterables.filter(data, new Predicate<CoachListEntity.DataBean.IncludeBean>() {
            @Override
            public boolean apply(CoachListEntity.DataBean.IncludeBean input) {

                for (CoachListEntity.DataBean.IncludeBean d:
                     mData) {
                    if (d.getMember_id() != null && input.getMember_id() != null){
                        if (d.getMember_id().equals(input.getMember_id())){
                            return false;
                        }
                    }
                }
                return true;
            }
        })));

        mAdapter.setNewData(mData);
        mPage++;
    }

    /**
     * 教练状态
     */
    public void onEventMainThread(final CoachStatusEntity entity){
        if (mData != null){
            List<CoachListEntity.DataBean.IncludeBean> newData = new ArrayList<>();
            for (CoachStatusEntity.DataBean data:
                 entity.getData()) {
                for (int i = 0; i < mData.size(); i++) {
                    if (data.getMember_id() != null && data.getMember_id().equals(mData.get(i).getMember_id())){
                        int status = 3;
                        try {
                            status = Integer.parseInt(data.getStatusX());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        mData.get(i).setStatusX(status);
                        newData.add(mData.get(i));
                    }
                }
            }

            mData.clear();
            mData.addAll(newData);
            mAdapter.setNewData(mData);
        }
    }


    /**
     * 网络错误
     */
    public void onEventMainThread(NetworkError error){
        if (error.getUrl().equals(RequestUrl.getInstance().getCoachList())){
            mRefresh.setRefreshing(false);
        }
    }


}
