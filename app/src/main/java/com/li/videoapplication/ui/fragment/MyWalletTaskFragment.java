package com.li.videoapplication.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;

import com.handmark.pulltorefresh.library.IPullToRefresh;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.MemberTaskEntity;
import com.li.videoapplication.framework.TBaseFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import io.rong.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 钱包任务
 */

public class MyWalletTaskFragment extends TBaseFragment {

    public ViewPager mPager;
    private List<Fragment> mFragments;
    private DailyRewardFragment mDailyReward;
    private MatchRewardFragment mMatchReward;
    private HonorRewardFragment mHonorReward;
    @Override
    protected int getCreateView() {
        return R.layout.fragment_my_wallet_task;
    }

    @Override
    protected void initContentView(View view) {

        mPager = (ViewPager)view.findViewById(R.id.vp_my_wallet_task);

        if (mFragments == null){
            mDailyReward = new DailyRewardFragment();
            mMatchReward = new MatchRewardFragment();
            mHonorReward = new HonorRewardFragment();
            mFragments = new ArrayList<>();
            mFragments.add(mDailyReward);
            mFragments.add(mMatchReward);
            mFragments.add(mHonorReward);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), mFragments, new String[]{"每日奖励","荣誉奖励","赛事奖励"});
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tb_my_wallet_task) ;
        tabLayout.setupWithViewPager(mPager);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    /**
     * 回调：会员任务接口
     */
    public void onEventMainThread(MemberTaskEntity event) {

        if (event != null && event.isResult()) {
            setFragmentData(event);
        }
    }

    private void setFragmentData(MemberTaskEntity event) {
        mDailyReward.setDailyData(event.getDaily());
        mDailyReward.setNoviceData(event.getNovice());
        mMatchReward.setMatchData(event.getEvents());
        mHonorReward.setHonorData(event.getHonor());
    }

    @Override
    protected IPullToRefresh getPullToRefresh() {
        return null;
    }
}
