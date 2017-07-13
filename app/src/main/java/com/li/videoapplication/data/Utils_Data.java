package com.li.videoapplication.data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ifeimo.screenrecordlib.util.TaskUtil;
import com.li.videoapplication.data.download.DownLoadExecutor;
import com.li.videoapplication.data.download.DownLoadManager;
import com.li.videoapplication.data.network.LightTask;
import com.li.videoapplication.data.network.RequestExecutor;
import com.li.videoapplication.data.network.RequestService;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.ui.activity.MainActivity;
import com.li.videoapplication.utils.ApkUtil;
import com.li.videoapplication.utils.AppUtil;
import com.li.videoapplication.utils.ThreadUtil;

public class Utils_Data {

    public static final String TAG = Utils_Data.class.getSimpleName();

    /**
     * 销毁所有资源（服务，调度器，下载器）
     */
    public static void destroyAllResouce() {
        Log.d(TAG, "destroyAllResouce: // -----------------------------------------------------");

        // 移除任务栈
//        finishAllActivity();

        // 暂停上传视频服务
        DataManager.UPLOAD.pauseVideo210();

        // 销毁下载器
        DownLoadManager.destruction();

        // 停止服务
        stopAllService();

        // 立即关闭所有调度器
        shutdownNowAllExecutor();

        // 关闭所有网络访问客户端
        shutdownAllClient();

        Log.d(TAG, "destroyAllResouce: true");
    }

    /**
     * 移除任务栈
     */
    private static void finishAllActivity() {
        Log.d(TAG, "finishAllActivity: // -----------------------------------------------------");
        if (AppUtil.isAppBackground()) {// 应用处于后台
            // 结束所有Activity
            AppManager.getInstance().removeAllActivity();
            Log.d(TAG, "finishAllActivity: true");
        }
    }

    /**
     * 移除任务栈
     */
    public static void finishApp() {
        Log.d(TAG, "finishApp: // -----------------------------------------------------");
        try {
            ApkUtil.finishApp();
            Log.d(TAG, "finishApp: true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除任务栈2
     */
    public static void finishTask() {
        Log.d(TAG, "finishTask: // -----------------------------------------------------");
        Context context = AppManager.getInstance().getContext();
        Activity activity = AppManager.getInstance().getActivity(MainActivity.class);
        if (activity != null) {
            TaskUtil.clearTaskAndAffinity(activity);
            Log.d(TAG, "finishTask: 1");
        } /*else {
            EmptyActivity.startEmptyActivity(context);
            Log.d(TAG, "finishTask: 2");
        }*/
        Log.d(TAG, "finishTask: true");
    }

    /**
     * 停止服务
     */
    private static void stopAllService() {
        // 停止网络请求服务
        RequestService.stopRequestService();
    }

    /**
     * 立即关闭所有调度器，线程
     */
    private static void shutdownNowAllExecutor() {

        ThreadUtil.stop();
        LightTask.quitSafely();

        DownLoadExecutor.shutdownNow();
//        GameDownLoadExecutor.shutdownNow();
        RequestExecutor.shutdownNow();
        // ImageExecutor.shutdownNow();
    }

    /**
     * 关闭所有网络访问客户端
     */
    private static void shutdownAllClient() {
//        HttpClient41.destruction();
//        HttpClient43.destruction();
//        OkApacheClient272.destruction();
//        OkHttpClient272.destruction();
    }
}
