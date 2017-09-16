package com.li.videoapplication.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * 左右滑动加弹性的HorizontalScrollView
 * http://blog.csdn.net/andylao62/article/details/41213015
 * 
 * @author z
 *
 */
@SuppressLint("ClickableViewAccessibility")
public class ElasticHorizontalListView extends HorizontalListView {

	// 初始可拉动Y轴方向距离
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 100;
	// 上下文环境
	private Context mContext;
	// 实际可上下拉动Y轴上的距离
	private int mMaxYOverscrollDistance;

	public ElasticHorizontalListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initBounceListView();
	}

	private void initBounceListView() {
		final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		final float density = metrics.density;
		mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// 实现的本质就是在这里动态改变了maxOverScrollY的值
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, mMaxYOverscrollDistance, maxOverScrollY, isTouchEvent);
	}
}
