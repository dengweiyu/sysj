package com.ifeimo.screenrecordlib.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by y on 2017/6/1.
 * 后台打开页面权限工具类（小米）
 */

public class BackgroundStartActivityUtil {
    private final String tag = this.getClass().getSimpleName();

    private boolean isActivityCreate;
    private ActivityCreateListener listener;

    private Handler handler = new Handler(Looper.getMainLooper());

    public interface ActivityCreateListener{
        void createSuccess();
        void createFail();
    }

    public BackgroundStartActivityUtil(ActivityCreateListener listener){
        this.listener = listener;
        isActivityCreate = false;
        // 一秒后如果isActivityCreate=false,即后台打开页面权限没开启
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                synchronized (BackgroundStartActivityUtil.class) {
                    Log.i(tag, "BackgroundStartActivityUtil--->");
                    if (!isActivityCreate) {
                        if (BackgroundStartActivityUtil.this.listener != null)
                            BackgroundStartActivityUtil.this.listener.createFail();
                    }
                }
            }
        }, 1500);
    }

    public void init(){
        synchronized (BackgroundStartActivityUtil.class) {
            Log.i(tag, "init--->");
            isActivityCreate = true;
            if (listener != null) {
                listener.createSuccess();
            }
        }
    }
}
