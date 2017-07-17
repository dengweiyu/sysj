package com.ifeimo.im.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ifeimo.im.common.bean.ConnectivityChangeBean;
import com.ifeimo.im.common.util.ConnectUtil;
import com.ifeimo.im.service.LoginService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lpds on 2017/1/11.
 */
public class ConnectReceiver extends BroadcastReceiver {


    public final String TAG = "XMPP_CONNECT";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectUtil.isConnect(context)) {
            context.startService(new Intent(context, LoginService.class));
        }
        if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            //postConnectivityChangeEvent(context);
        }
    }

    private void postConnectivityChangeEvent(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        try {
            Log.i(TAG, "connectivity=" + info);
            Log.i(TAG, "connectivity=" + info.getTypeName());
            Log.i(TAG, "connectivity=" + info.getSubtypeName());
            Log.i(TAG, "connectivity=" + info.getExtraInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (info != null && info.isAvailable()) {
            //AppData.getInstance().setConnectivity(true);
        } else {
            //AppData.getInstance().setConnectivity(false);
        }
        ConnectivityChangeBean event = new ConnectivityChangeBean();
        event.setNetworkInfo(info);
        EventBus.getDefault().post(event);
    }


}
