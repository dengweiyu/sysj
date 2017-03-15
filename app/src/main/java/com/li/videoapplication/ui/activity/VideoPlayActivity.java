package com.li.videoapplication.ui.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.entity.Bullet;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.BulletDo203Bullet2VideoEntity;
import com.li.videoapplication.data.model.response.BulletList203Entity;
import com.li.videoapplication.data.model.response.ChangeVideo208Entity;
import com.li.videoapplication.data.model.response.FndownClick203Entity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.SrtList203Entity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoCommentLike2Entity;
import com.li.videoapplication.data.model.response.VideoDetail201Entity;
import com.li.videoapplication.data.model.response.VideoDoComment2CommentEntity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.framework.TBaseActivity;
import com.li.videoapplication.tools.ArrayHelper;
import com.li.videoapplication.tools.RandomUtil;
import com.li.videoapplication.tools.ShareSDKShareHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.DialogManager;
import com.li.videoapplication.ui.fragment.AuthorVideoListFragment;
import com.li.videoapplication.ui.fragment.VideoPlayCommentFragment;
import com.li.videoapplication.ui.fragment.VideoPlayIntroduceFragment;
import com.li.videoapplication.ui.fragment.VideoPlayVideoFragment;
import com.li.videoapplication.ui.pageradapter.GamePagerAdapter;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.view.AddDanmukuView;
import com.li.videoapplication.ui.view.CommentView;
import com.li.videoapplication.ui.view.VideoPlayView;
import com.li.videoapplication.utils.HareWareUtil;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;
import com.li.videoapplication.views.ViewPagerY4;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ViewPagerOverScrollDecorAdapter;

/**
 * 活动：视频详情
 */
@SuppressLint("SetJavaScriptEnabled")
public class VideoPlayActivity extends TBaseActivity implements
        OnPageChangeListener,
        CommentView.CommentListener,
        RefreshUIInterface {

    public static long playPos;
    public static String playUrl;

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
                ActivityManeger.startActivityShareActivity4VideoPlay(this, url, title, imageUrl, content);
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

    public VideoImage item;

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
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        actionBar.hide();

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

        initTopMenu();
        initContentView();
    }

    @Override
    public void loadData() {
        super.loadData();
        // 网页视频 3137
        // 视频详情
        DataManager.videoDetail201(item.getId(), getMember_id());

        // 弹幕列表
        DataManager.DANMUKU.bulletList203(item.getId());

        // 字幕列表
        DataManager.SUBTITLE.srtList203(item.getId());

        IntentFilter filter = new IntentFilter();
        filter.addAction(Const.LINK_PLAY_STATE);
        filter.addAction(Const.HPPLAY_LINK_DISCONNECT);
        registerReceiver(myBroadcastReceiver, filter);
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

    private void initContentView() {
        videoPlayView = (VideoPlayView) findViewById(R.id.videoplay);
        videoPlayView.init(this);
        videoPlayView.minView();

        if (NetUtil.isWIFI()) {
            videoPlayView.switchPlay(VideoPlayView.STATE_VIDEOPLAY);
            if (videoPlayView.danmukuPlayer != null)
                videoPlayView.danmukuPlayer.resumeDanmaku();
        } else {
            videoPlayView.switchPlay(VideoPlayView.STATE_PREPARE);
        }

        commentView = (CommentView) findViewById(R.id.comment);
        commentView.init(this);
        commentView.setCommentListener(this);
        commentView.showView();

        addDanmukuView = (AddDanmukuView) findViewById(R.id.adddanmuku);
        addDanmukuView.init(this);
        addDanmukuView.setAddDanmukuListener(videoPlayView);
    }

    // 是否是弹幕
//    public boolean bullet;

    @Override
    public boolean comment(boolean isSecondComment, String text) {
        Log.d(tag, "comment/text=" + text);
        if (videoPlayView != null && videoPlayView.isVoideoPlaying()) {
            videoPlayView.addDanmuku(text);
        }
        // 评论
//        DataManager.DANMUKU.bulletDo203Comment2Video(item.getVideo_id(),
//                getMember_id(),
//                text);
//        bullet = false;
        return true;
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

    /**
     * 回调：视频详情
     */
    public void onEventMainThread(VideoDetail201Entity event) {
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
        if (videoPlayView != null)
            videoPlayView.resume();

        List<String> videoIds = PreferencesHepler.getInstance().getVideoIds();
        if (videoIds != null && videoIds.size() > 0) {
            // 推荐视频详情
            DataManager.changeVideo208(getVideoIdsRandom(videoIds));
        }
    }

    private String getVideoIdsRandom(List<String> videoIds) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int index = RandomUtil.getRandom(0, videoIds.size() - 1);
            list.add(videoIds.get(index));
        }
        return ArrayHelper.list2Array(list);
    }

    /**
     * 暂停
     */
    @Override
    public void onPause() {
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
        ShareSDKShareHelper.stopSDK(this);
        if (videoPlayView != null)
            videoPlayView.destroy();

        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
            myBroadcastReceiver = null;
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
            qn_key = videoImage.getQn_key();
            qn_url = AppConstant.getQnUrl(qn_key);
            Log.d(tag, "yk_url=" + yk_url);
            Log.d(tag, "youku_url=" + youku_url);
            Log.d(tag, "qn_key=" + qn_key);
            Log.d(tag, "qn_url=" + qn_url);

            videoPlayView.setVideoImage(videoImage);
            if (!StringUtil.isNull(qn_key) && URLUtil.isURL(qn_url)) {
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
}
