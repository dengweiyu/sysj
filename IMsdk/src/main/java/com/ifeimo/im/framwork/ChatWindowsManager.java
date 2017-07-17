package com.ifeimo.im.framwork;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.callback.LoginCallBack;
import com.ifeimo.im.common.util.AppUtil;
import com.ifeimo.im.framwork.commander.IHierarchy;
import com.ifeimo.im.framwork.commander.ILife;
import com.ifeimo.im.framwork.commander.IMMain;
import com.ifeimo.im.framwork.commander.IMWindow;


import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by lpds on 2017/1/10.
 */
final class ChatWindowsManager implements IHierarchy, LoginCallBack, ILife{
    private static ChatWindowsManager chatWindowsManager;
    private LinkedList<IMWindow> windowses;
    private Set<String> chats = new LinkedHashSet<>();
    public static final String TGA = "XMPP_ChatWindowsManage";
    private Handler mainHandler;

    static {
        chatWindowsManager = new ChatWindowsManager();
    }

    private ChatWindowsManager() {
        windowses = new LinkedList();
        mainHandler = new Handler(Looper.getMainLooper());
        ManagerList.getInstances().addManager(this);
    }

    public static IHierarchy getInstances() {
        return chatWindowsManager;
    }


    /**
     * 添加聊天窗口
     *
     * @param imMain
     */
    public void onCreate(IMMain imMain) {
        if (checkThisMain(imMain)) {
            windowses.add(imMain.getIMWindow());
            String im;
            if (imMain.getIMWindow().getType() == IMWindow.CHAT_TYPE) {
                im = " Single chat";
            } else {
                im = " Group chat ";
            }
            imMain.getIMWindow().cancelInformation();
            chats.add(imMain.getIMWindow().getKey());
            Log.i(TGA, "------ This account " + UserBean.getMemberID() + " join " + im + "------ size = " + windowses.size());
        }
    }

    public void onResume(IMMain imMain) {
        if (IMConnectManager.getInstances().getConnection() == null) {
            IMConnectManager.getInstances().runConnectThread();
        }
        if (checkThisMain(imMain)) {
            imMain.getIMWindow().clearAllNotify();
        }
    }

    public IMWindow getFirstWindow() {
        if (windowses == null || windowses.size() == 0) {
            return null;
        }
        return windowses.getFirst();
    }

    @Override
    public IMWindow getLastWindow() {
        if (windowses == null || windowses.size() == 0) {
            return null;
        }
        return windowses.getLast();
    }

    @Override
    public void releaseAll() {
        for (IMWindow imWindow : windowses) {
            if (imWindow.getContext() instanceof Activity) {
                ((Activity) imWindow.getContext()).finish();
            }
        }
    }

    @Override
    public boolean isHadWindow(Object o) {
        final LinkedList<IMWindow> list = (LinkedList<IMWindow>) windowses.clone();
        for (IMWindow imWindow : list) {
            if (o.equals(imWindow.getKey())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IMWindow getThisShowWindow() {
        IMWindow imWindow = getLastWindow();
        if (imWindow != null && AppUtil.isAppInForeground(imWindow.getContext())) {
            return imWindow;
        }
        return null;
    }

    @Override
    public Set<String> allKeys() {
        return new HashSet<>(chats);
    }

    /**
     * 销毁窗口
     *
     * @param imMain
     */
    public void onDestroy(IMMain imMain) {
        if (checkThisMain(imMain)) {
            imMain.getIMWindow().close();
            windowses.remove(imMain);
            chats.remove(imMain.getIMWindow().getKey());
            Log.i(TGA, "------ This IMWindow will exit ! " + imMain.getIMWindow().getIMWindow().getType() + "------ \n Then This Windows  size = " + windowses.size());
        }
    }

    @Override
    public void onPause(IMMain imWindow) {
    }

    @Override
    public void onStop(IMMain imWindow) {
    }

    /**
     * 刷新connection
     */
    @Deprecated
    public void refreshCon() {
        IMWindow imWindow = getFirstWindow();
        if (imWindow != null) {
            if (imWindow.getContext() != null) {
//                IMkit.getInstance().reconnect(imWindow.getContext(), this);
            }
        }
    }

    @Override
    public void callSuccess() {
        IMWindow imWindow = getLastWindow();
        if (imWindow != null) {
            imWindow.loginSucceed();
        }
    }

    @Override
    public void callFail() {

    }

    public LinkedList<IMWindow> getAllIMWindows() {
        return new LinkedList<>(windowses);
    }

    @Override
    public boolean isInitialized() {
        return windowses.size() > 0;
    }

    private boolean checkThisMain(IMMain imMain) {
        return imMain != null && imMain.getIMWindow() != null;
    }

}
