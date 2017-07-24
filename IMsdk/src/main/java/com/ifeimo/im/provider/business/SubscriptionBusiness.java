package com.ifeimo.im.provider.business;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ifeimo.im.common.bean.model.Account2SubscriptionModel;
import com.ifeimo.im.common.bean.model.SubscriptionModel;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;

import y.com.sqlitesdk.framework.business.Business;

/**
 * Created by lpds on 2017/4/24.
 */
abstract class SubscriptionBusiness extends InformationBusiness {

    private final String TAG = "XMPP_Subscription";


    /**
     * 添加订阅内容
     * @param sqLiteDatabase
     * @param subscription_id
     * @param subscription_name
     * @param subscription_pic_url
     * @param subscription_type
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws InstantiationException
     */
    public long insertSubscriptionDetails(SQLiteDatabase sqLiteDatabase,String subscription_id,
                                   String subscription_name,
                                   String subscription_pic_url,
                                   int subscription_type) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        SubscriptionModel subscriptionModel = new SubscriptionModel();
        subscriptionModel.setSubscription_type(subscription_type);
        subscriptionModel.setSubscription_pic_url(subscription_pic_url);
        subscriptionModel.setSubscription_name(subscription_name);
        subscriptionModel.setSubscription_id(subscription_id);
        return insertSubscriptionDetails(sqLiteDatabase,subscriptionModel);

    }
    public long insertSubscriptionDetails(SQLiteDatabase sqLiteDatabase, SubscriptionModel subscriptionModel) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
            if(Business.getInstances().insert(sqLiteDatabase,subscriptionModel) > 0){
                Log.i(TAG, "insertSubscription: ********** 订阅信息成功 Success SubscriptionModel Insert ************ "+subscriptionModel);
            }
        return subscriptionModel.getId();
    }


    /**
     * 开始订阅
     * @param sqLiteDatabase
     * @param subscriptionLineId
     * @param flag 是否确认
     */
    public Account2SubscriptionModel insertAccount2Subscription(
            SQLiteDatabase sqLiteDatabase,
            long subscriptionLineId,
            boolean flag) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        Account2SubscriptionModel account2SubscriptionModel = new Account2SubscriptionModel();
        account2SubscriptionModel.setCreate_time(System.currentTimeMillis()+"");
        account2SubscriptionModel.setMemberId(Proxy.getAccountManger().getUserMemberId());
        account2SubscriptionModel.setSubscription_id(subscriptionLineId);
        insertAccount2Subscription(sqLiteDatabase,account2SubscriptionModel,flag);
        return account2SubscriptionModel;
    }

    protected void insertAccount2Subscription(SQLiteDatabase sqLiteDatabase, Account2SubscriptionModel account2SubscriptionModel, boolean flag) throws IllegalAccessException, NoSuchFieldException, InstantiationException {

        Account2SubscriptionModel a =  Business.getInstances().queryLineByWhere(
                sqLiteDatabase,
                Account2SubscriptionModel.class,
                String.format("%s = ? AND %s = ?",
                        Fields.Account2SubscriptionFields.MEMBER_ID,
                        Fields.Account2SubscriptionFields.SUBSCRIPTION_ID),
                new String[]{account2SubscriptionModel.getMemberId(),account2SubscriptionModel.getSubscription_id()+""});
        if(a == null){
            if (Business.getInstances().insert(sqLiteDatabase, account2SubscriptionModel) > 0) {
                Log.i(TAG, "insertAccount2Subscription:********** 人员关联订阅消息成功 Success Account2Subscription Insert 订阅成功 ************ " + account2SubscriptionModel);
            }
        }else{
            account2SubscriptionModel.setId(a.getId());
        }


    }


    public void cancelAccount2Subscription(SQLiteDatabase sqLiteDatabase,long Id) throws IllegalAccessException, NoSuchFieldException, InstantiationException {

        Account2SubscriptionModel account2SubscriptionModel = new Account2SubscriptionModel();
        account2SubscriptionModel.setMemberId(Proxy.getAccountManger().getUserMemberId());
        account2SubscriptionModel.setSubscription_id(Id);
        cancelAccount2Subscription(sqLiteDatabase,account2SubscriptionModel);
    }

    public void cancelAccount2Subscription(SQLiteDatabase sqLiteDatabase,Account2SubscriptionModel account2SubscriptionModel) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        Account2SubscriptionModel queryAccount2SubscriptionModel = Business.getInstances().queryLineByWhere(sqLiteDatabase,Account2SubscriptionModel.class,
                String.format("%s = ? AND %s = ?",
                        Fields.Account2SubscriptionFields.MEMBER_ID,
                        Fields.Account2SubscriptionFields.SUBSCRIPTION_ID),
                new String[]{
                        account2SubscriptionModel.getMemberId(),
                        account2SubscriptionModel.getSubscription_id()+""});
        if(queryAccount2SubscriptionModel!=null){
            if(Business.getInstances().deleteById(sqLiteDatabase,queryAccount2SubscriptionModel) > 0){
                Log.i(TAG, "cancelAccount2Subscription: 移除订阅成功 SubscriptionModel.id = "+account2SubscriptionModel.getSubscription_id());
            }
        }
    }

}
