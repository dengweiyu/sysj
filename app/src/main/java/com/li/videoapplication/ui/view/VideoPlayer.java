package com.li.videoapplication.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.widget.PLVideoView;

/**
 * 视图：播放器
 */
public class VideoPlayer extends PLVideoView implements IVideoPlayer {

    public final static int MEDIA_CODEC_SW_DECODE = 0;
    public final static int MEDIA_CODEC_HW_DECODE = 1;
    public final static int MEDIA_CODEC_AUTO = 2;
    private long mStartTime = 0;
    private long mPlayDuration = 0;
    private AVOptions mAVOptions;
    public VideoPlayer(Context context) {
        super(context);
        initListener(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initListener(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initListener(context);

    }

    @Override
    public void initListener(Context context) {
        mAVOptions = new AVOptions();
       /*
        // 解码方式:
        // codec＝AVOptions.MEDIA_CODEC_HW_DECODE，硬解
        // codec=AVOptions.MEDIA_CODEC_SW_DECODE, 软解
        // codec=AVOptions.MEDIA_CODEC_AUTO, 硬解优先，失败后自动切换到软解
        int codec = AVOptions.MEDIA_CODEC_AUTO;
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        //默认的缓存大小，单位是 ms
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 1000);
        //最大的缓存大小，单位是 ms
        options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 2000);
        setAVOptions(options);*/
    }

    @Override
    public void startVideo() {
        start();
        //
        mStartTime = System.currentTimeMillis();
    }

    @Override
    public void pauseVideo() {
        pause();
        //
        mStartTime = 0;
        mPlayDuration = System.currentTimeMillis() - mStartTime;
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

    @Override
    public void resumeVideo() {
        start();
    }


    public AVOptions getAVOptions() {
        return mAVOptions;
    }

    @Override
    public void setAVOptions(AVOptions AVOptions) {
        super.setAVOptions(AVOptions);
        mAVOptions = AVOptions;

    }

    public void resetPlayDuration(){
        mStartTime = 0;
        mPlayDuration = 0;
    }

    public long getPlayDuration(){
        return mPlayDuration;
    }
}
