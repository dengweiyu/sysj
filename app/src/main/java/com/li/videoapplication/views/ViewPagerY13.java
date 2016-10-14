package com.li.videoapplication.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

/**
 * 动态设置ViewPager高度
 * 自动适应高度的ViewPager
 * http://zhidao.baidu.com/link?url=9dVBok9bvIv253IN2m6EFsBWzGVWxr5gNpe3LG_ZPtbHeUHpz0gO5t4uvnJsrMtygjeDg2NVqCnA3yjJ8vgs1Bjsu1IWw-dT9SxaYAdr46_
 * @author 
 *
 */
public class ViewPagerY13 extends ViewPager {
 
    public ViewPagerY13(Context context) {
        super(context);
    }
 
    public ViewPagerY13(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }
 
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
 
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
