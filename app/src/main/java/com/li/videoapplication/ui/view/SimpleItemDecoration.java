package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.li.videoapplication.R;
import com.li.videoapplication.utils.ScreenUtil;

import de.measite.minidns.record.A;

/**
 * 礼物页面Item分割线
 */

public class GiftItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private Paint mPaint;

    private int mSelect;
    public GiftItemDecoration(Context context) {
        super();
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#f3f3f3"));       //gray
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            int width = ScreenUtil.dp2px(mContext,1);
            drawTop(c,parent.getChildAt(i),parent,width);
            drawBottom(c,parent.getChildAt(i),parent,width);
            drawLeft(c,parent.getChildAt(i),parent,width);
           // drawRight(c,parent.getChildAt(i),parent,width);
        }

    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect,view,parent,state);
        outRect.set(1,1,1,1);           //每个方向都需要画分割线
    }

    //bottom
    private void drawBottom(Canvas c,View child, RecyclerView parent,int diverWidth){
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int left = child.getLeft()- params.leftMargin - diverWidth;
        int right = child.getRight()+ params.rightMargin + diverWidth;
        int top = child.getBottom() + params.bottomMargin;
        int bottom = top + diverWidth;

        c.drawRect(left,top,right,bottom,mPaint);
    }

    //top
    private void drawTop(Canvas c,View child, RecyclerView parent,int diverWidth){
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int left = child.getLeft()- params.leftMargin - diverWidth;
        int right = child.getRight()+ params.rightMargin + diverWidth;
        int bottom = child.getTop() - params.topMargin;
        int top = bottom - diverWidth;
        c.drawRect(left,top,right,bottom,mPaint);
    }

    //left
    private void drawLeft(Canvas c,View child, RecyclerView parent,int diverWidth){
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int top = child.getTop() - params.topMargin - diverWidth;
        int bottom = child.getBottom() + params.bottomMargin + diverWidth;
        int right = child.getLeft() - params.leftMargin;
        int left = right - diverWidth;
        c.drawRect(left,top,right,bottom,mPaint);
    }

    //right
    private void drawRight(Canvas c,View child, RecyclerView parent,int diverWidth){
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int top = child.getTop() - params.topMargin - diverWidth;
        int bottom = child.getBottom() + params.bottomMargin + diverWidth;
        int left = child.getRight() + params.rightMargin;
        int right = left + diverWidth;

        c.drawRect(left,top,right,bottom,mPaint);
    }

    public int getSelect() {
        return mSelect;
    }

    public void setSelect(int select) {
        mSelect = select;
    }
}
