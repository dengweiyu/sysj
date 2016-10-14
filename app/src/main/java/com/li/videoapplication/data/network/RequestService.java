package com.li.videoapplication.data.network;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.li.videoapplication.data.upload.ImageShareRequestObject;
import com.li.videoapplication.data.upload.ImageShareRequestObject208;
import com.li.videoapplication.data.upload.ImageShareTask;
import com.li.videoapplication.data.upload.ImageShareTask208;
import com.li.videoapplication.data.upload.ImageUploadRequstObject;
import com.li.videoapplication.data.upload.VideoShareRequestObject;
import com.li.videoapplication.data.upload.VideoShareTask;
import com.li.videoapplication.data.upload.VideoShareTask208;
import com.li.videoapplication.data.upload.VideoUploadRequestObject;
import com.li.videoapplication.framework.AppManager;
import com.li.videoapplication.framework.BaseService;

import io.rong.eventbus.EventBus;


/**
 * 功能：网络请求任务（在后台服务中执行）
 */
public class RequestService extends BaseService {

    public static final String TAG = RequestService.class.getSimpleName();

    /**
     * 启动服务
     */
    public synchronized final static void startRequestService() throws Exception {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent(context, RequestService.class);
        context.startService(intent);
    }

    /**
     * 停止服务
     */
    public synchronized final static void stopRequestService() throws Exception {
        Context context = AppManager.getInstance().getContext();
        Intent intent = new Intent(context, RequestService.class);
        context.stopService(intent);
    }

    /**
     * 网络访问：传递请求参数
     */
    public synchronized final static void postRequestEvent(RequestObject request) {
        EventBus.getDefault().post(request);

        try {
            Log.d(TAG, "postRequestEvent/url=" + request.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络访问：接受请求参数
     */
    public void onEventMainThread(RequestObject event) {
        if (event != null) {
            FragmentActivity activity = AppManager.getInstance().currentActivity();
            RequestHelper helper = new RequestHelper();
            if (activity != null && event.getA() == 0) {
                helper.doExecutor(activity, event);
            } else {
                helper.doExecutor(event);
            }

            try {
                Log.d(TAG, "onEventMainThread/url=" + event.getUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传图片：传递请求参数204
     */
    public synchronized final static void postImageUploadEvent(ImageUploadRequstObject request) {
        EventBus.getDefault().post(request);
    }

    /**
     * 上传图片：接受请求参数204
     */
    public void onEventMainThread(ImageUploadRequstObject event) {
        if (event != null) {
            ImageShareTask service = new ImageShareTask(event);
            service.upload204();
            Log.d(tag, "ImageUploadRequstObject/upload204");
        }
    }

    /**
     * 上传图片：传递请求参数
     */
    public synchronized final static void postImageShareEvent(ImageShareRequestObject request) {
        EventBus.getDefault().post(request);
    }

    /**
     * 上传图片：传递请求参数
     */
    public synchronized final static void postImageShareEvent208(ImageShareRequestObject208 request) {
        EventBus.getDefault().post(request);
    }

    /**
     * 上传图片：接受请求参数208
     */
    public void onEventMainThread(ImageShareRequestObject208 event) {
        if (event != null) {
            ImageShareTask208 service = new ImageShareTask208(event);
            service.upload();
            Log.d(tag, "postImageShareEvent/upload208");
        }
    }

    /**
     * 上传图片：接受请求参数
     */
    public void onEventMainThread(ImageShareRequestObject event) {
        if (event != null) {
            ImageShareTask service = new ImageShareTask(event);
            service.upload();
            Log.d(tag, "postImageShareEvent/upload");
        }
    }

    /**
     * 上传视频：传递请求参数
     */
    public synchronized final static void postVideoShareEvent(VideoShareRequestObject request) {
        EventBus.getDefault().post(request);
    }

    /**
     * 上传视频：传递请求参数208
     */
    public synchronized final static void postVideoShareEvent(VideoUploadRequestObject request) {
        EventBus.getDefault().post(request);
    }

    private VideoShareTask service;

    /**
     * 上传视频：接受请求参数
     */
    public void onEventMainThread(VideoShareRequestObject event) {
        if (event.getStatus() == VideoShareRequestObject.STATUS_START ||
                event.getStatus() == VideoShareRequestObject.STATUS_RESUME) {
            service = new VideoShareTask(event);
            service.upload();
            Log.d(tag, "postVideoShareEvent/upload");
        } else if (event.getStatus() == VideoShareRequestObject.STATUS_PAUSE) {
            if (service != null) {
                service.cancel();
                Log.d(tag, "postVideoShareEvent/cancel");
            }
        }
    }

    private VideoShareTask208 service208;

    /**
     * 上传视频：接受请求参数208
     */
    public void onEventMainThread(VideoUploadRequestObject event) {
        if (event.getStatus() == VideoUploadRequestObject.STATUS_START ||
                event.getStatus() == VideoUploadRequestObject.STATUS_RESUME) {
            service208 = new VideoShareTask208(event);
            service208.upload();
            Log.d(tag, "postVideoUploadEvent/upload");
        } else if (event.getStatus() == VideoUploadRequestObject.STATUS_PAUSE) {
            if (service208 != null) {
                service208.cancel();
                Log.d(tag, "postVideoUploadEvent/cancel");
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private MyBinder myBinder = new MyBinder();

    public static class MyBinder extends Binder {

    }
}
