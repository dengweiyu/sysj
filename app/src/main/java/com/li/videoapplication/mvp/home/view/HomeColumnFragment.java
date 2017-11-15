package com.li.videoapplication.mvp.home.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.mvp.adapter.HomeMultipleAdapterNew;
import com.li.videoapplication.ui.adapter.BannerAdapter;
import com.li.videoapplication.views.CircleFlowIndicator;
import com.li.videoapplication.views.ViewFlow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分栏
 */

public class HomeColumnFragment extends TBaseFragment {

    private RecyclerView mRecyclerView;

    private String mColumnId;

    private int mPage = 1;

    //FIXME 需要将其他类型的布局 参照 HomeMultipleAdapter来写
    private HomeMultipleAdapterNew  mAdapter;

    private List<HomeModuleEntity.ADataBean> mData;

    public static HomeColumnFragment newInstance(String columnId){

        HomeColumnFragment fragment = new HomeColumnFragment();

        Bundle bundle = new Bundle();
        bundle.putString("column_id",columnId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_home_column;
    }

    @Override
    protected void initContentView(View view) {

        Bundle bundle = getArguments();
        if (bundle != null){
            mColumnId = bundle.getString("column_id","1");
        }

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_home_column);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mData = new ArrayList<>();
        mAdapter = new HomeMultipleAdapterNew(mData);
        mRecyclerView.setAdapter(mAdapter);
        //TODO 这样 会导致 所有走到这里的fragment都会直接加载数据 应该限制一下
        loadData();
    }

    /**
     *
     */
    private void  loadData(){
        DataManager.getHomeInfoById(mColumnId,mPage);
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

            Map<String,HomeModuleEntity.ADataBean> beanMap = new HashMap<>();

            for (HomeModuleEntity.ADataBean dataBean: entity.getAData()) {

                //FIXME 第一页 的数据一定要在第二页之前否则页面数据显示混乱
                if ("common".equalsIgnoreCase(dataBean.getFlag())){
                    dataBean.setItemType(HomeModuleEntity.TYPE_VIDEO_GROUP);
                    mData.add(dataBean);
                }else {
                    beanMap.put(dataBean.getFlag(),dataBean);
                }
            }

            //banner
            if (beanMap.containsKey("banner")){
                //初始化banner
                mAdapter.addHeaderView(getBannerView(coverBannerData(beanMap.get("banner").getList())));
            }

            if (beanMap.containsKey("hotGame")){
                beanMap.get("hotGame").setItemType(HomeModuleEntity.TYPE_HOT_GAME);
                mData.add(beanMap.get("hotGame"));
            }
            if (beanMap.containsKey("guessVideo")){
                beanMap.get("hotGame").setItemType(HomeModuleEntity.TYPE_GUESS_YOU_LIKE);
                mData.add(beanMap.get("guessVideo"));
            }
            if (beanMap.containsKey("sysj_original")){
                beanMap.get("hotGame").setItemType(HomeModuleEntity.TYPE_SYSJ_VIDEO);

                mData.add(beanMap.get("sysj_original"));
            }
            if (beanMap.containsKey("hotMember")){
                beanMap.get("hotGame").setItemType(HomeModuleEntity.TYPE_HOT_NARRATE);

                mData.add(beanMap.get("hotMember"));
            }

            if (beanMap.containsKey("common")){
                beanMap.get("hotGame").setItemType(HomeModuleEntity.TYPE_VIDEO_GROUP);

                mData.add(beanMap.get("hotMember"));
            }

            mAdapter.notifyDataSetChanged();


            //保证第一页数据返回后再加载第二页
            if (mPage == 1){
                mPage++;
                loadData();
            }
        }else {

        }
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }


    private View getBannerView(List<Banner> bannerData) {

        View bannerView = inflater.inflate(R.layout.header_home_banner, null);
        setBannerView(bannerView);
        ViewFlow bannerFlow = (ViewFlow) bannerView.findViewById(R.id.viewflow);
        CircleFlowIndicator bannerIndicator = (CircleFlowIndicator) bannerView.findViewById(R.id.circleflowindicator);
        bannerFlow.setSideBuffer(4); // 初始化轮播图张数
        bannerFlow.setFlowIndicator(bannerIndicator);
        bannerFlow.setTimeSpan(4000);
        bannerFlow.setSelection(0); // 设置初始位置

        BannerAdapter bannerAdapter = new BannerAdapter(getActivity(), bannerData, BannerAdapter.PAGER_HOME);
        bannerFlow.setAdapter(bannerAdapter);

      //  bannerFlow.setOnViewSwitchListener(this);
        return bannerView;
    }

    private void setBannerView(View view) {
        //750:350 = 15:7
        int w = srceenWidth;
        int h = w * 7 / 15;
        setListViewLayoutParams(view, w, h);
    }

    //FIXME 列子而已  最好是重写BannerAdapter 来适配数据
    private List<Banner> coverBannerData(List<HomeModuleEntity.ADataBean.ListBean> data){
        List<Banner> bannerData = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            Banner banner = new Banner();
            HomeModuleEntity.ADataBean.ListBean dataBean = data.get(i);

            banner.setType(dataBean.getType());
            banner.setVideo_id(dataBean.getVideo_id());
            banner.setPackage_id(dataBean.getPackage_id());

            banner.setFlag(dataBean.getFlag());
            banner.setFlagPath(dataBean.getFlagPath());
            banner.setTitle(dataBean.getTitle());
            banner.setUrl(dataBean.getUrl());
            banner.setQn_key(dataBean.getQn_key());

            bannerData.add(banner);
        }

        return bannerData;
    }
}
