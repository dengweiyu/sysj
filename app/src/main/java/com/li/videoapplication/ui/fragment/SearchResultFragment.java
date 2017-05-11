package com.li.videoapplication.ui.fragment;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Ints;
import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.event.SearchResultEvent;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.activity.SearchActivity;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.views.ViewPagerY4;
import com.ypy.eventbus.EventBus;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.rong.imageloader.utils.L;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 碎片：搜索结果
 */
public class SearchResultFragment extends TBaseFragment{

    //搜索结果
    private List<SearchResultEvent> mResults = Lists.newArrayList();

    private SearchActivity activity;
    private List<Fragment> fragments;
    private String content;
    private SearchVideoFragment video;
    private SearchGameFragment game;
    private SearchMemberFragment member;
    private SearchGiftFragment gift;

    private ViewPagerY4 viewPager;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (SearchActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getCreateView() {
        return R.layout.activity_searchresult;
    }

    @Override
    protected void initContentView(View view) {
        getIntent();

        initViewPager(view);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void getIntent() {
        Bundle arguments = getArguments();
        content = arguments.getString("content");
    }

    public void setContent(String content){
        this.content = content;
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }

    /**
     * 初始化菜单
     */
    private void initViewPager(View view) {
        if (fragments == null) {
            fragments = new ArrayList<>();

            game = SearchGameFragment.newInstance(content);
            video = SearchVideoFragment.newInstance(content);
            member = SearchMemberFragment.newInstance(content);
            gift = SearchGiftFragment.newInstance(content);

            fragments.add(game);
            fragments.add(video);
            fragments.add(member);
            fragments.add(gift);
        }else {
            refreshSearchResult(content);
        }


        String[] titles = {"游戏圈", "相关视频", "相关主播", "相关礼包"};

        viewPager = (ViewPagerY4) view.findViewById(R.id.viewpager);
        viewPager.setScrollable(true);
        viewPager.setOffscreenPageLimit(3);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //如果触摸滑动tab 直接取消tab页筛选
                if (positionOffsetPixels > 0){
                    mResults = null;
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void refreshSearchResult(String content) {

        mResults = Lists.newArrayList();
        video.setContent(content);
        game.setContent(content);
        member.setContent(content);
        gift.setContent(content);

    }

    /**
     *搜索结果
     */
    public void onEventMainThread(SearchResultEvent event){
        if (event == null || mResults == null){
            return;
        }
        mResults.add(event);
        //根据 position排序
        Ordering<SearchResultEvent> comparator = Ordering.natural().onResultOf(new Function<SearchResultEvent, Integer>() {
            @Override
            public Integer apply(SearchResultEvent from) {
                return from.getPosition();
            }
        });

        Collections.sort(mResults,comparator);

        getPositivePosition();
    }


    /**
     * 选择合适的tab页进行展示
     */
    private void getPositivePosition(){
        if (mResults == null){
            return;
        }
        for (int i = 0; i < mResults.size(); i++) {
            if (i == fragments.size()-1){
                if (activity != null)
                    activity.setLoading(false);
            }
            if (mResults.get(i).isResult() && i == mResults.get(i).getPosition()){
                viewPager.setCurrentItem(mResults.get(i).getPosition());
                if (activity != null)
                    activity.setLoading(false);
                mResults.clear();
                mResults = null;
                break;
            }
        }
    }
}
