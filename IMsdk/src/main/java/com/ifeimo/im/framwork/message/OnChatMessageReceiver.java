package com.ifeimo.im.framwork.message;


import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by lpds on 2017/1/11.
 */
public interface OnChatMessageReceiver {

    /**
     * 群聊消息 xml格式
     * 可用 MuccMsgBean.buildMuccBean(Stanza message) 解析
     * @param message
     */
    void onMuccReceiver(Stanza message);

    /**
     * 单聊消息 xml格式
     * 可用 MsgBean.buildChatBean(Stanza message) 解析
     * @param message
     */
    void onChatReceiver(Stanza message);

}
