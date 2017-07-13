package com.li.videoapplication.tools;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.ifeimo.im.common.callback.LoginCallBack;
import com.ifeimo.im.common.callback.OnLoginSYSJCallBack;
import com.ifeimo.im.framwork.IMSdk;


import com.li.videoapplication.framework.AppManager;

/**
 * 飞磨IM帮助类
 */
public class FeiMoIMHelper {

    public static final String TAG = FeiMoIMHelper.class.getSimpleName();

    //登陆后链接IM
    public static void Login(String member_id, String nickName, String avatarUrl) {
        IMSdk.Login(AppManager.getInstance().getContext(), member_id,
                nickName, avatarUrl, new LoginCallBack() {
                    @Override
                    public void callSuccess() {
                        Log.d(TAG, "############### 飞磨IM SDK Login Success: ###############");
                    }

                    @Override
                    public void callFail() {
                        Log.d(TAG, "############### 飞磨IM SDK Login Fail: ###############");
                    }
                });
    }

    //注销或关闭app后断开链接IM
    public static void LogOut(Context context, boolean isClearCache) {
        IMSdk.logout(context, isClearCache);
    }

    //更新个人资料
    public static void upDateUser(String nickName, String avatarUrl) {
        IMSdk.upDateUser(AppManager.getInstance().getContext(), nickName, avatarUrl);
    }

    //进入群聊
    public static void createMuccRoom(Context context, String roomJID, String roomName, String roomPic) {
        IMSdk.createMuccRoom(context, roomJID, roomName, roomPic);
    }
}
