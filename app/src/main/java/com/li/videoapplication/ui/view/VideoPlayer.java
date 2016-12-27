package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.pili.pldroid.player.widget.PLVideoView;

/**
 * 视图：播放器
 */
public class VideoPlayer extends PLVideoView implements IVideoPlayer {

    public VideoPlayer(Context context) {
        super(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void startVideo() {
        start();
    }

    @Override
    public void pauseVideo() {
        pause();
    }

    @Override
    public boolean isPlayingVideo() {
        return isPlaying();
    }

    @Override
    public long getVideoDuration() {
        return getDuration();
    }

    @Override
    public long getVideoPosition() {
        return getCurrentPosition();
    }

    @Override
    public void seekToVideo(long ms) {
        seekTo((int) ms);
    }
}
