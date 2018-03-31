package com.li.videoapplication.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.li.videoapplication.ui.fragment.GameLazyColumnFragment;

import java.util.List;

/**
 * Created by y on 2018/3/21.
 */

public class GameTypeViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<GameLazyColumnFragment> fragments;
    private String[] tabTitle;
    private FragmentManager fm;

    public GameTypeViewPagerAdapter(FragmentManager fm, List<GameLazyColumnFragment> fragments, String[] tabTitle) {
        super(fm);
        this.fragments = fragments;
        this.tabTitle = tabTitle;
        this.fm = fm;
    }

    @Override
    public GameLazyColumnFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
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
