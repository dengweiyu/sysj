package com.ifeimo.im.common.bean.eventbus;

import com.ifeimo.im.framwork.commander.IMWindow;

/**
 * Created by lpds on 2017/5/12.
 */
public class ChatWindowEntity implements BaseChatWindowEntity {

    private String receiverID;
    private int id;

    public ChatWindowEntity(String receiverID,int id) {
        this.receiverID = receiverID;
        this.id = id;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getType() {
        return IMWindow.CHAT_TYPE;
    }

    @Override
    public String getkey() {
        return receiverID;
    }

    @Override
    public String toString() {
        return "ChatWindowEntity{" +
                "receiverID='" + receiverID + '\'' +
                ", id=" + id +
                '}';
    }
}
