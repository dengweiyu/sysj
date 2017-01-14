package com.li.videoapplication.utils;

import android.util.Log;

/**
 * 线程工具
 */
public class ThreadUtil {

    private static final String TAG = ThreadUtil.class.getSimpleName();

    // ---------------------------------------------------------------------------------

    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("newThreadGroup-RequestExecutor");
    private static long index = 0;

    /**
     * 功能：新线程执行
     */
    public static void start(Runnable r) {
        if (r != null) {
            try {
                Log.i(TAG, "start/r=" + r);
                ++index;
                Thread thread = new Thread(THREAD_GROUP, r, "newThraed-RequestExecutor" + index);
                thread.start();
                Log.d(TAG, "start: true");
                Log.d(TAG, "start: index=" + index);
                Log.d(TAG, "start: threadName=" + thread.getName());
                Log.d(TAG, "start: threadGroupName=" + thread.getThreadGroup());
                Log.d(TAG, "start: activeCount=" + thread.getThreadGroup().activeCount());
                Log.d(TAG, "start: activeGroupCount=" + thread.getThreadGroup().activeGroupCount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 功能：新线程关闭
     */
    public static void stop() {
        if (THREAD_GROUP != null) {
            try {
                THREAD_GROUP.stop();
                Log.d(TAG, "stop: true");
                Log.d(TAG, "stop: activeCount=" + THREAD_GROUP.activeCount());
                Log.d(TAG, "stop: activeGroupCount=" + THREAD_GROUP.activeGroupCount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}