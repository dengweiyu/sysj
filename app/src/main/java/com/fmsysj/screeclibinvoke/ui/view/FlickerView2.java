package com.fmsysj.screeclibinvoke.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.ifeimo.screenrecordlib.RecordingManager;

/**
 * 视图：闪烁圆点
 */
public class FlickerView2 extends ImageView {

    public static final String TAG = FlickerView2.class.getSimpleName();

    public FlickerView2(Context context) {
        super(context);
        init();
    }

    public FlickerView2(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public FlickerView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        handler = new Handler(Looper.myLooper());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (RecordingManager.getInstance().isRecording()) {
            if (RecordingManager.getInstance().isPausing()) {
                showView();
            } else {
                flickView();
            }
        } else {
            hideView();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        stopAnimation();
    }

    /**
     * 显示但不闪烁
     */
    public void showView() {
        Log.d(TAG, "showView: // -------------------------------------------");
        stopAnimation();
        setVisibility(VISIBLE);
    }

    /**
     * 隐藏
     */
    public void hideView() {
        Log.d(TAG, "hideView: // -------------------------------------------");
        stopAnimation();
        setVisibility(GONE);
    }

    /**
     * 显示并闪烁
     */
    public void flickView() {
        Log.d(TAG, "flickView: // -------------------------------------------");
        if (!isFlicking) {
            startAnimation();
        }
    }

    private Handler handler;
    private boolean isFlicking = false;

    private void startAnimation() {
        Log.d(TAG, "startAnimation: // -------------------------------------------");
        if (!isFlicking) {
            if (handler != null) {
                handler.post(runnable);
                isFlicking = true;
            }
        }
    }

    private void stopAnimation() {
        Log.d(TAG, "stopAnimation: // -------------------------------------------");
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            isFlicking = false;
        }
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (getVisibility() == GONE) {
                setVisibility(VISIBLE);
            } else {
                setVisibility(GONE);
            }
            handler.postDelayed(this, 1200);
        }
    };
}
