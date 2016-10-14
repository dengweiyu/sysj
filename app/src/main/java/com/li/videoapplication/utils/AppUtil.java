package com.li.videoapplication.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

import com.fmsysj.zbqmcs.record.Recorder44;
import com.li.videoapplication.R;
import com.li.videoapplication.framework.AppManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public static int getVersionCode(Context context){
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 功能：判断当前应用是否在前台或后台
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.processName.equals(context.getPackageName())) {
                if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true; // "后台"
                } else {
                    return false; // "前台"
                }
            }
        }
        return false;
    }

    //在进程中去寻找当前APP的信息，判断是否在前台运行
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager =(ActivityManager) context.getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName =context.getPackageName();
        List<RunningAppProcessInfo>appProcesses = activityManager.getRunningAppProcesses();
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
            version = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return version;
    }

    static int copytimes = 0;

    /**
     * 将文件拷贝至data下的应用文件夹内
     */
    public static void cpRecrodCore() {
        if (copytimes == 0) {
            copyFile("fmNewCore", R.raw.screenrecord1111);
        } else if (copytimes == 1) {
            copyFile("busybox", R.raw.busybox);
        } else if (copytimes == 2) {
            copyFile("fmOldCore", R.raw.screenrecord10272);
        }
        if (copytimes == 0) {
            Recorder44.StartRecord("chmod 777 " + getFilesDirs() + "/fmNewCore");
        } else if (copytimes == 1) {
            Recorder44.StartRecord("chmod 777 " + getFilesDirs() + "/busybox");
        } else if (copytimes == 2) {
            Recorder44.StartRecord("chmod 777 " + getFilesDirs() + "/fmOldCore");
        }
        copytimes++;
        if (copytimes < 3) {
            cpRecrodCore();
        }
    }

    /**
     * 将文件拷贝至data下的应用文件夹内
     */
    @SuppressLint("SdCardPath")
    private static void copyFile(String FileName, int Raw) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            File file = new File(getFilesDirs(), FileName);
            is = AppManager.getInstance().getContext().getResources()
                    .openRawResource(Raw);
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        byte[] buffer = new byte[1024];
        int count = 0;
        try {
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String filesDir;

    /**
     * 获取包名目录下的files
     */
    private static String getFilesDirs() {
        if (filesDir == null) {
            filesDir = AppManager.getInstance().getContext().getFilesDir()
                    .getPath();
        }
        return filesDir;
    }
}
