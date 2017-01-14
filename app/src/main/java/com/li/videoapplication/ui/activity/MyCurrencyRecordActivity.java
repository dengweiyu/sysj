package com.li.videoapplication.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.entity.CurrencySection;
import com.li.videoapplication.data.model.response.CurrencyRecordEntity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.mvp.adapter.CurrencyRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 活动：账单
 */

public class MyCurrencyRecordActivity extends TBaseActivity {

    private List<CurrencySection> datas;
    private CurrencyRecordAdapter adapter;

    @Override
    public int getContentView() {
        return R.layout.activity_mycurrencyrecord;
    }

    @Override
    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
        setAbTitle("账单");
    }

    @Override
    public void initView() {
        super.initView();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.currencyrecord_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        datas = new ArrayList<>();
        adapter = new CurrencyRecordAdapter(R.layout.adapter_currencyrecord_item,
                R.layout.adapter_currencyrecord_header, datas);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadData() {
        super.loadData();
        DataManager.getCurrencyRecord(getMember_id());
    }

    /**
     * 回调：账单
     */
    public void onEventMainThread(CurrencyRecordEntity event) {

        if (event != null) {
            if (event.isResult() && event.getData() != null) {

                for (Currency currency : event.getData()) {
                    if (currency.getList() != null && currency.getList().size() > 0) {
                        // 头项
                        CurrencySection header = new CurrencySection(true, currency.getTitle());
                        datas.add(header);
                        // 子项
                        for (Currency c : currency.getList()) {
                            CurrencySection item = new CurrencySection(c);
                            datas.add(item);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
