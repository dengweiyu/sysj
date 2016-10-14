package com.li.videoapplication.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/**
 * 功能：解决ListView嵌套不能全部显示的问题，继承该类ListView
 * @author Administrator
 *
 */
public class ListViewY1 extends ListView {

	public ListViewY1(Context context) {
		super(context);
	}

	public ListViewY1(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewY1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}