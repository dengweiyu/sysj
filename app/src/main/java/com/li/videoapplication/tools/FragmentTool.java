package com.li.videoapplication.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.li.videoapplication.R;

public class FragmentTool {
	
	/**
	 * 功能：碎片选择器
	 */
	public synchronized final void replaceFragment(FragmentManager manager, int res, Fragment target) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(res, target).commit();
	}

	/**
	 * 功能：碎片选择器
	 */
	public synchronized final void chooseFragment(FragmentManager manager, int res, Fragment fragment) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(res, fragment);
		transaction.commit();
	}

	/**
	 * 功能：碎片选择器
	 */
	public synchronized final void switchContent(FragmentManager manager, int resLayout, int animEnter, int animExit, Fragment mContent, Fragment from, Fragment to) {
		if (mContent != to) {
			mContent = to;
			FragmentTransaction transaction = manager.beginTransaction();
			if (animEnter == 0) {
				animEnter = R.anim.activity_hold;
			}
			if (animExit == 0) {
				animExit = R.anim.activity_hold;
			}
			transaction.setCustomAnimations(animEnter, animExit);
			if (!to.isAdded()) {
				transaction.hide(from).add(resLayout, to).commit();
			} else {
				transaction.hide(from).show(to).commit();
			}
		}
	}
}
