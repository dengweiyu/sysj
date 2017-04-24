package com.li.videoapplication.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.li.videoapplication.R;
import com.li.videoapplication.data.database.VideoCaptureEntity;
import com.li.videoapplication.tools.TimeHelper;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.activity.VideoActivity;
import com.li.videoapplication.ui.activity.VideoEditorActivity2;
import com.li.videoapplication.ui.view.PlayingView;
import com.li.videoapplication.ui.view.VideoPlayer;

/**
 * 视图：播放控制
 */
public class ControllerView2 extends RelativeLayout implements View.OnClickListener {

    public final String action = this.getClass().getName();
    public final String tag = this.getClass().getSimpleName();

    private LayoutInflater inflater;

    private ImageView play;
    private ImageView fullscreen;
    private TextView position, duration;
    private TouchSeekBar progress;

    public ControllerView2(Context context) {
        this(context, null);
    }

    public ControllerView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = LayoutInflater.from(getContext());
    }

    private VideoEditorActivity2 activity;
    private PlayingView playingView;
    private VideoPlayer videoPlayer;
    private VideoCaptureEntity record;

    public void init(VideoEditorActivity2 activity, PlayingView playingView, VideoPlayer videoPlayer, VideoCaptureEntity record) {
        this.activity = activity;
        this.videoPlayer = videoPlayer;
        this.playingView = playingView;
        this.record = record;

        initContentView();
    }

    private void initContentView() {

        inflater.inflate(R.layout.view_controller, this);

        play = (ImageView) findViewById(R.id.controller_play);
        fullscreen = (ImageView) findViewById(R.id.controller_fullscreen);

        position = (TextView) findViewById(R.id.controller_position);
        duration = (TextView) findViewById(R.id.controller_duration);

        progress = (TouchSeekBar) findViewById(R.id.controller_progress);
        progress.setDrag(false);

        play.setOnClickListener(this);
        fullscreen.setOnClickListener(this);

        updatePlay(false);

        progress.setMax(100);
        progress.setProgress(0);
        progress.setSecondaryProgress(0);
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
                Log.i(tag, "changed/progress=" + progress);
                updatePoaitionDurationAndSubtitle(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (playingView != null && videoPlayer != null) {
                    long duration = videoPlayer.getVideoDuration();
                    long position = (long) ((((float) progress) / 100f) * ((float) duration));
                    playingView.seekToPlayer(position);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == play) {
            if (activity != null && activity.isAudioRecording == true) {
                ToastHelper.s(R.string.videoeditor_tip_audiorecording);
                return;
            }
            if (activity != null && activity.isAddingSubtitle == true) {
                return;
            }
            if (playingView != null) {
                updatePlay(playingView.isVideoPlaying());
                if (playingView.isVideoPlaying()) {
                    playingView.updatePlayer(2);
                } else {
                    playingView.updatePlayer(1);
                }
            }
        } else if (v == fullscreen) {
            if (playingView != null) {
                playingView.pauseVideo();
                updatePlay(playingView.isVideoPlaying());
                playingView.seekToPlayer(0);
                playingView.updatePlayer(2);
            }
            VideoActivity.startVideoActivity(activity, record.getVideo_path(), record.getVideo_name());
        }
    }

    public void performClickPlay() {
        if (play != null) {
            play.performClick();
        }
    }

    /**
     * 更新播放时间（根据视频）
     */
    public void updateDuration(final long duration) {
        if (this.position != null && this.duration != null) {
            Log.d(tag, "----------------------------------------------------------------");
            try {
                this.position.setText("00:00");
                long allTime = Long.valueOf(duration);
                String format = TimeHelper.getVideoPlayTime(allTime / 1000);
                this.duration.setText(format);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新播放时间（根据播放器）
     */
    public void updateDuration() {
        if (videoPlayer != null && this.position != null && this.duration != null) {
            Log.d(tag, "----------------------------------------------------------------");
            long duration = videoPlayer.getVideoDuration();
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
    private void updatePoaitionDurationAndSubtitle(int progress) {
        if (progress < 0)
            progress = 0;
        if (progress > 100)
            progress = 100;
        if (videoPlayer != null && this.position != null && this.duration != null) {
            Log.d(tag, "----------------------------------------------------------------");
            long duration = videoPlayer.getVideoDuration();
            long position = progress * duration / 100;
            String playTime = TimeHelper.getVideoPlayTime(position / 1000);
            String totleTime = TimeHelper.getVideoPlayTime(duration / 1000);
            this.position.setText(playTime);
            this.duration.setText(totleTime);
            Log.i(tag, "playTime=" + playTime);
            Log.i(tag, "totleTime=" + totleTime);

            if (playingView != null)
                playingView.showSubtitle(position);
        }
    }

    /**
     * 更新播放进度（根据播放器）
     */
    public void updateProgress() {
        if (videoPlayer != null) {
            Log.d(tag, "----------------------------------------------------------------");
            long position = videoPlayer.getCurrentPosition();
            long duration = videoPlayer.getDuration();
            if (duration != 0) {
                // 缓冲进度
                int buffer = videoPlayer.getBufferPercentage();
                int progress = (int) (position * 100 / duration);
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
    }

    /**
     * 更新进度条
     */
    public void updateProgress(int progress) {
        Log.d(tag, "----------------------------------------------------------------");
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
        Log.d(tag, "----------------------------------------------------------------");
        if (this.progress != null) {
            Log.d(tag, "setDrag: drag=" + drag);
            this.progress.setDrag(drag);
            this.progress.setEnabled(drag);
            this.progress.setClickable(drag);
        }
    }

    /**
     * 更新播放按键
     */
    public void updatePlay(boolean isPlaying) {
        Log.d(tag, "----------------------------------------------------------------");
        if (play != null)
            if (isPlaying)
                play.setImageResource(R.drawable.videoeditor_pause);
            else
                play.setImageResource(R.drawable.videoeditor_play);
    }
}
