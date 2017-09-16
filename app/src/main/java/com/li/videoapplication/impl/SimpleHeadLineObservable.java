package com.li.videoapplication.impl;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.ifeimo.im.common.bean.model.HeadLineModel;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.notification.NotifyHeadLineObservable;
import com.li.videoapplication.R;
import com.li.videoapplication.data.model.entity.IMPushCustomEntity;
import com.li.videoapplication.data.model.event.RefreshAboutOrderEvent;
import com.li.videoapplication.mvp.match.view.PlayWithAndMatchActivity;
import com.li.videoapplication.tools.ToastHelper;
import com.li.videoapplication.ui.activity.CommentPlayWithOrderActivity;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.ui.activity.PlayWithOrderDetailActivity;
import com.li.videoapplication.ui.activity.RefundApplyActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.rong.eventbus.EventBus;

/**
 * IM推送回调
 */

public class SimpleHeadLineObservable implements NotifyHeadLineObservable {
    private final static int WHAT_RECEIVE_MESSAGE = 10;
    private Handler mHandler;
    private Context mContext;
    private int mNotifyId = 1111;
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    public SimpleHeadLineObservable(Context context) {
        mContext = context;
        mHandler = new Handler(context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == WHAT_RECEIVE_MESSAGE){
                    SimpleHeadLineObservable.this.handlerMessage(msg.getData());
                }
            }
        };
    }



    @Override
    public Intent subscribe(HeadLineModel headLineModel) {
        //
        Message msg = new Message();
        msg.what = WHAT_RECEIVE_MESSAGE;
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",headLineModel);
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        return null;
    }

    private void handlerMessage(Bundle bundle){
        if (bundle == null){
            return;
        }

        HeadLineModel model = (HeadLineModel) bundle.getSerializable("data");
        if (model == null){
            return;
        }

        //
        if (StringUtil.isNull(model.getExtras())){
            return;
        }

        IMPushCustomEntity entity = null;

        Gson gson = new Gson();
        try {
            entity = gson.fromJson(model.getExtras(),IMPushCustomEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (entity == null){
            return;
        }

        if (model.getMode() != null){
            switch (model.getMode()){
                case "notify_image":break;
                case "notify_text":
                    notifyText(model.getSubject(),model.getBody(),entity);
                    break;
                case "dialog_text":break;
                case "dialog_image":break;
                case "toast_text":
                    ToastHelper.l(model.getBody());
                    break;
                case "secret_text":
                    secretText(entity);
                    break;
            }
        }
    }

    /**
     * 处理通知栏消息
     * @param subject
     * @param body
     * @param entity
     */
    private void notifyText(String subject,String body,IMPushCustomEntity entity){
        Notification notification = new NotificationCompat
                .Builder(mContext)
                .setTicker("手游视界")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentText(body)
                .setContentTitle(subject)
                .setContentIntent(createIntent(entity))
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.logo_round))
                .setSmallIcon(R.drawable.sysj_logo_gray).build();
        NotificationManagerCompat.from(mContext).notify(mNotifyId,notification);
        mNotifyId++;

        if (!isNotificationEnable(mContext)){
            ToastHelper.l("收到新消息，请在\"设置\"中为\"手游视界\"开启通知栏权限");
        }
    }

    /**
     * 处理透传消息
     * @param entity
     */
    private void secretText(IMPushCustomEntity entity){
        if (entity == null){
            return;
        }

        switch (entity.getModule()){
            case "Training":
                //
                if (!StringUtil.isNull(entity.getAction())){
                    switch (entity.getAction()){

                        case "Refresh":                                                //刷新订单相关的页面
                            EventBus.getDefault().post(new RefreshAboutOrderEvent());
                            break;
                    }
                }
                break;
        }

    }

    /**
     *
     * @param entity
     * @return
     */
    private PendingIntent createIntent(IMPushCustomEntity entity){

        Intent intent = new Intent();
        PendingIntent pi = null;
        Class c = MainActivity.class;
        if (!StringUtil.isNull(entity.getAction())){
            switch (entity.getAction()){
                case "ShowOrderDetail":             //订单详情页
                    c = PlayWithOrderDetailActivity.class;
                    break;
                case "ShowEvaluatePage":             //用户评价页
                    c = CommentPlayWithOrderActivity.class;
                    break;
                case "ShowRefundPage":               //退款页面
                    c = RefundApplyActivity.class;
                    break;
                case "ShowGrabOrderList":
                case "ShowTakeOrderList":             //接单列表
                    c = PlayWithAndMatchActivity.class;
                    intent.putExtra("position",0);
                    intent.putExtra("order_position",1);
                    break;
            }
        }

        intent.setClass(mContext,c);
        if (entity.getModule() != null && entity.getParameter() != null){
            //add extras
            switch (entity.getModule()){
                case "Training":
                    if (!StringUtil.isNull(entity.getParameter().getOrder_id())){
                        intent.putExtra("order_id",entity.getParameter().getOrder_id());
                    }
                    intent.putExtra("role",entity.getParameter().getRole());
                    if (!StringUtil.isNull(entity.getParameter().getNickname())){
                        intent.putExtra("nick_name",entity.getParameter().getNickname());
                    }
                    if (!StringUtil.isNull(entity.getParameter().getAvatar())){
                        intent.putExtra("avatar",entity.getParameter().getAvatar());
                    }
                    if (!StringUtil.isNull(entity.getParameter().getScore())){
                        intent.putExtra("score",entity.getParameter().getScore());
                    }
                    if (!StringUtil.isNull(entity.getParameter().getCoach_id())){
                        intent.putExtra("coach_id",entity.getParameter().getCoach_id());
                    }

                    break;
            }
        }

        if (Activity.class.isAssignableFrom(c)){

            //为什么 requestCode 也是动态的
            //If you use intent extras, remeber to call PendingIntent.getActivity() with the flag PendingIntent.FLAG_UPDATE_CURRENT, otherwise the same extras will be reused for every notification
            //
            pi = PendingIntent.getActivities(mContext,mNotifyId,new Intent[]{intent},PendingIntent.FLAG_ONE_SHOT);
        }else if (Service.class.isAssignableFrom(c)){

        }
        return pi;
    }

    /**
     * 是否启用通知栏
     */
    private boolean isNotificationEnable(Context context){

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        ApplicationInfo appInfo = context.getApplicationInfo();

        String pkg = context.getApplicationContext().getPackageName();

        int uid = appInfo.uid;

        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

        try {

            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int)opPostNotificationValue.get(Integer.class);

            return ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
