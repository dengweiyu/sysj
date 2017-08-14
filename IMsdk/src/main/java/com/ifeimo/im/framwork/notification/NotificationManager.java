/**
 * Copyright (c) 2013, Redsolution LTD. All rights reserved.
 *
 * This file is part of Xabber project; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License, Version 3.
 *
 * Xabber is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package com.ifeimo.im.framwork.notification;

import java.util.Map;

import android.net.Uri;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.common.bean.model.HeadLineModel;
import com.ifeimo.im.common.bean.model.IChatMsg;

/**
 * Provides list of notifications first of which should be shown.
 *
 * @param <T>
 * @author alexander.ivanov
 */
public interface NotificationManager extends IEmployee {

    /**
     * @return List of notifications.
     */
    Map getNotifications();

    /**
     * @return Whether notification can be cleared.
     */
    boolean canClearNotifications();

    /**
     * Clear notifications.
     */
    void clearNotifications();

    /**
     * @return Sound for notification.
     */
    Uri getSound();

    /**
     * @return Audio stream type for notification.
     */
    int getStreamType();

    /**
     * @return Resource id with icon for notification bar.
     */
    int getIcon();

//    int notifyMessageNotification(MsgBean msgBean);

    /**
     * 单聊 推送
     * @param msgBean
     */
    void notifyMessageNotification2(IChatMsg msgBean);

    /**
     * im系统简单的通知headline 推送
     * @param msg
     */
    void notifyHeadLineNotifycation(HeadLineModel msg);

    /**
     * 添加检测系统通知
     * @param notifyHeadLineObservable
     */
    void  setNotifyObservable(NotifyHeadLineObservable notifyHeadLineObservable);

    /**
     * 移除检测系统通知
     */
    void removeNotifyObservable();

}
