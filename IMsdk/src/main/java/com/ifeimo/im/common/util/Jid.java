package com.ifeimo.im.common.util;

import android.content.Context;

import com.ifeimo.im.common.util.PManager;

/**
 * Created by lpds on 2017/1/11.
 */
public class Jid {


    public static String getJid(Context context,String account){
        return account+"@"+ PManager.getConnectConfig(context).getServiceName();
    }

    public static String getRoomJ(Context context,String roomID){
            return roomID+"@conference."+PManager.getConnectConfig(context).getServiceName();
    }

}
