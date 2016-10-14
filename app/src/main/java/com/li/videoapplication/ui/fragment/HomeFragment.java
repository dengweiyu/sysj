package com.li.videoapplication.ui.fragment;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Advertisement;
import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.data.model.event.ConnectivityChangeEvent;
import com.li.videoapplication.data.model.response.IndexChangeGuessEntity;
import com.li.videoapplication.data.model.response.IndexChangeGuessSecondEntity;
import com.li.videoapplication.data.model.response.IndexIndex204Entity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.ArrayHelper;
import com.li.videoapplication.tools.RandomUtil;
import com.li.videoapplication.tools.RefreshViewHepler;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.adapter.BannerAdapter;
import com.li.videoapplication.ui.adapter.HomeGroupAdapter;
import com.li.videoapplication.ui.adapter.HotGameAdapter;
import com.li.videoapplication.ui.adapter.HotNarrateAdapter;
import com.li.videoapplication.ui.adapter.YouLikeAdapter;
import com.li.videoapplication.ui.view.BannerView;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.views.CircleFlowIndicator;
import com.li.videoapplication.views.DynamicHeightImageView;
import com.li.videoapplication.views.GridViewY1;
import com.li.videoapplication.views.HorizontalListView;
import com.li.videoapplication.views.RefreshListView;
import com.li.videoapplication.views.RefreshListView.IXListViewListener;
import com.li.videoapplication.views.ViewFlow;
import com.li.videoapplication.views.ViewFlow.ViewSwitchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：首页
 */
public class HomeFragment extends TBaseFragment implements OnClickListener,
        ViewSwitchListener,
        IXListViewListener {

    /* 广告 */
    private View bannerView;
    private ViewFlow bannerFlow;
    private CircleFlowIndicator bannerIndicator;
    private BannerAdapter bannerAdapter;
    private List<Banner> bannerData = new ArrayList<>();

    /* 热门游戏 */
    private View hotGameView;
    private HorizontalListView hotGameListView;
    private HotGameAdapter hotGameAdapter;
    private List<Game> hotGameData = new ArrayList<>();

    /* 广告位 */
    private BannerView banner;

    /* 猜你喜欢 */
    private View youLikeView;
    private GridViewY1 youLikeGridView;
    private YouLikeAdapter youLikeAdapter;
    private List<VideoImage> youLikeData = new ArrayList<>();
    private LinearLayout change;
    private ImageView changeIcon;
    private RotateAnimation changeAnimation;

    /* 视频列表 */
    public RefreshListView refreshListView;
    private HomeGroupAdapter homeGroupAdapter;
    private List<VideoImageGroup> homeGroupData = new ArrayList<>();

    /* 热门解说 */
    private View hotNarrateView;
    private TextView hotNarrateTitle;
    private HorizontalListView hotNarrateListView;
    private HotNarrateAdapter hotNarrateAdapter;
    private List<Member> hotNarrateData = new ArrayList<>();

    /* 页脚 */
    private View footerView;
    private LinearLayout footerContainer;

    private int page = 1;
    private int page_count = 1;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_home;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        startAutoFlowTimer();
        refreshBanner();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "进入首页次数");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bannerFlow != null)
            bannerFlow.destroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoFlowTimer();
    }

    private MainActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.activity = (MainActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initContentView(View view) {

        initRefreshView(view);
        initAdapter();

        refreshListView.refresh();
    }

    private void initRefreshView(View view) {

        refreshListView = (RefreshListView) view.findViewById(R.id.refreeshlist);
        refreshListView.setVisibility(View.VISIBLE);

        refreshListView.setPullLoadEnable(false);
        refreshListView.setPullRefreshEnable(true);
        refreshListView.setXListViewListener(this);
        refreshListView.onHiddenFooterView();
    }

    private boolean isInitAdapter = false;

    private void initAdapter() {
        if (!isInitAdapter) {
            refreshListView.addHeaderView(getBannerView());
            refreshListView.addHeaderView(newEmptyView(7));
            refreshListView.addHeaderView(getHotGameView());
            refreshListView.addHeaderView(newEmptyView(7));
            refreshListView.addHeaderView(getBanner());
            refreshListView.addHeaderView(getYouLikeView());
            refreshListView.addHeaderView(newEmptyView(7));
            refreshListView.addFooterView(getFooterView());

            homeGroupAdapter = new HomeGroupAdapter(getActivity(), homeGroupData);
            refreshListView.setAdapter(homeGroupAdapter);

            isInitAdapter = true;
        }
    }

    private boolean isRefreshing = false;

    @Override
    public void onRefresh() {

        if (isRefreshing)
            return;
        isRefreshing = true;

        if (refreshListView != null) {
            refreshListView.onHiddenFooterView();
        }
        if (footerContainer != null) {
            footerContainer.setVisibility(View.GONE);
        }

        page = 1;
        // 首页
        DataManager.indexIndex204(page);

        refreshBanner();
    }

    @Override
    public void onLoadMore() {

        if (isRefreshing)
            return;
        isRefreshing = true;

        if (refreshListView != null) {
            refreshListView.onVisibleFooterView();
        }

        // 首页
        DataManager.indexIndex204(page);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.youlike_change:// 猜你喜欢 换一换
                changeIcon.startAnimation(getChangeAnimation());
                List<String> videoIds = PreferencesHepler.getInstance().getVideoIds();
                if (PreferencesHepler.getInstance().canVideoIdsTime()) {// 已过期
                    // 首页猜你喜歡
                    DataManager.indexChangeGuess(PreferencesHepler.getInstance().getGroupIds2());
                } else {
                    if (videoIds != null && videoIds.size() > 0) {
                        // 首页猜你喜歡详情
                        DataManager.indexChangeGuessSecond(getVideoIdsRandom(videoIds));
                    } else {
                        // 首页猜你喜歡
                        DataManager.indexChangeGuess(PreferencesHepler.getInstance().getGroupIds2());
                    }
                }
                UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "换一换");
                break;

            case R.id.hotgame_button:// 热门游戏
                if (activity != null) {
                    activity.locationAtGame();
                }
                break;
        }
    }

    /**
     * 换一换动画
     */
    private Animation getChangeAnimation() {

        if (changeAnimation == null) {
            changeAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            changeAnimation.setRepeatMode(Animation.RESTART);
            changeAnimation.setRepeatCount(1);
            changeAnimation.setDuration(1200);
            changeAnimation.setFillAfter(false);// 动画执行完后是否停留在执行完的状态
        }
        return changeAnimation;
    }

    private String getVideoIdsRandom(List<String> videoIds) {

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            int index = RandomUtil.getRandom(0, videoIds.size() - 1);
            list.add(videoIds.get(index));
        }
        return ArrayHelper.list2Array(list);
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
            bannerAdapter = new BannerAdapter(getActivity(), bannerData, BannerAdapter.PAGER_HOME);
            bannerFlow.setAdapter(bannerAdapter);

            bannerFlow.setOnViewSwitchListener(this);
        }
        return bannerView;
    }

    private void setBannerView(View view) {
        // 320/180
        // 16/9
        // 160/320
        int w = srceenWidth;
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
     * 热门游戏
     */
    private View getHotGameView() {

        if (hotGameView == null) {
            hotGameView = inflater.inflate(R.layout.header_home_hotgame, null);
            hotGameView.findViewById(R.id.hotgame_button).setOnClickListener(this);
            hotGameListView = (HorizontalListView) hotGameView.findViewById(R.id.horizontallistvierw);
            setHotGameView(hotGameListView);
            hotGameAdapter = new HotGameAdapter(getActivity(), hotGameData);
            hotGameListView.setAdapter(hotGameAdapter);
        }
        return hotGameView;
    }

    private void setHotGameView(View view) {
        // 72
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) hotGameListView.getLayoutParams();
        int h = ScreenUtil.dp2px(72);
        params.height = h;
        hotGameListView.setLayoutParams(params);
    }

    /**
     * 广告为
     */
    private View getBanner() {
        if (banner == null) {
            banner = new BannerView(getActivity());
            banner.refreshData(DataManager.ADVERTISEMENT.ADVERTISEMENT_8);
        }
        return banner;
    }

    private void refreshBanner() {
        if (banner != null)
            banner.refreshData(DataManager.ADVERTISEMENT.ADVERTISEMENT_8);
    }

    /**
     * 猜你喜欢
     */
    private View getYouLikeView() {

        if (youLikeView == null) {
            youLikeView = inflater.inflate(R.layout.header_home_youlike, null);
            youLikeGridView = (GridViewY1) youLikeView.findViewById(R.id.gridview);
            change = (LinearLayout) youLikeView.findViewById(R.id.youlike_change);
            changeIcon = (ImageView) youLikeView.findViewById(R.id.youlike_change_icon);
            change.setOnClickListener(this);
            youLikeAdapter = new YouLikeAdapter(getActivity(), youLikeData);
            youLikeGridView.setAdapter(youLikeAdapter);
        }
        return youLikeView;
    }

    /**
     * 热门解说
     */
    private View getHotNarrateView() {

        if (hotNarrateView == null) {
            hotNarrateView = inflater.inflate(R.layout.header_home_hotnarrate, null);
            hotNarrateTitle = (TextView) hotNarrateView.findViewById(R.id.home_hotnarrate_title);
            hotNarrateTitle.setText("热门解说");
            hotNarrateListView = (HorizontalListView) hotNarrateView.findViewById(R.id.horizontallistvierw);
            setHotNarrateView(hotNarrateListView);
            hotNarrateData = new ArrayList<Member>();
            hotNarrateAdapter = new HotNarrateAdapter(getActivity(), hotNarrateData);
            hotNarrateListView.setAdapter(hotNarrateAdapter);
        }
        return hotNarrateView;
    }

    private void setHotNarrateView(HorizontalListView view) {
        // 72
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        int h = ScreenUtil.dp2px(72);
        params.height = h;
        view.setLayoutParams(params);
    }

    /**
     * 页脚
     */
    private View getFooterView() {

        if (footerView == null) {
            footerView = inflater.inflate(R.layout.footer_home_end, null);
            footerContainer = (LinearLayout) footerView.findViewById(R.id.home_container);
            DynamicHeightImageView image = (DynamicHeightImageView) footerView.findViewById(R.id.home_image);
            image.setHeightRatio(0.36);
            footerContainer.setVisibility(View.GONE);
        }
        return footerView;
    }

    /**
     * 回调：統一接口
     */
    public void onEventMainThread(IndexIndex204Entity event) {

        if (event.isResult() && event.getData() != null) {
            RefreshViewHepler.setRefreshTime(refreshListView);
            page_count = event.getData().getPage_count();
            isFirstRefresh = true;
            if (page == 1) {// 第一页
                if (event.getData() != null &&
                        event.getData().getBanner() != null &&
                        event.getData().getBanner().size() > 0) {// 广告
                    refreshBanner(event.getData().getBanner());
                }
                if (event.getData() != null &&
                        event.getData().getHotGame() != null &&
                        event.getData().getHotGame().getList().size() > 0) {// 热门游戏
                    refreshHotGame(event.getData().getHotGame().getList());
                }
                if (event.getData() != null &&
                        event.getData().getGuessVideo() != null &&
                        event.getData().getGuessVideo().getList().size() > 0) {// 猜你喜歡
                    refreshYouLike(event.getData().getGuessVideo().getList());
                }
                if (event.getData() != null &&
                        event.getData().getSysjVideo() != null &&
                        event.getData().getSysjVideo().getList() != null &&
                        event.getData().getSysjVideo().getList().size() > 0) {// 视界原创
                    List<VideoImageGroup> list = new ArrayList<>();
                    list.add(event.getData().getSysjVideo());
                    refreshGroup(list);
                }
                if (event.getData() != null &&
                        event.getData().getHotMemberVideo() != null &&
                        event.getData().getHotMemberVideo().getList().size() > 0) {// 热门解说
                    refreshHotNarrate(event.getData().getHotMemberVideo().getList());
                }
                ++page;

                Log.d(tag, "onEventMainThread: 1");
                refreshListView.setPullLoadEnable(true);

                refreshListView.stopRefresh();
                refreshListView.stopLoadMore();

                refreshListView.onHiddenFooterView();
                footerContainer.setVisibility(View.GONE);
                Log.d(tag, "onEventMainThread: 2");
            } else if (page == page_count) {// 最后一页

                refreshListView.setPullLoadEnable(false);

                refreshListView.stopRefresh();
                refreshListView.stopLoadMore();

                refreshListView.onHiddenFooterView();
                footerContainer.setVisibility(View.VISIBLE);
            } else {
                if (event.getData() != null &&
                        event.getData().getVideoList() != null &&
                        event.getData().getVideoList().size() > 0) {// 游戏类型视频
                    refreshGroup(event.getData().getVideoList());
                    ++page;
                }

                refreshListView.setPullLoadEnable(true);

                refreshListView.stopRefresh();
                refreshListView.stopLoadMore();

                refreshListView.onHiddenFooterView();
                footerContainer.setVisibility(View.GONE);
            }
        } else {

            refreshListView.setPullLoadEnable(true);

            refreshListView.stopRefresh();
            refreshListView.stopLoadMore();

            refreshListView.onHiddenFooterView();
            footerContainer.setVisibility(View.GONE);
        }
        isRefreshing = false;
    }

    private int refreshBannerTime = 0;
    private int refreshHotGameTime = 0;

    /**
     * 刷新广告
     */
    private void refreshBanner(List<Banner> list) {
        if (refreshBannerTime < 1) {
            bannerAdapter.setMaxValue(false);
            stopAutoFlowTimer();
            bannerData.clear();
            bannerAdapter.notifyDataSetChanged();
            Banner banner = getAdvertisement();
            if (banner != null) {// 第四幅为广告位
                if (list.size() > 3) {
                    bannerData.add(banner);
                    list.subList(0, 3);
                    bannerData.addAll(list);
                } else {
                    bannerData.add(banner);
                    bannerData.addAll(list);
                }
            } else {
                bannerData.addAll(list);
            }
            bannerAdapter.notifyDataSetChanged();
            startAutoFlowTimer();
            ++refreshBannerTime;
        }
    }

    private Banner getAdvertisement() {
        Advertisement advertisement = PreferencesHepler.getInstance().getAdvertisement_7();
        if (advertisement != null &&
                advertisement.getFlag() != null &&
                TimeHelper.isBannerValid(advertisement.getStarttime(), advertisement.getEndtime())) {
            Banner banner = new Banner();
            banner.setFlagPath(advertisement.getFlag());
            banner.setType(Banner.TYPE_URL);
            return banner;
        }
        return null;
    }

    /**
     * 刷新热门游戏
     */
    private void refreshHotGame(List<Game> list) {
        if (refreshHotGameTime < 1) {
            hotGameData.clear();
            hotGameData.addAll(list);
            hotGameAdapter.notifyDataSetChanged();
            ++refreshHotGameTime;
        }
    }

    /**
     * 刷新猜你喜欢
     */
    private void refreshYouLike(List<VideoImage> list) {
        youLikeData.clear();
        youLikeData.addAll(list);
        youLikeAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新主页
     */
    private void refreshGroup(List<VideoImageGroup> list) {

        if (page == 1) {
            homeGroupData.clear();
            for (VideoImageGroup group : list) {
                group.setType(VideoImageGroup.TYPE_FIRST);
            }
        } else {
            for (VideoImageGroup group : list) {
                group.setType(VideoImageGroup.TYPE_THIRD);
            }
        }
        homeGroupData.addAll(list);
        homeGroupAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新热门解说
     */
    private void refreshHotNarrate(List<Member> list) {

        VideoImageGroup item = new VideoImageGroup();
        item.setType(VideoImageGroup.TYPE_SECOND);
        item.setMembers(list);
        homeGroupData.add(item);
        homeGroupAdapter.notifyDataSetChanged();
    }

    /**
     * 回调：首页猜你喜歡
     */
    public void onEventMainThread(IndexChangeGuessEntity event) {
        if (event != null) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {// 猜你喜歡
                    youLikeData.clear();
                    youLikeData.addAll(event.getData().getList());
                    youLikeAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 回调：首页猜你喜歡详情
     */
    public void onEventMainThread(IndexChangeGuessSecondEntity event) {
        if (event != null) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {// 猜你喜歡
                    youLikeData.clear();
                    youLikeData.addAll(event.getData().getList());
                    youLikeAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private boolean isFirstRefresh = false;

    /**
     * 回调：网络变化事件
     */
    public void onEventMainThread(ConnectivityChangeEvent event) {

        if (isFirstRefresh == false)
            refreshListView.refresh();
    }
}
