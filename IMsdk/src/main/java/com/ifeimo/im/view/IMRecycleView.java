package com.ifeimo.im.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ifeimo.im.common.adapter.BaseChatReCursorAdapter;
import com.ifeimo.im.common.util.ScreenUtil;
import com.ifeimo.im.common.util.WindowUtil;
import com.ifeimo.im.framwork.IMSdk;

/**
 * Created by lpds on 2017/2/18.
 */
public class IMRecycleView extends RecyclerView {

    private final static String TAG = "XMPP_IMRecyclerView";

    /**
     * 是否可以滑动到底部
     */
    private boolean isShowDown = true;
    /**
     * 是否移动
     */
    private boolean isMove = true;

    private boolean isUp = false;

    /**
     * 是否可以加载更多
     */
    private boolean isShowHadMore = true;
    /**
     * item 管理者
     */
    private LinearLayoutManager lm;
    /**
     * 加载更多回调
     */
    private OnItemShowListener onItemShowListener;
    /**
     * 按下的 y
     */
    private float downY;
    /**
     * 加载更多 runnable
     */
    private RefreshRunnable refreshRunnable = new RefreshRunnable();

    private int maxheight = ScreenUtil.getScreenHeight(IMSdk.CONTEXT);
    private int maxwidth = ScreenUtil.getScreenWidth(IMSdk.CONTEXT);
    private Rect rect = new Rect(0,0,maxwidth, maxheight);
    private View v;

    private Handler handler = null;

    private class RefreshRunnable implements Runnable {
        final int TIME = 600;
//        int findFirstVisibleItemPosition;
//        boolean isShowHadMore;
//
//        public void setShowHadMore(boolean showHadMore) {
//            this.isShowHadMore = showHadMore;
//        }
//
//        public void setFindFirstVisibleItemPosition(int findFirstVisibleItemPosition) {
//            synchronized (this) {
//                this.findFirstVisibleItemPosition = findFirstVisibleItemPosition;
//            }
//        }

        @Override
        public void run() {

//            if (this.isShowHadMore || this.findFirstVisibleItemPosition == 0) {
            onItemShowListener.onFirstCompletelyVisibleItemPosition0();
//            } else {
//                getAdapter().setRefreshNow(false);
//            }
        }
    }

    /**
     * 可以刷新到底部 runnable
     */
    private Runnable runnableShowDown = new Runnable() {
        @Override
        public void run() {
            isShowDown = true;
        }
    };
    private Runnable timeRefreshRunabele = new Runnable() {
        @Override
        public void run() {
            getAdapter().setRefreshNow(false);
        }
    };

    public IMRecycleView(Context context) {
        super(context);
    }

    public IMRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IMRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(RecyclerView.Adapter adapter,View v) {
        this.v = v;
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                handler = new Handler();
                Looper.loop();
            }
        }.start();
        setLayoutManager((lm = new LinearLayoutManager(getContext())));
        setItemAnimator(new DefaultItemAnimator());
        setAdapter(adapter);
    }

    @Override
    public BaseChatReCursorAdapter getAdapter() {
        return (BaseChatReCursorAdapter) super.getAdapter();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        /**
         * 滑动过程中禁止，显示最后一条数据
         */
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();//按下
                isMove = false;//默认不移动
                isUp = false;
                isShowHadMore = false;//默认不需要加载
                removeCallbacks(runnableShowDown);
                break;
            case MotionEvent.ACTION_MOVE:
                isShowDown = false;//移动后则不允许 notifyDataChange 滚动到底部
                isMove = true;//移动中
                WindowUtil.setHideSoft((Activity) getContext());//隐藏键盘

//                Log.e("1333", "lm.CompleteFirst() =  " + lm.findFirstCompletelyVisibleItemPosition());
//                Log.e("1333", "lm.First() =  " + lm.findFirstVisibleItemPosition());
//                Log.e("1333", "lm.CompleteLast() =  " + lm.findLastCompletelyVisibleItemPosition());
//                Log.e("1333", "lm.Last() =  " + lm.findLastVisibleItemPosition());
//                move(ev);
                break;
            case MotionEvent.ACTION_UP:
                isUp = true;
                if (!isMove) {
                    WindowUtil.setHideSoft((Activity) getContext());//隐藏键盘
                }
                isMove = false;
                up(ev);
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Deprecated
    private void move(MotionEvent ev) {
        if (ev.getY() - downY > 0) {//如果下拉
            if (lm.findFirstVisibleItemPosition() == 0) {//如果可看见菊花
                isShowHadMore = true;//可以加载更多了
            }
        }
    }

    /**
     * 松开
     *
     * @param ev
     */
    private void up(MotionEvent ev) {
        handler.postDelayed(runnableShowDown, 5000);//延迟5s之后就可以允许 notifyDataChange 滚动到底部
        //如果不再刷新中，适配器计算结果可以刷新，回调不为空的情况下
//        if (!getAdapter().isRefreshNow() && getAdapter().isHadMore() && onItemShowListener != null) {
//            getAdapter().setRefreshNow(true);//正在刷新中
//            refreshRunnable.setShowHadMore(isShowHadMore);
//            handler.postDelayed(refreshRunnable, refreshRunnable.TIME);//回调刷新
//        }

    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (getAdapter().isHadMore()) {
            if (!isUp && isMove && !getAdapter().isRefreshNow() && lm.findFirstVisibleItemPosition() == 0) {
                isShowHadMore = true;
            } else if (isUp && (isShowHadMore || lm.findFirstVisibleItemPosition() == 0)
                    && !getAdapter().isRefreshNow() && onItemShowListener != null) {
                Log.i("--13","getAdapter().isRefreshNow() = "+getAdapter().isRefreshNow());
                getAdapter().setRefreshNow(true);//正在刷新中
                handler.postDelayed(refreshRunnable, refreshRunnable.TIME);//回调刷新
                handler.postDelayed(timeRefreshRunabele,3000);
            }
        }

    }




    /**
     * 滚动指定位置，受 isShowDown 控制
     *
     * @param position
     */
    @Override
    public void scrollToPosition(int position) {
        if (!isShowDown) {
            return;
        }
        super.scrollToPosition(position);
    }

    /**
     * 自由的滚动，不受 isShowDown 属性影响
     *
     * @param position
     */
    public void freedomScrollToPosition(int position) {
        super.scrollToPosition(position);
    }

    /**
     * 无关 isShowDown 参数的滚动
     */
    public void scrollToPosition() {
        if (getAdapter() != null) {
            super.scrollToPosition(getAdapter().getItemCount() - 1);
        }
        isShowDown = true;
    }

    /**
     * 设置刷新回调
     *
     * @param onItemShowListener
     */
    public void setOnItemShowListener(OnItemShowListener onItemShowListener) {
        this.onItemShowListener = null;
        this.onItemShowListener = onItemShowListener;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return lm;
    }


    /**
     * 离开activity,或者被remove
     */
    @Override
    protected void onDetachedFromWindow() {
        Log.i(TAG, "------ 退出聊天列表 RecyclerView -------");
        handler.removeCallbacks(runnableShowDown);
        handler.removeCallbacks(refreshRunnable);
        handler.removeCallbacks(timeRefreshRunabele);
        handler.getLooper().quit();
        super.onDetachedFromWindow();
    }
}
