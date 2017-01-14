package com.li.videoapplication.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.response.SearchGame203Entity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseChildFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.ui.adapter.MyGameAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：搜索游戏
 */
public class SearchGameFragment extends TBaseChildFragment implements OnRefreshListener2<ListView> {

	public static SearchGameFragment newInstance(String content) {
		SearchGameFragment fragment = new SearchGameFragment();
		Bundle bundle = new Bundle();
		bundle.putString("content", content);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	private String content;
	
	public void setContent(String content) {
		this.content = content;
		
		refreshListView();
	}

	private String getContent() {
		return content;
	}
	
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private MyGameAdapter adapter;
	private List<Game> data;

	private int page = 1;

	@Override
	protected int getCreateView() {
		return R.layout.fragment_searchvideo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected IPullToRefresh getPullToRefresh() {
		return pullToRefreshListView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void initContentView(View view) {
		
		try {
			content = getArguments().getString("content");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		initListView(view);
		refreshListView();
	}
	
	private void initListView(View view) {
		
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
		pullToRefreshListView.setMode(Mode.BOTH);
		pullToRefreshListView.setOnRefreshListener(this);
		listView = pullToRefreshListView.getRefreshableView();
		View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
				(ViewGroup) listView.getParent(), false);
		TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
		emptyText.setText("没有相关游戏");
		listView.setEmptyView(emptyView);
		data = new ArrayList<>();
		adapter = new MyGameAdapter(getActivity(), MyGameAdapter.PAGE_SEARCHMEMBER, data);
		listView.setAdapter(adapter);
	}
	
	public void refreshListView() {
		
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				onPullDownToRefresh(pullToRefreshListView);
			}
		}, AppConstant.TIME.SEARCH_VIDEO);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {

		page = 1;
		onPullUpToRefresh(refreshView);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		PullToRefreshHepler.setLastUpdatedLabel(refreshView);
		onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);

		// 视频游戏203
		DataManager.searchGame203(getContent(), getMember_id(), page);
	}
	
	/**
	 * 回调：搜索游戏203
	 */
	public void onEventMainThread(SearchGame203Entity event) {
		if (event.isResult()) {
			if (event.getData().getList().size() > 0) {
				if (page == 1) {
					data.clear();
				}
				data.addAll(event.getData().getList());
				adapter.notifyDataSetChanged();
				++ page;
			}
		}
		onRefreshComplete();
	}

	/**
	 * 回调：视频点赞
	 */
	public void onEventMainThread(VideoFlower2Entity event) {

		if (event != null) {
			if (event.isResult()) {
				showToastShort(event.getMsg());
			} else {
				showToastShort(event.getMsg());
			}
		}
	}

	/**
	 * 回调：视频收藏
	 */
	public void onEventMainThread(VideoCollect2Entity event) {

		if (event != null) {
			if (event.isResult()) {
				showToastShort(event.getMsg());
			} else {
				showToastShort(event.getMsg());
			}
		}
	}
}
