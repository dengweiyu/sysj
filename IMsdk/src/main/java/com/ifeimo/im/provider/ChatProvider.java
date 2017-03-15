package com.ifeimo.im.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.ifeimo.im.common.MD5;
import com.ifeimo.im.common.adapter.BaseChatReCursorAdapter;
import com.ifeimo.im.common.bean.InformationBean;
import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.database.IMDataBaseHelper;
import com.ifeimo.im.framwork.message.OnSimpleMessageListener;
import com.ifeimo.im.service.MsgService;

/**
 * Created by lpds on 2017/1/11.
 */
public class ChatProvider extends BaseProvider {

    public static final String PROVIDER_NAME = "com.ifeimo.im.db.chat";
    private IMDataBaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase = null;
    private static UriMatcher matcher;
    private static final int CHAT = 1;
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/chat");
    //public static final Uri CONTEXT_BY_ID = Uri.parse("content://" + PROVIDER_NAME + "/deletebyid");

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(PROVIDER_NAME, "chat", CHAT);

    }

    @Override
    public boolean onCreate() {
        dbHelper = new IMDataBaseHelper(this.getContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return sqLiteDatabase == null ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (matcher.match(uri)) {
            case CHAT:
//                SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
//                qBuilder.setTables(DB_TB_NAME);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String sql =String.format(
                        "SELECT * " +
                        "FROM " +
                                "(SELECT * \n" +
                                "FROM tb_chat  \n" +
                                "WHERE (receiverId = ? AND memberId = ? AND send_type = 2001) OR (receiverId = ? AND memberId = ?) \n" +
                                "ORDER BY create_time " +
                                "DESC " +
                                "LIMIT %s) " +
                        "ORDER BY create_time ", Integer.parseInt(StringUtil.isNull(selection)?"5":selection) * BaseChatReCursorAdapter.MAX_PAGE_COUNT);
                Cursor ret = db.rawQuery(sql, new String[]{
                        selectionArgs[0], selectionArgs[1],
                        selectionArgs[1], selectionArgs[0],
                }, null);
                ret.setNotificationUri(getContext().getContentResolver(), uri);
                return ret;
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case CHAT:
                return "vnd.android.cursor.dir/com.ifeimo.im";
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowID = -1;
        switch (matcher.match(uri)) {
            case CHAT:
                sqLiteDatabase.beginTransaction();
                try {

                    if (StringUtil.isNull(contentValues.getAsString(Fields.ChatFields.MEMBER_ID))) {
                        rowID = -1;
                    } else {
                        rowID = insertCheck(contentValues);

                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                if (rowID > 0) {
                    Uri wordUri = ContentUris.withAppendedId(uri, rowID);
                    log(" ------- 数据插入成功 ------ " + contentValues);
                    contentValues.put(Fields.ChatFields.ID, rowID);
                    tellToCache(contentValues);
//                    tellToMessageListener();
                    return wordUri;
                } else if (rowID == -10) {
                    log(" -------  重复数据不插入 -------" + contentValues);
                    tellToCache(contentValues);
                } else {
                    log(" -------  数据插入失败 -------" + contentValues);
                }
                break;
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
        return null;
    }

    /**
     * 插入 chat
     * @param contentValues
     * @return
     */
    private long insertCheck(ContentValues contentValues) {
        String msgid = contentValues.getAsString(Fields.ChatFields.MSG_ID);
        String memberid = contentValues.getAsString(Fields.ChatFields.MEMBER_ID);
        String receiverid = contentValues.getAsString(Fields.ChatFields.RECEIVER_ID);


        if (!StringUtil.isNull(msgid)) {
            //查询此用户 在 AccounFields 表
            final Cursor cursor = sqLiteDatabase.query(Fields.AccounFields.TB_NAME,
                    new String[]{Fields.AccounFields.ID},Fields.AccounFields.MEMBER_ID+" = ?",new String[]{msgid},null,null,null);
            if(cursor.getCount() < 1){
                //此用户不存在，新增
                final ContentValues accountValues = new ContentValues();
                accountValues.put(Fields.AccounFields.MEMBER_ID,msgid);
                sqLiteDatabase.insert(Fields.AccounFields.TB_NAME,null,accountValues);
            }
            cursor.close();

            final String[] ara = {receiverid, memberid, memberid, receiverid, msgid};
            //查询此行 msiid 是否存在
            final Cursor c = sqLiteDatabase.query(Fields.ChatFields.TB_NAME, new String[]{Fields.ChatFields.MSG_ID},
                    " ((receiverId = ? AND memberId = ?) OR (receiverId = ? AND memberId = ?)) AND " + Fields.ChatFields.MSG_ID + " = ?",
                    ara, null, null, null);
            if (c.getCount() == 1) {
                c.close();
                if (sqLiteDatabase.update(Fields.ChatFields.TB_NAME, contentValues,
                        " ((receiverId = ? AND memberId = ?) OR (receiverId = ? AND memberId = ?)) AND " + Fields.ChatFields.MSG_ID + " = ? ", ara) > 0) {
                    Log.i(TGA, " ----- 单聊数据更新 -----");
                }
                return -10;
            }
        }
        //Android XMPP 中 msgid = null,是iphone端发送过来的消息
        return sqLiteDatabase.insert(Fields.ChatFields.TB_NAME, null, contentValues);

    }


    /**
     * 修改information
     * @param contentValues
     */
    private void tellToCache2(ContentValues contentValues) {
        ContentValues informationValues = new ContentValues();
        informationValues.put(Fields.InformationFields.SEND_TYPE,contentValues.getAsInteger(Fields.MsgFields.SEND_TYPE));
        informationValues.put(Fields.InformationFields.LAST_CONTENT,contentValues.getAsString(Fields.MsgFields.CONTENT));
        informationValues.put(Fields.InformationFields.LAST_CREATETIME,contentValues.getAsString(Fields.MsgFields.CREATE_TIME));
        informationValues.put(Fields.InformationFields.MSG_ID,contentValues.getAsString(Fields.MsgFields.MSG_ID));
        String where = String.format("%s = ? AND %s = ? AND %s = %s",
                Fields.InformationFields.MEMBER_ID,
                Fields.InformationFields.OPPOSITE_ID,
                Fields.InformationFields.TYPE,InformationBean.CHAT);
        MsgService.contentResolver.update(InformationProvide.update_URI,
                informationValues,where,
                new String[]{
                        UserBean.getMemberID(),
                        contentValues.getAsString(Fields.ChatFields.RECEIVER_ID),
                });
    }

    /**
     * 插入information
     * @param contentValues
     */
    protected void tellToCache(ContentValues contentValues) {
        ContentValues c = new ContentValues();
        String memberid = contentValues.getAsString(Fields.InformationFields.MEMBER_ID);
        //获取发送者
        String receiverid = null;
        if (UserBean.getMemberID().equals(memberid)) {
            c.put(Fields.InformationFields.IS_ME_SEND, 1);
            receiverid = contentValues.getAsString(Fields.ChatFields.RECEIVER_ID);
        } else {
            receiverid = memberid;
            c.put(Fields.InformationFields.IS_ME_SEND, 0);
        }
        checkUser(sqLiteDatabase,receiverid,contentValues);
        c.put(Fields.InformationFields.TYPE, InformationBean.CHAT);
        c.put(Fields.InformationFields.LAST_CONTENT, contentValues.getAsString(Fields.MsgFields.CONTENT));
        c.put(Fields.InformationFields.LAST_CREATETIME, contentValues.getAsString(Fields.MsgFields.CREATE_TIME));
        c.put(Fields.InformationFields.MEMBER_ID, UserBean.getMemberID());
        c.put(Fields.InformationFields.OPPOSITE_ID, receiverid);
        c.put(Fields.InformationFields.SEND_TYPE, contentValues.getAsString(Fields.MsgFields.SEND_TYPE));
        c.put(Fields.InformationFields.MSG_ID, contentValues.getAsString(Fields.MsgFields.MSG_ID));
        MsgService.contentResolver.update(InformationProvide.CONTENT_URI, c, null, null);


    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int num = 0;
        switch (matcher.match(uri)) {
            case CHAT:
                sqLiteDatabase.beginTransaction();
                try {
                    num = sqLiteDatabase.delete(Fields.ChatFields.TB_NAME, selection, selectionArgs);
                    sqLiteDatabase.setTransactionSuccessful();
                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                break;
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int num = 0;
        switch (matcher.match(uri)) {
            case CHAT:
                sqLiteDatabase.beginTransaction();
                try {
                    num = sqLiteDatabase.update(Fields.ChatFields.TB_NAME, values, selection, selectionArgs);
                    sqLiteDatabase.setTransactionSuccessful();
                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
                } finally {
                    sqLiteDatabase.endTransaction();
                    if(num >0){
                        tellToCache2(values);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }

//    private void tellToMessageListener(){
//        ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
//            @Override
//            public void run() {
//
//                final OnSimpleMessageListener onSimpleMessageListener = Proxy.getMessageManager().getOnMessageReceiver();
//                if(onSimpleMessageListener != null){
//                    onSimpleMessageListener.chat();
//                }
//            }
//        });
//    }


}
