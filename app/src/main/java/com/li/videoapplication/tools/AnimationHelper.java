package com.li.videoapplication.tools;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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

	public void startCircularRevealAnim(View view) {// FIXME: 2016/12/15
		//CircularReveal动画是api21之后才有
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			int cicular_R = view.getHeight() / 2 > view.getWidth() / 2 ? view.getHeight() / 2 : view.getWidth() / 2;
			Animator animator = ViewAnimationUtils.createCircularReveal(view, view.getWidth() / 2,
					view.getHeight() / 2, 0, cicular_R);
			animator.setDuration(1000);
			animator.start();
		}
	}

}
