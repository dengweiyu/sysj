package com.li.videoapplication.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;
/**
 * 嵌套ViewPager解决滑动冲突
 * android Viewpager 不能动态的设置高度，一直使用第一个fragment页面的高度
 * http://www.itmmd.com/201411/191.html
 * @author z
 *
 */
public class ScrollViewY12 extends ScrollView {

	private GestureDetector mGestureDetector;

	public ScrollViewY12(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
		setFadingEdgeLength(0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
	}

	// Return false if we're scrolling in the x direction
	private class YScrollDetector extends SimpleOnGestureListener {
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return (Math.abs(distanceY) > Math.abs(distanceX));
		}
	}
}
