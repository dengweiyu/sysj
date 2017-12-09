package com.li.videoapplication.views;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * 禁止SeekBar的拖动事件
 */
public class TouchSeekBar extends AppCompatSeekBar {

    public TouchSeekBar(Context context) {
        super(context);
    }

    public TouchSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 是否能拖动
     *      true:能拖动
     */
    private boolean drag = false;

    public boolean isDrag() {
        return drag;
    }

    public void setDrag(boolean drag) {
        this.drag = drag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (drag)
            return super.onTouchEvent(event);
        else
            return false;
    }
}
