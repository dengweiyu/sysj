package com.ifeimo.im.framwork.interface_im;

import com.ifeimo.im.IEmployee;

import java.util.LinkedList;
import java.util.Set;

/**
 * Created by lpds on 2017/1/19.
 */
public interface IHierarchy extends IEmployee{

    /**
     * 获取所有 一打开的IM窗口
     * @return
     */
    LinkedList<IMWindow> getAllIMWindows();
    /**
     * 获取最末聊天的窗口
     *
     * @return
     */
    IMWindow getFirstWindow();
    /**
     * 获取当前聊天的窗口
     *
     * @return
     */
    IMWindow getLastWindow();

    /**
     * 移除所有imwindow
     */
    void releaseAll();

    boolean isHadWindow(Object o);

    IMWindow getThisShowWindow();

    Set<String> allKeys();

}
