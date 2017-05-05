package com.ifeimo.im.common.bean.model;

import com.ifeimo.im.framwork.database.Fields;

import y.com.sqlitesdk.framework.annotation.TBColumn;
import y.com.sqlitesdk.framework.annotation.TBPrimarykey;

/**
 * Created by lpds on 2017/4/19.
 * 用户模型
 */
public class AccountModel extends Model<AccountModel> {

    public static final String TB_NAME = Fields.AccounFields.TB_NAME;

    @TBPrimarykey
    private long id;

    @TBColumn(unique = true)
    private String memberId;

    @TBColumn
    private String member_nick_name;

    @TBColumn
    private String avatarUrl;


    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setNickName(String member_nick_name) {
        this.member_nick_name = member_nick_name;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getNickName() {
        return member_nick_name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String getTableName() {
        return TB_NAME;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getCreateTime() {
        return null;
    }


}
