package com.ifeimo.im.common.bean.model;

import java.io.Serializable;

/**
 * Created by lpds on 2017/4/21.
 */
public interface IMsg extends IAccountModel{


    String getContent();
    String getMsgId();
    int getSendType();
    void setSendType(int i);
}
