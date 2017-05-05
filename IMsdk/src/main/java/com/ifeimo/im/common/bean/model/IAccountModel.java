package com.ifeimo.im.common.bean.model;

import java.io.Serializable;

/**
 * Created by lpds on 2017/4/25.
 */
public interface IAccountModel extends Serializable {
    long getId();

    String getMemberAvatarUrl();
    void setMemberAvatarUrl(String picurl);
    String getMemberNickName();
    void setMemberNickName(String nickname);
    String getMemberId();
}
