package com.li.videoapplication.framework;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.GameModuleEntity;
import com.li.videoapplication.tools.PullToRefreshHepler;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动：上拉下拉刷新加载
 */
public abstract class PullToRefreshActivity<T extends BaseEntity> extends TBaseActivity implements IPullToRefreshActivity, PullToRefreshBase.OnRefreshListener2 {

	public static final int TIME_REFRESH_SHORT = 1800;
	public static final int TIME_REFRESH_LONG = 6 * 1000;

	protected PullToRefreshListView pullToRefreshListView;
	protected ListView listView;
	protected List<T> data = new ArrayList<>();

	protected int page = 1;
	protected boolean isRefreshing;

	protected void refreshCompleteDelayedShort() {
		PullToRefreshHepler.onRefreshCompleteDelayed(getHandler(), pullToRefreshListView, TIME_REFRESH_SHORT);
	}

	protected void refreshCompleteDelayedLong() {
		PullToRefreshHepler.onRefreshCompleteDelayed(getHandler(), pullToRefreshListView, TIME_REFRESH_LONG);
	}

	protected void refreshComplete() {
		PullToRefreshHepler.onRefreshCompleteDelayed(getHandler(), pullToRefreshListView, 0);
	}

	@Override
	public void initView() {
		super.initView();

		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh);
		pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		pullToRefreshListView.setOnRefreshListener(this);

		listView = pullToRefreshListView.getRefreshableView();
		// listView.setEmptyView(getLoadingView());
	}

	private View loadingView;

	private View getLoadingView() {
		if (loadingView == null) {
			loadingView = inflater.inflate(R.layout.view_loading, null);
		}
		return loadingView;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		if (!isRefreshing) {
			page = 1;
			isRefreshing = true;
			onRefresh();
			refreshCompleteDelayedLong();
			PullToRefreshHepler.setLastUpdatedLabel(refreshView);
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		if (!isRefreshing) {
			isRefreshing = true;
			onLoadMore();
			refreshCompleteDelayedLong();
			PullToRefreshHepler.setLastUpdatedLabel(refreshView);
		}
	}

	protected void addHeaderView(View v) {
		if (listView != null && v != null)
			listView.addHeaderView(v);
	}

	protected ListAdapter getAdapter() {
		if (listView != null)
			return listView.getAdapter();
		return null;
	}

	protected void setEmptyView(View v) {
		if (listView != null)
			listView.setEmptyView(v);
	}

	protected void removeEmptyView() {
		if (listView != null)
			listView.removeView(getLoadingView());
	}

	protected void addFooterView(View v) {
		if (listView != null && v != null)
			listView.addFooterView(v);
	}

	protected void removeHeaderView(View v) {
		if (listView != null && v != null)
			listView.removeHeaderView(v);
	}

	protected void removeFooterView(View v) {
		if (listView != null && v != null)
			listView.removeFooterView(v);
	}

	protected void setAdapter(ListAdapter adapter) {
		if (listView != null && adapter != null)
			listView.setAdapter(adapter);
	}

	protected void removeAdapter() {
		if (listView != null) {
			listView.setAdapter(null);
		}
	}

	protected void setRefreshing() {
		if (pullToRefreshListView != null)
			pullToRefreshListView.setRefreshing();
	}

	protected void setMode(PullToRefreshBase.Mode mode) {
		if (pullToRefreshListView != null)
			pullToRefreshListView.setMode(mode);
	}

	protected void setModeStart() {
		if (pullToRefreshListView != null)
			pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
	}

	protected void setModeEnd() {
		if (pullToRefreshListView != null)
			pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
	}

	protected void setModeBoth() {
		if (pullToRefreshListView != null)
			pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
	}

	protected void setModeDisabled() {
		if (pullToRefreshListView != null)
			pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
	}

	protected void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		if (listView != null)
			listView.setOnItemClickListener(listener);
	}

	protected void setOnScrollListener(AbsListView.OnScrollListener listener) {
		if (listView != null)
			listView.setOnScrollListener(listener);
	}
}
