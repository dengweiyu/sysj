package com.li.videoapplication.data.model.event;

import android.net.NetworkInfo;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 事件：网络变化
 */
@SuppressWarnings("serial")
public class ConnectivityChangeEvent extends BaseEntity {

    private NetworkInfo networkInfo;

    public NetworkInfo getNetworkInfo() {
        return networkInfo;
    }

    public void setNetworkInfo(NetworkInfo networkInfo) {
        this.networkInfo = networkInfo;
    }
}
