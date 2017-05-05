package com.ifeimo.im.framwork;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
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
import com.ifeimo.im.R;
import com.ifeimo.im.activity.ChatActivity;
import com.ifeimo.im.common.bean.AccountBean;
import com.ifeimo.im.common.bean.model.AccountModel;
import com.ifeimo.im.common.bean.model.IMsg;
import com.ifeimo.im.common.util.AppUtil;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.interface_im.IMWindow;
import com.ifeimo.im.framwork.notification.NotificationManager;
import com.ifeimo.im.framwork.notification.NotifyBean;
import com.ifeimo.im.framwork.request.Account;
import com.ifeimo.im.framwork.setting.Builder;
import com.ifeimo.im.provider.ChatProvider;
import com.ifeimo.im.provider.InformationProvide;

import org.jivesoftware.smack.chat.Chat;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import y.com.sqlitesdk.framework.business.Business;
import y.com.sqlitesdk.framework.business.CenterServer;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/1/16.
 * <p/>
 * 管理消息推送
 */
final class IMNotificationManager implements NotificationManager<NotifyBean> {
    private static final String TAG = "XMPP_Notification";
    private static IMNotificationManager notificationManager;
    private android.app.NotificationManager notificationManagerServier;
    private static final long VIBRATION_DURATION = 500;
    private Map<String, List<IMsg>> messageNotifications;
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
     *
     * @return
     */
    private int notifyMessageNotification(IMsg iMsg) {
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
        if (!SettingsManager.getInstances().getBuilder().getNotificationSettings(false).getMode().equals(Builder.Notification.AUTO_MODE)) {
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
                    .load(iMsg.getMemberAvatarUrl())
                    .asBitmap() //必须
                    .fitCenter()
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
    public void notifyMessageNotification2(final IMsg iMsg) {
        final AccountModel[] accountModels = {null};

        Access.runCustomThread(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                AccountModel accountModel = Business.getInstances().queryLineByWhere(sqLiteDatabase, AccountModel.class,
                        Fields.AccounFields.MEMBER_ID + " = " + iMsg.getMemberId(), null);
                if (accountModel  == null) {
                    accountModel = new AccountModel();
                    accountModel.setMemberId(iMsg.getMemberId());
                    Business.getInstances().insert(sqLiteDatabase,accountModel);
                }
                accountModels[0] = accountModel;
            }

            @Override
            public void onExternalError() {

            }
        });

        if (accountModels[0] != null && !StringUtil.isNull(accountModels[0].getNickName())
                && !StringUtil.isNull(accountModels[0].getNickName())) {
            iMsg.setMemberNickName(accountModels[0].getNickName());
            iMsg.setMemberAvatarUrl(accountModels[0].getAvatarUrl());
        } else {
            try {
                if (StringUtil.isNull(iMsg.getMemberAvatarUrl()) || StringUtil.isNull(iMsg.getMemberNickName())) {
                    Response response = Account.getMemberInfo(IMSdk.CONTEXT, iMsg.getMemberId());
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.i(TAG, "------ 获取到的JSon" + jsonObject + " ------");
                    if (jsonObject.getInt("code") == 200) {
                        iMsg.setMemberAvatarUrl(jsonObject.getString("avatarUrl"));
                        iMsg.setMemberNickName(jsonObject.getString("nickname"));
                    }
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(Fields.AccounFields.MEMBER_ID, iMsg.getMemberId());
                contentValues.put(Fields.AccounFields.MEMBER_NICKNAME, iMsg.getMemberNickName());
                contentValues.put(Fields.AccounFields.MEMBER_AVATARURL, iMsg.getMemberAvatarUrl());

                final AccountModel insertModel = new AccountModel();
                insertModel.setNickName(iMsg.getMemberNickName());
                insertModel.setMemberId(iMsg.getMemberId());
                insertModel.setAvatarUrl(iMsg.getMemberAvatarUrl());
//                CenterServer.getInstances().insert(insertModel);


                Access.runCustomThread(new Execute() {
                    @Override
                    public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                        Business.getInstances().insert(sqLiteDatabase,insertModel);
                        IMSdk.CONTEXT.getContentResolver().notifyChange(InformationProvide.CONTENT_URI,null);
                        IMSdk.CONTEXT.getContentResolver().notifyChange(ChatProvider.CONTENT_URI,null);
                    }

                    @Override
                    public void onExternalError() {

                    }
                });

                notifyMessageNotification(iMsg);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 检验此消息的 Imwindow 是否已经打开
     *
     * @param m
     * @return
     */
    private boolean cheackShowNotification(IMsg m) {
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
     *
     * @param msgBean
     */
    @Deprecated
    private void addNotifycation(IMsg msgBean) {
        if (messageNotifications.containsKey(msgBean.getMemberId())) {
            List<IMsg> list = messageNotifications.get(msgBean.getMemberId());
            list.add(msgBean);
        } else {
            final LinkedList<IMsg> list = new LinkedList<>();
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
     *
     * @return
     */
    @Override
    public boolean canClearNotifications() {

        final IMWindow imWindow = ChatWindowsManager.getInstences().getAllIMWindows().getLast();
        switch (imWindow.getType()) {
            case IMWindow.CHAT_TYPE:
                List<IMsg> linkedList = messageNotifications.get(imWindow.getReceiver());
                if (linkedList != null) {
                    for (IMsg msgBean : linkedList) {
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
