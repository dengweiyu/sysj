package com.li.videoapplication.tools;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.li.videoapplication.R;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.io.File;

public class IntentHelper {

    private static final String TAG = IntentHelper.class.getSimpleName();

    /**
     * 播放网络视频（浏览器）
     *
     * @param url "http://forum.ea3w.com/coll_ea3w/attach/2008_10/12237832415.3gp"
     */
    public static void startActivityActionView(Context context, String url) {
        if (url == null)
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放网络视频
     *
     * @param url "http://forum.ea3w.com/coll_ea3w/attach/2008_10/12237832415.3gp"
     */
    public static void startActivityActionViewVideo(Context context, String url) {
        if (url == null)
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "video/* ";
        Uri uri = Uri.parse(url);
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }

    /**
     * 播放本地视频
     */
    public static void startActivityActionViewMp4(Context context, final String path) {
        if (path == null)
            return;
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null || !file.exists())
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "video/mp4");
        context.startActivity(intent);
    }

    /**
     * 浏览本地图片
     */
    public static void startActivityActionViewImage(Context context, final ScreenShotEntity record) {
        File file = new File(record.getPath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "image/*");
        context.startActivity(intent);
    }

    public static void startActivityStorge(Activity activity) {
        // 如果是华为系统
//        if (ExApplication.isInstallApp("com.huawei.systemmanager", activity)) {
//            startActivityForResultActionMain(activity);
//        } else {
//            startActivityActionGetContent(activity);
//        }
    }

    /**
     * 打开文件
     */
    private static void startActivityForResultActionMain(Activity activity) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.huawei.hidisk", "com.huawei.hidisk.filemanager.FileManager");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 打开存储路径
     */
    private static void startActivityActionGetContent(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            String path = Environment.getExternalStorageDirectory().toString() + File.separator + "LuPingDaShi" + File.separator;
            intent.setDataAndType(Uri.fromFile(new File(path)), "file/*");
            activity.startActivity(intent);
        } catch (Exception e) {
            ToastHelper.s(R.string.open_file_failure);
        }
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * 通知系统扫描媒体文件
     */
    public static void scanFile() {
        File lpdsFile = SYSJStorageUtil.getSysj();
        if (lpdsFile != null && lpdsFile.exists()) {
            scanFile(lpdsFile.getPath());
        }
    }

    /**
     * 通知系统扫描媒体文件
     */
    public static void scanFile(String filePath) {
        Log.d(TAG, "scanFile: // -------------------------------------");
        Context context = AppManager.getInstance().getContext();

        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(scanIntent);
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * 打开小米悬浮窗
     */
    public static void openFloatWindiws_XiaoMi() {
        Context context = AppManager.getInstance().getContext();
        try {
            Intent intent = new Intent();
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Log.d(TAG, "openFloatWindiws_XiaoMi: 1");
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Log.d(TAG, "openFloatWindiws_XiaoMi: 2");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 打开华为悬浮窗
     */
    public static void openFloatWindiws_HuaWei() {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.mainscreen.MainScreenActivity");
            context.startActivity(intent);
            Log.d(TAG, "openFloatWindiws_HuaWei: 1");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.SystemManagerMainActivity");
                context.startActivity(intent);
                Log.d(TAG, "openFloatWindiws_HuaWei: 2");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 打开魅族悬浮窗
     */
    public static void openFloatWindiws_Meizu() {
        // TODO: 2016/10/18
    }
}
