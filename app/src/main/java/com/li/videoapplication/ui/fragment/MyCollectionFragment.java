package com.li.videoapplication.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.response.MemberCancelCollectVideoEntity;
import com.li.videoapplication.data.model.response.MemberCollectVideoListEntity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.ArrayHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * 活动：收藏-我的收藏
 */
public class MyCollectionFragment extends TBaseFragment implements
        OnRefreshListener2<GridView>{

    public PullToRefreshGridView pullToRefreshGridView;
    public GridView gridView;
    public VideoAdapter adapter;
	public List<VideoImage> data = new ArrayList<VideoImage>();
	
	private int page = 1;

	/**
	 * 删除
	 */
	public void deleteData() {
        List<String> list = new ArrayList<>();
        for (VideoImage record : adapter.deleteData) {
            list.add(record.getVideo_id());
		}
		if (list.size() > 0) {
			String video_ids = ArrayHelper.list2Array(list);
			Log.d(tag, "video_ids=" + video_ids);
			// 视频取消收藏
			DataManager.memberCancelCollectVideo(getMember_id(), video_ids);
		}
        for (Iterator<VideoImage> it = adapter.deleteData.iterator(); it.hasNext();) {
            VideoImage value = it.next();
            data.remove(value);
        }
        adapter.notifyDataSetChanged();
	}

	@Override
	protected int getCreateView() {
		return R.layout.fragment_mycollection;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected IPullToRefresh getPullToRefresh() {
		return pullToRefreshGridView;
	}

	@Override
	protected void initContentView(View view) {
		
		initGridView(view);

		if (isLogin()) {
			onPullDownToRefresh(pullToRefreshGridView);
		}
	}
	
	private void initGridView(View view) {

		pullToRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.pulltorefresh);
		pullToRefreshGridView.setMode(Mode.BOTH);
		gridView = pullToRefreshGridView.getRefreshableView();

		adapter = new VideoAdapter(getActivity(), data, this);
        adapter.setDeleteMode(false);
		gridView.setAdapter(adapter);
		
		pullToRefreshGridView.setOnRefreshListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		
		page = 1;
		onPullUpToRefresh(pullToRefreshGridView);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);
		// 视频收藏列表
		DataManager.memberCollectVideoList(getMember_id(), page);
	}
	
	/**
	 * 回调：视频收藏列表
	 */
	public void onEventMainThread(MemberCollectVideoListEntity event) {

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
	 * 回调：视频取消收藏
	 */
	public void onEventMainThread(MemberCancelCollectVideoEntity event) {

		if (event != null) {
			if (event.isResult()) {
				showToastShort(event.getMsg());
			}
		}
	}
	
	/**
	 * 事件：登录
	 */
	public void onEventMainThread(LoginEvent event) {

		onPullDownToRefresh(pullToRefreshGridView);
	}
	
	/**
	 * 事件：注销
	 */
	public void onEventMainThread(LogoutEvent event) {

		onPullDownToRefresh(pullToRefreshGridView);
	}
}
