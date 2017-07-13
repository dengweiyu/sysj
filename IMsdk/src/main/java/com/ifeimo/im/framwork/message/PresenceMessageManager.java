package com.ifeimo.im.framwork.message;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.ifeimo.im.IEmployee;
import com.ifeimo.im.OnOutIM;
import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.bean.message.PresenceEntity;
import com.ifeimo.im.common.bean.xml.PresenceList;
import com.ifeimo.im.common.util.XMLUtil;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.commander.PresenceObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.PresenceListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Presence;

import de.measite.minidns.Record;
import y.com.sqlitesdk.framework.Mode;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/6/15.
 * 状态管理者
 */
public final class PresenceMessageManager implements PresenceObserver, IEmployee, PresenceOperate,OnOutIM {
    private PresenceMessageManager.Config config = new Config();
    private PresenceList.Presence presenceEntity = new PresenceList.Presence();
    private Handler handler;
    /**
     * available --(默认)用户空闲状态
     unavailable--用户没空看消息
     subscribe--请求加别人为好友
     subscribed--确认别人对自己的好友请求
     unsubscribe--请求删除好友
     unsubscribed--拒绝对方的添加请求
     error --当前状态packet有错误
     */

    /**
     *
     * affiliation = 岗位
     * role = 角色
     */

    /**
     * 进入房间
     * <?xml version="1.0" encoding="utf-8"?>
     <presence to="1715698@op.17sysj.com/android" id="E8yAt-125" from="39@conference.op.17sysj.com/2165750">
     <c xmlns="http://jabber.org/protocol/caps" hash="sha-1" node="http://www.igniterealtime.org/projects/smack" ver="os2Kusj3WEOivn5n4iFr/ZEO8ls="></c>
     <x xmlns="http://jabber.org/protocol/muc#user">
     <item jid="2165750@op.17sysj.com/android" affiliation="none" role="participant"/>
     </x>
     </presence>
     */
    /**
     * 离开房间
     *<?xml version="1.0" encoding="utf-8"?>
     <presence to="1715698@op.17sysj.com/android" from="39@conference.op.17sysj.com/2165750" id="E8yAt-125" type="unavailable">
     <c xmlns="http://jabber.org/protocol/caps" hash="sha-1" node="http://www.igniterealtime.org/projects/smack" ver="os2Kusj3WEOivn5n4iFr/ZEO8ls="></c>
     <x xmlns="http://jabber.org/protocol/muc#user">
     <item affiliation="none" jid="2165750@op.17sysj.com/android" role="none"/>
     </x>
     </presence>
     */
    /**
     * 被添加好友
     * <presence to='3079470@op.17sysj.com' from='2171501@op.17sysj.com' id='kqGOf-170' type='subscribe'></presence>
     */
    private static final String TAG = "XMPP_Presencer";
    static PresenceMessageManager presenceMessageManager;

    static {
        presenceMessageManager = new PresenceMessageManager();
    }


    public static PresenceOperate getInstances(){return presenceMessageManager;}
    private PresenceMessageManager() {
//        EventBus.getDefault().register(this);
        Proxy.getManagerList().addManager(this);
        HandlerThread t = new HandlerThread("PresenceMessageManager");
        t.start();
        handler = new Handler(t.getLooper());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onPresence(){

    }

    @Override
    public void onMessage(Presence presence) {
//        String to = presence.getTo();
        String from = presence.getFrom();
//        String type = presence.getType().name();
//        String status = presence.getStatus();
//        String mode = presence.getMode().name();
//        Log.i(TAG, "onMessage: " + presence + "\n " + String.format("to = %s \n from = %s \n type = %s \n status = %s \n mode = %s",
//                to, from, type, status, mode));
        if (from.contains("@conference")) {
            group(presence);
        } else {
            person(presence);

        }
    }

    /**
     * 检查上线的用户是不是自己
     * @param presence
     */
    private void checkPreson(final Presence presence) {
        if(UserBean.getMemberID() == null){
            return;
        }
        if(!UserBean.getMemberID().contains(presence.getTo())){
            return;
        }
        final Presence.Mode m = presence.getMode();
//        handler.removeCallbacksAndMessages(null);
        handler.post(new Runnable() {
            @Override
            public void run() {
                presenceEntity.setType(Presence.Type.available);
                presenceEntity.setShow(m);
                EventBus.getDefault().post(new PresenceEntity());
            }
        });
    }

    private void person(Presence presence) {
        switch (presence.getType()) {
            /**
             * 在线状态
             */
            case available:
                if (presence.getFrom().startsWith(UserBean.getMemberID())) {
                    Log.i(TAG, "available: 你已经上线了！ " + presence.getTo());
                } else {
                    Log.i(TAG, "available: 你的好友上线了！" + presence.getTo()+" 状态为 :" + XMLUtil.simpleXML(presence.toString(),"show"));
                }
                checkPreson(presence);
                break;
            /**
             * 不接收消息状态，离线隐身
             */
            case unavailable:
                if (presence.getFrom().startsWith(UserBean.getMemberID())) {
                    Log.i(TAG, "unavailable: 你离线了 " + presence.getTo());
                } else {
                    Log.i(TAG, "unavailable: 你的好友离线了！ " + presence.getTo());
                }
                break;
            /**
             * 此情况是对方订阅你，你收到消息
             */
            case subscribe:
                Log.i(TAG, "subscribe: 此好友关注了你" + presence.getTo());
                break;
            /**
             * 自己去订阅一个伙伴，反馈的消息
             */
            case subscribed:
                Log.i(TAG, "subscribed: 你关注了你的一个新的伙伴 " + presence.getTo());
                break;
            /**
             * 仅当你订阅伙伴，并且被对面订阅的时候，被伙伴取消之后才会收到次消息
             */
            case unsubscribe:
                Log.i(TAG, "subscribe: 双方的订阅关系被解除了！ " + presence.getTo());
                break;
            /**
             * 仅当你订阅伙伴，被伙伴取消之后才会收到次消息
             */
            case unsubscribed:
                if (presence.getFrom().startsWith(UserBean.getMemberID())) {
                    Log.i(TAG, "unsubscribed: 1 解除好友关系！ " + presence.getTo());
                } else {
                    Log.i(TAG, "unsubscribed: 2 解除好友关系！ " + presence.getTo());
                }
                break;
            default:
                Log.i(TAG, "this type is "+presence.getType());
                break;
        }


    }

    /**
     * 群聊消息状态
     * @param presence
     */
    private void group(Presence presence) {
        switch (presence.getType()) {
            case unavailable:
                Log.i(TAG, "group: " + presence.getExtensions("x", "http://jabber.org/protocol/muc#user") + "离开群聊");
                break;
            case available:
                Log.i(TAG, "group: " + presence.getExtensions("x", "http://jabber.org/protocol/muc#user") + "在群聊中");
                break;
            default:
                Log.i(TAG, "this type is "+presence.getType());
                break;
        }
    }

    /**
     * 改变个人状态
     * @param t
     */
    public void modiPresenceStatus(Presence.Mode t) {
        Presence personP = new Presence(Presence.Type.available);
        personP.setMode(t);
        presenceMode(t);
        try {
            Proxy.getConnectManager().getConnection().sendStanza(personP);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PresenceList.Presence getSeftPresence() {
        return presenceEntity;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    private void presenceMode(Presence.Mode m) {
        switch (m) {
            /**
             * 默认
             */
            case available:
                Log.i(TAG, "presenceMode: available 默认");
                break;
            /**
             * 离开
             */
            case away:
                Log.i(TAG, "presenceMode: away 离开");
                break;
            /**
             * 不希望被打扰
             */
            case dnd:
                Log.i(TAG, "presenceMode: dnd 不希望被打扰");
                break;
            /**
             * 有意聊天
             */
            case chat:
                Log.i(TAG, "presenceMode: chat 有意聊天");
                break;
            /**
             * 长期离开
             */
            case xa:
                Log.i(TAG, "presenceMode: xa 长期离开");
                break;
            default:
                Log.i(TAG, "this mode is "+m);
                break;
        }


    }


    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public void leaveIM() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                /**
                 * 下线默认离开
                 */
                presenceEntity.setType(Presence.Type.unavailable);
                presenceEntity.setShow(Presence.Mode.away);
                EventBus.getDefault().post(new PresenceEntity());
            }
        });
    }

    @Override
    public void leaveErrorIM() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                presenceEntity.setType(Presence.Type.unavailable);
                presenceEntity.setShow(Presence.Mode.away);
                EventBus.getDefault().post(new PresenceEntity());
            }
        });
    }

    public static final class Config{
        public Presence.Type type = Presence.Type.available;
        public Presence.Mode mode = Presence.Mode.available;
    }



}
