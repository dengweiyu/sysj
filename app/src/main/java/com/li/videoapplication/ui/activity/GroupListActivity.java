package com.li.videoapplication.ui.activity;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.GameModuleEntity;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.response.GroupList2Entity;
import com.li.videoapplication.data.model.response.GroupType210Entity;
import com.li.videoapplication.data.model.response.GroupType2Entity;
import com.li.videoapplication.data.model.response.GroupTypeEntity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.ui.adapter.GroupListAdapter;

/**
 * 活动：游戏圈子列表
 */
public class GroupListActivity extends PullToRefreshActivity<Game> {

	private GroupListAdapter adapter;
	public GroupType groupType;


	@Override
	public void refreshIntent() {
		super.refreshIntent();
		try {
			groupType = (GroupType) getIntent().getSerializableExtra("groupType");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (groupType == null) {
			finish();
		}
	}

	@Override
	public int getContentView() {
		return R.layout.activity_grouplist;
	}

	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void beforeOnCreate() {
		super.beforeOnCreate();
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();
		setSystemBarBackgroundWhite();
		setAbTitle(groupType.getGroup_type_name());
	}

	@Override
	public void initView() {
		super.initView();
		adapter = new GroupListAdapter(this,data);
		setAdapter(adapter);
	}

	@Override
	public void loadData() {
		super.loadData();

		onPullDownToRefresh(pullToRefreshListView);
	}

	@Override
	public void onRefresh() {

		// 游戏圈子列表
		DataManager.groupList2(page, groupType.getGroup_type_id(), getMember_id());
	}

	@Override
	public void onLoadMore() {

		// 游戏圈子列表
		DataManager.groupList2(page, groupType.getGroup_type_id(), getMember_id());
	}

	/**
	 * 回调:游戏圈子列表
	 */
	public void onEventMainThread(GroupList2Entity event) {

		if (event.isResult()) {
			if (event.getAData().getList() != null && event.getAData().getList().size() > 0) {
				if (page == 1) {
					data.clear();
				}
				data.addAll(event.getAData().getList());
				adapter.notifyDataSetChanged();
				++page;
			}
			isRefreshing = false;
			refreshComplete();
		}
	}
}
