package com.fmsysj.zbqmcs.animation;

import com.fmsysj.zbqmcs.floatview.FloatViewManager;
import com.fmsysj.zbqmcs.utils.ExApplication;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


/**
 * 录屏时计时浮窗2动画相关类
 * 
 * @author WYX
 * 
 */
public class FloatContentViewAnimation implements AnimationListener {

	private boolean isOpen = false;// 是否菜单打开状态

	private Context mContext;

	int bottomMargins;

	// 动画执行时间
	int animaTime = 500;

	AnimationSet animationSet;
	ScaleAnimation scale;
	TranslateAnimation translate;

	private int mScreenWidth, mScreenHeight;

	public FloatViewManager manager;

	RelativeLayout relativeLayout;
	View view;

	public int min = 0, sec = 0;

	/** 设置旋转动画 */
	final RotateAnimation RoaAnimation = new RotateAnimation(0f, 360f,
			Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

	public FloatContentViewAnimation(Context context,
			RelativeLayout relativeLayout, View view, FloatViewManager manager) {
		this.mContext = context;
		this.manager = manager;
		this.relativeLayout = relativeLayout;
		this.view = view;

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);

		mScreenWidth = displayMetrics.widthPixels;
		mScreenHeight = displayMetrics.heightPixels;
	}

	/**** 浮窗展开/回收动画 ***/
	public void extandAnmin() {

		// 开始执行悬浮框展开动画
		if (!isOpen) {

			isOpen = true;
			// 位移动画
			translate = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
					ExApplication.moveX - mScreenWidth / 2
							+ ExApplication.tvWidth / 2,
					TranslateAnimation.ABSOLUTE, 0.0f,
					TranslateAnimation.ABSOLUTE, ExApplication.moveY
							- mScreenHeight / 2 + ExApplication.tvWidth / 2,
					TranslateAnimation.ABSOLUTE, 0.0f);

			translate.setDuration(animaTime);
			translate.setFillAfter(true);
			// 缩放动画
			scale = new ScaleAnimation(0.2f, 1f, 0.2f, 1f,
					ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
					ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
			scale.setDuration(animaTime);
			scale.setFillAfter(true);
			// 动画集合
			animationSet = new AnimationSet(true);
			animationSet.addAnimation(scale);
			animationSet.addAnimation(translate);
			animationSet.setFillAfter(true);
			animationSet.setInterpolator(mContext, android.R.anim.overshoot_interpolator);
			relativeLayout.startAnimation(animationSet);
			relativeLayout.setVisibility(FrameLayout.VISIBLE);

		} else {// 开始执行悬浮框展开动画
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
			scale = new ScaleAnimation(1f, 0.05f, 1f, 0.05f,
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

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		manager.back2();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

}
