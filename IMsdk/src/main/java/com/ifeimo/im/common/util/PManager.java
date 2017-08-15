package com.ifeimo.im.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Debug;

import com.ifeimo.im.BuildConfig;
import com.ifeimo.im.common.bean.ConnectBean;
import com.ifeimo.im.common.bean.model.AccountModel;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.lock.LockSupport;
import com.ifeimo.im.framwork.request.Account;


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
        ConnectBean connectBean;
        if(!IMSdk.Debug) {
            connectBean = new ConnectBean("op.17sysj.com", 5222, "op.17sysj.com");
        }else{
            connectBean = new ConnectBean("192.168.48.54", 5222, "192.168.48.54");
        }



        return connectBean;
    }

    public static void saveUser(Context context) {
        AccountModel accountModel = Proxy.getAccountManger().getAccount(true);
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("MEMBER_ID", accountModel.getMemberId())
                .putString("AVATARURL", accountModel.getAvatarUrl())
                .putString("NICK_NAME", accountModel.getNickName()).commit();
    }

    public static void saveLogin(Context context) {
        AccountModel accountModel = Proxy.getAccountManger().getAccount(true);
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_1, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("LAST_MEMBER_ID", accountModel.getMemberId())
                .putString("LAST_AVATARURL", accountModel.getAvatarUrl())
                .putString("LAST_NICK_NAME", accountModel.getNickName()).commit();
    }

    public static boolean isOldAcoount(Context context) {
        AccountModel accountModel = Proxy.getAccountManger().getAccount(true);
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_1, Context.MODE_PRIVATE);
        String LAST_MEMBER_ID = sharedPreferences.getString("LAST_MEMBER_ID", "-1");
        String LAST_AVATARURL = sharedPreferences.getString("LAST_AVATARURL", "-1");
        String LAST_NICK_NAME = sharedPreferences.getString("LAST_NICK_NAME", "-1");
        if (! accountModel.getMemberId().equals(LAST_MEMBER_ID)) {
            return false;
        } else if (!accountModel.getAvatarUrl().equals(LAST_AVATARURL) || !accountModel.getNickName().equals(LAST_NICK_NAME)) {
            return false;
        } else {
            return true;
        }
    }

    public static void getCacheUser(Context context) {
        AccountModel accountModel = Proxy.getAccountManger().getAccount(false);
        synchronized (LockSupport.USER_LOCK) {
            if (context == null) {
                return;
            }
            SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
            accountModel.setMemberId(sharedPreferences.getString("MEMBER_ID", accountModel.getMemberId()));
            accountModel.setAvatarUrl(sharedPreferences.getString("AVATARURL", accountModel.getAvatarUrl()));
            accountModel.setNickName(sharedPreferences.getString("NICK_NAME", accountModel.getNickName()));
        }
    }

    public static void clearCacheUser(Context context) {
        synchronized (LockSupport.USER_LOCK) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();
//            sharedPreferences = context.getSharedPreferences(USER_1, Context.MODE_PRIVATE);
//            sharedPreferences.edit().clear().commit();
        }
    }


}
