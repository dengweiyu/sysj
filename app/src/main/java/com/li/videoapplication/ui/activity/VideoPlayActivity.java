package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.happly.link.HpplayLinkControl;
import com.happly.link.bean.WebPushInfo;
import com.happly.link.device.Const;
import com.happly.link.net.RefreshUIInterface;
import com.ifeimo.im.common.util.StatusBarBlackTextHelper;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Bullet;
import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.GoodAndStartEvent;
import com.li.videoapplication.data.model.event.SharedSuccessEvent;
import com.li.videoapplication.data.model.response.BulletDo203Bullet2VideoEntity;
import com.li.videoapplication.data.model.response.BulletList203Entity;
import com.li.videoapplication.data.model.response.ChangeVideo208Entity;
import com.li.videoapplication.data.model.response.FndownClick203Entity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.PlayGiftResultEntity;
import com.li.videoapplication.data.model.response.SrtList203Entity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoCommentLike2Entity;
import com.li.videoapplication.data.model.response.VideoDetail226Entity;
import com.li.videoapplication.data.model.response.VideoDoComment2CommentEntity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.data.preferences.Constants;
import com.li.videoapplication.data.preferences.NormalPreferences;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.TBaseAppCompatActivity;
import com.li.videoapplication.tools.ArrayHelper;
import com.li.videoapplication.tools.RandomUtil;
import com.li.videoapplication.tools.ShareSDKShareHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManager;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.fragment.AuthorVideoListFragment;
import com.li.videoapplication.ui.fragment.GiftTimeLineFragment;
import com.li.videoapplication.ui.fragment.VideoPlayCommentFragment;
import com.li.videoapplication.ui.fragment.VideoPlayGiftFragment;
import com.li.videoapplication.ui.fragment.VideoPlayIntroduceFragment;
import com.li.videoapplication.ui.fragment.VideoPlayVideoFragment;
import com.li.videoapplication.ui.pageradapter.GamePagerAdapter;
import com.li.videoapplication.ui.view.AddDanmukuView;
import com.li.videoapplication.ui.view.CommentView;
import com.li.videoapplication.ui.view.VideoPlayView;
import com.li.videoapplication.utils.HareWareUtil;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.TextUtil;
import com.li.videoapplication.utils.URLUtil;
import com.li.videoapplication.views.ViewPagerY4;
import com.li.videoapplication.views.sparkbutton.SparkButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.eventbus.EventBus;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：视频详情
 */
@SuppressLint("SetJavaScriptEnabled")
public class VideoPlayActivity extends TBaseAppCompatActivity implements
        OnPageChangeListener,
        CommentView.CommentListener,
        RefreshUIInterface ,
        View.OnClickListener{

    public static long playPos;
    public static String playUrl;
    private boolean afterCreate = false;

    //播放完毕时是横屏，点击推荐进入此页面，记录上次播放的横竖屏状态
    public boolean isLandscape;
    private AuthorVideoListFragment authorVideoListFragment;

    /**
     * 分享
     */
    public void startShareActivity() {

        if (item != null) {
            final String url = AppConstant.getMUrl(item.getQn_key());
            final String title = "精彩视频分享";
            final String imageUrl = item.getFlag();
            final String content = "快来看看" + item.getTitle();
            if (isLandscape()) {
                DialogManager.showShareDialog(this, url, imageUrl, title, content);
            } else {
                ActivityManager.startActivityShareActivity4VideoPlay(this, url, title, imageUrl, content);
            }
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.VIDEOPLAY, "视频分享");
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MACROSCOPIC_DATA, "视频总分享次数");
        }
    }

    private List<RelativeLayout> topButtons;
    private List<ImageView> topLines;
    private List<TextView> topTexts;
    @SuppressWarnings("unused")
    private int currIndex = 0;
    public VideoPlayCommentFragment comment;
    private VideoPlayVideoFragment video;
    private VideoPlayIntroduceFragment introduce;
    private List<Fragment> fragments;
    private ViewPagerY4 viewPager;
    private GamePagerAdapter adapter;
    private int[] location = new int[2];
    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 1000;
    public VideoImage item;
    private boolean ifFirst = true;
    private long mEntryTime;                //用于统计播放页停留时长

    public void setItem(VideoImage item) {
        if (!StringUtil.isNull(item.getVideo_id())) {
            String video_id = item.getVideo_id();
            item.setId(video_id);
        }
        this.item = item;
    }

    @Override
    public void refreshIntent() {
        try {
            item = (VideoImage) getIntent().getSerializableExtra("item");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (item == null) {
            finish();
        }
        if (!StringUtil.isNull(item.getMore_mark())){
            if (item.getMore_mark().equals("player_square")){
                UmengAnalyticsHelper.onEvent(this,UmengAnalyticsHelper.MAIN,"首页-玩家广场");
            }
        }
        try {
            isLandscape = getIntent().getBooleanExtra("isLandscape", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_videoplay;
    }

    public int inflateActionBar() {
        return R.layout.actionbar_second;
    }

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
        setSystemBar(false);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//显示状态栏

    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        StatusBarBlackTextHelper.initStatusBarTextColor(this.getWindow(),false);

       /// actionBar.hide();

        // 屏幕状态
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                        PowerManager.ON_AFTER_RELEASE,
                POWER_LOCK);

        // 音量
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

    }

    @Override
    public void initView() {
        super.initView();


        initContentView();
        //引导页
        showPlayTipDialog();
    }

    private boolean mIsInit = true;
    private void runOnResume(){
        if (!mIsInit){
            return;
        }
        mIsInit = false;

        UITask.postDelayed(new Runnable() {
            @Override
            public void run() {
                initTopMenu();
                //点赞 收藏.
                initStatus();
                // 网页视频 3137
                // 视频详情
                DataManager.videoDetail226(item.getId(), getMember_id());

                // 弹幕列表
                DataManager.DANMUKU.bulletList203(item.getId());

                // 字幕列表
                DataManager.SUBTITLE.srtList203(item.getId());

                //礼物列表
                mGiftFragment = VideoPlayGiftFragment.newInstance(item.video_id);

                //礼物时间轴
                resetTimeLineFragment();

                if (videoPlayView != null){
                    videoPlayView.addOnPreparedListener(mGiftTimeLineFragment,500);
                }
            }
        },1000);

    }

    @Override
    public void loadData() {
        super.loadData();


        IntentFilter filter = new IntentFilter();
        filter.addAction(Const.LINK_PLAY_STATE);
        filter.addAction(Const.HPPLAY_LINK_DISCONNECT);
        registerReceiver(myBroadcastReceiver, filter);
/*        //点赞 收藏.
        initStatus();
        // 网页视频 3137
        // 视频详情
        DataManager.videoDetail201(item.getId(), getMember_id());

        // 弹幕列表
        DataManager.DANMUKU.bulletList203(item.getId());

        // 字幕列表
        DataManager.SUBTITLE.srtList203(item.getId());



        //礼物列表
        mGiftFragment = VideoPlayGiftFragment.newInstance(item.video_id);*/
    }

    private SparkButton mGoodBtn;
    private TextView mGoodCount;
    private ImageView mGift;
    private TextView mGiftCount;
    private ImageView mShared;
    private TextView mSharedCount;
    private SparkButton mStart;
    private TextView mStartCount;

    //点赞 收藏 分享
    private void initStatus(){
        mGoodBtn = (SparkButton)findViewById(R.id.sb_video_play_good);
        mGoodCount = (TextView)findViewById(R.id.tv_video_play_good_count);

        mGift = (ImageView)findViewById(R.id.iv_video_play_status_gift);
        mGiftCount = (TextView)findViewById(R.id.tv_video_play_gift_count);
        mShared = (ImageView)findViewById(R.id.iv_video_play_shared);
        mSharedCount = (TextView)findViewById(R.id.tv_video_play_shared_count);
        mStart = (SparkButton) findViewById(R.id.sb_video_play_start);
        mStartCount = (TextView)findViewById(R.id.tv_video_play_start_count);

        findViewById(R.id.ll_video_play_good).setOnClickListener(this);
        findViewById(R.id.ll_video_play_status_gift).setOnClickListener(this);
        findViewById(R.id.ll_video_play_shared).setOnClickListener(this);
        findViewById(R.id.ll_video_play_start).setOnClickListener(this);

        mGoodBtn.setOnClickListener(this);
        mGift.setOnClickListener(this);
        mShared.setOnClickListener(this);
        mStart.setOnClickListener(this);
        mGiftCount.setOnClickListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mFragmentState){
            if (mGiftFragment != null){
                if (afterCreate == true) {
                float y = ev.getRawY();
                    try {
                        mGiftFragment.getRootView().getLocationOnScreen(location);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //touch outside the fragment
                    if (y < location[1]) {
                    mFragmentState = false;
                    hideGiftFragment(true);
                    return true;            //true or false will be intercept event
                }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //乐播连接广播
    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Const.LINK_PLAY_STATE.equals(intent.getAction())) {
                Log.d(tag, "~~~~~~~~~~~~~~~play_state == " + intent.getBooleanExtra("play_state", false));
                boolean play_state = intent.getBooleanExtra("play_state", false);
                if (play_state) {//已成功连接
                    videoPlayView.switchPlay(VideoPlayView.STATE_TV);

                    try {
                        int position = (int) videoPlayView.videoPlayer.getCurrentPosition();
                        Log.d(tag, "position : " + position);
                        int time = position / 1000;
                        Log.d(tag, "time position : " + time);
                        //设置投屏播放进度
                        if (!StringUtil.isNull(qn_key) && URLUtil.isURL(qn_url)) {
                            videoPlayView.linkControl.setPlayVideoPosition(VideoPlayActivity.this, 2, qn_url, time);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (Const.HPPLAY_LINK_DISCONNECT.equals(intent.getAction())) {
                ToastHelper.s("投屏连接已断开");
                Log.d(tag, "投屏连接已断开: ");
            }
        }
    };

    public CommentView commentView;
    public AddDanmukuView addDanmukuView;
    public VideoPlayView videoPlayView;

    private View mPlayGift;
    private View mLandPlayGift;
    private void initContentView() {
        //礼物按钮
        mPlayGift = findViewById(R.id.iv_video_play_gift);
        mPlayGift.setOnClickListener(this);


        videoPlayView = (VideoPlayView) findViewById(R.id.videoplay);
        videoPlayView.init(this);
        videoPlayView.minView();
        mLandPlayGift = findViewById(R.id.iv_play_gift_right);
        mLandPlayGift.setOnClickListener(this);


        if (NetUtil.isWIFI()) {
            videoPlayView.switchPlay(VideoPlayView.STATE_VIDEOPLAY);
            if (videoPlayView.danmukuPlayer != null)
                videoPlayView.danmukuPlayer.resumeDanmaku();
        } else {
            videoPlayView.switchPlay(VideoPlayView.STATE_PREPARE);
        }

        commentView = (CommentView) findViewById(R.id.comment);
        if (isLogin()) {
            commentView.setFocusable(true);
        }
        commentView.init(this);
        commentView.setCommentListener(this);
        commentView.showView();

        addDanmukuView = (AddDanmukuView) findViewById(R.id.adddanmuku);
        addDanmukuView.init(this);
        addDanmukuView.setAddDanmukuListener(videoPlayView);


    }



    private boolean mFragmentState;
    private VideoPlayGiftFragment mGiftFragment;
    private GiftTimeLineFragment mGiftTimeLineFragment;

    public void setGiftFragmentState(boolean isScroll){
        mFragmentState = !mFragmentState;
        if (mFragmentState) {
            showGiftFragment();
        }else {
            hideGiftFragment(isScroll);
        }
    }

    public long getProgress(){
        if (videoPlayView!= null){
            return videoPlayView.getCurrentPosition();
        }
        return  0;
    }

    public void resetTimeLineData(){
        if (mGiftTimeLineFragment != null){
            mGiftTimeLineFragment.resetData();
        }
    }

    /**
    *show gift time line fragment
    */
    private void resetTimeLineFragment(){
        mGiftTimeLineFragment = GiftTimeLineFragment.newInstance(item.video_id);
        if (!this.isDestroyed()){
            if (isLandscape()){
                findViewById(R.id.rv_play_gift_list_h).setVisibility(View.VISIBLE);
                findViewById(R.id.rv_play_gift_list_v).setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.rv_play_gift_list_h,mGiftTimeLineFragment).commitAllowingStateLoss();
            } else {
                findViewById(R.id.rv_play_gift_list_h).setVisibility(View.GONE);
                findViewById(R.id.rv_play_gift_list_v).setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.rv_play_gift_list_v,mGiftTimeLineFragment).commitAllowingStateLoss();
            }
        }
    }

    public void showOrHideTimeLine(boolean isShow){
        int id ;
        if (isLandscape()){
            id = R.id.rv_play_gift_list_h;

        }else {
            id = R.id.rv_play_gift_list_v;

        }

        if (isShow){
            findViewById(id).setVisibility(View.VISIBLE);
        }else {
            findViewById(id).setVisibility(View.GONE);
        }
    }

    /**
     *  update state
     */
    public void refreshState(boolean state){
        mFragmentState = state;
    }

    /**
     *
     */
    public void hideGiftFragment(){
        if (mGiftFragment == null){
            return;
        }
        if (!this.isDestroyed()){
            getSupportFragmentManager().beginTransaction().remove(mGiftFragment).commitAllowingStateLoss();
        }
    }

    /**
     *
     */
    private void showGiftFragment(){
        int id ;
        if (isLandscape()){
            id = R.id.fg_play_gift_h;
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.VIDEOPLAY, "视频播放页-横屏-礼物页面按钮");
        }else {
            id = R.id.fg_play_gift_v;
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.VIDEOPLAY, "视频播放页-竖屏-礼物页面按钮");
        }

        mGiftFragment = (VideoPlayGiftFragment) getSupportFragmentManager().findFragmentById(id);
        if (mGiftFragment == null){
            mGiftFragment = VideoPlayGiftFragment.newInstance(item.video_id);
        }
        getSupportFragmentManager().beginTransaction().replace(id,mGiftFragment).commitAllowingStateLoss();
        ifFirst = false;
        mGiftFragment.showContent();

    }




    /**
     *  hide gift fragment
     * @param isScroll
     *
     */
    private void hideGiftFragment(boolean isScroll) {
       // VideoPlayGiftFragment fragment = (VideoPlayGiftFragment) getSupportFragmentManager().findFragmentById(R.id.fg_play_gift);
        VideoPlayGiftFragment fragment = mGiftFragment;
        if (fragment != null ) {
            if (!isScroll){
                hideGiftFragment();
            }else {
                fragment.hideContent();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            afterCreate = true;
            Log.d(tag, "all view is ready");
        }
    }

    /**
     * 遮罩提示页：打赏
     */
    private void showPlayTipDialog() {
        boolean tip = NormalPreferences.getInstance().getBoolean(Constants.TIP_PLAY_GIFT, true);
        if (tip) {
            DialogManager.showPlayGiftTipDialog(this);
            NormalPreferences.getInstance().putBoolean(Constants.TIP_PLAY_GIFT, false);
        }
    }

            // 是否是弹幕
//    public boolean bullet;


    @Override
    public boolean comment(boolean isSecondComment, String text) {
        if (isLogin()) {
            Log.d(tag, "comment/text=" + text);
            if (videoPlayView != null && videoPlayView.isVoideoPlaying()) {
                videoPlayView.addDanmuku(text);
            }else {
                if (videoPlayView != null && videoPlayView.videoImage != null){
                    DataManager.DANMUKU.bulletDo203Bullet2Video(videoPlayView.videoImage.getVideo_id(),"1000",getMember_id(),text);
                }
            }
        } else {
            DialogManager.showLogInDialog(this);
            return false;
        }
        return true;
    }

    @Override
    public boolean comment2(boolean isSecondComment, String text, Comment c) {
        if (isLogin()) {
            Log.d(tag, "comment/text=" + text);
            if (videoPlayView != null && videoPlayView.videoImage != null){
//                DataManager.DANMUKU.bulletDo203Bullet2Video(videoPlayView.videoImage.getVideo_id(),"1000",getMember_id(),text);
                DataManager.DANMUKU.bulletDo203SecondComment(videoPlayView.videoImage.getVideo_id(),"1000",getMember_id(),text, c.getComment_id(), c.getMember_id());
            }
            return true;
        }
        DialogManager.showLogInDialog(this);
        return false;
    }

    private void initTopMenu() {

        if (topButtons == null) {
            topButtons = new ArrayList<>();
            topButtons.add((RelativeLayout) findViewById(R.id.top_first));
            topButtons.add((RelativeLayout) findViewById(R.id.top_second));
            topButtons.add((RelativeLayout) findViewById(R.id.top_third));
        }

        if (topLines == null) {
            topLines = new ArrayList<>();
            topLines.add((ImageView) findViewById(R.id.top_first_line));
            topLines.add((ImageView) findViewById(R.id.top_second_line));
            topLines.add((ImageView) findViewById(R.id.top_third_line));
        }

        if (topTexts == null) {
            topTexts = new ArrayList<>();
            topTexts.add((TextView) findViewById(R.id.top_first_text));
            topTexts.add((TextView) findViewById(R.id.top_second_text));
            topTexts.add((TextView) findViewById(R.id.top_third_text));
        }

        if (fragments == null) {
            fragments = new ArrayList<>();
            comment = new VideoPlayCommentFragment();
            fragments.add(comment);
            video = new VideoPlayVideoFragment();
            fragments.add(video);
            introduce = new VideoPlayIntroduceFragment();
            fragments.add(introduce);
            setFragmentData();
        }

        viewPager = (ViewPagerY4) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setScrollable(true);
        new HorizontalOverScrollBounceEffectDecorator(new ViewPagerOverScrollDecorAdapter(viewPager));

        adapter = new GamePagerAdapter(manager, fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        for (int i = 0; i < topButtons.size(); i++) {
            OnTabListener onTabListener = new OnTabListener(i);
            topButtons.get(i).setOnClickListener(onTabListener);
        }

        switchTab(0);
        viewPager.setCurrentItem(0);


    }

    private void setFragmentData() {
        if (item != null) {
            comment.setVideoImage(item);
            video.setItem(item);
            introduce.setItem(item);
        }
    }

    public void startAuthorVideoListFragment(String member_id, String game_id, String title) {
        authorVideoListFragment = new AuthorVideoListFragment();

        Bundle bundle = new Bundle();
        bundle.putString("member_id", member_id);
        bundle.putString("game_id", game_id);
        bundle.putString("title", title);
        authorVideoListFragment.setArguments(bundle);

        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.activity_slide_in_right, R.anim.activity_disappear);
        ft.add(R.id.authorVideoList, authorVideoListFragment).commit();
    }

    public void removeAuthorVideoListFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.remove(authorVideoListFragment).commit();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        switchTab(arg0);
    }

    private class OnTabListener implements OnClickListener {

        private int index;

        public OnTabListener(int i) {
            this.index = i;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
            switchTab(index);
            currIndex = index;
        }
    }

    private void switchTab(int index) {
        for (int i = 0; i < topTexts.size(); i++) {
            if (index == i) {
                topTexts.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_red));
            } else {
                topTexts.get(i).setTextColor(resources.getColorStateList(R.color.menu_game_gray));
            }
        }
        for (int i = 0; i < topLines.size(); i++) {
            if (index == i) {
                topLines.get(i).setImageResource(R.color.menu_game_red);
            } else {
                topLines.get(i).setImageResource(R.color.menu_game_transperent);
            }
        }
    }


    public void refreshTab(VideoImage item) {

        if (item != null) {
            if (!StringUtil.isNull(item.getComment_count())) {// 有玩家评论
                topTexts.get(0).setText("玩家评论（" + item.getComment_count() + "）");
                if (item.getComment_count().equals("0")) {
                    viewPager.setCurrentItem(1);
                }
            } else {
                viewPager.setCurrentItem(1);
            }


            mSharedCount.setText(StringUtil.toUnitW(item.getShare_count()));

            if (item.flower_tick == 1){
               mGoodCount.setText( Html.fromHtml(TextUtil.toColor(StringUtil.toUnitW(item.getFlower_count()), "#ff3d2e")));
            }else {
                mGoodCount.setText(StringUtil.toUnitW(item.getFlower_count()));
            }

            if (item.collection_tick == 1){
                mStartCount.setText( Html.fromHtml(TextUtil.toColor(StringUtil.toUnitW(item.getCollection_count()), "#ff3d2e")));
            }else {
                mStartCount.setText(StringUtil.toUnitW(item.getCollection_count()));
            }

            int count = 0;
            try {
                count = Integer.parseInt(item.getFrequency());

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (count > 0){
                mGift.setImageResource(R.drawable.gift_selected);
                mGiftCount.setText( Html.fromHtml(TextUtil.toColor(StringUtil.toUnitW(item.getFrequency()), "#ff3d2e")));
            }else {
                mGift.setImageResource(R.drawable.gift_unselected);
                mGiftCount.setText(StringUtil.toUnitW(item.getFrequency()));
            }

            if (item.getFlower_tick() == 0){
                mGoodBtn.setChecked(false);
//                item.setFlower_tick(0);
            }else {
                mGoodBtn.setChecked(true);
//                item.setFlower_tick(1);
            }

            if (item.getCollection_tick() == 0){
                mStart.setChecked(false);
            }else {
                mStart.setChecked(true);
            }
        }
    }

    /**
     * 回调：播放页猜你喜歡推荐详情
     */
    public void onEventMainThread(ChangeVideo208Entity event) {
        if (event != null) {
            if (event.isResult()) {
                if (event.getData().getList().size() > 0) {
                    videoPlayView.completeView.setData(event.getData().getList());
                }
            }
        }
    }

    /**
     * 回调：评论
     */
//    public void onEventMainThread(BulletDo203Comment2VideoEntity event) {
//        if (event.isResult()) {
//            if (comment != null) {
//                ToastHelper.s("发布评论成功");
//                comment.setVideoImage(item);
//                viewPager.setCurrentItem(0);
//                comment.smoothScrollToPosition(0);
//
//                String video_comment_id = event.getData().getVideo_comment_id();
//                if (!StringUtil.isNull(video_comment_id)) {
//                    //用户内容及言论入口，IP等行为统计接口
//                    DataManager.userdatabehavior(getMember_id(), "", "", video_comment_id, "sysj_a", "",
//                            HareWareUtil.getHardwareCode(), HareWareUtil.getIMEI(), "");
//                }
//                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.VIDEOPLAY, "视频播放-评论");
//                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.VIDEOPLAY, "总评论次数");
//            }
//        }
//    }

    /**
     * 回调：弹幕发射
     */
    public void onEventMainThread(BulletDo203Bullet2VideoEntity event) {

        if (event.isResult()) {
            if (comment != null) {
                ToastHelper.s("发布评论成功");
                comment.setVideoImage(item);
                viewPager.setCurrentItem(0);
                comment.smoothScrollToPosition(0);

                String video_comment_id = event.getData().getVideo_comment_id();
                if (!StringUtil.isNull(video_comment_id)) {
                    //用户内容及言论入口，IP等行为统计接口
                    DataManager.userdatabehavior(getMember_id(), "", "", video_comment_id, "sysj_a", "",
                            HareWareUtil.getHardwareCode(), HareWareUtil.getIMEI(), "");
                }
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.VIDEOPLAY, "视频播放-评论");
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.VIDEOPLAY, "总评论次数");
                UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MACROSCOPIC_DATA, "总评论次数");
            }
        }
    }

    private int mSharedSuccessCount;
    /**
     * 回调：分享成功
     */
    public void onEventMainThread(SharedSuccessEvent event){
        //触发一下后台
        if (item != null && item.getVideo_id() != null){
            DataManager.sharedSuccess(item.getVideo_id());
            mSharedSuccessCount++;
            int count = 0;

            try {
                count = Integer.parseInt(item.getShare_count());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            mSharedCount.setText(StringUtil.toUnitW((count+mSharedSuccessCount)+""));
        }

    }

    /**
     * 回调：视频详情
     */
    public void onEventMainThread(VideoDetail226Entity event) {
        Log.w(tag, event.toString());
        if (event.isResult()) {
            setItem(event.getData());
            setFragmentData();
            refreshData(item);
            refreshTab(item);

        } else {
            ToastHelper.s(event.getMsg());
            finish();
        }
    }


    /**
     * 回调：打赏成功
     */
    public void onEventMainThread(PlayGiftResultEntity entity) {
        if (mGiftCount != null && entity.isResult()){
            // 视频详情
            mGiftCount.setText( Html.fromHtml(TextUtil.toColor(StringUtil.toUnitW(entity.getData().getRewardedPrice()), "#ff3d2e")));
        }
    }

    /**
     * 回调：弹幕列表
     */
    public void onEventMainThread(BulletList203Entity entity) {

        if (entity != null && entity.getData() != null) {
            if (videoPlayView != null)
                videoPlayView.loadDanmuku(entity);
            initTVDanmuDatas(entity);
        }
    }

    //投屏弹幕数据
    private List<WebPushInfo> danmuDatasOnTV;

    private void initTVDanmuDatas(BulletList203Entity entity) {
        danmuDatasOnTV = new ArrayList<>();
        for (int i = 0; i < entity.getData().size(); i++) {
            Bullet bullet = entity.getData().get(i);

            WebPushInfo webPushInfo = new WebPushInfo();
            webPushInfo.setContent(bullet.getContent());

            Double time = Double.valueOf(bullet.getVideo_node());
            webPushInfo.setDelaytime((long) (time * 1000));

            danmuDatasOnTV.add(webPushInfo);
        }
        Log.d(tag, "initTVDanmuDatas: datas == " + danmuDatasOnTV);
    }

    /**
     * 回调：字幕列表
     */
    public void onEventMainThread(SrtList203Entity entity) {

        if (entity.isResult() &&
                entity.getData() != null &&
                entity.getData().getFile() != null &&
                URLUtil.isURL(entity.getData().getFile())) {
            file = entity.getData().getFile();

            if (videoPlayView != null)
                videoPlayView.setFile(file);

            // 下載字幕
            DataManager.SUBTITLE.download(file);
        }
    }

    /**
     * 回调：视频收藏
     */
    public void onEventMainThread(VideoCollect2Entity entity) {

        if (entity != null) {
            if (!entity.isResult()) {
                showToastShort(entity.getMsg());
            }
        }
    }

    /**
     * 回调：视频点赞
     */
    public void onEventMainThread(VideoFlower2Entity event) {

        if (!event.isResult()) {
            showToastShort(event.getMsg());
        }else {

        }
    }

    /**
     * 回调：视频发布评论
     */
    public void onEventMainThread(VideoDoComment2CommentEntity event) {

        if (event.isResult()) {
            if (comment != null) {
                comment.setVideoImage(item);
            }
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.MACROSCOPIC_DATA, "总评论次数");
        }
    }

    /**
     * 回调：视频踩
     */
    public void onEventMainThread(FndownClick203Entity event) {

        if (!event.isResult()) {
            showToastShort(event.getMsg());
        }
    }

    /**
     * 回调：玩家关注
     */
    public void onEventMainThread(MemberAttention201Entity event) {

        if (event != null) {
            if (!event.isResult()) {
                showToastShort(event.getMsg());
            }
        }
    }

    /**
     * 回调：视频评论点赞
     */
    public void onEventMainThread(VideoCommentLike2Entity event) {

        if (event != null) {
            if (!event.isResult()) {
                showToastShort(event.getMsg());
            }
        }
    }

    private int orientation = Configuration.ORIENTATION_PORTRAIT;

    public boolean isLandscape() {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true;
        else
            return false;
    }

    // 用来判断屏幕是否锁屏
    private boolean isScreenOn;
    private PowerManager powerManager;
    private WakeLock wakeLock;
    private static final String POWER_LOCK = VideoPlayActivity.class.getSimpleName();

    public AudioManager audioManager;
    private int maxVolume, currentVolume;
    private float currentBrightness;// 0 - 1

    /**
     * 获取最大音量
     */
    public int getMaxVolume() {
        if (audioManager != null)
            maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.i(tag, "maxVolume=" + maxVolume);
        return maxVolume;
    }

    /**
     * 获取当前音量
     */
    public int getCurrentVolume() {
        if (audioManager != null)
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.i(tag, "currentVolume=" + currentVolume);
        return currentVolume;
    }

    /**
     * 设置当前音量
     */
    public void setCurrentVolume(int volume) {
        if (audioManager != null)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        Log.i(tag, "volume=" + volume);
    }

    /**
     * 获取当前亮度
     */
    public float getCurrentBrightness() {
        currentBrightness = getWindow().getAttributes().screenBrightness;
        Log.i(tag, "currentBrightness=" + currentBrightness);
        if (currentBrightness == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE)
            currentBrightness = 0.50f;
        else if (currentBrightness < 0.0f)
            currentBrightness = 0.01f;
        else if (currentBrightness > 1.0f)
            currentBrightness = 1.0f;
        return currentBrightness;
    }

    /**
     * 设置当前亮度
     */
    public void setCurrentBrightness(float brightness) {
        if (brightness > 1.0f)
            brightness = 1.0f;
        else if (brightness < 0.01f)
            brightness = 0.01f;
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.screenBrightness = brightness;
        getWindow().setAttributes(attributes);
        Log.i(tag, "brightness=" + brightness);
    }



    @SuppressWarnings("deprecation")
    @Override
    protected void onStop() {

        super.onStop();
        isScreenOn = powerManager.isScreenOn();
        // 屏幕变暗
        if (!isScreenOn) {
            if (videoPlayView != null)
                videoPlayView.pause();
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onStart() {
        super.onStart();
        isScreenOn = powerManager.isScreenOn();
        // 屏幕变亮
        if (isScreenOn) {
            if (null != wakeLock && (!wakeLock.isHeld())) {
                wakeLock.acquire();
            }
            if (videoPlayView != null)
                videoPlayView.resume();
        }

    }

    /**
     * 复位
     */
    @Override
    public void onResume() {
        super.onResume();

        //延迟初始化View 提示渲染速度
        runOnResume();

        if (videoPlayView != null)
            videoPlayView.resume();

        List<String> videoIds = PreferencesHepler.getInstance().getVideoIds();
        if (videoIds != null && videoIds.size() > 0) {
            // 推荐视频详情
            try {
                DataManager.changeVideo208(getVideoIdsRandom(videoIds));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        mEntryTime = System.currentTimeMillis();

    }

    private String getVideoIdsRandom(List<String> videoIds) throws Exception {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int index = RandomUtil.getRandom(0, videoIds.size() - 1);
            list.add(videoIds.get(index));
        }
        return ArrayHelper.list2Array(list);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mEntryTime = System.currentTimeMillis();
    }

    /**
     * 暂停
     */
    @Override
    public void onPause() {
        //onDestroyFragment();
        Map<String,String > map = new HashMap<>();
        map.put("video_id",item.getVideo_id());
        map.put("member_id",item.getMember_id());
        //统计播放页停留时长事件
        UmengAnalyticsHelper.onEventValue(this,UmengAnalyticsHelper.VIDEO_PLAY_DURATION,map,(int) (System.currentTimeMillis()/1000-mEntryTime/1000));

//        commitPlayDuration();
        super.onPause();
        if (videoPlayView != null)
            videoPlayView.pause();
    }



    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        afterCreate = false;
        ifFirst = true;
        ShareSDKShareHelper.stopSDK(this);
        if (videoPlayView != null)
            videoPlayView.destroy();

        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
            myBroadcastReceiver = null;
        }

        if (mGiftTimeLineFragment != null){
            mGiftTimeLineFragment.recyclerData();
        }

    }

    /**
     * 屏幕切换
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(tag, "newConfig=" + newConfig);
        int orientation = resources.getConfiguration().orientation;
        Log.e(tag, "onConfigurationChanged/orientation=" + orientation);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setMinSize();
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setMaxSize();
        }

        //隐藏选择页面
        hideGiftFragment(false);
        //

        //礼物时间轴
        resetTimeLineFragment();
    }

    /**
     * 保存播放进度
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putLong("lastPos", videoPlayView.lastPos);
        Log.e(tag, "lastPos=" + videoPlayView.lastPos);
    }

    /**
     * 取出播放进度
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("lastPos")) {
            videoPlayView.lastPos = savedInstanceState.getLong("lastPos");
            Log.e(tag, "lastPos=" + videoPlayView.lastPos);
        }
    }


    /**
     * 统计播放时长
     */
    public void commitPlayDuration(){
        long playTime = videoPlayView.videoPlayer.getPlayDuration();
        if (playTime == 0){
            return;
        }
        //上传到后台
        DataManager.commitStayDuration(getMember_id(),item.video_id,(int)playTime);

        System.out.println("play duration:"+playTime);
    }

    /**
     * 屏幕切换
     */
    public void toogleScreen() {
        if (isLandscape()) {
            setMinSize();
        } else {
            setMaxSize();
        }
    }

    /**
     * 横屏
     */
    public void setMaxSize() {
        if (commentView != null)
            commentView.hideView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        videoPlayView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        // setSystemUiVisibility，全屏状态下点击评论，videoview被挤压缩小了

        orientation = Configuration.ORIENTATION_LANDSCAPE;

        if (videoPlayView != null)
            videoPlayView.maxView();
        if (addDanmukuView != null)
            addDanmukuView.hideView();
        UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.VIDEOPLAY, "视频-全屏");

        if (mLandPlayGift != null){
            mLandPlayGift.setVisibility(View.VISIBLE);
        }
        if (mPlayGift != null){
            mPlayGift.setVisibility(View.GONE);
        }

    }

    /**
     * 竖屏
     */
    public void setMinSize() {
        if (addDanmukuView != null)
            addDanmukuView.hideView();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        orientation = Configuration.ORIENTATION_PORTRAIT;

        if (commentView != null)
            commentView.showView();
        if (videoPlayView != null)
            videoPlayView.minView();

        if (mLandPlayGift != null){
            mLandPlayGift.setVisibility(View.GONE);
        }
        if (mPlayGift != null){
            mPlayGift.setVisibility(View.VISIBLE);
        }

    }

    public String yk_url;
    public String youku_url;
    public String qn_key;
    public String qn_url;
    public String file;

    /**
     * 刷新数据
     */
    private void refreshData(VideoImage videoImage) {
        if (videoImage != null) {
            yk_url = videoImage.getYk_url();
            youku_url = AppConstant.getYoukuUrl(yk_url);

            Log.d(tag, "yk_url=" + yk_url);
            Log.d(tag, "youku_url=" + youku_url);


            videoPlayView.setVideoImage(videoImage);
            if (videoImage.getIspass() != null && videoImage.getIspass().equals("0")){
                videoPlayView.switchPlay(VideoPlayView.STATE_UNVETIFY);
                return;
            }
            if (!StringUtil.isNull(videoImage.getVideoUrl())) {
                if (NetUtil.isWIFI()) {
                    videoPlayView.switchPlay(VideoPlayView.STATE_VIDEOPLAY);
                } else {
                    videoPlayView.switchPlay(VideoPlayView.STATE_START);
                }
                return;
            } else if (!StringUtil.isNull(yk_url) && URLUtil.isURL(youku_url)) {
                videoPlayView.switchPlay(VideoPlayView.STATE_WEBPLAY);
                return;
            }
        }
        videoPlayView.switchPlay(VideoPlayView.STATE_ERROR);
    }

    /**
     * 返回按键
     */
    @Override
    public void onBackPressed() {
        if (addDanmukuView != null && addDanmukuView.isShowView()) {
            addDanmukuView.hideView();
        } else {
            if (isLandscape()) {
                setMinSize();
                return;
            } else {
                super.onBackPressed();
            }
        }
    }

    //乐播端口数据接收回调
    @Override
    public void onRefresh(Object object, int port) {
        switch (port) {
            case 2://播放进度控制
                Log.d(tag, "播放进度控制: setPlayVideoPosition == " + (boolean) object);
                break;
            case 4://开始播放
                Log.d(tag, "开始播放: setPlayControl == " + (boolean) object);
                break;
            case 5://暂停播放
                Log.d(tag, "暂停播放: setPlayControl == " + (boolean) object);
                break;
            case 7://退出播放
                Log.d(tag, "退出播放: setStopVideo == " + (boolean) object);
                try {
                    if ((boolean) object) {
                        videoPlayView.RefreshViewAfterStopLebo();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogHelper.e(tag, "乐播退出播放出错");
                }
            case 9://是否播放随手机端APK退出
                Log.d(tag, "是否播放随手机端APK退出: setIsBackgroundPlay == " + (boolean) object);
                break;
            case 14://TV弹幕是否显示
                Log.d(tag, "TV弹幕是否显示: setWebPushVisibility == " + (boolean) object);
                if ((boolean) object)
                    HpplayLinkControl.getInstance().sendOtherBeantoJSon(VideoPlayActivity.this, 19, danmuDatasOnTV, 2);
                break;
            case 19://弹幕数据（非自己发的）
                Log.d(tag, "TV弹幕数据: sendOtherBeantoJSon == " + (boolean) object);
                break;
            case 20://发布弹幕（自己发的）
                Log.d(tag, "发布弹幕: sendUserBeantoJSon == " + (boolean) object);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_play_gift_right:
            case R.id.iv_video_play_gift:
                if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();
                if (ifFirst == true) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setGiftFragmentState(true);
                            if (commentView != null) {
                                commentView.hideFaceView();
                            }
                        }
                    }, 500);
                    Log.d(tag, "ifFirst = " + ifFirst);
                } else if (ifFirst == false) {
                    setGiftFragmentState(true);
                    if (commentView != null) {
                        commentView.hideFaceView();
                    }
                    Log.d(tag, "ifFirst = " + ifFirst);
                }


                break;
            case R.id.ll_video_play_good:           //点赞
            case R.id.sb_video_play_good:
                if (item.getFlower_tick() == 0) {
                    mGoodBtn.setChecked(true);
                    mGoodBtn.playAnimation();
                    item.setFlower_tick(1);
                    item.setFlower_count(Integer.valueOf(item.getFlower_count()) + 1 + "");
                    UmengAnalyticsHelper.onEvent(VideoPlayActivity.this, UmengAnalyticsHelper.VIDEOPLAY, "视频播放-点赞");
                    EventBus.getDefault().post(new GoodAndStartEvent(GoodAndStartEvent.TYPE_GOOD,item.getVideo_id(),true));
                } else {
                    mGoodBtn.setChecked(false);
                    mGoodBtn.playAnimation();
                    item.setFlower_tick(0);
                    item.setFlower_count(Integer.valueOf(item.getFlower_count()) - 1 + "");
                    UmengAnalyticsHelper.onEvent(VideoPlayActivity.this, UmengAnalyticsHelper.VIDEOPLAY, "视频播放-取消赞");
                    EventBus.getDefault().post(new GoodAndStartEvent(GoodAndStartEvent.TYPE_GOOD,item.getVideo_id(),false));
                }
                // 视频点赞
                DataManager.videoFlower2(item.getId(), getMember_id());
                videoPlayView.controllerViewLand.refreshIconView(item);
               // mGoodCount.setText(StringUtil.toUnitW(item.getFlower_count()));
                refreshTab(item);
                break;
            case R.id.ll_video_play_start:
            case R.id.sb_video_play_start:
                if (!isLogin()) {
                    DialogManager.showLogInDialog(VideoPlayActivity.this);
                    return;
                }
                if (item.getCollection_tick() == 0) {
                    mStart.setChecked(true);
                    mStart.playAnimation();
                    item.setCollection_tick(1);
                    item.setCollection_count(Integer.valueOf(item.getCollection_count()) + 1 + "");
                    UmengAnalyticsHelper.onEvent(VideoPlayActivity.this, UmengAnalyticsHelper.VIDEOPLAY, "视频播放-收藏");
                    EventBus.getDefault().post(new GoodAndStartEvent(GoodAndStartEvent.TYPE_START,item.getVideo_id(),true));

                } else {
                    mStart.setChecked(false);
                    mStart.playAnimation();
                    item.setCollection_tick(0);
                    item.setCollection_count(Integer.valueOf(item.getCollection_count()) - 1 + "");
                    UmengAnalyticsHelper.onEvent(VideoPlayActivity.this, UmengAnalyticsHelper.VIDEOPLAY, "视频播放-取消收藏");
                    EventBus.getDefault().post(new GoodAndStartEvent(GoodAndStartEvent.TYPE_START,item.getVideo_id(),false));

                }

                // 视频收藏
                DataManager.videoCollect2(item.getId(), getMember_id());
                videoPlayView.controllerViewLand.refreshIconView(item);
                //mStartCount.setText(StringUtil.toUnitW(item.getCollection_count()));
                refreshTab(item);
                break;

            case R.id.ll_video_play_status_gift:
                if (comment != null){
                    if (!comment.showRankDialog()){
                        setGiftFragmentState(true);
                    }
                }
                break;
            case R.id.ll_video_play_shared:
                startShareActivity();
                break;
        }
    }
}
