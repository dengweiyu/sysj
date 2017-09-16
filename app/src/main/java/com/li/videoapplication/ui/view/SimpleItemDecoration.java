package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * recycler view Item分割线
 */

public class SimpleItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private Paint mPaint;

    protected boolean mIsLeft;
    protected boolean mIsRight;
    protected boolean mIsTop;
    protected boolean mIsBottom;
    public SimpleItemDecoration(Context context,boolean isLeft,boolean isRight,boolean isTop,boolean isBottom) {
        super();
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#e2e2e2"));       //gray

        this.mIsLeft = isLeft;
        this.mIsRight = isRight;
        this.mIsTop = isTop;
        this.mIsBottom = isBottom;
    }

    public SimpleItemDecoration(Context context){
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#e2e2e2"));       //gray
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            int width = 1;
            if (mIsTop){
                drawTop(c,parent.getChildAt(i),parent,width);
            }
            if (mIsBottom){
                drawBottom(c,parent.getChildAt(i),parent,width);
            }
            if (mIsLeft){
                drawLeft(c,parent.getChildAt(i),parent,width);
            }
            if (mIsRight){
                drawRight(c,parent.getChildAt(i),parent,width);
            }
        }

    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect,view,parent,state);

        int top = mIsTop ? 1 : 0;
        int bottom = mIsBottom ? 1 : 0;
        int left = mIsLeft ? 1 : 0;
        int right = mIsRight ? 1 : 0;
        outRect.set(left,top,right,bottom);           //每个方向都需要画分割线
    }

    //bottom
    protected void drawBottom(Canvas c,View child, RecyclerView parent,int diverWidth){
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int left = child.getLeft()- params.leftMargin - diverWidth;
        int right = child.getRight()+ params.rightMargin + diverWidth;
        int top = child.getBottom() + params.bottomMargin;
        int bottom = top + diverWidth;

        c.drawRect(left,top,right,bottom,mPaint);
    }

    //top
    protected void drawTop(Canvas c,View child, RecyclerView parent,int diverWidth){
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int left = child.getLeft()- params.leftMargin - diverWidth;
        int right = child.getRight()+ params.rightMargin + diverWidth;
        int bottom = child.getTop() - params.topMargin;
        int top = bottom - diverWidth;
        c.drawRect(left,top,right,bottom,mPaint);
    }

    //left
    protected void drawLeft(Canvas c,View child, RecyclerView parent,int diverWidth){
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int top = child.getTop() - params.topMargin - diverWidth;
        int bottom = child.getBottom() + params.bottomMargin + diverWidth;
        int right = child.getLeft() - params.leftMargin;
        int left = right - diverWidth;
        c.drawRect(left,top,right,bottom,mPaint);
    }

    //right
    protected void drawRight(Canvas c,View child, RecyclerView parent,int diverWidth){
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int top = child.getTop() - params.topMargin - diverWidth;
        int bottom = child.getBottom() + params.bottomMargin + diverWidth;
        int left = child.getRight() + params.rightMargin;
        int right = left + diverWidth;

        c.drawRect(left,top,right,bottom,mPaint);
    }


}
