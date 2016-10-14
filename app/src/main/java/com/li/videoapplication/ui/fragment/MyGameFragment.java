package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.response.MyGroupListEntity;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.PullToRefreshHepler;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.MyGameAdapter;
/**
 * 碎片：我的游戏
 */
public class MyGameFragment extends TBaseFragment implements OnRefreshListener2<ListView> {
	
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private MyGameAdapter adapter;
	private List<Game> data;
	
	private int page = 1;

	@Override
	protected int getCreateView() {
		return R.layout.fragment_mygame;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected IPullToRefresh getPullToRefresh() {
		return pullToRefreshListView;
	}

	@Override
	protected void initContentView(View view) {
		
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
		pullToRefreshListView.setMode(Mode.PULL_FROM_START);
		listView = pullToRefreshListView.getRefreshableView();
		
		data = new ArrayList<>();
		adapter = new MyGameAdapter(getActivity(), MyGameAdapter.PAGE_MYGAME, data);
		listView.setAdapter(adapter);
		pullToRefreshListView.setOnRefreshListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		onPullDownToRefresh(pullToRefreshListView);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		//该fragment处于最前台交互状态
		if (isVisibleToUser) {
			UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.GAME, "找游戏-我的游戏");
		}
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
		
		// 我的游戏
		DataManager.myGroupList(getMember_id(), page);
	}
	
	/**
	 * 回调:热门游戏列表
	 */
	public void onEventMainThread(MyGroupListEntity event) {
		
		if (event != null) {
			boolean result = event.isResult();
			if (result) {
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
	 * 事件：登录
	 */
	public void onEventMainThread(LoginEvent event) {
		
		onPullDownToRefresh(pullToRefreshListView);
	}
	
	/**
	 * 事件：注销
	 */
	public void onEventMainThread(LogoutEvent event) {

		onPullDownToRefresh(pullToRefreshListView);
	}
}
