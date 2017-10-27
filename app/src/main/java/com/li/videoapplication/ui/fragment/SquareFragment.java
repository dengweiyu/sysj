package com.li.videoapplication.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.SquareScrollEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.views.ViewPagerY5;
import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;

/**
 * 玩家广场 最新 最热碎片
 */

public class SquareFragment extends TBaseFragment {
    private ViewPagerY5 mViewPager;

    private List<Fragment> mFragments;

    private ViewPagerAdapter mAdapter;

    private SquareScrollEntity mEntity;

    private String gameId;

    private boolean isNeedLoadData;

    private int delayTime = 0;

    public static  SquareFragment newInstance(String gameId,boolean isNeedLoadData,int delayTime){
        SquareFragment instance = new SquareFragment();

        Bundle bundle = new Bundle();
        bundle.putString("game_id",gameId);
        bundle.putBoolean("is_need_load_data",isNeedLoadData);
        bundle.putInt("delay_time",delayTime);
        instance.setArguments(bundle);
        return instance;
    }


    @Override
    protected int getCreateView() {
        return R.layout.fragment_square;
    }

    @Override
    protected void initContentView(View view) {
        try {
            gameId = getArguments().getString("game_id");

            isNeedLoadData = getArguments().getBoolean("is_need_load_data");

            delayTime = getArguments().getInt("delay_time");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mViewPager = (ViewPagerY5)view.findViewById(R.id.vp_square);

        if (mFragments == null){
            mFragments = new ArrayList<>();
            mFragments.add(NewSquareFragment.newInstance(NewSquareFragment.SQUARE_NEW,null,gameId,isNeedLoadData,delayTime));        //最新列表
            mFragments.add(NewSquareFragment.newInstance(NewSquareFragment.SQUARE_HOT,null,gameId,isNeedLoadData,delayTime));        //最热列表
            mAdapter = new ViewPagerAdapter(getChildFragmentManager(),mFragments,new String[]{});
        }

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mListener);

        mEntity = new SquareScrollEntity();
        mEntity.setGameId(gameId);
        mEntity.setPosition(0);
        EventBus.getDefault().post(mEntity);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    public int getChildPosition(){

        return mViewPager == null?0:mViewPager.getCurrentItem();
    }

    public void setCurrentItem(int position){
        if (mViewPager != null && position >= 0 && position < mFragments.size()){
            mViewPager.setCurrentItem(position);
        }
    }

    final ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mEntity.setPosition(mViewPager.getCurrentItem());
            mEntity.setGameId(gameId);
            EventBus.getDefault().post(mEntity);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE){

            }
        }
    };
}
