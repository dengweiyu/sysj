package com.li.videoapplication.ui.view;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Recycler view item span
 */

public class SpanItemDecoration extends  RecyclerView.ItemDecoration {

    private boolean mIsLeft;
    private boolean mIsRight;
    private boolean mIsTop;
    private boolean mIsBottom;

    private int mMargin;
    public SpanItemDecoration(int margin,boolean isLeft, boolean isRight, boolean isTop, boolean isBottom) {
        super();
        this.mIsLeft = isLeft;
        this.mIsRight = isRight;
        this.mIsTop = isTop;
        this.mIsBottom = isBottom;
        this.mMargin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mIsLeft){
            outRect.left = mMargin;
        }
        if (mIsRight){
            outRect.right = mMargin;
        }
        if (mIsTop){
            outRect.top = mMargin;
        }
        if (mIsBottom){
            outRect.bottom = mMargin;
        }
    }
}
