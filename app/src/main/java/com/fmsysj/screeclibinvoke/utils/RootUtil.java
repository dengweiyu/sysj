package com.fmsysj.screeclibinvoke.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;


import com.li.videoapplication.framework.AppManager;

import java.util.List;

public class RootUtil {

    public static final String TAG = RootUtil.class.getSimpleName();

    /**
     * 获取设备厂商
     */
    public static String getManufacturer() {
        /*
        String version = null;
        BufferedReader reader = null;
        try {// 判断是否MIUI系统
            Process process = Runtime.getRuntime().exec("getprop " + "ro.miui.ui.version.name");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024);
            version = reader.readLine();
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!version.isEmpty()) {// 判断是否小米系统
            return "MIUI";
        }
        if (isInstall("com.huawei.systemmanager")) {// 判断是否华为系统
            return "HUAWEI";
        }
        if (android.os.Build.USER.toString().equals("flyme")) {
            return "MEIZU";
        }*/
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer.equals("HUAWEI"))
            return "HUAWEI";
        if (manufacturer.equals("Xiaomi"))
            return "MIUI";
        if (manufacturer.equals("Meizu"))
            return "MEIZU";
        if (manufacturer.equals("vivo"))
            return "VIVO";
        if (manufacturer.equals("OPPO"))
            return "OPPO";
        return "";
    }

    /**
     * 是否安装某个应用
     */
    public static boolean isInstall(String packageName) {
        Context context = AppManager.getInstance().getContext();
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public static String getProcessName(int pid){
        Context context = AppManager.getInstance().getContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null && !runningApps.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return "";
    }

    public static String getCurrentProgressName() {
        Context context = AppManager.getInstance().getContext();
        String processName = "";
        int pid = android.os.Process.myPid();
        Log.i(TAG, "MyApplication is oncreate====" + "pid=" + pid);
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
            if (info.pid == pid) {
                processName = info.processName;
                Log.i(TAG, "processName=" + processName);
            }
        }
        if ("com.example.liuwangshu.myprogress".equals(processName)) {
            Log.i(TAG, "processName=" + processName + "-----work");
        } else {
            Log.i(TAG, "processName=" + processName + "-----work");
        }
        return "";
    }

    public static boolean isCurrentProgress() {
        String processName = getCurrentProgressName();
        if (processName.equals("com.screeclibinvoke")) {
            return true;
        } else {
            return false;
        }
    }
}
