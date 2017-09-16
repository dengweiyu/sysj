package com.li.videoapplication.tools;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;

public class AnimationHelper {

    private Context context;
    private Animation shake;
    public AnimationHelper() {
        super();
        context = AppManager.getInstance().getApplication();
        shake = AnimationUtils.loadAnimation(context, R.anim.anim_shake_7);
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

    public interface ITransition {
        void onTransitionEnd();
    }

    public void addTransitionListener(Activity activity, final ITransition iTransition) {
        //CircularReveal动画是api21之后才有
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = activity.getWindow().getSharedElementEnterTransition();
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override//过渡结束
                public void onTransitionEnd(Transition transition) {
                    iTransition.onTransitionEnd();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
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

    public void beginSlideTransition(ViewGroup viewGroup, int gravity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(viewGroup,
                    new Slide(gravity).setDuration(800).setInterpolator(new DecelerateInterpolator()));
        }
    }

    public void beginFadeTransition(ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(viewGroup,
                    new Fade().setDuration(800).setInterpolator(new DecelerateInterpolator()));
        }
    }

    //渐变滑动
    public void beginFadeSlideTransition(ViewGroup viewGroup, int gravity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet set = new TransitionSet()
                    .addTransition(new Fade())
                    .addTransition(new Slide(gravity))
                    .setDuration(800)
                    .setInterpolator(new DecelerateInterpolator());
            TransitionManager.beginDelayedTransition(viewGroup, set);
        }
    }

    //延迟从底部渐变滑出
    public void beginFadeSlideDelayTransition(ViewGroup viewGroup) {
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

    /**
     * 加号FAB旋转成叉号动画
     */
    public void add2CloseRotationAnim(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, -155, -135);
        animator.setDuration(600);
        animator.start();
    }

    /**
     * 加号FAB叉号旋转恢复成加号动画
     */
    public void close2AddRotationAnim(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", -135, 20, 0);
        animator.setDuration(600);
        animator.start();
    }

    public void startShake7(View view) {
        view.startAnimation(shake);
    }
}
