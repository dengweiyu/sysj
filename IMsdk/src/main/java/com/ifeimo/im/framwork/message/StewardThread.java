package com.ifeimo.im.framwork.message;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ifeimo.im.service.MsgService;

/**
 * Created by lpds on 2017/1/16.
 *
 * 处理离线消息
 *
 */
@Deprecated
public class StewardThread extends Thread{

    static StewardThread stewardThread;

    private Context context;
    private Handler handler;

    {
        stewardThread = new StewardThread();

    }


    private StewardThread(){

    }


    public static synchronized void init(Context context){
        if(!stewardThread.isAlive()) {
            stewardThread.context = context;
            stewardThread.start();
        }
    }


    @Override
    public void run() {
        if(handler == null){
            Looper.prepare();
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {




                }
            };
            Looper.loop();
        }
    }
}
