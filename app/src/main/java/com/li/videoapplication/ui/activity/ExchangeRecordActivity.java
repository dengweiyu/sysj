package com.li.videoapplication.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.response.OrderListEntity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.ExchangeRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 活动：兑换记录
 */
public class ExchangeRecordActivity extends TBaseActivity {

    private List<Currency> datas;
    private ExchangeRecordAdapter adapter;

    /**
     * 跳转：订单详情
     */
    private void startOrderDetailActivity(int pos) {
        ActivityManeger.startOrderDetailActivity(ExchangeRecordActivity.this, datas.get(pos).getId());

    }

    @Override
    public int getContentView() {
        return R.layout.activity_exchangerecord;
    }

    @Override
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle("兑换记录");
    }

    @Override
    public void initView() {
        super.initView();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.exchangerecord_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        datas = new ArrayList<>();
        adapter = new ExchangeRecordAdapter(datas);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        //recyclerview item点击事件处理
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                startOrderDetailActivity(position);
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();
        DataManager.getOrderList(getMember_id());
    }

    /**
     * 回调：兑换记录
     */
    public void onEventMainThread(OrderListEntity event) {

        if (event != null && event.isResult()) {
            if (event.getData() != null && event.getData().size() > 0) {
                datas.addAll(event.getData());
                adapter.notifyDataSetChanged();
            }
        }
    }
}
