package com.li.videoapplication.ui.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 单个item 分割线
 */

public class SpanSingleDecoration extends SpanItemDecoration {
    private int mPosition;
    public SpanSingleDecoration(int margin, boolean isLeft, boolean isRight, boolean isTop, boolean isBottom,int position) {
        super(margin, isLeft, isRight, isTop, isBottom);
        mPosition = position;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
       int position =  parent.getChildAdapterPosition(view);
        if (mPosition == position){
            super.getItemOffsets(outRect, view, parent, state);
        }

    }
}
