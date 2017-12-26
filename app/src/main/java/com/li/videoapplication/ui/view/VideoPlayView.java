package com.li.videoapplication.ui.view;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.happly.link.HpplayLinkControl;
import com.happly.link.HpplayLinkWindow;
import com.happly.link.bean.WebPushInfo;
import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.danmuku.DanmukuListEntity;
import com.li.videoapplication.data.danmuku.DanmukuListXmlParser;
import com.li.videoapplication.data.image.GlideHelper;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.data.model.event.ResetTimeLineEvent;
import com.li.videoapplication.data.model.response.BulletList203Entity;
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
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.rong.eventbus.EventBus;

import static com.pili.pldroid.player.PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT;

/**
 * 视图：完成播放：重复播放
 */
public class VideoPlayView extends RelativeLayout implements
        View.OnClickListener,
        AddDanmukuView.AddDanmukuListener,
        IVideoPlay {

    private static boolean DEBUG = true;
    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    public static final int STATE_PREPARE = 0;// 准备播放
    public static final int STATE_COMPLETE = 1;// 完成播放
    public static final int STATE_WEBPLAY = 2;// 网页播放
    public static final int STATE_VIDEOPLAY = 3;// 视频播放
    public static final int STATE_START = 4;// 开始播放
    public static final int STATE_TV = 5;// 乐播投屏
    public static final int STATE_ERROR = 9;// 错误
    public static final int STATE_UNVETIFY = -1;    //视频未审核

    public int state = STATE_PREPARE;

    private View view;
    private ImageView mUnVerifyView;
    private PrepareView prepareView;
    private ErrorView errorView;
    private StartView startView;
    public CompleteView completeView;
    private TitleBarView titleBarView;
    private ControllerView controllerView;
    public ControllerViewLand controllerViewLand;
    private RightBarView rightBarView;
    public TouchView touchView;
    private GPRSTipView gprsTipView;
    public LeBoView leBoView;

    public DanmukuPlayer danmukuPlayer;
    public TimedTextView timedTextView;

    // 新视频播放器pldroidplayer
    public VideoPlayer videoPlayer;
    // 网页播放器
    private WebView webPlayer;

    public long lastPos = 0L;
    private String yk_url;
    private String youku_url;
   // private String qn_key;
  //  private String qn_url;

    private String mVideoUrl;
    private String file;

    public VideoImage videoImage;
    public HpplayLinkControl linkControl;
    public int volumeBeforeTV;
    private SubtitleHelper2 subtitleHelper;

    public void setVideoImage(VideoImage videoImage) {
        this.videoImage = videoImage;

        if (videoImage != null) {
            yk_url = videoImage.getYk_url();
            youku_url = AppConstant.getYoukuUrl(yk_url);

            if (DEBUG) {
                Log.d(tag, "yk_url=" + yk_url);
                Log.d(tag, "youku_url=" + youku_url);
            }

            mVideoUrl = videoImage.getVideoUrl();
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

        errorView = (ErrorView) view.findViewById(R.id.videoplay_error);
        errorView.setPlayView(this);

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
                UmengAnalyticsHelper.onEvent(context,UmengAnalyticsHelper.VIDEOPLAY,"推荐/重播-重新播放");
                break;

            case R.id.rightbar_report:// 举报
                showPopupWindow(videoImage, v);
                break;

            case R.id.rightbar_send:// 发射弹幕
                if (danmukuPlayer != null && activity != null && activity.addDanmukuView != null)
                    activity.addDanmukuView.showView();
                break;

            case R.id.rightbar_tv: //投屏
                if (!StringUtil.isNull(activity.qn_key) && URLUtil.isURL(activity.qn_url)) {
                    //选择投屏设备窗口
                    new HpplayLinkWindow(activity, activity.qn_url);
                    return;
                } else if (!StringUtil.isNull(activity.yk_url) && URLUtil.isURL(activity.youku_url)) {
                    new HpplayLinkWindow(activity, activity.youku_url);
                    return;
                }
                UmengAnalyticsHelper.onEvent(context,UmengAnalyticsHelper.VIDEOPLAY,"投屏-点击投屏次数");
                break;
        }

        // 打开关闭弹幕
        if (v.getId() == R.id.controller_damuku) {
            toogleDanmuku();
        }
    }

    //退出投屏
    private void stopLebo() {
        try {
            linkControl.setIsBackgroundPlay(activity, 9, false);
            linkControl.setStopVideo(activity, 7);
        } catch (Exception e) {
            e.printStackTrace();
            if (DEBUG) LogHelper.e(tag, "退出投屏出错");
            activity.finish();
        }
    }

    public void RefreshViewAfterStopLebo() {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                leBoView.hideView();
                touchView.showView();
                toogleView();
                activity.setCurrentVolume(volumeBeforeTV);
                if (DEBUG) Log.d(tag, "投屏结束后恢复的音量: " + activity.getCurrentVolume());
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
                //乐播暂停/继续播放控制：参数：isPlay : true为继续播放,false为暂停播放
                try {
                    linkControl.setPlayControl(activity, 5, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                videoPlayer.start();
                if (danmukuPlayer != null)
                    danmukuPlayer.resumeDanmaku();
                try {
                    linkControl.setPlayControl(activity, 4, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
     * 是否在TV显示弹幕 fixme 没执行到~
     */
    private void showTVDanmu(boolean isShow) {
        if (DEBUG) Log.d(tag, "showTVDanmu: isDanmukuShow == " + isShow);
        //接收端是否支持弹幕
        Boolean isSucceed = linkControl.isHasWebPush();
        if (DEBUG) Log.d(tag, "接收端是否支持弹幕: " + isSucceed);
        if (isSucceed) {
            linkControl.setWebPushVisibility(activity, 14, isShow);
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
            if (DEBUG) Log.i(tag, "showDanmaku");
        } else {
            danmukuPlayer.hideDanmaku();
            if (DEBUG) Log.i(tag, "hideDanmaku");
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
            if (DEBUG) Log.i(tag, "resumeDanmaku");
        } else {
            danmukuPlayer.pauseDanmaku();
            if (DEBUG) Log.i(tag, "pauseDanmaku");
        }
    }

    /**
     * 加载弹幕
     */
    public void loadDanmuku(BulletList203Entity entity) {
        if (DEBUG) Log.d(tag, "loadDanmuku/entity=" + entity);
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
     * Todo 增加弹幕
     */
    @Override
    public boolean addDanmuku(String text) {
        if (DEBUG) Log.d(tag, "addDanmuku:/text=" + text);

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
//        activity.bullet = true;

        if (DEBUG)
            Log.d(tag, "addDanmuku: state == " + state + " ,isDanmukuShow == " + isDanmukuShow);
        if (state == STATE_TV && isDanmukuShow) {
            boolean hasWebPush = linkControl.isHasWebPush();//接收端是否支持弹幕
            if (DEBUG) Log.d(tag, "接收端是否支持弹幕: " + hasWebPush);
            if (hasWebPush) {
                List<WebPushInfo> webPushuserList = new ArrayList<>();
                WebPushInfo webPushInfo = new WebPushInfo();
                webPushInfo.setContent(text);
                if (DEBUG) Log.d(tag, "addDanmuku: getVideoPosition == "+videoPlayer.getVideoPosition());
                webPushInfo.setDelaytime(videoPlayer.getVideoPosition());
                webPushuserList.add(webPushInfo);
                linkControl.sendUserBeantoJSon(activity, 20, webPushuserList, 2);//type：1为直播，2为非直播流
            }
        }
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
        if (DEBUG) Log.d(tag, "seekToDanmaku/progress=" + progress);
        if (danmukuPlayer != null) {
            long duration = videoPlayer.getVideoDuration();
            long position = progress * duration / 100;
            if (DEBUG) Log.i(tag, "seekToDanmaku/duration=" + duration);
            if (DEBUG) Log.i(tag, "seekToDanmaku/position=" + position);
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
            if (DEBUG) Log.i(tag, "seekToDanmaku/duration=" + duration);
            if (DEBUG) Log.i(tag, "seekToDanmaku/position=" + position);
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
        if (DEBUG) Log.d(tag, "seekToVideo/progress=" + progress);
        if (videoPlayer != null) {
            long duration = videoPlayer.getVideoDuration();
            long position = progress * duration / 100;
            if (DEBUG) {
                Log.i(tag, "seekToVideo/duration=" + duration);
                Log.i(tag, "seekToVideo/position=" + position);
            }
            videoPlayer.seekToVideo(position);
            seekToDanmaku(progress);
            resumeDanmuku(videoPlayer.isPlayingVideo());

            //拖动进度 重置时间轴
            EventBus.getDefault().post(new ResetTimeLineEvent());

            activity.resetTimeLineData();
        }
    }

    private void initPlayer() {
        videoPlayer = (VideoPlayer) findViewById(R.id.videoplayer);
        videoPlayer.setOnErrorListener(onErrorListener);
        videoPlayer.setOnInfoListener(onInfoListener);

        View loadingView = findViewById(R.id.videoplay_loading);
        videoPlayer.setBufferingIndicator(loadingView);

        danmukuPlayer = (DanmukuPlayer) findViewById(R.id.danmuku);
        danmukuPlayer.initDanmuku();
    }


    private PLMediaPlayer.OnCompletionListener onCompletionListener = new PLMediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            videoPlayer.stopPlayback();
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

    //播放进度监听器
    public interface OnProgressListener{
        void onProgress(long p);
    }

    private int mDelay;
    //增加监听
    private List<OnProgressListener> mOnPreparedListeners = new ArrayList<>();

    /**
     *
     * @param listener
     * @param delay
     *              细粒度 所有新增的监听器会使用同一个细粒度
     */
    public void addOnPreparedListener(OnProgressListener listener,int delay){
        mOnPreparedListeners.add(listener);
        mDelay = delay;
        if (progressHandlerNew == null){
            progressHandlerNew = new Handler(Looper.getMainLooper());
        //    progressHandlerNew.post(progressRunnableNew);
        }
    }

    //通知监听器
    private void notifyOnPreparedListener(long p){
        for (OnProgressListener listener:
             mOnPreparedListeners) {
            if (listener != null){
                listener.onProgress(p);
            }
        }
    }

    private long pos;
    private PLMediaPlayer.OnPreparedListener onPreparedListener = new PLMediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(PLMediaPlayer plMediaPlayer) {
//            setVideoRatio(plMediaPlayer); // 拉伸问题
            videoPlayer.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);//画面预览模式--适应屏幕
            if (timedTextView != null)
                timedTextView.showView();
            addSubtitle(plMediaPlayer);
            plMediaPlayer.start();
            plMediaPlayer.seekTo((int) pos);
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
    private void addSubtitle(PLMediaPlayer plMediaPlayer) {
        if (plMediaPlayer == null)
            return;
        if (DEBUG) Log.d(tag, "addSubtitle/file=" + file);
        if (file == null)
            return;
        File f = SYSJStorageUtil.createFilecachePath(file);
        if (f == null || !f.exists())
            return;
        String path = f.getAbsolutePath();
        if (DEBUG) Log.d(tag, "addSubtitle/path=" + path);
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

    private PLMediaPlayer.OnInfoListener onInfoListener = new PLMediaPlayer.OnInfoListener() {

        @Override
        public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
            if (DEBUG) Log.d(tag, "onInfo: what == " + what + " , extra == " + extra);
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    if (DEBUG) Log.d(tag, "onInfo: " + what + "----正在缓冲----");
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    if (DEBUG) Log.d(tag, "onInfo: " + what + "----第一帧视频已成功渲染----");
                    break;
            }
            return false;
        }
    };

    private PLMediaPlayer.OnErrorListener onErrorListener = new PLMediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {
            if (DEBUG) Log.d(tag, "onError: code === " + errorCode);
            if (danmukuPlayer != null)
                danmukuPlayer.hideDanmaku();
            try {
                progressHandler.removeCallbacks(progressRunnable);
              //  progressHandlerNew.removeCallbacks(progressRunnableNew);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (timedTextView != null)
                timedTextView.hideView();

            switch (errorCode){
//                case MEDIA_ERROR_UNKNOWN://未知错误
//                case ERROR_CODE_INVALID_URI://无效的 URL
//                case ERROR_CODE_IO_ERROR://未知错误
//                case ERROR_CODE_STREAM_DISCONNECTED://与服务器连接断开
//                case ERROR_CODE_EMPTY_PLAYLIST://空的播放列表
//                case ERROR_CODE_404_NOT_FOUND://播放资源不存在
//                case ERROR_CODE_CONNECTION_REFUSED://服务器拒绝连接
//                case ERROR_CODE_CONNECTION_TIMEOUT://连接超时
//                case ERROR_CODE_UNAUTHORIZED://未授权，播放一个禁播的流
//                case ERROR_CODE_PREPARE_TIMEOUT	://播放器准备超时
//                case -2003://硬解码失败
//                    videoPlayer.stopPlayback();
//                    switchPlay(STATE_ERROR);
//                    break;
                case ERROR_CODE_READ_FRAME_TIMEOUT://读取数据超时
                    Log.d(tag, "onError: 读取数据超时");
//                    videoPlayer.pause();
                    break;
                default:
                    videoPlayer.stopPlayback();
                    switchPlay(STATE_ERROR);
                    break;
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

    @SuppressWarnings("")
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

    public long getCurrentPosition(){
        return videoPlayer.getCurrentPosition();
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
           // progressHandlerNew.removeCallbacks(progressRunnableNew);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //进度条显示
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

    //新增进度监听  可控制细粒度
    private Handler progressHandlerNew = new Handler(Looper.getMainLooper());
    private Runnable progressRunnableNew = new Runnable() {

        @Override
        public void run() {
            notifyOnPreparedListener(videoPlayer.getCurrentPosition());
            progressHandlerNew.postDelayed(progressRunnableNew, mDelay);
        }
    };

    /**
     *视频审核中
     */
    private void initUnVerifyView(){
        mUnVerifyView = (ImageView) findViewById(R.id.iv_verify_view);
        if (videoImage != null){
            GlideHelper.displayImageEmpty(getContext(),videoImage.getFlag(),mUnVerifyView);
        }
        View view =  findViewById(R.id.rl_verify_view);
        view.setVisibility(View.VISIBLE);

        //拦截事件
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void switchPlay(int state) {
        this.state = state;
        if (DEBUG) Log.d(tag, "switchPlay/state=" + state);

        /**
         * 视频未审核
         */
        if (state == STATE_UNVETIFY){
            initUnVerifyView();
            titleBarView.showView();
            titleBarView.clearShaw();
            touchView.hideView();
            errorView.hideView();
            startView.hideView();
            completeView.hideView();
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
         * 准备播放
         */
        if (state == STATE_PREPARE) {
            if (DEBUG) LogHelper.d(tag, "========= STATE_PREPARE =========");
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
            if (DEBUG) LogHelper.d(tag, "========= STATE_COMPLETE =========");
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

            //上传到后台
            activity.commitPlayDuration();
            videoPlayer.resetPlayDuration();
            return;
        }

        /**
         * 网页播放
         */
        if (state == STATE_WEBPLAY && !StringUtil.isNull(yk_url) && URLUtil.isURL(youku_url)) {
            if (DEBUG) LogHelper.d(tag, "========= STATE_WEBPLAY =========");
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
        if (state == STATE_START && !StringUtil.isNull(mVideoUrl) ) {
            if (DEBUG) LogHelper.d(tag, "========= STATE_START =========");
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
        if (state == STATE_VIDEOPLAY && mVideoUrl != null && URLUtil.isURL(mVideoUrl)) {
            if (DEBUG) LogHelper.d(tag, "========= STATE_VIDEOPLAY =========");
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

            startPlayer(mVideoUrl, 0);
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.VIDEOPLAY, "视频播放次数");
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.MACROSCOPIC_DATA, "视频总播放次数");
            return;
        }

        /**
         * 乐播投屏
         */
        if (state == STATE_TV) {
            if (DEBUG) LogHelper.d(tag, "========= STATE_TV =========");
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

            volumeBeforeTV = activity.getCurrentVolume();
            if (DEBUG) Log.d(tag, "手机投屏前音量: " + volumeBeforeTV);
            activity.setCurrentVolume(0);
            showTVDanmu(isDanmukuShow);

            leBoView.showView();
            if (videoImage != null && videoImage.getTitle() != null)
                leBoView.setTitleText(videoImage.getTitle());

            videoPlayer.setVisibility(VISIBLE);
            webPlayer.setVisibility(GONE);
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.VIDEOPLAY, "投屏-进入投屏中状态");
            return;
        }

        if (state == STATE_ERROR) {
            if (DEBUG) LogHelper.d(tag, "========= STATE_ERROR =========");
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
        if (DEBUG) {
            Log.d(tag, "url=" + url);
            Log.d(tag, "startPlayer uri = " + Uri.parse(url));
            Log.d(tag, "pos=" + pos);
        }

        if (window != null)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        prepareView.hideView();

        if (videoPlayer != null) {
            this.pos = pos;
            videoPlayer.setVideoPath(url);
            videoPlayer.setOnPreparedListener(onPreparedListener);
            videoPlayer.setOnCompletionListener(onCompletionListener);
            videoPlayer.startVideo();
            //开始播放 重置礼物时间轴
            EventBus.getDefault().post(new ResetTimeLineEvent());
            activity.resetTimeLineData();
        }

        if (pos == 0) {

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
        if (DEBUG) Log.d(tag, "resume: state == " + state);
        if (state != STATE_TV) {
            if (lastPos != 0 && URLUtil.isURL(mVideoUrl)) {
                startPlayer(mVideoUrl, lastPos);
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

        if (progressHandlerNew != null){
            progressHandlerNew.removeCallbacksAndMessages(null);
            progressHandlerNew.post(progressRunnableNew);
        }
    }

    public void pause() {
        if (DEBUG) Log.d(tag, "pause: state == " + state);
        if (state != STATE_TV) {
            if (videoPlayer != null) {
                lastPos = videoPlayer.getVideoPosition();
                VideoPlayActivity.playPos = lastPos;
                VideoPlayActivity.playUrl = mVideoUrl;
                if (DEBUG) Log.e(tag, "lastPos=" + lastPos);
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

        if (progressHandlerNew != null){
            progressHandlerNew.removeCallbacksAndMessages(null);
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
            } catch (IllegalAccessException | IllegalArgumentException |
                    NoSuchMethodException | InvocationTargetException e) {
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
        if (progressHandlerNew != null){
            progressHandlerNew.removeCallbacksAndMessages(null);
        }
//        try {
//            linkControl.colseHpplayLink();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    public static final float RATIO_MAX = 16F / 9F;

//    private void setVideoRatio(PLMediaPlayer plMediaPlayer) {
//        if (plMediaPlayer == null)
//            return;
//        float width = plMediaPlayer.getVideoWidth();
//        float height = plMediaPlayer.getVideoHeight();
//        float ratio = width / height;
//        if (layout != null)
//            layout.setAspectRatio(ratio);
//    }

    @Override
    public void minView() {
        if (DEBUG) Log.i(tag, "state=" + state);
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
        if (DEBUG) Log.i(tag, "state=" + state);
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

    @Override
    public void showCover() {

    }

    @Override
    public void hideCover() {

    }

    @Override
    public void showPlay() {

    }

    @Override
    public void hidePlay() {

    }
}
