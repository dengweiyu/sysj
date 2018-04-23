package com.li.videoapplication.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.li.videoapplication.data.cache.OrderDBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by y on 2018/4/9.
 */

public class SqliteDatabaseDao {
    private static final String TAG = SqliteDatabaseDao.class.getSimpleName();
    private Context context;
    private OrderDBHelper orderDBHelper;
    private String hash;
    private final String[] t_userPlayTime = new String[]{"id", "hash", "createtime", "playtime", "flower_tick", "comment_tick", "collection_tick", "fndown_tick", "data"};
    private final String[] t_userPlayTime2 = new String[]{"createtime", "playtime", "flower_tick", "collection_tick", "fndown_tick", "data"};

    public SqliteDatabaseDao(Context context) {
        this.context = context;
        orderDBHelper = new OrderDBHelper(context);
    }


    public boolean ifHaveHash() {
        if (null != getHash()) {
            return true;
        } else {
            return false;
        }
    }


    public String getHash() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = orderDBHelper.getReadableDatabase();
            cursor = db.query(orderDBHelper.getTableName(), null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.move(i);
                    hash = cursor.getString(1);
                }

            } else {

            }

        } catch (Exception e) {
            Log.d(TAG, "" + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return hash;
    }

    public boolean isDataExist() {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = orderDBHelper.getReadableDatabase();
            cursor = db.query(orderDBHelper.getTableName(), new String[]{"COUNT(id)"}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    public boolean isTableExists() {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = orderDBHelper.getReadableDatabase();
//            cursor = db.query(orderDBHelper.getTableName(), new String[]{"COUNT(id)"}, null, null, null, null, null);
            cursor = db.rawQuery("SELECT * FROM " + orderDBHelper.getTableName(), null);
            if (cursor.moveToFirst()) {
                count = cursor.getCount();
                if (count > 0) {
                    return true;
                }
            }
//            if (cursor.moveToFirst()) {
//                count = cursor.getInt(0);
//            }
//            if (count > 0) {
//                return true;
//            }

        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }


    public boolean booeanSameHash(String urlTimeStamp) {

        boolean stamp = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = orderDBHelper.getReadableDatabase();

            cursor = db.query(orderDBHelper.getTableName(), t_userPlayTime, "hash=?", new String[]{urlTimeStamp}, null, null, null);
            while (cursor.moveToFirst()) {

                if (cursor != null && cursor.getString(cursor.getColumnIndex("hash")).length() > 0) {
                    stamp = true;
                    break;
                } else {
                    stamp = false;
                    break;
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e + "");

        } finally {
            db.close();
            cursor.close();

        }
        return stamp;
    }


    public void updata(String hash, String playtime) {
        SQLiteDatabase db = null;
        try {
            db = orderDBHelper.getWritableDatabase();
            db.beginTransaction();
            String sql = "UPDATE " + OrderDBHelper.getTableName() + " SET playtime = " + "'" + playtime + "'" + " WHERE hash = " + "'" + hash + "'";
            Log.d(TAG, "UPDATE " + OrderDBHelper.getTableName() + " SET playtime = " + "'" + playtime + "'" + " WHERE hash = " + "'" + hash + "'");
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

    }

    public void initTable(int id, String hash, String createtime, String playtime, String flower_tick, String comment_tick, String collection_tick, String fndown_tick, String data) {
        SQLiteDatabase db = null;
        try {
            db = orderDBHelper.getWritableDatabase();
            db.beginTransaction();
            Log.d(TAG, "insert into " + OrderDBHelper.getTableName() + " (id, hash, createtime, playtime, flower_tick, comment_tick, collection_tick, fndown_tick, data) values ("
                    + "NULL" + "," + "'" + hash + "'" + "," + "'" + createtime + "'" + "," + "'" + playtime + "'" + "," + "'" + flower_tick + "'" + ","
                    + "'" + comment_tick + "'" + "," + "'" + collection_tick + "'" + "," + "'" + fndown_tick + "'" + "," + "'" + data + "'" + ")"
            );
            db.execSQL("insert into " + OrderDBHelper.getTableName() + " (id, hash, createtime, playtime, flower_tick, comment_tick, collection_tick, fndown_tick, data) values ("
                    + "NULL" + "," + "'" + hash + "'" + "," + "'" + createtime + "'" + "," + "'" + playtime + "'" + "," + "'" + flower_tick + "'" + ","
                    + "'" + comment_tick + "'" + "," + "'" + collection_tick + "'" + "," + "'" + fndown_tick + "'" + "," + "'" + data + "'" + ")"
            );

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "" + e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

    }

    //"createtime", "playtime", "flower_tick", "collection_tick", "fndown_tick", "data"
    public String query() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String createtime = null;
        String data = null;
        String fndown_tick = null;
        String playtime = null;
        String flower_tick = null;
        String collection_tick = null;
        JSONObject js = null;
        JSONArray ja = new JSONArray();
        try {
            db = orderDBHelper.getReadableDatabase();
            cursor = db.query(orderDBHelper.getTableName(), t_userPlayTime2, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {

//                for (int i = 0; i < cursor.getCount(); i++) {

                    do {
//                        cursor.move(i);
                        createtime = cursor.getString(0);
                        playtime = cursor.getString(1);
                        collection_tick = cursor.getString(3);
                        fndown_tick = cursor.getString(4);
                        data = cursor.getString(5);
                        try {
                            js = new JSONObject(data);
                            js.put("time02", createtime);
                            js.put("time01", praseSecond(playtime));
                            js.put("num07", collection_tick);
                            js.put("num08", fndown_tick);
                            ja.put(js);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());
                    }


//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null) {
                cursor.close();
            }
            Log.d(TAG, "" + ja.toString());
        }
        return ja.toString();
    }

    private String praseSecond(String createtime) {
        int i = Integer.parseInt(createtime);
        int second = i / 1000;
        String second2 = String.valueOf(second);
        return second2;
    }

    public void cleanTable() {
        SQLiteDatabase db = null;
        try {
            db = orderDBHelper.getWritableDatabase();
            String sql = "DELETE FROM " + OrderDBHelper.getTableName();
            Log.d(TAG, "DELETE FROM " + OrderDBHelper.getTableName());
            db.beginTransaction();
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

    }

    public boolean tableCount() {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = orderDBHelper.getReadableDatabase();
//            cursor = db.query(orderDBHelper.getTableName(), new String[]{"COUNT(id)"}, null, null, null, null, null);
            cursor = db.rawQuery("SELECT * FROM " + orderDBHelper.getTableName(), null);
            if (cursor.moveToFirst()) {
                count = cursor.getCount();
                if (count >= 5) {
                    return true;
                }
            }

//            if (cursor.moveToFirst()) {
//                count = cursor.getInt(0);
//            }
//            if (count > 0) {
//                return true;
//            }

        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;

    }

}
