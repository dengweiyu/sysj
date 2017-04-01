package com.li.videoapplication.ui.activity;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.response.MemberTaskEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.fragment.DailyRewardFragment;
import com.li.videoapplication.ui.fragment.HonorRewardFragment;
import com.li.videoapplication.ui.fragment.MatchRewardFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.AppUtil;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：我的钱包
 */
public class MyWalletActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    public ViewPager mViewPager;
    private List<Fragment> fragments;
    private TextView beanNum;
    private DailyRewardFragment dailyFragment;
    private MatchRewardFragment matchFragment;
    private HonorRewardFragment honorFragment;
    private TabLayout tabLayout;

    /**
     * 跳转：账单
     */
    private void startMyCurrencyRecordActivity() {
        ActivityManeger.startMyCurrencyRecordActivity(this);
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "我的钱包-账单");
    }

    /**
     * 跳转：充值
     */
    private void startTopUpActivity() {
        ActivityManeger.startTopUpActivity(this, Constant.TOPUP_ENTRY_MYWALLEY);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_mywallet;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        setSystemBarBackgroundColor(Color.WHITE);
    }

    @Override
    public void initView() {
        super.initView();
        beanNum = (TextView) findViewById(R.id.mywallet_beannum);

        findViewById(R.id.goback).setOnClickListener(this);
        findViewById(R.id.mywallet_currencyrecord).setOnClickListener(this);
        findViewById(R.id.mywallet_currencytopup).setOnClickListener(this);
        findViewById(R.id.tv_about_currency).setOnClickListener(this);

        initViewPager();
    }

    @Override
    public void loadData() {
        super.loadData();
        DataManager.getMemberTask(getMember_id());
    }


    private void initViewPager() {
        dailyFragment = new DailyRewardFragment();
        matchFragment = new MatchRewardFragment();
        honorFragment = new HonorRewardFragment();

        mViewPager = (ViewPager) findViewById(R.id.mywallet_viewpager);
        mViewPager.setOffscreenPageLimit(2);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(mViewPager));

        tabLayout = (TabLayout) findViewById(R.id.mywallet_tab);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goback:
                finish();
                break;
            case R.id.tv_about_currency:
                WebActivityJS.startWebActivityJS(this, Constant.WEB_WALLET, "我的钱包说明", null);
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "我的钱包-钱包说明");
                break;
            case R.id.mywallet_currencyrecord:
                startMyCurrencyRecordActivity();
                break;
            case R.id.mywallet_currencytopup://暂时用来测试支付宝支付
                startTopUpActivity();
                break;
        }
    }

    //tab内容读取接口，故对应的fragment顺序要跟着对应的tab
    private void setTabAndFragment(MemberTaskEntity event) {
        List<Currency> type_menu = event.getType_menu();
        String[] tabTitle = new String[type_menu.size()];

        if (fragments == null) {
            fragments = new ArrayList<>();

            for (int i = 0; i < type_menu.size(); i++) {
                tabTitle[i] = type_menu.get(i).getName();
                switch (type_menu.get(i).getGroup()) {
                    case "daily":
                        fragments.add(dailyFragment);
                        break;
                    case "events":
                        fragments.add(matchFragment);
                        break;
                    case "honor":
                        fragments.add(honorFragment);
                        break;
                }
            }
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setFragmentData(MemberTaskEntity event) {
        dailyFragment.setDailyData(event.getDaily());
        dailyFragment.setNoviceData(event.getNovice());
        matchFragment.setMatchData(event.getEvents());
        honorFragment.setHonorData(event.getHonor());
    }

    /**
     * 回调：会员任务接口
     */
    public void onEventMainThread(MemberTaskEntity event) {

        if (event != null && event.isResult()) {
            setTabAndFragment(event);
            setTextViewText(beanNum, StringUtil.formatNum(event.getMember_currency()));
            setFragmentData(event);
        }
    }
}
