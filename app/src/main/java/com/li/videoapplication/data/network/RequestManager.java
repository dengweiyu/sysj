package com.li.videoapplication.data.network;

import android.util.Log;

import java.util.ArrayList;
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
            tasks.add(task);
            Log.i(tag, "addTask/task=" + task);
        }
        printAllTask();
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
