package com.li.videoapplication.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.fragment.MatchCourseFragment;
import com.li.videoapplication.ui.fragment.QuestionFragment;
import com.li.videoapplication.ui.fragment.RecordCourseFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 碎片：使用帮助
 */
public class HelpActivity extends TBaseActivity implements OnClickListener {

    private ViewPagerAdapter adapter;
    public ViewPager viewPager;
    private List<Fragment> fragments;
    public QuestionFragment questionFragment;

    @Override
    public int getContentView() {
        return R.layout.activity_help;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setSystemBarBackgroundResource(R.color.white);
        setStatusBarDarkMode(false, this);
        setAbTitle(R.string.help_title);

        setAbTitleGray();
        setAbTitleGray();
        abGoback.setOnClickListener(this);
    }

    @Override
    public void initView() {
        super.initView();
        initViewPager();
    }

    private void initViewPager() {
        if (fragments == null) {
            fragments = new ArrayList<>();

            fragments.add(new MatchCourseFragment());
            fragments.add(new RecordCourseFragment());
            questionFragment = new QuestionFragment();
            fragments.add(questionFragment);
        }

        final String[] tabTitle = {"赛事教程", "录屏教程", "问题解答"};
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);
        viewPager.setAdapter(adapter);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_help);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {

        if (v == abGoback) {
            if (questionFragment.isMore) {
                questionFragment.isMore = false;
                // 隐藏该按钮
                questionFragment.moreQuestion.setVisibility(View.VISIBLE);
                questionFragment.normalQuestion.setText("常见问题");
            } else {
                finish();
            }
        }
    }
}
