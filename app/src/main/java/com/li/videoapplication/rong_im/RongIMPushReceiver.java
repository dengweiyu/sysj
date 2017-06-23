package com.li.videoapplication.rong_im;

import android.content.Context;

import com.li.videoapplication.ui.ActivityManager;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 融云消息提醒接收者
 */
public class RongIMPushReceiver extends PushMessageReceiver {

    /*
        onNotificationMessageArrived
        用来接收服务器发来的通知栏消息(消息到达客户端时触发)，默认return false，通知消息会以融云 SDK 的默认形式展现。
        如果需要自定义通知栏的展示，在这里实现⾃己的通知栏展现代码，同时 return true 即可。
    */
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }


    /*
        onNotificationMessageClicked
        是在⽤户点击通知栏消息时触发 (注意:如果自定义了通知栏的展现，则不会触发标红)，默认 return false。
        如果需要自定义点击通知时的跳转，return true 即可。
    */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {

        String targetId = pushNotificationMessage.getTargetId();
        String targetUserName = pushNotificationMessage.getTargetUserName();
        ActivityManager.startConversationActivity(context, targetId,targetUserName,false);
        return true;
    }
}
