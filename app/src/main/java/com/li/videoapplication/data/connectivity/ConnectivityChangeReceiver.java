package com.li.videoapplication.data.connectivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.li.videoapplication.data.EventManager;
import com.li.videoapplication.framework.BaseReceiver;

/**
 * 广播：监听网络状态，显示断网提示
 */
public class ConnectivityChangeReceiver extends BaseReceiver {

    public void onReceive(Context context, Intent intent) {
        try {
            Log.i(tag, "connectivity=" + intent);
            Log.i(tag, "connectivity=" + intent.getAction());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String action = intent.getAction();
		if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            EventManager.postConnectivityChangeEvent(context);
		}
	}
}
