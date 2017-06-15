package com.li.videoapplication.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.chad.library.adapter.base.animation.BaseAnimation;

/**
 * 左侧进入动画
 */

public class SlideLiftAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", new float[]{(float)(-view.getRootView().getWidth()), 0.0F});
        animator.setDuration(1000);
        return new Animator[]{animator};
    }
}
