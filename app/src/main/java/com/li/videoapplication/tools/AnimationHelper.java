package com.li.videoapplication.tools;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;

public class AnimationHelper {

	private Animation shake;

	private Context context;

	public AnimationHelper() {
		super();
		
		context = AppManager.getInstance().getApplication();
		shake = AnimationUtils.loadAnimation(context, R.anim.anim_shake_7);
	}

	public void startAnimationShake(View view) {
		view.startAnimation(shake);
	}
}
