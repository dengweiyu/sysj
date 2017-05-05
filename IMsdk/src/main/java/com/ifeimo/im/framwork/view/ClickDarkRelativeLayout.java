package com.ifeimo.im.framwork.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.ifeimo.im.R;


/**
 * Created by y on 2016/8/2.
 */
public class ClickDarkRelativeLayout extends RelativeLayout implements GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector;

    public ClickDarkRelativeLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        gestureDetector = new GestureDetector(context, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //在cancel里将滤镜取消，注意不要捕获cacncel事件,mGestureDetector里有对cancel的捕获操作
        //在滑动GridView时，AbsListView会拦截掉Move和UP事件，直接给子控件返回Cancel
        if(event.getActionMasked() == MotionEvent.ACTION_UP){
            removeFilter();
        }
        return gestureDetector.onTouchEvent(event);
    }

    private void setFilter(){
        setBackgroundColor(Color.BLACK);
    }

    private void removeFilter(){
        setBackgroundColor(getResources().getColor(R.color.content_background));
    }

    @Override
    public boolean onDown(MotionEvent e) {
        setFilter();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        removeFilter();
        performClick();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        performLongClick();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
