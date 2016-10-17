package com.li.videoapplication.ui.view;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.happly.link.HpplayLinkControl;
import com.happly.link.HpplayLinkWindow;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.danmuku.DanmukuListEntity;
import com.li.videoapplication.data.danmuku.DanmukuListXmlParser;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.response.BulletList203Entity;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppConstant;
import com.li.videoapplication.tools.SubtitleHelper2;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.ui.popupwindows.ReportPopupWindow;
import com.li.videoapplication.utils.LogHelper;
import com.li.videoapplication.utils.NetUtil;
import com.li.videoapplication.utils.ScreenUtil;
import com.li.videoapplication.utils.StringUtil;
import com.li.videoapplication.utils.URLUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 视图：完成播放：重复播放
 */
public class VideoPlayView extends RelativeLayout implements
        View.OnClickListener,
        AddDanmukuView.AddDanmukuListener,
        IVideoPlay {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    public static final int STATE_PREPARE = 0;// 准备播放
    public static final int STATE_COMPLETE = 1;// 完成播放
    public static final int STATE_WEBPLAY = 2;// 网页播放
    public static final int STATE_VIDEOPLAY = 3;// 视频播放
    public static final int STATE_START = 4;// 开始播放
    public static final int STATE_TV = 5;// 乐播投屏
    public static final int STATE_ERROR = 9;// 错误

    private int state = STATE_PREPARE;

    private View view;
    private RelativeLayout root;

    private PrepareView prepareView;
    private ErrorView errorView;
    private StartView startView;
    public CompleteView completeView;
    private TitleBarView titleBarView;
    private ControllerView controllerView;
    public ControllerViewLand controllerViewLand;
    private RightBarView rightBarView;
    private TouchView touchView;
    private GPRSTipView gprsTipView;
    private LeBoView leBoView;

    public DanmukuPlayer danmukuPlayer;
    public TimedTextView timedTextView;

    // 新视频播放器pldroidplayer
    public VideoPlayer videoPlayer;
    private AspectRatioLayout layout;
    // 网页播放器
    private WebView webPlayer;

    public long lastPos = 0L;
    private String yk_url;
    private String youku_url;
    private String qn_key;
    private String qn_url;
    private String file;

    public VideoImage videoImage;
    public HpplayLinkControl linkControl;
    private int currentVolume;
    private SubtitleHelper2 subtitleHelper;

    public void setVideoImage(VideoImage videoImage) {
        this.videoImage = videoImage;
        Log.d(tag, "setVideoImage/videoImage=" + videoImage);

        if (videoImage != null) {
            yk_url = videoImage.getYk_url();
            youku_url = AppConstant.getYoukuUrl(yk_url);
            qn_key = videoImage.getQn_key();
            qn_url = AppConstant.getQnUrl(qn_key);
            Log.d(tag, "yk_url=" + yk_url);
            Log.d(tag, "youku_url=" + youku_url);
            Log.d(tag, "qn_key=" + qn_key);
            Log.d(tag, "qn_url=" + qn_url);

            controllerViewLand.setVideoImage(videoImage);
        }
    }

    public void setFile(String file) {
        this.file = file;
    }

    private LayoutInflater inflater;
    private Context context;
    private VideoPlayActivity activity;
    private Window window;
    private WindowManager windowManager;

    public VideoPlayView(Context context) {
        this(context, null);
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(VideoPlayActivity activity) {
        this.activity = activity;
        window = activity.getWindow();
        windowManager = window.getWindowManager();
        inflater = LayoutInflater.from(context);
//        乐播连接初始化：
        linkControl = HpplayLinkControl.getInstance();
        initContentView();
        minView();
    }

    private void initContentView() {

        view = inflater.inflate(R.layout.view_videoplay, this);
        root = (RelativeLayout) findViewById(R.id.root);

        errorView = (ErrorView) view.findViewById(R.id.videoplay_error);
        prepareView = (PrepareView) view.findViewById(R.id.videoplay_prepare);
        startView = (StartView) view.findViewById(R.id.videoplay_start);
        completeView = (CompleteView) view.findViewById(R.id.videoplay_complete);
        titleBarView = (TitleBarView) view.findViewById(R.id.videoplay_titlebar);
        controllerView = (ControllerView) view.findViewById(R.id.videoplay_controller);
        controllerViewLand = (ControllerViewLand) view.findViewById(R.id.videoplay_controller_land);
        rightBarView = (RightBarView) view.findViewById(R.id.videoplay_rightbar);
        touchView = (TouchView) view.findViewById(R.id.videoplay_touch);
        timedTextView = (TimedTextView) view.findViewById(R.id.videoplay_timedtext);
        leBoView = (LeBoView) view.findViewById(R.id.videoplay_lebo);
        gprsTipView = (GPRSTipView) view.findViewById(R.id.videoplay_gprstip);

        layout = (AspectRatioLayout) view.findViewById(R.id.videoplay_container);
        layout.setAspectRatio(RATIO_MAX);

        initWeb();
        initPlayer();

        timedTextView.init(activity);
        controllerView.init(this, videoPlayer, danmukuPlayer);
        controllerViewLand.init(this, videoPlayer, danmukuPlayer);
//        touchView.init(videoPlayer, this, controllerView);
        touchView.init(videoPlayer, this, controllerViewLand);
        completeView.init(activity);

        startView.setPlayListener(this);
        gprsTipView.setPlayListener(this);
        completeView.setReplayListener(this);
        rightBarView.setReportListener(this);
        rightBarView.setSendListener(this);
        rightBarView.setTVListener(this);
        leBoView.setStopLinkListener(this);
        leBoView.setPlayButtonListener(this);

//        titleBarView.setDanmukListener(this);
        controllerView.setDanmukListener(this);
        controllerViewLand.setDanmukListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start_play:// 中心播放

                break;

            case R.id.lebo_play:
                setPlay();
                break;

            case R.id.lebo_stoplink:// 退出投屏
                stopLebo();
                break;

            case R.id.gprs_play://无wifi播放提示播放确认键
                switchPlay(STATE_VIDEOPLAY);

                if (danmukuPlayer != null)
                    danmukuPlayer.resumeDanmaku();
                break;

            case R.id.complete_replay:// 重复播放
                switchPlay(STATE_VIDEOPLAY);
                if (danmukuPlayer != null)
                    danmukuPlayer.resumeDanmaku();
                break;

            case R.id.rightbar_report:// 举报
                showPopupWindow(videoImage, v);
                break;

            case R.id.rightbar_send:// 发射弹幕
                if (danmukuPlayer != null && activity != null && activity.addDanmukuView != null)
                    activity.addDanmukuView.showView();
                break;

            case R.id.rightbar_tv:

                if (!StringUtil.isNull(activity.qn_key) && URLUtil.isURL(activity.qn_url)) {
                    //选择投屏设备窗口
                    new HpplayLinkWindow(activity, activity.qn_url);
                    return;
                } else if (!StringUtil.isNull(activity.yk_url) && URLUtil.isURL(activity.youku_url)) {
                    new HpplayLinkWindow(activity, activity.youku_url);
                    return;
                }
                break;
        }

        // 打开关闭弹幕
        if (v.getId() == R.id.controller_damuku) {
            toogleDanmuku();
        }
    }

    //退出投屏
    private void stopLebo() {
        RequestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean isSuccess1 = linkControl.setIsBackgroundPlay(false);
                    boolean isSuccess2 = linkControl.setStopVideo();
                    try {
                        if (isSuccess1 && isSuccess2) {
                            getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    leBoView.hideView();
                                    touchView.showView();
                                    showWiget();
                                    activity.setCurrentVolume(currentVolume);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogHelper.e(tag, "乐播stopLebo出错");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogHelper.e(tag, "退出投屏出错");
                    activity.finish();
                }
            }
        });
    }

    private void setPlay() {
        if (videoPlayer != null) {
            leBoView.setPlay(videoPlayer.isPlaying());
            if (videoPlayer.isPlaying()) {
                videoPlayer.pause();
                if (danmukuPlayer != null)
                    danmukuPlayer.pauseDanmaku();
                RequestExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //乐播暂停/继续播放控制：参数：isPlay : true为继续播放,false为暂停播放
                        try {
                            linkControl.setPlayControl(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                videoPlayer.start();
                if (danmukuPlayer != null)
                    danmukuPlayer.resumeDanmaku();
                RequestExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            linkControl.setPlayControl(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /**
     * 举报
     */
    private ReportPopupWindow popupWindow;

    private void showPopupWindow(final VideoImage record, View view) {
        dismissPopupWindow();
        popupWindow = new ReportPopupWindow(activity, record);
        popupWindow.showPopupWindow(view);
    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    public boolean isDanmukuShow = false;

    /**
     * 开关弹幕
     */
    private void toogleDanmuku() {
        if (isDanmukuShow) {
            showDanmuku(false);
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.VIDEOPLAY, "弹幕-被关闭");
        } else {
            showDanmuku(true);
            resumeDanmuku(true);
            seekToDanmaku();
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.VIDEOPLAY, "弹幕-被打开");
        }
    }

    /**
     * 是否显示弹幕
     */
    private void showDanmuku(boolean isShow) {
        if (danmukuPlayer == null)
            return;
        if (!danmukuPlayer.prepared)
            return;
        if (controllerView == null)
            return;
        if (controllerViewLand == null)
            return;
        if (titleBarView == null)
            return;
        controllerView.setDanmuku(isShow);
        controllerViewLand.setDanmuku(isShow);
//        titleBarView.setDanmuku(isShow);
        controllerView.setDanmukuEnabledDelayed();
        controllerViewLand.setDanmukuEnabledDelayed();
//        titleBarView.setDanmukuEnabledDelayed();
        if (isShow) {
            danmukuPlayer.showDanmaku();
            Log.i(tag, "showDanmaku");
        } else {
            danmukuPlayer.hideDanmaku();
            Log.i(tag, "hideDanmaku");
        }
        isDanmukuShow = isShow;
    }

    /**
     * 是否显示弹幕
     */
    private void resumeDanmuku(boolean isResume) {
        if (danmukuPlayer == null)
            return;
        if (!danmukuPlayer.prepared)
            return;
        if (!isDanmukuShow)
            return;
        if (isResume) {
            danmukuPlayer.resumeDanmaku();
            Log.i(tag, "resumeDanmaku");
        } else {
            danmukuPlayer.pauseDanmaku();
            Log.i(tag, "pauseDanmaku");
        }
    }

    /**
     * 加载弹幕
     */
    public void loadDanmuku(BulletList203Entity entity) {
        Log.d(tag, "loadDanmuku/entity=" + entity);
        if (danmukuPlayer != null && !danmukuPlayer.prepared) {
            DanmukuListEntity danmulku = DanmukuListEntity.tranform(entity);
            DanmukuListXmlParser parser = new DanmukuListXmlParser();
            ByteArrayInputStream is = null;
            try {
                String xml = parser.serialize(danmulku);
                is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (is != null)
                danmukuPlayer.loadDanmaku(is);
        }
    }

    /**
     * 增加弹幕
     */
    @Override
    public boolean addDanmuku(String text) {
        Log.d(tag, "addDanmuku:/text=" + text);

        if (activity.isLandscape()) {
            if (controllerViewLand != null)
                controllerViewLand.performDanmukuClick();
        } else {
            if (controllerView != null)
                controllerView.performDanmukuClick();
        }

        if (danmukuPlayer != null) {
            danmukuPlayer.addDanmaku(text);
        }
        String video_node = getSubtitlePosition();
        // 弹幕发射
        DataManager.DANMUKU.bulletDo203Bullet2Video(videoImage.getId(),
                video_node,
                PreferencesHepler.getInstance().getMember_id(),
                text);
        activity.bullet = true;
        return true;
    }

    public String getSubtitlePosition() {
        String video_node;
        if (videoPlayer != null) {
            long poaition = videoPlayer.getVideoPosition();
            float seconds = ((float) poaition) / 1000;
            DecimalFormat format = new DecimalFormat(".0");
            video_node = format.format(seconds);
        } else {
            video_node = "0";
        }
        return video_node;
    }

    /**
     * 快进快退弹幕
     */
    public void seekToDanmaku(int progress) {
        Log.d(tag, "seekToDanmaku/progress=" + progress);
        if (danmukuPlayer != null) {
            long duration = videoPlayer.getVideoDuration();
            long position = progress * duration / 100;
            Log.i(tag, "seekToDanmaku/duration=" + duration);
            Log.i(tag, "seekToDanmaku/position=" + position);
            danmukuPlayer.seekToDanmaku(position);
        }
    }

    /**
     * 快进快退弹幕（根据播放器）
     */
    public void seekToDanmaku() {
        if (videoPlayer != null) {
            long duration = videoPlayer.getVideoDuration();
            long position = videoPlayer.getVideoPosition();
            int progress = (int) (position / duration);
            Log.i(tag, "seekToDanmaku/duration=" + duration);
            Log.i(tag, "seekToDanmaku/position=" + position);
            seekToDanmaku(progress);
        }
    }

    /**
     * 开关播放器
     */
    public void toogleVideo() {
        if (videoPlayer != null) {
            if (videoPlayer.isPlayingVideo()) {
                videoPlayer.pauseVideo();
                resumeDanmuku(false);
                UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.VIDEOPLAY, "视频-暂停");
            } else {
                videoPlayer.startVideo();
                resumeDanmuku(true);
            }

            if (controllerView != null)
                controllerView.setPlay(videoPlayer.isPlayingVideo());

            if (controllerViewLand != null)
                controllerViewLand.setPlay(videoPlayer.isPlayingVideo());
        }
    }

    public boolean isVoideoPlaying() {
        return videoPlayer != null && videoPlayer.isPlayingVideo();
    }

    /**
     * 快进快退播放器
     */
    public void seekToVideo(int progress) {
        Log.d(tag, "seekToVideo/progress=" + progress);
        if (videoPlayer != null) {
            long duration = videoPlayer.getVideoDuration();
            long position = progress * duration / 100;
            Log.i(tag, "seekToVideo/duration=" + duration);
            Log.i(tag, "seekToVideo/position=" + position);
            videoPlayer.seekToVideo(position);
            seekToDanmaku(progress);
            resumeDanmuku(videoPlayer.isPlayingVideo());
        }
    }

    private void initPlayer() {

        videoPlayer = (VideoPlayer) findViewById(R.id.videoplayer);
        videoPlayer.setOnErrorListener(onErrorListener);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoPlayer.setOnInfoListener(onInfoListener);
        }

        danmukuPlayer = (DanmukuPlayer) findViewById(R.id.danmuku);
        danmukuPlayer.initDanmuku();
    }


    private IMediaPlayer.OnCompletionListener onCompletionListener = new IMediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(IMediaPlayer PLMediaPlayer) {
            switchPlay(STATE_COMPLETE);
            if (controllerView != null) {
                controllerView.setPlay(false);
                controllerView.setProgress();
                controllerView.setDrag(false);
                controllerView.setTime();
                removeProgress();
                if (danmukuPlayer != null)
                    danmukuPlayer.hideDanmaku();
                if (timedTextView != null)
                    timedTextView.hideView();
            }

            if (controllerViewLand != null) {
                controllerViewLand.setPlay(false);
                controllerViewLand.setProgress();
                controllerViewLand.setDrag(false);
                controllerViewLand.setTime();
                removeProgress();
                if (danmukuPlayer != null)
                    danmukuPlayer.hideDanmaku();
                if (timedTextView != null)
                    timedTextView.hideView();
            }
        }
    };

    private long pos;

    private IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            setVideoRatio(iMediaPlayer);
            if (timedTextView != null)
                timedTextView.showView();
            addSubtitle(iMediaPlayer);
            iMediaPlayer.start();
            iMediaPlayer.seekTo((int) pos);
            updateProgress();
            if (controllerView != null) {
                controllerView.setPlay(false);
                controllerView.setProgress();
                controllerView.setDrag(false);
                controllerView.setTime();
            }
            if (controllerView != null)
                controllerView.performDanmukuClick();

            if (controllerViewLand != null) {
                controllerViewLand.setPlay(false);
                controllerViewLand.setProgress();
                controllerViewLand.setDrag(false);
                controllerViewLand.setTime();
            }
            if (controllerViewLand != null)
                controllerViewLand.performDanmukuClick();
        }
    };

    /**
     * 加载字幕
     */
    private void addSubtitle(IMediaPlayer iMediaPlayer) {
        if (iMediaPlayer == null)
            return;
        Log.d(tag, "addSubtitle/file=" + file);
        if (file == null)
            return;
        File f = SYSJStorageUtil.createFilecachePath(file);
        if (f == null || !f.exists())
            return;
        String path = f.getAbsolutePath();
        Log.d(tag, "addSubtitle/path=" + path);
        playSubtitle(true, path);
    }

    private void resumeSubtitle() {
        if (subtitleHelper != null) {
            subtitleHelper.setPlaying(true);
            subtitleHelper.playSubtitle();
        }
    }

    private void pauseSubtitle() {
        if (subtitleHelper != null) {
            subtitleHelper.setPlaying(false);
        }
    }

    private void playSubtitle(boolean playing, String path) {
        if (subtitleHelper == null) {
            subtitleHelper = new SubtitleHelper2(path, videoPlayer, timedTextView.content);
        }
        subtitleHelper.setPlaying(playing);
        subtitleHelper.playSubtitle();
    }



    private IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {

        @Override
        public boolean onInfo(IMediaPlayer PLMediaPlayer, int i, int i1) {
            // PLMediaPlayer.getDataSource()
            // PLMediaPlayer.getCurrentPosition()
            return false;
        }
    };

    private IMediaPlayer.OnErrorListener onErrorListener = new IMediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            Log.d(tag, "onError: code === " + i);
            // mp.getDataSource()
            // mp.getCurrentPosition()
            switchPlay(STATE_ERROR);
            if (danmukuPlayer != null)
                danmukuPlayer.hideDanmaku();
            try {
                progressHandler.removeCallbacks(progressRunnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (timedTextView != null)
                timedTextView.hideView();

            if (i == 100) {
                videoPlayer.stopPlayback();
            } else if (i == 1) {
                videoPlayer.stopPlayback();
            } else if (i == 800) {
                videoPlayer.stopPlayback();
            } else if (i == 701) {
                videoPlayer.stopPlayback();
            } else if (i == 700) {
                videoPlayer.stopPlayback();
            } else if (i == -38) {
                videoPlayer.stopPlayback();
            }
            return true;
        }
    };

    /**
     * 控件显示时间
     */
    private static final int TIME_DISMISS = 5000;
    private boolean isShow = false;
    private Handler showHandler = new Handler(Looper.getMainLooper());

    /**
     * 显示隐藏
     */
    public void toogleView() {
        if (isShow) {
            hideWiget();
        } else {
            showWiget();
        }
    }

    private void showWiget() {
        isShow = true;
        titleBarView.showView();

        if (activity.isLandscape()) {
            controllerViewLand.showView();
        } else {
            controllerView.showView();
            rightBarView.showView();
        }

        showHandler.removeCallbacksAndMessages(null);
        showHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideWiget();
            }
        }, TIME_DISMISS);
    }

    private void hideWiget() {
        isShow = false;
        titleBarView.hideView();

        if (activity.isLandscape()) {
            controllerViewLand.hideView();
        } else {
            controllerView.hideView();
        }

        rightBarView.hideView();
        dismissPopupWindow();
    }

    @SuppressWarnings("                                                                                                                                                                           ")
    private void initWeb() {

        // 网页播放
        webPlayer = (WebView) findViewById(R.id.webview);
        webPlayer.setVisibility(View.GONE);

        webPlayer.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webPlayer.getSettings().setJavaScriptEnabled(true);
        webPlayer.getSettings().setPluginState(WebSettings.PluginState.ON);
        webPlayer.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webPlayer.setWebChromeClient(new WebChromeClient());
        webPlayer.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private void updateProgress() {
        try {
            progressHandler.post(progressRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeProgress() {
        try {
            progressHandler.removeCallbacks(progressRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler progressHandler = new Handler();
    private Runnable progressRunnable = new Runnable() {

        @Override
        public void run() {
            if (videoPlayer != null) {
                if (controllerView != null) {
                    controllerView.setPlay(videoPlayer.isPlayingVideo());
                    controllerView.setProgress();
                    controllerView.setDrag(true);
                    controllerView.setTime();
                }

                if (controllerViewLand != null) {
                    controllerViewLand.setPlay(videoPlayer.isPlayingVideo());
                    controllerViewLand.setProgress();
                    controllerViewLand.setDrag(true);
                    controllerViewLand.setTime();
                }
            }
            progressHandler.postDelayed(progressRunnable, 1000);
        }
    };

    public void switchPlay(int state) {
        this.state = state;
        Log.d(tag, "switchPlay/state=" + state);

        /**
         * 准备播放
         */
        if (state == STATE_PREPARE) {
            LogHelper.d(tag, "========= STATE_PREPARE =========");
            prepareView.showView();

            touchView.hideView();
            errorView.hideView();
            startView.hideView();
            completeView.hideView();
            titleBarView.hideView();
            controllerView.hideView();
            controllerViewLand.hideView();
            rightBarView.hideView();
            gprsTipView.showView();
            leBoView.hideView();

            videoPlayer.setVisibility(GONE);
            webPlayer.setVisibility(GONE);
            return;
        }

        /**
         * 完成播放
         */
        if (state == STATE_COMPLETE) {
            LogHelper.d(tag, "========= STATE_COMPLETE =========");
            completeView.showView();

            touchView.hideView();
            errorView.hideView();
            startView.hideView();
            prepareView.hideView();
            titleBarView.hideView();
            controllerView.hideView();
            controllerViewLand.hideView();
            rightBarView.hideView();
            gprsTipView.hideView();
            leBoView.hideView();

            videoPlayer.setVisibility(GONE);
            webPlayer.setVisibility(GONE);

            //播放完变成竖屏
            activity.setMinSize();
            return;
        }

        /**
         * 网页播放
         */
        if (state == STATE_WEBPLAY && !StringUtil.isNull(yk_url) && URLUtil.isURL(youku_url)) {
            LogHelper.d(tag, "========= STATE_WEBPLAY =========");
            touchView.hideView();
            errorView.hideView();
            prepareView.hideView();
            startView.hideView();
            completeView.hideView();
            titleBarView.hideView();
            controllerView.hideView();
            controllerViewLand.hideView();
            rightBarView.hideView();
            gprsTipView.hideView();
            leBoView.hideView();

            videoPlayer.setVisibility(GONE);
            webPlayer.setVisibility(VISIBLE);
            loadWeb(youku_url);
            return;
        }

        /**
         * 开始播放
         */
        if (state == STATE_START && !StringUtil.isNull(qn_key) && URLUtil.isURL(qn_url)) {
            LogHelper.d(tag, "========= STATE_START =========");
            errorView.hideView();
            prepareView.hideView();
            touchView.hideView();
            startView.showView();

            if (videoImage != null && videoImage.getFlag() != null)
                startView.displayCoverImage(videoImage.getFlag());

            completeView.hideView();
            titleBarView.hideView();
            controllerView.hideView();
            controllerViewLand.hideView();
            rightBarView.hideView();
            leBoView.hideView();

            videoPlayer.setVisibility(VISIBLE);
            webPlayer.setVisibility(GONE);

            if (NetUtil.isWIFI()) {
                gprsTipView.hideView();
                switchPlay(STATE_VIDEOPLAY);
            } else {
                gprsTipView.showView();
            }
            return;
        }

        /**
         * 视频播放
         */
        if (state == STATE_VIDEOPLAY && qn_url != null && URLUtil.isURL(qn_url)) {
            LogHelper.d(tag, "========= STATE_VIDEOPLAY =========");
            errorView.hideView();
            prepareView.hideView();
            startView.hideView();
            gprsTipView.hideView();
            leBoView.hideView();
            touchView.showView();
            if (videoImage != null && videoImage.getTitle() != null)
                titleBarView.setTitleText(videoImage.getTitle());

            completeView.hideView();

            showWiget();

            videoPlayer.setVisibility(VISIBLE);
            webPlayer.setVisibility(GONE);

            startPlayer(qn_url, 0);
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.VIDEOPLAY, "视频播放次数");
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.MACROSCOPIC_DATA, "视频总播放次数");
            return;
        }

        /**
         * 乐播投屏
         */
        if (state == STATE_TV) {
            LogHelper.d(tag, "========= STATE_TV =========");
            errorView.hideView();
            prepareView.hideView();
            startView.hideView();
            gprsTipView.hideView();
            touchView.hideView();
            completeView.hideView();
            controllerView.hideView();
            controllerViewLand.hideView();
            rightBarView.hideView();
            titleBarView.hideView();

            currentVolume = activity.getCurrentVolume();
            activity.setCurrentVolume(0);

            leBoView.showView();
            if (videoImage != null && videoImage.getTitle() != null)
                leBoView.setTitleText(videoImage.getTitle());

            videoPlayer.setVisibility(VISIBLE);
            webPlayer.setVisibility(GONE);

            return;
        }

        if (state == STATE_ERROR) {
            LogHelper.d(tag, "========= STATE_ERROR =========");
            errorView.showView();
            leBoView.hideView();
            prepareView.hideView();
            startView.hideView();
            completeView.hideView();
            titleBarView.hideView();
            controllerView.hideView();
            controllerViewLand.hideView();
            rightBarView.hideView();

            videoPlayer.setVisibility(GONE);
            webPlayer.setVisibility(GONE);
        }
    }

    /**
     * 开始播放
     */
    private void startPlayer(final String url, final long pos) {
        Log.d(tag, "url=" + url);
        Log.d(tag, "startPlayer uri = " + Uri.parse(url));
        Log.d(tag, "pos=" + pos);

        if (window != null)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        prepareView.hideView();

        if (videoPlayer != null) {
            this.pos = pos;
            videoPlayer.setVideoURI(Uri.parse(url));
            videoPlayer.setOnPreparedListener(onPreparedListener);
            videoPlayer.setOnCompletionListener(onCompletionListener);
        }

        if (pos == 0) {
            // 完成任务
            DataManager.TASK.doTask_19(PreferencesHepler.getInstance().getMember_id());
            // 播放历史
            if (videoImage != null) {
                if (StringUtil.isNull(videoImage.getId())) {
                    videoImage.setId(videoImage.getVideo_id());
                }
                // 保存观看记录
                PreferencesHepler.getInstance().addHistoryVideoList(videoImage);
            }
        }
    }

    /**
     * 加载网页
     */
    private void loadWeb(final String url) {

        webPlayer.clearCache(true);
        webPlayer.loadUrl(url);
    }

    public void resume() {
        // 继续播放
        lastPos = VideoPlayActivity.playPos;
        Log.d(tag, "resume: state == " + state);
        if (state != STATE_TV) {
            if (lastPos != 0 && URLUtil.isURL(qn_url)) {
                startPlayer(qn_url, lastPos);
            }
            if (webPlayer != null && URLUtil.isURL(youku_url)) {
                webPlayer.onResume();
                try {
                    webPlayer.getClass().getMethod("onResume").invoke(webPlayer, (Object[]) null);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            try {
                danmukuPlayer.resumeDanmaku();
                resumeSubtitle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        Log.d(tag, "pause: state == " + state);
        if (state != STATE_TV) {
            if (videoPlayer != null) {
                lastPos = videoPlayer.getVideoPosition();
                VideoPlayActivity.playPos = lastPos;
                VideoPlayActivity.playUrl = qn_url;
                Log.e(tag, "lastPos=" + lastPos);
                videoPlayer.pauseVideo();
            }
            if (webPlayer != null) {
                webPlayer.onPause();
                try {
                    webPlayer.getClass().getMethod("onPause").invoke(webPlayer, (Object[]) null);
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            try {
                danmukuPlayer.pauseDanmaku();
                pauseSubtitle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void destroy() {
        lastPos = 0;
        VideoPlayActivity.playPos = 0;
        VideoPlayActivity.playUrl = "";
        if (videoPlayer != null) {
            videoPlayer.pauseVideo();
            videoPlayer.stopPlayback();
        }
        if (webPlayer != null) {
            webPlayer.clearHistory();
            webPlayer.removeAllViewsInLayout();
            webPlayer.clearDisappearingChildren();
            webPlayer.clearFocus();
            webPlayer.clearView();
            webPlayer.loadUrl("");
            webPlayer.destroy();
            try {
                webPlayer.getClass().getMethod("destroy").invoke(webPlayer, (Object[]) null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        try {
            danmukuPlayer.destroyDanmaku();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (showHandler != null)
            showHandler.removeCallbacksAndMessages(null);
        if (progressHandler != null)
            progressHandler.removeCallbacksAndMessages(null);

//        try {
//            linkControl.colseHpplayLink();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static final float RATIO_MAX = 16F / 9F;

    private void setVideoRatio(IMediaPlayer iMediaPlayer) {
        if (iMediaPlayer == null)
            return;
        float width = iMediaPlayer.getVideoWidth();
        float height = iMediaPlayer.getVideoHeight();
        float ratio = width / height;
        if (layout != null)
            layout.setAspectRatio(ratio);
    }

    @Override
    public void minView() {
        Log.i(tag, "state=" + state);
        setRelativeMinSixe(this);
        setRelativeMinSixe(videoPlayer);

        if (state == STATE_VIDEOPLAY || state == STATE_TV)
            touchView.showView();
        else
            touchView.hideView();

        titleBarView.minView();
        controllerView.showView();
        controllerViewLand.hideView();
        rightBarView.minView();
        timedTextView.minView();
        leBoView.minView();
    }

    @Override
    public void maxView() {
        Log.i(tag, "state=" + state);
        setRelativeFullScreen(this);
        setRelativeFullScreen(videoPlayer);

        if (state == STATE_VIDEOPLAY || state == STATE_TV)
            touchView.showView();
        else
            touchView.hideView();

        titleBarView.maxView();
        controllerView.hideView();
        controllerViewLand.showView();
        rightBarView.hideView();
        timedTextView.maxView();
        leBoView.maxView();
    }

    /**
     * 全屏布局
     */
    public void setLinearFullScreen(View view) {
        if (view != null) {
            int w = ScreenUtil.getScreenWidth(windowManager);
            int h = ScreenUtil.getScreenHeight(windowManager);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
            view.setLayoutParams(params);
        }
    }

    /**
     * 全屏布局
     */
    public void setRelativeFullScreen(View view) {

        if (view != null) {
            int w = ScreenUtil.getScreenWidth(windowManager);
            int h = ScreenUtil.getScreenHeight(windowManager);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
            view.setLayoutParams(params);
        }
    }

    /**
     * 最小布局
     */
    public void setLinearMinSixe(View view) {
        if (view != null) {
            int w = ScreenUtil.getScreenWidth(windowManager);
            int h = ScreenUtil.dp2px(180);
            float ratio = (float) 9 / (float) 16;
            h = (int) (w * ratio);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
            view.setLayoutParams(params);
        }
    }

    /**
     * 最小布局
     */
    public void setRelativeMinSixe(View view) {
        if (view != null) {
            int w = ScreenUtil.getScreenWidth(windowManager);
            int h = ScreenUtil.dp2px(180);
            float ratio = (float) 9 / (float) 16;
            h = (int) (w * ratio);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
            view.setLayoutParams(params);
        }
    }

    @Override
    public void showView() {
    }

    @Override
    public void hideView() {
    }
}
