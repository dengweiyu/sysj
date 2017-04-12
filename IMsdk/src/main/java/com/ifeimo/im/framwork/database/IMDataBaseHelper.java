package com.ifeimo.im.framwork.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ifeimo.im.BuildConfig;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.provider.BaseProvider;
import com.ifeimo.im.provider.InformationProvide;
import com.ifeimo.im.provider.ChatProvider;
import com.ifeimo.im.provider.MuccProvider;

import java.io.File;
import java.util.concurrent.Semaphore;

/**
 * Created by lpds on 2017/1/11.
 */
public class IMDataBaseHelper extends SQLiteOpenHelper {
    public static final String TAG ="XMPP_DataBaseHelper";
    public IMDataBaseHelper(Context context) {
        super(context, "IM_chat.db", null, BuildConfig.VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql1());
        db.execSQL(sql2());
        db.execSQL(sql3());
        db.execSQL(sql4());
        db.execSQL(sql5());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion) {

            case 1:{
                Log.i(TAG," oldVersion = 1.0 , clear all data");
                db.execSQL(String.format("DELETE * FROM %s", Fields.ChatFields.TB_NAME));
                db.execSQL(String.format("DELETE * FROM %s", Fields.GroupChatFields.TB_NAME));
                db.execSQL(String.format("DELETE * FROM %s", Fields.InformationFields.TB_NAME));
                db.execSQL(String.format("DELETE * FROM %s", Fields.SubscriptionFields.TB_NAME));
                db.execSQL(String.format("DELETE * FROM %s", Fields.AccounFields.TB_NAME));

            }
            break;
            case 2:
                break;

        }
    }


    /**
     * group chat table
     *
     * @return
     */
    private String sql1() {
        return String.format("CREATE TABLE %s (%s integer primary key autoincrement," +
                        "%s integer,%s varchat(20),%s text,%s text,%s varchat(20),%s text)",
                Fields.GroupChatFields.TB_NAME, Fields.GroupChatFields.ID, Fields.GroupChatFields.SEND_TYPE,
                Fields.GroupChatFields.MSG_ID, Fields.GroupChatFields.MEMBER_ID, Fields.GroupChatFields.CREATE_TIME,
                Fields.GroupChatFields.ROOM_ID, Fields.GroupChatFields.CONTENT);
    }

    /**
     * chat table
     *
     * @return
     */
    private String sql2() {
        return String.format("CREATE TABLE %s (%s integer primary key autoincrement," +
                        "%s integer,%s varchat(10),%s varchat(20),%s text,%s text,%s text)",
                Fields.ChatFields.TB_NAME, Fields.ChatFields.ID, Fields.ChatFields.SEND_TYPE, Fields.ChatFields.MEMBER_ID,
                Fields.ChatFields.RECEIVER_ID, Fields.ChatFields.CREATE_TIME, Fields.ChatFields.CONTENT, Fields.ChatFields.MSG_ID);

    }

    /**
     * information table
     *
     * @return
     */
    private String sql3() {

        return String.format("CREATE TABLE %s (%s integer primary key autoincrement," +
                        "%s varchat(20), %s varchat(20), " +
                        "%s varchar(10)," +
                        "%s varchat(20), %s text," +
                        "%s integer, " +
                        "%s integer,%s varchat(20),%s integer,%s integer)",
                Fields.InformationFields.TB_NAME, Fields.InformationFields.ID, Fields.InformationFields.MEMBER_ID,
                Fields.InformationFields.OPPOSITE_ID,
                Fields.InformationFields.MSG_ID,
                Fields.InformationFields.LAST_CONTENT, Fields.InformationFields.LAST_CREATETIME,
                Fields.InformationFields.SEND_TYPE,
                Fields.InformationFields.TYPE, Fields.InformationFields.NAME,
                Fields.InformationFields.UNREAD_COUNT, Fields.InformationFields.IS_ME_SEND);
    }

    /**
     * accoun table
     *
     * @return
     */
    private String sql4() {
        return String.format("CREATE TABLE %s " +
                        "(%s integer primary key autoincrement,%s varchat(20),%s varchat(20),%s text)"
                , Fields.AccounFields.TB_NAME, Fields.AccounFields.ID, Fields.AccounFields.MEMBER_ID,
                Fields.AccounFields.MEMBER_NICKNAME, Fields.AccounFields.MEMBER_AVATARURL);

    }

    /**
     * subscription table
     */
    private String sql5() {

        return String.format("CREATE TABLE %s " +
                        "(%s integer primary key autoincrement,%s varchat(20)," +
                        "%s varchat(20),%s text," +
                        "%s varchat(20),%s integer)", Fields.SubscriptionFields.TB_NAME,
                Fields.SubscriptionFields.ID, Fields.SubscriptionFields.MEMBER_ID,
                Fields.SubscriptionFields.SUBSCRIPTION_ID, Fields.SubscriptionFields.PICURL,
                Fields.SubscriptionFields.NAME, Fields.SubscriptionFields.TYPE);

    }


}
