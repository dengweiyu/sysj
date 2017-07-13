package com.ifeimo.im.common.bean.response;

import java.io.Serializable;

/**
 * Created by lpds on 2017/6/21.
 */
public class MemberInfoRespones implements Serializable{

    /**
     * code : 200
     * msg : 获取成功
     * nickname : lovergg
     * avatarUrl : http://apps.ifeimo.com/Public/Uploads/Member/Avatar/59489069ac4b8.jpg
     */

    private int code;
    private String msg;
    private String nickname;
    private String avatarUrl;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
