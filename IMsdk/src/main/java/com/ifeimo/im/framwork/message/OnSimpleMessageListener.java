package com.ifeimo.im.framwork.message;

/**
 * Created by lpds on 2017/3/3.
 */
public interface OnSimpleMessageListener {

    /**
     * 单聊
     * @param memberid 发送者
     */
    void chat(String memberid);

    /**
     * 群聊
     * @param memberid 发送者
     * @param room 房间
     */
    void groupChat(String memberid,String room);

}
