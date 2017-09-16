package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.event.SearchResultEvent;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.SearchMember203Entity;
import com.li.videoapplication.data.network.RequestUrl;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.BaseAppCompatActivity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseChildFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.SearchMemberAdapter;

import io.rong.eventbus.EventBus;

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
		if (this.content != null && !this.content.equals(content)){
			this.content = content;
			if (data != null){
				data.clear();
			}
			if (adapter != null){
				adapter.updateKey(content);
			}

		}
		
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
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {//该fragment处于最前台交互状态
			UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "搜索-相关主播-点击相关主播次数");
		}
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
		View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview,
				(ViewGroup) listView.getParent(), false);
		TextView emptyText = (TextView) emptyView.findViewById(R.id.emptyview_text);
		emptyText.setText("没有相关主播");
		listView.setEmptyView(emptyView);
		data = new ArrayList<>();
		adapter = new SearchMemberAdapter(getActivity(), SearchMemberAdapter.PAGE_SEARCHMEMBER, data,content);
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
		Activity activity = getActivity();
		if (activity instanceof BaseAppCompatActivity){
			((BaseAppCompatActivity)activity).requestManager.cancelTask(RequestUrl.getInstance().searchMember203());
		}
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
				EventBus.getDefault().post(new SearchResultEvent(2,true));

				if (page == 1) {
					data.clear();
				}
				data.addAll(Lists.newArrayList(Iterables.filter(event.getData().getList(), new Predicate<Member>() {
					@Override
					public boolean apply(Member input) {
						for (Member m:
								data) {
							if (m.getMember_id().equals(input.getMember_id())){
								return false;
							}
						}
						return true;
					}
				})));

				page++;
			}else {
				EventBus.getDefault().post(new SearchResultEvent(2,false));

			}
			adapter.notifyDataSetChanged();
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
