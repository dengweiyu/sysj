package com.li.videoapplication.mvp.mall.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值页面
 */

public class TopUpActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    private ViewPager mPager;
    private TabLayout mTabLayout;

    public int entry;//充值页面入口

    private int currentPage;

    private List<Fragment> mFragments;

    private ViewPagerAdapter mAdapter;
    @Override
    public void refreshIntent() {
        super.refreshIntent();

        Intent intent =  getIntent();
        entry = intent.getIntExtra("entry", Constant.TOPUP_ENTRY_MYWALLEY);
        currentPage = intent.getIntExtra("position",0);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_topup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();

        mPager = (ViewPager)findViewById(R.id.vp_recharge_pager);
        mTabLayout = (TabLayout)findViewById(R.id.tl_recharge_tab);

        if (mFragments == null){
            mFragments = new ArrayList<>();
            mFragments.add(RechargeCurrencyFragment.newInstance());
            mFragments.add(RechargeCoinFragment.newInstance());
            mFragments.add(RechargeVipFragment.newInstance());
        }

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(),mFragments,new String[]{"充值魔豆","充值魔币","购买VIP"});
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(mAdapter);

        mPager.setCurrentItem(currentPage,false);
        mTabLayout.setupWithViewPager(mPager);
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText("充值");
        findViewById(R.id.tb_back).setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
        }
    }
}
