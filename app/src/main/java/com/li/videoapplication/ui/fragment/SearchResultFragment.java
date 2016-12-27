package com.li.videoapplication.ui.fragment;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.activity.SearchActivity;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.views.ViewPagerY4;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 碎片：搜索结果
 */
public class SearchResultFragment extends TBaseFragment{

    private SearchActivity activity;
    private List<Fragment> fragments;
    private String content;
    private SearchVideoFragment video;
    private SearchGameFragment game;
    private SearchMemberFragment member;
    private SearchGiftFragment gift;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = (SearchActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void getIntent() {
        Bundle arguments = getArguments();
        content = arguments.getString("content");
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

            video = SearchVideoFragment.newInstance(content);
            game = SearchGameFragment.newInstance(content);
            member = SearchMemberFragment.newInstance(content);
            gift = SearchGiftFragment.newInstance(content);

            fragments.add(video);
            fragments.add(game);
            fragments.add(member);
            fragments.add(gift);
        }
        String[] titles = {"相关视频", "相关游戏", "相关主播", "相关礼包"};

        ViewPagerY4 viewPager = (ViewPagerY4) view.findViewById(R.id.viewpager);
        viewPager.setScrollable(true);
        viewPager.setOffscreenPageLimit(3);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void refreshSearchResult(String content) {
        video.setContent(content);
        game.setContent(content);
        member.setContent(content);
        gift.setContent(content);
    }
}
