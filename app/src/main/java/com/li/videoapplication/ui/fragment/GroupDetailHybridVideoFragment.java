package com.li.videoapplication.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.SquareScrollEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.views.ViewPagerY5;

import java.util.ArrayList;
import java.util.List;


/**
 * 混合页面下的最新最热
 */

public class GroupDetailHybridVideoFragment extends TBaseFragment implements View.OnClickListener {
    private ViewPagerY5 mViewPager;

    private List<Fragment> mFragments;

    private ViewPagerAdapter mAdapter;

    private String mGroupId;

    private TextView mNew;
    private TextView mHot;


    public static  GroupDetailHybridVideoFragment newInstance(String groupId){
        GroupDetailHybridVideoFragment instance = new GroupDetailHybridVideoFragment();

        Bundle bundle = new Bundle();
        bundle.putString("group_id",groupId);

        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "游戏圈-视频");
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_group_detail_hybrid_video;
    }

    @Override
    protected void initContentView(View view) {
        try {
            mGroupId = getArguments().getString("group_id");

        } catch (Exception e) {
            e.printStackTrace();
        }

        mHot = (TextView)view.findViewById(R.id.tv_title_hot);
        mNew = (TextView)view.findViewById(R.id.tv_title_new);

        mViewPager = (ViewPagerY5)view.findViewById(R.id.vp_group_detail_video);

        if (mFragments == null){
            mFragments = new ArrayList<>();
            mFragments.add(GroupdetailVideoFragment.newInstance(GroupdetailVideoFragment.GROUPDETAILVIDEO_NEW,mGroupId));        //最新列表
            mFragments.add(GroupdetailVideoFragment.newInstance(GroupdetailVideoFragment.GROUPDETAILVIDEO_HOT,mGroupId));        //最热列表
            mAdapter = new ViewPagerAdapter(getChildFragmentManager(),mFragments,new String[]{});
        }

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mListener);

        mNew.setOnClickListener(this);
        mHot.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_title_hot:
                setCurrentItem(1);

                break;
            case R.id.tv_title_new:
                setCurrentItem(0);
                break;
        }
    }

    final ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    mNew.setTextColor(getResources().getColor(R.color.white));
                    mHot.setTextColor(Color.parseColor("#5e5e5e"));
                    mNew.setBackgroundResource(R.drawable.square_selected_title);
                    mHot.setBackground(null);
                    break;
                case 1:
                    mHot.setTextColor(getResources().getColor(R.color.white));
                    mNew.setTextColor(Color.parseColor("#5e5e5e"));
                    mHot.setBackgroundResource(R.drawable.square_selected_title);
                    mNew.setBackground(null);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
