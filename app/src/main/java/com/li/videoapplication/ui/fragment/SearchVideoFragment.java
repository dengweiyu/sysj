package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.SearchVideo203Entity;
import com.li.videoapplication.data.model.response.SearchVideoHotEntity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseChildFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.ui.adapter.GroupDetailVideoAdapter;

/**
 * 碎片：搜索视频
 */
public class SearchVideoFragment extends TBaseChildFragment implements OnRefreshListener2<ListView> {

	public static SearchVideoFragment newInstance(String content) {
		SearchVideoFragment fragment = new SearchVideoFragment();
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
	private GroupDetailVideoAdapter adapter;
	private List<VideoImage> data;

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
		
		data = new ArrayList<VideoImage>();
		adapter = new GroupDetailVideoAdapter(getActivity(), data);
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
		// 视频搜索（最新）
		//DataManager.searchVideoNew(getContent(), page);
		// 视频搜索（最热）
		//DataManager.searchVideoHot(getContent(), page);

		// 视频搜索203
		DataManager.searchVideo203(getContent(), getMember_id(), page);
	}
	
	/**
	 * 回调：视频搜索203
	 */
	public void onEventMainThread(SearchVideo203Entity event) {
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
	 * 回调：视频搜索（最热）
	 */
	public void onEventMainThread(SearchVideoHotEntity event) {
		if (event != null) {
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
