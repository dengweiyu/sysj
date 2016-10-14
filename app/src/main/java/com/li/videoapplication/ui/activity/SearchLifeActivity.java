package com.li.videoapplication.ui.activity;

import java.util.ArrayList;

import android.widget.ListView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.model.response.Associate201Entity;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.adapter.SearchLiftAdapter;

/**
 * 活动：查找精彩生活
 */
public class SearchLifeActivity extends TBaseActivity {

	// 搜索类型(手机游戏，精彩生活)
	private String type;
	
	// 搜索结果
	private ListView listView;
	private ArrayList<Associate> data = new ArrayList<Associate>();;
	private SearchLiftAdapter adapter;

	@Override
	public void refreshIntent() {
		super.refreshIntent();

		try {
			type = getIntent().getStringExtra("type");
		} finally {

		}
	}

	@Override
	public int getContentView() {
		return R.layout.activity_searchlife;
	}

	@Override
	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();

		setSystemBarBackgroundWhite();
		setAbTitle(R.string.searchlift_title);
	}

	@Override
	public void initView() {
		super.initView();

		listView = (ListView) findViewById(R.id.listview);
		adapter = new SearchLiftAdapter(this, data);
		listView.setAdapter(adapter);
	}

	@Override
	public void loadData() {
		super.loadData();

		// 搜索联想词201
		DataManager.associate210(type, "空");
	}

	/**
	 * 回调：搜索联想词201
	 */
	public void onEventMainThread(Associate201Entity event) {
		if (event.isResult()) {
			if (event.getData().size() > 0) {
				data.clear();
				data.addAll(event.getData());
				adapter.notifyDataSetChanged();
			}
		}
	}
}
