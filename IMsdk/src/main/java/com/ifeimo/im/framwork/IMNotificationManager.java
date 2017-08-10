package com.ifeimo.im.framwork;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.ifeimo.im.OnOutIM;
import com.ifeimo.im.R;
import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.common.bean.model.AccountModel;
import com.ifeimo.im.common.bean.model.HeadLineModel;
import com.ifeimo.im.common.bean.model.IChatMsg;
import com.ifeimo.im.common.bean.response.MemberInfoRespones;
import com.ifeimo.im.common.util.AppUtil;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.commander.IMWindow;
import com.ifeimo.im.framwork.notification.NotificationManager;
import com.ifeimo.im.framwork.notification.NotifyObservable;
import com.ifeimo.im.framwork.request.Account;
import com.ifeimo.im.framwork.setting.Builder;
import com.ifeimo.im.provider.ChatProvider;
import com.ifeimo.im.provider.InformationProvide;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import y.com.sqlitesdk.framework.business.Business;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/1/16.
 * <p/>
 * 管理消息推送
 */
final class IMNotificationManager implements NotificationManager, OnOutIM {
    private static final String TAG = "XMPP_Notification";
    private static IMNotificationManager notificationManager;
    private android.app.NotificationManager notificationManagerServier;
    private static final long VIBRATION_DURATION = 500;
    /**
     * 推送的消息
     */
    private Map<String, List<IChatMsg>> messageNotifications;
    /**
     * 震动
     */
    private Vibrator vibrator;

    /**
     * 系统IM消息集合
     */
    private Map<Integer,HeadLineModel> headLineMsgArray;
    /**
     * 系统IM消息监听
     */
    private NotifyObservable notifyObservableSet;

    static {
        notificationManager = new IMNotificationManager();
        notificationManager.notificationManagerServier = (android.app.NotificationManager) IMSdk.CONTEXT.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private IMNotificationManager() {
        ManagerList.getInstances().addManager(this);
        messageNotifications = new HashMap<>();
        headLineMsgArray = new HashMap<>();
        vibrator = (Vibrator) IMSdk.CONTEXT.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static NotificationManager getInstances() {
        return notificationManager;
    }

    /**
     * 准备发送notification
     *
     * @return
     */
    private int notifyChatMessageNotification(IChatMsg iMsg) {
        {
            switch (IMSdk.versionCode) {
                case 1:
                    Log.i(TAG, "------ versionCode = 1 ，不顯示 notification -------");
                    return 0;
                default:
                    break;
            }
        }

        if (!cheackShowNotification(iMsg)) {//
            return -1;
        }
        /**
         * 就算当前app在前端也显示
         */
        switch (SettingsManager.getInstances().getBuilder().getNotificationSettings(false).getMode()) {
            case Builder.NotificationMode.APP_NONE_MODE:
                return -1;
            case Builder.NotificationMode.APP_BACKGROUND_MODE:
                ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
                    @Override
                    public void run() {
                        vibrator.vibrate(500);
//                        vibrator.cancel();
                    }
                });
                if (AppUtil.isAppInForeground(IMSdk.CONTEXT) && vibrator != null) {
                    return -1;
                }
            case Builder.NotificationMode.APP_AUTO_MODE:
                break;
        }
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(IMSdk.CONTEXT)
                    .load(iMsg.getMemberAvatarUrl())
                    .asBitmap() //必须
                    .centerCrop()
                    .into(300, 300)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(IMSdk.CONTEXT);
        notificationBuilder.setContentTitle("手游视界");
        notificationBuilder.setContentText(
                StringUtil.isNull(iMsg.getMemberNickName()) ? "手游视界用户 " : iMsg.getMemberNickName() +
                        "说：" + iMsg.getContent());
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
        Intent intent = new Intent(IMSdk.CONTEXT, ChatActivity.class);    //点击通知进入的界面
        intent.putExtra("receiverID", iMsg.getMemberId());
        intent.putExtra("receiverNickName", iMsg.getMemberNickName());
        intent.putExtra("receiverAvatarUrl", iMsg.getMemberAvatarUrl());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(IMSdk.CONTEXT, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        Notification notification = notificationBuilder.build();
        notificationManagerServier.notify(new Integer(iMsg.getId() + ""), notification);
        addNotifycation(iMsg);
        return new Integer(iMsg.getId() + "");
    }

    @Override
    public void notifyMessageNotification2(final IChatMsg iMsg) {
        final AccountModel[] accountModels = {null};

        Access.runCustomThread(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                AccountModel accountModel = Business.getInstances().queryLineByWhere(sqLiteDatabase, AccountModel.class,
                        Fields.AccounFields.MEMBER_ID + " = " + iMsg.getMemberId(), null);
                if (accountModel == null) {
                    accountModel = new AccountModel();
                    accountModel.setMemberId(iMsg.getMemberId());
                    Business.getInstances().insert(sqLiteDatabase, accountModel);
                }
                accountModels[0] = accountModel;
            }

            @Override
            public void onExternalError() {

            }
        });

        if (accountModels[0] != null && !StringUtil.isNull(accountModels[0].getNickName())
                && !StringUtil.isNull(accountModels[0].getAvatarUrl())) {
            iMsg.setMemberNickName(accountModels[0].getNickName());
            iMsg.setMemberAvatarUrl(accountModels[0].getAvatarUrl());
        } else {
            try {
                MemberInfoRespones response = Account.getMemberInfo(IMSdk.CONTEXT, iMsg.getMemberId());
                if (response.getCode() == 200) {
                    iMsg.setMemberAvatarUrl(response.getAvatarUrl());
                    iMsg.setMemberNickName(response.getNickname());
                }
                final AccountModel insertModel = new AccountModel();
                insertModel.setNickName(iMsg.getMemberNickName());
                insertModel.setMemberId(iMsg.getMemberId());
                insertModel.setAvatarUrl(iMsg.getMemberAvatarUrl());
                Access.runCustomThread(new Execute() {
                    @Override
                    public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                        Business.getInstances().insert(sqLiteDatabase, insertModel);
                        IMSdk.CONTEXT.getContentResolver().notifyChange(InformationProvide.CONTENT_URI, null);
                        IMSdk.CONTEXT.getContentResolver().notifyChange(ChatProvider.CONTENT_URI, null);
                    }

                    @Override
                    public void onExternalError() {
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (Proxy.getAccountManger().getUserMemberId() != null && !Proxy.getAccountManger().getUserMemberId().equals(iMsg.getMemberId())) {
            notifyChatMessageNotification(iMsg);
        }
    }

    /**
     * im系统简单的通知
     *
     * @param headLineModel
     */
    @Override
    public void notifyHeadLineNotifycation(HeadLineModel headLineModel) {

        if (notifyObservableSet != null) {
            final Intent intent = notifyObservableSet.subscribe(headLineModel);

            if (intent == null) {
                return;
            }

            while (true) {
                int id = (int) (Math.random() * 1_000_000) + 100_000;
                if (headLineMsgArray.get(id) == null){
                    headLineModel.setId(id);
                    headLineMsgArray.put(id,headLineModel.clone());
                    break;
                }
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(IMSdk.CONTEXT);
            notificationBuilder.setContentTitle("手游视界");
            notificationBuilder.setContentText(headLineModel.getContent());
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(IMSdk.CONTEXT.getResources(), getIcon()));
            notificationBuilder.setWhen(System.currentTimeMillis());
            notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
            notificationBuilder.setTicker("简讯");
            notificationBuilder.setSmallIcon(getIcon());
            notificationBuilder.setVibrate(new long[]{0, VIBRATION_DURATION});
            notificationBuilder.setSound(getSound());
            notificationBuilder.setLights(0xFF0000, 3000, 3000);
            notificationBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            PendingIntent contentIntent = PendingIntent.getActivity(IMSdk.CONTEXT, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            notificationBuilder.setContentIntent(contentIntent);
            Notification notification = notificationBuilder.build();
            notificationManagerServier.notify(new Integer(headLineModel.getId() + ""), notification);
        }
    }

    @Override
    public void setNotifyObservable(NotifyObservable notifyObservable) {
        notifyObservableSet = notifyObservable;
    }

    @Override
    public void removeNotifyObservable() {
        notifyObservableSet = null;
    }

    /**
     * 检验此消息的 Imwindow 是否已经打开
     *
     * @param m
     * @return
     */
    private boolean cheackShowNotification(IChatMsg m) {
        final IMWindow imWindow = ChatWindowsManager.getInstances().getLastWindow();
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
     *
     * @param msgBean
     */
    @Deprecated
    private void addNotifycation(IChatMsg msgBean) {
        if (messageNotifications.containsKey(msgBean.getMemberId())) {
            List<IChatMsg> list = messageNotifications.get(msgBean.getMemberId());
            list.add(msgBean);
        } else {
            final LinkedList<IChatMsg> list = new LinkedList<>();
            messageNotifications.put(msgBean.getMemberId(), list);
            list.add(msgBean);
        }
    }


    @Override
    public Map getNotifications() {
        return messageNotifications;
    }

    /**
     * 清空所有根据具体发送者的notifycation
     *
     * @return
     */
    @Override
    public boolean canClearNotifications() {

        final IMWindow imWindow = ChatWindowsManager.getInstances().getAllIMWindows().getLast();
        switch (imWindow.getType()) {
            case IMWindow.CHAT_TYPE:
                List<IChatMsg> linkedList = messageNotifications.get(imWindow.getReceiver());
                if (linkedList != null) {
                    for (IChatMsg msgBean : linkedList) {
                        notificationManagerServier.cancel(new Integer(msgBean.getId() + ""));
                    }
                    linkedList.clear();
                }
                break;
            case IMWindow.MUCCHAT_TYPE:
                break;
        }
        return false;
    }

    /**
     * 清空notifycation
     */
    @Override
    public void clearNotifications() {
        for (String key : messageNotifications.keySet()) {
            for (IChatMsg msgBean : messageNotifications.get(key)) {
                notificationManagerServier.cancel(new Integer(msgBean.getId() + ""));
            }
        }
        messageNotifications.clear();
        for(Integer integer : headLineMsgArray.keySet()){
            notificationManagerServier.cancel(integer);
        }
        headLineMsgArray.clear();
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

    @Override
    public void leaveIM() {
        clearNotifications();
    }

    @Override
    public void leaveErrorIM() {
        leaveIM();
    }
}
