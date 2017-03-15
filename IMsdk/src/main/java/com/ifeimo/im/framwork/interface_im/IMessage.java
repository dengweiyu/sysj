package com.ifeimo.im.framwork.interface_im;

import android.content.Loader;
import android.database.ContentObserver;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.OnUpdate;
import com.ifeimo.im.common.bean.MsgBean;
import com.ifeimo.im.common.bean.MuccMsgBean;
import com.ifeimo.im.common.bean.chat.ChatBean;
import com.ifeimo.im.common.bean.chat.MuccBean;
import com.ifeimo.im.common.callback.OnSendCallBack;
import com.ifeimo.im.framwork.message.OnGroupItemOnClickListener;
import com.ifeimo.im.framwork.message.OnMessageReceiver;
import com.ifeimo.im.framwork.message.OnSimpleMessageListener;
import com.ifeimo.im.framwork.message.OnUnReadChange;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.StanzaListener;

/**
 * Created by lpds on 2017/1/17.
 */
public interface IMessage extends StanzaListener, MessageListener, IEmployee, OnUpdate {

    int DEFAULT_CACHE_TIME = 2 * 60 * 1000;
    int WAITING_TIME = 5000;

    int UN_CONNECT_STATUS = 0x120;
//    int CONNEXT_STATUS = 0x121;
    int DIFFERENT_LOGIN_STATUS = 0x122;
    int LOGIN_STATUS = 0x123;
//    int

    /**
     * 离开群聊
     *
     * @param imWindow
     */
    void leaveMuccRoom(IMWindow imWindow);

    /**
     * 离开单聊
     * DEFAULT_CACHE_TIME = 3分钟
     * Chat对象 默认缓存 DEFAULT_CACHE_TIME 分钟
     *
     * @param key 缓存的 key  = 发送者id+接受者id；
     */
    void leaveChat(String key);

    /**
     * 获得缓存的单聊的 ChatBean ,不允许修改bean信息
     *
     * @param key
     * @return
     */
    ChatBean getChatByChatSet(String key);

    /**
     * 尽量在删除单聊缓存 ChatBean之前，要调用 ChatBean.getChat().close() 方法；
     *
     * @param key
     */
    void removeChatSet(String key);
//
//    Loader loadChatData(IMWindow imWindow);
//
//    Loader loadMuccData(IMWindow imWindow);

    /**
     * 创建一个单聊
     *
     * @param imWindow
     * @param receiverID 对方用户
     * @param memberid   自己
     * @return
     */
    ChatBean createChat(IMWindow imWindow, String receiverID, String memberid);

    /**
     * 发送群聊消息
     *
     * @param key
     * @param muccBean
     * @param msg
     */
    void sendMuccMsg(String key, MuccBean muccBean, MsgBean msg);

    /**
     * 发送单聊消息
     *
     * @param key
     * @param msg
     */
    void sendChatMsg(String key, MsgBean msg);

    /**
     * 重发单聊消息
     *
     * @param key
     * @param msg
     */
    void reSendChatMsg(String key, MsgBean msg);

    /**
     * 重发群聊消息
     *
     * @param key
     * @param msg
     */
    void reSendMuccMsg(String key,MuccBean muccBean, MsgBean msg);

    /**
     * 註冊消息監聽事件
     * @param onMessageReceiver
     */
    void registerOnMessageReceiver(OnMessageReceiver onMessageReceiver);

    void removeOnMessageReceiver();

    /**
     * 释放所有chat缓存
     */
    void releaseAllChat();

    void registerOnMessageReceiver(OnSimpleMessageListener onSimpleMessageListener);
    OnSimpleMessageListener getOnMessageReceiver();

    void onUnReadChange(OnUnReadChange onUnReadChange);

    ContentObserver getUnReadObserver();

    void setOnGroupItemOnClickListener(OnGroupItemOnClickListener onGroupItemOnClickListener);

    OnGroupItemOnClickListener getOnGroupItemOnClickListener();
}
