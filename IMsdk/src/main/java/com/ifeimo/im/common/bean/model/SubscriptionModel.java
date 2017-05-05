package com.ifeimo.im.common.bean.model;


import com.ifeimo.im.framwork.database.Fields;

import y.com.sqlitesdk.framework.annotation.TBColumn;
import y.com.sqlitesdk.framework.annotation.TBForeign;
import y.com.sqlitesdk.framework.annotation.TBPrimarykey;

/**
 * Created by lpds on 2017/4/20.
 *
 * 订阅
 */
public class SubscriptionModel extends  Model<SubscriptionModel>{

    public static final String TB_NAME = Fields.SubscriptionFields.TB_NAME;

    @TBPrimarykey
    private long id;

    @TBColumn(unique = true)
    private String subscription_id;

    @TBColumn
    private String subscription_name;

    @TBColumn
    private String subscription_pic_url;

    @TBColumn
    private int subscription_type;


    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }

    public String getSubscription_name() {
        return subscription_name;
    }

    public void setSubscription_name(String subscription_name) {
        this.subscription_name = subscription_name;
    }

    public String getSubscription_pic_url() {
        return subscription_pic_url;
    }

    public void setSubscription_pic_url(String subscription_pic_url) {
        this.subscription_pic_url = subscription_pic_url;
    }

    public int getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(int subscription_type) {
        this.subscription_type = subscription_type;
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
