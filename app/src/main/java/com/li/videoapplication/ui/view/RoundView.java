package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 实心圆
 */

public class RoundView extends View {

    private Paint mPaint;
    private int mDefaultColor;
    private float mRadius;

    public RoundView(Context context) {
        super(context);
        init();
    }

    public RoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mDefaultColor = Color.parseColor("#b8b8b8");
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mDefaultColor);

        measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);

        mRadius = (getMeasuredHeight()>getMeasuredWidth()?getMeasuredWidth():getMeasuredHeight())/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(new RectF(0,0,mRadius*2,mRadius*2),mRadius,mRadius,mPaint);
    }

    public void setDefaultColor(int color){
        mPaint.setColor(color);
        invalidate();
    }
}
