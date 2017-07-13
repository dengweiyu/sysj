package com.li.videoapplication.mvp.match.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 陪玩/赛事
 */

public class PlayWithAndMatchActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    private ViewPager mPager;

    private ViewPagerAdapter mAdapter;

    private int mPosition = 0;

    private List<Fragment> mFragments;
    @Override
    public void refreshIntent() {
        super.refreshIntent();

        mPosition = getIntent().getIntExtra("position",0);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_play_with_and_match;
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        mPager = (ViewPager) findViewById(R.id.vp_play_with_and_match);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_play_with_and_match);
        findViewById(R.id.tb_back).setOnClickListener(this);
        mFragments = new ArrayList<>();

        mFragments.add(new PlayWithOrderListFragment());
        mFragments.add(new MyMatchFragment());
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(),mFragments,new String[]{"陪玩","赛事"});
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(mPager);
        mPager.setCurrentItem(mPosition);

        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(mPager));
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void loadData() {
        super.loadData();
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
