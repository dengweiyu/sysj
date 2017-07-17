package com.ifeimo.im.framwork.commander;

import android.content.Context;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.common.callback.LoginCallBack;
import com.ifeimo.im.common.callback.LogoutCallBack;
import com.ifeimo.im.framwork.connect.IConnectSupport;
import com.ifeimo.im.framwork.connect.MemberInfoObserver;
import com.ifeimo.im.framwork.connect.OnConnectErrorListener;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * 连接管理接口
 * Created by lpds on 2017/1/17.
 */
public interface IConnect extends ConnectionListener,IEmployee{

    void init(Context context);
    void init();
    XMPPTCPConnection getConnection();
    void disconnect();
    boolean isConnect();
    void addLoginListener(LoginCallBack loginCallBack);
    void removeLoginListener();
    void runConnectThread();
    void addLogoutCallBack(LogoutCallBack logoutCallBack);
    void removeLogoutCallBack();
    void updateLogin();
    void setErrorListener(OnConnectErrorListener errorListener);
    IConnectSupport getConnectSupport();
    void addMemberInfoObserver(MemberInfoObserver memberInfoObserver);
    void removeMemberInfoObserver(MemberInfoObserver memberInfoObserver);

}
