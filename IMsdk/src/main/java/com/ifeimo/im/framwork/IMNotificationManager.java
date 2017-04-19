package com.ifeimo.im.framwork;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.ifeimo.im.R;
import com.ifeimo.im.activity.ChatRecyclerActivity;
import com.ifeimo.im.common.bean.AccountBean;
import com.ifeimo.im.common.bean.msg.MsgBean;
import com.ifeimo.im.common.util.AppUtil;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.database.business.Business;
import com.ifeimo.im.framwork.interface_im.IMWindow;
import com.ifeimo.im.framwork.notification.NotificationManager;
import com.ifeimo.im.framwork.notification.NotifyBean;
import com.ifeimo.im.framwork.request.Account;
import com.ifeimo.im.framwork.setting.Builder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by lpds on 2017/1/16.
 *
 * 管理消息推送
 *
 *
 */
final class IMNotificationManager implements NotificationManager<NotifyBean> {
    private static final String TAG = "XMPP_Notification";
    private static IMNotificationManager notificationManager;
    private android.app.NotificationManager notificationManagerServier;
    private static final long VIBRATION_DURATION = 500;
    private Map<String, List<MsgBean>> messageNotifications;
    private Vibrator vibrator;

    static {
        notificationManager = new IMNotificationManager();
        notificationManager.notificationManagerServier = (android.app.NotificationManager) IMSdk.CONTEXT.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private IMNotificationManager() {
        ManagerList.getInstances().addManager(this);
        messageNotifications = new HashMap<>();
        vibrator = (Vibrator) IMSdk.CONTEXT.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static NotificationManager getInstances() {
        return notificationManager;
    }

    /**
     * 准备发送notification
     * @param msgBean
     * @return
     */
    private int notifyMessageNotification(MsgBean msgBean) {
        {
            switch (IMSdk.versionCode){
                case 1:Log.i(TAG,"------ versionCode = 1 ，不顯示 notification -------");
                return 0;
                default:
                    break;
            }
        }

        if (!cheackShowNotification(msgBean)) {//
            return -1;
        }
        /**
         * 就算当前app在前端也显示
         */
        if(!SettingsManager.getInstances().getBuilder().getNotificationSettings(false).getMode().equals(Builder.Notification.AUTO_MODE)) {
            if (AppUtil.isAppInForeground(IMSdk.CONTEXT) && vibrator != null) {
                ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
                    @Override
                    public void run() {
                        vibrator.vibrate(2000);
                        vibrator.cancel();

                    }
                });
                return -1;
            }
        }
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(IMSdk.CONTEXT)
                    .load(msgBean.getMemberAvatarUrl())
                    .asBitmap() //必须
                    .fitCenter()
                    .into(300,300)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(IMSdk.CONTEXT);
        notificationBuilder.setContentTitle("手游视界");
        notificationBuilder.setContentText(
                StringUtil.isNull(msgBean.getMemberNickName()) ? "手游视界用户 " : msgBean.getMemberNickName() +
                        "说：" + msgBean.getContent());
        notificationBuilder.setAutoCancel(true);
        if (bitmap != null) {
            notificationBuilder.setLargeIcon(bitmap);
        } else {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(IMSdk.CONTEXT.getResources(), getIcon()));
        }
        notificationBuilder.setWhen(System.currentTimeMillis());
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setTicker("手游视界");
//        notificationBuilder.setContentTitle("")
        notificationBuilder.setSmallIcon(getIcon());
        notificationBuilder.setVibrate(new long[]{0, VIBRATION_DURATION});
        notificationBuilder.setSound(getSound());
        notificationBuilder.setLights(0xFF0000, 3000, 3000);
        notificationBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        Intent intent = new Intent(IMSdk.CONTEXT, ChatRecyclerActivity.class);    //点击通知进入的界面
        intent.putExtra("receiverID", msgBean.getMemberId());
        intent.putExtra("receiverNickName", msgBean.getMemberNickName());
        intent.putExtra("receiverAvatarUrl", msgBean.getMemberAvatarUrl());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(IMSdk.CONTEXT, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        Notification notification = notificationBuilder.build();
        notificationManagerServier.notify(msgBean.getId(), notification);
        addNotifycation(msgBean);
        return msgBean.getId();
    }

    @Override
    public void notifyMessageNotification2(final MsgBean msgBean) {
//        ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
//            public void run() {
                AccountBean accountBean = Business.getInstances().queryByID(msgBean.getMemberId());
                if (accountBean != null && !StringUtil.isNull(accountBean.getNickName())) {
                    msgBean.setMemberNickName(accountBean.getNickName());
                    msgBean.setMemberAvatarUrl(accountBean.getAvatarUrl());
                } else {
                    try {
                        if (StringUtil.isNull(msgBean.getMemberAvatarUrl()) || StringUtil.isNull(msgBean.getMemberNickName())) {
                            Response response = Account.getMemberInfo(IMSdk.CONTEXT, msgBean.getMemberId());
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            Log.i(TAG, "------ 获取到的JSon" + jsonObject + " ------");
                            if (jsonObject.getInt("code") == 200) {
                                msgBean.setMemberAvatarUrl(jsonObject.getString("avatarUrl"));
                                msgBean.setMemberNickName(jsonObject.getString("nickname"));
                            }
                        }
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Fields.AccounFields.MEMBER_ID, msgBean.getMemberId());
                        contentValues.put(Fields.AccounFields.MEMBER_NICKNAME, msgBean.getMemberNickName());
                        contentValues.put(Fields.AccounFields.MEMBER_AVATARURL, msgBean.getMemberAvatarUrl());
                        Business.getInstances().insertById(contentValues);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                notifyMessageNotification(msgBean);
//            }
//        });
    }

    /**
     * 检验此消息的 Imwindow 是否已经打开
     * @param m
     * @return
     */
    private boolean cheackShowNotification(MsgBean m) {
        final IMWindow imWindow = ChatWindowsManager.getInstences().getLastWindow();
        if (imWindow != null && AppUtil.isAppInForeground(imWindow.getContext())) {
            switch (imWindow.getType()) {
                case IMWindow.CHAT_TYPE:
                    if (imWindow.getReceiver() != null) {
                        if (m.getMemberId().equals(imWindow.getReceiver())) {
                            return false;
                        }
                    }
                    break;
                case IMWindow.MUCCHAT_TYPE:
                    break;
            }
        }
        return true;
    }

    /**
     * 添加已发送的notification
     * @param msgBean
     */
    @Deprecated
    private void addNotifycation(MsgBean msgBean) {
        if (messageNotifications.containsKey(msgBean.getMemberId())) {
            List<MsgBean> list = messageNotifications.get(msgBean.getMemberId());
            list.add(msgBean);
        } else {
            final LinkedList<MsgBean> list = new LinkedList<>();
            messageNotifications.put(msgBean.getMemberId(), list);
            list.add(msgBean);
        }
    }


    @Override
    public Map getNotifications() {
        return messageNotifications;
    }

    /**
     * 清空所有notifycation
     * @return
     */
    @Override
    public boolean canClearNotifications() {

        final IMWindow imWindow = ChatWindowsManager.getInstences().getAllIMWindows().getLast();
        switch (imWindow.getType()) {
            case IMWindow.CHAT_TYPE:
                List<MsgBean> linkedList = messageNotifications.get(imWindow.getReceiver());
                if (linkedList != null) {
                    for (MsgBean msgBean : linkedList) {
                        notificationManagerServier.cancel(msgBean.getId());
                    }
                    linkedList.clear();
                }
                break;
            case IMWindow.MUCCHAT_TYPE:
                break;
        }
        return false;
    }

    @Override
    public void clearNotifications() {

    }

    @Override
    public Uri getSound() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    @Override
    public int getStreamType() {
        return 0;
    }

    @Override
    public int getIcon() {
        return R.drawable.logo_round;
    }

    @Override
    public boolean isInitialized() {
        return true;
    }
}
