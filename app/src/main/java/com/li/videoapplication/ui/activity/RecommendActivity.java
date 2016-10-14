package com.li.videoapplication.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.EditBanner203Entity;
import com.li.videoapplication.data.model.response.EditGoldMember203Entity;
import com.li.videoapplication.data.model.response.EditList203Entity;
import com.li.videoapplication.framework.PullToRefreshActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.adapter.BannerAdapter;
import com.li.videoapplication.ui.adapter.HotNarrateAdapter;
import com.li.videoapplication.ui.adapter.RecommendAdapter;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.views.CircleFlowIndicator;
import com.li.videoapplication.views.HorizontalListView;
import com.li.videoapplication.views.ViewFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动：精彩推荐
 */
public class RecommendActivity extends PullToRefreshActivity<VideoImage> implements ViewFlow.ViewSwitchListener,View.OnClickListener {

	/**
	 * 跳转：风云榜
	 */
	private void startBillboardActivity() {
		ActivityManeger.startBillboardActivity(this);
		UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "精彩推荐-金牌主播更多");
	}

	/* 广告 */
	private View bannerView;
	private ViewFlow bannerFlow;
	private CircleFlowIndicator bannerIndicator;
	private BannerAdapter bannerAdapter;
	private List<Banner> bannerData = new ArrayList<>();

	/*视频*/
	private RecommendAdapter adapter;

	/* 金牌主播 */
	private View hotNarrateView;
	private TextView hotNarrateTitle;
	private HorizontalListView hotNarrateListView;
	private HotNarrateAdapter hotNarrateAdapter;
	private List<Member> hotNarrateData = new ArrayList<>();

	@Override
	public int getContentView() {
		return R.layout.activity_recommend;
	}

	public int inflateActionBar() {
		return R.layout.actionbar_second;
	}

	@Override
	public void afterOnCreate() {
		super.afterOnCreate();
		setSystemBarBackgroundWhite();
		setAbTitle(R.string.recommend_title);
	}

	@Override
	public void initView() {
		super.initView();

		setModeEnd();
		addHeaderView(getBannerView());
		addHeaderView(newEmptyView(7));
		addHeaderView(getHotNarrateView());

		adapter = new RecommendAdapter(this, data);
		setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bannerFlow != null)
			bannerFlow.destroy();
	}

	@Override
	public void loadData() {
		super.loadData();

		// 精彩推荐--banner
		DataManager.editBanner203();

		// 精彩推荐--金牌主播
		DataManager.editGoldMember203();

		doRequest();
	}

	@Override
    public void onResume() {
        super.onResume();
        startAutoFlowTimer();
		UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.DISCOVER, "精彩推荐");
    }

    @Override
	public void onPause() {
		super.onPause();
		stopAutoFlowTimer();
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

		// 精彩推荐--视频列表
		DataManager.editList203(getMember_id(), page);
	}

	/**
	 * 广告
	 */
	private View getBannerView() {
		if (bannerView == null) {
			bannerView = inflater.inflate(R.layout.header_home_banner, null);
			setBannerView(bannerView);
			bannerFlow = (ViewFlow) bannerView.findViewById(R.id.viewflow);
			bannerIndicator = (CircleFlowIndicator) bannerView.findViewById(R.id.circleflowindicator);
			bannerFlow.setSideBuffer(4); // 实际图片张数
			bannerFlow.setFlowIndicator(bannerIndicator);
			bannerFlow.setTimeSpan(4000);
			bannerFlow.setSelection(0); // 设置初始位置
			bannerAdapter = new BannerAdapter(this, bannerData, BannerAdapter.PAGER_RECOMMEND);
			bannerFlow.setAdapter(bannerAdapter);
			bannerFlow.setOnViewSwitchListener(this);
		}
		return bannerView;
	} 
	
	private void setBannerView(View view) {
		// 320/180
		// 16:9
		int w = windowManager.getDefaultDisplay().getWidth();;
		int h = w * 9 / 16;
		setListViewLayoutParams(view, w, h);
	}

	public synchronized void startAutoFlowTimer() {
		if (bannerFlow != null && !bannerFlow.isAutoFlowTimer()) {
			// 自动播放
			bannerFlow.startAutoFlowTimer();
		}
	}

	public synchronized void stopAutoFlowTimer() {
		if (bannerFlow != null && bannerFlow.isAutoFlowTimer()) {
			// 暂停播放
			bannerFlow.stopAutoFlowTimer();
		}
	}

	@Override
	public void onSwitched(View view, int position) {
		bannerAdapter.setMaxValue(true);
		startAutoFlowTimer();
	}

	/**
	 * 金牌主播
	 */
	private View getHotNarrateView() {
		if (hotNarrateView == null) {
			hotNarrateView = inflater.inflate(R.layout.header_home_hotnarrate, null);
			hotNarrateTitle = (TextView) hotNarrateView.findViewById(R.id.home_hotnarrate_title);
			hotNarrateView.findViewById(R.id.home_hotnarrate_more).setOnClickListener(this);

			hotNarrateTitle.setText("金牌主播");
			hotNarrateListView = (HorizontalListView) hotNarrateView.findViewById(R.id.horizontallistvierw);
			setHotNarrateView(hotNarrateListView);
			hotNarrateAdapter = new HotNarrateAdapter(this, hotNarrateData);
			hotNarrateListView.setAdapter(hotNarrateAdapter);
		}
		return hotNarrateView;
	}

	private void setHotNarrateView(HorizontalListView view) {
		// 72
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
		params.height = ScreenUtil.dp2px(72);
		view.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.home_hotnarrate_more:
				startBillboardActivity();
				break;
		}
	}

	/**
	 * 回调：精彩推荐--banner
	 */
	public void onEventMainThread(EditBanner203Entity event) {

		if (event.isResult()) {
			if (event.getData() != null &&
					event.getData().getList() != null &&
					event.getData().getList().size() > 0) {
				refreshBanner(event.getData().getList());
			}
		}
	}

	/**
	 * 回调：精彩推荐--金牌主播
	 */
	public void onEventMainThread(EditGoldMember203Entity event) {

		if (event.isResult()) {
			if (event.getData() != null &&
					event.getData().getList() != null &&
					event.getData().getList().size() > 0) {
				refreshHotNarrate(event.getData().getList());
			}
		}
	}

	/**
	 * 回调：精彩推荐--视频列表
	 */
	public void onEventMainThread(EditList203Entity event) {

		if (event.isResult()) {
			if (event.getData() != null &&
					event.getData().getList() != null &&
					event.getData().getList().size() > 0) {
				if (page == 1) {
					data.clear();
				}
				data.addAll(event.getData().getList());
				adapter.notifyDataSetChanged();
				++page;
			}
		}
		isRefreshing = false;
		refreshComplete();
	}

	/**
	 * 刷新广告
	 */
	private void refreshBanner(List<Banner> list) {
		bannerAdapter.setMaxValue(false);
		stopAutoFlowTimer();
		bannerData.clear();
		bannerAdapter.notifyDataSetChanged();
		bannerData.addAll(list);
		bannerAdapter.notifyDataSetChanged();
		startAutoFlowTimer();
	}

	/**
	 * 刷新金牌主播
	 */
	private void refreshHotNarrate(List<Member> list) {
		hotNarrateData.clear();
		hotNarrateData.addAll(list);
		hotNarrateAdapter.notifyDataSetChanged();
	}
}
