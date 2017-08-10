package com.ifeimo.im.common.bean.model;

/**
 * Created by linhui on 2017/8/9.
 */
public interface Msg {
    String getContent();
    String getMsgId();
    int getSendType();
    void setSendType(int i);
}
