package com.li.videoapplication.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.fragment.MyPlayerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动 ：我的粉丝，我的关注
 */
public class MyPlayerActivity extends TBaseActivity implements RadioGroup.OnCheckedChangeListener {

	public static final int PAGE_MYFANS = 1;
	public static final int PAGE_MYFOCUS = 2;

	private RadioGroup radioGroup;
	private List<Fragment> fragments;
	private Fragment context;
	private Fragment myfans;
	private Fragment myfocus;

	private int page;
	public String member_id;

	@Override
	public void refreshIntent() {
		try {
			page = getIntent().getIntExtra("page", 0);
			member_id = getIntent().getStringExtra("member_id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (page == 0)
			finish();
	}

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
	}

	@Override
	public void initView() {
		super.initView();

		initTopMenu();
	}

	@Override
	public void loadData() {
		super.loadData();

		if (page == PAGE_MYFANS) {
			radioGroup.check(R.id.ab_billboard_left);
		} else if (page == PAGE_MYFOCUS) {
			radioGroup.check(R.id.ab_billboard_right);
		} else {
			radioGroup.check(R.id.ab_billboard_left);
		}
	}

	protected void initTopMenu() {

		if (fragments == null) {
			fragments = new ArrayList<>();
			myfans = MyPlayerFragment.newInstance(PAGE_MYFANS);
			fragments.add(myfans);
			myfocus = MyPlayerFragment.newInstance(PAGE_MYFOCUS);
			fragments.add(myfocus);
		}

		radioGroup = (RadioGroup) findViewById(R.id.ab_billboard_radio);
		radioGroup.setOnCheckedChangeListener(this);
		RadioButton left = (RadioButton) findViewById(R.id.ab_billboard_left);
		RadioButton right = (RadioButton) findViewById(R.id.ab_billboard_right);

		left.setText(R.string.myplayer_left);
		right.setText(R.string.myplayer_right);
		
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
