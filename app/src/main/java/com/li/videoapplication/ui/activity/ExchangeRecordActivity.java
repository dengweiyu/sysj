package com.li.videoapplication.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.response.OrderListEntity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.mall.view.ExchangeRecordFragment;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.mvp.adapter.ExchangeRecordAdapter;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：兑换记录
 */
public class ExchangeRecordActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    private List<Fragment> fragments;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public int getContentView() {
        return R.layout.activity_exchangerecord;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();
        findView();
        initViewPager();
    }

    private void findView() {
        findViewById(R.id.tb_back).setOnClickListener(this);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText("兑换记录");
        viewPager = (ViewPager) findViewById(R.id.exchangerecord_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.exchangerecord_tabs);
    }

    private void initViewPager() {
        if (fragments == null) {
            fragments = new ArrayList<>();
            fragments.add(ExchangeRecordFragment.newInstance(ExchangeRecordFragment.EXC_MALL));
            fragments.add(ExchangeRecordFragment.newInstance(ExchangeRecordFragment.EXC_SWEEPSTAKE));
        }

        final String[] tabTitle = {"商城兑换", "抽奖记录"};
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        viewPager.setAdapter(adapter);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
        }
    }
}
