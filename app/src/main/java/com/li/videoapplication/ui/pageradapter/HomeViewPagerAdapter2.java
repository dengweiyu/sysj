package com.li.videoapplication.ui.pageradapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.li.videoapplication.mvp.home.view.HomeLazyColumnFragment2;
import com.li.videoapplication.mvp.home.view.HomeLazyColumnFragment3;

import java.util.List;

/**
 * 通用viewpager适配器
 */
public class HomeViewPagerAdapter2 extends FragmentStatePagerAdapter {

    private List<HomeLazyColumnFragment2> fragments;
    private String[] tabTitle;
    private FragmentManager fm;
    public HomeViewPagerAdapter2(FragmentManager fm, List<HomeLazyColumnFragment2> fragments, String[] tabTitle) {
        super(fm);
        this.fragments = fragments;
        this.tabTitle = tabTitle;
        this.fm = fm;
    }

    @Override
    public HomeLazyColumnFragment2 getItem(int position) {
        return  fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null? 0:fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
