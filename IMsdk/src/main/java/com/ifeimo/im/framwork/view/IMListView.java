package com.ifeimo.im.framwork.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by lpds on 2017/2/16.
 */
@Deprecated
public class IMListView extends ListView {

    boolean isShowDown = true;

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isShowDown = true;
        }
    };

    public IMListView(Context context) {
        super(context);
    }

    public IMListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IMListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSelection(int position) {
        if(isShowDown ) {
            super.setSelection(position);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacks(runnable);
                break;
            case MotionEvent.ACTION_MOVE:
                isShowDown = false;
                break;
            case MotionEvent.ACTION_UP:
                handler.postDelayed(runnable,1500);
                break;

        }

        return super.onTouchEvent(ev);
    }
}
