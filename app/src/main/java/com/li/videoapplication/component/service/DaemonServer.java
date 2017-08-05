package com.li.videoapplication.component.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.push.example.BaiduPush;


/**
 * 守护服务
 */

public class DaemonServer extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BaiduPush.getInstances().onCreate(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        // 启动服务
        AppStartService.startAppStartService();
        super.onDestroy();
    }


}
