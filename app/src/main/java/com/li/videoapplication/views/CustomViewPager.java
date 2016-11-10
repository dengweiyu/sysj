package com.li.videoapplication.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
	private boolean isCanScroll = true;

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 是否允许滑动
	 * 
	 * @param isCanScroll
	 */
	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.isCanScroll) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public void scrollTo(int x, int y) {
		if (isCanScroll) {
			super.scrollTo(x, y);
		}
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
	}
}