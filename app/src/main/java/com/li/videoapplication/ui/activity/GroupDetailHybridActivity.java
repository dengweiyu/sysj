package com.li.videoapplication.ui.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.li.videoapplication.BuildConfig;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.event.LoginEvent;
import com.li.videoapplication.data.model.event.RefreshCommentPagerEvent;
import com.li.videoapplication.data.model.event.ReleaseScrollEvent;
import com.li.videoapplication.data.model.response.GroupAttentionGroupEntity;
import com.li.videoapplication.data.model.response.GroupHybridDetailEntity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.TabLayoutHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.fragment.GroupDetailHybridFragment;
import com.li.videoapplication.ui.fragment.GroupDetailHybridVideoFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.views.ViewPagerY4;
import com.qq.e.comm.util.Md5Util;
import com.umeng.analytics.AnalyticsConfig;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 圈子详情  主要是HTML页面
 */

public class GroupDetailHybridActivity extends TBaseAppCompatActivity implements View.OnClickListener {

    private String mGroupId;

    private ViewPagerY4 mViewPager;

    private GroupHybridDetailEntity mDetailEntity;

    private List<Fragment> mFragments;

    private List<String> mTitle;

    private ImageView mGroupIcon;

    private TextView mGroupName;

    private TextView mVideoNumber;

    private TextView mPlayerNumber;

    private ImageView mFollowIcon;

    private TextView mFollowText;

    private LinearLayout mFollowLayout;

    private TabLayout mTabLayout;

    public boolean mIsSingleEvent = false;//是否需要将埋点独立
    @Override
    public void refreshIntent() {
        super.refreshIntent();

        mGroupId = getIntent().getStringExtra("group_id");

        if (StringUtil.isNull(mGroupId)){
            finish();
        }

    }

    @Override
    public void loadData() {
        super.loadData();

        DataManager.getHybridGroupDetail(mGroupId,getMember_id(), BuildConfig.VERSION_NAME);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_group_detail_hybrid;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //释放滚动
        if (event.getAction() == MotionEvent.ACTION_UP){
            mViewPager.setScrollable(true);
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public void initView() {
        super.initView();
        initToolbar();
        findViewById(R.id.iv_record_screen).setOnClickListener(this);
        mViewPager = (ViewPagerY4)findViewById(R.id.vp_detail_pager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGroupIcon = (ImageView)findViewById(R.id.iv_group_detail_icon);
        mGroupName = (TextView)findViewById(R.id.tv_group_detail_name);
        mVideoNumber = (TextView)findViewById(R.id.tv_group_detail_video_number);
        mPlayerNumber = (TextView)findViewById(R.id.tv_group_detail_video_player);

        mFollowLayout = (LinearLayout)findViewById(R.id.ll_follow_game);
        mFollowText = (TextView)findViewById(R.id.tv_follow_text);
        mFollowIcon = (ImageView)findViewById(R.id.iv_follow_icon);
        mFollowLayout.setOnClickListener(this);

        if (isNeedHideDownload(this)){
            findViewById(R.id.tv_open_game).setVisibility(View.GONE);
        }else {
            findViewById(R.id.tv_open_game).setOnClickListener(this);
        }

        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(mViewPager));
        mFragments = new ArrayList<>();
        mTitle = new ArrayList<>();


    }

    private void initToolbar(){
        findViewById(R.id.tb_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tb_title)).setText("游戏圈");
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));
    }


    /**
     *
     */
    private boolean isNeedHideDownload(Context context){
        String channel = AnalyticsConfig.getChannel(context);
        if ("huawei".equals(channel)|| "xiaomi".equalsIgnoreCase(channel) || "anzhi".equalsIgnoreCase(channel)){
            return true;
        }
        return false;
    }

    private void initFragment(){
        if (mDetailEntity != null && mFragments.size() == 0){
            for (int i = 0; i < mDetailEntity.getAData().size(); i++) {
                mTitle.add(mDetailEntity.getAData().get(i).getName());

                if ("video".equals(mDetailEntity.getAData().get(i).getFlagName())){
                    mFragments.add(GroupDetailHybridVideoFragment.newInstance(mDetailEntity.getOData().getGroup_id()));
                }else {
                    mFragments.add(GroupDetailHybridFragment.newInstance(coverUrl(mDetailEntity.getAData().get(i).getGoUrl(),mDetailEntity.getAData().get(i).getGame_id()),true));
                }
            }
            mViewPager.setOffscreenPageLimit(5);
            mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),mFragments,mTitle.toArray(new String[]{})));
        }
    }


    @Override
    protected void onDestroy(){
        if (mFragments != null){
            for (int i = 0; i < mFragments.size(); i++) {
                 if (mFragments.get(i) instanceof GroupDetailHybridFragment){
                     if (((GroupDetailHybridFragment)mFragments.get(i)).logout()){
                         break;
                     }
                 }
            }
        }
        super.onDestroy();
    }

    //拼接URL
    private String coverUrl(String url,String gameId){

        String sign = "game_id="+gameId+(StringUtil.isNull(getMember_id())?"":"&member_id="+getMember_id())+"&target=a_sysj&key=sysj";
        url += "?sign="+Md5Util.encode(sign).toUpperCase()+"&"+sign;
        return url;
    }


    public void setFragmentByFlagName(String flagName){
        if (StringUtil.isNull(flagName)){
            return;
        }
        if (mViewPager == null || mDetailEntity == null){
            return;
        }

        int index = -1;
        for (int i = 0; i < mDetailEntity.getAData().size(); i++) {
            mTitle.add(mDetailEntity.getAData().get(i).getName());

            if (flagName.equals(mDetailEntity.getAData().get(i).getFlagName())){
                index = i;
                break;
            }
        }
        if (index >= 0){
            mViewPager.setCurrentItem(index);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_back:
                finish();
                break;

            case R.id.ll_follow_game:
                if(mDetailEntity == null){
                    return;
                }

                if(StringUtil.isNull(getMember_id())){
                    DialogManager.showLogInDialog(this);
                    return;
                }

                if (mDetailEntity.getOData().getTick() == 1) {
                    DialogManager.showConfirmDialog(this,"取消关注该游戏圈?",new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()){
                                case R.id.tv_confirm_dialog_yes:
                                    mDetailEntity.getOData().setAttention_num(Integer.valueOf(mDetailEntity.getOData().getAttention_num()) - 1 + "");
                                    mDetailEntity.getOData().setTick(0);
                                    // 关注圈子201
                                    DataManager.groupAttentionGroup(mDetailEntity.getOData().getGroup_id(), getMember_id());

                                    if (mIsSingleEvent){
                                        UmengAnalyticsHelper.onEvent(GroupDetailHybridActivity.this, UmengAnalyticsHelper.GAME, mDetailEntity.getOData().getGroup_name()+"-"+"游戏圈关注");
                                    }else {
                                        UmengAnalyticsHelper.onEvent(GroupDetailHybridActivity.this, UmengAnalyticsHelper.GAME, "游戏圈关注");
                                    }
                                    refreshHeader();
                                    break;
                            }
                        }
                    });
                } else {
                    mDetailEntity.getOData().setAttention_num(Integer.valueOf(mDetailEntity.getOData().getAttention_num()) + 1 + "");
                    mDetailEntity.getOData().setTick(1);
                    // 关注圈子201
                    DataManager.groupAttentionGroup(mDetailEntity.getOData().getGroup_id(), getMember_id());
                    if (mIsSingleEvent){
                        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, mDetailEntity.getOData().getGroup_name()+"-"+"游戏圈关注");
                    }else {
                        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈关注");
                    }

                    refreshHeader();
                }
                break;

            case R.id.tv_open_game:
                install();
                break;

            case R.id.iv_record_screen:
                Game game = new Game();

                if (mDetailEntity != null){
                    game.setGroup_id(mDetailEntity.getOData().getGroup_id());
                    game.setGame_id(mDetailEntity.getOData().getGame_id());
                    game.setGroup_name(mDetailEntity.getOData().getGroup_name());
                }

                DialogManager.showGameDetailDialog(this,game);
                break;
        }
    }

    private void refreshHeader(){
        mGroupName.setText(mDetailEntity.getOData().getGroup_name());
        mVideoNumber.setText("视频："+StringUtil.toUnitW(mDetailEntity.getOData().getVideo_num()+""));
        mPlayerNumber.setText("玩家："+StringUtil.formatNum(mDetailEntity.getOData().getAttention_num()+""));
        GlideHelper.displayImage(this,mDetailEntity.getOData().getFlag(),mGroupIcon);
        //
        if (mDetailEntity.getOData().getTick() == 0){
            mFollowLayout.setBackgroundResource(R.drawable.hybrid_pager_follow);
            mFollowText.setText("关注");
            mFollowIcon.setVisibility(View.VISIBLE);
            mFollowIcon.setImageResource(R.drawable.simple_group);
        }else {
            mFollowLayout.setBackgroundColor(resources.getColor(R.color.groupdetail_player_gray));
            mFollowText.setText("已关注");
            mFollowIcon.setVisibility(View.GONE);
        }

    }

    /**
     * 跳转下载管理
     */
    private void install() {
        if ( mDetailEntity != null && mDetailEntity.getOData().getA_download() != null) {

            ActivityManager.startDownloadManagerActivity(GroupDetailHybridActivity.this,mDetailEntity.getOData().getGame_id(),"1",mDetailEntity.getOData().getGame_id());
            // 游戏下载数+1
            DataManager.downloadClick217(mDetailEntity.getOData().getGame_id(), getMember_id(),
                    Constant.DOWNLOAD_LOCATION_GROUP, mDetailEntity.getOData().getGroup_id());
        }
    }


    public void onEventMainThread(final GroupHybridDetailEntity entity){

        if (entity.isResult()){
            mIsSingleEvent = UmengAnalyticsHelper.isSingleEvent(entity.getOData().getGroup_name());
            mDetailEntity = entity;

            findViewById(R.id.collapse_toolbar).setVisibility(View.VISIBLE);

            findViewById(R.id.vp_detail_pager).setVisibility(View.VISIBLE);

            UITask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int margin = ScreenUtil.dp2px(20);
                    TabLayoutHelper.setIndicator(mTabLayout,margin,margin);
                }
            },100);

            refreshHeader();
            UITask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GlideHelper.displayImage(GroupDetailHybridActivity.this,entity.getOData().getFlag(),mGroupIcon);
                }
            },1000);

            initFragment();
        }
    }

    public void onEventMainThread(GroupAttentionGroupEntity entity){
        if (entity.isResult()){

        }
    }

    //刷新评价页
    public void onEventMainThread(RefreshCommentPagerEvent event){
        for (int i = 0; i < mDetailEntity.getAData().size(); i++) {
            if ("appraise".equals(mDetailEntity.getAData().get(i).getFlagName())){
                ((GroupDetailHybridFragment)mFragments.get(i)).refresh();
                break;
            }
        }
    }

    public void onEventMainThread(LoginEvent event){
        loadData();
    }

    public void onEventMainThread(ReleaseScrollEvent event){
        mViewPager.setScrollable(false);
    }
}
