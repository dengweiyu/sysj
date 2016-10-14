package com.li.videoapplication.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.pageradapter.GamePagerAdapter;
import com.li.videoapplication.ui.fragment.MyActivityFragment;
import com.li.videoapplication.ui.fragment.MyGiftFragment;
import com.li.videoapplication.views.ViewPagerY4;
/**
 * 活动：我的福利
 */
public class MyWelfareActivity extends TBaseActivity {

    private List<RelativeLayout> topButtons;
	private List<ImageView> topLine;
	private List<ImageView> topPoint;
	private List<TextView> topText;
	private int currIndex = 0;// 当前页卡编号
	private List<Fragment> fragments;
	private ViewPagerY4 viewPager;
	private GamePagerAdapter adapter;

	@Override
	public int getContentView() {
		return R.layout.activity_mywelfare;
	}

	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();
		setSystemBarBackgroundWhite();
		setAbTitle(R.string.mywelfare_title);
	}

	@Override
	public void initView() {
		super.initView();
		initTopMenu();
	}

	/**
	 * 功能：初始化菜单
	 */
	protected void initTopMenu() {
		
		if (topButtons == null) {
			topButtons = new ArrayList<RelativeLayout>();
			topButtons.add((RelativeLayout) findViewById(R.id.top_left));
			topButtons.add((RelativeLayout) findViewById(R.id.top_right));
		}

		if (topLine == null) {
			topLine = new ArrayList<ImageView>();
			topLine.add((ImageView) findViewById(R.id.top_left_line));
			topLine.add((ImageView) findViewById(R.id.top_right_line));
		}
		
		if (topText == null) {
			topText = new ArrayList<TextView>();
			topText.add((TextView) findViewById(R.id.top_left_text));
			topText.add((TextView) findViewById(R.id.top_right_text));
		}
		
		if (topPoint == null) {
			topPoint = new ArrayList<ImageView>();
			topPoint.add((ImageView) findViewById(R.id.top_left_point));
			topPoint.add((ImageView) findViewById(R.id.top_right_point));
		}
		
		if (fragments == null) {
			fragments = new ArrayList<Fragment>();
			Fragment a = new MyActivityFragment();
			Fragment b = new MyGiftFragment();
			fragments.add(a);
			fragments.add(b);
		}
		
		viewPager = (ViewPagerY4) findViewById(R.id.viewpager);
		viewPager.setScrollable(true);
		viewPager.setOffscreenPageLimit(2);
		adapter = new GamePagerAdapter(manager, fragments);
		viewPager.setAdapter(adapter);
		PageChangeListener listener = new PageChangeListener();
		viewPager.setOnPageChangeListener(listener);
		
		for (int i = 0; i < topButtons.size(); i++) {
			OnTabClickListener onTabClickListener = new OnTabClickListener(i);
			topButtons.get(i).setOnClickListener(onTabClickListener);
		}
		
		setTextViewText(topText.get(0), R.string.mywelfare_myactivity);
		setTextViewText(topText.get(1), R.string.mywelfare_mygift);
		
		for (ImageView point : topPoint) {
			point.setVisibility(View.GONE);
		}
		
		switchTab(0);
		viewPager.setCurrentItem(0);
	}
	
	private class PageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageSelected(int arg0) {
			switchTab(arg0);
		}
	}
	
	/**
	 * 功能：菜单点击事件
	 * 
	 * @author Administrator
	 *
	 */
	private class OnTabClickListener implements OnClickListener {
		
		private int index;

		public OnTabClickListener(int i) {
			this.index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
			switchTab(index);
			currIndex = index;
		}
	}
	
	/**
	 * 功能：叶卡选择
	 * 
	 * @param index
	 */
	private void switchTab(int index) {
		for (int i = 0; i < topText.size(); i++) {
			if (index == i) {
				topText.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_red));
			} else {
				topText.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_gray));
			}
		}
		for (int i = 0; i < topLine.size(); i++) {
			if (index == i) {
				topLine.get(i).setImageResource(R.color.menu_game_red);
			} else {
				topLine.get(i).setImageResource(R.color.menu_game_transperent);
			}
		}
	}
	
	/**
	 * 事件：登录
	 * @param event
	 */
	public void onEventMainThread(LoginEvent event) {
		
	}
	
	/**
	 * 事件：注销
	 * @param event
	 */
	public void onEventMainThread(LogoutEvent event) {
		
		finish();
	}
}
