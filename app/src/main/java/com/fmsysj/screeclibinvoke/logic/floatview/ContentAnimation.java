package com.fmsysj.screeclibinvoke.logic.floatview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.li.videoapplication.framework.AppManager;


/**
 * 浮窗动画
 */
public class ContentAnimation {

    /**
     * 是否打开
     */
    public boolean isOpen = false;
    private static final int ANIMA_TIME = 200;

    private AnimationSet openSet, closeSet;
    private ScaleAnimation openScale, closeScale;
    private TranslateAnimation openTranslate, closeTranslate;

    private Context context;
    private Handler handler;
    private View container, root;

    public ContentAnimation(View container, View root) {
        context = AppManager.getInstance().getContext();
        handler = new Handler(Looper.myLooper());
        this.container = container;
        this.root = root;
        container.setVisibility(View.GONE);
        root.setEnabled(false);
        root.setClickable(false);
    }

    private void setCloseAnimaion() {
        // 关闭动画
        closeTranslate = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
                0.0f,
                TranslateAnimation.ABSOLUTE,
                FloatViewManager.getInstance().moveX + FloatViewManager.getInstance().floatWidth / 2 - FloatViewManager.getInstance().displayWidth / 2,
                TranslateAnimation.ABSOLUTE,
                0.0f,
                TranslateAnimation.ABSOLUTE,
                FloatViewManager.getInstance().moveY + FloatViewManager.getInstance().floatWidth / 2 - FloatViewManager.getInstance().displayHeight / 2);
        closeTranslate.setDuration(ANIMA_TIME);
        closeTranslate.setFillAfter(true);

        closeScale = new ScaleAnimation(1f, 0.2f,
                1f, 0.2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        closeScale.setDuration(ANIMA_TIME);
        closeScale.setFillAfter(false);

        closeSet = new AnimationSet(true);
        closeSet.addAnimation(closeScale);
        closeSet.addAnimation(closeTranslate);
        openSet.setFillAfter(true);
        closeSet.setAnimationListener(listener);
    }

    private void setOpenAnimation() {
        // 打开动画
        openTranslate = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
                FloatViewManager.getInstance().moveX + FloatViewManager.getInstance().floatWidth / 2 - FloatViewManager.getInstance().displayWidth / 2,
                TranslateAnimation.ABSOLUTE,
                0.0f,
                TranslateAnimation.ABSOLUTE,
                FloatViewManager.getInstance().moveY + FloatViewManager.getInstance().floatWidth / 2 - FloatViewManager.getInstance().displayHeight / 2,
                TranslateAnimation.ABSOLUTE,
                0.0f);
        openTranslate.setDuration(ANIMA_TIME);
        openTranslate.setFillAfter(true);

        openScale = new ScaleAnimation(0.2f, 1f,
                0.2f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        openScale.setDuration(ANIMA_TIME);
        openScale.setFillAfter(true);

        openSet = new AnimationSet(true);
        openSet.addAnimation(openScale);
        openSet.addAnimation(openTranslate);
        openSet.setFillAfter(true);
        // closeSet.setInterpolator(context, android.R.anim.accelerate_interpolator);
        openSet.setAnimationListener(listener);
    }

    private Animation.AnimationListener listener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            if (!isOpen) {// 关闭
                container.setVisibility(View.GONE);
                if (animationable != null)
                    animationable.animate();
            } else {// 打开
                root.setEnabled(true);
                root.setClickable(true);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };

    private Animationable animationable;

    /**
     * 开始浮窗动画
     */
    public void startAnimation(Animationable animationable) {
        this.animationable = animationable;
        if (!isOpen) {// 展开
            isOpen = true;
            setOpenAnimation();
            container.startAnimation(openSet);
            container.setVisibility(View.VISIBLE);
        } else {// 关闭
            root.setEnabled(false);
            root.setClickable(false);
            isOpen = false;
            setCloseAnimaion();
            container.startAnimation(closeSet);
            container.setVisibility(FrameLayout.VISIBLE);
        }
    }
}
