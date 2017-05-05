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

    protected void checkUser(SQLiteDatabase sqLiteDatabase, String memeberid, ContentValues contentValues){
        try {
            Cursor cursor = sqLiteDatabase.query(Fields.AccounFields.TB_NAME, null, String.format("%s = ?", Fields.AccounFields.MEMBER_ID),
                    new String[]{memeberid}, null, null, null);
            if (cursor.getCount() == 1 && cursor.moveToFirst()) {
                contentValues.put(Fields.AccounFields.MEMBER_NICKNAME, cursor.getString(cursor.getColumnIndex(Fields.AccounFields.MEMBER_NICKNAME)));
                contentValues.put(Fields.AccounFields.MEMBER_AVATARURL, cursor.getString(cursor.getColumnIndex(Fields.AccounFields.MEMBER_AVATARURL)));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
