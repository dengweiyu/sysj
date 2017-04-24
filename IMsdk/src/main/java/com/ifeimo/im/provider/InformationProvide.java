package com.ifeimo.im.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ifeimo.im.common.bean.InformationBean;
import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.database.IMDataBaseHelper;

/**
 * Created by lpds on 2017/1/24.
 */
public class InformationProvide extends BaseProvider {


//    /**
//     * 数据库结构信息
//     */
//    public static final String TB_NAME = "tb_information";//表格
//    public static final String DB_OPPOSITE_ID = "opposite_id";//对方id
//    public static final String DB_TITLE = "title";//头文字
//    public static final String DB_LAST_CONTENT = "last_content";//最后一次会话
//    public static final String DB_LAST_CREATETIME = "last_create_time";//最后一次会话时间
//    public static final String DB_PICURL = "pic_url";//来者头像
//    public static final String DB_TYPE = "type";//来者类型
//    public static final String DB_NAME = "name";//对方姓名，或者房间名字
//    public static final String DB_UNREAD_COUNT = "un_read";//未读数量
//    public static final String DB_IS_ME_SEND = "is_mesend";//是否自己发送

    private IMDataBaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    public static final String PROVIDER_NAME = "com.ifeimo.im.db.cachemsglist";
    public static final Uri QURRY_MAX_URI = Uri.parse("content://" + PROVIDER_NAME + "/querymax");
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/Cache");
    public static final Uri ClearUnreadById_URI = Uri.parse("content://" + PROVIDER_NAME + "/ClearUnreadById");
    public static final Uri update_URI = Uri.parse("content://" + PROVIDER_NAME + "/_update");
    public static final Uri SimpleUpdate_URI = Uri.parse("content://" + PROVIDER_NAME + "/simpleUpdate");
    public static final Uri UnreadCount_URI = Uri.parse("content://" + PROVIDER_NAME + "/UnreadCount");
    private static UriMatcher matcher;
    private static final int Cache = 0x111;
    private static final int ClearUnreadByID = 0x110;
    private static final int $update = 0x131;
    private static final int SimpleUpdate = 0x132;
    private static final int Querymax = 0x133;
    private static final int UnreadCount = 0x134;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(PROVIDER_NAME, "Cache", Cache);
        matcher.addURI(PROVIDER_NAME, "ClearUnreadById", ClearUnreadByID);
        matcher.addURI(PROVIDER_NAME, "_update", $update);
        matcher.addURI(PROVIDER_NAME, "simpleUpdate", SimpleUpdate);
        matcher.addURI(PROVIDER_NAME, "querymax", Querymax);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new IMDataBaseHelper(this.getContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return sqLiteDatabase == null ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        switch (matcher.match(uri)) {
            case Cache:
                try {
//                    Cursor cursor = sqLiteDatabase.query(TB_NAME, null,
//                            s, strings1, null, null, InformationProvide.DB_LAST_CREATETIME+" DESC");

//                    String sql = String.format(
//                            "SELECT tb_information.*,tb_user.avatarUrl AS pic_url,tb_user.member_nick_name AS title \n" +
//                            "FROM tb_information INNER JOIN tb_user ON tb_information.opposite_id = tb_user.memberId \n" +
//                            "WHERE tb_information.memberId = '%s' " +
//                            "ORDER BY %s DESC",UserBean.getMemberID(), Fields.InformationFields.LAST_CREATETIME);
//


                    String memberId = "SELECT * FROM (\n" +
                            "\n" +
                            "SELECT t1.*,tb_accoun.member_nick_name,tb_accoun.avatarUrl,count(*) as maxCount FROM (\n" +
                            "       SELECT memberId as opposide_id,create_time as last_create_time,content as last_content,msgId,send_type FROM tb_chat WHERE receiverId = '1722092' AND send_type = 2001 GROUP BY memberId UNION ALL\n" +
                            "       SELECT receiverId as opposide_id,create_time as last_create_time,content as last_content,msgId,send_type FROM tb_chat WHERE memberId = '1722092' GROUP BY receiverId) as t1,tb_accoun \n" +
                            "WHERE tb_accoun.memberId = t1.opposide_id\n" +
                            "GROUP BY t1.opposide_id\n" +
                            "\n" +
                            "UNION ALL\n" +
                            "\n" +
                            "SELECT tb_subscription.subscription_id as opposide_id,tb_mucc.create_time as last_create_time,tb_mucc.content as last_content,tb_mucc.msgId,tb_mucc.send_type, tb_subscription.subscription_name,tb_subscription.subscription_pic_url,count(*) as maxCount\n" +
                            "FROM tb_mucc,tb_subscription WHERE tb_mucc.roomId = tb_subscription.subscription_id AND tb_subscription.memberId = '1722092' \n" +
                            "GROUP BY tb_mucc.roomId \n" +
                            ") \n" +
                            "ORDER BY last_create_time Desc";


                    String memberid = UserBean.getMemberID();
                    String sql = String.format(
                            "SELECT * \n" +
                                    "FROM \n" +
                                    "      (SELECT tb_information.*,tb_accoun.member_nick_name as title,tb_accoun.avatarUrl as pic_url \n" +
                                    "       FROM tb_information,tb_accoun\n" +
                                    "       WHERE tb_information.memberId = '%s' AND  tb_information.opposite_id = tb_accoun.memberId) \n" +
                                    "       UNION ALL\n" +
                                    "            SELECT tb_information.*,tb_subscription.subscription_name as title,tb_subscription.subscription_pic_url as pic_url \n" +
                                    "            FROM tb_information,tb_subscription\n" +
                                    "            WHERE tb_information.memberId = '%s' AND  tb_information.opposite_id = tb_subscription.subscription_id \n" +
                                    "ORDER BY last_create_time DESC"
                    ,memberid,memberid);



                    sql = sql.replaceAll("tb_user", Fields.AccounFields.TB_NAME);
                    Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursor;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
//            case Querymax:
//            break;
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    @Deprecated
    public Uri insert(Uri uri, ContentValues contentValues) {


        switch (matcher.match(uri)){
            case Cache:
                getContext().getContentResolver().notifyChange(UnreadCount_URI, Proxy.getMessageManager().getUnReadObserver());
                break;
            default:
                new IllegalArgumentException("未知uri:" + uri);
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        switch (matcher.match(uri)) {
            case Cache:
                sqLiteDatabase.delete(Fields.InformationFields.TB_NAME, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                getContext().getContentResolver().notifyChange(UnreadCount_URI, Proxy.getMessageManager().getUnReadObserver());
                break;
        }
        return 0;
    }

    @Override
    @Deprecated
    public int update(Uri uri, ContentValues contentValues, String where, String[] args) {
        switch (matcher.match(uri)) {
            case Cache: {
                try {
                    sqLiteDatabase.beginTransaction();
                    if (contentValues == null) {
                        return -2;
                    }
                    Cursor cursor = sqLiteDatabase.query(Fields.InformationFields.TB_NAME, null,
                            String.format("%s = '%s' AND %s = %s AND %s = '%s' ",
                                    Fields.InformationFields.OPPOSITE_ID, contentValues.getAsString(Fields.InformationFields.OPPOSITE_ID),
                                    Fields.InformationFields.TYPE, contentValues.getAsInteger(Fields.InformationFields.TYPE),
                                    Fields.InformationFields.MEMBER_ID, UserBean.getMemberID()), null, null, null, null);
                    final int count = cursor.getCount();
                    int re;
                    int unreadCount = 1;
                    if (count == 1) {
                        cursor.moveToFirst();
                        String rid = contentValues.getAsString(Fields.InformationFields.OPPOSITE_ID);
                        contentValues.remove(Fields.InformationFields.OPPOSITE_ID);
                        if(!checkMemberId(rid,contentValues.getAsInteger(Fields.InformationFields.TYPE)) &&
                                contentValues.getAsInteger(Fields.InformationFields.IS_ME_SEND) != 1){
                                unreadCount = cursor.getInt(cursor.getColumnIndex(Fields.InformationFields.UNREAD_COUNT));
                                unreadCount++;
                                contentValues.put(Fields.InformationFields.UNREAD_COUNT, unreadCount);
                        }else {
                            unreadCount = 0;
                            contentValues.put(Fields.InformationFields.UNREAD_COUNT, unreadCount);
                        }
                        final int id = cursor.getInt(cursor.getColumnIndex(Fields.InformationFields.ID));

                        re = sqLiteDatabase.update(Fields.InformationFields.TB_NAME, contentValues, Fields.InformationFields.ID + " = " + id, null);
                        if (re > 0) {
                            Log.i(TGA, "------- 修改通知消息 infomation ------" + contentValues);
                        }

                    } else {
                        if (!checkMemberId(contentValues.getAsString(Fields.InformationFields.OPPOSITE_ID),contentValues.getAsInteger(Fields.InformationFields.TYPE)) &&
                                contentValues.getAsInteger(Fields.InformationFields.IS_ME_SEND) != 1) {
                            contentValues.put(Fields.InformationFields.UNREAD_COUNT, unreadCount);
                        }else{
                            unreadCount = 0;
                            contentValues.put(Fields.InformationFields.UNREAD_COUNT, unreadCount);
                        }

                        re = (int) sqLiteDatabase.insert(Fields.InformationFields.TB_NAME, null, contentValues);
                        if (re > 0) {
                            Log.i(TGA, "------- 新增通知消息 infomation ------" + contentValues);
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                    cursor.close();
                    getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                    getContext().getContentResolver().notifyChange(UnreadCount_URI, Proxy.getMessageManager().getUnReadObserver());
                    return re;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
            }
            break;
            case ClearUnreadByID:
                sqLiteDatabase.execSQL(String.format("UPDATE %s SET %s = %s WHERE %s = %s",
                        Fields.InformationFields.TB_NAME, Fields.InformationFields.UNREAD_COUNT, 0,
                        Fields.InformationFields.ID, contentValues.getAsInteger(Fields.InformationFields.ID)));
                getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                getContext().getContentResolver().notifyChange(UnreadCount_URI, Proxy.getMessageManager().getUnReadObserver());
                return contentValues.getAsInteger(Fields.InformationFields.ID);
            case $update:
                final int i = sqLiteDatabase.update(Fields.InformationFields.TB_NAME,contentValues,where,args);
                getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                getContext().getContentResolver().notifyChange(UnreadCount_URI, Proxy.getMessageManager().getUnReadObserver());
                return i;
            case SimpleUpdate:
                Cursor cursor = null;
                int result = -1;
                if((cursor = sqLiteDatabase.query(Fields.InformationFields.TB_NAME,
                        new String[]{Fields.InformationFields.SEND_TYPE},where,args,null,null,null)).getCount() == 1
                        && cursor.moveToFirst()){
                    if(cursor.getInt(cursor.getColumnIndex(Fields.InformationFields.SEND_TYPE))
                            != Fields.MsgFields.SEND_FINISH){
                        result = sqLiteDatabase.update(Fields.InformationFields.TB_NAME,contentValues,where,args);
                        cursor.close();
                    }
                }else{
                    result = sqLiteDatabase.update(Fields.InformationFields.TB_NAME,contentValues,where,args);
                }
                getContext().getContentResolver().notifyChange(CONTENT_URI, null);
                getContext().getContentResolver().notifyChange(UnreadCount_URI, Proxy.getMessageManager().getUnReadObserver());
                return result;
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
        return -1;
    }


    private boolean checkMemberId(String id,int type){
        switch (type){
            case InformationBean.CHAT:
                return Proxy.getIMWindowManager().allKeys().contains(UserBean.getMemberID()+id);
            case InformationBean.ROOM:
                return Proxy.getIMWindowManager().allKeys().contains(id);

        }
        return false;
    }

}
