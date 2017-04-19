package com.li.videoapplication.ui.pageradapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 通用viewpager适配器
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private String[] tabTitle;
    private FragmentManager fm;
    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] tabTitle) {
        super(fm);
        this.fragments = fragments;
        this.tabTitle = tabTitle;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return  fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null? 0:fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
