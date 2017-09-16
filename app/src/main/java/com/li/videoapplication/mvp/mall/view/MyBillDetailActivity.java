package com.li.videoapplication.mvp.mall.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的账单 魔豆/魔币
 */

public class MyBillDetailActivity extends TBaseAppCompatActivity implements View.OnClickListener {
    public static int MODE_BEANS = 1;
    public static int MODE_COIN = 2;
    private int mMode ;
    private ViewPager mPager;
    private ViewPagerAdapter mAdapter;
    private List<Fragment> mFragments;
    private TextView mTitle;
    @Override
    public void refreshIntent() {
        super.refreshIntent();
        Intent intent = getIntent();
        mMode = intent.getIntExtra("mode",MODE_BEANS);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_my_bill_detail;
    }

    @Override
    public void initView() {
        initToolbar();
        mPager = (ViewPager)findViewById(R.id.vp_my_bill_pager);
        mFragments = new ArrayList<>();
        if (mMode == MODE_BEANS){
            mFragments.add(new MyBeansBillFragment());
            mTitle.setText("魔豆账单");
        }else if (mMode == MODE_COIN){
            mFragments.add(new MyCoinBillFragment());
            mTitle.setText("魔币账单");
        }

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(),mFragments,new String[]{});
        mPager.setAdapter(mAdapter);
    }

    @Override
    public void loadData() {
        super.loadData();
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitle = (TextView) findViewById(R.id.tb_title);

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
