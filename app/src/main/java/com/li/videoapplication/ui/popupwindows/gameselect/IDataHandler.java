package com.li.videoapplication.ui.popupwindows.gameselect;

/**
 * Created by linhui on 2017/9/18.
 *
 *
 * 定义了基本的页面处理方法
 *
 */
interface IDataHandler {
    void init();
    void loadData();
    void onDestroy();
    int getContentView();
}
