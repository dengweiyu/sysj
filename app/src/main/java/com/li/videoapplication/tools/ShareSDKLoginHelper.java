package com.li.videoapplication.tools;

import java.util.HashMap;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.framework.AppManager;
import com.mob.tools.utils.UIHandler;

/**
 * 功能：第三方登录ShareSDK
 */
public class ShareSDKLoginHelper implements PlatformActionListener, Callback {

    protected final String name = this.getClass().getName();

    protected final String simpleName = this.getClass().getSimpleName();

    protected final String tag = name;

    protected final String action = simpleName;

    private Context context;

    public void initSDK(Context context) {
        this.context = context;
        ShareSDK.initSDK(context);
    }

    public void stopSDK(Context context) {
        ShareSDK.stopSDK(context);
    }

    // 登录平台 1：qq 2.微博 3.微信
    private int openState;

    /**
     * QQ登录
     */
    public void qq() {
        openState = 1;
        authorize(new QQ(context));
    }

    /**
     * 微博登录
     */
    public void wb() {
        openState = 2;
        authorize(new SinaWeibo(context));
    }

    /**
     * 微信登录
     */
    public void wx() {
        openState = 3;
        authorize(new Wechat(context));
    }

    /**
     * 第三方登录
     *
     * @param platform
     */
    private void authorize(Platform platform) {
        if (platform == null) {
            return;
        }
        // 判断指定平台是否已经完成授权
        if (platform.isAuthValid()) {
            String userId = platform.getDb().getUserId();
            if (userId != null) {// 已经登录过了
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);

                Message msg = new Message();
                msg.what = MSG_LOGIN;
                msg.obj = platform;
                UIHandler.sendMessage(msg, this);

                // 提交登录
                // DataManager.login(userId);

                String userName = platform.getDb().getUserName();
                String userGender = platform.getDb().getUserGender();
                String userIcon = platform.getDb().getUserIcon();
                DataManager.login(userId, userName, userName, userGender, "", userIcon);
                return;
            }
        }
        platform.setPlatformActionListener(this);
        // true不使用SSO授权，false使用SSO授权
        platform.SSOSetting(false);
        // 获取用户资料
        platform.showUser(null);
        UIHandler.sendEmptyMessage(MSG_LOGIN, this);

    }

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;

    /**
     * 处理登录状态
     */
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_USERID_FOUND:
                ToastHelper.l("用户信息已存在，正在跳转登录操作…");
                break;

            case MSG_LOGIN:
                ToastHelper.l("正在登录中…");
                break;

            case MSG_AUTH_CANCEL:
                ToastHelper.s("授权操作已取消");
                break;

            case MSG_AUTH_ERROR:
                ToastHelper.s("授权操作遇到错误");
                break;

            case MSG_AUTH_COMPLETE:
                ToastHelper.l("授权成功，正在跳转登录操作…");
                break;
        }
        return false;
    }

    /**
     * 第三方登录回调
     */
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        Log.e(tag, "platform=" + platform);
        Log.e(tag, "action=" + action);
        Log.e(tag, "res=" + res);
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);

            Message msg = new Message();
            msg.what = MSG_LOGIN;
            msg.obj = platform;
            UIHandler.sendMessage(msg, this);
        }
        if (openState == 1) {
            System.out.println("qq++++++++++");
            String openId = platform.getDb().getUserId();
            String nickname = res.get("nickname").toString();
            String sex = res.get("gender").toString();
            String location = res.get("province").toString() + res.get("city").toString();
            String figureurl = res.get("figureurl").toString();
            DataManager.login(openId, nickname, nickname, sex, location, figureurl);
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.SLIDER, "QQ登录成功");
        }
        if (openState == 2) {
            System.out.println("sina++++++++++");
            String openId = res.get("id").toString();
            String nickname = res.get("screen_name").toString();
            String sex = ((res.get("gender").equals("m")) ? "男" : "女");
            String location = res.get("location").toString();
            String figureurl = res.get("avatar_large").toString();
            DataManager.login(openId, nickname, nickname, sex, location, figureurl);
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.SLIDER, "新浪微博登录成功");
        }
        if (openState == 3) {
            System.out.println("weixin++++++++++");
            String openId = res.get("unionid").toString();
            String nickname = res.get("nickname").toString();
            String sex = res.get("sex").toString();
            String location = res.get("province").toString() + " " + res.get("city").toString();
            String figureurl = res.get("headimgurl").toString();
            DataManager.login(openId, nickname, nickname, sex, location, figureurl);
            UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.SLIDER, "微信登录成功");
        }
    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    public static void removeAccount(Platform platform) {
        if (platform.isValid()) {
            platform.removeAccount();
        }
    }

    public static void removeAllAccount() {
        Context context = AppManager.getInstance().getContext();
        try {
            removeAccount(new QQ(context));
            removeAccount(new SinaWeibo(context));
            removeAccount(new Wechat(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
