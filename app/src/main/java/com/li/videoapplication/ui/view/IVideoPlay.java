package com.li.videoapplication.ui.view;

public interface IVideoPlay {

    /**
     * 显示
     */
    void showView();

    /**
     * 隐藏
     */
    void hideView();

    /**
     * 竖屏
     */
    void minView();

    /**
     * 横屏
     */
    void maxView();

    /**
     *显示封面
     */
    void showCover();

    /**
     * 隐藏封面
     */
    void hideCover();

    /**
     * 显示播放
     */
    void showPlay();

    /**
     * 隐藏播放
     */
    void hidePlay();
}
