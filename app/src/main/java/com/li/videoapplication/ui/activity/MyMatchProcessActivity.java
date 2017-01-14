package com.li.videoapplication.ui.activity;

import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.fragment.MyFinishedMatchFragment;
import com.li.videoapplication.ui.fragment.MyOnGoingMatchFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：我的赛程
 */
public class MyMatchProcessActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    public ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragments;
    public String event_id;
    public Match matchDetailMatch;
    public String customerServiceID, customerServiceName;
    private MyOnGoingMatchFragment ongoingFragment;
    public int contactLeft, contactTop, uploadLeft, uploadTop;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        try {
            matchDetailMatch = (Match) getIntent().getSerializableExtra("match");
            event_id = matchDetailMatch.getEvent_id();
            customerServiceID = getIntent().getStringExtra("customer_service");
            customerServiceName = getIntent().getStringExtra("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_mymatchprocess;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundWhite();
    }

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.goback).setOnClickListener(this);
        initViewPager();
    }

    @Override
    public void loadData() {
        super.loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MATCH, "我的赛程");
    }

    //只能在Activity这个周期中获取控件坐标
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //状态栏高度
        int statusHeight = ScreenUtil.getStatusHeight(this);
        //"约战TA"全局坐标（包括状态栏）
        Rect contactRect = new Rect();
        ongoingFragment.contact.getGlobalVisibleRect(contactRect);

        contactLeft = contactRect.left - 40;
        contactTop = contactRect.top - statusHeight - 13;

        //上传按钮 全局坐标
        Rect uploadRect = new Rect();
        ongoingFragment.uploadImage.getGlobalVisibleRect(uploadRect);

        uploadLeft = uploadRect.left;
        uploadTop = uploadRect.top - statusHeight;
    }

    private void initViewPager() {
        if (fragments == null) {
            fragments = new ArrayList<>();
            ongoingFragment = new MyOnGoingMatchFragment();
            fragments.add(ongoingFragment);
            fragments.add(new MyFinishedMatchFragment());
        }

        final String[] tabTitle = {"进行中", "已结束"};
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_mymatchprocess);
        mViewPager.setAdapter(adapter);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(mViewPager));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_mymatchprocess);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goback:
                finish();
                break;
        }
    }
}
