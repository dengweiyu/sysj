package com.li.videoapplication.tools;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.li.videoapplication.data.local.FileOperateUtil;
import com.li.videoapplication.data.local.StorageUtil;

import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.activity.MainActivity;

import java.io.File;

/**
 * @author liuwei
 * 处理未捕获异常
 * 能够做到不Crash
 */

public class AppExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static AppExceptionHandler sInstance = null;
    public final static String ERROR_MSG = "error_msg";

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private AppExceptionHandler(){

    }

    public static AppExceptionHandler getInstance(){
        if (sInstance == null){
            sInstance = new AppExceptionHandler();
        }
        return sInstance;
    }

    public void init(){
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        //以下代码保证不会直接闪退
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (;;){
                    try {
                        //启动循环
                        Looper.loop();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        uncaughtException(Looper.getMainLooper().getThread(),e);
                    }
                }
            }
        });
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable)){
            mDefaultHandler.uncaughtException(thread,throwable);
        }else {
            //kill current activity
            AppManager.getInstance().removeCurrentActivity();
            //call gc
            System.gc();
        }
    }


    /**
     * handle exception
     * @param throwable
     * @return
     *
     */
    private boolean handleException(final Throwable throwable){
        if (throwable == null){
            return false;
        }
        final Context context = AppManager.getInstance().getContext();
        if (throwable != null){
            Log.e("Message","{"+throwable.getMessage()+"}");
            throwable.printStackTrace();
        }
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context,"很抱歉，程序出现了一点问题~",Toast.LENGTH_SHORT).show();
                saveLog(throwable.getMessage());
                Looper.loop();
            }
        }.start();
        //can record exception message in here
      //  restartApp(throwable.getMessage());
        return true;
    }

    /**
     * will be restart main activity
     */
    @Deprecated
    private  void restartApp(String message){
        Application context = AppManager.getInstance().getApplication();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (message != null){
            intent.putExtra(ERROR_MSG,message);         //maybe new thread can not running
        }
        PendingIntent pi = PendingIntent.getActivities(context,0,new Intent[]{intent},PendingIntent.FLAG_ONE_SHOT);

        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC,System.currentTimeMillis(),pi);
    }

    /**
     *
     * @param message
     */
    public static void saveLog(final String message){
        if (message == null){
            return;
        }
        try {
            File logDir = new File(StorageUtil.getInner()+"/sysj/logs");
            if (!logDir.exists()){
                logDir.mkdir();
            }
            FileOperateUtil.save2File(message.getBytes(),StorageUtil.createLogName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
