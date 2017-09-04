package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.CreatePlayWithOrderActivity;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.adapter.CoachLisAdapter;
import com.li.videoapplication.ui.view.SpanItemDecoration;
import com.li.videoapplication.ui.view.SpanSingleDecoration;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;


import java.util.ArrayList;
import java.util.List;
/**
 * 教练列表
 */

public class CoachListFragment extends TBaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener ,
        View.OnClickListener{
    private final long TICK_DELAY = 40000;
    private SwipeRefreshLayout mRefresh;
    private RecyclerView mList;
    private CoachLisAdapter mAdapter;

    private List<CoachListEntity.DataBean.IncludeBean> mData;
    private int mPage = 1;

    private TextView mDiscount;

    private MainActivity mActivity;

    private float mOffset = 0f;
    private float mStartOffset = 0f;
    private float mLastDy = 0f;
    private boolean mIsShowMenu = true;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity){
            mActivity  = (MainActivity) activity;
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_coach_list;
    }

    @Override
    protected void initContentView(View view) {

        view.findViewById(R.id.tv_create_order_shortcut).setOnClickListener(this);

        mList = (RecyclerView) view.findViewById(R.id.rv_coach_list);
        mDiscount = (TextView) view.findViewById(R.id.tv_discount_top);
        mRefresh = (SwipeRefreshLayout)view.findViewById(R.id.srl_coach_refresh_layout);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mData = new ArrayList<>();
        mAdapter = new CoachLisAdapter(getActivity(),mData);
        mList.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mList.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(10),true,true,false,true){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position =  parent.getChildAdapterPosition(view);
                if (position % 2 == 0){
                    super.getItemOffsets(outRect, view, parent, state);
                }else {
                    outRect.right = mMargin;
                    outRect.bottom = mMargin;
                }
            }
        });



        mList.addItemDecoration(new SpanItemDecoration(ScreenUtil.dp2px(10),false,false,false,false){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position =  parent.getChildAdapterPosition(view);
                switch (position){
                    case 0:
                        if (mDiscount.getVisibility() == View.VISIBLE){
                            outRect.top = ScreenUtil.dp2px(62);
                        }else {
                            outRect.top = ScreenUtil.dp2px(62);
                        }
                        break;
                    case 1:
                        if (mDiscount.getVisibility() == View.VISIBLE){
                            outRect.top = ScreenUtil.dp2px(62);
                        }else {
                            outRect.top = ScreenUtil.dp2px(62);
                        }
                        break;
                }
            }
        });

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


        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mStartOffset = mOffset;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
             //   System.out.println("dy:"+dy+" mOffset:"+mOffset+" mStartOffset:"+mStartOffset+" mIsShowMenu:"+mIsShowMenu);
                if (mLastDy > 0 && dy < 0 || mLastDy < 0 && dy >0){
                    mStartOffset = mOffset;
                }
                mLastDy = dy;

                mOffset += dy;
                if (mOffset - mStartOffset > ScreenUtil.dp2px(5) && mIsShowMenu){
                    if (mActivity != null){
                        mIsShowMenu = false;
                        mActivity.refreshBottomMenu(false);
                        mStartOffset = mOffset;
                    }
                } else if (mOffset - mStartOffset < -ScreenUtil.dp2px(5) && !mIsShowMenu) {
                    if (mActivity != null){
                        mIsShowMenu = true;
                        mActivity.refreshBottomMenu(true);
                        mStartOffset = mOffset;

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_create_order_shortcut:
                ActivityManager.startCreatePlayWithOrderActivity(getActivity(), CreatePlayWithOrderActivity.MODE_ORDER_GRAB,null,null,null,null);
                break;
        }
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
            UmengAnalyticsHelper.onEvent(getActivity(),UmengAnalyticsHelper.MAIN,"陪练列表");

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
            }else {
                mDiscount.setVisibility(View.VISIBLE);
                mDiscount.setText(entity.getNotice());
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
