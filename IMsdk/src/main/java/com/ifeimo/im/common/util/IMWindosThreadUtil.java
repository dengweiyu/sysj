package com.ifeimo.im.common.util;


import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lpds on 2017/4/1.
 */
public final class IMWindosThreadUtil {
    private final String TAG= "XMPP_IMWindosThreadUtil";
    private static IMWindosThreadUtil imWindosThreadUtil;
    static {
        imWindosThreadUtil = new IMWindosThreadUtil();
    }

    private IMWindosThreadUtil(){
        mainExecutorServiceMap = new HashMap<>();
    }

    public static IMWindosThreadUtil getInstances()
    {
        return imWindosThreadUtil;
    }


    private Map<String,ExecutorService> mainExecutorServiceMap;

    private void createThreadPoolByIMMain(String key){
        if(!mainExecutorServiceMap.containsKey(key)) {
            mainExecutorServiceMap.put(key, Executors.newCachedThreadPool());
            Log.i(TAG, "onCreate: join ThreadPool " + key);
        }else{
            Log.i(TAG, "onCreate: had ThreadPool " + key);
        }
    }

    public void run(String key, Runnable runnable){
        if(mainExecutorServiceMap.containsKey(key)){
            mainExecutorServiceMap.get(key).execute(runnable);
            Log.i(TAG, "createCurrentThreadPoolRunnable: 线程启动");
        }else{
            createThreadPoolByIMMain(key);
            run(key,runnable);
        }
    }


    public void leaveThreadPool(String key,boolean isShutdownNow){
        if(mainExecutorServiceMap.containsKey(key)){
            ExecutorService executorService = mainExecutorServiceMap.get(key);
            if(isShutdownNow) {
                executorService.shutdownNow();
            }else{
                executorService.shutdown();
            }
            mainExecutorServiceMap.remove(key);
        }
        Log.i(TAG, "onDestroy: destroy ThreadPool " + key);

    }


}
