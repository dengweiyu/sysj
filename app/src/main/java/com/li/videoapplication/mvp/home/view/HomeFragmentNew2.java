package com.li.videoapplication.mvp.home.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.data.model.entity.HomeColumnEntity;
import com.li.videoapplication.data.model.entity.HomeGameSelectEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.pageradapter.HomeViewPagerAdapter;
import com.li.videoapplication.ui.pageradapter.HomeViewPagerAdapter2;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.views.ViewPagerY4;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;


/**
 * 首页
 */

public class HomeFragmentNew2 extends TBaseFragment {
    public static final String HomeFragmentNew_FLAG = "HomeFragmentNew";
    public static LruCache<String, String> lruCache = new LruCache<>(40);
    private static final int PAGE_LIMIT = 1;
    private boolean otherGameFlag = false; //判断是否有其它游戏分栏。决定选择游戏后返回刷新的封拆逻辑
    private boolean isLoginStatusChange = false;

    private ViewPagerY4 mViewPager;
    private String member_id;
    private List<HomeLazyColumnFragment2> mFragments;
    private int mCurrentPage = 0;
    private List<String> mColumnList;
    private List<String> mColumnIdList;
    private MagicIndicator magicIndicator;
    private CommonNavigator commonNavigator;

    private HomeViewPagerAdapter2 mHomeViewPagerAdapter;
    private HomePageChangeListener mPageChangeListener;

    /**
     * 跳转：选择ITEM
     */
    public void startChoiceHomeTabActivity() {
        Log.d(tag, "starChoiceHomeTabActivity: ");
        ActivityManager.startChoiceHomeTabActivity(getContext());
    }

    @Override
    protected int getCreateView() {
        return R.layout.fragment_home_new;
    }

    /////////////////////////////控件的初始化与调用网络请求/////////////////////
    @Override
    protected void initContentView(View view) {
        Log.w(tag, "initContentView");

        //FIXME 需要处理滑动事件冲突的问题
        mViewPager = (ViewPagerY4) view.findViewById(R.id.vp_home);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_add_game);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到选择游戏中
                startChoiceHomeTabActivity();
            }
        });

        magicIndicator = (MagicIndicator) view.findViewById(R.id.magic_indicator);
        commonNavigator = new CommonNavigator(getContext());
        Log.w(tag, "loadData前.......");
        loadData();

    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    //////////////////////////////网络请求与响应的处理///////////////////////////
    public void loadData() {
        member_id = getMember_id();
//        DataManager.getHomeColumnByUser(member_id);
        HttpManager.getInstance().getTopIndexColumn2(member_id, topIndexColumnObserver);
    }

    private rx.Observer<HomeColumnEntity> topIndexColumnObserver = new rx.Observer<HomeColumnEntity>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(HomeColumnEntity entity) {
            PreferencesHepler.getInstance().saveHomeColumnEntity(entity);
            NetUtil.checkNCallBackData(entity);
            if (mColumnIdList == null) {
                mColumnIdList = new ArrayList<>();
            } else {
                mColumnIdList.clear();
            }
            if (mColumnList == null) {
                mColumnList = new ArrayList<>();
            } else {
                mColumnList.clear();
            }

            //拿到数据装入bean ，根据分栏ID初始化加载fragment,初始化VP和指示器
            Log.w(tag, entity.toString());
            if (entity != null) {
                for (HomeColumnEntity.ADataBean dataBean : entity.getAData()) {
                    mColumnList.add(dataBean.getName());//推荐,王者荣耀
                    mColumnIdList.add(dataBean.getId());//01,20，
                }
                for (String column : mColumnList) {
                    Log.d(tag, "column: " + column);
                }
                for (String columnId : mColumnIdList) {
                    Log.d(tag, "columnId: " + columnId);
                }
                if (!isLoginStatusChange) {
                    initFragment();
                    setViewpager();
                    setMagicIndicator();
                } else {
                    initFragment();
                    commonNavigatorAdapter.notifyDataSetChanged();
                    mHomeViewPagerAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(0);
                    isLoginStatusChange = false;
                }
            }
        }
    };

    public void onEventMainThread(StringBuffer flag) {
        Log.w(tag, "flag回调");
        if (flag.toString().equals(HomeFragmentNew_FLAG)) {
            Log.w(tag, "注销完成后返回重新加载数据");
            isLoginStatusChange = true;
            loadData();
        }
    }

    public void onEventMainThread(List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameBeanList) {
        for (int i = 0; i < myGameBeanList.size(); i++) {
            Log.i(tag, myGameBeanList.get(i).getName() + " : " + myGameBeanList.get(i).getColumn_id());
        }
        List<String> oldColumnIds = new ArrayList<>();
        for (int i = 0; i < mColumnIdList.size(); i++) {
            oldColumnIds.add(mColumnIdList.get(i));
        }
        List<HomeLazyColumnFragment2> oldFragments = new ArrayList<>();
        for (int i = 0; i < mFragments.size(); i++) {
            mFragments.get(i).setIsShowView(false);
            oldFragments.add(mFragments.get(i));
        }
        if (otherGameFlag) { //有其它游戏分栏
            //重新拆合，第一个是推荐，最后一个是其他游戏
            String recommendName = mColumnList.get(0);
            String recommendId = mColumnIdList.get(0);

            String otherGameName = mColumnList.get(mColumnList.size() - 1);
            String otherGameId = mColumnIdList.get(mColumnIdList.size() - 1);
            mColumnList.clear();
            mColumnIdList.clear();
            mColumnList.add(recommendName);
            mColumnIdList.add(recommendId);

            for (int i = 0; i < myGameBeanList.size(); i++) {
                mColumnList.add(myGameBeanList.get(i).getName());
                mColumnIdList.add(myGameBeanList.get(i).getColumn_id());
            }
            mColumnList.add(otherGameName);
            mColumnIdList.add(otherGameId);
        } else {   //无其它游戏分栏
            String recommendName = mColumnList.get(0);
            String recommendId = mColumnIdList.get(0);

            mColumnList.clear();
            mColumnIdList.clear();
            mColumnList.add(recommendName);
            mColumnIdList.add(recommendId);

            for (int i = 0; i < myGameBeanList.size(); i++) {
                mColumnList.add(myGameBeanList.get(i).getName());
                mColumnIdList.add(myGameBeanList.get(i).getColumn_id());
            }
        }

        mFragments.clear();

        for (int i = 0; i < mColumnIdList.size(); i++) {
            for (int j = 0; j < oldColumnIds.size(); j++) {
                if (oldColumnIds.get(j).equals(mColumnIdList.get(i))) {
                    mFragments.add(oldFragments.get(j));
                    Log.d(tag, "mFragments的id是 : " + mColumnIdList.get(i) + "->" + mColumnList.get(i) + "->" + oldFragments.get(j).getColumnId());
                    break;
                }
                if (j == oldColumnIds.size() - 1) {
                    mFragments.add(HomeLazyColumnFragment2.newInstance(mColumnIdList.get(i), false, 1000, false));
                    Log.d(tag, "mFragments的id是 : " + mColumnIdList.get(i) + "->" + mColumnList.get(i) + "->" + oldFragments.get(j).getColumnId());
                }
            }
        }
        oldPosition = mViewPager.getCurrentItem();
        commonNavigatorAdapter.notifyDataSetChanged();
        mHomeViewPagerAdapter.notifyDataSetChanged();
        mFragments.get(mViewPager.getCurrentItem()).notifyAdapter();

    }

    /////////////////////////////视图处理/////////////////////////////////
    public void setViewpager() {
        Log.w(tag, "setViewpager..");
        mHomeViewPagerAdapter = new HomeViewPagerAdapter2(getFragmentManager(), mFragments, new String[]{});
        mViewPager.setOffscreenPageLimit(PAGE_LIMIT);
        mViewPager.setAdapter(mHomeViewPagerAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener = new HomePageChangeListener());

    }

    public void setMagicIndicator() {
        Log.w(tag, "setMagicIndicator..");
        commonNavigator.setAdapter(commonNavigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private void initFragment() {
        Log.w(tag, "initFragment..");
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        } else {
            mFragments.clear();
        }
        //做延缓加载处理，防止出现视图重叠
//        if (mColumnIdList != null) {
//            for (int i = 0; i < mColumnIdList.size(); i++) {
//                boolean isNeedLoaData = false;
//                int offset = Math.abs(mCurrentPage - i);
////                if (offset <= 1){
////                    isNeedLoaData = true;
////                }
//                if (i < PAGE_LIMIT) {
//                    isNeedLoaData = true;
//                }
//                //TODO 懒加载
////                mFragments.add(HomeColumnFragment.newInstance(mColumnIdList.get(i),isNeedLoaData,
////                        offset*1000));
//                if (mColumnIdList.equals("6"))
//                    otherGameFlag = true; //6为其它游戏
//
//                mFragments.add(HomeLazyColumnFragment2.newInstance(
//                        mColumnIdList.get(i),
//                        isNeedLoaData,
//                        offset * 1000, !isNeedLoaData));
//
//            }
//        }
        mFragments.add(HomeLazyColumnFragment2.newInstance(
                "1",
                false,
                 0, false));
    }


    private int oldPosition = 0;
    private boolean isMakePageChange = true;
    private class HomePageChangeListener implements ViewPager.OnPageChangeListener {

        private boolean instant = false;
        private int bPosition;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            bPosition = position - oldPosition;
            if (oldPosition < mColumnIdList.size()){
                mFragments.set(oldPosition, HomeLazyColumnFragment2.newInstance(mColumnIdList.get(oldPosition), false, 1000, false));
            }
//                if (bPosition >= 2) {
//                    mFragments.set(oldPosition, HomeLazyColumnFragment3.newInstance(mColumnIdList.get(oldPosition), false, 1000, false));
//                } else {
//                    instant = true;
//                }
            oldPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {
        @Override
        public int getCount() {
//            return mColumnList == null ? 0 : mColumnList.size();
            return 1;
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
            colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
            colorTransitionPagerTitleView.setSelectedColor(Color.RED);
            colorTransitionPagerTitleView.setTextSize(16);
            //colorTransitionPagerTitleView.setTypeface();
            colorTransitionPagerTitleView.setText("推荐"); //mColumnList.get(index)
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
                return indicator*/
            ;
            //不用指示器
            return null;
        }
    };
}
