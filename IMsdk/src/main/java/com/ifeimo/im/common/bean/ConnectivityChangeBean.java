package com.ifeimo.im.common.bean;

import android.net.NetworkInfo;

/**
 * Created by lpds on 2017/2/10.
 */
public class ConnectivityChangeBean {

    private NetworkInfo networkInfo;

    public NetworkInfo getNetworkInfo() {
        return networkInfo;
    }

    public void setNetworkInfo(NetworkInfo networkInfo) {
        this.networkInfo = networkInfo;
    }
}
