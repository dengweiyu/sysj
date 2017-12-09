package com.li.videoapplication.data.network;

import android.util.Log;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 功能：网络请求页面管理类
 */
public class RequestManager {

    private final String action = this.getClass().getName();
    private final String tag = this.getClass().getSimpleName();

    private List<RequestRunnable> tasks = new ArrayList<>();

    public RequestManager() {
        super();
    }

    /**
     * 添加网络任务
     */
    private void addTask(RequestRunnable task) {
        if (tasks != null && !tasks.contains(task)) {
            Log.e(tag, "-------------addTask-----------------");
            Log.i(tag, "tasks大小：" + tasks.size());
            tasks.add(task);
            Log.i(tag, "tasks大小：" + tasks.size());
            Log.i(tag, "addTask/task=" + task);
        }
       // printAllTask();
        Log.i(tag, "执行完addTask方法..........." );
    }

    /**
     * 取消所有网络任务
     */
    public void cancelAllTask() {
        if (tasks != null) {
            Log.e(tag, "-------------cancelAllTask-----------------");
            for (int i = 0; i < tasks.size(); ++i) {
                RequestRunnable t = tasks.get(i);
                t.cancel();
                tasks.clear();
                Log.i(tag, "cancelAllTask");
            }
        }
        printAllTask();
    }

    /**
     * 取消网络任务
     */
    public void cancelTask(RequestRunnable task) {
        if (task != null) {
            Log.e(tag, "-------------cancelTask-----------------");
            task.cancel();
            tasks.remove(task);
            Log.i(tag, "cancelTask/task=" + task);
        }
    }

    /**
     * 取消网络任务 取消同一url任务
     */
    public  void cancelTask(final String url){
        Collection<RequestRunnable> collection = Collections2.filter(tasks, new Predicate<RequestRunnable>() {
            @Override
            public boolean apply(RequestRunnable input) {
                RequestObject object = input.getRequestObject();
                if (object != null){
                    if (object.getUrl() != null && object.getUrl().equals(url)){
                        return true;
                    }
                }
                return false;
            }

        });

        for (RequestRunnable runnable:
             collection) {
            runnable.cancel();

        }
        tasks.removeAll(collection);
    }

    /**
     * 生产网络任务
     */
    public RequestRunnable createTask(RequestObject request) {
        if (request != null) {
            Log.e(tag, "-------------createTask-----------------");
            RequestRunnable runnable = new RequestRunnable(request);
            addTask(runnable);
            Log.i(tag, "createTask/request=" + request);
            return runnable;
        }
        return null;
    }

    /**
     * 移除所有网络任务
     */
    public void clearAllTask() {
        if (tasks != null) {
            Log.e(tag, "-------------clearAllTask-----------------");
            tasks.clear();
        }
    }

    /**
     * 打印网络任务
     */
    public void printAllTask() {
        if (tasks != null) {
            Log.e(tag, "-------------printAllTask-----------------");
            for (RequestRunnable task : tasks) {
                Log.i(tag, "printAllTask/task=" + task);
            }
        }
    }
}
