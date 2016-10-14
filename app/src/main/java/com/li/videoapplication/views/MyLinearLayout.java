package com.li.videoapplication.views;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private PointF downPoint = new PointF();

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (getChildCount() == 0)
			return false;

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			// 记录按下时候的坐标
			downPoint.x = ev.getX();
			downPoint.y = ev.getY();
			if (this.getChildCount() > 1) { // 有内容，多于1个时
				// 通知其父控件，现在进行的是本控件的操作，不允许拦截
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (this.getChildCount() > 1) { // 有内容，多于1个时
				// 通知其父控件，现在进行的是本控件的操作，不允许拦截
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_UP:
			// 在up时判断是否按下和松手的坐标为一个点
			if (PointF.length(ev.getX() - downPoint.x, ev.getY() - downPoint.y) < 5) {
				return false;
			}
			// 不同点拦截事件
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
