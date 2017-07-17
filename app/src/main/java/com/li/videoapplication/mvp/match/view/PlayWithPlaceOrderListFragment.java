package com.li.videoapplication.mvp.match.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.PlayWithPlaceOrderEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.PlayWithPlaceOrderAdapter;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.PlayWithOrderDetailActivity;
import com.li.videoapplication.ui.view.SimpleItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.model.Event;

/**
 * 陪玩 下单列表
 */

public class PlayWithPlaceOrderListFragment extends TBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener ,SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{

    private RecyclerView mList;

    private SwipeRefreshLayout mRefresh;

    private PlayWithPlaceOrderAdapter mAdapter;

    private List<PlayWithPlaceOrderEntity.SectionData> mData;

    private int mPage = 1;

    private  View mEmptyView;
    @Override
    protected int getCreateView() {
        return R.layout.fragment_play_with_place_order;
    }

    @Override
    protected void initContentView(View view) {
        mEmptyView = view.findViewById(R.id.rl_order_list_empty);
        view.findViewById(R.id.ll_order_list_empty).setOnClickListener(this);

        mRefresh = (SwipeRefreshLayout)view.findViewById(R.id.srl_place_order);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mList = (RecyclerView)view.findViewById(R.id.rv_play_with_place_order);
        mData = new ArrayList<>();
        mAdapter = new PlayWithPlaceOrderAdapter(mData);

        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.addItemDecoration(new SimpleItemDecoration(getContext(),false,false,false,true));
        mList.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);

        //点击事件
        mList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (mData.get(i).t != null){

                    ActivityManager.startPlayWithOrderDetailActivity(getContext()
                            ,mData.get(i).t.getOrder_id(),
                            PlayWithOrderDetailActivity.ROLE_OWNER,true);
                }
            }
        });

        mRefresh.setRefreshing(true);
        DataManager.getPlayWithPlaceOrder(getMember_id(),mPage);
    }



    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }


    @Override
    public void onRefresh() {
        mPage = 1;
        DataManager.getPlayWithPlaceOrder(getMember_id(),mPage);
    }

    @Override
    public void onLoadMoreRequested() {
        DataManager.getPlayWithPlaceOrder(getMember_id(),mPage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_order_list_empty:
                //跳转主页教练列表
                ActivityManager.startMainActivity(getContext(),3,0);
                break;
        }
    }

    /**
     * 下单列表
     */
    public void onEventMainThread(PlayWithPlaceOrderEntity entity){
        if (entity != null && entity.isResult()){
            if (mPage == 1){
                mData.clear();
            }
            for (PlayWithPlaceOrderEntity.DataBean data:
                 entity.getData()) {
                PlayWithPlaceOrderEntity.SectionData title = new PlayWithPlaceOrderEntity.SectionData(true,data.getTitle());
                mData.add(title);
                for (PlayWithPlaceOrderEntity.DataBean.ListBean listData:
                     data.getList()) {
                    PlayWithPlaceOrderEntity.SectionData content = new PlayWithPlaceOrderEntity.SectionData(listData);
                    mData.add(content);
                }
            }

            if (entity.getPage_count() > mPage){
                mAdapter.setEnableLoadMore(true);
                mAdapter.setOnLoadMoreListener(this);
            }else {
                mAdapter.setEnableLoadMore(false);
                mAdapter.setOnLoadMoreListener(null);
            }
            mPage++;
            mAdapter.setNewData(mData);
            mRefresh.setRefreshing(false);
            if (mData.size() == 0){
                mEmptyView.setVisibility(View.VISIBLE);
            }else {
                mEmptyView.setVisibility(View.GONE);
            }
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }
}
