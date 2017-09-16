package com.li.videoapplication.utils;

import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import com.li.videoapplication.framework.AppManager;

import java.io.File;
import java.util.List;

/**
 * 安装包相关操作
 */
public class ApkUtil {

    public static final String TAG = ApkUtil.class.getSimpleName();

    public static String getPackageName(String filePath) {
        Context context = AppManager.getInstance().getContext();
        return getPackageName(context, filePath);
    }

    public static boolean isAvilible(String packageName) {
        Context context = AppManager.getInstance().getContext();
        return isAvilible(context, packageName);
    }

    // -------------------------------------------------------------------------------

    /**
     * 获得包名
     */
    public static String getPackageName(Context context, String filePath) {
        Log.d(TAG, "getPackageName: // ----------------------------------------------------------");
        Log.d(TAG, "getPackageName: filePath=" + filePath);
        if (context == null)
            return null;
        if (filePath == null)
            return null;
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info = packageManager.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo applicationInfo = info.applicationInfo;
            // String lable = packageManager.getApplicationLabel(applicationInfo).toString();
            String packageName = applicationInfo.packageName;// 得到安装包名称
            // String versionName = info.versionName;// 得到版本信息
            // Drawable icon = packageManager.getApplicationIcon(applicationInfo);// 得到图标信息
            return packageName;
        }
        return null;
    }

    /**
     * 应用是否安装
     */
    public static boolean isAvilible(Context context, String packageName) {
        Log.d(TAG, "isAvilible: // ----------------------------------------------------------");
        Log.d(TAG, "isAvilible: packageName=" + packageName);
        if (context == null)
            return false;
        if (packageName == null)
            return false;
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            if (packageInfos.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 检测是否安装某个应用
     */
    public static boolean isInstallApp(String packageName, Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 启动应用
     *
     * @param pakageName 应用包名
     */
    public static void launchApp(Context context, String pakageName) {
        Log.d(TAG, "launchApp: // ----------------------------------------------------------");
        Log.d(TAG, "launchApp: context=" + context);
        Log.d(TAG, "launchApp: pakageName=" + pakageName);
        if (context == null)
            return;
        if (pakageName == null)
            return;
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(pakageName);// 要启动应用的包名
        try {
            context.startActivity(intent);
            Log.d(TAG, "launchApp: true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装应用
     */
    public static void installApp(Context context, String filePath) {
        Log.d(TAG, "installApp: // ----------------------------------------------------------");
        Log.d(TAG, "installApp: context=" + context);
        Log.d(TAG, "installApp: filePath=" + filePath);
        if (context == null)
            return;
        if (filePath == null)
            return;
        Uri uri = Uri.fromFile(new File(filePath));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
            Log.d(TAG, "installApp: true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装应用
     */
    public static void openFile(Intent intent, Context context) {
        //拿到下载的Id
        long myDownLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        SharedPreferences preferences = context.getSharedPreferences("downloadcomplete", 0);
        long refence = preferences.getLong("refernece", 0);

        //如果是我们下载完成后发送的广播消息,那么启动并安装
        if (refence == myDownLoadId) {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Intent updateApk = new Intent(Intent.ACTION_VIEW);
            Uri downloadFileUri = downloadManager.getUriForDownloadedFile(myDownLoadId);
            updateApk.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            updateApk.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(updateApk);
        }
    }

    /**
     * 安装应用 20170106
     */
    public static void installAPK(Context context, Intent intent) {
        Log.d(TAG, "installAPK: ");
        File file;
        DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        //在广播中取出下载任务的id
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        query.setFilterById(id);
        Cursor c = manager.query(query);
        if(c.moveToFirst()) {
            //获取文件下载路径
            String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
            Log.d(TAG, "installAPK: filename = "+filename);
            //如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
            if(filename != null){
                Intent updateApk = new Intent(Intent.ACTION_VIEW);
                updateApk.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                file = new File(filename);
                updateApk.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                context.startActivity(updateApk);
            }
        }
    }

    /**
     * 卸载应用
     */
    public static void uninstallApp(Context context, String packageName) {
        Log.d(TAG, "uninstallApp: // ----------------------------------------------------------");
        Log.d(TAG, "uninstallApp: context=" + context);
        Log.d(TAG, "uninstallApp: packageName=" + packageName);
        if (context == null)
            return;
        if (packageName == null)
            return;
        Uri packageURI = Uri.parse("package: " + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE);
        try {
            context.startActivity(intent);
            Log.d(TAG, "uninstallApp: true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束任务
     */
    public static void finishApp() {
        Log.d(TAG, "finishApp: // ----------------------------------------------------------");
        Context context = AppManager.getInstance().getContext();
        if (context == null)
            return;
        String packageName = VersionUtils.getCurrentPackageName(context);
        if (packageName == null)
            return;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= 21) {
            List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();
            for (ActivityManager.AppTask appTask : appTasks) {
                ActivityManager.RecentTaskInfo recentTaskInfo = appTask.getTaskInfo();
                if (recentTaskInfo != null &&
                        recentTaskInfo.baseIntent != null &&
                        recentTaskInfo.baseIntent.toString().contains(packageName)) {
                    try {
                        Log.d(TAG, "finishApp: package=" + recentTaskInfo.baseIntent.getPackage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        appTask.finishAndRemoveTask();
                        Log.d(TAG, "finishApp: true");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        context = AppManager.getInstance().getContext();
        Log.d(TAG, "finishApp: context=" + context);
        runningTasks(context);
    }

    public static List<ActivityManager.RunningTaskInfo> runningTasks(Context context) {
        Log.d(TAG, "runningTasks: // ----------------------------------------------------------");
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1000);
        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfos) {
            Log.d(TAG, "runningTasks: baseActivity=" + runningTaskInfo.baseActivity);
            Log.d(TAG, "runningTasks: topActivity=" + runningTaskInfo.topActivity);
            Log.d(TAG, "runningTasks: numActivities=" + runningTaskInfo.numActivities);
            Log.d(TAG, "runningTasks: numRunning=" + runningTaskInfo.numRunning);
        }
        return runningTaskInfos;
    }
}
