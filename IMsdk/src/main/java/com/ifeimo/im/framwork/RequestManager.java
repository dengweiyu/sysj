package com.ifeimo.im.framwork;


import android.util.Log;

import com.ifeimo.im.common.bean.RequestTag;
import com.ifeimo.im.framwork.commander.ILife;
import com.ifeimo.im.framwork.commander.IMMain;
import com.ifeimo.im.framwork.commander.IMRequest;
import com.ifeimo.im.framwork.request.Account;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.HasParamsable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by lpds on 2017/2/9.
 */
public final class RequestManager implements IMRequest,ILife{

    private final static String TAG = "XMPP_RequestManager";

    private static RequestManager request;
    static {
        request = new RequestManager();
    }

    private Map<String,RequestTag> requestQueue;

    private RequestManager(){
        init();
        ManagerList.getInstances().addManager(this);
        requestQueue = new HashMap<>();
        saveToQueue(getKey(IMSdk.CONTEXT));
        saveToQueue(getKey(IMConnectManager.getInstances()));
    }

    public static IMRequest getInstances(){
        return request;
    }


    public void init(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Log.i(TAG, "intercept: uri = "+chain.request().url()+"  body = "+chain.request().body());
                        return chain.proceed(chain.request());
                    }
                })
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

    }

    public Account createRequestAccount() {
        return null;
    }

    @Override
    public boolean isInitialized() {
        return this != null;
    }

    @Override
    public void onCreate(IMMain imMain) {
        saveToQueue(getKey(imMain));
        Log.i(TAG,"------ requestQueue 队列添加此 activity  ------");
    }

    @Override
    public void onResume(IMMain imMain) {

    }

    @Override
    public void onDestroy(IMMain imMain) {
        removeByQueue(getKey(imMain));
        OkHttpUtils.getInstance().cancelTag(getKey(imMain));
        Log.i(TAG,"------ requestQueue 队列移除此 activity  ------");
    }

    @Override
    public void onPause(IMMain imWindow) {

    }

    @Override
    public void onStop(IMMain imWindow) {

    }

    public void convertParameter(Map<String, String> para, HasParamsable hasParamsable){
        if(para != null && para.size() > 0){
            Set<String> set = para.keySet();
            for(String str : set){
                hasParamsable.addParams(str,para.get(str));
            }
        }
    }

    public Map<String, RequestTag> getRequestQueue() {
        return requestQueue;
    }

    /**
     * 存储请求key
     * @param key
     */
    private void saveToQueue(String key) {
        if(!requestQueue.keySet().contains(key)) {
            requestQueue.put(key, new RequestTag(key));
        }
    }

    /**
     * 移除请求key
     * @param key
     */
    private void removeByQueue(String key){
        requestQueue.remove(key);
    }

    public static String getKey(Object o){
        return o.hashCode()+"";
    }

}
