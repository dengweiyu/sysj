package com.ifeimo.im.framwork.database.access;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.database.IMDataBaseHelper;
import com.ifeimo.im.framwork.database.business.Business;

import java.net.URI;
import java.util.concurrent.Semaphore;

/**
 * Created by lpds on 2017/2/14.
 */
public class Access {

    private static final String TAG = "XMPP_DB_ACCESS";

    private static Access server;
    private IMDataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private final Semaphore semaphore = new Semaphore(1);
    static{
        server = new Access();
    }

    private Access(){
        dataBaseHelper = new IMDataBaseHelper(IMSdk.CONTEXT);
    }

    public static Access getInstances(){
        return server;
    }


    @Deprecated
    public SQLiteDatabase open(){
        if(sqLiteDatabase == null){
            sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        }
        sqLiteDatabase.beginTransaction();
        return sqLiteDatabase;
    }

    private void setTransactionSuccessful(){
        sqLiteDatabase.setTransactionSuccessful();
    }

    @Deprecated
    public void close(){
        sqLiteDatabase.endTransaction();
    }

    public interface OnExecSQL<T> {
        T onExecSQL(SQLiteDatabase sqLiteDatabase);
    }

    public static abstract class SimpleOnExecSQL<T> implements  OnExecSQL<T>{
        private Uri uri;

        public Uri getUri() {
            return uri;
        }

        public SimpleOnExecSQL setUri(Uri uri) {
            this.uri = uri;
            return this;
        }


    }


    /**
     * 必须在子线程操作！
     * @param onExecSQL 提供操作的sqlitedatabase
     */
    public synchronized Object open(OnExecSQL onExecSQL){
        try {
            if(ThreadUtil.isMainThread()){
                Log.e(TAG," ----- com.ifeimo.im.framwork.database.Server ：不能再主线程操作数据库 ------");
//                if(onExecSQL != null) {
//                    final OnExecSQL finalOnExecSQL = onExecSQL;
//                    ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
//                        @Override
//                        public void run() {
//                            open(finalOnExecSQL);
//                        }
//                    });
//                }
                return null;
            }
            if(onExecSQL == null){
                Log.e(TAG," ----- 数据库无操作  ------");
                return null;
            }
            if (sqLiteDatabase == null) {
                sqLiteDatabase = dataBaseHelper.getWritableDatabase();
            }
            sqLiteDatabase.beginTransaction();
            semaphore.acquire();
            final Object o = onExecSQL.onExecSQL(sqLiteDatabase);
            setTransactionSuccessful();
            if(onExecSQL instanceof SimpleOnExecSQL){
                Uri uri = ((SimpleOnExecSQL) onExecSQL).getUri();
                if(uri != null) {
                    IMSdk.CONTEXT.getContentResolver().notifyChange(uri,null);
                }
            }
            return o;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                close();
            }catch (Exception e){
                e.printStackTrace();
            }
            semaphore.release();
        }

        return null;

    }
}
