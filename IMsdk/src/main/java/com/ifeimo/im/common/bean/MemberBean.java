package com.ifeimo.im.common.bean;

import java.io.Serializable;

/**
 * Created by lpds on 2017/2/27.
 */
 abstract class MemberBean implements Serializable {

    int id;
    String memeberid;
    String nickName;
    String avatarUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemeberid() {
        return memeberid;
    }

    public void setMemeberid(String memeberid) {
        this.memeberid = memeberid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
