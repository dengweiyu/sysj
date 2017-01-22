package com.li.videoapplication.mvp.home.view;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.data.model.entity.LaunchImage;
import com.li.videoapplication.data.model.event.ConnectivityChangeEvent;
import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.response.UnfinishedTaskEntity;
import com.li.videoapplication.mvp.adapter.HomeMultipleAdapter;
import com.li.videoapplication.data.model.entity.AdvertisementDto;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.mvp.billboard.view.BillboardActivity;
import com.li.videoapplication.mvp.home.HomeContract.IHomePresenter;
import com.li.videoapplication.mvp.home.HomeContract.IHomeView;
import com.li.videoapplication.mvp.home.presenter.HomePresenter;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.tools.ArrayHelper;
import com.li.videoapplication.tools.DownloadHelper;
import com.li.videoapplication.tools.RandomUtil;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.ui.adapter.BannerAdapter;
import com.li.videoapplication.ui.view.HomeTaskView;
import com.li.videoapplication.utils.HareWareUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleFlowIndicator;
import com.li.videoapplication.views.DynamicHeightImageView;
import com.li.videoapplication.views.ViewFlow;
import com.li.videoapplication.views.ViewFlow.ViewSwitchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * View层：首页
 */
public class HomeFragment extends TBaseFragment implements IHomeView,
        OnRefreshListener,
        RequestLoadMoreListener,
        ViewSwitchListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.home_task)
    HomeTaskView taskView;

    private int page = 1;
    private int page_count;

    private IHomePresenter presenter;
    public HomeMultipleAdapter homeAdapter;
    private List<HomeDto> homeData;

    //顶部广告
    private View bannerView;
    private List<Banner> bannerData;
    private ViewFlow bannerFlow;
    private BannerAdapter bannerAdapter;
    //页尾
    private View footerView;
    //首页加载了2次数据（缓存一次），所以会insert2次，控制只插入1条广告
    private boolean noAdvertisement = true;

    /**
     * 跳转：首页更多
     */
    private void startHomeMoreActivity(VideoImageGroup group) {
        ActivityManeger.startHomeMoreActivity(getContext(), group);
    }

    /**
     * 跳转：风云榜
     */
    private void startBillboardActivity() {
        ActivityManeger.startBillboardActivity(getContext(), BillboardActivity.TYPE_PLAYER);
        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MAIN, "热门主播更多");
    }

    /**
     * 页面跳转：广告跳转
     */
    private void startWebActivity(String url) {

        WebActivity.startWebActivity(getContext(), url);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "进入首页次数");
        }
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
    protected int getCreateView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initContentView(View view) {
        initRecyclerView();

        initAdapter();

        loadCacheData();

        addOnClickListener();
    }

    private void loadCacheData() {
        Log.d(tag, "------------ loadCacheData: ------------");
        HomeDto homeData = PreferencesHepler.getInstance().getHomeData();
        if (homeData == null) {
            //初始化时使用缓存（rxcache缓存）
            presenter.loadHomeData(page, false);
            // 任务初始化时使用缓存（rxcache缓存）
            presenter.unfinishedTask(getMember_id(), false);
        } else {
            //直接调用本地缓存回调（sp缓存）
            refreshHomeData(homeData);
        }
    }

    private void initRecyclerView() {
        presenter = HomePresenter.getInstance();
        presenter.setHomeView(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initAdapter() {
        homeData = new ArrayList<>();

        homeAdapter = new HomeMultipleAdapter(homeData);
        homeAdapter.setOnLoadMoreListener(this);
        //预加载：当列表滑动到倒数第1个Item的时候回调onLoadMoreRequested方法
        homeAdapter.setAutoLoadMoreSize(2);

        bannerData = new ArrayList<>();
        homeAdapter.addHeaderView(getBannerView(bannerData));

        recyclerView.setAdapter(homeAdapter);
        View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptyview_main,
                (ViewGroup) recyclerView.getParent(), false);
        homeAdapter.setEmptyView(emptyView);
    }

    private View getBannerView(List<Banner> bannerData) {
        if (bannerView == null) {
            bannerView = inflater.inflate(R.layout.header_home_banner, null);
            setBannerView(bannerView);
            bannerFlow = (ViewFlow) bannerView.findViewById(R.id.viewflow);
            CircleFlowIndicator bannerIndicator = (CircleFlowIndicator) bannerView.findViewById(R.id.circleflowindicator);
            bannerFlow.setSideBuffer(4); // 初始化轮播图张数
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
        //750:350 = 15:7
        int w = srceenWidth;
        int h = w * 7 / 15;
        setListViewLayoutParams(view, w, h);
    }

    private void addOnClickListener() {
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HomeDto item = (HomeDto) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.hometype_youlike_change://换一换
                        List<String> videoIds = PreferencesHepler.getInstance().getVideoIds();
                        if (PreferencesHepler.getInstance().canVideoIdsTime()) {// id已过期
                            // 首页猜你喜歡 换一换（问卷id）带50个猜你喜欢视频id
                            presenter.changeGuess(PreferencesHepler.getInstance().getGroupIds2());
                        } else {// id未过期
                            if (videoIds != null && videoIds.size() > 0) {
                                // 首页猜你喜歡详情
                                presenter.changeGuessSecond(getVideoIdsRandom(videoIds));
                            } else {
                                // 首页猜你喜歡
                                presenter.changeGuess(PreferencesHepler.getInstance().getGroupIds2());
                            }
                        }
                        UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MAIN, "换一换");
                        break;
                    case R.id.homehotgame_more://热门游戏更多
                        if (activity != null) {
                            activity.locationAtGame();
                        }
                        break;
                    case R.id.hometype_sysj://视界原创更多
                        startHomeMoreActivity(item.getSysjVideo());
                        break;
                    case R.id.hometype_game://游戏视频更多
                        startHomeMoreActivity(item.getVideoGroupItem());
                        break;
                    case R.id.home_hotnarrate_more://热门解说更多
                        startBillboardActivity();
                        break;
                    case R.id.banner_delete://通栏广告关闭
                        homeAdapter.remove(1);
                        break;
                    case R.id.banner_image://通栏广告
                        LaunchImage launchImage = item.getAdvertisement().getData().get(0);
                        int ad_type = launchImage.getAd_type();
                        String download_android = launchImage.getDownload_android();
                        switch (ad_type) {
                            case 1://页面展示
                                String go_url = launchImage.getGo_url();
                                if (!StringUtil.isNull(go_url))
                                    startWebActivity(go_url);
                                else
                                    startWebActivity(download_android);
                                break;
                            case 2://文件下载
                                String app_name = launchImage.getDownload_desc().get(0).getApp_name();

                                Download download = new Download();
                                download.setDownload_url(download_android);
                                download.setTitle(app_name);

                                DownloadHelper.downloadFile(getActivity(), download);
                                break;
                        }
                        // 广告点击统计+1
                        presenter.adClick(launchImage.getAd_id(), AdvertisementDto.AD_CLICK_STATUS_11,
                                HareWareUtil.getHardwareCode());
                        break;
                }
            }
        });
    }

    private String getVideoIdsRandom(List<String> videoIds) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            int index = RandomUtil.getRandom(0, videoIds.size() - 1);
            list.add(videoIds.get(index));
        }
        return ArrayHelper.list2Array(list);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    public void onRefresh() {
        Log.d(tag, "------------ onRefresh: ------------");
        homeAdapter.removeAllFooterView();
        noAdvertisement = true;
        page = 1;
        if (NetUtil.isConnect()) {
            // 加载首页
            presenter.loadHomeData(page, true);
            // 任务
            presenter.unfinishedTask(getMember_id(), true);
        } else {
            ToastHelper.s(R.string.net_disable);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        Log.d(tag, "onLoadMoreRequested: ");
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (page <= page_count) {
                    presenter.loadHomeData(page, true);
                } else {
                    // 数据全部加载完毕
                    homeAdapter.addFooterView(getFootView());
                    homeAdapter.loadMoreEnd();
                }
            }
        });
    }

    private View getFootView() {
        if (footerView == null) {
            footerView = inflater.inflate(R.layout.footer_home_end, null);
            DynamicHeightImageView image = (DynamicHeightImageView) footerView.findViewById(R.id.home_image);
            image.setHeightRatio(0.36);
        }
        return footerView;
    }

    @Override
    public void hideProgress() {
        Log.d(tag, "======== hideProgress: ========");
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        homeAdapter.loadMoreComplete();//加载完成
    }

    //首页每日任务
    @Override
    public void refreshUnFinishTaskView(UnfinishedTaskEntity data) {
        Log.d(tag, "======== refreshUnFinishTaskView: ========");
        if (data.getNum() > 0) {
            long lastTime4Task = PreferencesHepler.getInstance().getTaskTime();
            try {
                long currentTime = TimeHelper.getCurrentTime();
                //上次保存时间与当前时间不是同一天，则显示任务提示条
                if (!TimeHelper.isSameDay(lastTime4Task, currentTime)) {
                    taskView.appear();
                    //保存当前时间
                    PreferencesHepler.getInstance().saveTaskTime(currentTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //首页猜你喜欢（换一换）、猜你喜欢详情
    @Override
    public void refreshChangeGuessView(ChangeGuessEntity data) {
        Log.d(tag, "======== refreshChangeGuessView: ========");
        if (data.getVideoIds() != null) {
            // 保存50个猜你喜歡视频id
            PreferencesHepler.getInstance().saveVideoIds(data.getVideoIds());
            // 保存猜你喜歡视频保存的时间
            PreferencesHepler.getInstance().saveVideoIdsTime();
        }
        homeAdapter.changeGuessVideo(data);
    }

    //广告
    @Override
    public void refreshAdvertisementView(AdvertisementDto data) {
        Log.d(tag, "======== refreshAdvertisementView: ========");
        Log.d(tag, "Advertisement data == " + data);
        if (data.isResult() && noAdvertisement) {
            homeAdapter.addData(1, new HomeDto(HomeDto.TYPE_AD, data));
            noAdvertisement = false;
        }
    }

    //主页数据
    @Override
    public void refreshHomeData(HomeDto data) {
        Log.d(tag, "======== refreshHomeData: ========");
        Log.d(tag, "Home Data: " + data);
        if (data != null) {
            page_count = data.getPage_count();
            if (page == 1) {
                homeData.clear();
                if (data.getBanner() != null && data.getBanner().size() > 0) {
                    refreshBanner(data.getBanner());
                }
                if (data.getHotGame() != null && data.getHotGame().getList().size() > 0) {
                    homeData.add(new HomeDto(HomeDto.TYPE_HOTGAME, data.getHotGame()));
                }
                if (data.getGuessVideo() != null && data.getGuessVideo().getList().size() > 0) {
                    homeData.add(new HomeDto(HomeDto.TYPE_GUESSYOULIKE, data.getGuessVideo()));
                }
                if (data.getSysjVideo() != null && data.getSysjVideo().getList().size() > 0) {
                    homeData.add(new HomeDto(HomeDto.TYPE_SYSJVIDEO, data.getSysjVideo()));
                }
                if (data.getHotMemberVideo() != null && data.getHotMemberVideo().getList().size() > 0) {
                    homeData.add(new HomeDto(HomeDto.TYPE_HOTNARRATE, data.getHotMemberVideo()));
                }
                homeAdapter.setNewData(homeData);
            } else {
                if (data.getVideoList() != null && data.getVideoList().size() > 0) {
                    for (int i = 0; i < data.getVideoList().size(); i++) {
                        homeAdapter.addData(new HomeDto(HomeDto.TYPE_VIDEOGROUP, data.getVideoList().get(i)));
                    }
                }
            }
            ++page;

            if (noAdvertisement)
                //主页数据加载成功后再加载通栏广告（因为我insert一行广告进recyclerview）
                presenter.adImage208(AdvertisementDto.ADVERTISEMENT_8, false);

            isNetWordChange = true;
        } else {
            onRefresh();
        }
    }

    private int refreshBannerTime = 0;

    /**
     * 刷新广告
     */
    private void refreshBanner(List<Banner> list) {
        if (refreshBannerTime < 1) {
            bannerAdapter.setMaxValue(false);
            stopAutoFlowTimer();
            bannerFlow.setSideBuffer(list.size()); // 实际轮播图张数
            bannerData.clear();
            bannerData.addAll(list);
            bannerAdapter.notifyDataSetChanged();
            startAutoFlowTimer();
            ++refreshBannerTime;
        }
    }

    @Override
    public void onSwitched(View view, int position) {
        bannerAdapter.setMaxValue(true);
        startAutoFlowTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        startAutoFlowTimer();
        Log.d(tag, "onResume: homeData==" + homeData);
        if (homeData == null || homeData.size() == 0) {
            loadCacheData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoFlowTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bannerFlow != null)
            bannerFlow.destroy();
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

    private boolean isNetWordChange = true;

    /**
     * 回调：网络变化事件
     */
    public void onEventMainThread(ConnectivityChangeEvent event) {
        Log.d(tag, "ConnectivityChangeEvent: 网络变化事件");
        if (isNetWordChange && event.getNetworkInfo() != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isNetWordChange = false;
                    onRefresh();
                }
            }, 600);
        }
    }
}
