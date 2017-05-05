package com.ifeimo.im.framwork.database;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ifeimo.im.BuildConfig;
import com.ifeimo.im.common.bean.OpenHelpBean;
import com.ifeimo.im.common.bean.model.Account2SubscriptionModel;
import com.ifeimo.im.common.bean.model.AccountModel;
import com.ifeimo.im.common.bean.model.ChatMsgModel;
import com.ifeimo.im.common.bean.model.GroupChatModel;
import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.common.bean.model.SubscriptionModel;

import y.com.sqlitesdk.framework.business.Business;
import y.com.sqlitesdk.framework.business.CenterServer;

/**
 * Created by lpds on 2017/1/11.
 */
public class IMDataBaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "XMPP_DataBaseHelper";
    private Context context;
    private static final int VERCODE = 20170427;

    public IMDataBaseHelper(Context context) {
        super(context, "IM_chat.db", null, VERCODE);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Business.getInstances().createTable(sqLiteDatabase, AccountModel.class);
            Business.getInstances().createTable(sqLiteDatabase, ChatMsgModel.class);
            Business.getInstances().createTable(sqLiteDatabase, SubscriptionModel.class);
            Business.getInstances().createTable(sqLiteDatabase, GroupChatModel.class);
            Business.getInstances().createTable(sqLiteDatabase, InformationModel.class);
            Business.getInstances().createTable(sqLiteDatabase, Account2SubscriptionModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: oldVersion = " + oldVersion + " newVersion = " + newVersion);
        if (oldVersion < VERCODE) {
            try {
                sqLiteDatabase.execSQL("DROP TABLE tb_accoun");
                sqLiteDatabase.execSQL("DROP TABLE tb_chat");
                sqLiteDatabase.execSQL("DROP TABLE tb_information");
                sqLiteDatabase.execSQL("DROP TABLE tb_mucc");
                sqLiteDatabase.execSQL("DROP TABLE tb_subscription");
                Business.getInstances().createTable(sqLiteDatabase, AccountModel.class);
                Business.getInstances().createTable(sqLiteDatabase, ChatMsgModel.class);
                Business.getInstances().createTable(sqLiteDatabase, SubscriptionModel.class);
                Business.getInstances().createTable(sqLiteDatabase, GroupChatModel.class);
                Business.getInstances().createTable(sqLiteDatabase, InformationModel.class);
                Business.getInstances().createTable(sqLiteDatabase, Account2SubscriptionModel.class);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("onUpgrade: oldVersion = " + oldVersion + " newVersion = ");
            }
        }
    }


}
