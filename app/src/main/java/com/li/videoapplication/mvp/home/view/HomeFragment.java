package com.li.videoapplication.mvp.home.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
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
import com.li.videoapplication.data.model.entity.LaunchImage;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.ConnectivityChangeEvent;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.data.model.response.UnfinishedTaskEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.mvp.adapter.HomeMultipleAdapter;
import com.li.videoapplication.data.model.response.AdvertisementDto;
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
import com.li.videoapplication.tools.RandomUtil;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.activity.WebActivity;
import com.li.videoapplication.ui.adapter.BannerAdapter;
import com.li.videoapplication.ui.adapter.YouLikeAdapter;
import com.li.videoapplication.ui.view.HomeTaskView;
import com.li.videoapplication.utils.ClickUtil;
import com.li.videoapplication.utils.GDTUtil;
import com.li.videoapplication.utils.HareWareUtil;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleFlowIndicator;
import com.li.videoapplication.views.DynamicHeightImageView;
import com.li.videoapplication.views.ViewFlow;
import com.li.videoapplication.views.ViewFlow.ViewSwitchListener;
import com.meituan.android.walle.WalleChannelReader;
import com.qq.e.ads.nativ.NativeADDataRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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

    HomeTaskView taskView;

    private int page = 1;
    private int page_count = 1;
    private boolean mIsInit = true;

    private IHomePresenter presenter = HomePresenter.getInstance();
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

    public static NativeADDataRef adItem;
    private static final int GUESSVIDEO_HOME = 0;
    private static final int GUESSVIDEO_CHANGE = 1;
    private Timer refreshTimer;
    private Timer loadMoreTimer;

    private boolean isRefresh = false;

    private boolean isClickLike = false;
    /**
     * 跳转：首页更多
     */
    private void startHomeMoreActivity(VideoImageGroup group) {
        ActivityManager.startHomeMoreActivity(getActivity(), group);
    }

    /**
     * 跳转：圈子详情
     */
    private void startGameDetailActivity(String group_id) {
        ActivityManager.startGroupDetailActivity(getActivity(), group_id);
    }

    /**
     * 跳转：风云榜
     */
    private void startBillboardActivity() {
        ActivityManager.startBillboardActivity(getActivity(), BillboardActivity.TYPE_PLAYER);
        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "热门主播更多");
    }

    /**
     * 页面跳转：广告跳转
     */
    private void startWebActivity(String url) {

        WebActivity.startWebActivity(getActivity(), url);
    }

    /**
     * 跳转：下载管理
     */
    public void startDownloadManagerActivity(LaunchImage launchImage) {
        Log.d(tag, "startDownloadManagerActivity: ");
        ActivityManager.startDownloadManagerActivity(getActivity(), launchImage);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //该fragment处于最前台交互状态
        if (isVisibleToUser) {
            UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "进入首页次数");

            //可见再渲染
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

        taskView = (HomeTaskView) view.findViewById(R.id.home_task);


        initRecyclerView();

        initAdapter();

        addOnClickListener();

    }


    public void initData() {
        Log.d(tag, "------------ loadCacheData: ------------");
        page = 1;
        HomeDto homeData = PreferencesHepler.getInstance().getHomeData();
        if (homeData == null) {
            Log.d(tag, "----loadCacheData: rxcache----");
            //没有缓存 加载新数据
            presenter.loadHomeData(page, true);
        } else {
            Log.d(tag, "----loadCacheData: sharepreferences----");
            Log.d(tag, "sp: homeData == " + homeData);
            //直接调用本地缓存回调（sp缓存）
            refreshHomeData(homeData);
            //异步加载新数据
            presenter.loadHomeData(page, true);
        }
        setRefreshStatus(true);
        presenter.unfinishedTask(getMember_id(),false);
    }

    private void initRecyclerView() {

        presenter.setHomeView(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                        isClickLike = true;
                        if (ClickUtil.canClick(1000)) {//防止连续点击
                            List<String> videoIds = PreferencesHepler.getInstance().getVideoIds();
                            if (PreferencesHepler.getInstance().canVideoIdsTime()) {// id已过期
                                // 首页猜你喜歡 换一换（问卷id）带50个猜你喜欢视频id
                                presenter.changeGuess(PreferencesHepler.getInstance().getGroupIds2());
                            } else {// id未过期
                                if (videoIds != null && videoIds.size() > 0) {
                                    // 首页猜你喜歡详情
                                    try {
                                        presenter.changeGuessSecond(getVideoIdsRandom(videoIds));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    // 首页猜你喜歡
                                    presenter.changeGuess(PreferencesHepler.getInstance().getGroupIds2());
                                }
                            }
                        }
                        UmengAnalyticsHelper.onEvent(getActivity(), UmengAnalyticsHelper.MAIN, "换一换");
                        break;
                    case R.id.homehotgame_more://热门游戏更多
                        if (activity != null) {
                            activity.locationAtGame();
                        }
                        break;
                    case R.id.hometype_sysj://视界原创更多
                        startHomeMoreActivity(item.getData().getSysjVideo());
                        break;
                    case R.id.hometype_game://游戏视频更多
                        //item 其它游戏集合中的一个 item，代表了一个
                        if (item.getData().getVideoGroupItem().getIsGame() == 1 &&
                                !StringUtil.isNull(item.getData().getVideoGroupItem().getGroup_id())) {
                            startGameDetailActivity(item.getData().getVideoGroupItem().getGroup_id());
                            UmengAnalyticsHelper.onMainGameMoreEvent(getActivity(), item.getData().getVideoGroupItem().getMore_mark());
                        } else {
                            if (item.getData().getVideoGroupItem().getMore_mark().equals("player_square")) { //玩家广场
                                ActivityManager.startSquareActivity(getActivity(),null);
                                UmengAnalyticsHelper.onEvent(getActivity(),UmengAnalyticsHelper.MAIN,"首页-玩家广场-更多");
                            } else {
                                startHomeMoreActivity(item.getData().getVideoGroupItem());
                            }
                        }
                        break;
                    case R.id.home_hotnarrate_more://热门解说更多
                        startBillboardActivity();
                        break;
                    case R.id.banner_delete://通栏广告关闭
                        homeAdapter.remove(1);
                        break;
                    case R.id.banner_image://通栏广告
                        LaunchImage launchImage = item.getData().getAdvertisement().getData().get(0);
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
//                                String app_name = launchImage.getDownload_desc().get(0).getApp_name();
//
//                                Download download = new Download();
//                                download.setDownload_url(download_android);
//                                download.setTitle(app_name);
//
//                                DownloadHelper.downloadFile(getActivity(), download);
                                startDownloadManagerActivity(launchImage);
                                break;
                        }
                        // 广告点击统计+1
                        presenter.adClick(
                                launchImage.getAd_id(),
                                AdvertisementDto.AD_CLICK_STATUS_11,
                                HareWareUtil.getHardwareCode(),
                                HareWareUtil.getIMEI());
                        break;
                }
            }
        });
    }

    private String getVideoIdsRandom(List<String> videoIds) throws Exception {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            int index = RandomUtil.getRandom(0, videoIds.size() - 1);
            list.add(videoIds.get(index));
        }
        return ArrayHelper.list2Array(list);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    private void setRefreshStatus(boolean status){
        isRefresh = status;
        if (isRefresh){
            //关闭上拉加载更多
            homeAdapter.setEnableLoadMore(false);
        }else {
            homeAdapter.setEnableLoadMore(true);
        }
    }

    @Override
    public void onRefresh() {
        Log.d(tag, "------------ onRefresh: ------------");
        setRefreshStatus(true);
        homeAdapter.removeAllFooterView();
        isClickLike = false;
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

        //刷新状态刷新超过十秒钟取消
        refreshTimer = TimeHelper.runAfter(new TimeHelper.RunAfter() {
            @Override
            public void runAfter() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    ToastHelper.s(R.string.net_unstable);
                    setRefreshStatus(false);
                }
            }
        }, 1000 * 10);
    }

    @Override
    public void onLoadMoreRequested() {
        Log.d(tag, "onLoadMore: page == " + page + ", pagecount == " + page_count);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (page <= page_count) {
                    presenter.loadHomeData(page, true);

                    //超过十秒钟取消
                    loadMoreTimer = TimeHelper.runAfter(new TimeHelper.RunAfter() {
                        @Override
                        public void runAfter() {
                            ToastHelper.s(R.string.net_unstable);
                            homeAdapter.loadMoreComplete();
                            setRefreshStatus(false);
                        }
                    }, 1000 * 10);
                } else {
                    // 数据全部加载完毕
                    homeAdapter.addFooterView(getFootView());
                    homeAdapter.loadMoreEnd();
                    setRefreshStatus(false);
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

    /**
     * 回调：首页数据
     * @param homeDto
     */
    public void onEventMainThread(HomeDto homeDto){
        if (page == 1){
            PreferencesHepler.getInstance().saveHomeData(homeDto);//保存首页json 只要第一页数据
        }
        refreshHomeData(homeDto);
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
                    taskView.setVisibility(View.VISIBLE);
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
        if (AppConstant.SHOW_DOWNLOAD_AD) {//普通渠道，替换广告
            replaseGDT(data.getData().getList(), GUESSVIDEO_CHANGE);
        } else {//特殊渠道，不加广告
            homeAdapter.changeGuessVideo(data.getData().getList());
        }
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


    private VideoImageGroup mVideoImageGroup;
    //主页数据
    @Override
    public void refreshHomeData(HomeDto homeDto) {
        Log.d(tag, "======== refreshHomeData: ========");
        if (homeDto != null) {
            HomeDto.Data data = homeDto.getData();
            if (data == null){
                return;
            }
            page_count = data.getPage_count();
            if (page < 2) {
                Log.d(tag, "refreshHomeData: page = 1");
                homeData.clear();
                if (data.getBanner() != null && data.getBanner().size() > 0) {
                    refreshBanner(data.getBanner());
                }
                if (data.getHotGame() != null && data.getHotGame().getList().size() > 0) {
                    homeData.add(new HomeDto(HomeDto.TYPE_HOTGAME, data.getHotGame()));
                }

                if (data.getGuessVideo() != null && data.getGuessVideo().getList().size() > 0) {
                    mVideoImageGroup = data.getGuessVideo();
                    homeData.add(new HomeDto(HomeDto.TYPE_GUESSYOULIKE, data.getGuessVideo()));
                }
                if (data.getSysjVideo() != null && data.getSysjVideo().getList().size() > 0) {
                    homeData.add(new HomeDto(HomeDto.TYPE_SYSJVIDEO, data.getSysjVideo()));
                }
                if (data.getHotMemberVideo() != null && data.getHotMemberVideo().getList().size() > 0) {
                    homeData.add(new HomeDto(HomeDto.TYPE_HOTNARRATE, data.getHotMemberVideo()));
                }
                homeAdapter.setNewData(homeData);
                if (AppConstant.SHOW_DOWNLOAD_AD) //普通渠道，替换广告
                    replaseGDT(data.getGuessVideo().getList(), GUESSVIDEO_HOME);//替换猜你喜欢最后一个为广告

            } else {
                Log.d(tag, "refreshHomeData: page = " + page);
                if (data.getVideoList() != null && data.getVideoList().size() > 0) {
                    for (int i = 0; i < data.getVideoList().size(); i++) {
                        homeAdapter.addData(new HomeDto(HomeDto.TYPE_VIDEOGROUP, data.getVideoList().get(i)));
                    }
                }
            }
            //由于此回调会执行多次，page++导致页数问题，暂时做以下处理
            //如果加载到的数据还是第一页，那么page=2，其余的++
            if (data.getBanner() != null) {
                page = 2;
                Log.d(tag, "refreshHomeData: getBanner != null, page = " + page);
            } else {
                ++page;
                Log.d(tag, "refreshHomeData: getBanner == null, page = " + page);
            }

            if (AppConstant.SHOW_DOWNLOAD_AD && noAdvertisement)//普通渠道&&还未显示通栏广告
                //主页数据加载成功后再加载通栏广告（因为insert一行广告进recyclerview）
                presenter.adImage208(AdvertisementDto.ADVERTISEMENT_8, false);

            isNetWordChange = true;
            if (refreshTimer != null) refreshTimer.cancel();
            if (loadMoreTimer != null){
                loadMoreTimer.cancel();
                loadMoreTimer = null;
            }
            swipeRefreshLayout.setRefreshing(false);
            setRefreshStatus(false);
            homeAdapter.loadMoreComplete();
        } else {
            onRefresh();
        }
    }

    @Override
    public void refreshHomeData(HomeModuleEntity homeModuleEntity) {

    }

    /**
     * 加载首页数据失败
     */
    @Override
    public void refreshHomeDataFault(final Throwable t) {

    }

    /**
     * 刷新广告
     */
    private void refreshBanner(List<Banner> list) {

            bannerAdapter.setMaxValue(false);
            stopAutoFlowTimer();
            bannerFlow.setSideBuffer(list.size()); // 实际轮播图张数
            bannerData.clear();
            bannerData.addAll(list);
            bannerAdapter.notifyDataSetChanged();
            bannerFlow.setSelection(0);
            startAutoFlowTimer();
    }

    //替换广告的猜你喜欢 List
    private List<VideoImage> newGuessList = new ArrayList<>();

    /**
     * 替换猜你喜欢最后一条数据为广点通广告
     *
     * @param guessList 猜你喜欢数据
     * @param location  操作猜你喜欢广告的位置。主页的猜你喜欢数据如加载不到广告则不动，
     *                  点击换一换后的猜你喜欢数据如加载不到广告则将新数据更新上
     */
    private void replaseGDT(List<VideoImage> guessList, final int location) {
        if (isInterceptAD()){
            return;
        }

        newGuessList.clear();
        newGuessList.addAll(guessList);
        GDTUtil.nativeAD(getActivity(), GDTUtil.POS_ID_GUESSYOURLIKE, new GDTUtil.GDTonLoaded() {

            @Override
            public void onADLoaded(NativeADDataRef adItem) {
                Log.d(tag, "onADLoaded: ");
                if (adItem != null) {
                    HomeFragment.adItem = adItem;
                    VideoImage ad = new VideoImage();
                    ad.setAD(true);//自己定一个广告标识
                    ad.setVideo_id("1");//自己定一个id给广告
                    ad.setAvatar(adItem.getIconUrl());
                    ad.setFlagPath(adItem.getImgUrl());
                    ad.setVideo_flag(adItem.getImgUrl());
                    ad.setNickname(adItem.getTitle());
                    ad.setTitle(adItem.getDesc());
                    //替换广告
                    replaceGuessVideo2AD(newGuessList, ad);
                }
            }

            @Override
            public void onADError() {//没广告或广告加载出错
                Log.d(tag, "onADError: ");
                if (location == GUESSVIDEO_CHANGE)
                    homeAdapter.changeGuessVideo(newGuessList);
            }
        });
    }

    private void replaceGuessVideo2AD(List<VideoImage> newGuessList, VideoImage adItem) {
        if (newGuessList.size() == 4) {//防止连续点击造成个数问题
            newGuessList.remove(newGuessList.size() - 1);//移除第4条item

            newGuessList.add(adItem);//替换广告
            homeAdapter.changeGuessVideo(newGuessList);
            if (!isClickLike){
                recyclerView.scrollToPosition(0);           //不知道为何 更新了广告图后 会滚动到相应的item  所以直接回到0的位置
            }else {
                isClickLike = false;
            }
        }
    }


    /**
     * 是否拦截广告展示
     * OPPO 渠道默认不显示广告
     * 其他渠道根据VIP 判断
     */
    private boolean isInterceptAD(){
        boolean isIntercept = false;
        //以Walle渠道号为准
        String channel = WalleChannelReader.getChannel(getActivity());

        if (StringUtil.isNull(channel)){
            channel="default_channel";
        }

        //oppo渠道默认不展示广告
        if ("oppo".equals(channel)){
            isIntercept = true;
        }

        //VIP3不展示广告
        Member member = getUser();
        if (member!= null && member.getVipInfo() != null){
            if ("3".equals(member.getVipInfo().getLevel())){
                return true;
            }
        }

        return isIntercept;
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

        if (mIsInit){

            UITask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        initData();
                    } catch (Exception e) {
                        e.printStackTrace();
                        presenter.loadHomeData(page, true);
                    }
                }
            },200);
            mIsInit = false;
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
