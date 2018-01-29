package com.li.videoapplication.ui.popupwindows.gameselect;

/**
 * Created by linhui on 2017/9/18.
 *
 * 基本的popup处理的方式
 *
 */
interface ISimpleHandler {
    void show();
    void dismiss();
    IInfoEntity getSelect();
    void setSelect(IInfoEntity iInfoEntity);
    IInfoEntity getOladSelect();
}
