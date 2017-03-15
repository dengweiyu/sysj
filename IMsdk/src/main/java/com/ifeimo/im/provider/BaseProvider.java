package com.ifeimo.im.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ifeimo.im.common.ILog;
import com.ifeimo.im.framwork.database.Fields;

import java.security.Provider;

/**
 * Created by lpds on 2017/1/11.
 */
public abstract class BaseProvider extends ContentProvider{


//    public static final String DB_ID = "_id";
//    public static final String DB_TB_MEMBER = "tb_member";
//    public static final String DB_MEMBER_ID = "memberId";
//    public static final String DB_MEMBER_NICKNAME = "member_nick_name";
//    public static final String DB_MEMBER_AVATARURL= "avatarUrl";
//    public static final String DB_REMARK = "remark";
//    public static final String DB_MSG_ID = "msgId";
    public static final String TGA = "XMPP_provide";

//    public static final String DB_TB_USER = "tb_accoun";


    protected void log(String msg) {
        Log.i(TGA, msg);
    }


    protected void checkUser(SQLiteDatabase sqLiteDatabase,String memeberid,ContentValues contentValues){
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
