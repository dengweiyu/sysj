package com.ifeimo.im.framwork;

import android.content.Context;
import android.drm.DrmManagerClient;
import android.util.Log;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.common.bean.ConnectBean;
import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.callback.LoginCallBack;
import com.ifeimo.im.common.callback.LogoutCallBack;
import com.ifeimo.im.common.callback.OnLoginSYSJCallBack;
import com.ifeimo.im.common.util.ConnectUtil;
import com.ifeimo.im.common.util.PManager;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.common.util.ThreadRunnable;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.connect.OnConnectErrorListener;
import com.ifeimo.im.framwork.interface_im.IConnect;
import com.ifeimo.im.framwork.interface_im.IMWindow;
import com.ifeimo.im.OnUpdate;
import com.ifeimo.im.framwork.request.Account;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lpds on 2017/1/11.
 */
final class IMConnectManager implements IConnect {
    private static final String TGA = "XMPP_IMConnectManager";
    static IMConnectManager connectManager;
    private XMPPTCPConnectionConfiguration connConfig = null;
    private ReconnectionManager reconnectionManager;
    private ConnectBean connectBean = null;
    private XMPPTCPConnection connection;
    private Semaphore semaphore;
    private DeflaterStanzaFilter deflaterStanzaFilter;
    private StanzaListener stanzaListener;
    private static Context application;
    private boolean isSYSJlLogin = false;
    private boolean isInit = false;
    @Deprecated
    private boolean status ;

    private LoginCallBack loginCallBack;
    private OnLoginSYSJCallBack onLoginSYSJCallBack;
    private LogoutCallBack logoutCallBack;
    private OnConnectErrorListener errorListener;

    public void setErrorListener(OnConnectErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    private Runnable runConnect = new Runnable() {
        @Override
        public void run() {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startConnection();
            semaphore.release();
        }
    };


    static {
        connectManager = new IMConnectManager();
        // RequestManager.getInstances().getRequestQueue().put(RequestManager.getKey(connectManager),new ArrayList<RequestCall>());
    }

    private IMConnectManager(){
        ManagerList.getInstances().addManager(this);
    }

    @Deprecated
    public void init(Context context) {
        if(isInit){
            return;
        }
        isInit = true;
        application = context;
        semaphore = new Semaphore(1);
        deflaterStanzaFilter = new DeflaterStanzaFilter();
        connectBean = PManager.getConnectConfig(application);
        connConfig = XMPPTCPConnectionConfiguration.builder()
                .setHost(connectBean.getHost())
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setPort(connectBean.getPort())
                .setDebuggerEnabled(true)
//                .setSendPresence(true)
                .setServiceName(connectBean.getServiceName())
                .build();

    }

    @Override
    public void init() {
        init(IMSdk.CONTEXT);
    }

    public static IConnect getInstances() {
        return connectManager;
    }

    private void startConnection() {

        if (StringUtil.isNull(UserBean.getMemberID())) {
            PManager.getCacheUser(application);
            if (StringUtil.isNull(UserBean.getMemberID())) {
                log("------ Error : This Acoount memberId is null ------");
                return;
            }
        }
        if (!ConnectUtil.isConnect(application)) {
            log(" ------ Error : NO NETWORK ------");
            return;
        }

        if (PManager.isOldLogin(application) && PManager.isLoginSucceed(application)) {
            log(" ------ Msg: Old acoount will connect IM of the Server ------");
            connect();
        } else if (connection == null || !connection.isConnected()) {
            if (isSYSJlLogin = sendSysjService()) {
                log(" ------ Msg: Acoount will connect IM of the Server ------");
                connect();
            } else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startConnection();
            }
        }
    }

    /**
     * 開啟登錄線程
     */
    public void runConnectThread() {
        if (!ConnectUtil.isConnect(application)) {
            return;
        }
        new Thread(runConnect).start();
    }

    /**
     * 提交信息到sysj
     * @return
     */
    private boolean sendSysjService() {
        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        StringBuilder tempParams = new StringBuilder();
        Map<String, String> map = new HashMap<String, String>();
        map.put("avatarUrl", UserBean.getAvatarUrl());
        map.put("nickname", UserBean.getNickName());
        map.put("memberId", UserBean.getMemberID());
        try {
            //处理参数
            int pos = 0;
            for (String key : map.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                //对参数进行URLEncoder
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(map.get(key), "utf-8")));
                pos++;
            }
            //补全请求地址 正式环境
            String requestUrl = String.format("http://im.17sysj.com:8080/IM/SetMemberInfo?%s", tempParams.toString());

            //測試環境
//            String requestUrl = String.format("http://192.168.48.185:8080/IM/SetMemberInfo?%s", tempParams.toString());


            //创建一个请求
//                String content = "?memberId=" + a + "&nickname=" + b + "&avatarUrl=" + c;
            Request request = new Request.Builder()
//                    .addHeader("Connection", "keep-alive")
//                    .addHeader("accept", "*/*")
//                    .addHeader("user-agent", "Mozilla/5.0 (compatible; MSIE 6.0; Windows NT 6.1; SV1)")
                    .url(requestUrl)
                    .build();
            Response response = null;
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                final String reJson = response.body().string();
                final JSONObject jsonObject = new JSONObject(reJson);
                final Object code = jsonObject.get("code");
                if (!"200".equals(code.toString())) {
                    if (onLoginSYSJCallBack != null) {
                        onLoginSYSJCallBack.callFail(code.toString());
                    }
                    throw new Exception();
                } else {
                    if (onLoginSYSJCallBack != null) {
                        onLoginSYSJCallBack.callSuccess();
                    }
                    PManager.saveLogin(application, true);
                    log(" ------ Msg: Submit SYSJ' information successfully ------ " + jsonObject);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (onLoginSYSJCallBack != null) {
                onLoginSYSJCallBack.callFail(null);
            }
            PManager.saveLogin(application, false);
            log(" ------ Error: Submit SYSJ' information Error ------ " + e);
        }
        return false;
    }

    @Deprecated
    private boolean sendSysjService2() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("avatarUrl", UserBean.getAvatarUrl());
        map.put("nickname", UserBean.getNickName());
        map.put("memberId", UserBean.getMemberID());
        try {
            Response response = Account.sendMemberMsgToSYSJ(RequestManager.getKey(connectManager), map);
            JSONObject jsonObject = new JSONObject(response.body().string());
            final Object code = jsonObject.get("code");
            if ("200".equals(code.toString())) {
                if (onLoginSYSJCallBack != null) {
                    onLoginSYSJCallBack.callSuccess();
                }
                return true;
            } else {
                onLoginSYSJCallBack.callFail(code.toString());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    private synchronized boolean connect() {
        try {
            if (!ConnectUtil.isConnect(application)) {
                return false;
            }
            if (connection == null) {
                connection = new XMPPTCPConnection(connConfig);
                connection.addConnectionListener(IMConnectManager.this);
//                    reconnectionManager = ReconnectionManager.getInstanceFor(connection);
//                    reconnectionManager.setFixedDelay(5);//重联间隔
//                    reconnectionManager.enableAutomaticReconnection();
                connection.connect();
            } else if (!connection.isConnected()) {
                connection.connect();
            }
            return true;
        } catch (Exception e) {
            log(" ------ Error:The account connection IM of the server failed ------ " + e);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            connect();
        }
        return false;

    }

    public void close() {
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
    }

    @Override
    public boolean isInitialized() {
        return connectManager != null;
    }

    public class DeflaterStanzaFilter implements StanzaFilter {
        @Override
        public boolean accept(Stanza stanza) {
            return true;
        }

    }


    public void addStanzaListener(StanzaListener stanzaListener, DeflaterStanzaFilter deflaterStanzaFilter) {
        if (connection != null) {
            connection.removeAsyncStanzaListener(stanzaListener);
            connection.addAsyncStanzaListener(stanzaListener, deflaterStanzaFilter);
        }
        this.stanzaListener = stanzaListener;
        this.deflaterStanzaFilter = deflaterStanzaFilter;
    }

    @Override
    public void updateLogin() {
        ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new ThreadRunnable(3) {
            @Override
            public void run() {
                if (this.count > 0) {
                    if (!(isSYSJlLogin = sendSysjService())) {
                        try {
                            Thread.sleep(5000);
                            this.count--;
                            run();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void connected(XMPPConnection c) {
        log("------ Msg: This account connect successfully ------");
        login();
    }

    private void login(){
        try {
//                AbstractXMPPConnection connection = (AbstractXMPPConnection) connection;
            if (connection != null && connection.isConnected()) {
                connection.login(UserBean.getMemberID(), UserBean.getMemberID());
                Presence presence = new Presence(Presence.Type.available);
                presence.setMode(Presence.Mode.available);
                connection.sendStanza(presence);
                connection.addAsyncStanzaListener(MessageManager.getInstances(), deflaterStanzaFilter);
//                log("------ 登陆IM成功 ------");
                loginSucceed();
            } else {

            }
            throw new SmackException.AlreadyLoggedInException();
        } catch (SmackException.AlreadyLoggedInException e) {
            log("------ Msg: This account login IM of the server successfully ------");
            loginSucceed();
        } catch (Exception e) {
            e.printStackTrace();
            log("------ Error: This account login IM of the server failure ------");
            if (loginCallBack != null) {
                loginCallBack.callFail();
            }
            try {
                Thread.sleep(5000);
                login();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void loginSucceed() {

        final LinkedList<IMWindow> imWindows = ChatWindowsManager.getInstences().getAllIMWindows();
        for (IMWindow imWindow : imWindows) {
            imWindow.loginSucceed();
        }
        if (loginCallBack != null) {
            loginCallBack.callSuccess();
        }

        for (IEmployee iEmployee : ManagerList.getInstances().getAllManager()) {
            if (iEmployee instanceof OnUpdate) {
                ((OnUpdate) iEmployee).update();
            }
        }

    }


    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {

    }

    @Override
    public void connectionClosed() {
        log("------ IM连接关闭 ------");
        if (!ConnectUtil.isConnect(application)) {
            return;
        }
//        runConnectThread();
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        log("------ IM connection Closed OnError ------");
        if (!ConnectUtil.isConnect(application)) {
            return;
        }
        if (e instanceof XMPPException.StreamErrorException) {
            log("------ This Account logined from other place ------");
            if (errorListener != null) {
                errorListener.onStreamErrorException();
            }

            log(" ----- 异地登录！！！connect = "+connection.isConnected()+" -------");

        } else {
            runConnectThread();
        }
    }

    @Override
    public void reconnectionSuccessful() {
        login();
        log("------ IM重连成功 ------");
    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionFailed(Exception e) {
        if (loginCallBack != null) {
            loginCallBack.callFail();
        }
        log("------ IM重连失败 ------");
    }

    public XMPPTCPConnection getConnection() {
        return connection;
    }

    public void disconnect() {
        if (connection != null && connection.isConnected()) {
            PManager.saveLogin(application, false);
            connection.disconnect();
            connection = null;
            if (logoutCallBack != null) {
                logoutCallBack.logoutSuccess();
                return;
            }
        }
    }

    public boolean isConnect() {
        return connection != null && connection.isConnected();
    }

    @Override
    public boolean isLogin() {
//        if(isConnect()){
//            connection.is
//        }
        return false;
    }

    public void log(String msg) {
        Log.i(TGA, msg);
    }

    public void addLoginListener(LoginCallBack loginCallBack) {
        this.loginCallBack = loginCallBack;
    }

    public void removeLoginListener() {
        this.loginCallBack = null;
    }

    @Override
    public void addOnSYSJLoginListener(OnLoginSYSJCallBack onLoginSYSJCallBack) {

        this.onLoginSYSJCallBack = onLoginSYSJCallBack;

    }

    @Override
    public void removeOnSYSJLoginListener() {
        onLoginSYSJCallBack = null;
    }


    @Override
    public void addLogoutCallBack(LogoutCallBack logoutCallBack) {
        this.logoutCallBack = logoutCallBack;
    }

    @Override
    public void removeLogoutCallBack() {
        logoutCallBack = null;
    }

}
