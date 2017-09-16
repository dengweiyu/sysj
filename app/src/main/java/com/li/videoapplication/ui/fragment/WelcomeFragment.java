package com.li.videoapplication.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.animation.ViewPagerZoomOutTransformer;
import com.li.videoapplication.data.model.event.WelcomeScrollEvent;
import com.li.videoapplication.framework.TBaseChildFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.views.ViewPagerY4;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class WelcomeFragment extends TBaseChildFragment {
    private ViewPagerY4 mViewPager;
    private List<Fragment> mFragments;
    @Override
    protected int getCreateView() {
        return R.layout.fragment_welcome;
    }

    @Override
    protected void initContentView(View view) {
        mViewPager = (ViewPagerY4) view.findViewById(R.id.viewpager);
        mFragments = new ArrayList<>();
        mFragments.add(WelcomeImageFragment.newInstance(R.drawable.welcome_bg_four));
        mFragments.add(new WelcomeChoiceGameFragment());
        mFragments.add(new WelcomeChoiceLoginFragment());
        mViewPager.setPageTransformer(true, new ViewPagerZoomOutTransformer());
        mViewPager.setAdapter(new ViewPagerAdapter(getFragmentManager(),mFragments,new String[]{}));
        mViewPager.setOffscreenPageLimit(4);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    public void onEventMainThread(WelcomeScrollEvent event){
        int position = event.getPosition();
        if (mViewPager != null){
            mViewPager.setCurrentItem(position);
        }
    }

    public boolean canGoBack(){
        if (mViewPager == null ){
            return true;
        }
        int position = mViewPager.getCurrentItem();
        if (position == 0){
            return true;
        }

        mViewPager.setCurrentItem(--position);
        return false;
    }

}
