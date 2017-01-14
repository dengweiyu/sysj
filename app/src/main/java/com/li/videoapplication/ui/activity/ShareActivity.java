package com.li.videoapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.network.UITask;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseActivity;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.utils.LogHelper;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * 活动：分享
 */
public class ShareActivity extends BaseActivity implements OnClickListener {

    public static final int PAGE_SYSJ = 22;
    public static final int PAGE_VIDEOPLAY = 33;
    public static final int PAGE_MYCLOUDVIDEO = 44;
    public static final int PAGE_MYCLOCALVIDEO = 55;
    public static final int PAGE_MYSCREENSHOT = 66;

    @Override
    public void refreshIntent() {
        super.refreshIntent();
        Intent intent = getIntent();
        page = intent.getIntExtra("page", 0);
        videoUrl = intent.getStringExtra("videoUrl");
        VideoTitle = intent.getStringExtra("VideoTitle");
        imageUrl = intent.getStringExtra("imageUrl");
        // 这个为邀请好友的内容字段
        text = intent.getStringExtra("content");

        Log.d(tag, "VideoTitle: " + VideoTitle);
    }

    private View touch;
    private Button cancel;
    private LinearLayout qq, qzone, wx, wb, wxfriends, sysj;

    private String videoUrl, imageUrl, VideoTitle, text;
    private int page;

    private PlatformActionListener listener = new PlatformActionListener() {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            UmengAnalyticsHelper.onEvent(ShareActivity.this, UmengAnalyticsHelper.SLIDER, "邀请好友-有效");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            LogHelper.i(tag, "throwable : " + throwable);
        }

        @Override
        public void onCancel(Platform platform, int i) {

        }
    };

    private AlphaAnimation enterAnimation, exitAnimation;

    @Override
    public void beforeOnCreate() {
        super.beforeOnCreate();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void afterOnCreate() {
        super.afterOnCreate();

        setContentView(R.layout.activity_share);
        ShareSDK.initSDK(this);
    }

    @Override
    public void initView() {
        super.initView();

        initContentView();
        startEnterAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void onDestroy() {
        startExitAnimation();
        super.onDestroy();
        ShareSDK.stopSDK(this);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_hold, R.anim.push_bottom_out);
    }

    private void startEnterAnimation() {

        if (touch != null) {
            enterAnimation = new AlphaAnimation(0.1f, 1.0f);
            enterAnimation.setDuration(300);
            touch.startAnimation(enterAnimation);
        }
    }

    private void startExitAnimation() {

        if (touch != null) {
            exitAnimation = new AlphaAnimation(1.0f, 0.1f);
            exitAnimation.setDuration(300);
            touch.startAnimation(exitAnimation);
        }
    }

    private void initContentView() {

        sysj = (LinearLayout) findViewById(R.id.share_sysj);
        wx = (LinearLayout) findViewById(R.id.share_wx);
        qq = (LinearLayout) findViewById(R.id.share_qq);
        wxfriends = (LinearLayout) findViewById(R.id.share_wxfriends);
        qzone = (LinearLayout) findViewById(R.id.share_qzone);
        wb = (LinearLayout) findViewById(R.id.share_wb);

        touch = findViewById(R.id.share_touch);
        cancel = (Button) findViewById(R.id.videoplay_cancel);


        sysj.setOnClickListener(this);
        wx.setOnClickListener(this);
        qq.setOnClickListener(this);
        wxfriends.setOnClickListener(this);
        qzone.setOnClickListener(this);
        wb.setOnClickListener(this);

        touch.setOnClickListener(this);
        cancel.setOnClickListener(this);

        if (page == PAGE_MYCLOUDVIDEO || page == PAGE_SYSJ || page == PAGE_VIDEOPLAY) {
            sysj.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == wx) {
            share(this, VideoTitle, videoUrl, text, imageUrl, "Wechat");
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SHARE, "分享渠道-微信");
        } else if (v == qq) {
            share(this, VideoTitle, videoUrl, text, imageUrl, "QQ");
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SHARE, "分享渠道-qq");
        } else if (v == wxfriends) {
            share(this, VideoTitle, videoUrl, text, imageUrl, "WechatMoments");
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SHARE, "分享渠道-朋友圈");
        } else if (v == wb) {
            share(this, VideoTitle, videoUrl, text, imageUrl, "SinaWeibo");
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SHARE, "分享渠道-新浪微博");
        } else if (v == qzone) {
            share(this, VideoTitle, videoUrl, text, imageUrl, "QZone");
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SHARE, "分享渠道-QQ空间");
        } else if (v == sysj) {
            share(this, VideoTitle, videoUrl, text, imageUrl, "SYSJ");
            UmengAnalyticsHelper.onEvent(this, UmengAnalyticsHelper.SHARE, "分享渠道-手游视界");
        }
        finish();
    }

    /**
     * 分享方法
     *
     * @param context      上下文
     * @param title        分享的标题
     * @param url          分享的超链接
     * @param text         分享的内容
     * @param imageUrl     分享的图片
     * @param shareChannel 分享的频道 Wechat：
     *                     QQ：
     *                     WechatMoments：
     *                     SinaWeibo：
     *                     QZone：
     *                     SYSJ：手游视界
     */
    public void share(Context context, String title, String url, String text, String imageUrl, final String shareChannel) {
        Log.d(tag, "share: shareChannel=" + shareChannel);
        ShareParams params = new ShareParams();
        if (page == PAGE_MYCLOCALVIDEO) {// 本地视频
            UITask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventManager.postShare2VideoShareEvent(shareChannel);
                }
            }, 200);
            finish();
        } else if (page == PAGE_MYSCREENSHOT) {// 本地图片
            params.setImagePath(imageUrl);
            params.setShareType(Platform.SHARE_IMAGE);
            if (shareChannel.equals("SYSJ")) {
                UITask.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startImageShareActivity();
                    }
                }, 200);
                finish();
            } else {
                if (shareChannel.equals("QZone")) {
                    params.setTitleUrl("http://lp.ifeimo.com/");
                    params.setUrl("http://lp.ifeimo.com/");
                    params.setTitle(title);
                    params.setText("我分享了一张图片");
                }
            }
        } else if (page == PAGE_MYCLOUDVIDEO) {// 云端视频
            // 新浪微博文字内容无法携带超链接， 所以将超链接直接放在分享内容中
            if (shareChannel.equals("SinaWeibo")) {
                params.setText(text + url);
            }
            params.setImageUrl(imageUrl);
            params.setTitle(title);
            params.setText(text);
            params.setUrl(url);
            params.setTitleUrl(url);
            params.setShareType(Platform.SHARE_WEBPAGE);

        } else if (page == PAGE_SYSJ) {// 手游视界
            // 新浪微博文字内容无法携带超链接， 所以将超链接直接放在分享内容中
            if (shareChannel.equals("SinaWeibo")) {
                params.setText(text + url);
            }
            params.setImageUrl(imageUrl);
            params.setTitle(title);
            params.setText(text);
            params.setUrl(url);
            params.setTitleUrl(url);
            params.setShareType(Platform.SHARE_WEBPAGE);
        } else if (page == PAGE_VIDEOPLAY) {// 视频播放
            // 新浪微博文字内容无法携带超链接， 所以将超链接直接放在分享内容中
            if (shareChannel.equals("SinaWeibo")) {
                params.setText(text + url);
            }
            params.setImageUrl(imageUrl);
            params.setTitle(title);
            params.setText(text);
            params.setUrl(url);
            params.setTitleUrl(url);
            params.setShareType(Platform.SHARE_WEBPAGE);
            Log.d(tag, "share: 7");
        }
        if (!shareChannel.equals("SYSJ")) {
            Platform platform = ShareSDK.getPlatform(context, shareChannel);
            if (listener != null) {
                platform.setPlatformActionListener(listener);
            }
            platform.share(params);
            Log.d(tag, "share: 9");
        }
    }

    private void startImageShareActivity() {
        VideoMangerActivity activity = (VideoMangerActivity) AppManager.getInstance().getActivity(VideoMangerActivity.class);
        Game game = null;
        if (activity != null && activity.game != null) {
            game = activity.game;
        }
        ActivityManeger.startImageShareActivity(this, this.imageUrl, game);
    }
}