package com.li.videoapplication.rong_im;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.li.videoapplication.R;
import com.li.videoapplication.data.DataManager;
import com.li.videoapplication.data.model.response.UserProfilePersonalInformationEntity;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.UmengAnalyticsHelper;
import com.li.videoapplication.ui.activity.ConversationActivity;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.utils.AppUtil;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 接收消息监听器的实现，所有接收到的消息、通知、状态都经由此处设置的监听器处理。
 * 包括私聊消息、讨论组消息、群组消息、聊天室消息以及各种状态。
 */
public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

    private static final String TAG = MyReceiveMessageListener.class.getSimpleName();
    private Context context;
    private int Notification_ID;

    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(Message message, int left) {
        context = AppManager.getInstance().getContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                MainActivity activity = AppManager.getInstance().getMainActivity();
                //如果Activity 退出了 会导致空指针
                if (activity != null && activity.leftCount != null){
                    activity.leftCount.setVisibility(View.VISIBLE);
                }

                UmengAnalyticsHelper.onEvent(context, UmengAnalyticsHelper.SLIDER, "我的消息-即时消息");
            }
        });

        if (AppUtil.isAppOnForeground(context)) {
            Log.d(TAG, "----------- isforeground -----------");
            //app处于前台时，融云对收到的消息提醒没做处理，所以麻痹的自己写
            if (message.getConversationType() == Conversation.ConversationType.GROUP) {
                Log.d(TAG, "ConversationType.GROUP");
                //群组消息，由于不需要提示，所以走融云默认处理方法
                return false;
            }

            MessageContent messageContent = message.getContent();
            String targetId = message.getTargetId();
            String userId = message.getSenderUserId();
            try {//将对应的targetId作为通知id，清除通知时可用
                Notification_ID = Integer.parseInt(targetId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            Notification.Builder builder = new Builder(context);
            builder.setSmallIcon(R.drawable.logo_round);//设置图标
            builder.setWhen(System.currentTimeMillis());//设置通知时间
            builder.setDefaults(Notification.DEFAULT_LIGHTS);//设置指示灯
            builder.setDefaults(Notification.DEFAULT_SOUND);//设置提示声音
            builder.setDefaults(Notification.DEFAULT_VIBRATE);//设置震动
            builder.setAutoCancel(true);

            if (message.getConversationType() == Conversation.ConversationType.PRIVATE) {
                UserProfilePersonalInformationEntity entity = DataManager.userProfilePersonalInformationSync(userId, "");

                Intent intent = new Intent(context, ConversationActivity.class);
                intent.putExtra("targetId", targetId);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                builder.setContentIntent(pendingIntent);//点击后的意图
                builder.setContentTitle(entity.getData().getNickname());//设置标题
            }

            if (messageContent instanceof TextMessage) {//文本消息
                TextMessage textMessage = (TextMessage) messageContent;
                Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
                builder.setTicker(textMessage.getContent());//手机状态栏的提示
                builder.setContentText(textMessage.getContent());//设置通知内容

            } else if (messageContent instanceof ImageMessage) {//图片消息
                ImageMessage imageMessage = (ImageMessage) messageContent;
                Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
                builder.setTicker("您收到一张图片");//手机状态栏的提示
                builder.setContentText("[图片]");//设置通知内容
            } else if (messageContent instanceof VoiceMessage) {//语音消息
                VoiceMessage voiceMessage = (VoiceMessage) messageContent;
                Log.d(TAG, "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
                builder.setTicker("您收到一条语音信息");//手机状态栏的提示
                builder.setContentText("[语音]");//设置通知内容
            } else {//其他消息
                Log.d(TAG, "onReceived-其他消息");
                builder.setTicker("您收到一条信息");//手机状态栏的提示
                builder.setContentText("您收到一条信息，请点击查看");//设置通知内容
            }

            Notification notification = builder.build();//4.1以上，以下要用getNotification()
            notificationManager.notify(Notification_ID, notification);
            return true;

        } else {
            Log.d(TAG, "----------- isBackground -----------");
            //app处于后台时，走融云默认处理
            return false;
        }
    }
}
