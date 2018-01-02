package com.li.videoapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.tools.ToastHelper;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 功能：网络工具
 */
public class NetUtil {

    /**
     * 功能：检查网络链接的状态
     */
    public static boolean isConnect() {
        if (AppManager.getInstance().getApplication() == null) {
            return false;
        }
        try {
            return isConnect(AppManager.getInstance().getApplication());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 功能：检查网络链接的状态
     */
    public static boolean isConnect(Context context) throws Exception {
        // 获取手机所有连接管理对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            // 判断当前网络是否已经连接
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前ip地址
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        Log.i("NetUtil", "getLocalIpAddress: " + inetAddress.getHostAddress());
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            LogHelper.e("NetUtil", "获取本地ip地址失败");
        }
        return "";
    }

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    /**
     * 是否是WIFI
     */
    public static boolean isWIFI() {
        int networkType = getNetworkType();
        if (networkType == 1)
            return true;
        return false;
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType() {
        Context context = AppManager.getInstance().getContext();
        return getNetworkType(context);
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType(Context context) {
        // 获取当前网络环境信息
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // 对网络环境进行判断
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETTYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NETTYPE_CMNET;
            }
        }
        return 0;
    }

    /**
     * 检查网络返回数据转化后是否为空。
     * @param o
     * @return true：不为空 false:为空并提示
     */
    public static boolean checkNCallBackData (Object o) {
        if (o == null) {
            ToastHelper.s("返回的数据为空..服务器的锅");
            return false;
        }
        return true;
    }

}
