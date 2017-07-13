package com.li.videoapplication.data.model.event;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 清除未读消息
 */

public class ReadMessageEntity extends BaseEntity {
    private String msgId;
    private String symbol;
    private String msgType;
    private int all;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}
