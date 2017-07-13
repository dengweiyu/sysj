package com.li.videoapplication.mvp.match.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.activity.PrivacyActivity;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.views.ViewPagerY1;

import java.util.ArrayList;
import java.util.List;

/**
 * 陪玩订单：接单、下单
 */

public class PlayWithOrderListFragment extends TBaseFragment {
    private ViewPagerY1 mPager;

    private ViewPagerAdapter mAdapter;

    private List<Fragment> mFragments;
    @Override
    protected int getCreateView() {
        return R.layout.fragment_play_with_order;
    }

    @Override
    protected void initContentView(View view) {
        mPager = (ViewPagerY1)view.findViewById(R.id.vp_play_with_order);
        mFragments = new ArrayList<>();
        mFragments.add(new PlayWithPlaceOrderListFragment());
        mFragments.add(new PlayWithTakeOrderListFragment());
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tl_play_with_order);
        mAdapter = new ViewPagerAdapter(getFragmentManager(),mFragments,new String[]{"下单","接单"});
        mPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mPager);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }
}
