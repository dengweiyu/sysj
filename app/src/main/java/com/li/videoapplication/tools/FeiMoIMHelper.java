package com.li.videoapplication.tools;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.ifeimo.im.common.callback.LoginCallBack;
import com.ifeimo.im.common.callback.OnLoginSYSJCallBack;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.interface_im.IMessage;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.GroupName208Entity;
import com.li.videoapplication.data.model.response.UserProfilePersonalInformationEntity;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.rong_im.MyConversationBehaviorListener;
import com.li.videoapplication.rong_im.MyConversationListBehaviorListener;
import com.li.videoapplication.rong_im.MyReceiveMessageListener;
import com.li.videoapplication.ui.activity.ConversationActivity;
import com.li.videoapplication.utils.AppUtil;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

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
        IMSdk.upDateUser(AppManager.getInstance().getContext(), nickName, avatarUrl, new OnLoginSYSJCallBack() {
            @Override
            public void callSuccess() {
                Log.d(TAG, "############### 飞磨IM SDK upDateUser Success: ###############");
            }

            @Override
            public void callFail(String code) {
                Log.d(TAG, "############### 飞磨IM SDK upDateUser Fail: ###############");
            }
        });
    }

    //进入群聊
    public static void createMuccRoom(Context context, String roomJID, String roomName, String roomPic) {
        IMSdk.createMuccRoom(context, roomJID, roomName, roomPic);
    }
}
