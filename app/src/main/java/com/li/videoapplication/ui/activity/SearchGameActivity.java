package com.li.videoapplication.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.data.model.response.Associate201Entity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.ui.adapter.SearchGameAdapter;
import com.li.videoapplication.utils.InputUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;

/**
 * 活动：查找游戏
 */
@SuppressLint("HandlerLeak")
public class SearchGameActivity extends TBaseActivity implements TextWatcher, OnClickListener {
    
    private String getAbSearchEdit() {
		return abSearchEdit.getText().toString().trim();
    }
    
	// 搜索结果
	private ArrayList<Associate> data;
	private SearchGameAdapter adapter;
	private ListView mListView;
	
	// 头部
	private View headerView;
	private TextView title;
	
	// 搜索类型(手机游戏，精彩生活)
	private String type;

	@Override
	public void refreshIntent() {
		super.refreshIntent();

		try {
			type = getIntent().getStringExtra("type");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getContentView() {
		return R.layout.activity_searchgame;
	}

	@Override
	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();

		setSystemBarBackgroundWhite();
	}

	@Override
	public void initView() {
		super.initView();

		initSearchView();
		initListView();

		refreshHistoty();
	}

	@Override
	public void loadData() {
		super.loadData();

		// 搜索联想词201
		DataManager.associate210(type, getAbSearchEdit());
	}

	private void initSearchView() {
		
		abSearchContainer.setVisibility(View.VISIBLE);
		abSearchDelete.setVisibility(View.GONE);
		abSearchSubmit.setEnabled(false);
		abSearchDelete.setOnClickListener(this);
		//设置输入多少字符后提示，默认值为1
		abSearchEdit.setThreshold(1);
		abSearchEdit.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {

		case R.id.ab_search_delete:
			abSearchEdit.setText("");
            InputUtil.showKeyboard(abSearchEdit);
			break;
		}
	}
	
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
    	
    	if (!StringUtil.isNull(getAbSearchEdit())) {
    		// 搜索联想词201
    		DataManager.associate210(type, getAbSearchEdit());
		}
    	
        if (StringUtil.isNull(getAbSearchEdit())) {
        	abSearchDelete.setVisibility(View.GONE);
        } else {
            abSearchDelete.setVisibility(View.VISIBLE);
        }
    }

	private void initListView() {
		
		mListView = (ListView) findViewById(R.id.listview);
		mListView.addHeaderView(getHeaderView());
		data = new ArrayList<>();
		adapter = new SearchGameAdapter(this, data);
		mListView.setAdapter(adapter);
	}
	
	public View getHeaderView() {
		if (headerView == null) {
			headerView = inflater.inflate(R.layout.header_searchgame, null);
			title = (TextView) headerView.findViewById(R.id.searchgame_title);
			title.setText("");
			
			setListViewLayoutParams(headerView, 38);
		}
		return headerView;
	}
	
	private void refreshHistoty() {
		
		List<Associate> list = PreferencesHepler.getInstance().getAssociate201List();
		if (list != null && list.size() > 0) {
			setTitleHistory();
			data.addAll(list);
			adapter.notifyDataSetChanged();
		}
	}
	
	public void setTitleHistory() {
		title.setText("搜索过的记录");
	}
	
	public void setTitleSearch() {
		StringBuffer sb = new StringBuffer();
		sb.append("搜索游戏");
		sb.append("\"");
		sb.append(TextUtil.toColor(getAbSearchEdit(), "#2c93f7"));
		sb.append("\"");
		title.setText(Html.fromHtml(sb.toString()));
	}

	/**
	 * 回调：搜索联想词201
	 */
	public void onEventMainThread(Associate201Entity event) {

		if (event != null) {
			if (event.isResult()) {
				if (event.getData().size() > 0) {
					data.clear();
			        data.addAll(event.getData());
			        setTitleSearch();
			        adapter.notifyDataSetChanged();
				}
			}
		}
	}
}
