package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.response.GetMatchInfo208Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.fragment.ActivityRulesFragment;
import com.li.videoapplication.ui.fragment.JoinActivityFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：活动详情
 */
@SuppressLint({"SetJavaScriptEnabled", "CutPasteId"})
public class ActivityDetailActivity208 extends TBaseAppCompatActivity implements OnClickListener {

    public Match match;
    private List<Fragment> fragments;
    public String match_id;
    private TextView tb_title;
    private ActivityRulesFragment activityRulesFragment;
    private JoinActivityFragment joinActivityFragment;

    /**
     * 分享
     */
    public void startShareActivity() {
        if (match != null) {
            final String url = match.getUrl();
            final String title = "精彩活动分享";
            final String imageUrl = match.getFlag();
            final String content = "快来看看" + match.getTitle();

            ActivityManeger.startActivityShareActivity4VideoPlay(this, url, title, imageUrl, content);
        }
    }

    @Override
    public void refreshIntent() {
        super.refreshIntent();

        try {
            match_id = getIntent().getStringExtra("match_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isNull(match_id)) finish();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_activitydetail208;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundColor(Color.parseColor("#0f0f20"));
    }

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.tb_back).setOnClickListener(this);
        findViewById(R.id.tb_share).setOnClickListener(this);
        tb_title = (TextView) findViewById(R.id.tb_title);
        initViewPager();
    }

    @Override
    public void loadData() {
        super.loadData();
        //活动详情
        DataManager.getMatchInfo208(match_id);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                joinActivityFragment.onRefresh();
            }
        }, 200);
    }

    private void initViewPager() {
        if (fragments == null) {
            fragments = new ArrayList<>();

            activityRulesFragment = new ActivityRulesFragment();
            joinActivityFragment = new JoinActivityFragment();
            fragments.add(activityRulesFragment);
            fragments.add(joinActivityFragment);
        }

        final String[] tabTitle = {"活动规则", "参加活动"};
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(mViewPager));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_share:
                startShareActivity();
                break;
        }
    }

    private void refreshData() {
        if (match != null) {
            setTextViewText(tb_title, match.getTitle());
            if (URLUtil.isURL(match.getUrl()) && !StringUtil.isNull(match.getUrl()))
                activityRulesFragment.webView.loadUrl(match.getUrl());
        }
    }

    /**
     * 回调：活动详情
     */
    public void onEventMainThread(GetMatchInfo208Entity event) {

        if (event != null) {
            match = event.getData();
            refreshData();
        }
    }
}
