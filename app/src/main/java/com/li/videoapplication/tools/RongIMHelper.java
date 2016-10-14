package com.li.videoapplication.tools;


import android.net.Uri;
import android.util.Log;

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
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

public class RongIMHelper {

    public static final String TAG = RongIMHelper.class.getSimpleName();

    public static int getConversationType(Conversation.ConversationType type) {
        switch (type) {
            case PRIVATE:
                return ConversationActivity.PRIVATE;
            case GROUP:
                return ConversationActivity.GROUP;
            default:
                return ConversationActivity.PRIVATE;
        }
    }

    public static void setUserInfoProvider() {

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {
                Log.d(TAG, "getUserInfo/userId=" + userId);

                UserProfilePersonalInformationEntity entity = DataManager.userProfilePersonalInformationSync(userId, "");

                UserInfo userInfo = null;

                if (userId.equals(entity.getData().getId()))
                    userInfo = new UserInfo(entity.getData().getId(), entity.getData().getNickname(),
                            Uri.parse(entity.getData().getAvatar()));

                return userInfo;
            }
        }, true);
    }

    public static void setGroupInfoProvider() {

        RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
            @Override
            public Group getGroupInfo(String groupId) {

                GroupName208Entity entity = DataManager.groupName208Sync(groupId);
                Group group = null;

                if (entity.getData().getChatroom_group_name() != null && entity.getData().getAvatar() != null)
                    group = new Group(groupId,
                            entity.getData().getChatroom_group_name(),
                            Uri.parse(entity.getData().getAvatar()));

                return group;
            }
        }, true);
    }

    public static void connectIM() {

        if (AppManager.getInstance().getApplication().getPackageName()
                .equals(AppUtil.getCurrentProcessName(AppManager.getInstance().getApplication().getApplicationContext()))) {
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(PreferencesHepler.getInstance().getUserToken(),
                    new RongIMClient.ConnectCallback() {

                        @Override
                        public void onTokenIncorrect() {
                            Log.d(TAG, "--onTokenIncorrect");
                        }

                        @Override
                        public void onSuccess(String userid) {
                            Log.d(TAG, "--------RongIM.connect onSuccess-------- userid == " + userid);

                            //融云个人信息
                            setUserInfoProvider();
                            //设置群组信息
                            setGroupInfoProvider();
                            // 设置会话列表界面操作的监听器
                            RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());
                            // 设置会话界面操作监听器
                            RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
                            //设置接收消息的监听器
                            RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());

                            // 设置会话界面未读新消息是否展示 注:未读新消息大于1条即展示
                            RongIM.getInstance().enableNewComingMessageIcon(true);

                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Log.d(TAG, "--onError" + errorCode);
                        }
                    });
        }
    }

    public static void refreshUserInfoCache(final String id, final String name, final String portraitUri) {
        UserInfo userInfo = new UserInfo(id, name, Uri.parse(portraitUri));
        RongIM.getInstance().refreshUserInfoCache(userInfo);
    }

    public static void setCoversationNotifMute(Conversation.ConversationType type,
                                               String targetId, boolean state) {
        Conversation.ConversationNotificationStatus cns;
        if (state) {
            cns = Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
        } else {
            cns = Conversation.ConversationNotificationStatus.NOTIFY;
        }

        RongIM.getInstance().setConversationNotificationStatus(type, targetId, cns,
                new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus status) {
                        if (status == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {
                            Log.d(TAG, "onSuccess: 设置免打扰成功");
                        } else if (status == Conversation.ConversationNotificationStatus.NOTIFY) {
                            Log.d(TAG, "onSuccess: 关闭免打扰成功");
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.d(TAG, "onSuccess: 设置免打扰失败");
                    }
                });
    }

    public interface totalUnreadCountCallback {

        void totalUnreadCount(int count);
    }

    public static void getTotalUnreadCount(final totalUnreadCountCallback callback) {

        RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {

            @Override
            public void onSuccess(Integer integer) {
                Log.d(TAG, "getTotalUnreadCount onSuccess: "+integer);
                callback.totalUnreadCount(integer);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d(TAG, "getTotalUnreadCount onError: "+errorCode);
                callback.totalUnreadCount(0);
            }
        });
    }
}
