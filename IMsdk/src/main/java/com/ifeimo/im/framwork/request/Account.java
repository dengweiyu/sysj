package com.ifeimo.im.framwork.request;

import com.google.gson.Gson;
import com.ifeimo.im.BuildConfig;
import com.ifeimo.im.common.bean.xml.CoachRosterList;
import com.ifeimo.im.common.bean.xml.PresenceList;
import com.ifeimo.im.common.bean.response.MemberInfoRespones;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.common.util.XMLUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.RequestManager;
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.DomDriver;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by lpds on 2017/2/13.
 */
public class Account {

    /**
     * 提交用户消息给im
     * @param object
     * @param para
     * @return
     * @throws IOException
     */
    public static Response sendMemberMsgToSYSJ(Object object, Map<String, String> para) throws IOException {
        GetBuilder builder;
        if(IMSdk.Debug) {
            builder = OkHttpUtils.get().url(RequestUrl.REQUEST_SEND_SYSJ_DEBUG).tag(RequestManager.getKey(object));
        }else{
            builder = OkHttpUtils.get().url(RequestUrl.REQUEST_SEND_SYSJ).tag(RequestManager.getKey(object));
        }
        RequestManager.getInstances().convertParameter(para, builder);
        RequestCall requestCall = builder.build();
        RequestManager.getInstances().getRequestQueue().get(RequestManager.getKey(object)).put(RequestUrl.REQUEST_SEND_SYSJ,requestCall);
        return requestCall.execute();
    }

//    public static void sendMemberMsgToSYSJ(Object object, Map<String, String> para,Callback callback){
//        GetBuilder builder = OkHttpUtils.get().url(RequestUrl.REQUEST_SEND_SYSJ).tag(RequestManager.getKey(object));
//        RequestManager.getInstances().convertParameter(para, builder);
//        RequestCall requestCall = builder.build();
//        RequestManager.getInstances().getRequestQueue().get(RequestManager.getKey(object)).put(RequestUrl.REQUEST_SEND_SYSJ,requestCall);
//        requestCall.execute(callback);
//    }

    /**
     * 获得用户消息
     * @param object
     * @param memberid
     * @return
     * @throws IOException
     */
    public static MemberInfoRespones getMemberInfo(Object object, String memberid) throws IOException {
        GetBuilder builder;
        if(IMSdk.Debug) {
            builder = OkHttpUtils.get().url(RequestUrl.REQUEST_GET_SYSJ_DEBUG).tag(RequestManager.getKey(object));
        }else{
            builder = OkHttpUtils.get().url(RequestUrl.REQUEST_GET_SYSJ).tag(RequestManager.getKey(object));
        }
        builder.addParams("memberId",memberid);
        RequestCall requestCall = builder.build();
        if(!StringUtil.isNull(RequestManager.getKey(object))) {
            Proxy.getRequest().getRequestQueue().get(RequestManager.getKey(object)).put(RequestUrl.REQUEST_GET_SYSJ, requestCall);
        }
        Gson gson = new Gson();
        return gson.fromJson(requestCall.execute().body().string(),MemberInfoRespones.class);
    }

//    /**
//     * 获取陪练列表
//     * @return
//     * @throws IOException
//     */
//    public static List<CoachRosterList.RosterItem> getCoachRosters(){
//        try {
//            GetBuilder builder = OkHttpUtils.get().url(RequestUrl.REQUETS_GET_ROSTERS).tag(RequestManager.getKey(IMSdk.CONTEXT));
//            builder.addHeader("Authorization", "Basic YWRtaW46Zm0xMjM0NTY=");
//            RequestCall requestCall = builder.build();
//            if (!StringUtil.isNull(RequestManager.getKey(IMSdk.CONTEXT))) {
//                Proxy.getRequest().getRequestQueue().get(RequestManager.getKey(IMSdk.CONTEXT)).put(RequestUrl.REQUEST_GET_SYSJ, requestCall);
//            }
//            final String xml = requestCall.execute().body().string();
//
//        }catch (Exception ex){
//
//        }
//        return null;
//    }

    /**
     * 获取陪练人员状态
//     * @param object
     * @return
     * @throws IOException
     */
    public static PresenceList getCoachPresence(){
        try {
            GetBuilder builder = OkHttpUtils.get().url(RequestUrl.REQUETS_GET_ROSTERS_PRESENCE).tag(RequestManager.getKey(IMSdk.CONTEXT));
            RequestCall requestCall = builder.build();
            if (!StringUtil.isNull(RequestManager.getKey(IMSdk.CONTEXT))) {
                Proxy.getRequest().getRequestQueue().get(RequestManager.getKey(IMSdk.CONTEXT)).put(RequestUrl.REQUEST_GET_SYSJ, requestCall);
            }
            final String xml = requestCall.execute().body().string();
//            EventBus.getDefault().post(XMLUtil.convertCoachByXMl(xml));
            return XMLUtil.convertCoachByXMl(xml);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new PresenceList();
    }

}
