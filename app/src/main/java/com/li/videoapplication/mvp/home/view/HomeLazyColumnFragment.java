package com.li.videoapplication.mvp.home.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.data.model.response.LoginEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.HomeMultipleAdapterNew;
import com.li.videoapplication.mvp.billboard.view.BillboardActivity;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.adapter.BannerAdapter;
import com.li.videoapplication.ui.adapter.BannerAdapterFor226;
import com.li.videoapplication.utils.GDTUtil;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.CircleFlowIndicator;
import com.li.videoapplication.views.DynamicHeightImageView;
import com.li.videoapplication.views.ViewFlow;
import com.meituan.android.walle.WalleChannelReader;
import com.qq.e.ads.nativ.NativeADDataRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import butterknife.BindView;
import io.rong.eventbus.EventBus;

/**
 * 分栏
 */

public class HomeLazyColumnFragment extends TBaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private static final String TAG = "HomeLazy";


    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private ScrollListener scrollListener;
    private View footerLoadView;

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

    private static List<HomeModuleEntity.ADataBean.ListBean> sData;         //所有数据

    private int mTotalPage;
    private int mPage = 1;
    private int mVideoGamerPage = 1;
    //尾部
    private View footerView;

    //FIXME 需要将其他类型的布局 参照 HomeMultipleAdapter来写
    private HomeMultipleAdapterNew mAdapter;

    private List<HomeModuleEntity.ADataBean> mData;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private boolean isInit = false; //真正显示的View是否已经被初始化
    private boolean isStartLoadGamerVideo = false;
    private boolean isGamerVideoLoadComplete = false;
    private boolean isLoadDataComplete = false; //第一次加载数据是否完成
    public static final String INTENT_BOOLEAN_LAZY_LOAD = "intent_boolean_lazy_load";

    private static boolean mIsNeedLoadData = false;
    private boolean isLazyLoad = true;
    private boolean isShowView = false; //是否已经展示过
    private static boolean isLoadMoreFlag = false; //判断是否是加载更多的flag
    private boolean isRefreshFlag = false;  //判断是否下拉刷新的flag



    private MainActivity mActivity;

    //滑动状态参数
    private float mOffset = 0f;
    private float mStartOffset = 0f;
    private float mLastDy = 0f;
    private boolean mIsShowMenu = true;

    public static HomeLazyColumnFragment newInstance(String columnId, boolean isNeedLoadData,
                                                     int delayTime, boolean isLazyLoad) {
        final HomeLazyColumnFragment fragment = new HomeLazyColumnFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_load_data", isNeedLoadData);
        bundle.putString("column_id", columnId);
        bundle.putBoolean(HomeLazyColumnFragment.INTENT_BOOLEAN_LAZY_LOAD, isLazyLoad);

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
        lazyLoadView(view);
    }

    private void lazyLoadView(View view) {
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
        if (mAdapter == null) {
            mAdapter = new HomeMultipleAdapterNew(mData);
        }
        footerLoadView = inflater.inflate(R.layout.view_loadmore_accent, null);
//        mAdapter.addFooterView(footerLoadView);
//        mAdapter.setEmptyView(inflater.inflate(R.layout.view_loadmore_accent, null));
        mAdapter.setOnLoadMoreListener(mLoadMoreListener);
        mRecyclerView.setAdapter(mAdapter);
        //TODO 这样 会导致 所有走到这里的fragment都会直接加载数据 应该限制一下
        //包括了数据的加载，转换，视图的处理
        if (!mIsNeedLoadData) {
            isLoadMoreFlag = false;
            loadData();
        }
    }

    /**
     * 获取网络数据
     */
    private void loadData() {
        // FIXME: 2017/11/21 应该加上page参数，做到选择加载页数
        Log.w("HomeLazy", "loadData->" + "mColumnId:" + mColumnId + "和mPage:" + mPage);
        DataManager.getHomeInfoById(mColumnId, mPage);
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

        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mAdapter == null) {
            mAdapter = new HomeMultipleAdapterNew(mData);
        }
        Map<String,Object> extra = entity.getExtra();
        if (extra == null){
            return;
        }
        //避免加载到 其他分栏的数据

        if (!mColumnId .equals(extra.get("column_id"))){
            return;
        }

        Log.d("HomeLazy", entity.getAData().size() + "");
        Log.w("HomeLazy", entity.toJSON());
        if (entity.isResult()) {
            mTotalPage = entity.getPage_count();

            Map<String, HomeModuleEntity.ADataBean> beanMap = new HashMap<>();

            for (HomeModuleEntity.ADataBean dataBean : entity.getAData()) {
                Log.w("HomeLazy", dataBean.toString());
                //FIXME 第一页 的数据一定要在第二页之前否则页面数据显示混乱
                //如果是标志位common的数据就添加到mData中
                if ("common".equalsIgnoreCase(dataBean.getFlag())) {
                    if (!dataBean.getMore_mark().equals("sysj_original")) {  //视界推荐的flag也是common
                        dataBean.setItemType(HomeModuleEntity.TYPE_VIDEO_GROUP);
                        Log.e(tag, "mData的大小：" + mData.size());
                        mData.add(dataBean);
                    } else {
                        dataBean.setItemType(HomeModuleEntity.TYPE_SYSJ_VIDEO);
                        Log.e(tag, "mData的大小：" + mData.size());
                        mData.add(dataBean);
                    }
                } else {//如果不是就添加到map中
                    beanMap.put(dataBean.getFlag(), dataBean);
                }
            }

            //banner
            if (beanMap.containsKey("banner")) {
                if (isInitBanner) {
                    //初始化banner
                    mAdapter.addHeaderView(getBannerView(beanMap.get("banner").getList()));
                    isInitBanner = false;
                } else {
                    mAdapter.removeAllHeaderView();
                    mAdapter.addHeaderView(getBannerView(beanMap.get("banner").getList()));
                }
            }

            if (beanMap.containsKey("hotGame")) {
                beanMap.get("hotGame").setItemType(HomeModuleEntity.TYPE_HOT_GAME);
                mData.add(beanMap.get("hotGame"));
            }
            if (beanMap.containsKey("guessVideo")) {
                beanMap.get("guessVideo").setItemType(HomeModuleEntity.TYPE_GUESS_YOU_LIKE);
                mData.add(beanMap.get("guessVideo"));
            }
            if (beanMap.containsKey("sysj_original")) {
                beanMap.get("sysj_original").setItemType(HomeModuleEntity.TYPE_SYSJ_VIDEO);
                mData.add(beanMap.get("sysj_original"));
            }
            if (beanMap.containsKey("hotMember")) {
                beanMap.get("hotMember").setItemType(HomeModuleEntity.TYPE_HOT_NARRATE);
                mData.add(beanMap.get("hotMember"));
            }
            if (beanMap.containsKey("common")) {
                beanMap.get("common").setItemType(HomeModuleEntity.TYPE_VIDEO_GROUP);
            }
            if (beanMap.containsKey("gamerVideo")) {
                isStartLoadGamerVideo = true;
                beanMap.get("gamerVideo").setItemType(HomeModuleEntity.TYPE_GAMER_VIDEO);
                sData = beanMap.get("gamerVideo").getList();
                Log.d("HomeLazy", sData.toString());
                if (beanMap.get("gamerVideo").getList().size() > 10) {
                    List sSaveData = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        sSaveData.add(beanMap.get("gamerVideo").getList().get(i));
                    }
                    Log.w(TAG, sData.toString());
                    beanMap.get("gamerVideo").setList(sSaveData);
                }
                mData.add(beanMap.get("gamerVideo"));
            }

            Log.w(TAG, mData.toString());
            if (mPage == 1 && mVideoGamerPage == 1 &&
                    swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
//            refreshData(sSaveData);
            if (!isLoadMoreFlag && !isRefreshFlag) {
                mAdapter.notifyDataSetChanged();
                isShowView = true;
                if (footerLoadView != null) {
                    footerLoadView.setVisibility(View.GONE);
                }
            } else {
                mAdapter.loadMoreComplete();
            }
            isLoadDataComplete = true;
            if (isRefreshFlag) {
                mAdapter.setNewData(mData);
                isRefreshFlag = false;
            }

            //保证第一页数据返回后再加载第二页

//            if (mPage == 1) {
//                for (int i = 0; i < page; i++) {
//                    mPage++;
//                    loadData();
//                }
//            }
        } else {
            //返回为结果处理
        }
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

            //annerFlow.setOnViewSwitchListener(this);
        }
        return bannerView;

        //  bannerFlow.setOnViewSwitchListener(this);
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
        isLoadDataComplete = false;
        isGamerVideoLoadComplete = false;
        isStartLoadGamerVideo = false;
        footerLoadView.setVisibility(View.VISIBLE);
        mPage = 1;
        mVideoGamerPage = 1;
        isRefreshFlag = true;
        isLoadMoreFlag = false;
        loadData();
        mAdapter.removeFooterView(getFootView());
    }

    @Override
    public void onLoadMoreRequested() {


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //可见，但还没初始化
        if (isVisibleToUser && !isInit && getBaseRootView() != null) {
            loadView();
            Log.w(tag, mColumnId + "->初始化....");
            isInit = true;
        }
        //已经初始化过
        if (isInit && getBaseRootView() != null) {
            if (isVisibleToUser) {
//                isShowView = true;
                if (!isShowView) {
                    mAdapter.notifyDataSetChanged();
                }
                Log.d("HomeLazyColumn", mColumnId + "已经初始化，并可见");
            } else {
//                isShowView = false;
                Log.d("HomeLazyColumn", mColumnId + "已经初始化，不可见");
            }
        }
    }

    OnItemChildClickListener mItemChildClickListener = new OnItemChildClickListener() {

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
                case R.id.hometype_sysj:
                    if (group != null) {
                        ActivityManager.startHomeMoreActivity(getActivity(), group);
                    }
                    break;
                case R.id.homehotgame_more:
                    EventBus.getDefault().post(Integer.valueOf(1));
                    break;
                case R.id.hometype_youlike_change:
                    if (!PreferencesHepler.getInstance().canVideoIdsTime()
                            && PreferencesHepler.getInstance().getVideoIds().size() > 0) {
                        List<String> videoIds = PreferencesHepler.getInstance().getVideoIds(4);
                        StringBuffer video_ids = new StringBuffer();
                        for (int j = 0; j < videoIds.size(); j++) {
                            video_ids.append(videoIds.get(position));
                            if (position < (videoIds.size() - 1)) {
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

    private void loadMoreGamerVideoData() {
        if (isLoadDataComplete && !isGamerVideoLoadComplete) {
            Log.w("HomeLazy", "加载更多玩家视频.....");
            Log.w("HomeLazy", "sData的大小是：" + sData.size());
            int videoGamerIndex = 0;
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getItemType() == HomeModuleEntity.TYPE_GAMER_VIDEO) {
                    videoGamerIndex = i;
                    break;
                }
            }

            List<HomeModuleEntity.ADataBean.ListBean> datas = new ArrayList<>();
            if ((mData.get(videoGamerIndex).getList().size() + 10) <=
                    sData.size()) {
                Log.w("HomeLazy", "+" + (mData.get(videoGamerIndex).getList().size() + 10));
                for (int i = 0; i < 10; i++) {
                    datas.add(sData.get((mVideoGamerPage - 1) * 10 + i));
                }
                if ((mData.get(videoGamerIndex).getList().size() + 10) == sData.size()) {
                    isGamerVideoLoadComplete = true;
                    footerLoadView.setVisibility(View.GONE);
                }
            } else {
                isGamerVideoLoadComplete = true;
                footerLoadView.setVisibility(View.GONE);
                for (int i = 0; i < (sData.size() - (mVideoGamerPage - 1) * 10); i++) {
                    datas.add(sData.get((mVideoGamerPage - 1) * 10 + i));
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

    private int lastVisibleItem;

    public class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                EventBus.getDefault().post(false);
                mAdapter.setScrolling(false);
                mAdapter.notifyDataSetChanged();
                Log.w(tag, "滚动停止，刷新..");
            } else {
                EventBus.getDefault().post(true);
                mAdapter.setScrolling(true);
            }
//            if (newState == RecyclerView.SCROLL_STATE_IDLE
//                    && lastVisibleItem + 1 == mAdapter.getItemCount()) {
//                Log.w(tag, "mAdapter的item大小：" + mAdapter.getItemCount());
//                Log.w(tag, "lastVisibleItem为：" +lastVisibleItem);
//                if (mPage < mTotalPage) {
//                    ++mPage;
//                    loadData();
//                }
//                if (isStartLoadGamerVideo) {
//                    loadMoreGamerVideoData();
//                }
//            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
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
        }
    }

    public BaseQuickAdapter.RequestLoadMoreListener mLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mPage >= mTotalPage) {
                        if (isStartLoadGamerVideo) {
                            loadMoreGamerVideoData();
                            Log.i(tag, "加载更多本地页..");
                            mAdapter.loadMoreComplete();
                            if (isGamerVideoLoadComplete) {
                                mAdapter.addFooterView(getFootView());
                                mAdapter.loadMoreEnd();
                            }
                        }
                    } else {
                        if (mPage < mTotalPage) {
                            ++mPage;
                            isLoadMoreFlag = true;
                            Log.d(tag, "加载更多页..");
                            loadData();
                        }
                    }
                }
            }, 500);
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
                    mAdapter.changeGuessVideo(newGuessList);
            }
        });
    }

    private void replaceGuessVideo2AD(List<VideoImage> newGuessList, VideoImage adItem) {
        if (newGuessList.size() == 4) {//防止连续点击造成个数问题
            newGuessList.remove(newGuessList.size() - 1);//移除第4条item

            newGuessList.add(adItem);//替换广告
            mAdapter.changeGuessVideo(newGuessList);
            if (!isClickLike){
//                mRecyclerView.scrollToPosition(0);           //不知道为何 更新了广告图后 会滚动到相应的item  所以直接回到0的位置
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
    public void onDestroy() {
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
        super.onDestroy();
    }
}

