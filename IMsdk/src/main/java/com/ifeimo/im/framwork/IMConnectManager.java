package com.ifeimo.im.framwork;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.ifeimo.im.BuildConfig;
import com.ifeimo.im.IEmployee;
import com.ifeimo.im.OnOutIM;
import com.ifeimo.im.common.bean.AccountBean;
import com.ifeimo.im.common.bean.ConnectBean;
import com.ifeimo.im.common.bean.model.AccountModel;
import com.ifeimo.im.common.callback.LoginCallBack;
import com.ifeimo.im.common.callback.LogoutCallBack;
import com.ifeimo.im.common.util.ConnectUtil;
import com.ifeimo.im.common.util.PManager;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.connect.ConnectSupport;
import com.ifeimo.im.framwork.connect.IConnectSupport;
import com.ifeimo.im.framwork.connect.MemberInfoObserver;
import com.ifeimo.im.framwork.connect.OnConnectErrorListener;
import com.ifeimo.im.framwork.commander.IConnect;
import com.ifeimo.im.framwork.commander.IMWindow;
import com.ifeimo.im.OnUpdate;
import com.ifeimo.im.framwork.message.PresenceMessageManager;
import com.ifeimo.im.framwork.request.Account;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import okhttp3.Response;
import y.com.sqlitesdk.framework.business.CenterServer;

/**
 * Created by lpds on 2017/1/11.
 * 管理IM连接
 */
final class IMConnectManager implements IConnect {
    private static final String TAG = "XMPP_IMConnectManager";
    private static IMConnectManager connectManager;
    private XMPPTCPConnectionConfiguration connConfig = null;
    private ReconnectionManager reconnectionManager;
    private ConnectBean connectBean = null;
    private XMPPTCPConnection connection;
    private DeflaterStanzaFilter deflaterStanzaFilter;
    private Context application;
    private boolean isSYSJlLogin = false;
    private boolean isInit = false;
    private LoginCallBack loginCallBack;
    private LogoutCallBack logoutCallBack;
    private OnConnectErrorListener errorListener;
    private HandlerThread connectHandlerthread = null;
    private Handler handler;
    private IConnectSupport iConnectSupport;
    private Set<MemberInfoObserver> memberInfoObservers;
    private AccountBean lastConnextAccount;

    public void setErrorListener(OnConnectErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    @Override
    public IConnectSupport getConnectSupport() {
        return iConnectSupport;
    }

    private Runnable runConnectRunnbale = new Runnable() {
        @Override
        public void run() {
            startConnection();
        }
    };
    private Runnable updateLoginRunnable = new Runnable() {
        @Override
        public void run() {
            if (!(isSYSJlLogin = sendSysjService2())) {
                handler.postDelayed(updateLoginRunnable, 5000);
            }
        }
    };

    static {
        connectManager = new IMConnectManager();
    }

    private IMConnectManager() {
        ManagerList.getInstances().addManager(this);
        connectHandlerthread = new HandlerThread(TAG);
        connectHandlerthread.start();
        handler = new Handler(connectHandlerthread.getLooper());
        iConnectSupport = new ConnectSupport(this);
        memberInfoObservers = new HashSet<MemberInfoObserver>() {
            @Override
            public boolean add(MemberInfoObserver memberInfoObserver) {
                synchronized (this) {
                    boolean flag = super.add(memberInfoObserver);
                    return flag;
                }
            }

            @Override
            public boolean remove(Object object) {
                synchronized (this) {
                    boolean flag = super.remove(object);
                    return flag;
                }
            }
        };
    }

    @Deprecated
    public synchronized void init(Context context) {
        if (isInit) {
            return;
        }
        isInit = true;
        application = context;
//        semaphore = new Semaphore(1);
        deflaterStanzaFilter = new DeflaterStanzaFilter();
        connectBean = PManager.getConnectConfig(application);
        connConfig = XMPPTCPConnectionConfiguration.builder()
                .setHost(connectBean.getHost())
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setPort(connectBean.getPort())
                .setDebuggerEnabled(true)
//                .setSendPresence(true)
                .setServiceName(connectBean.getServiceName())
                .setResource("android" + BuildConfig.VERSION_CODE)
                .build();
    }

    @Override
    public void init() {
        init(IMSdk.CONTEXT);
    }

    public static IConnect getInstances() {
        return connectManager;
    }

    /**
     * 开始连接 im
     */
    private void startConnection() {
        if (application == null){
            return;
        }

        if (!ConnectUtil.isConnect(application)) {
            log(" ------ Error : NO NETWORK ------");
            //return;
        }

        if (StringUtil.isNull(Proxy.getAccountManger().getUserMemberId())) {
            PManager.getCacheUser(application);
            if (StringUtil.isNull(Proxy.getAccountManger().getUserMemberId())) {
                log("------ Error : This Acoount memberId is null ------");
                return;
            }
        }

        if (PManager.isOldAcoount(application)) {
            log(" ------ Msg: Old acoount will connect IM of the Server ------");
            connect();
        } else if (connection == null || !connection.isConnected()) {
            if (isSYSJlLogin = sendSysjService2()) {
                log(" ------ Msg: Acoount will connect IM of the Server ------");
                connect();
            } else {
                handler.postDelayed(runConnectRunnbale, 5000);
            }
        }
    }

    /**
     * 開啟登錄線程
     */
    public void runConnectThread() {
        handler.removeCallbacksAndMessages(null);
        handler.post(runConnectRunnbale);
    }

    /**
     * 提交信息到sysj
     *
     * @return
     */
    private boolean sendSysjService2() {
        if (!ConnectUtil.isConnect(IMSdk.CONTEXT)) {
            return true;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("avatarUrl", Proxy.getAccountManger().getAccount(false).getAvatarUrl());
        map.put("nickname", Proxy.getAccountManger().getAccount(false).getNickName());
        map.put("memberId", Proxy.getAccountManger().getUserMemberId());
        try {
            Response response = Account.sendMemberMsgToSYSJ(this, map);
            JSONObject jsonObject = new JSONObject(response.body().string());
            final Object code = jsonObject.get("code");
            if ("200".equals(code.toString())) {
                synchronized (memberInfoObservers) {
                    for (MemberInfoObserver memberInfoObserver : memberInfoObservers) {
                        memberInfoObserver.onSucceed();
                    }
                }
                PManager.saveLogin(application);
                log(" ------ Msg: Submit SYSJ information successfully ------ " + jsonObject);
                CenterServer.getInstances().insert(new AccountModel(
                        Proxy.getAccountManger().getUserMemberId(),
                        Proxy.getAccountManger().getAccount(false).getNickName(),
                        Proxy.getAccountManger().getAccount(false).getAvatarUrl()));
                return true;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            synchronized (memberInfoObservers) {
                for (MemberInfoObserver memberInfoObserver : memberInfoObservers) {
                    memberInfoObserver.onError(e);
                }
            }
            return false;
        }


    }

    private synchronized boolean connect() {
        try {
            if (!ConnectUtil.isConnect(application)) {
                log("------ Msg: is connected ------");
                return false;
            }
            if (connection == null) {
                connection = new XMPPTCPConnection(connConfig);
                connection.addConnectionListener(IMConnectManager.this);
//                    reconnectionManager = ReconnectionManager.getInstanceFor(connection);
//                    reconnectionManager.setFixedDelay(5);//重联间隔
//                    reconnectionManager.enableAutomaticReconnection();
                connection.connect();
                log("------ Msg: connection is null  so start connecting ------");
            } else if (!connection.isConnected()) {
                connection.connect();
                log("------ Msg: disconnect so start connecting ------");
            }
            return true;
        } catch (Exception e) {
            log(" ------ Error:The account connection IM of the server failed ------ " + e);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    connect();
                }
            }, 5000);
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
        return true;
    }

    public class DeflaterStanzaFilter implements StanzaFilter {
        @Override
        public boolean accept(Stanza stanza) {
            return true;
        }

    }

    @Override
    public void updateLogin() {
        handler.post(updateLoginRunnable);
    }

    @Override
    public void connected(XMPPConnection c) {
        log("------ Msg: This account connect successfully ------");
        login();
    }

    /**
     * 登录im
     */
    private void login() {
        try {
            if (connection != null && connection.isConnected()) {
                connection.login(Proxy.getAccountManger().getUserMemberId(), Proxy.getAccountManger().getUserMemberId());
                Presence presence = new Presence(PresenceMessageManager.getInstances().getConfig().type);
                presence.setMode(PresenceMessageManager.getInstances().getConfig().mode);
                connection.sendStanza(presence);
                connection.addAsyncStanzaListener(MessageManager.getInstances(), deflaterStanzaFilter);
                loginSucceed();
            } else {
                handler.removeCallbacksAndMessages(null);
                handler.post(runConnectRunnbale);
            }
        } catch (SmackException.AlreadyLoggedInException e) {
            log("------ 返回码 " + e.getMessage() + " ------");
            log("------ Msg: This account login IM of the server successfully ------");
            loginSucceed();
        } catch (Exception e) {
            log("------ 返回码 " + e.getMessage() + " ------");
            e.printStackTrace();
            log("------ Error: This account login IM of the server failure ------");
            if (loginCallBack != null) {
                loginCallBack.callFail();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    login();
                }
            }, 5000);
        }
    }

    /**
     * 登陆成功
     */
    private void loginSucceed() {

        final LinkedList<IMWindow> imWindows = ChatWindowsManager.getInstances().getAllIMWindows();
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
        for (IEmployee iEmployee : ManagerList.getInstances().getAllManager()) {
            if (iEmployee instanceof OnOutIM) {
                ((OnOutIM) iEmployee).leaveIM();
            }
        }
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        log("------ IM connection Closed OnError ------");
        if (e instanceof XMPPException.StreamErrorException) {
            log("------ This Account logined from other place ------");
            if (errorListener != null) {
                errorListener.onStreamErrorException();
            }

            log(" ----- 异地登录！！！connect = " + connection.isConnected() + " -------");

        } else {
            if (!ConnectUtil.isConnect(application)) {
                runConnectThread();
            }
        }
        for (IEmployee iEmployee : ManagerList.getInstances().getAllManager()) {
            if (iEmployee instanceof OnOutIM) {
                ((OnOutIM) iEmployee).leaveErrorIM();
            }
        }
    }

    @Override
    public void reconnectionSuccessful() {
        login();
        log("------ IM reconnection Successful ------");
    }

    @Override
    public void reconnectingIn(int seconds) {
    }

    @Override
    public void reconnectionFailed(Exception e) {
        if (loginCallBack != null) {
            loginCallBack.callFail();
        }
        log("------ IM reconnection Failed ------");
    }

    public XMPPTCPConnection getConnection() {
        return connection;
    }

    public void disconnect() {
        for (IEmployee iEmployee : ManagerList.getInstances().getAllManager()) {
            if (iEmployee instanceof OnOutIM) {
                ((OnOutIM) iEmployee).leaveIM();
            }
        }
        if (connection != null && connection.isConnected()) {
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

    public void log(String msg) {
        Log.i(TAG, msg);
    }

    public void addLoginListener(LoginCallBack loginCallBack) {
        this.loginCallBack = loginCallBack;
    }

    public void removeLoginListener() {
        this.loginCallBack = null;
    }

    @Override
    public void addLogoutCallBack(LogoutCallBack logoutCallBack) {
        this.logoutCallBack = logoutCallBack;
    }

    @Override
    public void removeLogoutCallBack() {
        logoutCallBack = null;
    }

    @Override
    public void addMemberInfoObserver(MemberInfoObserver memberInfoObserver) {
        if (memberInfoObserver == null) {
            log("------ Msg: Add memberInfoObserver error , Because memberInfoObserver is null ------");
        } else if (memberInfoObservers.add(memberInfoObserver)) {
            log("------ Msg: Add memberInfoObserver successfully ------");
        } else {
            log("------ Msg: Add memberInfoObserver error , Because memberInfoObserver already exist ------");
        }
    }

    @Override
    public void removeMemberInfoObserver(MemberInfoObserver memberInfoObserver) {
        if (memberInfoObserver == null) {
            log("------ Msg: Remove memberInfoObserver error , Because memberInfoObserver is null ------");
        } else if (memberInfoObservers.remove(memberInfoObserver)) {
            log("------ Msg: Remove memberInfoObserver successfully ------");
        } else {
            log("------ Msg: Remove memberInfoObserver error , Because memberInfoObserver does not exist ------");
        }
    }

}
