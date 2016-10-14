package com.li.videoapplication.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.fmsysj.zbqmcs.utils.ExApplication;
import com.li.videoapplication.R;
import com.li.videoapplication.data.local.ScreenShotEntity;
import com.li.videoapplication.ui.toast.ToastHelper;

import java.io.File;

public class IntentHelper {

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
        if (ExApplication.isInstallApp("com.huawei.systemmanager", activity)) {
            startActivityForResultActionMain(activity);
        } else {
            startActivityActionGetContent(activity);
        }
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
}
