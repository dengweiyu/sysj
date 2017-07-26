package com.ifeimo.im.framwork;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ifeimo.im.R;
import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.common.bean.model.AccountModel;
import com.ifeimo.im.common.callback.LoginCallBack;
import com.ifeimo.im.common.IntentManager;
import com.ifeimo.im.common.callback.LogoutCallBack;
import com.ifeimo.im.common.util.PManager;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.message.FileTransferImp;
import com.ifeimo.im.framwork.message.IQMessageManager;
import com.ifeimo.im.framwork.message.OnGroupItemOnClickListener;
import com.ifeimo.im.framwork.message.PresenceMessageManager;
import com.ifeimo.im.service.LoginService;

import java.util.HashMap;
import java.util.Map;

import y.com.sqlitesdk.framework.IfeimoSqliteSdk;

/**
 * Created by lpds on 2017/1/12.
 */
public final class IMSdk {
    private static final String TAG = "XMPP_IMSDK";
    public static Application CONTEXT;
    public static final int versionCode = 4;

    /**
     * 初始化 application
     *
     * @param application
     */
    public static void init(Application application) {
        CONTEXT = application;
        FileTransferImp.getInstances();
        ChatWindowsManager.getInstances();
        IMConnectManager.getInstances();
        IMAccountManager.getInstances();
        PresenceMessageManager.getInstances();
        IQMessageManager.getInstances();
        MessageManager.getInstances();
        SettingsManager.getInstances();
        RequestManager.getInstances();
        IMNotificationManager.getInstances();
        ThreadUtil.getInstances();
        Activitys.getInstances();
        ProviderManager.getInstances();
        IfeimoSqliteSdk.init(SqliteMain.getInstances());
        //application.registerActivityLifecycleCallbacks( IMConnectManager.getInstances().getConnectSupport());
    }

    /**
     * 注销登录
     *
     * @param context * @param isClearCache 是否清空用户缓存
     */
    public static void logout(Context context, boolean isClearCache) {
        Log.i(TAG, "Thread.currentThread() "+Thread.currentThread().getName());
        Proxy.getAccountManger().clearUserSelf();
        if (isClearCache) {
            PManager.clearCacheUser(context);
        }

        IMConnectManager.getInstances().disconnect();
        IMConnectManager.getInstances().addLogoutCallBack(new LogoutCallBack() {
            @Override
            public void logoutSuccess() {
                Log.i("XMPP", "------ 注销成功 ------");
            }
        });
    }

    /**
     * 登录
     *
     * @param context
     * @param myMemberID    用户id
     * @param myNickName    昵称
     * @param myAvatarUrl   图片
     * @param loginCallBack 回调
     */
    public static void Login(Context context, String myMemberID, String myNickName, String myAvatarUrl, LoginCallBack loginCallBack) {

        AccountModel.Build build = new AccountModel.Build();
        build.memberId = myMemberID;
        build.avatarUrl = myAvatarUrl;
        build.member_nick_name = myNickName;
        Proxy.getAccountManger().setAccount(build);
        PManager.saveUser(context);
        addLoginCallBack(loginCallBack);
        context.startService(new Intent(context, LoginService.class));
    }

    /**
     * 登录
     *
     * @param context
     * @param myMemberID  用户id
     * @param myNickName  昵称
     * @param myAvatarUrl 图片
     */
    @Deprecated
    public static void Login(Context context, String myMemberID, String myNickName, String myAvatarUrl) {
        AccountModel.Build build = new AccountModel.Build();
        build.memberId = myMemberID;
        build.avatarUrl = myAvatarUrl;
        build.member_nick_name = myNickName;
        Proxy.getAccountManger().setAccount(build);
        PManager.saveUser(context);
        context.startService(new Intent(context, LoginService.class));
    }

    /**
     * 更新用户信息
     *
     * @param context
     * @param myNickName          昵称
     * @param myAvatarUrl         图片
     */
    public static void upDateUser(Context context, String myNickName, String myAvatarUrl) {
        AccountModel accountModel = Proxy.getAccountManger().getAccount(false);
        accountModel.setAvatarUrl(myAvatarUrl);
        accountModel.setNickName(myNickName);
        PManager.saveUser(context);
        Proxy.getConnectManager().updateLogin();
    }


    /**
     * 进入房间
     *
     * @param context
     * @param roomJID  房间id
     * @param roomName 房间名字
     */
    public static void createMuccRoom(Context context, String roomJID, String roomName, String roomPicurl) {
        if(Proxy.getAccountManger().isUserNull()){
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("roomJID", roomJID);
        map.put("roomName", roomName);
        map.put("roomPicurl", roomPicurl);
        IntentManager.createIMWindows(context, map);
        Log.i("XMPP", "------ 进入群聊 ------");

    }

    /**
     * 进入单聊
     *
     * @param context
     * @param receiverID        对方id
     * @param receiverNickName  对方昵称
     * @param receiverAvatarUrl 对方图片
     */
    public static void createChat(Context context, String receiverID,
                                  String receiverNickName, String receiverAvatarUrl) {

        createChat(context,receiverID,receiverNickName,receiverAvatarUrl,ChatActivity.SHOW_EFAULT,null);


    }

    /**
     * 进入单聊
     *
     * @param context
     * @param receiverID        对方id
     * @param receiverNickName  对方昵称
     * @param receiverAvatarUrl 对方图片
     */
    public static void createChat(Context context, String receiverID,
                                  String receiverNickName, String receiverAvatarUrl,int isFastReply,String showqq) {
        if(Proxy.getAccountManger().isUserNull()){
            return;
        }
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("receiverID", receiverID);
        intent.putExtra("receiverNickName", receiverNickName);
        intent.putExtra("receiverAvatarUrl", receiverAvatarUrl);
        intent.putExtra("show", isFastReply);
        if(!StringUtil.isNull(showqq) && StringUtil.isNumber(showqq)) {
            intent.putExtra("showQQ", showqq);
        }
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }


    public static void addLoginCallBack(LoginCallBack loginCallBack) {
        IMConnectManager.getInstances().addLoginListener(loginCallBack);
    }
    public static void setOnGroupItemOnClick(OnGroupItemOnClickListener onGroupItemOnClick) {
        Proxy.getMessageManager().setOnGroupItemOnClickListener(onGroupItemOnClick);
    }

}
