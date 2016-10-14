package com.li.videoapplication.ui.view;

/**
 * 视图：播放器
 */
public interface IVideoPlayer {

    void startVideo();

    void pauseVideo();

    boolean isPlayingVideo();

    long getVideoDuration();

    long getVideoPosition();

    void seekToVideo(long ms);
}
