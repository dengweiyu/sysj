package com.li.videoapplication.tools;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.chad.library.adapter.base.animation.BaseAnimation;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;

public class AnimationHelper {

    private Context context;

    public AnimationHelper() {
        super();
        context = AppManager.getInstance().getApplication();
    }

    public void startAnimationShake(View view) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.anim_shake_7);
        view.startAnimation(shake);
    }

    /**
     * view 操作的视图
     * centerX 动画开始的中心点X
     * centerY 动画开始的中心点Y
     * startRadius 动画开始半径
     * startRadius 动画结束半径
     */
    public void startCenterCRAnim(View view) {
        //CircularReveal动画是api21之后才有
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cicular_R = view.getHeight() / 2 > view.getWidth() / 2 ? view.getHeight() / 2 : view.getWidth() / 2;
            Animator animator = ViewAnimationUtils.createCircularReveal(view, view.getWidth() / 2,
                    view.getHeight() / 2, 0, cicular_R);
            animator.setDuration(400);
            animator.start();
        }
    }

    public void startCircularRevealAnim(View view) {
        //CircularReveal动画是api21之后才有
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float cicular_R = (float) Math.hypot(view.getWidth(), view.getHeight());
            Animator animator = ViewAnimationUtils.createCircularReveal(view, 0, 0, 0, cicular_R);
            animator.setDuration(400);
            animator.start();
        }
    }

    public void beginFadeSlideTransition(ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet set = new TransitionSet()
                    .addTransition(new Fade())
                    .addTransition(new Slide())
                    .setDuration(600)
                    .setStartDelay(400)
                    .setInterpolator(new DecelerateInterpolator());
            TransitionManager.beginDelayedTransition(viewGroup, set);
        }
    }
}
