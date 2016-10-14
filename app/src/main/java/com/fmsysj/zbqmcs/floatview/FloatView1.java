package com.fmsysj.zbqmcs.floatview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fmsysj.zbqmcs.activity.PackageInfoGridview;
import com.fmsysj.zbqmcs.activity.ScreenRecordActivity;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 视图：浮窗1
 */
public class FloatView1 extends LinearLayout {

    public int preXFirst;
    public int preYFirst;
    public boolean showFloatView = true;
    private RelativeLayout floatViewRL;
    public int mWidth;
    public int mHeight;

    private ImageView tvInfo;
    private int preX;
    private int preY;
    private int x;
    private int y;
    private boolean isMove;

    private Context mContext;
    private LayoutInflater inflater;
    private TimerTask task;
    private Timer timer = new Timer(); // 定时器

    /**
     * 调置页面上控制是否开启浮窗
     */
    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (mContext != null) {
                if (msg.what == 3) {
                    msg.what = 0;
                    HideFloatView();
                } else if (msg.what == 4) {
                    msg.what = 0;
                    showFloatView();
                }
            } else {
                if (msg.what == 3) {
                    msg.what = 0;
                    showFloatView = false;
                } else if (msg.what == 4) {
                    msg.what = 0;
                    showFloatView = true;
                }
            }
        }
    };

    public FloatView1(Context context) {
        this(context, null);
    }

    public FloatView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflater = LayoutInflater.from(context);

        inflater.inflate(R.layout.fm_float_view_1, this);
        tvInfo = (ImageView) findViewById(R.id.background1);
        floatViewRL = (RelativeLayout) findViewById(R.id.float_view_RL);

        mWidth = tvInfo.getLayoutParams().width;
        mHeight = tvInfo.getLayoutParams().height;

        handler.sendEmptyMessage(0);

        // 定时器任务
        task = new TimerTask() {
            @Override
            public void run() {
                if (ScreenRecordActivity.isInMain || PackageInfoGridview.isInPackageInfo) {
                    handler.sendEmptyMessage(3);
                } else {
                    handler.sendEmptyMessage(4);
                }
            }
        };

        timer.schedule(task, 1000, 1000); // 启动定时器
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        FloatViewManager.getInstance().move(FloatView1.this, width, height / 2);

    }

    /**
     * 隐藏悬浮窗
     *
     * @return
     */
    public boolean HideFloatView() {
        floatViewRL.setVisibility(RelativeLayout.GONE);
        return true;
    }

    /**
     * 显示悬浮窗
     *
     * @return
     */
    public boolean showFloatView() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        // 是否开启无浮窗模式
        if (sp.getBoolean("no_float_view_record", false)) {
            floatViewRL.setVisibility(RelativeLayout.GONE);
        } else {
            floatViewRL.setVisibility(RelativeLayout.VISIBLE);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                tvInfo.setImageDrawable(getResources().getDrawable(R.drawable.fm_float_ico_down));
                preX = (int) event.getRawX();
                preY = (int) event.getRawY();
                preXFirst = preX;
                preYFirst = preY;
                isMove = false;
                break;
            }
            case MotionEvent.ACTION_UP: {
                // 停止移动时，将控件当前坐标重新赋值给全局变量
                int[] location = new int[2];
                tvInfo.getLocationOnScreen(location);
                ExApplication.moveX = location[0];
                ExApplication.moveY = location[1];
                ExApplication.tvWidth = tvInfo.getWidth();
                if (!isMove) {
                    FloatViewManager.getInstance().showContent(FloatView1.this);
                    FloatContentView.isFirstTimeUse = false;
                    FloatContentView.floatContentAnimation.extandAnmin();
                    // 将isFirstTimeUse置为false，悬浮窗直接跳转到floatcontentview，而不是floatview1；
                    // floatViewAnimation.extandAnmin();
                    // FloatViewService.h.sendEmptyMessage(1);
                }
                // 重新设置图片
                tvInfo.setImageDrawable(getResources().getDrawable(R.drawable.fm_float_ico));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                // 消除手指点击误差
                if (((x - preXFirst) > -15 && (x - preXFirst) < 15) && ((y - preYFirst) > -15 && (y - preYFirst) < 15)) {
                    isMove = false;
                } else {
                    FloatViewManager.getInstance().move(FloatView1.this, x - preX, y - preY);
                    isMove = true;
                }
                preX = x;
                preY = y;
                break;
            }
        }
        return super.onTouchEvent(event);
    }
}