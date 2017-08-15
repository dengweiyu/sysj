package com.li.videoapplication.data.network;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.li.videoapplication.framework.BaseActivity;
import com.li.videoapplication.framework.BaseAppCompatActivity;
import com.li.videoapplication.framework.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：网络请求帮助类
 */
public class RequestHelper<T extends BaseEntity> {

    protected final String action = this.getClass().getName();
    protected final String tag = this.getClass().getSimpleName();

    private static List<RequestRunnable> sRunable = new ArrayList<>();
    public void doNetwork(RequestObject request) {
        doService(request);

        try {
            Log.d(tag, "doNetwork/url=" + request.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：Android后台服务Service执行Http网络访问任务
     */
    public void doService(RequestObject request) {
        if (request == null)
            return;
        try {
            RequestService.startRequestService();
            RequestService.postRequestEvent(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Log.d(tag, "doService/url=" + request.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：AsyncTask异步执行Http网络访问任务
     */
    public void doTask(RequestObject request) {
        if (request == null)
            return;
        RequestTask task = new RequestTask();
        task.execute(request);

        try {
            Log.d(tag, "doTask/url=" + request.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：Java线程池ExecutorService执行Http网络访问任务
     */
    public void doExecutor(RequestObject request) {
        if (request == null)
            return;
        RequestRunnable runnable = new RequestRunnable(request);

        RequestExecutor.execute(runnable);
        try {
            Log.d(tag, "doExecutor/url=" + request.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：Java线程池ExecutorService执行Http网络访问任务
     */
    public void doExecutor(Activity activity, RequestObject request) {
        if (request == null || activity == null)
            return;
        RequestRunnable runnable = null;
        if (activity instanceof BaseActivity)
            runnable = ((BaseActivity) activity).requestManager.createTask(request);
        if (activity instanceof BaseAppCompatActivity)
            runnable = ((BaseAppCompatActivity) activity).requestManager.createTask(request);
        if (runnable == null){
            runnable = new RequestManager().createTask(request);
        }
        if (runnable != null)
            RequestExecutor.execute(runnable);

        try {
            Log.d(tag, "doExecutor/url=" + request.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：执行Http网络访问任务并返回实体
     */
    public T postEntity(RequestObject request) {
        try {
            Log.d(tag, "postEntity/url=" + request.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (request == null)
            return null;
        RequestTarget target = new RequestTarget();
        BaseEntity entity = target.postEntity(request);
        if (entity == null) {
            return null;
        } else {
            return (T) entity;
        }
    }
}
