package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmsysj.screeclibinvoke.ui.activity.ScreenRecordActivity;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.ImageLoaderHelper;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.response.GroupAttentionGroupEntity;
import com.li.videoapplication.data.model.response.GroupInfoEntity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.FglassHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.fragment.GroupdetailHotVideoFragment;
import com.li.videoapplication.ui.fragment.GroupdetailIntroduceFragment;
import com.li.videoapplication.ui.fragment.GroupdetailNewVideoFragment;
import com.li.videoapplication.ui.fragment.GroupdetailPlayerFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;
import com.li.videoapplication.views.RoundedImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：圈子详情
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("InflateParams")
public class GroupDetailActivity extends TBaseAppCompatActivity implements
        OnClickListener {

    public static final String TAG = GroupDetailActivity.class.getSimpleName();

    private GroupdetailIntroduceFragment introduceFragment;
    private GroupdetailNewVideoFragment newVideoFragment;
    private GroupdetailHotVideoFragment hotVideoFragment;
    private GroupdetailPlayerFragment playerFragment;

    /**
     * 跳转：安装应用
     */
    private void install() {
        if (game != null && game.getA_download() != null) {
            if (URLUtil.isURL(game.getA_download())) {
                Uri uri = Uri.parse(game.getA_download());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                // 游戏下载数+1
                DataManager.TASK.downloadClick203(game.getGame_id());
            }
        }
    }

    /**
     * 跳转：录屏
     */
    public void startScreenRecordActivity() {
        ScreenRecordActivity.startScreenRecordActivity(this);
        overridePendingTransition(R.anim.activity_slide_in_top, R.anim.activity_hold);
    }

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragments;

    private ImageView bg;
    private RoundedImageView head;
    private TextView name;
    private TextView mark;
    private TextView focus;
    private View focusView;
    public AppBarLayout appBarLayout;

    public Game game;

    public String group_id;
    private ImageView tb_gift;

    @Override
    public void refreshIntent() {
        super.refreshIntent();

        try {
            group_id = getIntent().getStringExtra("group_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StringUtil.isNull(group_id) || group_id.equals("0")) {
            finish();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_groupdetail;
    }

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
            setSystemBarBackgroundColor(Color.TRANSPARENT);
        } else {
            setSystemBarBackgroundColor(Color.BLACK);
        }
    }

    @Override
    public void initView() {
        super.initView();

        initToolbar();
        initHeaderView();

//        setHeaderData(game);
        initViewPager();
    }

    private void initViewPager() {
        if (fragments == null) {
            fragments = new ArrayList<>();

            introduceFragment = new GroupdetailIntroduceFragment();
            newVideoFragment = new GroupdetailNewVideoFragment();
            hotVideoFragment = new GroupdetailHotVideoFragment();
            playerFragment = new GroupdetailPlayerFragment();

            fragments.add(introduceFragment);
            fragments.add(newVideoFragment);
            fragments.add(hotVideoFragment);
            fragments.add(playerFragment);
        }

        final String[] tabTitle = {"游戏介绍", "最新视频", "精彩视频", "玩家"};
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);

        viewPager.setAdapter(adapter);

        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initToolbar() {
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.tb_back).setOnClickListener(this);
        findViewById(R.id.tb_download).setOnClickListener(this);
        findViewById(R.id.tb_plus).setOnClickListener(this);
        tb_gift = (ImageView) findViewById(R.id.tb_gift);
        tb_gift.setOnClickListener(this);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
        tb_title.setText("游戏圈");

        if (AppConstant.DOWNLOAD) {
            findViewById(R.id.tb_download).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.tb_download).setVisibility(View.GONE);
        }
    }

    @Override
    public void loadData() {
        super.loadData();

        // 圈子详情
        DataManager.groupInfo(group_id, getMember_id());

        postDelayed(new Runnable() {

            @Override
            public void run() {

                newVideoFragment.onRefresh();
            }
        }, AppConstant.TIME.GAMEDETAIL_SECOND);

        postDelayed(new Runnable() {

            @Override
            public void run() {

                hotVideoFragment.onRefresh();
            }
        }, AppConstant.TIME.GAMEDETAIL_THIRD);

        postDelayed(new Runnable() {

            @Override
            public void run() {

                playerFragment.onRefresh();
            }
        }, AppConstant.TIME.GAMEDETAIL_FOURTH);
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈");
    }

    private void initHeaderView() {
        head = (RoundedImageView) findViewById(R.id.groupdetail_head);
        name = (TextView) findViewById(R.id.groupdetail_name);
        mark = (TextView) findViewById(R.id.groupdetail_mark);
        focusView = findViewById(R.id.groupdetail_focusview);
        focus = (TextView) findViewById(R.id.groupdetail_focus);
        bg = (ImageView) findViewById(R.id.groupdetail_bg);

        findViewById(R.id.groupdetail_chatview).setOnClickListener(this);
        findViewById(R.id.groupdetail_matchview).setOnClickListener(this);
        focusView.setOnClickListener(this);
    }

    public void refreshHeaderView(final Game item) {
        if (item != null) {
            setMark(mark, item);
            setFocus(item);
        }
    }

    private void setHeaderData(final Game item) {
        if (item != null) {
            ImageLoaderHelper.displayImageWhiteListener(item.getFlag(), head, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    try {
                        FglassHelper.blur(head, bg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });

            setTextViewText(name, item.getGroup_name());
            setMark(mark, item);
            setFocus(item);
            if (item.getIs_gift() == 1) {//有礼包
                tb_gift.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setFocus(final Game game) {
        if (game != null) {
            if (game.getTick() == 1) {
                focusView.setBackgroundColor(resources.getColor(R.color.groupdetail_player_gray));
                setTextViewText(focus, R.string.dynamic_focused);
            } else {
                focusView.setBackgroundResource(R.drawable.gamematch_cs_bg);
                setTextViewText(focus, R.string.dynamic_focus);
            }
        }
    }

    private void setMark(TextView view, final Game item) {

        StringBuilder sb = new StringBuilder();
        String attention_num = item.getAttention_num();
        String video_num = item.getVideo_num();
        if (!StringUtil.isNull(video_num)) {
            sb.append("话题\t").append(video_num);
        }
        if (!StringUtil.isNull(attention_num)) {
            sb.append("\t\t玩家\t").append(attention_num);
        }
        view.setText(sb.toString());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tb_back:
                finish();
                break;

            case R.id.tb_gift:
                ActivityManeger.startGroupGiftActivity(this, game);
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈-礼包");
                break;

            case R.id.tb_download:
                install();
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈-下载游戏");
                break;

            case R.id.tb_plus:
                DialogManager.showGameDetailDialog(this, game);
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈-发表");
                break;

            case R.id.groupdetail_focusview:
                if (!isLogin()) {
                    showToastLogin();
                    return;
                }
                if (game.getTick() == 1) {
                    game.setAttention_num(Integer.valueOf(game.getAttention_num()) - 1 + "");
                    game.setTick(0);
                } else {
                    game.setAttention_num(Integer.valueOf(game.getAttention_num()) + 1 + "");
                    game.setTick(1);
                }
                // 关注圈子201
                DataManager.groupAttentionGroup(game.getGroup_id(), getMember_id());
                refreshHeaderView(game);
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈关注");
                break;

            case R.id.groupdetail_chatview:
                // TODO: 2016/10/24 chat
                break;

            case R.id.groupdetail_matchview:
                // TODO: 2016/10/24 chat
                break;
        }
    }

    /**
     * 回调：圈子详情
     */
    public void onEventMainThread(GroupInfoEntity event) {

        if (event != null) {
            if (event.isResult()) {
                game = event.getData();
                setHeaderData(game);
                introduceFragment.loadData();
                viewPager.setCurrentItem(1);
            }
        }
    }

    /**
     * 回调：关注圈子201
     */
    public void onEventMainThread(GroupAttentionGroupEntity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            }
        }
    }

    /**
     * 回调：视频收藏
     */
    public void onEventMainThread(VideoCollect2Entity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }
    }

    /**
     * 回调：视频点赞
     */
    public void onEventMainThread(VideoFlower2Entity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }
    }

    /**
     * 回调：玩家关注
     */
    public void onEventMainThread(MemberAttention201Entity event) {

        if (event != null) {
            if (event.isResult()) {
                showToastShort(event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }
    }
}
