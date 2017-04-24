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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fmsysj.screeclibinvoke.ui.activity.ScreenRecordActivity;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.message.OnGroupItemOnClickListener;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.response.EventsList214Entity;
import com.li.videoapplication.data.model.response.GroupAttentionGroupEntity;
import com.li.videoapplication.data.model.response.GroupInfoEntity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.BaseHttpResult;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.mvp.match.MatchContract.IGroupDetailView;
import com.li.videoapplication.mvp.match.MatchContract.IMatchPresenter;
import com.li.videoapplication.mvp.match.presenter.MatchPresenter;
import com.li.videoapplication.tools.FeiMoIMHelper;
import com.li.videoapplication.tools.FglassHelper;
import com.li.videoapplication.tools.RongIMHelper;
import com.li.videoapplication.tools.StatusBarBlackTextHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.dialog.ConfirmDialog;
import com.li.videoapplication.ui.fragment.GroupdetailIntroduceFragment;
import com.li.videoapplication.ui.fragment.GroupdetailPlayerFragment;
import com.li.videoapplication.ui.fragment.GroupdetailVideoFragment;
import com.li.videoapplication.ui.pageradapter.ViewPagerAdapter;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;
import com.li.videoapplication.views.RoundedImageView;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：圈子详情
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("InflateParams")
public class GroupDetailActivity extends TBaseAppCompatActivity implements
        OnClickListener, IGroupDetailView {

    public static final String TAG = GroupDetailActivity.class.getSimpleName();

    private GroupdetailIntroduceFragment introduceFragment;
    private GroupdetailVideoFragment newVideoFragment;
    private GroupdetailVideoFragment hotVideoFragment;
    private GroupdetailPlayerFragment playerFragment;
    private IMatchPresenter presenter;

    public boolean isSingleEvent = false;//是否需要将埋点独立

    private int attent ; //关注状态
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
                DataManager.downloadClick217(game.getGame_id(), getMember_id(),
                        Constant.DOWNLOAD_LOCATION_GROUP, group_id);
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
    private View focusView, matchView;
    public AppBarLayout appBarLayout;

    public Game game;

    public String group_id;
    private ImageView tb_gift;


    @Override
    public void refreshIntent() {
        super.refreshIntent();

        try {
            group_id = getIntent().getStringExtra("group_id");
            Log.e("group_id",group_id);
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
            newVideoFragment = GroupdetailVideoFragment.newInstance(GroupdetailVideoFragment.GROUPDETAILVIDEO_NEW);
            hotVideoFragment = GroupdetailVideoFragment.newInstance(GroupdetailVideoFragment.GROUPDETAILVIDEO_HOT);
            playerFragment = new GroupdetailPlayerFragment();
            fragments.add(introduceFragment);
            fragments.add(newVideoFragment);
            fragments.add(hotVideoFragment);
            fragments.add(playerFragment);
        }

        final String[] tabTitle = {"游戏介绍", "最新视频", "最热视频", "玩家"};
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, tabTitle);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);

        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initToolbar() {
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView tb_download = (ImageView) findViewById(R.id.tb_download);
        ImageView tb_plus = (ImageView) findViewById(R.id.tb_plus);
        tb_download.setVisibility(View.VISIBLE);
        tb_plus.setVisibility(View.VISIBLE);

        findViewById(R.id.tb_back).setOnClickListener(this);
        tb_download.setOnClickListener(this);
        tb_plus.setOnClickListener(this);

        tb_gift = (ImageView) findViewById(R.id.tb_gift);
        tb_gift.setOnClickListener(this);
        TextView tb_title = (TextView) findViewById(R.id.tb_title);
//        tb_title.setText("游戏圈");
        tb_title.setVisibility(View.INVISIBLE);

        if (AppConstant.SHOW_DOWNLOAD_AD) {
            findViewById(R.id.tb_download).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.tb_download).setVisibility(View.GONE);
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        presenter = MatchPresenter.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 圈子详情
        DataManager.groupInfo(group_id, getMember_id());
        presenter.setGroupDetailView(this);
    }

    private void initHeaderView() {
        head = (RoundedImageView) findViewById(R.id.groupdetail_head);
        name = (TextView) findViewById(R.id.groupdetail_name);
        mark = (TextView) findViewById(R.id.groupdetail_mark);
        focusView = findViewById(R.id.groupdetail_focusview);
        focus = (TextView) findViewById(R.id.groupdetail_focus);
        bg = (ImageView) findViewById(R.id.groupdetail_bg);

        matchView = findViewById(R.id.groupdetail_matchview);

        findViewById(R.id.groupdetail_chatview).setOnClickListener(this);
        matchView.setOnClickListener(this);
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
            GlideHelper.displayImageWhiteTargets(this, item.getFlag(), new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    head.setImageBitmap(resource);
                    try {
                        FglassHelper.blur(head, bg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        String attention_num = StringUtil.toUnitW(item.getAttention_num());
        String video_num = StringUtil.toUnitW(item.getVideo_num());
        if (!StringUtil.isNull(video_num)) {
            sb.append("话题\t").append(video_num);
        }
        if (!StringUtil.isNull(attention_num)) {
            sb.append("\t\t玩家\t").append(attention_num);
        }
        view.setText(sb.toString());
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;

            case R.id.tb_gift:
                ActivityManeger.startGroupGiftActivity(this, game);
                if (isSingleEvent){
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, game.getGroup_name()+"-"+ "游戏圈-礼包");
                }else {
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈-礼包");
                }

                break;

            case R.id.tb_download:
                install();
                if (isSingleEvent){
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, game.getGroup_name()+"-"+"游戏圈-下载游戏");
                }else {
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈-下载游戏");
                }

                break;

            case R.id.tb_plus:
                DialogManager.showGameDetailDialog(this, game);
                if (isSingleEvent){
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, game.getGroup_name()+"-"+"游戏圈-发表");
                }else {
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈-发表");
                }

                break;

            case R.id.groupdetail_focusview://关注
                if (!isLogin()) {
                    DialogManager.showLogInDialog(this);
                    return;
                }

                if (game.getTick() == 1) {
                    DialogManager.showConfirmDialog(this,"取消关注该游戏圈?",new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()){
                                case R.id.tv_confirm_dialog_yes:
                                    game.setAttention_num(Integer.valueOf(game.getAttention_num()) - 1 + "");
                                    game.setTick(0);
                                    // 关注圈子201
                                    DataManager.groupAttentionGroup(game.getGroup_id(), getMember_id());
                                    refreshHeaderView(game);
                                    if (isSingleEvent){
                                        UmengAnalyticsHelper.onEvent(GroupDetailActivity.this, UmengAnalyticsHelper.GAME, game.getGroup_name()+"-"+"游戏圈关注");
                                    }else {
                                        UmengAnalyticsHelper.onEvent(GroupDetailActivity.this, UmengAnalyticsHelper.GAME, "游戏圈关注");
                                    }
                                    //刷新游戏圈界面

                                    break;
                            }
                        }
                    });
                } else {
                    game.setAttention_num(Integer.valueOf(game.getAttention_num()) + 1 + "");
                    game.setTick(1);
                    // 关注圈子201
                    DataManager.groupAttentionGroup(game.getGroup_id(), getMember_id());
                    refreshHeaderView(game);
                    if (isSingleEvent){
                        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, game.getGroup_name()+"-"+"游戏圈关注");
                    }else {
                        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈关注");
                    }

                }


                break;

            case R.id.groupdetail_chatview://群聊
                if (!isLogin()) {
                    DialogManager.showLogInDialog(this);
                    return;
                }

                if (game.isPrivateIM()) {
                    if (Proxy.getMessageManager().getOnGroupItemOnClickListener() == null) {
                        IMSdk.setOnGroupItemOnClick(new OnGroupItemOnClickListener() {
                            @Override
                            public void onGroupItemOnClick(String memberid, String name) {
                                ActivityManeger.startConversationActivity(GroupDetailActivity.this,
                                        memberid, name, ConversationActivity.PRIVATE);
                            }
                        });
                    }
                    if (!Proxy.getConnectManager().isConnect()) {
                        Member user = getUser();
                        String memberId = user.getMember_id();
                        if(null == memberId  || memberId.equals("")){
                            memberId = getMember_id();
                        }
                        FeiMoIMHelper.Login(memberId, user.getNickname(), user.getAvatar());
                    }
                    FeiMoIMHelper.createMuccRoom(this, game.getGame_id(), game.getGroup_name(), game.getFlag());
                }else {
                    presenter.groupJoin(getMember_id(), game.getChatroom_group_id());
                }
                break;

            case R.id.groupdetail_matchview://赛事
                ActivityManeger.startGroupMatchListActivity(this, game.getGame_id());
                if (isSingleEvent){
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, game.getGroup_name()+"-"+"游戏圈-赛事-游戏圈点击赛事按钮");
                }else {
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈-赛事-游戏圈点击赛事按钮");
                }

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
                attent = game.getTick();
                setHeaderData(game);
                introduceFragment.loadData();

                presenter.getGroupEventsList(1, game.getGame_id());
                if (game != null){
                    isSingleEvent = UmengAnalyticsHelper.isSingleEvent(game.getGroup_name());
                }

                if (isSingleEvent){
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, game.getGroup_name()+"-"+"游戏圈");
                }else {
                    UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.GAME, "游戏圈");
                }

            }
        }
    }

    /**
     * 回调：关注圈子201
     */
    public void onEventMainThread(GroupAttentionGroupEntity event) {

        if (event != null && event.isResult()) {
            Log.d(tag, event.getMsg());
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
                Log.d(tag, event.getMsg());
            } else {
                showToastShort(event.getMsg());
            }
        }
    }

    /**
     * 回调：圈子赛事列表
     */
    @Override
    public void refreshGroupMatchListData(EventsList214Entity data) {
        Log.d(TAG, "圈子赛事列表: " + data);
        if (data.getList().size() > 0) {
            matchView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 回调：加入群聊
     */
    @Override
    public void refreshGroupJoin(BaseHttpResult data) {
        Log.d(TAG, "圈子加入群聊: " + data);
        if (data.isResult() && RongIM.getInstance() != null &&
                RongIM.getInstance().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {

            RongIMHelper.setCoversationNotifMute(Conversation.ConversationType.GROUP,
                    game.getChatroom_group_id(), true);

            ActivityManeger.startConversationActivity(this,
                    game.getChatroom_group_id(),
                    game.getGroup_name(),
                    ConversationActivity.GROUP);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.setGroupDetailView(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.setGroupDetailView(null);

        if (game != null && attent != game.getTick()){
            //关注发生改变事件
            EventBus.getDefault().post(new GroupAttentionGroupEntity());
        }
    }
}
