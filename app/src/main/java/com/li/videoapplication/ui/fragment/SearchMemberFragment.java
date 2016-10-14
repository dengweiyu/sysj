package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.SearchMember203Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseChildFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.ui.adapter.SearchMemberAdapter;

/**
 * 碎片：搜索视频
 */
public class SearchMemberFragment extends TBaseChildFragment implements OnRefreshListener2<ListView> {

	public static SearchMemberFragment newInstance(String content) {
		SearchMemberFragment fragment = new SearchMemberFragment();
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
	private SearchMemberAdapter adapter;
	private List<Member> data;
	
	private int page = 1;

	@Override
	protected int getCreateView() {
		return R.layout.fragment_searchmember;
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
		pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		
		listView = pullToRefreshListView.getRefreshableView();
		
		data = new ArrayList<>();
		adapter = new SearchMemberAdapter(getActivity(), SearchMemberAdapter.PAGE_SEARCHMEMBER, data);
		listView.setAdapter(adapter);
		
		pullToRefreshListView.setOnRefreshListener(this);
	}
	
	public void refreshListView() {
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				onPullDownToRefresh(pullToRefreshListView);
			}
		}, AppConstant.TIME.SEARCH_MEMBER);
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

	    // 搜索玩家
		//DataManager.searchMember(getContent(), page);

		// 视频玩家203
		DataManager.searchMember203(getContent(), getMember_id(), page);
	}
	
	/**
	 * 回调：搜索玩家203
	 */
	public void onEventMainThread(SearchMember203Entity event) {
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
	 * 回调：玩家关注
	 */
	public void onEventMainThread(MemberAttention201Entity event) {

		if (event != null) {
			if (event.isResult()) {
				showToastShort(event.getMsg());
			} else {
				showToastShort(event.getMsg());
			}
		}
	}
}
