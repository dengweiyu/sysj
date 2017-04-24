package com.li.videoapplication.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.pageradapter.WelfarePagerAdapter;
import com.li.videoapplication.ui.fragment.NewSquareFragment;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.ViewPagerY4;

/**
 * 活动：首页更多
 */
public class HomeMoreActivity extends TBaseActivity implements OnPageChangeListener {

    private int page = 1;

    private VideoImageGroup group;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        if (group == null) {
            group = (VideoImageGroup) getIntent().getSerializableExtra("group");
            if (group == null) {
                finish();
            }
            if (StringUtil.isNull(group.getMore_mark())) {
                finish();
            }
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_homemore;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();

        if (group.getTitle().equals("视界原创")){
            setAbTitle("视界推荐");         //为了兼容旧版本 只能这样
        }else {
            setAbTitle(group.getTitle());
        }
    }

    @Override
    public void initView() {
        super.initView();

        initTopMenu();
    }

    private List<RelativeLayout> topButtons;
    private List<ImageView> topLine;
    private List<ImageView> topPoint;
    private List<TextView> mTopMenuText;
    @SuppressWarnings("unused")
    private int currIndex = 0;// 当前页卡编号
    private List<Fragment> fragments;
    private ViewPagerY4 mViewPager;
    private WelfarePagerAdapter adapter;

    protected void initTopMenu() {

        if (topButtons == null) {
            topButtons = new ArrayList<>();
            topButtons.add((RelativeLayout) findViewById(R.id.top_left));
            topButtons.add((RelativeLayout) findViewById(R.id.top_right));
        }

        if (topLine == null) {
            topLine = new ArrayList<>();
            topLine.add((ImageView) findViewById(R.id.top_left_line));
            topLine.add((ImageView) findViewById(R.id.top_right_line));
        }

        if (mTopMenuText == null) {
            mTopMenuText = new ArrayList<>();
            mTopMenuText.add((TextView) findViewById(R.id.top_left_text));
            mTopMenuText.add((TextView) findViewById(R.id.top_right_text));
        }

        if (topPoint == null) {
            topPoint = new ArrayList<>();
            topPoint.add((ImageView) findViewById(R.id.top_left_point));
            topPoint.add((ImageView) findViewById(R.id.top_right_point));
        }

        if (fragments == null) {
            fragments = new ArrayList<>();
            fragments.add(NewSquareFragment.newInstance(NewSquareFragment.HOMEMORE_NEW, group,null,false));
            fragments.add(NewSquareFragment.newInstance(NewSquareFragment.HOMEMORE_HOT, group,null,false));
        }

        mViewPager = (ViewPagerY4) findViewById(R.id.viewpager);
        mViewPager.setScrollable(true);
        mViewPager.setOffscreenPageLimit(2);
        adapter = new WelfarePagerAdapter(manager, fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);

        for (int i = 0; i < topButtons.size(); i++) {
            OnTabClickListener onTabClickListener = new OnTabClickListener(i);
            topButtons.get(i).setOnClickListener(onTabClickListener);
        }

        setTextViewText(mTopMenuText.get(0), R.string.homemore_new);
        setTextViewText(mTopMenuText.get(1), R.string.homemore_hot);

        for (ImageView point : topPoint) {
            point.setVisibility(View.GONE);
        }

        switchTab(0);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        switchTab(arg0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!StringUtil.isNull(group.getMore_mark()))
            UmengAnalyticsHelper.onMainGameMoreEvent(this, group.getMore_mark());
    }

    private class OnTabClickListener implements OnClickListener {

        private int index;

        public OnTabClickListener(int i) {
            this.index = i;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
            switchTab(index);
            currIndex = index;
        }
    }

    private void switchTab(int index) {
        for (int i = 0; i < mTopMenuText.size(); i++) {
            if (index == i) {
                mTopMenuText.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_red));
            } else {
                mTopMenuText.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_gray));
            }
        }
        for (int i = 0; i < topLine.size(); i++) {
            if (index == i) {
                topLine.get(i).setImageResource(R.color.menu_game_red);
            } else {
                topLine.get(i).setImageResource(R.color.menu_game_transperent);
            }
        }
    }
}
