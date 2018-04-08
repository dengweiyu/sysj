package com.li.videoapplication.ui.fragment;

import android.content.Context;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.GameTypeEntity;
import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.LogoutEvent;
import com.li.videoapplication.data.network.RequestConstant;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.adapter.ClassifiedAdapter;

import com.li.videoapplication.ui.adapter.GameTypeViewPagerAdapter;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.views.ViewPagerY4;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 碎片：游戏分类
 */
public class ClassifiedGameFragment extends TBaseFragment {




    private int page = 1;
    public String sort = RequestConstant.GAMELIST_SORT_HOT;
    private ClassifiedAdapter headerAdapter;
    private List<GroupType> headerData;
    private List<GameLazyColumnFragment> mFragment;
    //    private boolean eatChicken = false;
    private TextView show;
    private CommonNavigator commonNavigator;
    private MagicIndicator header_magicIndicator;
    private CommonPagerTitleView commmonPagerTitleView;
    private boolean isLoginStatusChange = false;
    private List<String> header_id;
    private List<String> header_name;
    private List<String> header_iv;
    private GameTypeViewPagerAdapter mGameTypeViewPagerAdapter;
    private ViewPagerY4 viewPagerY4;
    private int mCurrentPage = 0;
    private static final int PAGE_LIMIT = 1;

    @Override
    protected int getCreateView() {
        return R.layout.fragment_classifiedgame;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    @Override
    protected void initContentView(View view) {
        header_magicIndicator = (MagicIndicator) view.findViewById(R.id.header_magic_indicator);

        commonNavigator = new CommonNavigator(getContext());
        viewPagerY4 = (ViewPagerY4) view.findViewById(R.id.vp_game);
        Log.i("context", "" + getContext());


        loadData();


    }

    private void loadData() {
        HttpManager.getInstance().getGameTop2(topGameObserver);
    }

    private rx.Observer<GameTypeEntity> topGameObserver = new rx.Observer<GameTypeEntity>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(GameTypeEntity gameTypeEntity) {
            PreferencesHepler.getInstance().saveGameTypeEntity(gameTypeEntity);
            NetUtil.checkNCallBackData(gameTypeEntity);
            if (header_id == null) {
                header_id = new ArrayList<>();
            } else {
                header_id.clear();
            }
            if (header_name == null) {
                header_name = new ArrayList<>();
            } else {
                header_name.clear();
            }
            if (header_iv == null) {
                header_iv = new ArrayList<>();
            } else {
                header_iv.clear();
            }

            //把数据装入bean，根据id初始化加载fragment、viewpager、指示器
            Log.i(tag, gameTypeEntity.toString());
            if (gameTypeEntity != null) {
                for (GameTypeEntity.ADataBean dataBean : gameTypeEntity.getAData()) {
                    header_id.add(dataBean.getGroup_type_id());
                    header_name.add(dataBean.getGroup_type_name());
                    header_iv.add(dataBean.getFlag());
                }
//                for (String str : header_id) {
//                    if (str.contains("11")) {
//                        eatChicken = true;
//                    }
//                }
//                if (eatChicken) {
//                    String chickenName = header_name.get(0);
//                    String chickenId = header_id.get(0);
//                    String chickenIv = header_iv.get(0);
//                    Log.i("chicken", chickenName + " : " + chickenId);
//                    header_name.remove(0);
//                    header_id.remove(0);
//                    header_iv.remove(0);
//                    header_name.add(2, chickenName);
//                    header_id.add(2, chickenId);
//                    header_iv.add(2, chickenIv);
//                    Log.i("new List", header_name + "  " + header_id);
//
//                }

                for (String column : header_name) {
                    Log.d(tag, "header_name" + column);
                }
                for (String columnId : header_id) {
                    Log.d(tag, "header_id" + columnId);
                }
                for (String columnIv : header_iv) {
                    Log.d(tag, "header_iv" + columnIv);
                }
                if (!isLoginStatusChange) {
                    initFragment();
                    setViewPager();
                    setMagicAdapter();
                    Log.i(tag, "运行了!isLoginStatusChange");
                } else {
                    Log.i(tag, "运行了else");
                    initFragment();
                    commonNavigator.notifyDataSetChanged();
                    mGameTypeViewPagerAdapter.notifyDataSetChanged();
                    viewPagerY4.setCurrentItem(0);
                    isLoginStatusChange = false;
                }
            }
        }


    };

    private void setViewPager() {
        Log.w(tag, "setViewpager");
        mGameTypeViewPagerAdapter = new GameTypeViewPagerAdapter(getChildFragmentManager(), mFragment, new String[]{});
        viewPagerY4.setOffscreenPageLimit(PAGE_LIMIT);
        viewPagerY4.setAdapter(mGameTypeViewPagerAdapter);
        viewPagerY4.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFragment() {
        Log.w(tag, "initFragment");
        if (mFragment == null) {
            mFragment = new ArrayList<>();
        } else {
            mFragment.clear();
        }
        if (header_id != null) {
            for (int i = 0; i < header_id.size(); i++) {
                boolean isNeedLoadData = false;
                int offset = Math.abs(mCurrentPage - i);
                if (i < PAGE_LIMIT) {
                    isNeedLoadData = true;
                }
                Log.d("start-header_id", header_id.get(i));
                mFragment.add(GameLazyColumnFragment.newIstance(header_id.get(i),
                        false,
                        offset * 1000,
                        false));
            }
        }

    }

    private void setMagicAdapter() {
        commonNavigator.setAdapter(commonNavigatorAdapter);
        header_magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(header_magicIndicator, viewPagerY4);
    }


    /**
     * 事件：登录
     */
    public void onEventMainThread(LoginEvent event) {
        if (event != null) {
            page = 1;
            // 最热，最新游戏列表,请求只是为了刷新关注按钮状态
            DataManager.gameList(page, getMember_id(), sort);
        }
    }

    /**
     * 事件：注销
     */
    public void onEventMainThread(LogoutEvent event) {
        if (event != null) {
            page = 1;
            // 最热，最新游戏列表,请求只是为了刷新关注按钮状态
            DataManager.gameList(page, getMember_id(), sort);
        }
    }

    CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {
        @Override
        public int getCount() {
            return header_id.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int i) {
            commmonPagerTitleView = new CommonPagerTitleView(getContext());
            Log.i("context", ":" + context + "  i:" + i);
            commmonPagerTitleView.setContentView(R.layout.adapter_classified);
            final ImageView iv = (ImageView) commmonPagerTitleView.findViewById(R.id.classified_icon);
            final ImageView sellection = (ImageView) commmonPagerTitleView.findViewById(R.id.classified_sellection);
            final TextView tv = (TextView) commmonPagerTitleView.findViewById(R.id.classified_text);
            setImageViewImageNet(iv, header_iv.get(i));
            tv.setText(header_name.get(i));
            tv.setTextColor(getResources().getColor(R.color.menu_game_gray));
            commmonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                @Override
                public void onSelected(int i, int i1) {
                    tv.setTextColor(getResources().getColor(R.color.white));
                    sellection.setVisibility(View.VISIBLE);

                }

                @Override
                public void onDeselected(int i, int i1) {
                    tv.setTextColor(getResources().getColor(R.color.menu_game_gray));
                    sellection.setVisibility(View.GONE);


                }

                @Override
                public void onLeave(int i, int i1, float v, boolean b) {

                }

                @Override
                public void onEnter(int i, int i1, float v, boolean b) {

                }
            });
            commmonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPagerY4.setCurrentItem(i);
                }
            });

            return commmonPagerTitleView;
    }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            return null;
        }
    };

}
