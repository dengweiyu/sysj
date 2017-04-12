package com.ifeimo.im.framwork.interface_im;

import android.content.Context;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.common.callback.LoginCallBack;
import com.ifeimo.im.common.callback.LogoutCallBack;
import com.ifeimo.im.common.callback.OnLoginSYSJCallBack;
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
    boolean isLogin();
    void addLoginListener(LoginCallBack loginCallBack);
    void removeLoginListener();
    void addOnSYSJLoginListener(OnLoginSYSJCallBack onLoginSYSJCallBack);
    void removeOnSYSJLoginListener();
    void runConnectThread();
    void addLogoutCallBack(LogoutCallBack logoutCallBack);
    void removeLogoutCallBack();
    void updateLogin();
    void setErrorListener(OnConnectErrorListener errorListener);

}
