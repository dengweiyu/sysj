package com.fmsysj.zbqmcs.animation;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fmsysj.zbqmcs.floatview.FloatViewManager;
import com.fmsysj.zbqmcs.utils.ExApplication;
import com.li.videoapplication.R;


/**
 * 浮窗动画相关类
 * 
 * @author WYX
 * 
 */
public class FloatContentAnimation implements AnimationListener {
	private boolean isOpen = false;// 是否菜单打开状态
	// 动画执行时间
	int animaTime = 200;
	int animaTime2 = 300;

	AnimationSet animationSet, animationSet2, animationSet3, animationSet4,
			animationSet5;
	ScaleAnimation scale, scale2;
	TranslateAnimation translate, translate2, translate3, translate4,
			translate5;
	Context mContext;
	int bottomMargins;

	private int mScreenWidth, mScreenHeight;

	public FloatViewManager manager;

	LinearLayout screenLayout, creamLayout, recrodLayout, homeLayout;

	// 浮窗菜单宽度
	int btn_width;
	View relativeLayout;

	public FloatContentAnimation(Context context, View view, FloatViewManager manager) {
		mContext = context;
		this.manager = manager;
		relativeLayout = view;
		initButtons(view);
		inintView(context);

		initAnimin();

	}

	public FloatContentAnimation(Context context, View view) {

		inintView(context);
		relativeLayout = view;
	}

	private void inintView(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);

		mScreenWidth = displayMetrics.widthPixels;
		mScreenHeight = displayMetrics.heightPixels;

		screenLayout = (LinearLayout) relativeLayout
				.findViewById(R.id.layout_sc);
		creamLayout = (LinearLayout) relativeLayout
				.findViewById(R.id.layout_float);
		recrodLayout = (LinearLayout) relativeLayout
				.findViewById(R.id.layout_starts);
		homeLayout = (LinearLayout) relativeLayout
				.findViewById(R.id.layout_home);
	}

	void initAnimin() {
		translate = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
				ExApplication.moveX - mScreenWidth / 2 + ExApplication.tvWidth
						/ 2, TranslateAnimation.ABSOLUTE, 0.0f,
				TranslateAnimation.ABSOLUTE, ExApplication.moveY
						- mScreenHeight / 2 + ExApplication.tvWidth / 2,
				TranslateAnimation.ABSOLUTE, 0.0f);

		translate.setDuration(animaTime);
		translate.setFillAfter(true);

		scale = new ScaleAnimation(0.2f, 1f, 0.2f, 1f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		scale.setDuration(animaTime);
		scale.setFillAfter(true);
		animationSet = new AnimationSet(true);
		animationSet.addAnimation(scale);
		animationSet.addAnimation(translate);
		animationSet.setFillAfter(true);
		// animationSet.setInterpolator(context,
		// android.R.anim.accelerate_interpolator);
		animationSet.setAnimationListener(this);
	}

	/**** 浮窗展开/回收动画 ***/
	public void extandAnmin() {

		// 开始执行悬浮框展开动画
		if (!isOpen) {

			isOpen = true;

			relativeLayout.startAnimation(animationSet);

			relativeLayout.setVisibility(FrameLayout.VISIBLE);

		} else {// 开始执行悬浮框回收动画

			isOpen = false;

			translate = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
					0.0f, TranslateAnimation.ABSOLUTE, ExApplication.moveX
							- mScreenWidth / 2 + ExApplication.tvWidth / 2,
					TranslateAnimation.ABSOLUTE, 0.0f,
					TranslateAnimation.ABSOLUTE, ExApplication.moveY
							- mScreenHeight / 2 + 30);

			translate.setDuration(animaTime);
			translate.setFillAfter(true);
			animationSet = new AnimationSet(true);
			scale = new ScaleAnimation(1f, 0.2f, 1f, 0.2f,
					ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
					ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
			scale.setDuration(animaTime);
			scale.setFillAfter(false);
			animationSet.addAnimation(scale);
			animationSet.addAnimation(translate);
			animationSet.setAnimationListener(this);
			relativeLayout.startAnimation(animationSet);
		}
	}

	public void extandAnminForView1() {

		translate2 = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
				ExApplication.moveX - mScreenWidth / 2 + ExApplication.tvWidth
						/ 2, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
				TranslateAnimation.ABSOLUTE, ExApplication.moveY
						- mScreenHeight / 2 + ExApplication.tvWidth / 2,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f);
		translate2.setDuration(animaTime2);
		translate2.setFillAfter(true);

		translate3 = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_PARENT, -0.25f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f);
		translate3.setDuration(animaTime2);
		translate3.setFillAfter(true);

		translate4 = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0.25f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f);
		translate4.setDuration(animaTime2);
		translate4.setFillAfter(true);

		translate5 = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_PARENT, 0.0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, -0.25f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f);
		translate5.setDuration(animaTime2);
		translate5.setFillAfter(true);

		scale2 = new ScaleAnimation(0f, 1f, 0f, 1f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		scale2.setDuration(animaTime2);
		scale2.setFillAfter(true);

		animationSet2 = new AnimationSet(true);
		animationSet2.addAnimation(scale2);
		animationSet2.addAnimation(translate2);
		animationSet2.setFillAfter(true);

		animationSet3 = new AnimationSet(true);
		animationSet3.addAnimation(scale2);
		animationSet3.addAnimation(translate3);
		animationSet3.setFillAfter(true);

		animationSet4 = new AnimationSet(true);
		animationSet4.addAnimation(scale2);
		animationSet4.addAnimation(translate4);
		animationSet4.setFillAfter(true);

		animationSet5 = new AnimationSet(true);
		animationSet5.addAnimation(scale2);
		animationSet5.addAnimation(translate5);
		animationSet5.setFillAfter(true);

		screenLayout.startAnimation(animationSet2);
		creamLayout.startAnimation(animationSet2);
		recrodLayout.startAnimation(animationSet2);
		homeLayout.startAnimation(animationSet2);
	}

	public boolean isOpen() {
		return isOpen;
	}

	private void initButtons(View view) {

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// extandAnminForView1();
	}

	@Override
	public void onAnimationEnd(Animation animation) {

		if (isOpen) {

		} else {
			manager.back();
		}

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

}
