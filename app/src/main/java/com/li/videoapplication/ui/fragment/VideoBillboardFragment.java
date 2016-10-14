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
import com.li.videoapplication.ui.pageradapter.GamePagerAdapter;
import com.li.videoapplication.views.ViewPagerY4;
/**
 * 碎片：风云榜-视频榜
 */
public class VideoBillboardFragment extends TBaseChildFragment {
	
	private List<RelativeLayout> topButtons;
	private List<ImageView> topLine;
	private List<ImageView> topIcon;
	private List<TextView> topText;
	private int currIndex = 0;// 当前页卡编号
	private List<Fragment> fragments;
	private ViewPagerY4 viewPager;
	private GamePagerAdapter adapter;

	@Override
	protected int getCreateView() {
		return R.layout.fragment_playerbillboard;
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
		
		if (topIcon == null) {
			topIcon = new ArrayList<ImageView>();
			topIcon.add((ImageView) view.findViewById(R.id.top_left_icon));
			topIcon.add((ImageView) view.findViewById(R.id.top_middle_icon));
			topIcon.add((ImageView) view.findViewById(R.id.top_right_icon));
		}
		
		if (fragments == null) {
			fragments = new ArrayList<Fragment>();
			fragments.add(LikeBillboardFragment.newInstance(LikeBillboardFragment.VIDEOBILLBOARD_LIKE));
			fragments.add(LikeBillboardFragment.newInstance(LikeBillboardFragment.VIDEOBILLBOARD_COMMENT));
			fragments.add(LikeBillboardFragment.newInstance(LikeBillboardFragment.VIDEOBILLBOARD_CLICK));
		}
		
		viewPager = (ViewPagerY4) view.findViewById(R.id.viewpager);
		viewPager.setScrollable(true);
		viewPager.setOffscreenPageLimit(6);
		adapter = new GamePagerAdapter(childManager, fragments);
		viewPager.setAdapter(adapter);
		PageChangeListener listener = new PageChangeListener();
		viewPager.addOnPageChangeListener(listener);
		
		for (int i = 0; i < topButtons.size(); i++) {
			OnTabClickListener onTabClickListener = new OnTabClickListener(i);
			topButtons.get(i).setOnClickListener(onTabClickListener);
		}

		setTextViewText(topText.get(0), R.string.videoBillboard_left);
		setTextViewText(topText.get(1), R.string.videoBillboard_middle);
		setTextViewText(topText.get(2), R.string.videoBillboard_right);
		
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
	 * 菜单点击事件
	 */
	private class OnTabClickListener implements OnClickListener {
		
		private int index;

		public OnTabClickListener(int i) {
			this.index = i;
		}

		@Override
		public void onClick(View v) {
			switchTab(index);
			viewPager.setCurrentItem(index);
			currIndex = index;
		}
	}
	
	/**
	 * 叶卡选择
	 */
	private void switchTab(int index) {

		for (int i = 0; i < topText.size(); i++) {
			if (index == i) {
				switch (index) {
					case 0:
						topText.get(i).setTextColor(resources.getColorStateList(R.color.menu_billboard_blue));
						break;
					case 1:
						topText.get(i).setTextColor(resources.getColorStateList(R.color.menu_billboard_yellow));
						break;
					case 2:
						topText.get(i).setTextColor(resources.getColorStateList(R.color.menu_billboard_purple));
						break;
				}
			} else {
				topText.get(i).setTextColor(resources.getColorStateList(R.color.menu_billboard_gray));
			}
		}
		for (int i = 0; i < topLine.size(); i++) {
			if (index == i) {
				switch (index) {
					case 0:
						topLine.get(i).setImageResource(R.color.menu_billboard_blue);
						break;
					case 1:
						topLine.get(i).setImageResource(R.color.menu_billboard_yellow);
						break;
					case 2:
						topLine.get(i).setImageResource(R.color.menu_billboard_purple);
						break;
				}
			} else {
				topLine.get(i).setImageResource(R.color.menu_billboard_transperent);
			}
		}

		for (int i = 0; i < topIcon.size(); i++) {
			if (index == i) {
				switch (index) {
					case 0:
						setImageViewImageRes(topIcon.get(0), R.drawable.vediobiilboard_like);
						setImageViewImageRes(topIcon.get(1), R.drawable.vediobiilboard_comment_gray);
						setImageViewImageRes(topIcon.get(2), R.drawable.vediobiilboard_watched_gray);
						break;
					case 1:
						setImageViewImageRes(topIcon.get(0), R.drawable.vediobiilboard_like_gray);
						setImageViewImageRes(topIcon.get(1), R.drawable.vediobiilboard_comment);
						setImageViewImageRes(topIcon.get(2), R.drawable.vediobiilboard_watched_gray);
						break;
					case 2:
						setImageViewImageRes(topIcon.get(0), R.drawable.vediobiilboard_like_gray);
						setImageViewImageRes(topIcon.get(1), R.drawable.vediobiilboard_comment_gray);
						setImageViewImageRes(topIcon.get(2), R.drawable.vediobiilboard_watched);
						break;
				}
			}
		}
	}

}
