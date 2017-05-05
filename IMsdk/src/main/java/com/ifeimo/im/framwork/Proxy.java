package com.ifeimo.im.framwork;

import com.ifeimo.im.framwork.interface_im.IManagerList;
import com.ifeimo.im.framwork.interface_im.IAccount;
import com.ifeimo.im.framwork.interface_im.IConnect;
import com.ifeimo.im.framwork.interface_im.IHierarchy;
import com.ifeimo.im.framwork.interface_im.ILife;
import com.ifeimo.im.framwork.interface_im.IMRequest;
import com.ifeimo.im.framwork.interface_im.IMessage;
import com.ifeimo.im.framwork.interface_im.IProvider;
import com.ifeimo.im.framwork.notification.NotificationManager;

/**
 * Created by lpds on 2017/2/15.
 */
public final class Proxy {


    public static IMessage getMessageManager(){
        return MessageManager.getInstances();
    }

    public static IConnect getConnectManager(){
        return IMConnectManager.getInstances();
    }

    public static IHierarchy getIMWindowManager(){
        return ChatWindowsManager.getInstences();
    }

    public static ILife getIMWindowLifeManager(){return (ILife) ChatWindowsManager.getInstences();}

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
