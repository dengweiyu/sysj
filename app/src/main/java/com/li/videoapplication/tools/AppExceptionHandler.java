package com.li.videoapplication.tools;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import com.li.videoapplication.R;
import com.li.videoapplication.data.local.FileOperateUtil;
import com.li.videoapplication.data.local.StorageUtil;

import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.activity.MainActivity;

import java.io.File;

/**
 * Created by liuwei on 2017/3/28 0028.
 */

public class AppExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static AppExceptionHandler sInstance = null;

    private static Long mCrashTime = 0L;

    private AppExceptionHandler(){

    }

    public static AppExceptionHandler getInstance(){
        if (sInstance == null){
            sInstance = new AppExceptionHandler();
        }
        return sInstance;
    }

    public void init(){
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable)){
            System.exit(1);
        }
    }

    /**
     * handle exception
     * @param throwable
     * @return
     *
     */
    private boolean handleException(final Throwable throwable){
        mCrashTime = System.currentTimeMillis();
        //kill all activity
        AppManager.getInstance().removeAllActivity();
        if (throwable != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    ToastHelper.l(R.string.app_exception_tip);
                    saveLog(throwable.getMessage());
                    //can record exception message in here
                    if (isRestartApp()){
                        restartApp();
                    }
                    //kill current process
                    android.os.Process.killProcess(android.os.Process.myPid());
                    Looper.loop();
                }
            }).start();
            return true;
        }
        return false;
    }

    /**
     * if crash duration less 1 min will not be restart app
     */
    private boolean isRestartApp(){
        if (System.currentTimeMillis() - mCrashTime > 1000 * 60){
            return  true;
        }
        return false;
    }

    /**
     * will be restart main activity
     */
    private void restartApp(){
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void saveLog(String message){
        try {
            FileOperateUtil.save2File(message.getBytes(),StorageUtil.createLogName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
