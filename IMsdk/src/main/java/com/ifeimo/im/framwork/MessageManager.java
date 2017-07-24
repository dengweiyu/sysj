package com.ifeimo.im.framwork;


import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.ifeimo.im.common.bean.model.ChatMsgModel;
import com.ifeimo.im.common.bean.model.GroupChatModel;
import com.ifeimo.im.common.bean.model.IMsg;
import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.bean.chat.ChatBean;
import com.ifeimo.im.common.bean.chat.GroupChatBean;
import com.ifeimo.im.common.util.ConnectUtil;
import com.ifeimo.im.common.util.Jid;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.commander.IMWindow;
import com.ifeimo.im.framwork.commander.MessageObserver;
import com.ifeimo.im.framwork.commander.IQObserver;
import com.ifeimo.im.framwork.commander.PresenceObserver;
import com.ifeimo.im.framwork.message.OnGroupItemOnClickListener;
import com.ifeimo.im.framwork.message.OnHtmlItemClickListener;
import com.ifeimo.im.framwork.message.OnChatMessageReceiver;
import com.ifeimo.im.framwork.message.OnSimpleMessageListener;
import com.ifeimo.im.framwork.message.OnUnReadChange;
import com.ifeimo.im.provider.InformationProvide;
import com.ifeimo.im.provider.business.ChatBusiness;
import com.ifeimo.im.provider.business.GroupChatBusiness;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lpds on 2017/1/11.
 */
final class MessageManager implements MessageObserver {


    public static final String TAG = "XMPP_MessageManager";
    private static final int RECEIVER_MSG = 2;
    private static final int SEND_MSG = 4;
    private static final int RESEN_MSG = 8;
    static MessageManager messageManager;

    /**
     * 接收到的消息轮询
     */
    private static Handler handler;

    /**
     * 未读数量的观察者
     */
    private ContentObserver unReadCountObserver;
    private static Handler unReadHander;

    static {
        messageManager = new MessageManager();
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                unReadHander = new Handler();
                Looper.loop();
            }
        }.start();
        new Thread() {
            @Override
            public void run() {

                Looper.prepare();

                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        try {
                            switch (msg.what) {
                                case IMWindow.CHAT_TYPE:
                                    if (msg.arg1 == RECEIVER_MSG) {
                                        ChatMsgModel chatMsgModel = ChatMsgModel.buildChatBean((org.jivesoftware.smack.packet.Message) msg.obj);
                                        if (chatMsgModel != null) {
                                            ChatBusiness.getInstances().insert(chatMsgModel);
                                            messageManager.handlerNotifycationChatMsg(chatMsgModel);
                                            if (messageManager.onSimpleMessageListener != null) {
                                                messageManager.onSimpleMessageListener.chat(chatMsgModel.getMemberId());
                                            }
                                        }
                                    } else if (msg.arg1 == SEND_MSG) {
                                        ChatBusiness.getInstances().insert((ChatMsgModel) msg.obj);
                                    }
                                    break;
                                case IMWindow.MUCCHAT_TYPE:
                                    if (msg.arg1 == RECEIVER_MSG) {
                                        GroupChatModel groupChatModel = GroupChatModel.buildMuccBean((org.jivesoftware.smack.packet.Message) msg.obj);
                                        if (groupChatModel != null) {
                                            GroupChatBusiness.getInstances().insert(groupChatModel);
                                        }
                                        if (messageManager.onSimpleMessageListener != null) {
                                            messageManager.onSimpleMessageListener.groupChat(groupChatModel.getMemberId(), groupChatModel.getRoomid());
                                        }
                                    } else if (msg.arg1 == SEND_MSG) {
                                        GroupChatBusiness.getInstances().insert((GroupChatModel) msg.obj);
                                    }
                                    break;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.i(TAG, " ----- XML Format Error -----" + msg.obj);
                        }
                    }
                };

                Looper.loop();


            }
        }.start();
    }


    private MessageManager() {
        ManagerList.getInstances().addManager(this);
        unReadCountObserver = new ContentObserver(unReadHander) {
            @Override
            public void onChange(boolean selfChange) {
                checkUnRead();
            }
        };
        IMSdk.CONTEXT.getContentResolver().registerContentObserver(InformationProvide.CONTENT_URI, true, unReadCountObserver);
        onChatMessageReceivers = new HashSet<OnChatMessageReceiver>();
    }

    public static MessageObserver getInstances() {
        return messageManager;
    }

    private Map<String, ChatBean> singleChatSet = new HashMap<String, ChatBean>() {
        @Override
        public ChatBean put(String key, ChatBean value) {
            synchronized (this) {
                return super.put(key, value);
            }
        }

        @Override
        public ChatBean remove(Object key) {
            synchronized (this) {
                return super.remove(key);
            }
        }

        @Override
        public boolean containsKey(Object key) {
            synchronized (this) {
                return super.containsKey(key);
            }
        }

        @Override
        public void clear() {
            synchronized (this) {
                super.clear();
            }
        }
    };
    private Map<String, GroupChatBean> groupChatSet = new HashMap<String, GroupChatBean>() {
        @Override
        public GroupChatBean put(String key, GroupChatBean value) {
            synchronized (this) {
                return super.put(key, value);
            }
        }

        @Override
        public GroupChatBean remove(Object key) {
            synchronized (this) {
                return super.remove(key);
            }
        }

        @Override
        public boolean containsKey(Object key) {
            synchronized (this) {
                return super.containsKey(key);
            }
        }

        @Override
        public void clear() {
            synchronized (this) {
                super.clear();
            }
        }
    };
//    /**
//     * 无响应队列
//     */
//    @Deprecated
//    private Map<String, Map<String, IMsg>> unMessageList = new HashMap<>();

    private Set<OnChatMessageReceiver> onChatMessageReceivers;
    private OnSimpleMessageListener onSimpleMessageListener;
    private OnUnReadChange onUnRead = new OnUnReadChange() {
        @Override
        public void change(int count) {
            Log.d(TAG, "change: unread max count " + count);
        }
    };
    private OnGroupItemOnClickListener onGroupItemOnClickListener;
    private OnHtmlItemClickListener onHtmlItemClickListener;

    /**
     * 创建群聊
     *
     * @param roomid
     */
    @Override
    public GroupChatBean createGruopChat(String roomid) {
            GroupChatBean groupChatBean = null;
            if (groupChatSet.containsKey(roomid)) {
                groupChatBean = groupChatSet.get(roomid);
            } else {
                try {
                    if (Proxy.getConnectManager().isConnect()) {
                        groupChatBean = new GroupChatBean(UserBean.getMemberID(), roomid, null,
                                MultiUserChatManager.getInstanceFor(Proxy.getConnectManager().getConnection()).getMultiUserChat(Jid.getRoomJ(IMSdk.CONTEXT, roomid)));
                        joinRoom(groupChatBean.getMultiUserChat());
                        groupChatSet.put(roomid, groupChatBean);
                        Log.i(TAG, "createMucc: 创建房间 " + roomid);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return groupChatBean;
    }

    @Override
    public Collection<GroupChatBean> getAllGroups() {
        return groupChatSet.values();
    }

    @Override
    public Set<String> getAllRoomKeys() {
        return groupChatSet.keySet();
    }

    /**
     * 进入房间
     *
     * @param multiUserChat
     */
    private synchronized void joinRoom(MultiUserChat multiUserChat) {
        if (multiUserChat != null) {
            try {
                multiUserChat.join(UserBean.getMemberID());
                Log.i(TAG, "joinRoom: 进入房间 ");
            } catch (SmackException.NoResponseException e) {
                e.printStackTrace();
            } catch (XMPPException.XMPPErrorException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建单聊
     *
     * @param receiverID 对方用户
     * @param memberid   自己
     * @return
     */
    public ChatBean createChat(String receiverID, String memberid) {
        final String key = memberid + receiverID;
        if (singleChatSet.containsKey(key)) {
            ChatBean chatBean = (ChatBean) singleChatSet.get(key).clone();
            handler.removeCallbacks(singleChatSet.get(key).getRunnable());
            Log.i(TAG, "------- Find Chat By receiverID = " + receiverID + " --------");
            return chatBean;
        } else {
            ChatBean chatBean = new ChatBean(receiverID, memberid, null);
            try {
                initChat(chatBean);
                ChatBean c2 = (ChatBean) chatBean.clone();
                singleChatSet.put(key, c2);
                Log.i(TAG, "------- create Chat by receiverID = " + receiverID + " --------");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return chatBean;
        }
    }

    /**
     * 初始化单聊
     *
     * @param chatBean
     */
    private void initChat(ChatBean chatBean) {
        Chat chat = chatBean.getChat();
        if (chat == null) {
            chat = ChatManager.getInstanceFor(
                    IMConnectManager.getInstances().
                            getConnection()).createChat(Jid.getJid(IMSdk.CONTEXT, chatBean.getAccount()));
            Log.i(TAG, "------- Join Chat Opposide ID = " + chatBean.getAccount() + "  --------");
        }
        chatBean.setChat(chat);
    }

    public void sendChatMsg(String key, final ChatMsgModel msg) {
        final String finalKey = key;
        ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
            @Override
            public void run() {
                final ChatMsgModel msgBean = msg;
                org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
                ReSenRunnable reSenRunnable = new ReSenRunnable(IMWindow.CHAT_TYPE, finalKey, message, new Runnable() {
                    @Override
                    public void run() {
                        msgBean.setSendType(Fields.MsgFields.SEND_UNCONNECT);
                        sendMessageToHandler(IMWindow.CHAT_TYPE, SEND_MSG, RESEN_MSG, msgBean);
                    }
                }) {
                    @Override
                    public void sendFinish() {
                        msgBean.setSendType(Fields.MsgFields.SEND_FINISH);
                        sendMessageToHandler(IMWindow.CHAT_TYPE, SEND_MSG, RESEN_MSG, msgBean);
                    }
                };
//                handler.postDelayed(reSenRunnable.runnable, WAITING_TIME);
                try {
                    message.setBody(msgBean.getContent());
                    msgBean.setSendType(Fields.MsgFields.SEND_WAITING);
                    msgBean.setMsgId(message.getPacketID());
                    sendMessageToHandler(IMWindow.CHAT_TYPE, SEND_MSG, 0, msgBean);

                    getChatByChatSet(finalKey).getChat().sendMessage(message);

                    msgBean.setSendType(Fields.MsgFields.SEND_FINISH);
                    sendMessageToHandler(IMWindow.CHAT_TYPE, SEND_MSG, RESEN_MSG, msgBean);

                } catch (Exception e) {
                    ThreadUtil.getInstances().createThreadStartToCachedThreadPool(reSenRunnable);

                }
            }
        });


    }

    @Override
    public void sendMuccMsg(String key, final GroupChatModel msg) {
        final String finalKey = key;
        ThreadUtil.getInstances().createThreadStartToFixedThreadPool(new Runnable() {
            @Override
            public void run() {
                org.jivesoftware.smack.packet.Message message =
                        new org.jivesoftware.smack.packet.Message();
                ReSenRunnable reSenRunnable = new ReSenRunnable(IMWindow.MUCCHAT_TYPE, finalKey, message, new Runnable() {
                    @Override
                    public void run() {
                        msg.setSendType(Fields.MsgFields.SEND_UNCONNECT);
                        msg.setCreateTime(System.currentTimeMillis() + "");
                        sendMessageToHandler(IMWindow.MUCCHAT_TYPE, SEND_MSG, 0, msg);
                    }


                }) {
                    @Override
                    public void sendFinish() {
//                        waitCheck(imWindow.getKey(), msg);
                    }
                };
                handler.postDelayed(reSenRunnable.runnable, WAITING_TIME);


                msg.setSendType(Fields.MsgFields.SEND_WAITING);
                try {
                    message.setBody(msg.getContent());
                    msg.setMsgId(message.getPacketID());
                    sendMessageToHandler(IMWindow.MUCCHAT_TYPE, SEND_MSG, 0, msg);
                    createGruopChat(finalKey).getMultiUserChat().sendMessage(message);
                } catch (Exception e) {
                    ThreadUtil.getInstances().createThreadStartToFixedThreadPool(reSenRunnable);
                }

            }
        });

    }

    /**
     * 重发单聊
     *
     * @param key
     * @param msg
     */
    public void reSendChatMsg(String key, final ChatMsgModel msg) {
        final String finalKey = key;
        ThreadUtil.getInstances().createThreadStartToFixedThreadPool(new Runnable() {
            @Override
            public void run() {
                final ChatMsgModel msgBean = msg;
                org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
                message.setPacketID(msg.getMsgId());
                message.setBody(msg.getContent());
                ReSenRunnable reSenRunnable = new ReSenRunnable(IMWindow.CHAT_TYPE, finalKey, message, new Runnable() {
                    @Override
                    public void run() {
                        msgBean.setSendType(Fields.MsgFields.SEND_UNCONNECT);
                        sendMessageToHandler(IMWindow.CHAT_TYPE, SEND_MSG, RESEN_MSG, msgBean);
                    }
                }) {
                    @Override
                    public void sendFinish() {
                        msgBean.setSendType(Fields.MsgFields.SEND_FINISH);
                        sendMessageToHandler(IMWindow.CHAT_TYPE, SEND_MSG, RESEN_MSG, msg);
                    }
                };
//                handler.postDelayed(reSenRunnable.runnable,WAITING_TIME);
                try {
                    msgBean.setCreateTime(System.currentTimeMillis() + "");
                    msgBean.setSendType(Fields.MsgFields.SEND_WAITING);
                    sendMessageToHandler(IMWindow.CHAT_TYPE, SEND_MSG, RESEN_MSG, msg);

                    singleChatSet.get(finalKey).getChat().sendMessage(msg.getContent());
                    msgBean.setSendType(Fields.MsgFields.SEND_FINISH);
                    sendMessageToHandler(IMWindow.CHAT_TYPE, SEND_MSG, RESEN_MSG, msg);
                } catch (Exception e) {
                    ThreadUtil.getInstances().createThreadStartToFixedThreadPool(reSenRunnable);
                }
            }
        });
    }

    /**
     * 重发群聊
     *
     * @param key
     * @param groupChatModel
     */
    public void reSendMuccMsg(String key, final GroupChatModel groupChatModel) {
        final String finalKey = key;
        ThreadUtil.getInstances().createThreadStartToFixedThreadPool(new Runnable() {
            @Override
            public void run() {
                groupChatModel.setSendType(Fields.MsgFields.SEND_WAITING);
                ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
                    @Override
                    public void run() {
//                        MsgService.upDataMucc(msg);
                        GroupChatBusiness.getInstances().insert(groupChatModel);
                    }
                });
                org.jivesoftware.smack.packet.Message message = createMessage(groupChatModel);
                ReSenRunnable reSenRunnable = new ReSenRunnable(IMWindow.MUCCHAT_TYPE, finalKey, message, new Runnable() {
                    @Override
                    public void run() {
                        groupChatModel.setSendType(Fields.MsgFields.SEND_UNCONNECT);
                        groupChatModel.setCreateTime(System.currentTimeMillis() + "");
                        sendMessageToHandler(IMWindow.MUCCHAT_TYPE, SEND_MSG, 0, groupChatModel);
                    }
                }) {
                    @Override
                    public void sendFinish() {
//                        waitCheck(imWindow.getKey(), msg);
                    }
                };
                handler.postDelayed(reSenRunnable.runnable, WAITING_TIME);
                try {
                    createGruopChat(finalKey).getMultiUserChat().sendMessage(message);
                } catch (Exception e) {
                    ThreadUtil.getInstances().createThreadStartToFixedThreadPool(reSenRunnable);
                }
            }
        });
    }

    /**
     * 创建简单消息
     *
     * @param msgBean
     * @return
     */
    private org.jivesoftware.smack.packet.Message createMessage(IMsg msgBean) {
        org.jivesoftware.smack.packet.Message message =
                new org.jivesoftware.smack.packet.Message();
        message.setBody(msgBean.getContent());
        message.setPacketID(msgBean.getMsgId());
        return message;
    }

    /**
     * 发送handler去处理消息
     *
     * @param what
     * @param arg1
     * @param arg2
     * @param obj
     */
    private void sendMessageToHandler(int what, int arg1, int arg2, Object obj) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        handler.sendMessage(message);
    }

    /**
     * @param what
     * @param arg1
     * @param arg2
     * @param obj
     * @param time
     */
    private void sendMessageToHandler(int what, int arg1, int arg2, Object obj, int time) {
        final Message message = handler.obtainMessage();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendMessage(message);
            }
        }, time);
    }

    /**
     * 发送单聊消息到notify管理者
     *
     * @param msgBean
     */
    private void handlerNotifycationChatMsg(ChatMsgModel msgBean) {
        IMNotificationManager.getInstances().
                notifyMessageNotification2(msgBean);
    }

    /**
     * 清空群聊，单聊
     */
    public void releaseAllChat() {
        Set<String> keys = new HashSet<>(singleChatSet.keySet());
        for (String key : keys) {
            ChatBean chatBean = singleChatSet.get(key);
            if (chatBean != null) {
                handler.removeCallbacks(chatBean.getRunnable());
                chatBean.getRunnable().run();
            }
        }
        if (ConnectUtil.isConnect(IMSdk.CONTEXT)) {
            Set<String> groupkeys = new HashSet<>(groupChatSet.keySet());
            for (String key : groupkeys) {
                if (groupChatSet.get(key).getMultiUserChat().isJoined()) {
                    try {
                        groupChatSet.get(key).getMultiUserChat().leave();
                        removeGroupChat(key);
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            groupChatSet.clear();
        }
        Log.i(TAG, "--------------- 清空群聊 groupChatSet.size = "+groupChatSet.size()+"，" +
                "单聊 singleChatSet.size = "+ singleChatSet.size()+"-----------------");
    }


    @Deprecated
    public void leaveMuccRoom(String key) {
        Log.i(TAG, "leaveMuccRoom: 已过期");
        try {
            if (groupChatSet.containsKey(key)) {
                GroupChatBean mBean = groupChatSet.get(key);
                if (mBean != null) {
                    MultiUserChat mChat = mBean.getMultiUserChat();
                    if (mChat != null && mChat.isJoined()) {
                        mChat.removeMessageListener(this);
                        mChat.leave();
                    }
                    mBean.setMultiUserChat(null);
                    groupChatSet.remove(key);
                }
                Log.i(TAG, " ------- 离开群聊 --------");
            }
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 离开单聊
     *
     * @param key 缓存的 key  = 发送者id+接受者id；
     */
    @Override
    public void leaveChat(String key) {
        Log.i(TAG, " ------- Postpone Chat Leave  --------");
        ChatBean chatBean = getChatByChatSet(key);
        if (chatBean != null) {
            handler.postDelayed(chatBean.getRunnable(), DEFAULT_CACHE_TIME);
        }
    }

    @Override
    public ChatBean getChatByChatSet(String key) {
        if (singleChatSet.containsKey(key)) {
            return singleChatSet.get(key);
        }
        return null;
    }

    @Deprecated
    public void removeChatSet(String key) {
        if (singleChatSet.containsKey(key)) {
            singleChatSet.remove(key);
            Log.i(TAG, " ------- Delete Cache Chat " + key + " , singleChatSet.size = " + singleChatSet.size() + " Now --------");
        }
    }

    @Override
    public GroupChatBean getGroupChatBean(String key) {
        return groupChatSet.get(key);
    }

    @Override
    public void removeGroupChat(String key) {
        groupChatSet.remove(key);
    }

    @Override
    @Deprecated
    public void processMessage(final org.jivesoftware.smack.packet.Message message) {

    }

    /**
     * 接受到了IM消息，消息处理
     *
     * @param message
     * @throws SmackException.NotConnectedException
     */
    @Override
    public void processPacket(Stanza message) throws SmackException.NotConnectedException {
        if (message instanceof org.jivesoftware.smack.packet.Message) {
            org.jivesoftware.smack.packet.Message.Type t = ((org.jivesoftware.smack.packet.Message) message).getType();
            switch (t) {
                case groupchat: {
                    Log.i(TAG, " ------ Receiver Group Chat Messages ------- \n" + message);
                    sendMessageToHandler(IMWindow.MUCCHAT_TYPE, RECEIVER_MSG, 0, message, 300);
                }
                break;
                case chat: {
                    Log.i(TAG, " ------- Receiver Single Chat Messages  ------- \n" + message);
                    sendMessageToHandler(IMWindow.CHAT_TYPE, RECEIVER_MSG, 0, message);
                }
                break;
                case error:
                    Log.i(TAG, " ------- Receiver Error Messages  ------- \n" + message);
                    break;
            }
        } else if (message instanceof Presence) {
            final Presence presence = (Presence) message;
            switch (presence.getType()) {
                case error:
                    Log.i(TAG, " ------- Receiver IM Server Presence Error Messages ------- \n" + message);
                    break;
                default:
                    Log.i(TAG, " ------- Receiver IM Server Presence Messages ------- \n" + message);
                    for (PresenceObserver observer : ManagerList.managerList.getAllHandlerMessageLeader(PresenceObserver.class)) {
                        observer.onMessage(presence);
                    }
                    break;
            }
        } else if (message instanceof IQ) {
            final IQ iq = (IQ) message;
            switch (iq.getType()) {
                case error:
                    Log.i(TAG, " ------- Receiver IM Server IQ Error Messages ------- \n" + message);
                    break;
                default:
//                    Log.i(TAG, " ------- Receiver IM Server IQ Messages ------- \n" + message);
                    for (IQObserver observer : ManagerList.managerList.getAllHandlerMessageLeader(IQObserver.class)) {
                        observer.onMessage(iq);
                    }
                    break;
            }

        }

    }


    @Override
    public boolean isInitialized() {
        return messageManager != null;
    }

    @Override
    public void update() {
    }


    @Override
    public boolean registerOnMessageReceiver(OnChatMessageReceiver onChatMessageReceiver) {
        return onChatMessageReceivers.add(onChatMessageReceiver);
    }

    @Override
    public boolean removeOnMessageReceiver(OnChatMessageReceiver onChatMessageReceiver) {
        return onChatMessageReceivers.remove(onChatMessageReceiver);
    }

    @Override
    public void onMessage(org.jivesoftware.smack.packet.Message stanza) {

    }

    /**
     * 重发机制runnable
     */
    private abstract class ReSenRunnable implements Runnable {
        private int recount = 1;
        private String key;
        private org.jivesoftware.smack.packet.Message message;
        private int type;
        Runnable runnable;

        public ReSenRunnable(int type, String key, org.jivesoftware.smack.packet.Message message, Runnable runnable) {
            this.message = message;
            this.key = key;
            this.runnable = runnable;
            this.type = type;
        }

        @Override
        public void run() {
            go();
        }

        private void go() {
            Log.e(TAG + "12", "------ Trying to send again  ." + message.getPacketID() + "  " + message.getBody() + " The " + recount + " Count -------");
            try {
                switch (type) {
                    case IMWindow.CHAT_TYPE:
                        ChatBean chatBean = singleChatSet.get(key);
                        chatBean.getChat().sendMessage(message);
                        handler.removeCallbacks(runnable);
                        break;
                    case IMWindow.MUCCHAT_TYPE:
                        IMWindow imWindow = ChatWindowsManager.getInstances().getLastWindow();
                        if (imWindow == null || !imWindow.getKey().equals(key)) {
                            Log.e(TAG + "12", "------ Key Matching Error" + ".  The " + recount + " Count -------");
                            handler.removeCallbacks(runnable);
                            runnable.run();
                            return;
                        }
                        GroupChatBean groupChatBean = createGruopChat(imWindow.getKey());
                        if(groupChatBean != null) {
                            groupChatBean.getMultiUserChat().sendMessage(message);
                        }else{
                            runnable.run();
                            handler.removeCallbacks(runnable);
                            return;
                        }
                        break;
                }
                sendFinish();
            } catch (Exception e) {
                e.printStackTrace();
                if (recount > 3 || !ConnectUtil.isConnect(IMSdk.CONTEXT)) {
                    Log.e(TAG + "12", "------ Has more than the largest number ." + message.getPacketID() + "  The " + recount + " Count -------");
                    handler.removeCallbacks(runnable);
                    runnable.run();
                    return;
                }
                recount++;
                try {
                    Thread.sleep(1300);
                    go();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

        public abstract void sendFinish();

    }

    @Override
    @Deprecated
    public void registerOnMessageReceiver(OnSimpleMessageListener onSimpleMessageListener) {
        this.onSimpleMessageListener = onSimpleMessageListener;
    }

    @Override
    @Deprecated
    public OnSimpleMessageListener getOnChatMessageReceiver() {
        return onSimpleMessageListener;
    }

    @Override
    public void onUnReadChange(OnUnReadChange onUnRead) {
        this.onUnRead = onUnRead;
    }

    private synchronized void checkUnRead() {
        Log.d(TAG, "checkUnRead: information update");
        if (StringUtil.isNull(UserBean.getMemberID())) {
            return;
        }
        if (onUnRead != null) {
            onUnRead.change(GroupChatBusiness.getInstances().getMaxUnRead());
        }
    }


    @Override
    public ContentObserver getUnReadObserver() {
        return unReadCountObserver;
    }

    /**
     * 设置群聊图像点击回调
     *
     * @param onGroupItemOnClickListener
     */
    @Override
    @Deprecated
    public void setOnGroupItemOnClickListener(OnGroupItemOnClickListener onGroupItemOnClickListener) {
        this.onGroupItemOnClickListener = onGroupItemOnClickListener;
    }

    @Override
    @Deprecated
    public OnGroupItemOnClickListener getOnGroupItemOnClickListener() {
        return onGroupItemOnClickListener;
    }

    @Override
    @Deprecated
    public OnHtmlItemClickListener getOnHtmlItemClickListener() {
        return onHtmlItemClickListener;
    }

    @Override
    @Deprecated
    public void setOnHtmlItemClickListener(OnHtmlItemClickListener onHtmlItemClickListener) {
        this.onHtmlItemClickListener = onHtmlItemClickListener;
    }
}
