package com.ifeimo.im.common.bean.chat;

import java.io.Serializable;

/**
 * Created by lpds on 2017/1/11.
 */
public abstract class BaseChatBean implements Serializable,Cloneable{
    protected String usert;

    public BaseChatBean(String usert) {
        this.usert = usert;
    }

    public String getUsert() {
        return usert;
    }

    public void setUsert(String usert) {
        this.usert = usert;
    }

}
