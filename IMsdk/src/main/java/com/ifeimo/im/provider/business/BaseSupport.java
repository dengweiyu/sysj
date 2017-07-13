package com.ifeimo.im.provider.business;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ifeimo.im.common.bean.model.AccountModel;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.database.Fields;

import y.com.sqlitesdk.framework.business.Business;

/**
 * Created by lpds on 2017/4/24.
 */
abstract class BaseSupport{

    private final String TAG = "XMPP_BaseSupport";


    protected void insertAccount(SQLiteDatabase sqLiteDatabase, String memberId, String nickNmae, String avatarUrl) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        AccountModel accountModel = new AccountModel();
        accountModel.setAvatarUrl(avatarUrl);
        accountModel.setMemberId(memberId);
        accountModel.setNickName(nickNmae);
        insertAccount(sqLiteDatabase,accountModel);
    }

    protected void insertAccount(SQLiteDatabase sqLiteDatabase,final AccountModel accountModel) throws IllegalAccessException, NoSuchFieldException, InstantiationException {

                final AccountModel model = Business.getInstances().queryLineByWhere(sqLiteDatabase,AccountModel.class,
                        String.format("%s = ?", Fields.AccounFields.MEMBER_ID),
                        new String[]{accountModel.getMemberId()});
                if(model != null){
                    if((!StringUtil.isNull(accountModel.getAvatarUrl()) && !accountModel.getAvatarUrl().equals(model.getAvatarUrl()))
                            || (!StringUtil.isNull(accountModel.getNickName()) && !accountModel.getNickName().equals(model.getNickName()))) {
                        accountModel.setId(model.getId());
                        if(Business.getInstances().modify(sqLiteDatabase,accountModel)>0){
                            Log.i(TAG, "onExecute: ********** Success Account Modi ************ "+accountModel);
                        }
                    }
                }else{
                    if(Business.getInstances().insert(sqLiteDatabase,accountModel)>0){
                        Log.i(TAG, "onExecute: ********** Success Account Insert ************ "+accountModel);
                    }
                }
    }

}
