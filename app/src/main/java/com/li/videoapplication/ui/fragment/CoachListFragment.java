package com.li.videoapplication.ui.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import com.li.videoapplication.data.model.response.ConfirmOrderEntity;
import com.li.videoapplication.data.model.response.PackageInfo203Entity;
import com.li.videoapplication.data.network.RequestUrl;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.adapter.CoachLisAdapter;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.ui.view.SpanSingleDecoration;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 教练列表
 */

public class CoachListFragment extends TBaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {
    private final long TICK_DELAY = 30000;
    private Timer mTimer = null;

    private SwipeRefreshLayout mRefresh;
    private RecyclerView mList;
    private CoachLisAdapter mAdapter;

    private List<CoachListEntity.DataBean.IncludeBean> mData;
    private int mPage = 1;

    private TextView mDiscount;

    private RecyclerView.ItemDecoration mItemDecoration;

    private RecyclerView.ItemDecoration mBottomDecoration;
    @Override
    protected int getCreateView() {
        return R.layout.fragment_coach_list;
    }

    @Override
    protected void initContentView(View view) {
        mList = (RecyclerView) view.findViewById(R.id.rv_coach_list);
        mDiscount = (TextView) view.findViewById(R.id.tv_discount_top);
        mRefresh = (SwipeRefreshLayout)view.findViewById(R.id.srl_coach_refresh_layout);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mData = new ArrayList<>();
        mAdapter = new CoachLisAdapter(getActivity(),mData);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));

       // mItemDecoration = new SpanItemDecoration(ScreenUtil.dp2px(10),true,true,true,false);
     //   mList.addItemDecoration(mItemDecoration);

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
        DataManager.getCoachList(page,false);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadData(mPage);
    }

    final  Handler mHandler = new Handler();

    private boolean mIsVisibleToUser = false;

    private long mLastTimeRefresh = 0L;

    final Runnable mRefreshTask = new Runnable() {
        @Override
        public void run() {

            if (System.currentTimeMillis() - mLastTimeRefresh < TICK_DELAY - 5000){
                mHandler.removeCallbacks(this);
                return;
            }

            mLastTimeRefresh = System.currentTimeMillis();

            if (mIsVisibleToUser){
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this,TICK_DELAY);
            }else {
                mHandler.removeCallbacks(this);
            }
            DataManager.getCoachStatus();
        }
    };


    /**
     * 是否可见
     */
    public void onUserVisibleHint(boolean isVisibleToUser){
        if (mIsVisibleToUser == isVisibleToUser){
            return;
        }
        mIsVisibleToUser = isVisibleToUser;

        if (isVisibleToUser){
            mHandler.removeCallbacks(mRefreshTask);
            mHandler.postDelayed(mRefreshTask,TICK_DELAY);
        }else {
            mHandler.removeCallbacks(mRefreshTask);
        }
    }


    @Override
    public void onLoadMoreRequested() {
        loadData(mPage);
    }

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

            if (StringUtil.isNull(entity.getNotice())){
                mDiscount.setVisibility(View.GONE);

                mList.removeItemDecoration(mBottomDecoration);
                mList.removeItemDecoration(mItemDecoration);
                mItemDecoration = new SpanItemDecoration(ScreenUtil.dp2px(10),true,true,true,false);
                mBottomDecoration = new SpanSingleDecoration(ScreenUtil.dp2px(10),false,false,false,true,entity.getData().getInclude().size() -1);
                mList.addItemDecoration(mItemDecoration);
                mList.addItemDecoration(mBottomDecoration);
            }else {
                mDiscount.setVisibility(View.VISIBLE);
                mDiscount.setText(entity.getNotice());

                mList.removeItemDecoration(mBottomDecoration);
                mList.removeItemDecoration(mItemDecoration);
                mItemDecoration = new SpanItemDecoration(ScreenUtil.dp2px(10),true,true,false,true);
                mList.addItemDecoration(mItemDecoration);
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


    /**
     *订单支付结果
     */
    public void onEventMainThread(ConfirmOrderEntity entity){
        if (entity.isResult()){
            //更新优惠信息
            mPage = 1;
            loadData(mPage);
        }
    }

}
