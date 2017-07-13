package com.ifeimo.im.provider.business;

import android.database.sqlite.SQLiteDatabase;

import com.ifeimo.im.common.bean.model.InformationModel;

import java.util.List;

/**
 * Created by lpds on 2017/6/21.
 */
public interface IInfortmationBusiness {
    List<InformationModel> getAllSubscriptionByType(SQLiteDatabase sqLiteDatabase, int type) throws IllegalAccessException, NoSuchFieldException, InstantiationException;
}
