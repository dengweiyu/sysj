package com.ifeimo.im.framwork.message;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lpds on 2017/1/17.
 */
@Deprecated
public class OfflineMessage {

    Map<String,Runnable> waiteMap;
    static Handler handler;
    private static OfflineMessage offlineMessage;
    static {
        offlineMessage = new OfflineMessage();
        new Thread(){

            @Override
            public void run() {
                Looper.prepare();

                handler = new Handler(){

                    @Override
                    public void handleMessage(Message msg) {



                    }
                };

                Looper.loop();
            }
        }.start();
    }


    public OfflineMessage getInstances(){
        return offlineMessage;
    }

    private OfflineMessage(){
        waiteMap = new HashMap<>();
    }




}
