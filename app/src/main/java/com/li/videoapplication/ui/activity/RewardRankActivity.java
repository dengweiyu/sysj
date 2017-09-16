package com.li.videoapplication.ui.activity;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.ui.fragment.SendRewardRankFragment;
import com.li.videoapplication.ui.fragment.VideoRewardFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 打赏榜
 */

public class RewardRankActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    private List<Fragment> mFragments;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;

    private List<TextView> mTileText;
    private List<ImageView> mTitleIcon;
    private List<ImageView> mTitleBottomLine;


    private String[] mTitleStr ={
            "土豪榜",
            "人气榜",
            "视频榜"
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_reward_rank;
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
        mViewPager = (ViewPager)findViewById(R.id.vp_reward_rank);
        mFragments = new ArrayList<>();
        mFragments.add(SendRewardRankFragment.newInstance(SendRewardRankFragment.TYPE_SEND));
        mFragments.add(SendRewardRankFragment.newInstance(SendRewardRankFragment.TYPE_RECEIVED));
        mFragments.add(new VideoRewardFragment());
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(),mFragments,null);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnPageChangeListener(mListener);

        findViewById(R.id.top_left).setOnClickListener(this);
        findViewById(R.id.top_middle).setOnClickListener(this);
        findViewById(R.id.top_right).setOnClickListener(this);

        mTileText = new ArrayList<>();
        mTitleIcon = new ArrayList<>();
        mTitleBottomLine = new ArrayList<>();
        mTileText.add((TextView)findViewById(R.id.top_left_text));
        mTileText.add((TextView)findViewById(R.id.top_middle_text));
        mTileText.add((TextView)findViewById(R.id.top_right_text));

        mTitleIcon.add((ImageView)findViewById(R.id.top_left_icon));
        mTitleIcon.add((ImageView)findViewById(R.id.top_middle_icon));
        mTitleIcon.add((ImageView)findViewById(R.id.top_right_icon));

        mTitleBottomLine.add((ImageView)findViewById(R.id.top_left_line));
        mTitleBottomLine.add((ImageView)findViewById(R.id.top_middle_line));
        mTitleBottomLine.add((ImageView)findViewById(R.id.top_right_line));


        mTileText.get(0).setText(mTitleStr[0]);
        mTileText.get(1).setText(mTitleStr[1]);
        mTileText.get(2).setText(mTitleStr[2]);

        refreshTab(0);
    }

    private void initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tb_title)).setText("打赏榜");
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));

    }

    final ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            refreshTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int mDefaultColor = Color.parseColor("#bfbfbf");
    private void refreshTab(int index){
        switch (index){
            case 0:
                mTileText.get(0).setTextColor(Color.parseColor("#f9b601"));
                mTileText.get(1).setTextColor(mDefaultColor);
                mTileText.get(2).setTextColor(mDefaultColor);

                mTitleIcon.get(0).setImageResource(R.drawable.send_reward_selected);
                mTitleIcon.get(1).setImageResource(R.drawable.receive_reward_unselected);
                mTitleIcon.get(2).setImageResource(R.drawable.video_reward_unselected);

                mTitleBottomLine.get(0).setImageResource(R.drawable.send_reward_bottom_selected);
                mTitleBottomLine.get(1).setImageResource(0);
                mTitleBottomLine.get(2).setImageResource(0);
                break;
            case 1:
                mTileText.get(0).setTextColor(mDefaultColor);
                mTileText.get(1).setTextColor(Color.parseColor("#ff9150"));
                mTileText.get(2).setTextColor(mDefaultColor);

                mTitleIcon.get(0).setImageResource(R.drawable.send_reward_unselected);
                mTitleIcon.get(1).setImageResource(R.drawable.receive_reward_selected);
                mTitleIcon.get(2).setImageResource(R.drawable.video_reward_unselected);

                mTitleBottomLine.get(0).setImageResource(0);
                mTitleBottomLine.get(1).setImageResource(R.drawable.receivce_reward_bottom_selected);
                mTitleBottomLine.get(2).setImageResource(0);
                break;
            case 2:
                mTileText.get(0).setTextColor(mDefaultColor);
                mTileText.get(1).setTextColor(mDefaultColor);
                mTileText.get(2).setTextColor(Color.parseColor("#40a7ff"));


                mTitleIcon.get(0).setImageResource(R.drawable.send_reward_unselected);
                mTitleIcon.get(1).setImageResource(R.drawable.receive_reward_unselected);
                mTitleIcon.get(2).setImageResource(R.drawable.video_reward_selected);

                mTitleBottomLine.get(0).setImageResource(0);
                mTitleBottomLine.get(1).setImageResource(0);
                mTitleBottomLine.get(2).setImageResource(R.drawable.video_reward_bottom_selected);
                break;
        }
    }

    @Override
    public void loadData() {
        super.loadData();
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_left:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.top_middle:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.top_right:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.tb_back:
                finish();
                break;
        }
    }
}
