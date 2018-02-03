package com.li.videoapplication.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.SquareScrollEntity;
import com.li.videoapplication.data.model.entity.SquareGameEntity;
import com.li.videoapplication.data.model.event.SquareFilterEvent;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.fragment.SquareFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.views.ViewPagerY4;

import io.rong.eventbus.EventBus;


/**
 * 活动：玩家广场
 */
public class SquareActivity extends TBaseActivity implements View.OnClickListener {

    private int mCurrentPage = 0;

    private List<Fragment> fragments;
    private ViewPagerY4 viewPager;
    private ViewPagerAdapter adapter;

    private TextView mNew;
    private TextView mHot;

    private ImageView mRecord;
    private SquareGameEntity game;

    private View mTabFirst;
    private View mTabSecond;
    @Override
    public int getContentView() {
        return R.layout.activity_square;
    }

    private String mGameId;

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        //
        try {
            mGameId = getIntent().getStringExtra("game_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //根据上次的页面还原
        mCurrentPage = PreferencesHepler.getInstance().getSquareTabPosition();
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

        mTabFirst = findViewById(R.id.ll_square_tab);
        mTabSecond = findViewById(R.id.ll_square_tab_second);

        mRecord = (ImageView)findViewById(R.id.iv_tb_record);
        mRecord.setVisibility(View.VISIBLE);

        mHot = (TextView)findViewById(R.id.tv_title_hot);
        mNew = (TextView)findViewById(R.id.tv_title_new);

        findViewById(R.id.tb_back).setOnClickListener(this);
        findViewById(R.id.iv_square_menu).setOnClickListener(this);

        mNew.setOnClickListener(this);
        mHot.setOnClickListener(this);
        mRecord.setOnClickListener(this);
    }

    protected void initFragment() {
        if (game == null){
            return;
        }

        mTabSecond.setVisibility(View.VISIBLE);
        mTabFirst.setVisibility(View.VISIBLE);

        fragments = new ArrayList<>();
        List<String> title = new ArrayList<>();

        for (int i = 0; i < game.getData().size(); i++) {
            boolean isNeedLoaData = false;
            SquareGameEntity.DataBean bean = game.getData().get(i);
            int offset = Math.abs(mCurrentPage - i);
            if (offset <= 1){
                isNeedLoaData = true;
            }
            fragments.add(SquareFragment.newInstance(bean.getGame_id(),isNeedLoaData,offset * 1000));
            title.add(bean.getName());
        }


        viewPager = (ViewPagerY4) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1);
        adapter = new ViewPagerAdapter(manager, fragments,title.toArray(new String[]{}));
        viewPager.setAdapter(adapter);
        PageChangeListener listener = new PageChangeListener();
        viewPager.addOnPageChangeListener(listener);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_square);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);


        if (mCurrentPage < 0 || mCurrentPage >= fragments.size()){
            setCurrentPage(0);
        }

        if (fragments.size() > 0){
            viewPager.setCurrentItem(mCurrentPage);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "广场");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
        if (game.getData() != null && game.getData().size() >0){
            EventBus.getDefault().post(new SquareScrollEntity(game.getData().get(position).getGame_id(),childPosition));
        }

        setCurrentPage(position);
    }

    private void setCurrentPage(int position){
        mCurrentPage = position;
        PreferencesHepler.getInstance().saveSquareTabPosition(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;
            case R.id.iv_square_menu:
                game.getData().get(viewPager.getCurrentItem()).setChoice(true);
                ActivityManager.startSquareGameChoiceActivity(SquareActivity.this,game);
                game.getData().get(viewPager.getCurrentItem()).setChoice(false);
                break;
            case R.id.tv_title_hot:
                SquareFragment fragmentNew = (SquareFragment) fragments.get(viewPager.getCurrentItem());
                fragmentNew.setCurrentItem(1);
                break;
            case R.id.tv_title_new:
                SquareFragment fragmentHot = (SquareFragment) fragments.get(viewPager.getCurrentItem());
                fragmentHot.setCurrentItem(0);
                break;
            case R.id.iv_tb_record:
                //录屏
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "玩家广场--录制按钮");
                DialogManager.showSquareRecordDialog(this);
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
                //update tab position
                if (mGameId != null){
                    if (mGameId.equals(bean.getGame_id())){
                        mGameId = null;
                        setCurrentPage(title.size() - 1);
                    }
                }
            }

            //not found game, so go to last tab
            if (mGameId  != null){
                setCurrentPage(title.size() - 1);
            }

            initFragment();
        }
    }

    /**
     * 内层ViewPager 滚动事件
     */

    public void onEventMainThread(SquareScrollEntity entity){
        String gameId = game.getData().get(viewPager.getCurrentItem()).getGame_id();
        if (entity.getGameId().equals(gameId)){    //非当前游戏不理会，因为使用了预加载
            String sort = "";
            if (entity.getPosition() == 0){                 //最新列表
                mNew.setTextColor(getResources().getColor(R.color.white));
                mHot.setTextColor(Color.parseColor("#5e5e5e"));
                mNew.setBackgroundResource(R.drawable.square_selected_title);
                mHot.setBackground(null);
                sort = "time";
            }else {                                         //最热列表
                mHot.setTextColor(getResources().getColor(R.color.white));
                mNew.setTextColor(Color.parseColor("#5e5e5e"));
                mHot.setBackgroundResource(R.drawable.square_selected_title);
                mNew.setBackground(null);
                sort = "setRead";
            }
            //统计事件
            DataManager.squareGamePageStatistical(gameId,sort);
        }
    }

    /**
     * 筛选游戏事件
     */

    public void onEventMainThread(SquareFilterEvent event){
        viewPager.setCurrentItem(event.getPosition());

    }

}
