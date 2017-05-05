package com.ifeimo.im.framwork.connect;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ifeimo.im.OnInitialization;
import com.ifeimo.im.common.bean.model.IMsg;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.interface_im.IConnect;
import com.ifeimo.im.service.LoginService;

/**
 * Created by lpds on 2017/5/2.
 */
public final class ConnectSupport implements IConnectSupport,OnInitialization{
    private boolean isInit = false;
    private IConnect iConnect;
    public ConnectSupport(IConnect iConnect) {
        this.iConnect = iConnect;
    }

    private final String TAG = "XMPP_ConnectSupport";

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i(TAG, "onActivityResumed: App 回到前端");
        if(isInit) {
            if (!iConnect.isConnect()) {
                Log.i(TAG, "onActivityResumed: App IM 断开连接！");
                activity.startService(new Intent(activity, LoginService.class));
            } else {
                Log.i(TAG, "onActivityResumed: App IM 保持连接！");
            }
        }else{
            isInit = true;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    @Override
    public boolean isInitialized() {
        return isInit;
    }
}
