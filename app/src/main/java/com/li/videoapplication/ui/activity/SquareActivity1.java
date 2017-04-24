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
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.fragment.NewSquareFragment1;
import com.li.videoapplication.ui.pageradapter.GamePagerAdapter;
import com.li.videoapplication.ui.fragment.NewSquareFragment;
import com.li.videoapplication.views.ViewPagerY4;

/**
 * 活动：玩家广场 旧版本
 */
public class SquareActivity1 extends TBaseActivity {

    private List<RelativeLayout> topButtons;
    private List<ImageView> topLine;
    private List<ImageView> topIcon;
    private List<TextView> topText;
    private int currIndex = 0;
    private List<Fragment> fragments;
    private ViewPagerY4 viewPager;
    private GamePagerAdapter adapter;

    @Override
    public int getContentView() {
        return R.layout.activity_square1;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundWhite();
        setAbTitle(R.string.square_title);
    }

    @Override
    public void initView() {
        super.initView();

        initTopMenu();
    }

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

        if (topText == null) {
            topText = new ArrayList<>();
            topText.add((TextView) findViewById(R.id.top_left_text));
            topText.add((TextView) findViewById(R.id.top_right_text));
        }

        if (topIcon == null) {
            topIcon = new ArrayList<>();
            topIcon.add((ImageView) findViewById(R.id.top_left_icon));
            topIcon.add((ImageView) findViewById(R.id.top_right_icon));
        }

        if (fragments == null) {
            fragments = new ArrayList<>();
            fragments.add(NewSquareFragment1.newInstance(NewSquareFragment1.SQUARE_NEW));
            fragments.add(NewSquareFragment1.newInstance(NewSquareFragment1.SQUARE_HOT));
        }

        viewPager = (ViewPagerY4) findViewById(R.id.viewpager);
        viewPager.setScrollable(true);
        viewPager.setOffscreenPageLimit(2);
        adapter = new GamePagerAdapter(manager, fragments);
        viewPager.setAdapter(adapter);
        PageChangeListener listener = new PageChangeListener();
        viewPager.addOnPageChangeListener(listener);

        for (int i = 0; i < topButtons.size(); i++) {
            OnTabClickListener onTabClickListener = new OnTabClickListener(i);
            topButtons.get(i).setOnClickListener(onTabClickListener);
        }

        setTextViewText(topText.get(0), R.string.square_left);
        setTextViewText(topText.get(1), R.string.square_right);

        switchTab(0);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "广场");
    }

    private class PageChangeListener implements OnPageChangeListener {

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
    }

    private class OnTabClickListener implements OnClickListener {

        private int index;

        public OnTabClickListener(int i) {
            this.index = i;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
            switchTab(index);
            currIndex = index;
        }
    }

    private void switchTab(int index) {
        for (int i = 0; i < topText.size(); i++) {
            if (index == i) {
                if (i == 0)
                    topText.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_yellow));
                else
                    topText.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_red));
            } else {
                topText.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_gray));
            }
        }
        for (int i = 0; i < topLine.size(); i++) {
            if (index == i) {
                if (i == 0)
                    topLine.get(i).setImageResource(R.color.menu_game_yellow);
                else
                    topLine.get(i).setImageResource(R.color.menu_game_red);
            } else {
                topLine.get(i).setImageResource(R.color.menu_game_transperent);
            }
        }
    }

}
