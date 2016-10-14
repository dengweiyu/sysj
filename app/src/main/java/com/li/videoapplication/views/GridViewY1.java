package com.li.videoapplication.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * 功能：解决GridView嵌套不能全部显示的问题，继承该类ListView
 * @author Administrator
 *
 */
public class GridViewY1 extends GridView {

	public GridViewY1(Context context) {
		super(context);
	}

	public GridViewY1(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridViewY1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
