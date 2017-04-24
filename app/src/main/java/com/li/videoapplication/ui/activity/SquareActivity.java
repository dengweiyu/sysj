package com.li.videoapplication.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.SquareScrollEntity;
import com.li.videoapplication.data.model.entity.SquareGameEntity;
import com.li.videoapplication.data.model.event.SquareFilterEvent;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.fragment.NewSquareFragment;
import com.li.videoapplication.ui.fragment.SquareFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.views.ViewPagerY4;
import com.ypy.eventbus.EventBus;

/**
 * 活动：玩家广场
 */
public class SquareActivity extends TBaseActivity implements View.OnClickListener {

    private List<Fragment> fragments;
    private ViewPagerY4 viewPager;
    private ViewPagerAdapter adapter;

    private TextView mNew;
    private TextView mHot;

    private SquareGameEntity game;


    @Override
    public int getContentView() {
        return R.layout.activity_square;
    }


    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        EventBus.getDefault().register(this);
    }


    @Override
    public void initView() {
        super.initView();

        initToolbar();
    }

    @Override
    public void loadData() {
        super.loadData();

        //获取分类列表
        DataManager.squareGameList();
    }

    private void initToolbar(){
        setSystemBarBackgroundWhite();
        setTextViewText((TextView)findViewById(R.id.tb_title),"玩家广场");

        mHot = (TextView)findViewById(R.id.tv_title_hot);
        mNew = (TextView)findViewById(R.id.tv_title_new);

        findViewById(R.id.tb_back).setOnClickListener(this);
        findViewById(R.id.iv_square_menu).setOnClickListener(this);
    }

    protected void initFragment() {
        if (game == null){
            return;
        }
        fragments = new ArrayList<>();
        List<String> title = new ArrayList<>();
        for (SquareGameEntity.DataBean bean:
             game.getData()) {
            fragments.add(SquareFragment.newInstance(bean.getGame_id()));
            title.add(bean.getName());
        }


        viewPager = (ViewPagerY4) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        adapter = new ViewPagerAdapter(manager, fragments,title.toArray(new String[]{}));
        viewPager.setAdapter(adapter);
        PageChangeListener listener = new PageChangeListener();
        viewPager.addOnPageChangeListener(listener);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_square);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "广场");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

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


    private void switchTab(int position) {
        int childPosition = ((SquareFragment)fragments.get(position)).getChildPosition();
        EventBus.getDefault().post(new SquareScrollEntity(childPosition));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
            case R.id.iv_square_menu:
                game.getData().get(viewPager.getCurrentItem()).setChoice(true);
                ActivityManeger.startSquareGameChoiceActivity(SquareActivity.this,game);
                game.getData().get(viewPager.getCurrentItem()).setChoice(false);
                break;
        }
    }

    /**
     * 回调：游戏列表
     */
    public void onEventMainThread(SquareGameEntity entity) {
        if (entity.isResult()) {
            game = entity;
            List<String> title = new ArrayList<>();
            for (SquareGameEntity.DataBean bean:
                 entity.getData()) {
                title.add(bean.getName());
            }
            initFragment();
        }
    }

    /**
     * 内层ViewPager 滚动事件
     */

    public void onEventMainThread(SquareScrollEntity entity){
        if (entity.getPosition() == 0){                 //最新列表
            mNew.setTextColor(getResources().getColor(R.color.white));
            mHot.setTextColor(Color.parseColor("#5e5e5e"));
            mNew.setBackgroundResource(R.drawable.square_selected_title);
            mHot.setBackground(null);
        }else {                                         //最热列表
            mHot.setTextColor(getResources().getColor(R.color.white));
            mNew.setTextColor(Color.parseColor("#5e5e5e"));
            mHot.setBackgroundResource(R.drawable.square_selected_title);
            mNew.setBackground(null);
        }
    }

    /**
     * 筛选游戏事件
     */

    public void onEventMainThread(SquareFilterEvent event){
        viewPager.setCurrentItem(event.getPosition());
    }

}
