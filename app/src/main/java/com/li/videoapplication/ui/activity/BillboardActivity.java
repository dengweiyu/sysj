package com.li.videoapplication.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.fragment.PlayerBillboardFragment;
import com.li.videoapplication.ui.fragment.VideoBillboardFragment;
/**
 * 活动 ：风云榜
 */
public class BillboardActivity extends TBaseActivity implements RadioGroup.OnCheckedChangeListener {

	private RadioGroup radioGroup;
	private List<Fragment> fragments;
	private Fragment context;

	@Override
	public int getContentView() {
		return R.layout.activity_billboard;
	}

	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();

		setSystemBarBackgroundWhite();
		initTopMenu();
	}

	protected void initTopMenu() {

		if (fragments == null) {
			fragments = new ArrayList<>();
			Fragment a = new PlayerBillboardFragment();
			Fragment b = new VideoBillboardFragment();
			fragments.add(a);
			fragments.add(b);
		}

		radioGroup = (RadioGroup) findViewById(R.id.ab_billboard_radio);
		radioGroup.setOnCheckedChangeListener(this);
		RadioButton left = (RadioButton) findViewById(R.id.ab_billboard_left);
		RadioButton right = (RadioButton) findViewById(R.id.ab_billboard_right);
		
		left.setChecked(true);
		replaceFragment(fragments.get(0));
		context = fragments.get(0);
		
		abBillboardBg.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {

		case R.id.ab_billboard_left:
			switchContent(context, fragments.get(0));
			break;

		case R.id.ab_billboard_right:
			switchContent(context, fragments.get(1));
			break;
		}
	}
	
	public void replaceFragment(Fragment target) {
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container, target).commit();
	}
	
	public void switchContent(Fragment from, Fragment to) {
		if (context != to) {
			context = to;
			FragmentTransaction transaction = manager.beginTransaction();
			if (!to.isAdded()) {
				transaction.hide(from).add(R.id.container, to).commit();
			} else {
				transaction.hide(from).show(to).commit();
			}
		}
	}
}
