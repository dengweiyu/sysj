package com.li.videoapplication.mvp.home.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.HomeColumnEntity;
import com.li.videoapplication.data.model.entity.SquareGameEntity;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.views.ViewPagerY4;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;



/**
 *首页
 */

public class HomeFragmentNew extends TBaseFragment {

    private ViewPagerY4 mViewPager;
    private String member_id;
    private List<Fragment> mFragments;
    private int mCurrentPage = 0;
    private List<String> mColumnList;
    private List<String> mColumnIdList;
    private MagicIndicator magicIndicator;
    private CommonNavigator commonNavigator;
    /**
     * 跳转：选择ITEM
     */
    public void starChoiceHomeTabActivity( ) {
        Log.d(tag, "starChoiceHomeTabActivity: ");
        ActivityManager.starChoiceHomeTabActivity(getContext());
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_home_new;
    }
    /////////////////////////////控件的初始化与调用网络请求/////////////////////
    @Override
    protected void initContentView(View view) {
        mColumnList=new ArrayList<>();
        mColumnIdList=new ArrayList<>();
        //FIXME 需要处理滑动事件冲突的问题
        mViewPager = (ViewPagerY4)view.findViewById(R.id.vp_home);
        ImageView imageView = (android.widget.ImageView) view.findViewById(R.id.iv_add_game);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到选择游戏中
                starChoiceHomeTabActivity();
            }
        });
        magicIndicator = (MagicIndicator) view.findViewById(R.id.magic_indicator);
        commonNavigator = new CommonNavigator(getContext());
        loadData();


    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }
    //////////////////////////////网络请求与响应的处理///////////////////////////
    public void loadData(){
        member_id=getMember_id();
        DataManager.getHomeColumnByUser(member_id);
    }
    public void onEventMainThread(HomeColumnEntity entity){
        //拿到数据装入bean ，根据分栏ID初始化加载fragment,初始化VP和指示器


        if (entity!=null){
            for (HomeColumnEntity.ADataBean dataBean :entity.getAData() ) {
                mColumnList.add(dataBean.getName());
                mColumnIdList.add(dataBean.getId());
            }
            initFragment();
            setViewpager();
            setMagicIndicator();

        }





    }
    /////////////////////////////视图处理/////////////////////////////////
    public void setViewpager(){

        mViewPager.setOffscreenPageLimit(mColumnIdList.size());
        mViewPager.setAdapter(new ViewPagerAdapter(getFragmentManager(),mFragments,new String []{}));
    }
    public  void setMagicIndicator(){
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mColumnList == null ? 0 : mColumnList.size();
            }
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.RED);
                colorTransitionPagerTitleView.setTextSize(16);
                //colorTransitionPagerTitleView.setTypeface();
                colorTransitionPagerTitleView.setText(mColumnList.get(index));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(index);
                    }
                });

                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                /*LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator*/;
                //不用指示器
                return null;
            }
        });

        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);

    }
    private void initFragment() {
        mFragments = new ArrayList<>();
        //做延缓加载处理，防止出现视图重叠
        if (mColumnIdList!=null){
            for (int i = 0; i <mColumnIdList.size() ; i++) {
                boolean isNeedLoaData = false;
                int offset = Math.abs(mCurrentPage - i);
                if (offset <= 1){
                    isNeedLoaData = true;
                }
                mFragments.add(HomeColumnFragment.newInstance(mColumnIdList.get(i),isNeedLoaData,offset*1000));
            }
        }


    }
}
