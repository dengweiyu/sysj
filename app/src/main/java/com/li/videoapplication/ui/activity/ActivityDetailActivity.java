package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.GetDetailModeEntity;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IActivityDetailView;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IActivityPresenter;
import com.li.videoapplication.mvp.activity_gift.presenter.ActivityPresenter;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.fragment.ActivityRulesFragment;
import com.li.videoapplication.ui.fragment.JoinActivityFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：活动详情
 */
@SuppressLint({"SetJavaScriptEnabled", "CutPasteId"})
public class ActivityDetailActivity extends TBaseAppCompatActivity implements IActivityDetailView,
        OnClickListener, ViewPager.OnPageChangeListener {

    public Match match;
    private List<Fragment> fragments;
    public String match_id;
    private TextView tb_title;
    private JoinActivityFragment joinActivityFragment;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private GetDetailModeEntity event;
    private ImageView tb_share;
    private String share_url;
    private IActivityPresenter presenter;

    /**
     * 分享
     */
    public void startShareActivity() {
        if (event != null && !StringUtil.isNull(share_url)) {

            final String title = event.getTitle();
            final String imageUrl = event.getFlag();
            final String content = event.getShare_description();

            ActivityManeger.startActivityShareActivity4VideoPlay(this, share_url, title, imageUrl, content);
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
        presenter = ActivityPresenter.getInstance();
        presenter.setActivityDetailView(this);

        findViewById(R.id.tb_back).setOnClickListener(this);

        tb_share = (ImageView) findViewById(R.id.tb_share);
        tb_title = (TextView) findViewById(R.id.tb_title);

        tb_share.setOnClickListener(this);
        initViewPager();
    }

    @Override
    public void loadData() {
        super.loadData();
        //活动详情
        presenter.getMatchInfo(match_id);
        //tab
        DataManager.getDetailMode(match_id, getMember_id());
    }

    private void initViewPager() {
        joinActivityFragment = new JoinActivityFragment();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(mViewPager));
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(2);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    //tab内容读取接口，故对应的fragment顺序要跟着对应的tab
    private void setTabAndFragment(GetDetailModeEntity event) {
        Log.d(tag, "setTabAndFragment: ");
        List<GetDetailModeEntity.Tab> tab = event.getTab();
        String[] tabTitle = new String[tab.size()];
        refreshShareBtn(0);
        if (fragments == null) {
            fragments = new ArrayList<>();

            for (int i = 0; i < tab.size(); i++) {
                tabTitle[i] = tab.get(i).getName();
                switch (tab.get(i).getType()) {//类型，1=>原生活动参与内容列表，2=>url外链
                    case 1://参与内容列表
                        fragments.add(joinActivityFragment);
                        break;
                    case 2://url外链
                        ActivityRulesFragment web = new ActivityRulesFragment();
                        fragments.add(web);
                        web.setUrl(tab.get(i).getUrl());
                        break;
                }
            }
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        mViewPager.setAdapter(adapter);

        if (tab.size() == 1) {
            tabLayout.setVisibility(View.GONE);
        }else {
            tabLayout.setupWithViewPager(mViewPager);
        }
    }

    private void refreshShareBtn(int tab) {
        Log.d(tag, "refreshShareBtn: tab == " + tab);
        if (event != null) {
            share_url = event.getTab().get(tab).getShare_url();
            if (StringUtil.isNull(share_url)) {
                tb_share.setVisibility(View.INVISIBLE);
            } else {
                tb_share.setVisibility(View.VISIBLE);
            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.competitionRecordClick(match_id, 23);//活动流水点击:23该页面点击
    }

    /**
     * 回调：活动详情
     */
    @Override
    public void refreshActivityDetailData(Match data) {
        match = data;
        setTextViewText(tb_title, data.getTitle());
    }

    /**
     * 回调：活动 tab
     */
    public void onEventMainThread(GetDetailModeEntity event) {

        if (event != null && event.isResult()) {
            this.event = event;
            setTabAndFragment(event);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(tag, "onPageSelected: position == " + position);
        refreshShareBtn(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
