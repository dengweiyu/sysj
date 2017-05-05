package com.ifeimo.im.framwork.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;;import com.ifeimo.im.R;
import com.ifeimo.im.common.util.ScreenUtil;

/**
 * Created by lpds on 2017/4/26.
 */
public class SlideView extends HorizontalScrollView {

    public static final int NORMAL = 12;
    public static final int BAN = 123;

    private int mode = NORMAL;

    private static final String TAG = "SlideView";
    View centerView;
    View delet_view;
    int deletViewWidth;
    private boolean isUp;
    private boolean isShow;
    public SlideView(Context context) {
        super(context);
        init();
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        centerView = LayoutInflater.from(getContext()).inflate(R.layout.item_contract,null);

        centerView.findViewById(R.id.muc_left_layout).getLayoutParams().width = ScreenUtil.getScreenWidth(getContext());


        delet_view = centerView.findViewById(R.id.delet_view);
        addView(centerView);
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        delet_view.post(new Runnable() {
            @Override
            public void run() {
                deletViewWidth = delet_view.getMeasuredWidth();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (mode){
            case BAN:
                return false;
            case NORMAL:
                break;
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                isShow = true;
                Log.i(TAG, "onTouchEvent: ACTION_DOWN");
               break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent: ACTION_UP isShow = "+isShow);
                if(!isShow){
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    },100);
                }else{
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fullScroll(HorizontalScrollView.FOCUS_LEFT);
                        }
                    },100);
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    public void scrollMode(int mode){
        this.mode=  mode;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        Rect rect = new Rect();
        isShow = true;
        if(delet_view.getLocalVisibleRect(rect)){
            if(rect.right > 0 && rect.right > deletViewWidth/2){
                isShow = false;
            }
        }
        Log.i(TAG, "isShow = "+isShow+"  rect = "+rect);
        super.onScrollChanged(x, y, oldX, oldY);
    }
}
