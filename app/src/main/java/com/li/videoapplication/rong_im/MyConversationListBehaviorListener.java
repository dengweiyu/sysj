package com.li.videoapplication.rong_im;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.RongIMHelper;
import com.li.videoapplication.ui.ActivityManeger;
import com.li.videoapplication.ui.activity.ConversationActivity;
import com.li.videoapplication.ui.activity.MainActivity;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 融云会话列表点击监听。false为使用融云逻辑处理，true为自定义处理逻辑
 */

public class MyConversationListBehaviorListener implements RongIM.ConversationListBehaviorListener {
    private static final String TAG = MyConversationListBehaviorListener.class.getSimpleName();

    @Override
    public boolean onConversationPortraitClick(Context context, final Conversation.ConversationType conversationType, final String id) {
        Log.d(TAG, "onConversationPortraitClick: s == "+id);

        ActivityManeger.startConversationActivity(context,
                id, "",
                RongIMHelper.getConversationType(conversationType));

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (conversationType == Conversation.ConversationType.GROUP)
                    DataManager.groupName208(id);
                if (conversationType == Conversation.ConversationType.PRIVATE)
                    DataManager.userProfilePersonalInformation(id,"");
            }
        },200);

        return true;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        RongIMClient.getInstance().clearMessagesUnreadStatus(uiConversation.getConversationType(),
                uiConversation.getConversationTargetId(),
                new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Log.d(TAG, "clearMessagesUnreadStatus onSuccess: ");
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.d(TAG, "clearMessagesUnreadStatus onError: ");
                    }
                });

        ActivityManeger.startConversationActivity(context,
                uiConversation.getConversationTargetId(),
                uiConversation.getUIConversationTitle(),
                RongIMHelper.getConversationType(uiConversation.getConversationType()));
        return true;
    }
}
