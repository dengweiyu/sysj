package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.VideoPlayActivity;
import com.li.videoapplication.views.TouchSeekBar;

/**
 * 视图：播放控制
 */
public class ControllerView extends RelativeLayout implements View.OnClickListener, IVideoPlay {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;

    private View view;
    private LinearLayout root;
    private ImageView play, zoom;
    private ImageView danmuku;
    private TextView position, duration;
    private TouchSeekBar progress;// 0 - 100

    public ControllerView(Context context) {
        this(context, null);
    }

    public ControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = LayoutInflater.from(getContext());
        activity = (VideoPlayActivity) AppManager.getInstance().getActivity(VideoPlayActivity.class);
    }

    private VideoPlayActivity activity;
    private VideoPlayView videoPlayView;
    private VideoPlayer videoPlayer;
    private DanmukuPlayer danmukuPlayer;

    public void init(VideoPlayView videoPlayView, VideoPlayer videoPlayer, DanmukuPlayer danmukuPlayer) {
        this.videoPlayer = videoPlayer;
        this.videoPlayView = videoPlayView;
        this.danmukuPlayer = danmukuPlayer;

        initContentView();
        minView();
        hideView();
    }

    private void initContentView() {

        view = inflater.inflate(R.layout.view_videoplay_controller, this);
        root = (LinearLayout) view.findViewById(R.id.root);

        play = (ImageView) view.findViewById(R.id.controller_play);
        zoom = (ImageView) view.findViewById(R.id.controller_zoom);

        danmuku = (ImageView) view.findViewById(R.id.controller_damuku);

        position = (TextView) view.findViewById(R.id.controller_position);
        duration = (TextView) view.findViewById(R.id.controller_duration);

        progress = (TouchSeekBar) view.findViewById(R.id.controller_progress);
        progress.setDrag(false);

        play.setOnClickListener(this);
        zoom.setOnClickListener(this);

        setTime();
        setPlay(false);
        setDanmuku(false);

        progress.setMax(100);
        progress.setProgress(0);
        progress.setSecondaryProgress(0);
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
                Log.i(tag, "changed/progress=" + progress);
                setTime(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //计时器停止
                videoPlayView.onStopTime();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setTime();
                if (videoPlayView != null){
                    videoPlayView.seekToVideo(this.progress);
                    //计时器继续
                    videoPlayView.onStartTime();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == play) {
            videoPlayView.toogleVideo();
        } else if (v == zoom) {
            if (activity != null && videoPlayer != null) {
                activity.toogleScreen();
                if (activity.isLandscape()) {
                    setZoom(true);
                } else {
                    setZoom(false);
                }
            }
            //
            UmengAnalyticsHelper.onEvent(getContext(), UmengAnalyticsHelper.MACROSCOPIC_DATA, "竖屏切换到横屏点击");

        }
    }

    public void setDanmukListener(View.OnClickListener listener) {
        if (danmuku != null)
            danmuku.setOnClickListener(listener);
    }

    public void performDanmukuClick() {
        activity.post(new Runnable() {
            @Override
            public void run() {
                if (danmuku != null && !videoPlayView.isDanmukuShow)
                    danmuku.performClick();
            }
        });
    }

    public void setDanmuku(boolean isShow) {
        if (danmuku != null) {
            if (isShow) {
                danmuku.setImageResource(R.drawable.videoplay_small_danmuku_yellow);
            } else {
                danmuku.setImageResource(R.drawable.videoplay_small_danmuku_gray);
            }
        }
    }

    public void setDanmukuEnabledDelayed() {
        if (danmuku != null && activity != null) {
            danmuku.setEnabled(false);
            danmuku.setClickable(false);
            activity.postDelayed(new Runnable() {
                @Override
                public void run() {

                    danmuku.setEnabled(true);
                    danmuku.setClickable(true);
                }
            }, 800);
        }
    }

    /**
     * 更新播放时间（根据播放器）
     */
    public void setTime() {
        if (videoPlayer != null && this.position != null && this.duration != null) {
            long duration = videoPlayer.getDuration();
            long position = videoPlayer.getCurrentPosition();
            String playTime = TimeHelper.getVideoPlayTime(position / 1000);
            String totleTime = TimeHelper.getVideoPlayTime(duration / 1000);
            this.position.setText(playTime);
            this.duration.setText(totleTime);
            Log.i(tag, "playTime=" + playTime);
            Log.i(tag, "totleTime=" + totleTime);
        }
    }

    /**
     * 更新播放时间
     */
    public void setTime(int progress) {
        if (progress < 0)
            progress = 0;
        if (progress > 100)
            progress = 100;
        if (videoPlayer != null && this.position != null && this.duration != null) {
            long duration = videoPlayer.getDuration();
            long position = progress * duration / 100;
            String playTime = TimeHelper.getVideoPlayTime(position / 1000);
            String totleTime = TimeHelper.getVideoPlayTime(duration / 1000);
            this.position.setText(playTime);
            this.duration.setText(totleTime);
            Log.i(tag, "playTime=" + playTime);
            Log.i(tag, "totleTime=" + totleTime);
        }
    }

    /**
     * 更新播放进度（根据播放器）
     */
    public void setProgress() {
        if (videoPlayer != null) {
            long position = videoPlayer.getCurrentPosition();
            long duration = videoPlayer.getDuration();
            // 缓冲进度
            int buffer = videoPlayer.getBufferPercentage();
            int progress = 0;
            if (duration != 0 && position != 0)
                progress = (int) (position * 100 / duration);


            Log.i(tag, "position=" + position);
            Log.i(tag, "duration=" + duration);
            Log.i(tag, "buffer=" + buffer);
            Log.i(tag, "progress=" + progress);
            if (this.progress != null) {
                this.progress.setMax(100);
                this.progress.setProgress(progress);
                this.progress.setSecondaryProgress(buffer);
            }
        }
    }

    /**
     * 更新进度条
     */
    public void setProgress(int progress) {
        if (progress < 0)
            progress = 0;
        if (progress > 100)
            progress = 100;
        if (this.progress != null)
            this.progress.setProgress(progress);
    }

    /**
     * 拖动条是否能拖动
     */
    public void setDrag(boolean drag) {
        if (this.progress != null) {
            this.progress.setDrag(drag);
            this.progress.setEnabled(drag);
            this.progress.setClickable(drag);
        }
    }

    /**
     * 更新播放按键
     */
    public void setPlay(boolean isPlaying) {
        if (play != null)
            if (isPlaying)
                play.setImageResource(R.drawable.videoplay_pause_208);
            else
                play.setImageResource(R.drawable.videoplay_play_208);
    }

    /**
     * 更新全屏按键
     */
    public void setZoom(boolean isFull) {
        if (zoom != null)
            if (isFull)
                zoom.setImageResource(R.drawable.videoplay_zoom_large);
//               zoom.setImageResource(R.drawable.videoplay_zoom_small);
            else
                zoom.setImageResource(R.drawable.videoplay_zoom_large);
    }

    @Override
    public void showView() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hideView() {
        setVisibility(GONE);
    }

    @Override
    public void minView() {
        danmuku.setVisibility(View.VISIBLE);
    }

    @Override
    public void maxView() {
        danmuku.setVisibility(View.GONE);
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
