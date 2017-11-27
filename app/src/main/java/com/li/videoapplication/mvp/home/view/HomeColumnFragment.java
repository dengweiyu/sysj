package com.li.videoapplication.mvp.home.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.BaseReceiver;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.HomeMultipleAdapterNew;
import com.li.videoapplication.ui.adapter.BannerAdapter;
import com.li.videoapplication.ui.adapter.BannerAdapterFor226;
import com.li.videoapplication.ui.fragment.NewSquareFragment;
import com.li.videoapplication.views.CircleFlowIndicator;
import com.li.videoapplication.views.DynamicHeightImageView;
import com.li.videoapplication.views.ViewFlow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.li.videoapplication.R.id.recyclerView;

/**
 * 分栏
 */

public class HomeColumnFragment extends TBaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView mRecyclerView;
    //顶部广告
    private View bannerView;
    private ViewFlow bannerFlow;
    private BannerAdapterFor226 bannerAdapter;
    private String mColumnId;

    private int mPage = 1;
    //尾部
    private View footerView;

    //FIXME 需要将其他类型的布局 参照 HomeMultipleAdapter来写
    private HomeMultipleAdapterNew  mAdapter;

    private List<HomeModuleEntity.ADataBean> mData;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    public static HomeColumnFragment newInstance(String columnId,boolean isNeedLoadData,int delayTime){
        final HomeColumnFragment fragment = new HomeColumnFragment();

        Bundle bundle = new Bundle();

        bundle.putString("column_id",columnId);

        fragment.setArguments(bundle);
        if (isNeedLoadData){
            UITask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragment.loadData();
                }
            },delayTime);
        }
        return fragment;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initContentView(View view) {

        Bundle bundle = getArguments();
        if (bundle != null){
            mColumnId = bundle.getString("column_id","1");
            //拿到了分栏ID,根据ID来传入不同的适配器
        }
        initRecyclerView(view);
        mData = new ArrayList<>();
        mAdapter = new HomeMultipleAdapterNew(mData);
        mRecyclerView.setAdapter(mAdapter);
        //TODO 这样 会导致 所有走到这里的fragment都会直接加载数据 应该限制一下
        //包括了数据的加载，转换，视图的处理
        loadData();
    }

    /**
     *获取网络数据
     */
    private void  loadData(){
        // FIXME: 2017/11/21 应该加上page参数，做到选择加载页数
        DataManager.getHomeInfoById(mColumnId,mPage);
    }

    /**
     * 初始化数据，做缓存判断，优化，不直接加载数据
     */
    public void initData(){
        mPage=1;
        HomeModuleEntity homeModuleEntity = PreferencesHepler.getInstance().getHomeEntity();
        if (homeModuleEntity==null){
            //没有数据，加载新的数据
            loadData();
        }else {
            //调用本地缓存
            // TODO
            //再次加载数据
            loadData();
        }
    }


    public void onEventMainThread(HomeModuleEntity entity){

        Bundle bundle = entity.getExtra();
        if (bundle == null){
            return;
        }
        //避免加载到 其他分栏的数据

        if (!mColumnId .equals(bundle.getString("column_id"))){
            return;
        }

        if (entity.isResult()){
            int page=entity.getPage_count();

            Map<String,HomeModuleEntity.ADataBean> beanMap = new HashMap<>();

            for (HomeModuleEntity.ADataBean dataBean: entity.getAData()) {

                //FIXME 第一页 的数据一定要在第二页之前否则页面数据显示混乱
                //如果是标志位common的数据就添加到mData中
                if ("common".equalsIgnoreCase(dataBean.getFlag())){
                    dataBean.setItemType(HomeModuleEntity.TYPE_VIDEO_GROUP);
                    mData.add(dataBean);
                }else {//如果不是就添加到map中
                    beanMap.put(dataBean.getFlag(),dataBean);
                }
            }

            //banner
            if (beanMap.containsKey("banner")){
                //初始化banner
                mAdapter.addHeaderView(getBannerView(beanMap.get("banner").getList()));
            }

            if (beanMap.containsKey("hotGame")){
                beanMap.get("hotGame").setItemType(HomeModuleEntity.TYPE_HOT_GAME);
                mData.add(beanMap.get("hotGame"));

            }
            if (beanMap.containsKey("guessVideo")){
                beanMap.get("guessVideo").setItemType(HomeModuleEntity.TYPE_GUESS_YOU_LIKE);
                mData.add(beanMap.get("guessVideo"));
            }
            if (beanMap.containsKey("sysj_original")){
                beanMap.get("sysj_original").setItemType(HomeModuleEntity.TYPE_SYSJ_VIDEO);

                mData.add(beanMap.get("sysj_original"));
            }
            if (beanMap.containsKey("hotMember")){
                beanMap.get("hotMember").setItemType(HomeModuleEntity.TYPE_HOT_NARRATE);
                mData.add(beanMap.get("hotMember"));
            }
            if (beanMap.containsKey("common")){
                beanMap.get("common").setItemType(HomeModuleEntity.TYPE_VIDEO_GROUP);
            }
            if (beanMap.containsKey("gamerVideo")){
                beanMap.get("gamerVideo").setItemType(HomeModuleEntity.TYPE_GAMER_VIDEO);
                mData.add(beanMap.get("gamerVideo"));
            }

            mAdapter.notifyDataSetChanged();



            //保证第一页数据返回后再加载第二页

            if (mPage == 1){
                for (int i = 0; i <page ; i++) {
                    mPage++;
                    loadData();
                }
            }
        }else {

        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }
    ////////////////初始化/////////////////
    private void initRecyclerView(View view) {

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

    }

    @Override
    public void onLoadMoreRequested() {

    }
}
