package com.ifeimo.im.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ifeimo.im.common.MD5;
import com.ifeimo.im.common.bean.ConnectBean;
import com.ifeimo.im.common.bean.UserBean;

/**
 * Created by lpds on 2017/1/11.
 */
public final class PManager {

    public static final String NAME = "ifeimo";
    private final static String CONNECT_CONFIG = String.format("%s_connnect_config", NAME);
    private final static String USER = String.format("%s_user_config", NAME);

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
        ConnectBean connectBean = new ConnectBean("op.17sysj.com", 5222,"op.17sysj.com");
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

    public static void saveLogin(Context context, boolean flag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isLogin", flag).commit();
    }

    public static boolean isLoginSucceed(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLogin", false);
    }

    public static void getCacheUser(Context context) {
        if (context == null) {
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        UserBean.setMemberID(sharedPreferences.getString("MEMBER_ID", UserBean.getMemberID()));
        UserBean.setAvatarUrl(sharedPreferences.getString("AVATARURL", UserBean.getAvatarUrl()));
        UserBean.setNickName(sharedPreferences.getString("NICK_NAME", UserBean.getNickName()));
    }

    public static boolean isOldLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        try {

            if (sharedPreferences.getString("T_MD5", "-1").equals(sharedPreferences.getString("L_MD5", "-1"))) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.i("XMPP_IMConnectManager", "------- 不是老用户 -------");
        return false;
    }

    public static void clearCacheUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    @Deprecated
    public static String getDefaultRoom(Context context) {

        return context.getSharedPreferences(CONNECT_CONFIG, Context.MODE_PRIVATE).getString("room", "1");

    }

    @Deprecated
    public static void saveDefaultRoom(Context context, String roomid) {
        context.getSharedPreferences(CONNECT_CONFIG, Context.MODE_PRIVATE).edit().putString("room", roomid).commit();
    }

}
