package com.ifeimo.im.common.util;

import android.app.Activity;

import com.ifeimo.im.framwork.interface_im.IMMain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.measite.minidns.record.A;

/**
 * Created by lpds on 2017/4/1.
 */
final class IMWindosThreadUtil {
    private static IMWindosThreadUtil imWindosThreadUtil;
    static {
        imWindosThreadUtil = new IMWindosThreadUtil();
    }

    IMWindosThreadUtil(){
        mainExecutorServiceMap = new HashMap<>();
    }

    static IMWindosThreadUtil getInstances()
    {
        return imWindosThreadUtil;
    }


    private Map<String,ExecutorService> mainExecutorServiceMap;

    void createThreadPoolByIMMain(IMMain imMain){
        final String hashCode = imMain.hashCode()+"";
        if(!mainExecutorServiceMap.containsKey(hashCode)){
            mainExecutorServiceMap.put(hashCode, Executors.newCachedThreadPool());
        }
    }

    void createCurrentThreadPoolRunnable(IMMain imMain,Runnable runnable){
        final String hashCode = imMain.hashCode()+"";
        if(mainExecutorServiceMap.containsKey(hashCode)){
            mainExecutorServiceMap.get(hashCode).execute(runnable);
        }
    }


    void leaveThreadPool(IMMain imMain){
        final String hashCode = imMain.hashCode()+"";
        if(mainExecutorServiceMap.containsKey(hashCode)){
            ExecutorService executorService = mainExecutorServiceMap.get(hashCode);
            executorService.shutdownNow();
            mainExecutorServiceMap.remove(hashCode);
            executorService = null;
        }
    }


}
