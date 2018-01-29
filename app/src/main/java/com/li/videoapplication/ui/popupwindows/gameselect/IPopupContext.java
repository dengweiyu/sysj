package com.li.videoapplication.ui.popupwindows.gameselect;

import android.support.v4.app.FragmentActivity;

/**
 * Created by linhui on 2017/9/18.
 *
 * 上下文的必须实现的方法
 *
 */
public interface IPopupContext extends IPopup.PopupListener {
    FragmentActivity getActivity();
    int getMaxWidth();
    int getMaxHeigt();
    void closeSoftKeyboard();
    void runOnUiThread(Runnable runnable);
}
