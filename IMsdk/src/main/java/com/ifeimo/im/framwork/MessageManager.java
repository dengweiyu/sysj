package com.ifeimo.im.framwork;


import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.ifeimo.im.common.bean.MsgBean;
import com.ifeimo.im.common.bean.MuccMsgBean;
import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.bean.chat.ChatBean;
import com.ifeimo.im.common.bean.chat.MuccBean;
import com.ifeimo.im.common.util.ConnectUtil;
import com.ifeimo.im.common.util.Jid;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.database.business.Business;
import com.ifeimo.im.framwork.interface_im.IMWindow;
import com.ifeimo.im.framwork.interface_im.IMessage;
import com.ifeimo.im.framwork.message.OnGroupItemOnClickListener;
import com.ifeimo.im.framwork.message.OnHtmlItemClickListener;
import com.ifeimo.im.framwork.message.OnMessageReceiver;
import com.ifeimo.im.framwork.message.OnSimpleMessageListener;
import com.ifeimo.im.framwork.message.OnUnReadChange;
import com.ifeimo.im.provider.InformationProvide;
import com.ifeimo.im.service.MsgService;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lpds on 2017/1/11.
 */
final class MessageManager implements IMessage {

    public static final String TGA = "XMPP_MessageManager";
    private static final int RECEIVER_MSG = 2;
    private static final int SEND_MSG = 4;
    private static final int RESEN_MSG = 8;
    static MessageManager messageManager;
    private static Handler handler;
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

                                        MsgBean msgBean = MsgBean.buildChatBean((org.jivesoftware.smack.packet.Message) msg.obj);
                                        if (msgBean != null) {
                                            MsgService.insertChat(msgBean);
                                            messageManager.handlerNotifycationChatMsg(msgBean);
                                            messageManager.onSimpleMessageListener.chat(msgBean.getMemberId());
//                                            messageManager.checkUnRead();
                                        }
                                    } else if (msg.arg1 == SEND_MSG) {

                                        if (msg.arg2 == RESEN_MSG) {
                                            MsgService.upDataChat((MsgBean) msg.obj);

                                        } else {
                                            MsgService.insertChat((MsgBean) msg.obj);
                                        }
                                    }

                                    break;
                                case IMWindow.MUCCHAT_TYPE:
                                    if (msg.arg1 == RECEIVER_MSG) {
                                        MuccMsgBean muccMsgBean = MuccMsgBean.buildMuccBean((org.jivesoftware.smack.packet.Message) msg.obj);
                                        if (muccMsgBean != null) {
                                            MsgService.insertMucc(muccMsgBean);
                                        }
                                    } else if (msg.arg1 == SEND_MSG) {

//                                        if (msg.arg2 == RESEN_MSG) {
//                                            MuccMsgBean muccMsgBean = (MuccMsgBean) msg.obj;
//                                            MsgService.deleteMuccById(muccMsgBean);
//
//                                        } else {

                                        MsgService.insertMucc((MuccMsgBean) msg.obj);
//                                        }
                                    }
                                    break;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.i(TGA, " ----- XML Format Error -----" + msg.obj);
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
        IMSdk.CONTEXT.getContentResolver().registerContentObserver(InformationProvide.UnreadCount_URI,true,unReadCountObserver);

    }

    public static IMessage getInstances() {

        return messageManager;
    }

    private Map<String, ChatBean> chatSet = new HashMap<>();
    private Map<String, MuccBean> muccSet = new HashMap<>();
    /**
     * 无响应队列
     */
    @Deprecated
    private Map<String, Map<String, MsgBean>> unMessageList = new HashMap<>();

    private OnMessageReceiver onMessageReceiver;
    private OnSimpleMessageListener onSimpleMessageListener;
    private OnUnReadChange onUnRead;
    private OnGroupItemOnClickListener onGroupItemOnClickListener;
    private OnHtmlItemClickListener onHtmlItemClickListener;

    public void createMucc(IMWindow imWindow, String roomid) {
        if (!muccSet.containsKey(roomid)) {
            MuccBean muccBean = new MuccBean(UserBean.getMemberID(), roomid, null, null);
            muccSet.put(roomid, muccBean);
//            Mucm


        } else {
//            muccSet.put(roomid, muccBean);
        }
    }

    /**
     * 创建单聊
     * @param context
     * @param receiverID 对方用户
     * @param memberid   自己
     * @return
     */
    public ChatBean createChat(IMWindow context, String receiverID, String memberid) {
        final String key = memberid + receiverID;
        if (chatSet.containsKey(key)) {
            ChatBean chatBean = (ChatBean) chatSet.get(key).clone();
            handler.removeCallbacks(chatSet.get(key).getRunnable());
            Log.i(TGA, "------- Find Chat By receiverID = "+receiverID+" --------");
            return chatBean;
        } else {
            ChatBean chatBean = new ChatBean(receiverID, memberid, null);
            try {
                initChat(context, chatBean);
                ChatBean c2 = (ChatBean) chatBean.clone();
                chatSet.put(key, c2);
                Log.i(TGA, "------- create Chat by receiverID = "+receiverID+" --------");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return chatBean;
        }
    }

    /**
     * 初始化单聊
     *
     * @param context
     * @param chatBean
     */
    private void initChat(IMWindow context, ChatBean chatBean) {
        Chat chat = chatBean.getChat();
        if (chat == null) {
            chat = ChatManager.getInstanceFor(
                    IMConnectManager.getInstances().
                            getConnection()).createChat(Jid.getJid(context.getContext(), chatBean.getAccount()));
            Log.i(TGA, "------- Join Chat Opposide ID = " + chatBean.getAccount() + "  --------");
            chat.addMessageListener(new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
                    System.err.print("123123 " + message);
                }
            });
        }
        chatBean.setChat(chat);
    }

    public void sendChatMsg(String key, final MsgBean msg) {
        final String finalKey = key;
        ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
            @Override
            public void run() {
                final MsgBean msgBean = msg;
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

    @Deprecated
    public void sendMuccMsg(String key, final MuccBean muccBean, final MsgBean msg) {
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
                    muccBean.getMultiUserChat().sendMessage(message);
                    waitCheck(finalKey, msg);
                } catch (Exception e) {
                    ThreadUtil.getInstances().createThreadStartToFixedThreadPool(reSenRunnable);
                }

            }
        });

    }

    public void reSendChatMsg(String key, final MsgBean msg) {
        final String finalKey = key;
        ThreadUtil.getInstances().createThreadStartToFixedThreadPool(new Runnable() {
            @Override
            public void run() {
                final MsgBean msgBean = msg;
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

                    chatSet.get(finalKey).getChat().sendMessage(msg.getContent());
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
     * @param key
     * @param muccBean
     * @param msg
     */
    public void reSendMuccMsg(String key, final MuccBean muccBean,final MsgBean msg) {
        final String finalKey = key;
        ThreadUtil.getInstances().createThreadStartToFixedThreadPool(new Runnable() {
            @Override
            public void run() {
                msg.setSendType(Fields.MsgFields.SEND_WAITING);
                ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
                    @Override
                    public void run() {
                        MsgService.upDataMucc(msg);
                    }
                });
                org.jivesoftware.smack.packet.Message message = createMessage(msg);
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
                handler.postDelayed(reSenRunnable.runnable,WAITING_TIME);
                try {
                    muccBean.getMultiUserChat().sendMessage(message);
                    waitCheck(finalKey, msg);
                } catch (Exception e) {
                    ThreadUtil.getInstances().createThreadStartToFixedThreadPool(reSenRunnable);
                }
            }
        });
    }

    /**
     * 过期
     * @param key
     * @param msg
     */
    @Deprecated
    private void waitCheck(String key, MsgBean msg) {
        if (unMessageList.containsKey(key)) {
            Map<String, MsgBean> map = unMessageList.get(key);
            map.put(msg.getMsgId(), msg);
        } else {
            unMessageList.put(key, new HashMap<String, MsgBean>());
            this.waitCheck(key, msg);
        }
    }

    /**
     * 发送的未成功，最终退出此群的waiting状态都会自定变成失败
     *
     * @param im
     */
    private void finishUnMessageStatus(IMWindow im) {
        final String k = RequestManager.getKey(im);
        handler.post(new Runnable() {
            @Override
            public void run() {
                String key = k;
                if (unMessageList.containsKey(key)) {
                    Map<String, MsgBean> map = unMessageList.get(key);
                    unMessageList.remove(key);
                    if (map.size() > 0) {
                        Set<String> set = map.keySet();
                        for (String i : set) {
                            MsgBean msg = map.get(i);
                            if (msg.getSendType() == Fields.MsgFields.SEND_WAITING) {
                                msg.setSendType(Fields.MsgFields.SEND_UNCONNECT);
                                MsgService.upDataMucc(msg);
                            }
                        }
                        set.clear();
                    }
                }
                System.gc();
            }
        });
    }

    private org.jivesoftware.smack.packet.Message createMessage(MsgBean msgBean) {
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
     *
     * @param what
     * @param arg1
     * @param arg2
     * @param obj
     * @param time
     */
    private void sendMessageToHandler(int what, int arg1, int arg2, Object obj,int time) {
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
        },time);
    }

    private void handlerNotifycationChatMsg(MsgBean msgBean) {
        IMNotificationManager.getInstances().
                notifyMessageNotification2(msgBean);
    }

    public void releaseAllChat() {
        Set<String> keys = new HashSet<>(chatSet.keySet());
        for(String key : keys){
            ChatBean chatBean = chatSet.get(key);
            if(chatBean!=null){
                handler.removeCallbacks(chatBean.getRunnable());
                chatBean.getRunnable().run();
            }
        }
    }

    @Deprecated
    private void releaseChat() {
    }


    public void leaveMuccRoom(IMWindow imWindow) {
        if (imWindow.getType() == IMWindow.MUCCHAT_TYPE) {
            try {

                MuccBean mBean = ((MuccBean) imWindow.getBean());
                if (mBean != null) {
                    MultiUserChat mChat = mBean.getMultiUserChat();
                    if (mChat != null && mChat.isJoined()) {
                        mChat.removeMessageListener(this);
                        mChat.leave();
                        mBean.setMultiUserChat(null);
                    }
                }

                finishUnMessageStatus(imWindow);

                Log.i(TGA, " ------- 离开群聊 --------");
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }

        }
    }

    public void leaveChat(String key) {
        Log.i(TGA, " ------- Postpone Chat Leave  --------");
        ChatBean chatBean = getChatByChatSet(key);
        if (chatBean != null) {
            handler.postDelayed(chatBean.getRunnable(), DEFAULT_CACHE_TIME);
        }
    }

    @Override
    public ChatBean getChatByChatSet(String key) {
        if (chatSet.containsKey(key)) {
            return chatSet.get(key);
        }
        return null;
    }

    @Deprecated
    public void removeChatSet(String key) {
        if (chatSet.containsKey(key)) {
            chatSet.remove(key);
            Log.i(TGA, " ------- Delete Cache Chat " + key + " , chatSet.size = " + chatSet.size() + " Now --------");
        }
    }

    @Override
    @Deprecated
    public void processMessage(final org.jivesoftware.smack.packet.Message message) {
//        Log.i(TGA, " ------- 2222 --------" + message);

    }

    /**
     * 消息处理
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
                    Log.i(TGA, " ------ Receiver Group Chat Messages ------- \n" + message);

                    sendMessageToHandler(IMWindow.MUCCHAT_TYPE, RECEIVER_MSG, 0, message,300);
                    if (onMessageReceiver != null) {
                        onMessageReceiver.onMuccReceiver(message);
                    }
                }
                break;
                case chat: {
                    Log.i(TGA, " ------- Receiver Single Chat Messages  ------- \n"+message);
                    sendMessageToHandler(IMWindow.CHAT_TYPE, RECEIVER_MSG, 0, message);
                    if (onMessageReceiver != null) {
                        onMessageReceiver.onChatReceiver(message);
                    }
                }
                break;
                case error:
                    Log.i(TGA, " ------- Receiver Error Messages  ------- \n"+message);
                    break;
            }
        } else {
            Log.i(TGA, " ------- Receiver IM Server Messages ------- \n"+message);
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
    public void registerOnMessageReceiver(OnMessageReceiver onMessageReceiver) {
        removeOnMessageReceiver();
        this.onMessageReceiver = onMessageReceiver;
    }

    @Override
    public void removeOnMessageReceiver() {
        onMessageReceiver = null;
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
            Log.e(TGA + "12", "------ Trying to send again  ." + message.getPacketID() + "  " + message.getBody() + " The " + recount + " Count -------");
            try {
                switch (type) {
                    case IMWindow.CHAT_TYPE:
                        ChatBean chatBean = chatSet.get(key);
                        chatBean.getChat().sendMessage(message);
                        handler.removeCallbacks(runnable);
                        break;
                    case IMWindow.MUCCHAT_TYPE:
                        IMWindow imWindow = ChatWindowsManager.getInstences().getLastWindow();
                        if (imWindow == null || !imWindow.getKey().equals(key)) {
                            Log.e(TGA + "12", "------ Key Matching Error" + ".  The " + recount + " Count -------");
                            handler.removeCallbacks(runnable);
                            runnable.run();
                            return;
                        }
                        MuccBean muccBean = (MuccBean) imWindow.getBean();
                        muccBean.getMultiUserChat().sendMessage(message);
                        break;
                }
                sendFinish();
            } catch (Exception e) {
                e.printStackTrace();
                if (recount > 3 || !ConnectUtil.isConnect(IMSdk.CONTEXT)) {
                    Log.e(TGA + "12", "------ Has more than the largest number ." + message.getPacketID() + "  The " + recount + " Count -------");
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
    public void registerOnMessageReceiver(OnSimpleMessageListener onSimpleMessageListener) {
        this.onSimpleMessageListener = onSimpleMessageListener;
    }

    @Override
    public OnSimpleMessageListener getOnMessageReceiver() {
        return onSimpleMessageListener;
    }
    @Override
    public void onUnReadChange(OnUnReadChange onUnRead) {
        this.onUnRead = onUnRead;
    }

    private void checkUnRead(){
        if(onUnRead != null) {
            onUnRead.change(Business.getInstances().getMaxUnReadCount());
        }
    }

    @Override
    public ContentObserver getUnReadObserver() {
        return unReadCountObserver;
    }

    /**
     * 设置群聊图像点击回调
     * @param onGroupItemOnClickListener
     */
    @Override
    public void setOnGroupItemOnClickListener(OnGroupItemOnClickListener onGroupItemOnClickListener) {
        this.onGroupItemOnClickListener = onGroupItemOnClickListener;
    }

    @Override
    public OnGroupItemOnClickListener getOnGroupItemOnClickListener() {
        return onGroupItemOnClickListener;
    }

    @Override
    public OnHtmlItemClickListener getOnHtmlItemClickListener() {
        return onHtmlItemClickListener;
    }

    @Override
    public void setOnHtmlItemClickListener(OnHtmlItemClickListener onHtmlItemClickListener) {
        this.onHtmlItemClickListener = onHtmlItemClickListener;
    }
}
