package com.ifeimo.screenrecordlib.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.ifeimo.screenrecordlib.RecordingManager;
import com.ifeimo.screenrecordlib.util.Utils;

/**
 * 服务：启动服务
 */
public class AppStartService extends IntentService {

    public static final String TAG = AppStartService.class.getSimpleName();

    /**
     * 启动服务
     */
    public static void startAppStartService() {
        Log.d(TAG, "startAppStartService: // -------------------------------------------------------------");
        Context context = RecordingManager.getInstance().context();
        Intent intent = new Intent(context, AppStartService.class);
        context.startService(intent);
    }

    public AppStartService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: // -------------------------------------------------------------");
        initialization();
    }

    /**
     * 初始化
     */
    private void initialization() {
        Log.d(TAG, "initialization: // -------------------------------------------------------------");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {// 4.4以上，5.0以下
            // 将录屏核心复制到相应文件夹（4.4以上）
            try {
                Utils.copyScreenrecord();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 将核心复制到相应文件夹
            try {
                Utils.copyBusybox();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {// 4.4以下
            try {
                Utils.uninstallCore();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 将录屏核心复制到相应文件夹（4.4以下）
            try {
                Utils.copyFfmpegV2sh();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 将核心复制到相应文件夹
            try {
                Utils.copyBusybox();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
