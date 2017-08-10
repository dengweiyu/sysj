package com.ifeimo.im.framwork.request;

/**
 * Created by lpds on 2017/2/13.
 */
public final class RequestUrl {

    /**
     * 提交用户消息到im
     */
    public static final String REQUEST_SEND_SYSJ = "http://im.17sysj.com:8080/IM/SetMemberInfo";
    /**
     * 提交用户消息到imDebug
     */
    public static final String REQUEST_SEND_SYSJ_DEBUG = "http://192.168.48.54:9090/plugins/imservlet/setmemberinfo";
    /**
     * 获取用户消息
     */
    public static final String REQUEST_GET_SYSJ = "http://im.17sysj.com:8080/IM/GetMemberInfo";
    /**
     * 获取用户消息Debug
     */
    public static final String REQUEST_GET_SYSJ_DEBUG = "http://192.168.48.54:9090/plugins/imservlet/getmemberinfo";
    /**
     * 获取陪练名单
     */
    public static final String REQUETS_GET_ROSTERS = "http://op.17sysj.com:9090/plugins/restapi/v1/users/presencemanager/roster";
    /**
     * 获取陪练状态
     */
    public static final String REQUETS_GET_ROSTERS_PRESENCE = "http://op.17sysj.com:9090/plugins/presenceupdate/status?groupname=陪练&type=xml";
}
