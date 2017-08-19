package com.li.videoapplication.impl;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.TextView;

import com.ifeimo.im.common.bean.model.HeadLineModel;
import com.ifeimo.im.framwork.notification.NotifyHeadLineObservable;
import com.li.videoapplication.R;
import com.li.videoapplication.tools.ToastHelper;

/**
 * IM推送回调
 */

public class SimpleHeadLineObservable implements NotifyHeadLineObservable {
    private final static int WHAT_RECEIVE_MESSAGE = 10;
    private Handler mHandler;
    private Context mContext;
    private int mNotifyId = 1111;
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

        if (model.getMode() != null){
            switch (model.getMode()){
                case "notify_image":break;
                case "notify_text":
                    notifyText(model.getSubject(),model.getBody());
                    break;
                case "dialog_text":break;
                case "dialog_image":break;
                case "toast_text":
                    ToastHelper.l(model.getBody());
                    break;
                case "secret_text":

                    break;
            }
        }
    }

    private void notifyText(String subject,String body){
        Notification notification = new NotificationCompat
                .Builder(mContext)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentText(body)
                .setContentTitle(subject)
                .setSmallIcon(R.drawable.logo_round)
                .setSmallIcon(R.drawable.logo_round).build();
        NotificationManagerCompat.from(mContext).notify(mNotifyId,notification);
        mNotifyId++;
    }

    private void notifyImage(){

    }
}
