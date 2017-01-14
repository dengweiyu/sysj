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
public class SplashFragment extends TBaseFragment {

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

	}
}
