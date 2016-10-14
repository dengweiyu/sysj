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
import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.PhotoPhotoCommentListEntity;
import com.li.videoapplication.data.model.response.VideoCommentLike2Entity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.adapter.VideoPlayCommentAdapter;
import com.li.videoapplication.views.ListViewY1;
/**
 * 碎片：图文详情评论
 */
public class ImageDetailCommentFragment extends TBaseFragment implements OnRefreshListener2<ListView> {
	
	private VideoImage item;
	
	public void setItem(VideoImage item) {
		this.item = item;
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {

				onPullDownToRefresh(null);
			}
		}, 0);
	}
	
	private ListViewY1 mListView;
	private VideoPlayCommentAdapter adapter;
	private List<Comment> data;
	
	private int page = 1;
	
	public List<Comment> getData() {
		return data;
	}

	@Override
	protected int getCreateView() {
		return R.layout.fragment_imagedetail_list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected IPullToRefresh getPullToRefresh() {
		return null;
	}
	
	public void smoothScrollToPosition(int position) {
		mListView.smoothScrollToPosition(position);
	}

	@Override
	protected void initContentView(View view) {
		
		mListView = (ListViewY1) view.findViewById(R.id.listview);

		data = new ArrayList<Comment>();
		/*for (int i = 0; i < 8; i++) {
			data.add(new Comment());
		}*/
		adapter = new VideoPlayCommentAdapter(getActivity(), data);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		
		page = 1;
		onPullUpToRefresh(refreshView);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

		// 图文评论列表
		DataManager.photoPhotoCommentList(item.getPic_id(), page, 10);
		onRefreshCompleteDelayed(PullToRefreshActivity.TIME_REFRESH_SHORT);
	}

	/**
	 * 回调：图文评论列表
	 */
	public void onEventMainThread(PhotoPhotoCommentListEntity event) {
		onRefreshComplete();
		if (event.isResult()) {
			if (event.getData().size() > 0) {
				if (page == 1) {
					data.clear();
				}
				data.addAll(event.getData());
				adapter.notifyDataSetChanged();
				++page;
			}
		}
	}

	/**
	 * 回调：视频评论点赞
	 */
	public void onEventMainThread(VideoCommentLike2Entity event) {

		if (event != null) {
			if (event.isResult()) {
				showToastShort(event.getMsg());
			} else {
				showToastShort(event.getMsg());
			}
		}
	}

}
