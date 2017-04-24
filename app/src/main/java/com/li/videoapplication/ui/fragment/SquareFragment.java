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
import com.li.videoapplication.views.ViewPagerY4;
import com.li.videoapplication.views.ViewPagerY5;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家广场 最新 最热碎片
 */

public class SquareFragment extends TBaseFragment {
    private ViewPagerY5 mViewPager;

    private List<Fragment> mFragments;

    private ViewPagerAdapter mAdapter;

    private SquareScrollEntity mEntity;

    public static  SquareFragment newInstance(String gameId){
        SquareFragment instance = new SquareFragment();

        Bundle bundle = new Bundle();
        bundle.putString("game_id",gameId);
        instance.setArguments(bundle);
        return instance;
    }


    @Override
    protected int getCreateView() {
        return R.layout.fragment_square;
    }

    @Override
    protected void initContentView(View view) {

        String game_id = null;
        try {
            game_id = getArguments().getString("game_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mViewPager = (ViewPagerY5)view.findViewById(R.id.vp_square);

        mFragments = new ArrayList<>();
        mFragments.add(NewSquareFragment1.newInstance(NewSquareFragment1.SQUARE_NEW,null));        //最新列表
        mFragments.add(NewSquareFragment1.newInstance(NewSquareFragment1.SQUARE_HOT,null));        //最热列表

        mAdapter = new ViewPagerAdapter(getChildFragmentManager(),mFragments,new String[]{});


        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mListener);

        mEntity = new SquareScrollEntity();

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

    final ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mEntity.setPosition(mViewPager.getCurrentItem());
            EventBus.getDefault().post(mEntity);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE){

            }
        }
    };
}
