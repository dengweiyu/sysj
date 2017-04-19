package com.li.videoapplication.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.mvp.Constant;
import com.li.videoapplication.tools.ToastHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 功能：应用系统工具
 */
public class AppUtil {

    /**
     * 功能：获取当前进程名称
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 功能：获取手机机型信息
     */
    public static String getPhoneInfo(Context context) {
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = android.os.Build.MODEL;
        String all = imei + "," + imsi + "," + mtype;
        return all;
    }

    /**
     * 功能：获取应用版本号
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    //获取版本号(内部识别号)
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 应用是否处于后台
     *
     * @return true:处于后台 false:处于前台
     */
    public static boolean isAppBackground() {
        Context context = AppManager.getInstance().getContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                // 后台
                return true;
            }
        }
        // 前台
        return false;
    }

    //在进程中去寻找当前APP的信息，判断是否在前台运行
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 尝试获取权限,成功返回true，否则返回false
     */
    public static boolean appRoot() {
        // 检测是否ROOT过
        DataInputStream stream;
        boolean flag = false;
        try {
            stream = terminal("ls /data/");
            // 目录哪都行，不一定要需要ROOT权限的
            if (stream.readLine() != null)
                flag = true;
            // 根据是否有返回来判断是否有root权限
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static DataInputStream terminal(String command) throws Exception {
        Process process = Runtime.getRuntime().exec("su");
        // 执行到这，Superuser会跳出来，选择是否允许获取最高权限
        OutputStream outstream = process.getOutputStream();
        DataOutputStream DOPS = new DataOutputStream(outstream);
        InputStream instream = process.getInputStream();
        DataInputStream DIPS = new DataInputStream(instream);
        String temp = command + "\n";
        // 加回车
        DOPS.writeBytes(temp);
        // 执行
        DOPS.flush();
        // 刷新，确保都发送到outputstream
        DOPS.writeBytes("exit\n");
        // 退出
        DOPS.flush();
        process.waitFor();
        return DIPS;
    }

    /**
     * 判断当前手机系统版本
     */
    public static int getAndroidSDKVersion() {
        int version = 1;
        try {
            version = android.os.Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static void startQQChat(Context context,String qqID){
        if (isQQAvailable(context)) {
            String url = Constant.QQURL + qqID;
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } else {
            ToastHelper.s(R.string.share_qq_client_inavailable);
        }
    }

    public static boolean isQQAvailable(Context context) {
        final PackageManager mPackageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = mPackageManager.getInstalledPackages(0);
        for (PackageInfo info : installedPackages) {
            String packageName = info.packageName;
            if (packageName.equals("com.tencent.mobileqq")) {
                return true;
            }
        }
        return false;
    }

    /**
     *判断当前网络是否正常
     */
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 模拟Home键
     */
    public static void simulateHomeKey(Activity activity){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(intent);
    }
}
