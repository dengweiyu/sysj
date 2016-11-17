package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.Task;
import com.li.videoapplication.data.model.entity.TaskGroup;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.model.response.TaskList203Entity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.tools.ShareSDKShareHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.adapter.MyTaskParentAdapter;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.views.CircleImageView;
/**
 * 活动：我的任务
 */
@SuppressLint("InflateParams")
public class MyTaskActivity extends PullToRefreshActivity<TaskGroup> implements OnClickListener {

    /**
	 * 跳转：个人资料
	 */
	public void startMyPersonalInfoActivity() {
		ActivityManeger.startMyPersonalInfoActivity(this);
	}
	
	/**
	 * 跳转：主页
	 */
	public void startMainActivity() {
        ActivityManeger.startMainActivityNewTask();
	}
	
	/**
	 * 跳转：分享
	 */
	public void startActivityShareActivity4SYSJ() {
		ActivityManeger.startActivityShareActivity4SYSJ(this);
	}

	/**
	 * 做任务
	 */
	public void startActivity(Task record) {

		if (record.getId().equals("10")) {//跳转 搜索页面
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			finish();
		} else if (record.getId().equals("11")) {
			startMainActivity();
			finish();
		} else if (record.getId().equals("12")) {
			startMainActivity();
			finish();
		} else if (record.getId().equals("13")) {
			startMainActivity();
			finish();
		} else if (record.getId().equals("14")) {

		} else if (record.getId().equals("15")) {

		} else if (record.getId().equals("16")) {//新手任务——完善个人资料
			startMyPersonalInfoActivity();
			showToastShort("新手任务：完善个人资料");
			finish();
		} else if (record.getId().equals("17")) {//新手任务——推广手游视界APP
			startActivityShareActivity4SYSJ();
		} else if (record.getId().equals("18")) {
			startMainActivity();
			finish();
		} else if (record.getId().equals("19")) {//每日任务——观看3个视频
			startMainActivity();
			showToastShort("每日任务：观看3个视频");
			finish();
		} else if (record.getId().equals("20")) {//每日任务——收藏1个视频
			startMainActivity();
			showToastShort("每日任务：收藏1个视频");
			finish();
		} else if (record.getId().equals("21")) {//每日任务——给1部视频点赞
			startMainActivity();
			showToastShort("每日任务：给1部视频点赞");
			finish();
		} else if (record.getId().equals("22")) {//每日任务——分享2个视频给好友
			startMainActivity();
			showToastShort("每日任务：分享2个视频给好友");
			finish();
		}
	}
	
	private CircleImageView head;
	private TextView name;
	private TextView level;
	private TextView levelText ;
	private ProgressBar levelBar;

	private View headerView;
	private RelativeLayout container;
	private ImageView quection;

	private MyTaskParentAdapter adapter;

	@Override
	public int getContentView() {
		return R.layout.activity_mytask;
	}

	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();
		ShareSDKShareHelper.initSDK(this);

		setSystemBarBackgroundWhite();
		setAbTitle(R.string.mytask_title);
	}

	@Override
	public void initView() {
		super.initView();

		setModeDisabled();
	}

	@Override
	public void loadData() {
		super.loadData();

		onPullDownToRefresh(pullToRefreshListView);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDKShareHelper.stopSDK(this);
	}

	@Override
	public void onRefresh() {
		doRequest();
	}

	@Override
	public void onLoadMore() {
		doRequest();
	}

	private void doRequest() {

		// 任务列表203
		DataManager.taskList203(getMember_id());
	}

	@Override
	public void onClick(View v) {

		if (v == quection) {
			DialogManager.showMyPersonalInfoLevelDialog(this);
			UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SLIDER, "我的任务-帮助");
		}

		if (v == headerView) {
			startMyPersonalInfoActivity();
		}
	}
	
	public View getHeaderView() {
		if (headerView == null) {
			headerView = inflater.inflate(R.layout.header_mytask, null);
			container = (RelativeLayout) headerView.findViewById(R.id.container);
			head = (CircleImageView) headerView.findViewById(R.id.mytask_head);
			name = (TextView) headerView.findViewById(R.id.mytask_name);
			quection = (ImageView) headerView.findViewById(R.id.mytask_question);
			quection.setOnClickListener(this);
			level = (TextView) headerView.findViewById(R.id.mytask_level);
			levelText = (TextView) headerView.findViewById(R.id.mytask_level_text);
			levelBar = (ProgressBar) headerView.findViewById(R.id.mytask_level_bar);
			headerView.setOnClickListener(this);
			
			setHeaderInfoLayoutParams(66);
		}
		return headerView;
	}
	
	private void setHeaderInfoLayoutParams(int height) {
		if (headerView != null) {
			int pxHeight = ScreenUtil.dp2px(height);
			ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, pxHeight);
			headerView.setLayoutParams(params);
		}
	}
	
	private void showHeaderInfo(Member item) {
		
		if (isLogin()) {
			container.setVisibility(View.VISIBLE);
			setImageViewImageNet(head, item.getAvatar());
			setTextViewText(name, item.getName());
			
			setLevelBar(item);
		} else {
			container.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 进度条
	 */
	public void setLevelBar(Member item) {
		setTextViewText(level, "Lv." + item.getDegree());
		setTextViewText(levelText, item.getRank() + "/" + item.getNext_exp());
		levelBar.setMax(100);
		int max = levelBar.getMax();
		int r = Integer.valueOf(item.getRank());
		int e = item.getNext_exp();
		int progress = max*r/e;
		levelBar.setProgress(progress);
	}

	/**
	 * 回调：任务列表203
	 */
	public void onEventMainThread(TaskList203Entity entity) {

		if (entity.isResult()) {
			if (entity.getData().size() > 0) {
				data.clear();
				data.addAll(entity.getData());
				if (isLogin()) {
					// addHeaderView(newEmptyView(8));
					// addHeaderView(getHeaderView());
					// addFooterView(newEmptyView(24));
				}
				adapter = new MyTaskParentAdapter(this, data);
				setAdapter(adapter);

				if (isLogin()) {
					// showHeaderInfo(getUser());
				}
				adapter.notifyDataSetChanged();
			}
		}
		isRefreshing = false;
		refreshComplete();
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
