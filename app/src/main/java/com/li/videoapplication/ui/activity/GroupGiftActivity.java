package com.li.videoapplication.ui.activity;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.data.model.response.GamePackage203Entity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.ui.adapter.GiftAdapter;

/**
 * 活动：游戏圈子礼包
 */
public class GroupGiftActivity extends PullToRefreshActivity<Gift> {

	private GiftAdapter adapter;

	private Game game;

	@Override
	public void refreshIntent() {
		super.refreshIntent();
		try {
			game = (Game) getIntent().getSerializableExtra("game");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (game == null) {
			finish();
		}
	}

	@Override
	public int getContentView() {
		return R.layout.activity_groupgift;
	}

	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();

		setSystemBarBackgroundWhite();
		setAbTitle(game.getGroup_name());
	}

	@Override
	public void initView() {
		super.initView();

		adapter = new GiftAdapter(this, data);
		setAdapter(adapter);
	}

	@Override
	public void loadData() {
		super.loadData();

		onPullDownToRefresh(pullToRefreshListView);
	}

	@Override
	public void onRefresh() {

		// 圈子礼包203
		DataManager.gamePackage203(game.getGame_id(), getMember_id(), page);
	}

	@Override
	public void onLoadMore() {

		// 圈子礼包203
		DataManager.gamePackage203(game.getGame_id(), getMember_id(), page);
	}

	/**
	 * 回调：礼包列表
	 */
	public void onEventMainThread(GamePackage203Entity entity) {

		if (entity.isResult()) {
			if (entity.getData().getList().size() > 0) {
				if (page == 1) {
					data.clear();
				}
				data.addAll(entity.getData().getList());
				adapter.notifyDataSetChanged();
				++ page;
			}
		}
		isRefreshing = false;
		refreshComplete();
	}
}
