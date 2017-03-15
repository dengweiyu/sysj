package com.ifeimo.im.common.util;

import android.os.Looper;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.interface_im.ILife;
import com.ifeimo.im.framwork.interface_im.IMMain;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lpds on 2017/2/15.
 */
public class ThreadUtil implements IEmployee {

    private static ThreadUtil threadUtil;
    private final int MAX_THREAD = 100;

    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(MAX_THREAD);

    static {
        threadUtil = new ThreadUtil();
    }

    private ThreadUtil() {
        Proxy.getManagerList().addManager(this);
    }

    public static ThreadUtil getInstances() {
//        cachedThreadPool.s
        return threadUtil;
    }

    /**
     * 此线程仅此用于IMWindow中
     *
     * @param runnable
     * @return
     */
    public void createThreadStartToFixedThreadPool(Runnable runnable) {
        fixedThreadPool.execute(runnable);
    }

    /**
     * 此线程仅此用于IMWindow中
     *
     * @param runnable
     * @return
     */
    public void createThreadStartToCachedThreadPool(Runnable runnable) {
        cachedThreadPool.execute(runnable);

    }


    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }


    @Override
    public boolean isInitialized() {
        return true;
    }

    public int getMAX_THREAD() {
        return MAX_THREAD;
    }
}
