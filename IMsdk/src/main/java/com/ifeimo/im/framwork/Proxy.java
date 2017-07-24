package com.ifeimo.im.framwork;

import com.ifeimo.im.framwork.commander.ILockManager;
import com.ifeimo.im.framwork.commander.IManagerList;
import com.ifeimo.im.framwork.commander.IAccount;
import com.ifeimo.im.framwork.commander.IConnect;
import com.ifeimo.im.framwork.commander.IHierarchy;
import com.ifeimo.im.framwork.commander.ILife;
import com.ifeimo.im.framwork.commander.IMRequest;
import com.ifeimo.im.framwork.commander.MessageObserver;
import com.ifeimo.im.framwork.commander.IProvider;
import com.ifeimo.im.framwork.notification.NotificationManager;

/**
 * Created by lpds on 2017/2/15.
 */
public final class Proxy {


    public static MessageObserver getMessageManager(){
        return MessageManager.getInstances();
    }

    public static IConnect getConnectManager(){
        return IMConnectManager.getInstances();
    }

    public static IHierarchy getIMWindowManager(){
        return ChatWindowsManager.getInstances();
    }

    public static ILife getIMWindowLifeManager(){return (ILife) ChatWindowsManager.getInstances();}

    public static IAccount getAccountManger(){
        return IMAccountManager.getInstances();
    }

    public static NotificationManager getIMNotificationManager(){return IMNotificationManager.getInstances();}

    public static IMRequest getRequest(){
        return RequestManager.getInstances();
    }

    public static IManagerList getManagerList(){return ManagerList.getInstances();}

    public static IProvider getProvide(){
        return ProviderManager.getInstances();
    }

    @Deprecated
    public static Activitys getActivitys(){return Activitys.getInstances();}



}
