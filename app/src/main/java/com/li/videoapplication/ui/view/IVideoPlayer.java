package com.li.videoapplication.ui.view;

import android.content.Context;

/**
 * 视图：播放器
 */
public interface IVideoPlayer {

    void initListener(Context context);

    void startVideo();

    void pauseVideo();

    boolean isPlayingVideo();

    long getVideoDuration();

    long getVideoPosition();

    void seekToVideo(long ms);

    void resumeVideo();
}
