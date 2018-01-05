package com.li.videoapplication.mvp.home.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.data.model.event.ConnectivityChangeEvent;
import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.HomeMultipleAdapterNew;
import com.li.videoapplication.mvp.billboard.view.BillboardActivity;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.adapter.BannerAdapter;
import com.li.videoapplication.ui.adapter.BannerAdapterFor226;
import com.li.videoapplication.utils.GDTUtil;
import com.li.videoapplication.utils.MD5Util;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleFlowIndicator;
import com.li.videoapplication.views.DynamicHeightImageView;
import com.li.videoapplication.views.ViewFlow;
import com.meituan.android.walle.WalleChannelReader;
import com.qq.e.ads.nativ.NativeADDataRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.eventbus.EventBus;

/**
 * 分栏
 */

public class HomeLazyColumnFragment3 extends TBaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, ViewFlow.ViewSwitchListener {

    private static final String TAG = "HomeLazy";

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private ScrollListener scrollListener;

    //顶部广告
    private boolean isInitBanner = true;
    private View bannerView;
    private ViewFlow bannerFlow;
    private BannerAdapterFor226 bannerAdapter;
    private String mColumnId;

    //换一换广告
    private static final int GUESSVIDEO_HOME = 0;
    private static final int GUESSVIDEO_CHANGE = 1;
    private boolean isClickLike = false;

    //首页加载了2次数据（缓存一次），所以会insert2次，控制只插入1条广告
    private boolean noAdvertisement = true;
    public static NativeADDataRef adItem;
    private Timer refreshTimer;
    private Timer loadMoreTimer;

    private List<HomeModuleEntity.ADataBean.ListBean> sGamerVideoData;         //所有数据

    private int mTotalPage;
    private int mPage = 1;
    private int mVideoGamerPage = 2; //先load后加页，所以一开始是第二页
    //尾部
    private View footerView;

    //FIXME 需要将其他类型的布局 参照 HomeMultipleAdapter来写
    private HomeMultipleAdapterNew mAdapter;

    private List<HomeModuleEntity.ADataBean> mData; //获取的总数据
    private List<HomeModuleEntity.ADataBean> tData; //渲染的数据
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_top)
    ImageView mIvTop;

    private boolean isReZero = false;
    private boolean isInit = false; //真正显示的View是否已经被初始化
    private boolean isStartLoadGamerVideo = false;
    private boolean isGamerVideoLoadComplete = false;
    private boolean isLoadDataComplete = false; //第一次加载数据是否完成
    public static final String INTENT_BOOLEAN_LAZY_LOAD = "intent_boolean_lazy_load";

    private boolean mIsNeedLoadData = false;
    private boolean isLazyLoad = false;
    private boolean isShowView = false; //是否已经展示过
    private static boolean isLoadMoreFlag = false; //判断是否是加载更多的flag
    private boolean isRefreshFlag = false;  //判断是否下拉刷新的flag
    private boolean isNetWordData = false; //判断是否是从网络获取数据的flag，用来作缓存判断

    private MainActivity mActivity;

    //滑动状态参数
    private float mOffset = 0f;
    private float mStartOffset = 0f;
    private float mLastDy = 0f;
    private boolean mIsShowMenu = true;

    public static HomeLazyColumnFragment3 newInstance(String columnId, boolean isNeedLoadData,
                                                      int delayTime, boolean isLazyLoad) {
        final HomeLazyColumnFragment3 fragment = new HomeLazyColumnFragment3();

        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_load_data", isNeedLoadData);
        bundle.putString("column_id", columnId);
        bundle.putBoolean(HomeLazyColumnFragment3.INTENT_BOOLEAN_LAZY_LOAD, isLazyLoad);

        fragment.setArguments(bundle);
        if (isNeedLoadData) {
            UITask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isLoadMoreFlag = false;
                    fragment.loadData();
                }
            }, delayTime);
        }
        return fragment;
    }

    public String getColumnId() {
        return mColumnId;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mActivity == null) {
            if (activity instanceof MainActivity) {
                mActivity = (MainActivity) activity;
            }
        }
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initContentView(View view) {
        if (!isInit) {
            lazyLoadView();
        }
    }

    private void lazyLoadView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIsNeedLoadData = bundle.getBoolean("is_need_load_data", false);
            mColumnId = bundle.getString("column_id", "1"); //拿到了分栏ID,根据ID来传入不同的适配器
            isLazyLoad = bundle.getBoolean(INTENT_BOOLEAN_LAZY_LOAD, isLazyLoad);
        }
        Log.i(tag, "HomeLazy加载view:" + mColumnId);
        if (isLazyLoad) {
            //若isVisibleToUser==true就对真正需要的显示内容进行加载
            if (getUserVisibleHint() && !isInit) {
                Log.i(tag, "懒加载:loadview...");
                loadView();
                isInit = true;
            } else {
                //进行懒加载
//                layout = new FrameLayout(getActivity().getApplicationContext());
//                layout.setLayoutParams(new FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                View v = LayoutInflater.from(getActivity()
//                        .getApplicationContext()).inflate(R.layout.fragment_lazy_loading, null);
//                layout.addView(v);
//                super.setBaseRootView(layout);
            }
        } else {
            loadView();
            isInit = true;
        }
    }

    private void loadView() {
        setDefRootView();
        initRecyclerView(getBaseRootView());
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (tData == null) {
            tData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new HomeMultipleAdapterNew(this, tData);
        }

        View emptyView = inflater.inflate(R.layout.emptyview_main,
                (ViewGroup) mRecyclerView.getParent(), false);
        mAdapter.setEmptyView(emptyView);
        mAdapter.setOnLoadMoreListener(mLoadMoreListener, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        showIvTop(false);
        //TODO 这样 会导致 所有走到这里的fragment都会直接加载数据 应该限制一下
        //包括了数据的加载，转换，视图的处理
        if (!mIsNeedLoadData) {
            isLoadMoreFlag = false;
            loadData();
        }
    }

    public void setIsShowView(boolean isShowView) {
        this.isShowView = isShowView;
    }

    public void notifyAdapter() {
        this.mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取网络数据
     */
    private void loadData() {
        // FIXME: 2017/11/21 应该加上page参数，做到选择加载页数
        Log.w("HomeLazy", "loadData->" + "mColumnId:" + mColumnId + "和mPage:" + mPage);

        HomeModuleEntity entity = new Gson().fromJson(HomeFragmentNew.lruCache.get(MD5Util.string2MD5(mColumnId + mPage)), HomeModuleEntity.class);
        if (entity != null && !isRefreshFlag) {
            Map<String, Object> extra = entity.getExtra();
            extra.put("cache", "1");
            entity.setExtra(extra);
            EventBus.getDefault().post(entity);
        } else {
            if (NetUtil.isConnect()) {
                DataManager.getHomeInfoById(mColumnId, mPage);
            } else {
                if (mColumnId.equals("1")) {
                    EventBus.getDefault().post(PreferencesHepler.getInstance().getHomeEntity());
                } else {
                    ToastHelper.s(R.string.net_disable);
                }
            }
        }
    }

    /**
     * 初始化数据，做缓存判断，优化，不直接加载数据
     */
    public void initData() {
        mPage = 1;
        HomeModuleEntity homeModuleEntity = PreferencesHepler.getInstance().getHomeEntity();
        if (homeModuleEntity == null) {
            //没有数据，加载新的数据
            loadData();
        } else {
            //调用本地缓存
            // TODO
            //再次加载数据
            loadData();
        }
    }

    public void onEventMainThread(HomeModuleEntity entity) {
        Map<String, Object> extra = entity.getExtra();
        if (extra == null) {
            return;
        }
        //避免加载到 其他分栏的数据
        if (!mColumnId.equals(extra.get("column_id"))) {
            return;
        }

        if (mColumnId.equals("1")) //缓存首页
            PreferencesHepler.getInstance().saveHomeEntity(entity);

        if (extra.get("cache").equals("0")) {
            HomeModuleEntity cacheEntity = (HomeModuleEntity) entity.clone();
            String jsonCacheEntity = cacheEntity.toJSON();
            HomeFragmentNew.lruCache.put(MD5Util.string2MD5(mColumnId + mPage), jsonCacheEntity);
        }

        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (!mData.isEmpty()) {
            mData.clear();
        }
        if (tData == null) {
            tData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new HomeMultipleAdapterNew(this, tData);
        }
        Log.d("HomeLazy", entity.getAData().size() + "");
        Log.w("HomeLazy", entity.toJSON());
        if (entity.isResult()) {
            mTotalPage = entity.getPage_count();

            Map<String, HomeModuleEntity.ADataBean> beanMap = new HashMap<>();

            for (HomeModuleEntity.ADataBean dataBean : entity.getAData()) {
                Log.w("HomeLazy", dataBean.toString());
                //FIXME 第一页 的数据一定要在第二页之前否则页面数据显示混乱
                if (dataBean.getFlag().equals("banner")) {
                    if (isInitBanner) {
                        //初始化banner
                        mAdapter.addHeaderView(getBannerView(dataBean.getList()));
                        isInitBanner = false;
                    } else {
                        mAdapter.removeAllHeaderView();
                        mAdapter.addHeaderView(getBannerView(dataBean.getList()));
                    }
                    continue;
                }
                if (dataBean.getFlag().equals("hotGame")) {
                    dataBean.setItemType(HomeModuleEntity.TYPE_HOT_GAME);
                    mData.add(dataBean);
                    continue;
                }
                if (dataBean.getFlag().equals("guessVideo")) {
                    dataBean.setItemType(HomeModuleEntity.TYPE_GUESS_YOU_LIKE);
                    mData.add(dataBean);
                    if (AppConstant.SHOW_DOWNLOAD_AD) {//普通渠道，替换广告
                        replaseGDT(HomeMultipleAdapterNew.changeVideoImageType(dataBean.getList()), GUESSVIDEO_CHANGE);
                    }
                    continue;
                }
                if (dataBean.getFlag().equals("hotMember")) {
                    dataBean.setItemType(HomeModuleEntity.TYPE_HOT_NARRATE);
                    mData.add(dataBean);
                    continue;
                }
                if (dataBean.getFlag().equals("common")) {
                    if (dataBean.getMore_mark().equals("sysj_original")) {
                        dataBean.setItemType(HomeModuleEntity.TYPE_SYSJ_VIDEO);
                        mData.add(dataBean);
                        continue;
                    } else {
                        dataBean.setItemType(HomeModuleEntity.TYPE_VIDEO_GROUP);
                        mData.add(dataBean);
                        continue;
                    }
                }
                if (dataBean.getFlag().equals("gamerVideo")) {
                    dataBean.setItemType(HomeModuleEntity.TYPE_GAMER_VIDEO);
                    mData.add(dataBean);
                }
            }
            addTData();
            Log.w(TAG, mData.toString());
            if (mPage == 1 && mVideoGamerPage == 2 &&
                    swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            if (isInit && !isLoadMoreFlag && !isRefreshFlag) {
                mAdapter.notifyDataSetChanged();
                isShowView = true;
            } else {
                mAdapter.loadMoreComplete();
            }
            isLoadDataComplete = true;
            if (isRefreshFlag) {
                mAdapter.setNewData(tData);
                isRefreshFlag = false;
            }
            if (bannerFlow != null) {
                startAutoFlowTimer();
            }
        } else {
            //返回为结果处理
        }
    }

    @OnClick(R.id.iv_top)
    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    ////////////////初始化/////////////////
    private void initRecyclerView(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        scrollListener = new ScrollListener();
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.addOnScrollListener(scrollListener);
        mRecyclerView.addOnItemTouchListener(mItemChildClickListener);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    ///////////////////////banner 和foot 视图
    private View getBannerView(List<HomeModuleEntity.ADataBean.ListBean> bannerData) {

        if (bannerView == null) {
            bannerView = inflater.inflate(R.layout.header_home_banner, null);
            setBannerView(bannerView);
            bannerFlow = (ViewFlow) bannerView.findViewById(R.id.viewflow);
            CircleFlowIndicator bannerIndicator = (CircleFlowIndicator) bannerView.findViewById(R.id.circleflowindicator);
            bannerFlow.setSideBuffer(6); // 初始化轮播图张数
            bannerFlow.setFlowIndicator(bannerIndicator);
            bannerFlow.setTimeSpan(4000);
            bannerFlow.setSelection(0); // 设置初始位置

            bannerAdapter = new BannerAdapterFor226(getActivity(), bannerData, BannerAdapter.PAGER_HOME);
            bannerFlow.setAdapter(bannerAdapter);
            bannerFlow.setOnViewSwitchListener(this);
            //annerFlow.setOnViewSwitchListener(this);
        }
        return bannerView;

    }

    private void setBannerView(View view) {
        //750:350 = 15:7
        int w = srceenWidth;
        int h = w * 7 / 15;
        setListViewLayoutParams(view, w, h);
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
    public void onRefresh() {
        mData.clear();

        tData.clear();
        isLoadDataComplete = false;
        isGamerVideoLoadComplete = false;
        isStartLoadGamerVideo = false;
        mPage = 1;
        gamerVideoIndex = 0;
        mVideoGamerPage = 2; //先load后加页，所以一开始是第二页
        isRefreshFlag = true;
        isLoadMoreFlag = false;
        loadData();
        mAdapter.removeFooterView(getFootView());
        //刷新状态刷新超过十秒钟取消
        refreshTimer = TimeHelper.runAfter(new TimeHelper.RunAfter() {
            @Override
            public void runAfter() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    ToastHelper.s(R.string.net_unstable);
//                    setRefreshStatus(false);
                }
            }
        }, 1000 * 10);
    }

    public boolean isReZero() {
        return isReZero;
    }

    public void reZero() {
        mData.clear();
        tData.clear();
        isLoadDataComplete = false;
        isGamerVideoLoadComplete = false;
        isStartLoadGamerVideo = false;
        mPage = 1;
        gamerVideoIndex = 0;
        mVideoGamerPage = 2; //先load后加页，所以一开始是第二页
        isRefreshFlag = true;
        isLoadMoreFlag = false;
        mAdapter.removeFooterView(getFootView());
        mAdapter.notifyDataSetChanged();
        isReZero = true;
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //可见，但还没初始化
        if (isLazyLoad && isVisibleToUser && !isInit && getBaseRootView() != null) {
            loadView();
            Log.w("visibleStatus", mColumnId + "->初始化....");
            isInit = true;
        }
        //已经初始化过
        if (isInit && getBaseRootView() != null) {
            if (isVisibleToUser) {
                if (!isShowView && isInit) {
                    mAdapter.notifyDataSetChanged();
                    isShowView = true;
                }
            } else {
                Log.i("visibleStatus", mColumnId + "已经初始化，不可见");
            }
        }
    }

    private int gamerVideoIndex = 0; //玩家视频的索引

    private void addTData() {

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getFlag().equals("gamerVideo")) {
                isStartLoadGamerVideo = true;
                gamerVideoIndex = tData.size() + i;
                for (int j = 0; j < i + 1; j++) {
                    tData.add(mData.get(j));
                }
                sGamerVideoData = mData.get(i).getList();
                if (sGamerVideoData.size() > 10) {
                    List<HomeModuleEntity.ADataBean.ListBean> sSaveData = new ArrayList<>();
                    for (int j = 0; j < 10; j++) {
                        sSaveData.add(sGamerVideoData.get(j));
                    }
                    tData.get(gamerVideoIndex).setList(sSaveData);
                }
            }
        }
        if (!isStartLoadGamerVideo) {
            for (int i = 0; i < mData.size(); i++) {
                tData.add(mData.get(i));
            }
        }
    }

    private void loadMoreGamerVideoData2() {
        boolean isComplete = false;
        if (isLoadDataComplete && !isGamerVideoLoadComplete) {
            Log.w("HomeLazy", "加载更多玩家视频.....");
            Log.w("HomeLazy", "sData的大小是：" + sGamerVideoData.size());

            List<HomeModuleEntity.ADataBean.ListBean> datas = new ArrayList<>();
            if ((tData.get(gamerVideoIndex).getList().size() + 10) <=
                    sGamerVideoData.size()) {
                Log.w("HomeLazy", "+" + (tData.get(gamerVideoIndex).getList().size() + 10));
                for (int i = 0; i < 10; i++) {
                    if ((mVideoGamerPage - 1) * 10 + i >= sGamerVideoData.size())
                        Log.e(tag, "超出了！");
                    datas.add(sGamerVideoData.get((mVideoGamerPage - 1) * 10 + i));
                }
                if ((tData.get(gamerVideoIndex).getList().size() + 10) == sGamerVideoData.size()) {
                    isComplete = true;
//                    isGamerVideoLoadComplete = true;
//                    footerLoadView.setVisibility(View.GONE);
                }
            } else {
//                isGamerVideoLoadComplete = true;
//                footerLoadView.setVisibility(View.GONE);
                isComplete = true;
                for (int i = 0; i < (sGamerVideoData.size() - (mVideoGamerPage - 1) * 10); i++) {
                    datas.add(sGamerVideoData.get((mVideoGamerPage - 1) * 10 + i));
                }

            }

            if (!datas.isEmpty()) {
                tData.get(gamerVideoIndex).getList().addAll(datas);
            }

            if (isComplete) {
                for (int i = gamerVideoIndex + 1; i < mData.size(); i++) {
                    tData.add(mData.get(i));
                }
                isStartLoadGamerVideo = false;
                isGamerVideoLoadComplete = true;
            }

            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
            ++mVideoGamerPage;
        }
    }

    private OnItemChildClickListener mItemChildClickListener = new OnItemChildClickListener() {

        @Override
        public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            Log.d(tag, "item点击了");
            HomeModuleEntity.ADataBean dataBean = (HomeModuleEntity.ADataBean) baseQuickAdapter.getItem(position);
            if (dataBean != null) {
                Log.d(tag, "item点击了->" + dataBean.getTitle());
            }
            VideoImageGroup group = new VideoImageGroup();
            if (dataBean.getItemType() == HomeModuleEntity.TYPE_SYSJ_VIDEO
                    || dataBean.getItemType() == HomeModuleEntity.TYPE_HOT_NARRATE
                    || dataBean.getItemType() == HomeModuleEntity.TYPE_VIDEO_GROUP
                    || dataBean.getItemType() == HomeModuleEntity.TYPE_GAMER_VIDEO) {

                group.setMore_mark(dataBean.getMore_mark());
                group.setGame_id(dataBean.getGame_id());
                group.setTitle(dataBean.getTitle());
                if (dataBean.getIsGame() != null && dataBean.getIsGame().length() > 0) {
                    group.setIsGame(Integer.parseInt(dataBean.getIsGame()));
                }
                group.setGroup_id(dataBean.getGroup_id());
            } else {
                group = null;
            }
            switch (view.getId()) {
                case R.id.hometype_sysj: //视界推荐
                    if (group != null) {
                        ActivityManager.startHomeMoreActivity(getActivity(), group);
                    }
                    break;
                case R.id.homehotgame_more: //热门游戏
                    EventBus.getDefault().post(Integer.valueOf(1));
                    break;
                case R.id.hometype_youlike_change: //猜你喜欢
                    isClickLike = true;
                    if (!PreferencesHepler.getInstance().canVideoIdsTime()
                            && PreferencesHepler.getInstance().getVideoIds().size() > 0) {
                        List<String> videoIds = PreferencesHepler.getInstance().getVideoIds(4);
                        StringBuffer video_ids = new StringBuffer();
                        for (int j = 0; j < videoIds.size(); j++) {
                            video_ids.append(videoIds.get(j));
                            if (j < (videoIds.size() - 1)) {
                                video_ids.append(",");
                            }
                        }
                        DataManager.indexChangeGuess217(video_ids.toString());
                    } else {
                        DataManager.indexChangeGuess217(PreferencesHepler.getInstance().getGroupIds2());
                    }
                    break;
                case R.id.home_hotnarrate_more:  //热门解说
                    ActivityManager.startBillboardActivity(getActivity(), BillboardActivity.TYPE_PLAYER);
                    break;
                case R.id.hometype_game:
                    if (group != null) {
                        if (group.getIsGame() == 1) {
                            ActivityManager.startGroupDetailActivity(getActivity(), group.getGroup_id());
                        } else {
                            if (group.getMore_mark().equals("player_square")) {
                                ActivityManager.startSquareActivity(getActivity(), group.getGame_id());
                            } else if (group.getMore_mark().equals("new_game_flag")) {
                                ActivityManager.startHomeMoreActivity(getActivity(), group);
                            } else {
                                ActivityManager.startHomeMoreActivity(getActivity(), group);
                            }
                        }
                    }
                    break;
            }
        }
    };

    //有video_ids的换一换
    public void onEventMainThread(ChangeGuessEntity entity) {
        Log.d(tag, "======== refreshChangeGuessView: ========");
        if (mAdapter != null) {
            if (entity.getVideoIds() != null) {
                // 保存50个猜你喜歡视频id
                PreferencesHepler.getInstance().saveVideoIds(entity.getVideoIds());
                // 保存猜你喜歡视频保存的时间
                PreferencesHepler.getInstance().saveVideoIdsTime();
            }
            if (AppConstant.SHOW_DOWNLOAD_AD) {//普通渠道，替换广告
                replaseGDT(entity.getData().getList(), GUESSVIDEO_CHANGE);
            } else {//特殊渠道，不加广告
                mAdapter.changeGuessVideo(entity.getData().getList());
            }
        }
    }

    private void loadMoreGamerVideoData() {
        if (isLoadDataComplete && !isGamerVideoLoadComplete) {
            Log.w("HomeLazy", "加载更多玩家视频.....");
            Log.w("HomeLazy", "sData的大小是：" + sGamerVideoData.size());
            int videoGamerIndex = 0;
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getItemType() == HomeModuleEntity.TYPE_GAMER_VIDEO) {
                    videoGamerIndex = i;
                    break;
                }
            }

            List<HomeModuleEntity.ADataBean.ListBean> datas = new ArrayList<>();
            if ((mData.get(videoGamerIndex).getList().size() + 10) <=
                    sGamerVideoData.size()) {
                Log.w("HomeLazy", "+" + (mData.get(videoGamerIndex).getList().size() + 10));
                for (int i = 0; i < 10; i++) {
                    datas.add(sGamerVideoData.get((mVideoGamerPage - 1) * 10 + i));
                }
                if ((mData.get(videoGamerIndex).getList().size() + 10) == sGamerVideoData.size()) {
                    isGamerVideoLoadComplete = true;
                }
            } else {
                isGamerVideoLoadComplete = true;
                for (int i = 0; i < (sGamerVideoData.size() - (mVideoGamerPage - 1) * 10); i++) {
                    datas.add(sGamerVideoData.get((mVideoGamerPage - 1) * 10 + i));
                }
            }
            if (mData == null) {
                mData = new ArrayList<>();
            }

            mData.get(videoGamerIndex).getList().addAll(datas);
            mAdapter.notifyDataSetChanged();
            ++mVideoGamerPage;

        }
    }

    @Override
    public void onSwitched(View view, int position) {
        bannerAdapter.setMaxValue(true);
        startAutoFlowTimer();
    }

    public class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                mAdapter.setScrolling(false);
                mAdapter.setScrollingSingle(true);
//                mAdapter.notifyDataSetChanged();
//                Log.w(tag, "滚动停止，刷新..");

            } else {
                mAdapter.setScrolling(true);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

//            Log.d(tag, "最后一个可见：" + lastVisibleItem);
//            if (lastVisibleItem + 1 == mAdapter.getItemCount()) {
//                footerLoadView.setVisibility(View.GONE);
//            }
            if (mLastDy > 0 && dy < 0 || mLastDy < 0 && dy > 0) {
                mStartOffset = mOffset;
            }
            mLastDy = dy;
            mOffset += dy;
            if (mOffset - mStartOffset > ScreenUtil.dp2px(5) && mIsShowMenu) {
                if (mActivity != null) {
                    mIsShowMenu = false;
                    mActivity.refreshBottomMenu(false);
                    mStartOffset = mOffset;
                }
            } else if (mOffset - mStartOffset < -ScreenUtil.dp2px(5) && !mIsShowMenu) {
                if (mActivity != null) {
                    mIsShowMenu = true;
                    mActivity.refreshBottomMenu(true);
                    mStartOffset = mOffset;
                }
            }

            if (mOffset < 1200 && isShowIvTop)
                showIvTop(false);
            else if (mOffset > 1200 && !isShowIvTop)
                showIvTop(true);

//            if (mAdapter.getItemCount() > 1) {
//                if (layoutManager.findFirstVisibleItemPosition() == 0 && isShowIvTop) {
//                    showIvTop(false);
//                } else if (layoutManager.findFirstVisibleItemPosition() != 0 && !isShowIvTop){
//                    showIvTop(true);
//                }
//            }
        }
    }

    public BaseQuickAdapter.RequestLoadMoreListener mLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            Log.i(tag, "加载更多回调..");
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mPage <= mTotalPage) {
                        if (isStartLoadGamerVideo && !isGamerVideoLoadComplete) {
                            Log.d(tag, "加载更多本地页..");
                            loadMoreGamerVideoData2();
                            isLoadMoreFlag = true;
                        } else {
                            ++mPage;
                            isLoadMoreFlag = true;
                            Log.d(tag, "加载更多页..");
                            loadData();
                        }
                    } else {
                        mAdapter.addFooterView(getFootView());
                        mAdapter.loadMoreEnd();

                    }
                }
            }, 200);
        }
    };

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
        if (isInterceptAD()) {
            Log.d(tag, "广告通道被拦截");
            mAdapter.changeGuessVideo(guessList);
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
                    mAdapter.changeGuessVideo(newGuessList);
            }
        });
    }

    private void replaceGuessVideo2AD(List<VideoImage> newGuessList, VideoImage adItem) {
        if (newGuessList.size() == 4) {//防止连续点击造成个数问题
            newGuessList.remove(newGuessList.size() - 1);//移除第4条item

            newGuessList.add(adItem);//替换广告
            mAdapter.changeGuessVideo(newGuessList);
            if (isClickLike && mRecyclerView != null) {
//                mRecyclerView.scrollToPosition(0);           //不知道为何 更新了广告图后 会滚动到相应的item  所以直接回到0的位置
            } else {
                isClickLike = false;
            }
        }
    }

    /**
     * 是否拦截广告展示
     * OPPO 渠道默认不显示广告
     * 其他渠道根据VIP 判断
     */
    private boolean isInterceptAD() {
        boolean isIntercept = false;
        //以Walle渠道号为准
        String channel = WalleChannelReader.getChannel(getActivity());

        if (StringUtil.isNull(channel)) {
            channel = "default_channel";
        }

        //oppo渠道默认不展示广告
        if ("oppo".equals(channel)) {
            isIntercept = true;
        }

        //VIP3不展示广告
        Member member = getUser();
        if (member != null && member.getVipInfo() != null) {
            if ("3".equals(member.getVipInfo().getLevel())) {
                return true;
            }
        }
        return isIntercept;
    }

    @Override
    public void onResume() {
        super.onResume();
        startAutoFlowTimer();
        if (mRecyclerView != null) {
            if (scrollListener != null) {
                mRecyclerView.addOnScrollListener(scrollListener);
                Log.w(tag, "执行了addOnScrollListener");
            }
            if (mItemChildClickListener != null) {
                mRecyclerView.addOnItemTouchListener(mItemChildClickListener);
                Log.w(tag, "addOnItemTouchListener");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoFlowTimer();
    }


    @Override
    public void onDestroy() {
        isShowView = false;
        if (mColumnId != null) {
            Log.w(tag, mColumnId + "执行onDestroy");
        } else {
            Log.w(tag, "执行onDestroy");
        }
        if (mRecyclerView != null) {
            if (scrollListener != null) {
                mRecyclerView.removeOnScrollListener(scrollListener);
                Log.w(tag, "执行了removeOnScrollListener");
            }
            if (mItemChildClickListener != null) {
                mRecyclerView.removeOnItemTouchListener(mItemChildClickListener);
                Log.w(tag, "removeOnItemTouchListener");
            }
        }
//        if (bannerFlow != null)
//            bannerFlow.destroy();
        super.onDestroy();

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
                    if (NetUtil.isConnect()) {
                        if (swipeRefreshLayout.isRefreshing()) {
                            onRefresh();
                        }
                    } else {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
            }, 600);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listLostByViewGroup((ViewGroup) ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0));
    }

    /**
     * 初始将fragment的item失去焦点 (解决换一换刷新和拉到其它fragment中recyclerview自动滚动的问题)
     * 也可在xml中设置布局获取焦点
     *
     * @param viewGroup
     */
    public static void listLostByViewGroup(ViewGroup viewGroup) {
        viewGroup.setFocusable(true);
        viewGroup.setFocusableInTouchMode(true);
        viewGroup.requestFocus();
    }

    private boolean isShowIvTop = false;

    public boolean showIvTop(boolean isShowTop) {
        if (mIvTop == null)
            return false;
        if (isShowTop) {
            Animation animation = new ScaleAnimation(0.f, 1.f, 0.f, 1.f, mIvTop.getWidth() / 2f, mIvTop.getHeight() / 2f);
            animation.setDuration(300);
            animation.setAnimationListener(new TopButtonAnimListener(mIvTop, isShowTop));
            animation.setFillAfter(true);
            mIvTop.startAnimation(animation);
            Log.d(tag, "start to show top button");
        } else {
            Animation animation = new ScaleAnimation(1.f, 0.f, 1f, 0.f, mIvTop.getWidth() / 2f, mIvTop.getHeight() / 2f);
            animation.setDuration(300);
            animation.setAnimationListener(new TopButtonAnimListener(mIvTop, isShowTop));
            animation.setFillAfter(true);
            mIvTop.startAnimation(animation);
            Log.d(tag, "start to hide top button");
        }
        return isShowIvTop = isShowTop;
    }
    public boolean getIsShowIvTop() {
        return isShowIvTop;
    }

    private class TopButtonAnimListener implements Animation.AnimationListener {

        private View view;
        private boolean isShowTop;
        public TopButtonAnimListener(View view, boolean isShowTop) {
            this.view = view;
            this.isShowTop = isShowTop;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (isShowTop) {
                view.setVisibility(View.VISIBLE);
                Log.i(tag, "视图显示");
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (!isShowTop){
                view.setVisibility(View.GONE);
                Log.i(tag, "视图隐藏");
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}

