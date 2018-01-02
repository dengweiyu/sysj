package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.li.videoapplication.utils.ScreenUtil;

/**
 * Created by cx on 2017/12/14.
 */

public class VideoItemDecoration extends SimpleItemDecoration {

    private int mWidth;
    public VideoItemDecoration(Context context) {
        super(context);
        setColor(ContextCompat.getColor(context, android.R.color.white));
        mIsLeft = true;
        mIsRight = true;
        mIsTop = true;
        mIsBottom = true;
        mWidth = ScreenUtil.dp2px(context, 16);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i%2 == 0) {
                drawRight(c, parent.getChildAt(i), parent, mWidth);
            } else {
                drawLeft(c, parent.getChildAt(i), parent, mWidth);
            }
        }
    }
}
