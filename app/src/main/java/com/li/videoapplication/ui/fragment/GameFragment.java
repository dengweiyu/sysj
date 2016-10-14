package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseChildFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.pageradapter.GamePagerAdapter;
import com.li.videoapplication.views.ViewPagerY1;
/**
 * 碎片：游戏
 */
public class GameFragment extends TBaseChildFragment {
	
	private List<RelativeLayout> topButtons;
	private List<ImageView> topLine;
	private List<ImageView> topPoint;
	private List<TextView> topText;
	private int currIndex = 0;// 当前页卡编号
	private List<Fragment> fragments;
	private ViewPagerY1 mViewPager;
	private GamePagerAdapter adapter;

	@Override
	protected int getCreateView() {
		return R.layout.fragment_game;
	}
	
	protected int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected IPullToRefresh getPullToRefresh() {
		return null;
	}

	@Override
	protected void initContentView(View view) {
		
		initTopMenu(view);
	}
	
	protected void initTopMenu(View view) {
		
		if (topButtons == null) {
			topButtons = new ArrayList<RelativeLayout>();
			topButtons.add((RelativeLayout) view.findViewById(R.id.top_left));
			topButtons.add((RelativeLayout) view.findViewById(R.id.top_right));
		}

		if (topLine == null) {
			topLine = new ArrayList<ImageView>();
			topLine.add((ImageView) view.findViewById(R.id.top_left_line));
			topLine.add((ImageView) view.findViewById(R.id.top_right_line));
		}
		
		if (topText == null) {
			topText = new ArrayList<TextView>();
			topText.add((TextView) view.findViewById(R.id.top_left_text));
			topText.add((TextView) view.findViewById(R.id.top_right_text));
		}
		
		if (topPoint == null) {
			topPoint = new ArrayList<ImageView>();
			topPoint.add((ImageView) view.findViewById(R.id.top_left_point));
			topPoint.add((ImageView) view.findViewById(R.id.top_right_point));
		}
		
		if (fragments == null) {
			fragments = new ArrayList<Fragment>();
			fragments.add(new ClassifiedGameFragment());
			fragments.add(new MyGameFragment());
		}
		
		mViewPager = (ViewPagerY1) view.findViewById(R.id.viewpager);
//		mViewPager.setScrollable(true);
		mViewPager.setOffscreenPageLimit(2);
		adapter = new GamePagerAdapter(childManager, fragments);
		mViewPager.setAdapter(adapter);
		PageChangeListener listener = new PageChangeListener();
		mViewPager.setOnPageChangeListener(listener);
		
		for (int i = 0; i < topButtons.size(); i++) {
			OnTabClickListener onTabClickListener = new OnTabClickListener(i);
			topButtons.get(i).setOnClickListener(onTabClickListener);
		}
		
		setTextViewText(topText.get(0), R.string.game_left);
		setTextViewText(topText.get(1), R.string.game_right);
		
		for (ImageView point : topPoint) {
			point.setVisibility(View.GONE);
		}
		
		switchTab(0);
		mViewPager.setCurrentItem(0);
	}
	
	public void setCurrentItem(int pager) {
		if (pager == 0 || pager == 1) {
			mViewPager.setCurrentItem(pager);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		//该fragment处于最前台交互状态
		if (isVisibleToUser) {
			UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "进入找游戏页面次数");
			UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "进入找游戏页面次数");
		}
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
	
	private class OnTabClickListener implements OnClickListener {
		
		private int index;

		public OnTabClickListener(int i) {
			this.index = i;
		}

		@Override
		public void onClick(View v) {
			mViewPager.setCurrentItem(index);
			switchTab(index);
			currIndex = index;
		}
	}
	
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
}
