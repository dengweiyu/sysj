package com.ifeimo.im.provider.business;

import android.database.sqlite.SQLiteDatabase;

import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.common.bean.model.Model;

import java.util.List;

/**
 * Created by lpds on 2017/4/27.
 */
public interface IMsgBusiness<T> {
    void modifyErrorSituation();
    void deleteInformation(InformationModel informationModel);
    void insert(Model<T> model);
    int getMaxUnRead();
}
