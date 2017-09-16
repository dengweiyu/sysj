package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.AuthorVideoGroupEntity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.adapter.VideoPlayVideoAdapter2;

import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.AbsListViewOverScrollDecorAdapter;

/**
 * 碎片：视频播放/图文详情--玩家视频
 */
public class VideoPlayVideoFragment extends TBaseFragment implements OnRefreshListener2<ListView>{

	public VideoImage item;
	private TextView title;

	public void setItem(VideoImage videoImage) {
		this.item = videoImage;
		onPullDownToRefresh(pullToRefreshListView);
	}
	
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private VideoPlayVideoAdapter2 adapter;
	private List<VideoImage> data;
	
	private int page = 1;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.VIDEOPLAY, "TA的视频");
		}
	}

	@Override
	protected int getCreateView() {
		return R.layout.fragment_videoplay_video;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected IPullToRefresh getPullToRefresh() {
		return pullToRefreshListView;
	}

	@Override
	protected void initContentView(View view) {
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
		pullToRefreshListView.setMode(Mode.PULL_FROM_END);
		listView = pullToRefreshListView.getRefreshableView();
		new VerticalOverScrollBounceEffectDecorator(new AbsListViewOverScrollDecorAdapter(listView));

		data = new ArrayList<>();
		adapter = new VideoPlayVideoAdapter2((VideoPlayActivity) getActivity(), data);
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
		// 用户视频分组
		DataManager.authorVideoGroup(item.getMember_id(), page);
		onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);
	}
	
	/**
	 * 回调:用户视频列表2
	 */
	public void onEventMainThread(AuthorVideoGroupEntity event) {
		
		if (event.isResult()) {
			if (event.getData().getData().size() > 0) {
				if (page == 1) {
					data.clear();
				}
				data.addAll(event.getData().getData());
				adapter.notifyDataSetChanged();
				++ page;
			}
		}
		onRefreshComplete();
	}
}
