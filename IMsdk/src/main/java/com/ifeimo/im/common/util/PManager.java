package com.ifeimo.im.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ifeimo.im.common.MD5;
import com.ifeimo.im.common.bean.ConnectBean;
import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.commander.ILock;

/**
 * Created by lpds on 2017/1/11.
 */
public final class PManager {

    public static final String NAME = "ifeimo";
    private final static String CONNECT_CONFIG = String.format("%s_connnect_config", NAME);
    private final static String USER = String.format("%s_user_config", NAME);
    private final static String USER_1 = String.format("%s_user_config_1", NAME);

    public static void saveConnectConfig(Context context, ConnectBean connectBean) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONNECT_CONFIG, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("HOST", connectBean.getHost())
                .putInt("PROT", connectBean.getPort())
                .putString("SERVICE_NAME", connectBean.getServiceName()).commit();
    }

    public static ConnectBean getConnectConfig(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(CONNECT_CONFIG, Context.MODE_PRIVATE);
//        ConnectBean connectBean = new ConnectBean(
//                sharedPreferences.getString("HOST", "op.17sysj.com"),
//                sharedPreferences.getInt("PROT", 5222),
//                sharedPreferences.getString("SERVICE_NAME", "op.17sysj.com"));
//                sharedPreferences.getString("HOST", "192.168.48.185"),
//                sharedPreferences.getInt("PROT", 5222),
//                sharedPreferences.getString("SERVICE_NAME", "192.168.48.185"));
        ConnectBean connectBean = new ConnectBean("op.17sysj.com", 5222, "op.17sysj.com");
        return connectBean;
    }

    public static void saveUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        String $memberid = sharedPreferences.getString("MEMBER_ID", "-1");
        String $avatarurl = sharedPreferences.getString("AVATARURL", "-1");
        String $nickname = sharedPreferences.getString("NICK_NAME", "-1");
        String saveL = MD5.getMD5($memberid + $nickname + $avatarurl);
        String saveT = MD5.getMD5(UserBean.getMemberID() + UserBean.getNickName() + UserBean.getAvatarUrl());
        sharedPreferences.edit().putString("MEMBER_ID", UserBean.getMemberID())
                .putString("AVATARURL", UserBean.getAvatarUrl())
                .putString("NICK_NAME", UserBean.getNickName()).putString("T_MD5", saveT).putString("L_MD5", saveL).commit();
    }

    public static void saveLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_1, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("LAST_MEMBER_ID", UserBean.getMemberID())
                .putString("LAST_AVATARURL", UserBean.getAvatarUrl())
                .putString("LAST_NICK_NAME", UserBean.getNickName()).commit();
    }

    public static boolean isOldAcoount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_1, Context.MODE_PRIVATE);
        String LAST_MEMBER_ID = sharedPreferences.getString("LAST_MEMBER_ID", "-1");
        String LAST_AVATARURL = sharedPreferences.getString("LAST_AVATARURL", "-1");
        String LAST_NICK_NAME = sharedPreferences.getString("LAST_NICK_NAME", "-1");
        if (!UserBean.getMemberID().equals(LAST_MEMBER_ID)) {
            return false;
        } else if (!UserBean.getAvatarUrl().equals(LAST_AVATARURL) || !UserBean.getNickName().equals(LAST_NICK_NAME)) {
            return false;
        } else {
            return true;
        }
    }

    public static void getCacheUser(Context context) {
        synchronized (Proxy.getLockManager().getLock(ILock.USER_LOCK)) {
            if (context == null) {
                return;
            }
            SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
            UserBean.setMemberID(sharedPreferences.getString("MEMBER_ID", UserBean.getMemberID()));
            UserBean.setAvatarUrl(sharedPreferences.getString("AVATARURL", UserBean.getAvatarUrl()));
            UserBean.setNickName(sharedPreferences.getString("NICK_NAME", UserBean.getNickName()));
        }
    }

    public static void clearCacheUser(Context context) {
        synchronized (Proxy.getLockManager().getLock(ILock.USER_LOCK)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();
        }
    }


}
