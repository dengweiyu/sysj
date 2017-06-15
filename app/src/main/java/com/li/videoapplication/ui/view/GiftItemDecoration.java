package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/6/9.
 */

public class GiftItemDecoration extends SimpleItemDecoration {
    private boolean mIsLand;
    public GiftItemDecoration(Context context,boolean isLand) {
        super(context);
        this.mIsLand = isLand;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            int width = 1;

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
