package com.li.videoapplication.ui.view;

import java.io.InputStream;

/**
 * 视图：弹幕
 */
public interface IDanmukuPlayer {

    void showView();

    void hideView();

    void hideDanmaku();

    boolean isShownDanmaku();

    void showDanmaku();

    void loadDanmaku(InputStream is);

    void addDanmaku(String text);

    void seekToDanmaku(long ms);

    void pauseDanmaku();

    void resumeDanmaku();

    void destroyDanmaku();
}
