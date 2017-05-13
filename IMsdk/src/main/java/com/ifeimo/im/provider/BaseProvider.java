package com.ifeimo.im.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ifeimo.im.framwork.database.Fields;

/**
 * Created by lpds on 2017/1/11.
 */
public abstract class BaseProvider extends ContentProvider{
    public static final String TAG = "XMPP_provide";


    protected void log(String msg) {
        Log.i(TAG, msg);
    }


    @Override
    public boolean onCreate() {
        return true;
    }


}
