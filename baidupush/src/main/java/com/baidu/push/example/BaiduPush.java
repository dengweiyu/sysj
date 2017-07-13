package com.baidu.push.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.example.commont.IBaiduPushDispense;
import com.baidu.push.example.entity.BaiduEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lpds on 2017/6/14.
 */
public class BaiduPush{

    public static final String APP_ID = "9818322";
    public static final String APP_KEY = "a3TNVZopGI4pnDfsCv9D8wL6";
    private static BaiduPush baiduPush;
    private Application application;
    private OnSucceed mOnSucceed;
    private BaiduEntity baiduEntity = new BaiduEntity();
    private final String TAG = "BaiduPush";

    static {
        baiduPush = new BaiduPush();
    }

    public void init(Application app,OnSucceed mOnSucceed ) {
        this.mOnSucceed = mOnSucceed;
        application = app;
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                onResume(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
        PushManager.startWork(
                application,
                PushConstants.LOGIN_TYPE_API_KEY,
                APP_KEY);
    }

    public static BaiduPush getInstances() {
        return baiduPush;
    }

    private BaiduPush() {
    }

    BaiduEntity getBaiduEntity(){
        return baiduEntity;
    }

    void succeed() {
        if(this.mOnSucceed!=null){
            Log.i(TAG, "succeed: baiduEntity");
            this.mOnSucceed.succeed(baiduEntity);
        }
    }

    public interface OnSucceed{

        void succeed(BaiduEntity b);

    }

    public void onResume(Context context){
//        if(!PushManager.isPushEnabled(context)) {
            Log.i(TAG, "onResume: resumeWork");
            PushManager.resumeWork(context);
//        }
    }

    public void onStop(Context context){
//        if(PushManager.isPushEnabled(context)) {
            Log.i(TAG, "onStop: stopWork");
//            PushManager.stopWork(context);
//        }
    }

}
