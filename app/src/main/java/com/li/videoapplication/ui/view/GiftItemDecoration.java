package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

/**
 * 礼物RecyclerView分割线
 */

public class GiftItemDecoration extends SimpleItemDecoration {
    private boolean mIsLand;
    public GiftItemDecoration(Context context,boolean isLand) {
        super(context);
        this.mIsLand = isLand;

        mIsLeft = true;
        mIsBottom = true;
        mIsRight = true;
        mIsTop = true;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            int width = 20;

            if (i < 4){
                drawTop(c,parent.getChildAt(i),parent,width);
                drawBottom(c,parent.getChildAt(i),parent,width);
                drawRight(c,parent.getChildAt(i),parent,width);
            }else {
                drawRight(c,parent.getChildAt(i),parent,width);
            }

            if (mIsLand){
                drawTop(c,parent.getChildAt(i),parent,width);
            }
        }
    }

}
