
package com.li.videoapplication.data.download;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 下载文件调度器
 */
public class DownLoadExecutor {

    public static final String TAG = DownLoadExecutor.class.getSimpleName();

    // ---------------------------------------------------------------------------------

    public static ThreadPoolExecutor executor;

    public static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            synchronized (DownLoadExecutor.class) {
                if (executor == null) {
                    executor = newExecutor();
                }
            }
        }
        return executor;
    }

    private static ThreadPoolExecutor newExecutor() {

        ThreadFactory threadFactory = new ThreadFactory() {

            private final AtomicInteger count = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "DownLoadManager-" + count.getAndIncrement());
            }
        };
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(128);

        RejectedExecutionHandler handler = new RejectedExecutionHandler() {

            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            }
        };

        ThreadPoolExecutor pool = new ThreadPoolExecutor(3,
                4,
                30,
                TimeUnit.SECONDS,
                queue,
                threadFactory,
                handler) {

            @Override
            public void execute(Runnable command) {
                super.execute(command);
                Log.d(TAG, "execute: activeCount=" + this.getActiveCount());
                Log.d(TAG, "execute: taskCount=" + this.getTaskCount());
                Log.d(TAG, "execute: queueSize=" + this.getQueue().size());
            }
        };
        return pool;
    }

    /**
     * 功能：线程池执行
     */
    public static void execute(Runnable r) {
        Log.d(TAG, "execute: r=" + r);
        try {
            getExecutor().execute(r);
            Log.d(TAG, "execute: true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：立即关闭，并挂起所有正在执行的线程，不接受新任务
     */
    public static void shutdownNow() {
        if (executor != null && !executor.isShutdown()) {
            try {
                executor.shutdownNow();
                Log.d(TAG, "shutdownNow: true");
                Log.d(TAG, "shutdownNow: isShutdown=" + executor.isShutdown());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor = null;
    }
}
