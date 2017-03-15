package com.ifeimo.im.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ifeimo.im.common.bean.MsgBean;
import com.ifeimo.im.common.bean.MuccMsgBean;
import com.ifeimo.im.framwork.database.DataBaseThread;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.provider.BaseProvider;
import com.ifeimo.im.provider.ChatProvider;
import com.ifeimo.im.provider.MuccProvider;

/**
 * Created by lpds on 2017/1/11.
 */
public class MsgService extends Service {

    public static ContentResolver contentResolver;

    private DataBaseThread dataBaseThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        contentResolver = getContentResolver();
//        dataBaseThread = new DataBaseThread(contentResolver);
//        dataBaseThread.clearChatMsg();
//        dataBaseThread.clearMuccMsg();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contentResolver = null;
    }

    public static void insertMucc(MuccMsgBean muccMsgBean) {
        ContentValues values = new ContentValues();
        values.put(Fields.GroupChatFields.MEMBER_ID, muccMsgBean.getMemberId());
        values.put(Fields.AccounFields.MEMBER_NICKNAME, muccMsgBean.getMemberNickName());
        values.put(Fields.AccounFields.MEMBER_AVATARURL, muccMsgBean.getMemberAvatarUrl());
        values.put(Fields.GroupChatFields.CONTENT, muccMsgBean.getContent());
        values.put(Fields.GroupChatFields.SEND_TYPE, muccMsgBean.getSendType());
        values.put(Fields.GroupChatFields.CREATE_TIME, muccMsgBean.getCreateTime());
        values.put(Fields.GroupChatFields.ROOM_ID, muccMsgBean.getRooomID());
        values.put(Fields.GroupChatFields.MSG_ID, muccMsgBean.getMsgId());
        contentResolver.insert(MuccProvider.CONTENT_URI, values);
    }

    public static void upDataMucc(MsgBean msgBean) {
        ContentValues values = new ContentValues();
        values.put(Fields.GroupChatFields.SEND_TYPE, msgBean.getSendType());
        contentResolver.update(MuccProvider.CONTENT_URI, values,
                String.format(" %s = ? and %s = ? and %s != %s",
                        Fields.GroupChatFields.MSG_ID, Fields.GroupChatFields.ROOM_ID,Fields.GroupChatFields.SEND_TYPE,Fields.MsgFields.SEND_FINISH),
                new String[]{msgBean.getMsgId(),((MuccMsgBean)msgBean).getRooomID()});
    }

    public static void insertChat(MsgBean msgBean) {
        ContentValues values = new ContentValues();
        values.put(Fields.ChatFields.MEMBER_ID, msgBean.getMemberId());
        values.put(Fields.ChatFields.RECEIVER_ID, msgBean.getReceiverId());
        values.put(Fields.ChatFields.CONTENT, msgBean.getContent());
        values.put(Fields.ChatFields.SEND_TYPE, msgBean.getSendType());
        values.put(Fields.ChatFields.CREATE_TIME, msgBean.getCreateTime());
        values.put(Fields.ChatFields.MSG_ID,msgBean.getMsgId());
        contentResolver.insert(ChatProvider.CONTENT_URI, values);
        msgBean.setId(values.getAsInteger(Fields.ChatFields.ID));
    }

    public static void upDataChat(MsgBean msgBean) {
        ContentValues values = new ContentValues();
        values.put(Fields.ChatFields.MEMBER_ID, msgBean.getMemberId());
        values.put(Fields.ChatFields.RECEIVER_ID, msgBean.getReceiverId());
        values.put(Fields.ChatFields.CONTENT, msgBean.getContent());
        values.put(Fields.ChatFields.SEND_TYPE, msgBean.getSendType());
        values.put(Fields.ChatFields.CREATE_TIME, msgBean.getCreateTime());
        values.put(Fields.ChatFields.MSG_ID,msgBean.getMsgId());
        contentResolver.update(ChatProvider.CONTENT_URI, values,
                String.format(" %s = %s ", Fields.ChatFields.ID, msgBean.getId()), null);
    }

    public void deleteChat(MsgBean msgBean) {
        contentResolver.delete(ChatProvider.CONTENT_URI,
                String.format(" %s = '?' and %s = '?' and %s = '?' and %s = '?' ",
                        Fields.ChatFields.MEMBER_ID, Fields.ChatFields.CONTENT,
                        Fields.ChatFields.RECEIVER_ID, Fields.ChatFields.CREATE_TIME),
                new String[]{
                        msgBean.getMemberId(), msgBean.getContent(),
                        msgBean.getReceiverId(), msgBean.getCreateTime()
                });
    }

    public static void deleteMucc(MuccMsgBean msgBean) {
        contentResolver.delete(ChatProvider.CONTENT_URI,
                String.format(" %s = '?' and %s = '?' and %s = '?' and %s = '?' ",
                        Fields.GroupChatFields.MEMBER_ID, Fields.GroupChatFields.CONTENT,
                        Fields.GroupChatFields.ROOM_ID, Fields.GroupChatFields.CREATE_TIME),
                new String[]{
                        msgBean.getMemberId(), msgBean.getContent(),
                        msgBean.getRooomID(), msgBean.getCreateTime()
                });
    }

    public static void deleteMuccById(MuccMsgBean msgBean) {
        contentResolver.delete(MuccProvider.CONTENT_URI,
                String.format(" %s = ? ",
                        Fields.GroupChatFields.ID),
                new String[]{
                        msgBean.getId() + ""});
    }
}
