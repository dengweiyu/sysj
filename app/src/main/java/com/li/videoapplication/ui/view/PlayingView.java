package com.li.videoapplication.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.tools.SubtitleHelper;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;
import com.li.videoapplication.ui.fragment.AudioFragment;
import com.li.videoapplication.views.ControllerView2;

import java.io.File;

/**
 * 视图：完成播放
 */
public class PlayingView extends RelativeLayout {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private View view;
    private FrameLayout container;
    private StartView startView;
    public SRTEditView srtEditView;
    private ControllerView2 controllerView;
    public TimedTextView timedTextView;

    // 视频播放器
    public VideoPlayer videoPlayer;
    private AspectRatioLayout layout;

    public long lastPosition = 0l;
    private VideoCaptureEntity entity;
    private File srtFile;

    private LayoutInflater inflater;
    private Context context;
    private VideoEditorActivity2 activity;
    private AudioFragment audioFragment;
    private Window window;
    private WindowManager windowManager;
    private float srceenWidth, srceenHeight;
    private LinearLayout.LayoutParams params;

    public PlayingView(Context context) {
        this(context, null);
    }

    public PlayingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(VideoEditorActivity2 activity, VideoCaptureEntity entity) {
        this.activity = activity;
        this.entity = entity;
        window = activity.getWindow();
        windowManager = window.getWindowManager();
        inflater = LayoutInflater.from(context);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        srceenWidth = windowManager.getDefaultDisplay().getWidth();
        srceenHeight = windowManager.getDefaultDisplay().getHeight();

        initContentView();
    }

    public void init(AudioFragment audioFragment) {
        this.audioFragment = audioFragment;
    }

    public void updateDuration(long duration) {
        if (controllerView != null)
            controllerView.updateDuration(duration);
    }

    private Bitmap bitmap;

    public void loadCover(Bitmap bitmap) {
        if (bitmap != null) {
            this.bitmap = bitmap;
            if (startView != null) {
                startView.loadCover(bitmap);
                startView.showCover();
            }
        }
    }

    public void playVideo() {
        if (!videoPlayer.isPlayingVideo()) {
            controllerView.performClickPlay();
        }
    }

    public void pauseVideo() {
        if (videoPlayer.isPlayingVideo()) {
            controllerView.performClickPlay();
        }
        pauseSubtitle();
    }

    public void setDrag(boolean drag) {
        if (controllerView != null) {
            controllerView.setDrag(drag);
        }
    }

    private void initContentView() {

        view = inflater.inflate(R.layout.view_playing, this);
        container = (FrameLayout) view.findViewById(R.id.container);
        startView = (StartView) view.findViewById(R.id.playing_start);
        srtEditView = (SRTEditView) view.findViewById(R.id.playing_srtedit);
        controllerView = (ControllerView2) view.findViewById(R.id.playing_controller);
        timedTextView = (TimedTextView) view.findViewById(R.id.playing_timedtext);

        params = new LinearLayout.LayoutParams((int) srceenWidth, (int) (srceenWidth / RATIO_MAX));
        logLayoutParams(params);
        container.setLayoutParams(params);

        layout = (AspectRatioLayout) view.findViewById(R.id.playing_container);
        layout.setAspectRatio(RATIO_MAX);
        logLayoutParams(layout.getLayoutParams());

        initPlayer();

        timedTextView.init(activity);
        controllerView.init(activity, this, videoPlayer, entity);

        srtEditView.init(activity);

        startView.setPlayListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 播放视频
                updatePlayer(1);
            }
        });

        // 封面
        updatePlayer(5);
        // 载入视频
        updatePlayer(3);
    }

    private void logLayoutParams(ViewGroup.LayoutParams params) {
        if (params != null) {
            Log.d(tag, "--------------------------logLayoutParams-----------------------------");
            Log.d(tag, "logLayoutParams: width=" + params.width);
            Log.d(tag, "logLayoutParams: height=" + params.height);
        }
    }

    public boolean isVideoPlaying() {
        if (videoPlayer != null && videoPlayer.isPlayingVideo())
            return true;
        else
            return false;
    }

    private void initPlayer() {

        videoPlayer = (VideoPlayer) findViewById(R.id.videoplayer);
        videoPlayer.setOnErrorListener(onErrorListener);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoPlayer.setOnInfoListener(onInfoListener);
        }
    }

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.d(tag, "onCompletion: // --------------------------------------------------------");
            if (activity != null &&
                    activity.isAudioRecording == true &&
                    audioFragment != null) {
                audioFragment.performClickRecord();
            }
            lastPosition = 0l;
            if (controllerView != null) {
                controllerView.updatePlay(false);
                controllerView.updateProgress();
                controllerView.setDrag(false);
                controllerView.updateDuration();
            }
            removeProgress();
            if (timedTextView != null)
                timedTextView.hideView();
            isPrepared = false;
            // 载入视频
            updatePlayer(3);
            pauseSubtitle();
        }
    };

    private long startPostion;
    private boolean isPrepared = false;
    private boolean isLoading = false;
    private MediaPlayer mediaPlayer;

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            Log.d(tag, "onPrepared: // --------------------------------------------------------");
            PlayingView.this.mediaPlayer = mediaPlayer;
            isPrepared = true;
            onErrorFlag = true;
            setVideoRatio(mediaPlayer);
            addSubtitle((File) srtFile);
            if (timedTextView != null)
                timedTextView.showView();
            if (isLoading == false) {
                mediaPlayer.start();
                mediaPlayer.seekTo((int) startPostion);
            }
            updateProgress();
            if (controllerView != null) {
                controllerView.updatePlay(false);
                controllerView.updateProgress();
                controllerView.updateDuration();

                if (activity != null &&
                        !activity.isAddingSubtitle &&
                        !activity.isAudioRecording) {
                    controllerView.setDrag(true);
                }
            }
        }
    };

    /**
     * 加载字幕
     */
    public void addSubtitle(File file) {
        Log.d(tag, "addSubtitle: ");
        srtFile = file;
        Log.d(tag, "addSubtitle: file=" + file);
        if (mediaPlayer == null)
            return;
        if (file == null ||
                !file.exists() ||
                !file.isFile())
            return;
        String path = file.getAbsolutePath();
        Log.d(tag, "addSubtitle/path=" + path);

        // addSubtitle(file, mediaPlayer);
        playSubtitle(true, mediaPlayer, path);
    }

    private void addSubtitle(File file, MediaPlayer mediaPlayer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try {
                mediaPlayer.addTimedTextSource(file.getPath(), MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
                MediaPlayer.TrackInfo[] trackInfos = mediaPlayer.getTrackInfo();
                int index = -1;
                for (int i = 0; i < trackInfos.length; i++) {
                    if (trackInfos[i].getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT) {
                        index = i;
                    }
                }
                if (index >= 0) {
                    mediaPlayer.selectTrack(index);
                    Log.d(tag, "addSubtitle/index=" + index);
                }
                mediaPlayer.setOnTimedTextListener(timedTextView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private SubtitleHelper subtitleHelper;

    public void showSubtitle(long position) {
        if (subtitleHelper != null) {
            subtitleHelper.showSubtitle(position);
        }
    }

    public void resumeSubtitle() {
        if (subtitleHelper != null) {
            subtitleHelper.setPlaying(true);
            subtitleHelper.playSubtitle();
        }
    }

    public void pauseSubtitle() {
        if (subtitleHelper != null) {
            subtitleHelper.setPlaying(false);
        }
    }

    private void playSubtitle(boolean playing, MediaPlayer mediaPlayer, String path) {
        if (subtitleHelper == null) {
            subtitleHelper = new SubtitleHelper(path, videoPlayer, timedTextView.getContent());
        }
        subtitleHelper.setPlaying(playing);
        subtitleHelper.playSubtitle();
    }

    private MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.d(tag, "onInfo: // --------------------------------------------------------");
            // mp.getDataSource()
            // mp.getCurrentPosition()
            return false;
        }
    };

    private boolean onErrorFlag = false;

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.d(tag, "onError: // --------------------------------------------------------");
            // mp.getDataSource()
            // mp.getCurrentPosition()
            if (onErrorFlag == true) {
                lastPosition = 0l;
                try {
                    h.removeCallbacks(r);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (timedTextView != null)
                    timedTextView.hideView();
                if (what == 100) {
                    videoPlayer.stopPlaybackVideo();
                } else if (what == 1) {
                    videoPlayer.stopPlaybackVideo();
                } else if(what == 800) {
                    videoPlayer.stopPlaybackVideo();
                } else if (what == 701) {
                    videoPlayer.stopPlaybackVideo();
                } else if(what == 700) {
                    videoPlayer.stopPlaybackVideo();
                } else if (what == -38) {
                    videoPlayer.stopPlaybackVideo();
                }
                // 停止
                updatePlayer(4);
                // 封面
                updatePlayer(5);
                isPrepared = false;
                // 载入视频
                updatePlayer(3);
                onErrorFlag = false;
                pauseSubtitle();
                Log.d(tag, "onError: true");
            }
            return true;
        }
    };

    private void updateProgress() {
        try {
            h.post(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeProgress() {
        try {
            h.removeCallbacks(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler h = new Handler();
    private Runnable r = new Runnable() {

        @Override
        public void run() {
            if (controllerView != null && videoPlayer != null) {
                controllerView.updatePlay(videoPlayer.isPlayingVideo());
                controllerView.updateProgress();
                controllerView.updateDuration();
            }
            if (activity != null &&
                    activity.isAudioRecording == true &&
                    audioFragment != null) {
                long position = videoPlayer.getCurrentPositionVideo();
                long duration = videoPlayer.getDurationVideo();
                int progress = (int) (position * 100 / duration);
                audioFragment.updateProgress(progress);
            }
            if (activity != null) {
                long position = videoPlayer.getCurrentPositionVideo();
                if (position > activity.getRightTime()) {
                    updatePlayer(2);
                    seekToPlayer(activity.getLeftTime());
                }
            }
            h.postDelayed(r, 1000);
        }
    };

    /**
     * 播放视频
     * @param state
     *              1：播放
     *              2：暂停
     *              3：继续
     *              4：停止
     *              5：封面
     */
    public void updatePlayer(int state) {

        if (state == 1 && !StringUtil.isNull(entity.getVideo_path())) {// 开始, 继续播放
            startView.hideCover();
            startView.hidePlay();
            if (isPrepared)
                startPlayer();
            else
                startPlayer(entity.getVideo_path(), 0);
        } else if (state == 2) {// 暂停播放
            startView.hideCover();
            startView.showPlay();
            pausePlayer();
        }  else if (state == 3) {// 载入视频
            startView.hideCover();
            startView.showPlay();
            startPlayer(entity.getVideo_path());
        } else if (state == 4) {// 停止播放
            startView.hideCover();
            startView.showPlay();
            stopPlayer();
        } else if (state == 5) {// 封面
            startView.showCover();
            startView.showPlay();
            startView.loadCover(bitmap);
        }
    }

    /**
     * 开始播放（已经载入）
     */
    public void startPlayer() {
        Log.d(tag, "startPlayer: ");
        if (videoPlayer != null && isPrepared) {
            videoPlayer.start();
        }
    }

    /**
     * 开始播放（载入视频，播放到指定位置）
     */
    private void startPlayer(String path, long poaition) {
        Log.d(tag, "startPlayer: ");
        Log.d(tag, "url=" + path);
        Log.d(tag, "startPostion=" + poaition);

        if (window != null)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (videoPlayer != null) {
            isLoading = false;
            this.startPostion = poaition;
            videoPlayer.setVideoURI(Uri.parse(path));
            videoPlayer.setOnPreparedListener(onPreparedListener);
            videoPlayer.setOnCompletionListener(onCompletionListener);
        }
    }

    /**
     * 开始播放（载入视频）
     */
    private void startPlayer(String path) {
        Log.d(tag, "startPlayer: ");
        Log.d(tag, "url=" + path);
        if (videoPlayer != null) {
            isLoading = true;
            videoPlayer.setVideoURI(Uri.parse(path));
            videoPlayer.setOnPreparedListener(onPreparedListener);
            videoPlayer.setOnCompletionListener(onCompletionListener);
        }
    }

    /**
     * 暂停
     */
    private void pausePlayer() {
        if (videoPlayer != null) {
            Log.d(tag, "pausePlayer: ");
            lastPosition = videoPlayer.getCurrentPositionVideo();
            videoPlayer.pauseVideo();
        }
    }

    /**
     * 继续
     */
    private void  resumePlayer() {
        if (videoPlayer != null) {
            Log.d(tag, "resumePlayer: ");
            videoPlayer.resumeVideo();
        }
    }

    /**
     * 停止
     */
    private void  stopPlayer() {
        if (videoPlayer != null) {
            Log.d(tag, "stopPlayer: ");
            lastPosition = 0l;
            videoPlayer.pauseVideo();
            videoPlayer.stopPlaybackVideo();
        }
    }

    /**
     * 快进快退
     */
    public void seekToPlayer(long position) {
        if (videoPlayer != null) {
            Log.d(tag, "seekToPlayer: ");
            Log.d(tag, "seekToPlayer: position=" + position);
            videoPlayer.seekToVideo(position);
            lastPosition = position;
            if (startView != null) {
                startView.hidePlay();
                startView.hideCover();
            }
        }
    }

    public void resumeRescouce() {
        if (lastPosition != 0 && !StringUtil.isNull(entity.getVideo_path())) {
            Log.d(tag, "resumeRescouce: ");
            videoPlayer.resumeVideo();
        }
        resumeSubtitle();
    }

    public void pauseRescouce() {
        if (videoPlayer != null) {
            Log.d(tag, "pauseRescouce: ");
            lastPosition = videoPlayer.getCurrentPositionVideo();
            videoPlayer.pauseVideo();
        }
        pauseSubtitle();
    }

    public void destroyRescouce() {
        if (videoPlayer != null) {
            Log.d(tag, "destroyRescouce: ");
            videoPlayer.pauseVideo();
            videoPlayer.stopPlaybackVideo();
        }
        if (h != null)
            h.removeCallbacksAndMessages(null);
    }

    public static final float RATIO_MAX = 16F / 9F;

    /**
     * 设置播放器比例
     */
    private void setVideoRatio(MediaPlayer mediaPlayer) {
        Log.d(tag, "setVideoRatio: // --------------------------------------------------------");
        if (mediaPlayer == null)
            return;
        float width = mediaPlayer.getVideoWidth();
        float height = mediaPlayer.getVideoHeight();
        float ratio = width / height;
        Log.d(tag, "setVideoRatio: ratio=" + ratio);
        if (layout != null)
            layout.setAspectRatio(ratio);
    }
}
