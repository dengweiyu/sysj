package com.li.videoapplication.mvp.mall.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.ExchangeRecordAdapter;
import com.li.videoapplication.mvp.mall.MallContract.IExchangeRecordView;
import com.li.videoapplication.mvp.mall.MallContract.IMallPresenter;
import com.li.videoapplication.mvp.mall.presenter.MallPresenter;
import com.li.videoapplication.ui.ActivityManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 碎片：兑换记录--商城兑换/抽奖记录
 */
public class ExchangeRecordFragment extends TBaseFragment implements IExchangeRecordView {

    public static final int EXC_MALL = 1;//商城兑换
    public static final int EXC_SWEEPSTAKE = 2;//抽奖记录

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ExchangeRecordAdapter adapter;
    private IMallPresenter presenter;

    /**
     * 跳转：订单详情
     */
    private void startOrderDetailActivity(String order_id) {
        ActivityManager.startOrderDetailActivity(getActivity(), order_id, getTab());
    }

    public static ExchangeRecordFragment newInstance(int tab) {
        ExchangeRecordFragment fragment = new ExchangeRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab", tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    public int tab;

    public int getTab() {
        if (tab == 0) {
            try {
                tab = getArguments().getInt("tab");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tab == 0) {
                tab = EXC_MALL;
            }
        }
        return tab;
    }

    @Override
    protected int getCreateView() {
        return R.layout.refresh_recyclerview;
    }

    @Override
    protected void initContentView(View view) {
        initRecyclerView();

        initAdapter();

        loadData();

        addOnClickListener();
    }

    private void initRecyclerView() {
        presenter = new MallPresenter();
        presenter.setExchangeRecordView(this);

        swipeRefreshLayout.setEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initAdapter() {
        List<Currency> data = new ArrayList<>();
        adapter = new ExchangeRecordAdapter(data, getTab());
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
                (ViewGroup) recyclerView.getParent(), false);
        TextView emptyViewText = (TextView) emptyView.findViewById(R.id.emptyview_text);
        if (getTab() == EXC_MALL)
            emptyViewText.setText("您还没有兑换过商品喔~");
        else if (getTab() == EXC_SWEEPSTAKE)
            emptyViewText.setText("您还没有抽奖记录喔~");
        adapter.setEmptyView(emptyView);

        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        switch (getTab()) {
            case EXC_MALL://商城兑换
                presenter.getOrderList(getMember_id());
                break;
            case EXC_SWEEPSTAKE://抽奖记录
                presenter.getMemberAward(getMember_id());
                break;
        }

    }

    private void addOnClickListener() {
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Currency record = (Currency) adapter.getItem(position);
                startOrderDetailActivity(record.getId());
            }

        });
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    /**
     * 回调：商城兑换记录
     */
    @Override
    public void refreshOrderListData(List<Currency> data) {
        if (getTab() == EXC_MALL) {
            adapter.setNewData(data);
        }
    }

    /**
     * 回调：抽奖记录
     */
    @Override
    public void refreshRewardListData(List<Currency> data) {
        if (getTab() == EXC_SWEEPSTAKE) {
            adapter.setNewData(data);
        }
    }
}
