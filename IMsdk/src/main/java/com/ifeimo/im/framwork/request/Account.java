package com.ifeimo.im.framwork.request;

import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.RequestManager;
import com.ifeimo.im.framwork.interface_im.IMMain;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by lpds on 2017/2/13.
 */
public class Account {

    public static Response sendMemberMsgToSYSJ(Object key, Map<String, String> para) throws IOException {
        GetBuilder builder = OkHttpUtils.get().url(RequestUrl.REQUEST_SEND_SYSJ).tag(RequestManager.getKey(key));
        Proxy.getRequest().convertParameter(para, builder);
        RequestCall requestCall = builder.build();
        Proxy.getRequest().getRequestQueue().get(RequestManager.getKey(key)).put(RequestUrl.REQUEST_SEND_SYSJ,requestCall);
        return requestCall.execute();
    }

    public static void sendMemberMsgToSYSJ(Object key, Map<String, String> para,Callback callback){
        GetBuilder builder = OkHttpUtils.get().url(RequestUrl.REQUEST_SEND_SYSJ).tag(RequestManager.getKey(key));
        Proxy.getRequest().convertParameter(para, builder);
        RequestCall requestCall = builder.build();
        Proxy.getRequest().getRequestQueue().get(RequestManager.getKey(key)).put(RequestUrl.REQUEST_SEND_SYSJ,requestCall);
        requestCall.execute(callback);
    }

    public static Response getMemberInfo(Object key, String memberid) throws IOException {
        GetBuilder builder = OkHttpUtils.get().url(RequestUrl.REQUEST_GET_SYSJ).tag(RequestManager.getKey(key));
        builder.addParams("memberId",memberid);
        RequestCall requestCall = builder.build();
        if(!StringUtil.isNull(RequestManager.getKey(key))) {
            Proxy.getRequest().getRequestQueue().get(RequestManager.getKey(key)).put(RequestUrl.REQUEST_GET_SYSJ, requestCall);
        }
        return requestCall.execute();
    }


}
