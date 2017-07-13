package com.li.videoapplication.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AbsListView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.match.view.GameMatchFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.views.ViewPagerY1;
import com.li.videoapplication.views.ViewPagerY4;

import java.util.ArrayList;
import java.util.List;

/**
 * 陪玩
 */

public class PlayWithFragment extends TBaseFragment {

    private ViewPagerY1 mPager;

    private List<Fragment> mFragments;

    private ViewPager.OnPageChangeListener mListener;
    @Override
    protected int getCreateView() {
        return R.layout.fragment_play_with;
    }

    @Override
    protected void initContentView(View view) {
        mPager = (ViewPagerY1)view.findViewById(R.id.vp_play_with);
        mFragments = new ArrayList<>();
        mFragments.add(new CoachListFragment());
        mFragments.add(new GameMatchFragment());

        mPager.setAdapter(new ViewPagerAdapter(getFragmentManager(),mFragments,new String[]{}));
        mPager.addOnPageChangeListener(mListener);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    /**
     *
     */
    public void setOnScrollListener(ViewPager.OnPageChangeListener listener){
        this.mListener = listener;
    }

    /**
     *
     */
    public void setCurrentPage(int position){
        if (mPager != null){
            mPager.setCurrentItem(position);
        }
    }

    /**
     *
     */
    public int getCurrentPage(){
        return mPager != null?mPager.getCurrentItem():0;
    }
}
