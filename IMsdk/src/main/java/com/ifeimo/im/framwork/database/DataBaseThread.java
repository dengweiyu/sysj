package com.ifeimo.im.framwork.database;

import android.content.ContentResolver;
import android.content.Context;

import com.ifeimo.im.common.bean.AccountBean;
import com.ifeimo.im.provider.ChatProvider;
import com.ifeimo.im.provider.MuccProvider;

import java.util.concurrent.Semaphore;

/**
 * Created by lpds on 2017/1/18.
 */
public class DataBaseThread {

    private ContentResolver resolver;

    public DataBaseThread(ContentResolver resolver) {
        this.resolver = resolver;
    }

    public void clearAllChatMsg() {
        resolver.delete(ChatProvider.CONTENT_URI, null, null);
    }

    public void clearAllMuccMsg() {
        resolver.delete(MuccProvider.CONTENT_URI, null, null);
    }

    public void clearChatMsg() {
        final long time5 = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 1;
        resolver.delete(ChatProvider.CONTENT_URI,
                String.format("%s < ?", Fields.ChatFields.CREATE_TIME), new String[]{time5 + ""});
    }

    public void clearMuccMsg() {

//        final long time5 = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 1;
//
//        resolver.delete(MuccProvider.CONTENT_URI,
//                String.format("%s < ?", MuccProvider.DB_CREATE_TIME), new String[]{time5 + ""});
    }

    public void checkRoomWaitingType(String roomid){




    }



}
