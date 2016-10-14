package com.li.videoapplication.ui.pageradapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 适配器：圈子详情
 */
public class ImageDetailPagerAdapter extends PagerAdapter {

	private List<View> views;

	public ImageDetailPagerAdapter(List<View> views) {
		super();
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = views.get(position);
		int w = LinearLayout.LayoutParams.MATCH_PARENT;
		int h = view.getHeight();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);//这里设置params的高度。
		container.addView(view, 0, params);//使用这个params
		return view;
	}
}