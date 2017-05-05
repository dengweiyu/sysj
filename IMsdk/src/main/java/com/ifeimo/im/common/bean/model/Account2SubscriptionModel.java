package com.ifeimo.im.common.bean.model;

import com.ifeimo.im.framwork.database.Fields;

import y.com.sqlitesdk.framework.Mode;
import y.com.sqlitesdk.framework.annotation.TBColumn;
import y.com.sqlitesdk.framework.annotation.TBForeign;
import y.com.sqlitesdk.framework.annotation.TBPrimarykey;

/**
 * Created by lpds on 2017/4/24.
 */
public class Account2SubscriptionModel extends Model<Account2SubscriptionModel>{

    public static final String TA_NAME = Fields.Account2SubscriptionFields.TB_NAME;

    @TBPrimarykey
    private long id;
    @TBColumn
    @TBForeign(referencesTableName = Fields.AccounFields.TB_NAME,referencesTableField = Fields.AccounFields.MEMBER_ID)
    private String memberId;
    @TBColumn
    @TBForeign(referencesTableName = SubscriptionModel.TB_NAME,referencesTableField = Fields.SubscriptionFields.ID)
    private long subscription_id;
    @TBColumn
    private String create_time;

    public long getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(long subscription_id) {
        this.subscription_id = subscription_id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String getTableName() {
        return TA_NAME;
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
        return create_time;
    }
}
