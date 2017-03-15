package com.ifeimo.im.common.bean.expose;

import com.ifeimo.im.common.bean.chat.ChatBean;

/**
 * Created by lpds on 2017/3/3.
 */
public class ChatEntity extends MsgEntity {

    //接收者
    protected String receiverId;

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }



}


