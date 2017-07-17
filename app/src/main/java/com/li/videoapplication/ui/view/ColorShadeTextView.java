package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 字体颜色渐变 TextView
 */

public class ColorShadeTextView extends AppCompatTextView {
    private int mEntryColor = Color.parseColor("#000000");
    private int mExitColor = Color.parseColor("#B6B6B6");

    public float mScale;

    private int[] mEntryColors = {mEntryColor,mExitColor};

    private int[] mExitColors = {mExitColor,mEntryColor};

    public boolean isEntry;

    private   LinearGradient mGradient;
    public ColorShadeTextView(Context context) {
        super(context);
        init();
    }

    public ColorShadeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorShadeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (isEntry){
            mGradient = new LinearGradient(0,0,0,getMeasuredHeight()*mScale,mEntryColors,null,LinearGradient.TileMode.CLAMP);
        }else {
            mGradient = new LinearGradient(0,0,0,getMeasuredHeight()*mScale,mExitColors,null,LinearGradient.TileMode.CLAMP);
        }

        getPaint().setShader(mGradient);
        super.onDraw(canvas);
    }

    public boolean isEntry() {
        return isEntry;
    }

    public void setEntry(boolean entry) {
        isEntry = entry;
    }

    /**
     * @param scale
     */
    public void refreshScale(float scale){
        mScale = scale;
        invalidate();
    }
}
