package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.RankingVideoRankingClickEntity;
import com.li.videoapplication.data.model.response.RankingVideoRankingCommentEntity;
import com.li.videoapplication.data.model.response.RankingVideoRankingEntity;
import com.li.videoapplication.data.model.response.RankingVideoRankingLikeEntity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.BillboardActivity;
import com.li.videoapplication.ui.adapter.GroupDetailVideoAdapter;

/**
 * 碎片：榜单-视频榜
 */
public class LikeBillboardFragment extends TBaseFragment implements OnItemClickListener, OnRefreshListener2<ListView> {

	public static final int VIDEOBILLBOARD_LIKE = 1;
	public static final int VIDEOBILLBOARD_COMMENT = 2;
	public static final int VIDEOBILLBOARD_CLICK = 3;

	public static LikeBillboardFragment newInstance(int billboard) {
		LikeBillboardFragment fragment = new LikeBillboardFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("tab", billboard);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	public BillboardActivity activity;
	
	public int billboard;
	
	public int getBillboard() {
		if (billboard == 0) {
			try {
				billboard = getArguments().getInt("tab");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (billboard == 0) {
				billboard = VIDEOBILLBOARD_LIKE;
			}
		}
		return billboard;
	}

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
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			this.activity = (BillboardActivity) activity;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (getBillboard() == VIDEOBILLBOARD_COMMENT) {
				UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "视频榜-评论榜");
			} else if (getBillboard() == VIDEOBILLBOARD_CLICK) {
				UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.DISCOVER, "视频榜-观看榜");
			}
		}
	}
	
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private GroupDetailVideoAdapter adapter;
	private List<VideoImage> data;
	
	private int page = 1;
	private boolean isRefreshing;

	@Override
	protected void initContentView(View view) {
		
		pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pulltorefresh);
		pullToRefreshListView.setMode(Mode.BOTH);
		listView = pullToRefreshListView.getRefreshableView();

		data = new ArrayList<>();
		adapter = new GroupDetailVideoAdapter(getActivity(), data);
		adapter.setTab(getBillboard());
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(this);
		pullToRefreshListView.setOnRefreshListener(this);
		
		if (getBillboard() == VIDEOBILLBOARD_LIKE) {// 点赞榜
			
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					onPullDownToRefresh(pullToRefreshListView);
				}
			}, 0);
			
		} else if (getBillboard() == VIDEOBILLBOARD_COMMENT) {// 评论榜
			
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					onPullDownToRefresh(pullToRefreshListView);
				}
			}, 200);
			
		} else if (getBillboard() == VIDEOBILLBOARD_CLICK) {// 观看榜
			
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					onPullDownToRefresh(pullToRefreshListView);
				}
			}, 400);
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isRefreshing) {
			page = 1;
			isRefreshing = true;
			pullToRefreshListView.setRefreshing();
			if (getBillboard() == VIDEOBILLBOARD_LIKE) {
				// 玩家榜--点赞榜
				DataManager.rankingVideoRankingLike(getMember_id(), page);
			} else if (getBillboard() == VIDEOBILLBOARD_COMMENT) {
				// 玩家榜--评论榜
				DataManager.rankingVideoRankingComment(getMember_id(), page);
			} else if (getBillboard() == VIDEOBILLBOARD_CLICK) {
				// 玩家榜--观看榜
				DataManager.rankingVideoRankingClick(getMember_id(), page);
			}
			onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_LONG);
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!isRefreshing) {
			isRefreshing = true;
			pullToRefreshListView.setRefreshing();
			if (getBillboard() == VIDEOBILLBOARD_LIKE) {
				// 玩家榜--点赞榜
				DataManager.rankingVideoRankingLike(getMember_id(), page);
			} else if (getBillboard() == VIDEOBILLBOARD_COMMENT) {
				// 玩家榜--评论榜
				DataManager.rankingVideoRankingComment(getMember_id(), page);
			} else if (getBillboard() == VIDEOBILLBOARD_CLICK) {
				// 玩家榜--观看榜
				DataManager.rankingVideoRankingClick(getMember_id(), page);
			}
			onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_LONG);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
	
	/**
	 * 玩家榜--点赞榜
	 */
	public void onEventMainThread(RankingVideoRankingLikeEntity event) {
		if (getBillboard() == VIDEOBILLBOARD_LIKE) {
			refreshData(event);
		}
	}
	
	/**
	 * 玩家榜--评论榜
	 */
	public void onEventMainThread(RankingVideoRankingCommentEntity event) {
		
		if (getBillboard() == VIDEOBILLBOARD_COMMENT) {
			refreshData(event);
		}
	}
	
	/**
	 * 玩家榜--观看榜
	 */
	public void onEventMainThread(RankingVideoRankingClickEntity event) {
		
		if (getBillboard() == VIDEOBILLBOARD_CLICK) {
			refreshData(event);
		}
	}

	private void refreshData(RankingVideoRankingEntity event) {
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
		isRefreshing = false;
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
}
