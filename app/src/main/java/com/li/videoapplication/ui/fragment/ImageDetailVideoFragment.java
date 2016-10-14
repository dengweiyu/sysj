package com.li.videoapplication.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.AuthorVideoList2EntityImageDetail;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.adapter.VideoPlayVideoAdapter;
import com.li.videoapplication.views.ListViewY1;
/**
 * 碎片：图文详情玩家视频
 */
public class ImageDetailVideoFragment extends TBaseFragment implements OnRefreshListener2<ListView> {

	private VideoImage item;
	
	public void setItem(VideoImage item) {
		this.item = item;
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {

				onPullDownToRefresh(null);
			}
		}, 200);
	}
	
	private ListViewY1 mListView;
	private VideoPlayVideoAdapter adapter;
	private List<VideoImage> data;
	
	private int page = 1;

	@Override
	protected int getCreateView() {
		return R.layout.fragment_imagedetail_list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected IPullToRefresh getPullToRefresh() {
		return null;
	}

	@Override
	protected void initContentView(View view) {
		
		mListView = (ListViewY1) view.findViewById(R.id.listview);

		data = new ArrayList<VideoImage>();
		adapter = new VideoPlayVideoAdapter(getActivity(), data);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		
		page = 1;
		onPullUpToRefresh(refreshView);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		
		// 用户视频列表2
		DataManager.authorVideoList2(new AuthorVideoList2EntityImageDetail(), item.getMember_id(), page);
		onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);
	}

	/**
	 * 回调:用户视频列表2
	 */
	public void onEventMainThread(AuthorVideoList2EntityImageDetail event) {
		onRefreshComplete();
		if (event.isResult()) {
			if (event.getData().getList().size() > 0) {
				if (page == 1) {
					data.clear();
				}
				data.addAll(event.getData().getList());
				adapter.notifyDataSetChanged();
				++page;
			}
		}
	}
}
