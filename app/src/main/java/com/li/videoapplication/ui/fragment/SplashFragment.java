package com.li.videoapplication.ui.fragment;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseFragment;
/**
 * 碎片：启动-logo
 */
public class SplashFragment extends TBaseFragment implements AnimationListener {
	
	private View cover;
	private AlphaAnimation alphaAnimation;

	@Override
	protected int getCreateView() {
		return R.layout.fragment_splash;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected IPullToRefresh getPullToRefresh() {
		return null;
	}

	@Override
	protected void initContentView(View view) {
		
		cover = view.findViewById(R.id.cover);
		cover.setVisibility(View.VISIBLE);
		
		alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
		alphaAnimation.setDuration(1200);
		alphaAnimation.setInterpolator(new DecelerateInterpolator());
		alphaAnimation.setAnimationListener(this);

        cover.startAnimation(alphaAnimation);
	}

	@Override
	public void onAnimationStart(Animation animation) {

    }

	@Override
	public void onAnimationEnd(Animation animation) {
        cover.setVisibility(View.GONE);
    }

	@Override
	public void onAnimationRepeat(Animation animation) {

	}
}
