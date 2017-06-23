package com.ifeimo.im.framwork;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Switch;

import com.ifeimo.im.R;
import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.common.bean.OpenHelpBean;
import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.bean.model.Account2SubscriptionModel;
import com.ifeimo.im.common.bean.model.AccountModel;
import com.ifeimo.im.common.bean.model.ChatMsgModel;
import com.ifeimo.im.common.bean.model.GroupChatModel;
import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.common.bean.model.SubscriptionModel;
import com.ifeimo.im.common.callback.LoginCallBack;
import com.ifeimo.im.common.IntentManager;
import com.ifeimo.im.common.callback.LogoutCallBack;
import com.ifeimo.im.common.callback.OnLoginSYSJCallBack;
import com.ifeimo.im.common.util.PManager;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.message.OnGroupItemOnClickListener;
import com.ifeimo.im.framwork.message.OnMessageReceiver;
import com.ifeimo.im.framwork.message.OnSimpleMessageListener;
import com.ifeimo.im.service.LoginService;

import java.util.HashMap;
import java.util.Map;

import y.com.sqlitesdk.framework.AppMain;
import y.com.sqlitesdk.framework.IfeimoSqliteSdk;
import y.com.sqlitesdk.framework.business.Business;
import y.com.sqlitesdk.framework.business.CenterServer;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/1/12.
 */
public class IMSdk {
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
        LockManager.getInstances();
        ChatWindowsManager.getInstences();
        IMConnectManager.getInstances();
        IMAccountManager.getInstances();
        MessageManager.getInstances();
        SettingsManager.getInstances();
        RequestManager.getInstances();
        IMNotificationManager.getInstances();
        ThreadUtil.getInstances();
        Activitys.getInstances();
        ProviderManager.getInstances();
        IfeimoSqliteSdk.init(SqliteMain.getInstances());

        application.registerActivityLifecycleCallbacks(Proxy.getConnectManager().getConnectSupport());

    }

    /**
     * 注销登录
     *
     * @param context * @param isClearCache 是否清空用户缓存
     */
    public static void logout(Context context, boolean isClearCache) {
        Log.i(TAG, "Thread.currentThread() "+Thread.currentThread().getName());
        UserBean.clear();
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
        UserBean.setLoginUser(myMemberID, myNickName, myAvatarUrl, null, null, null, null, null, null);
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
    public static void Login(Context context, String myMemberID, String myNickName, String myAvatarUrl) {
        UserBean.setLoginUser(myMemberID, myNickName, myAvatarUrl, null, null, null, null, null, null);
        PManager.saveUser(context);
        context.startService(new Intent(context, LoginService.class));
    }

    /**
     * 更新用户信息
     *
     * @param context
     * @param myNickName          昵称
     * @param myAvatarUrl         图片
     * @param onLoginSYSJCallBack 回调
     */
    public static void upDateUser(Context context, String myNickName, String myAvatarUrl, OnLoginSYSJCallBack onLoginSYSJCallBack) {
        UserBean.setAvatarUrl(myAvatarUrl);
        UserBean.setNickName(myNickName);
        PManager.saveUser(context);
        IMConnectManager.getInstances().addOnSYSJLoginListener(onLoginSYSJCallBack);
        Intent intent = new Intent(context, LoginService.class);
        intent.putExtra(LoginService.RELOGIN_KEY, LoginService.RELOGIN);
        context.startService(intent);
    }


    /**
     * 进入房间
     *
     * @param context
     * @param roomJID  房间id
     * @param roomName 房间名字
     */
    public static void createMuccRoom(Context context, String roomJID, String roomName, String roomPicurl) {
//        if (IMConnectManager.getInstance() == null || !IMConnectManager.getInstance().isConnect()) {
//            Toast.makeText(context, "网络不稳定，无法进入群聊！", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Map<String, String> map = new HashMap<>();
        map.put("roomJID", roomJID);
        map.put("roomName", roomName);
        map.put("roomPicurl", roomPicurl);
        IntentManager.createIMWindows(context, map);
        Log.i("XMPP", "------ 进入群聊 ------");

//        testCreateList(context);

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

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("receiverID", receiverID);
        intent.putExtra("receiverNickName", receiverNickName);
        intent.putExtra("receiverAvatarUrl", receiverAvatarUrl);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }


    public static void addLoginCallBack(LoginCallBack loginCallBack) {
        IMConnectManager.getInstances().addLoginListener(loginCallBack);
    }

    public static void removeLoginCallBack() {
        IMConnectManager.getInstances().addLoginListener(null);
    }


    /**
     * 註銷
     *
     * @param logoutCallBack
     */
    public static void addLogoutCallBack(LogoutCallBack logoutCallBack) {
        IMConnectManager.getInstances().addLogoutCallBack(logoutCallBack);
    }

    public static void removeLogoutCallBack() {
        IMConnectManager.getInstances().removeLogoutCallBack();
    }

    public static void reigisterMessageListener(OnMessageReceiver onMessageReceiver) {
        MessageManager.getInstances().registerOnMessageReceiver(onMessageReceiver);
    }

    public static void reigisterMessageListener(OnSimpleMessageListener onSimpleMessageListener) {
        MessageManager.getInstances().registerOnMessageReceiver(onSimpleMessageListener);
    }

    public static void setOnGroupItemOnClick(OnGroupItemOnClickListener onGroupItemOnClick) {
        Proxy.getMessageManager().setOnGroupItemOnClickListener(onGroupItemOnClick);
    }

}
