package com.ifeimo.screenrecordlib.listener;

/**
 * 服务：录屏监听接口
 */
public interface RecordingListener {

    /**
     * 录屏开始
     */
    void onRecordingStarted();

    /**
     * 录屏开始2
     */
    void onRecordingStarted2();

    /**
     * 录屏暂停
     */
    void onRecordingPaused();

    /**
     * 录屏继续
     */
    void onRecordingResumed();

    /**
     * 录屏停止
     */
    void onRecordingStoped();

    /**
     * 录屏完成
     */
    void onRecordingCompleted();

    /**
     * 录屏完成2
     */
    void onCapturingCompleted();

    /**
     * 录屏计时
     */
    void onRecordingTiming();
}
