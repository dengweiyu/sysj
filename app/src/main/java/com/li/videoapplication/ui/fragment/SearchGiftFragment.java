package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.data.model.event.SearchResultEvent;
import com.li.videoapplication.data.model.response.ClaimPackageEntity;
import com.li.videoapplication.data.model.response.SearchPackage203Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseChildFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.GiftAdapter;
import com.ypy.eventbus.EventBus;

/**
 * 碎片：搜索视频
 */
public class SearchGiftFragment extends TBaseChildFragment implements OnRefreshListener2<ListView> {

	public static SearchGiftFragment newInstance(String content) {
		SearchGiftFragment fragment = new SearchGiftFragment();
		Bundle bundle = new Bundle();
		bundle.putString("content", content);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	private String content;
	
	public void setContent(String content) {
		if (this.content != null &&!this.content.equals(content)){
			this.content = content;
			if (data != null){
				data.clear();
			}
		}

		refreshListView();
	}

	private String getContent() {
		return content;
	}
	
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private GiftAdapter adapter;
	private List<Gift> data;
	
	private int page = 1;

	@Override
	protected int getCreateView() {
		return R.layout.fragment_gift;
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
	public void onDestroyView() {
		super.onDestroyView();
		if (data != null){
			data.clear();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {//该fragment处于最前台交互状态
			UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "搜索-相关礼包-点击相关礼包次数");
		}
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
	
	public void refreshListView() {
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				onPullDownToRefresh(pullToRefreshListView);
			}
		}, AppConstant.TIME.SEARCH_GIFT);
	}

	private void initListView(View view) {
		
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
		pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		listView = pullToRefreshListView.getRefreshableView();
		View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
				(ViewGroup) listView.getParent(), false);
		TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
		emptyText.setText("没有相关礼包");
		listView.setEmptyView(emptyView);
		data = new ArrayList<>();
		adapter = new GiftAdapter(getActivity(), data);
		listView.setAdapter(adapter);
		
		pullToRefreshListView.setOnRefreshListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
	    page = 1;
	    onPullUpToRefresh(refreshView);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		PullToRefreshHepler.setLastUpdatedLabel(refreshView);
		onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);

	    // 搜索礼包
		//DataManager.searchPackage(getContent(), page);

		// 视频礼包203
		DataManager.searchPackage203(getContent(), getMember_id(), page);
	}
	
	/**
	 * 回调：搜索礼包203
	 */
	public void onEventMainThread(SearchPackage203Entity event) {

		if (event.isResult()) {
			if (event.getData().getList().size() > 0) {
				EventBus.getDefault().post(new SearchResultEvent(3,true));
				if (page == 1) {
					data.clear();
				}
				data.addAll(event.getData().getList());
				++ page;
			}else {
				EventBus.getDefault().post(new SearchResultEvent(3,false));

			}
			adapter.notifyDataSetChanged();
		}
		onRefreshComplete();
	}
	
	/**
	 * 回调：领取礼包
	 */
	public void onEventMainThread(ClaimPackageEntity event) {
		
		if (event != null) {
			if (event.isResult()) {
				showToastShort(event.getMsg());
			}
		}
	}
}
