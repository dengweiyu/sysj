package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.GetMatchList201Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.ui.adapter.ActivityAdapter;
/**
 * 碎片：活动
 */
public class ActivityFragment extends TBaseFragment implements OnRefreshListener2<ListView> {

	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private ActivityAdapter adapter;
	private List<Match> data;
	
	private int page = 1;

	@Override
	protected int getCreateView() {
		return R.layout.fragment_activity;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected IPullToRefresh getPullToRefresh() {
		return pullToRefreshListView;
	}

	@Override
	protected void initContentView(View view) {
		
		initListView(view);
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {

				onPullDownToRefresh(pullToRefreshListView);
			}
		}, AppConstant.TIME.WELFARE_ACTIVITY);
	}

	private void initListView(View view) {
		
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);

		pullToRefreshListView = (PullToRefreshListView) view .findViewById(R.id.pulltorefresh);
		pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

		listView = pullToRefreshListView.getRefreshableView();

		listView.addFooterView(newEmptyView(2));
		
		data = new ArrayList<Match>();
		adapter = new ActivityAdapter(getActivity(), data);
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
		
	    // 赛事列表
		DataManager.getMatchList201(page);
	}
	
	/**
	 * 回调：赛事列表
	 */
	public void onEventMainThread(GetMatchList201Entity event) {
		
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
}
