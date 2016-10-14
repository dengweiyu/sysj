package com.li.videoapplication.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决ViewPager禁止滑动的问题
 * 有效果
 */
public class ViewPagerY4 extends ViewPager {

    private boolean isScrollable = true;

    private boolean isTouchable = true;

    public ViewPagerY4(Context context) {
        super(context);
    }

    public ViewPagerY4(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrollable == false) {
            return false;
        } else {
            if (isTouchable) {// 放行触摸事件， 父视图放行
                getParent().requestDisallowInterceptTouchEvent(true);
                return super.onInterceptTouchEvent(ev);
            } else {// 拦截触摸事件， 父视图拦截
                clearFocus();
                getParent().requestDisallowInterceptTouchEvent(false);
                return true;
            }
        }
    }

    public boolean isScrollable() {
        return isScrollable;
    }

    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    public boolean isTouchable() {
        return isTouchable;
    }

    public void setTouchable(boolean isTouchable) {
        this.isTouchable = isTouchable;
    }
}
