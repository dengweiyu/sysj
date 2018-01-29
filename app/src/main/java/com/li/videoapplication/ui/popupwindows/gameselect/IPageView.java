package com.li.videoapplication.ui.popupwindows.gameselect;

import android.view.View;

/**
 * Created by linhui on 2017/9/18.
 *
 * 选项卡的基本方法
 *
 */
interface IPageView<T> extends IDataHandler,ISimpleHandler{
    View getView();
    CharSequence getTitle();
    void sendChange();
    void handlerData(T t);

}
