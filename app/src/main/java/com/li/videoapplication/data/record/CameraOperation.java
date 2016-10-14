package com.li.videoapplication.data.record;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * 相机操作接口，用以统一CameraContainer和CameraView的功能
 */
public interface CameraOperation {

    /**
     * 开始录像
     *
     * @return 是否成功开始录像
     */
    boolean startRecord();

    /**
     * 停止录像
     *
     * @return 录像缩略图
     */
    Bitmap stopRecord();

    /**
     * 切换前置和后置相机
     */
    void switchCamera();

    /**
     * 获取当前闪光灯模式
     */
    CameraView.FlashMode getFlashMode();

    /**
     * 设置闪光灯模式
     */
    void setFlashMode(CameraView.FlashMode flashMode);

    /**
     * 拍照
     *
     */
    void takePicture();

    /**
     * 相机最大缩放级别
     */
    int getMaxZoom();

    /**
     * 获取当前缩放级别
     */
    int getZoom();

    /**
     * 设置当前缩放级别
     */
    void setZoom(int zoom);

    /**
     * 获得当前视频质量
     */
    CameraView.RecorderQuality getQuality();

    /**
     * 设置当前视频质量
     */
    boolean setQuality(CameraView.RecorderQuality recorderQuality);

    /**
     * 自动对焦
     */
    void autoFocus();

    /**
     * 手动聚焦
     */
    void focus(Point point);
}
