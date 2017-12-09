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
import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.data.model.entity.HomeColumnEntity;
import com.li.videoapplication.data.model.entity.HomeGameSelectEntity;
import com.li.videoapplication.data.model.entity.SquareGameEntity;
import com.li.videoapplication.data.model.response.HomeModuleEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.pageradapter.HomeViewPagerAdapter;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.NetUtil;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 首页
 */

public class HomeFragmentNew extends TBaseFragment {
    private static final int PAGE_LIMIT = 3;
    private ViewPagerY4 mViewPager;
    private String member_id;
    private List<HomeLazyColumnFragment3> mFragments;
    private int mCurrentPage = 0;
    private List<String> mColumnList;
    private List<String> mColumnIdList;
    private MagicIndicator magicIndicator;
    private CommonNavigator commonNavigator;

    private HomeViewPagerAdapter mHomeViewPagerAdapter;

    /**
     * 跳转：选择ITEM
     */
    public void starChoiceHomeTabActivity() {
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
        Log.w(tag, "initContentView");
        mColumnList = new ArrayList<>();
        mColumnIdList = new ArrayList<>();

        //FIXME 需要处理滑动事件冲突的问题
        mViewPager = (ViewPagerY4) view.findViewById(R.id.vp_home);
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
            NetUtil.checkNCallBackData(entity);
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
                initFragment();
                setViewpager();
                setMagicIndicator();
            }
        }
    };

    public void onEventMainThread(List<HomeGameSelectEntity.ADataBean.MyGameBean> myGameBeanList) {
        for (int i = 0; i < myGameBeanList.size(); i++) {
            Log.i(tag, myGameBeanList.get(i).getName() + " : " + myGameBeanList.get(i).getColumn_id());
        }
        List<String> oldColumnIds = new ArrayList<>();
        for (int i = 0; i < mColumnIdList.size(); i++) {
            oldColumnIds.add(mColumnIdList.get(i));
        }
        List<HomeLazyColumnFragment3> oldFragments = new ArrayList<>();
        for (int i = 0; i < mFragments.size(); i++) {
            oldFragments.add(mFragments.get(i));
        }
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

        mFragments.clear();

        for (int i = 0; i < mColumnIdList.size(); i++) {
            for (int j = 0; j < oldColumnIds.size(); j++) {
                if (oldColumnIds.get(j).equals(mColumnIdList.get(i))) {
                    mFragments.add(oldFragments.get(j));
                    Log.d(tag, "mFragments的id是 : " + mColumnIdList.get(i) + "->" + mColumnList.get(i) + "->" + oldFragments.get(j).getColumnId());
                    break;
                }
                if (j == oldColumnIds.size() - 1) {
                    mFragments.add(HomeLazyColumnFragment3.newInstance(mColumnIdList.get(i), false, 1000, true));
                    Log.d(tag, "mFragments的id是 : " + mColumnIdList.get(i) + "->" + mColumnList.get(i)  + "->" + oldFragments.get(j).getColumnId());
                }

            }
        }
        commonNavigatorAdapter.notifyDataSetChanged();
        mHomeViewPagerAdapter.notifyDataSetChanged();
        oldColumnIds = null;
        oldFragments =  null;
    }

    /////////////////////////////视图处理/////////////////////////////////
    public void setViewpager() {
        Log.w(tag, "setViewpager..");
        mHomeViewPagerAdapter = new HomeViewPagerAdapter(getFragmentManager(), mFragments, new String[]{});
        mViewPager.setOffscreenPageLimit(PAGE_LIMIT);
        mViewPager.setAdapter(mHomeViewPagerAdapter);
    }

    public void setMagicIndicator() {
        Log.w(tag, "setMagicIndicator..");
        commonNavigator.setAdapter(commonNavigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private void initFragment() {
        Log.w(tag, "initFragment..");
        mFragments = new ArrayList<>();
        //做延缓加载处理，防止出现视图重叠
        if (mColumnIdList != null) {
            for (int i = 0; i < mColumnIdList.size(); i++) {
                boolean isNeedLoaData = false;
                int offset = Math.abs(mCurrentPage - i);
//                if (offset <= 1){
//                    isNeedLoaData = true;
//                }
                if (i < PAGE_LIMIT) {
                    isNeedLoaData = true;
                }
                //TODO 懒加载
//                mFragments.add(HomeColumnFragment.newInstance(mColumnIdList.get(i),isNeedLoaData,
//                        offset*1000));
                mFragments.add(HomeLazyColumnFragment3.newInstance(
                        mColumnIdList.get(i),
                        isNeedLoaData,
                        offset * 1000, true));

            }
        }

    }

    CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {
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
                return indicator*/
            ;
            //不用指示器
            return null;
        }
    };
}
