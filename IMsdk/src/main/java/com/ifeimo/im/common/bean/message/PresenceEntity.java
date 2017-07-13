package com.ifeimo.im.common.bean.message;

import java.util.IdentityHashMap;

/**
 * Created by lpds on 2017/7/13.
 */
public class PresenceEntity {
    private String memberid;


    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public PresenceEntity() {
    }

    public PresenceEntity(String memberid) {
        this.memberid = memberid;
    }
}
