package com.li.videoapplication.data.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by y on 2018/4/9.
 */

public class OrderDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 3;
    private String tag = OrderDBHelper.class.getSimpleName();
    private static final String DB_NAME = "t_userPlayTime.db";
    private static final String TABLE_NAME = "t_userPlayTime";

    public OrderDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table playTime(id integer primary key,hash text,createtime text,playtime text,flower_tick text,comment_tick text,collection_tick text ,fndown_tick text,data blob NOT NULL);
        String sql = "create table if not exists " + TABLE_NAME + "(id integer primary key autoincrement,hash text,createtime text,playtime text,flower_tick text,comment_tick text,collection_tick text ,fndown_tick text,data text)";
        db.execSQL(sql);
        Log.d(tag, "create table" + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
        Log.d(tag, "DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static int getDbVersion() {
        return DB_VERSION;
    }

}
