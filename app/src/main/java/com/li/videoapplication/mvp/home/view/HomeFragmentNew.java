package com.li.videoapplication.mvp.home.view;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.views.ViewPagerY4;

import java.util.ArrayList;
import java.util.List;

/**
 *首页
 */

public class HomeFragmentNew extends TBaseFragment {

    private ViewPagerY4 mViewPager;

    private List<Fragment> mFragments;
    @Override
    protected int getCreateView() {
        return R.layout.fragment_home_new;
    }

    @Override
    protected void initContentView(View view) {
        if (mFragments != null){
            return;
        }
        mFragments = new ArrayList<>();


        //FIXME 需要处理滑动事件冲突的问题
        mViewPager = (ViewPagerY4)view.findViewById(R.id.vp_home);

        //FIXME id 应该从后台获取
        //推荐
        mFragments.add(HomeColumnFragment.newInstance("1"));

        //其他
        mFragments.add(HomeColumnFragment.newInstance("6"));

        mViewPager.setAdapter(new ViewPagerAdapter(getFragmentManager(),mFragments,new String []{}));
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }
}
