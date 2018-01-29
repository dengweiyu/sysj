package com.li.videoapplication.ui.popupwindows.gameselect;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by linhui on 2017/9/18.
 */
final class MyPageAdapter extends PagerAdapter {

    List<IPageView> pageViews;

    public MyPageAdapter(List<IPageView> pageViews) {
        this.pageViews = pageViews;
    }

    @Override
    public int getCount() {
        return pageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pageViews.get(position).getView());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(pageViews.get(position).getView());
        return pageViews.get(position);
    }
}
