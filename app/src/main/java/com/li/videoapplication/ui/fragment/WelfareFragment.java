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
import com.li.videoapplication.ui.pageradapter.WelfarePagerAdapter;
import com.li.videoapplication.views.ViewPagerY1;
/**
 * 碎片：福利
 */
public class WelfareFragment extends TBaseChildFragment implements OnPageChangeListener {
	
	private List<RelativeLayout> topButtons;

	private List<ImageView> topLine;
	private List<ImageView> topPoint;
	private List<TextView> topText;
	@SuppressWarnings("unused")
	private int currIndex = 0;// 当前页卡编号
	private List<Fragment> fragments;
	private ViewPagerY1 mViewPager;
	private WelfarePagerAdapter adapter;

	@Override
	protected int getCreateView() {
		return R.layout.fragment_welfare;
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

	/**
	 * 初始化菜单
	 */
	protected void initTopMenu(View view) {
		
		if (topButtons == null) {
			topButtons = new ArrayList<RelativeLayout>();
			topButtons.add((RelativeLayout) view.findViewById(R.id.top_left));
			topButtons.add((RelativeLayout) view.findViewById(R.id.top_middle));
			topButtons.add((RelativeLayout) view.findViewById(R.id.top_right));
		}

		if (topLine == null) {
			topLine = new ArrayList<ImageView>();
			topLine.add((ImageView) view.findViewById(R.id.top_left_line));
			topLine.add((ImageView) view.findViewById(R.id.top_middle_line));
			topLine.add((ImageView) view.findViewById(R.id.top_right_line));
		}
		
		if (topText == null) {
			topText = new ArrayList<TextView>();
			topText.add((TextView) view.findViewById(R.id.top_left_text));
			topText.add((TextView) view.findViewById(R.id.top_middle_text));
			topText.add((TextView) view.findViewById(R.id.top_right_text));
		}
		
		if (topPoint == null) {
			topPoint = new ArrayList<ImageView>();
			topPoint.add((ImageView) view.findViewById(R.id.top_left_point));
			topPoint.add((ImageView) view.findViewById(R.id.top_middle_point));
			topPoint.add((ImageView) view.findViewById(R.id.top_right_point));
		}
		
		if (fragments == null) {
			fragments = new ArrayList<Fragment>();
			fragments.add(new GameMatchFragment());
			fragments.add(new ActivityFragment());
			fragments.add(new GiftFragment());
		}
		
		mViewPager = (ViewPagerY1) view.findViewById(R.id.viewpager);
//		mViewPager.setScrollable(true);
		mViewPager.setOffscreenPageLimit(2);
		adapter = new WelfarePagerAdapter(childManager, fragments);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(this);
		
		for (int i = 0; i < topButtons.size(); i++) {
			OnTabClickListener onTabClickListener = new OnTabClickListener(i);
			topButtons.get(i).setOnClickListener(onTabClickListener);
		}
		setTextViewText(topText.get(0),R.string.welfare_left);
		setTextViewText(topText.get(1), R.string.welfare_middle);
		setTextViewText(topText.get(2), R.string.welfare_right);
		
		for (ImageView point : topPoint) {
			point.setVisibility(View.GONE);
		}
		
		switchTab(0);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int arg0) {
		switchTab(arg0);
	}
	
	/**
	 * 菜单点击事件
	 */
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
	
	/**
	 * 叶卡选择
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
}
