package com.ifeimo.im.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

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
public class MuccProvider extends BaseProvider {

//    public static final String DB_TB_NAME = "tb_mucc";
//
//    public static final String DB_MEMBER_ID = "memberId";
//    public static final String DB_CONTENT = "content";
//    public static final String DB_SEND_TYPE = "send_type";
//    public static final String DB_CREATE_TIME = "create_time";
//    public static final String DB_ROOM_ID = "roomid";
    public static final String PROVIDER_NAME = "com.ifeimo.im.db.mucc";
    private static UriMatcher matcher;
    private static final int MUCC = 2;
    private IMDataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/mucc");

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(PROVIDER_NAME, "mucc", MUCC);
    }

    @Override
    public boolean onCreate() {
        dataBaseHelper = new IMDataBaseHelper(getContext());
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        return sqLiteDatabase == null ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (matcher.match(uri)) {
            case MUCC:

                String sql1 =
                        String.format(
                                "SELECT * " +
                                "FROM (" +
                                        "SELECT tb_mucc.* , tb_user.member_nick_name , tb_user.avatarUrl \n" +
                                        "FROM tb_mucc " +
                                        "INNER JOIN tb_user ON tb_mucc.memberId = tb_user.memberId \n" +
                                        "WHERE tb_mucc.roomid = ? AND ((tb_mucc.memberId != ? AND tb_mucc.send_type = 2001) or tb_mucc.memberId = ?)\n" +
                                        "ORDER BY tb_mucc.create_time " +
                                        "DESC " +
                                        "LIMIT %s) " +
                                "ORDER BY create_time",
                                BaseChatReCursorAdapter.MAX_PAGE_COUNT * Integer.parseInt(StringUtil.isNull(selection)?"5":selection));
                sql1 = sql1.replaceAll("tb_user", Fields.AccounFields.TB_NAME);

                String sql2 =
                        "SELECT tb_mucc.* , tb_user.member_nick_name , tb_user.avatarUrl \n" +
                        "FROM tb_mucc INNER JOIN tb_user ON tb_mucc.memberId = tb_user.memberId \n" +
                        "WHERE tb_mucc.roomid = ? AND tb_mucc._id NOT IN (\n" +
                        "SELECT tb_mucc._id\n" +
                        "FROM tb_mucc \n" +
                        "WHERE tb_mucc.memberId != ? AND tb_mucc.send_type != 2001 AND tb_mucc.roomid = ?)\n" +
                        "ORDER BY tb_mucc.create_time";


                Cursor cursor = sqLiteDatabase.rawQuery(sql1, new String[]{selectionArgs[0],UserBean.getMemberID(),UserBean.getMemberID()});
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case MUCC:
                return "vnd.android.cursor.dir/com.ifeimo.im";
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowID = -1;
        switch (matcher.match(uri)) {
            case MUCC:
                sqLiteDatabase.beginTransaction();
                try {
                    String avatarUrl = contentValues.getAsString(Fields.AccounFields.MEMBER_AVATARURL);
                    String nickName = contentValues.getAsString(Fields.AccounFields.MEMBER_NICKNAME);
                    rowID = muccInsertTest(contentValues);
                    contentValues.put(Fields.AccounFields.MEMBER_AVATARURL,avatarUrl);
                    contentValues.put(Fields.AccounFields.MEMBER_NICKNAME,nickName);
                    sqLiteDatabase.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                if (rowID > 0) {
                    tellToCache(contentValues);
                    Uri wordUri = ContentUris.withAppendedId(uri, rowID);
                    log(" -------  数据插入成功 -------" + contentValues);
                    return wordUri;
                } else if (rowID == -10) {
                    log(" -------  重复数据不插入 -------" + contentValues);
                } else if (rowID == -11) {
                    log(" -------  重复数据,数据更新成功 -------" + contentValues);
                } else if(rowID == -12){
                    log(" -------  头像数据更新-------" + contentValues);
                }else{
                    log(" -------  数据插入失败 -------" + contentValues);
                }
                break;
            default:
                throw new IllegalArgumentException("未知uri:" + uri);
        }
        return null;
    }

    /**
     * 修改
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
                Fields.InformationFields.TYPE,InformationBean.ROOM);
        if(sqLiteDatabase.update(Fields.InformationFields.TB_NAME,
                informationValues,where,
                new String[]{
                        UserBean.getMemberID(),
                        contentValues.getAsString(Fields.GroupChatFields.ROOM_ID),
                })>0) {
            MsgService.contentResolver.notifyChange(InformationProvide.CONTENT_URI, null);
        }
    }

    /**
     * 新增
     * @param contentValues
     */
    protected void tellToCache(ContentValues contentValues) {
        ContentValues c = new ContentValues();
        if (UserBean.getMemberID().equals(contentValues.getAsString(Fields.InformationFields.MEMBER_ID))) {
            c.put(Fields.InformationFields.IS_ME_SEND, 1);
        } else {
            c.put(Fields.InformationFields.IS_ME_SEND, 0);
        }
        c.put(Fields.InformationFields.TYPE, InformationBean.ROOM);
        c.put(Fields.InformationFields.LAST_CONTENT, contentValues.getAsString(Fields.MsgFields.CONTENT));
        c.put(Fields.InformationFields.LAST_CREATETIME, contentValues.getAsString(Fields.MsgFields.CREATE_TIME));
        c.put(Fields.InformationFields.MEMBER_ID, UserBean.getMemberID());
        c.put(Fields.InformationFields.OPPOSITE_ID, contentValues.getAsString(Fields.GroupChatFields.ROOM_ID));
        c.put(Fields.InformationFields.SEND_TYPE, contentValues.getAsString(Fields.MsgFields.SEND_TYPE));
        c.put(Fields.InformationFields.MSG_ID, contentValues.getAsString(Fields.MsgFields.MSG_ID));
        MsgService.contentResolver.update(InformationProvide.CONTENT_URI, c, null, null);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {

        switch (matcher.match(uri)) {
            case MUCC:
                sqLiteDatabase.beginTransaction();
                try {
                    if (sqLiteDatabase.delete(Fields.GroupChatFields.TB_NAME, s, strings) > 0) {
                        log("删除旧数据！");
                    }
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
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int num = 0;
        switch (matcher.match(uri)) {
            case MUCC:
                sqLiteDatabase.beginTransaction();
                try {
                    num = sqLiteDatabase.update(Fields.GroupChatFields.TB_NAME, values, selection, selectionArgs);
                    if (num < 1) {
                        log(" ------- 重发失败！！ -------");
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
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

    /**
     * 新增
     * @param contentValues
     * @return
     */
    private long muccInsertTest( ContentValues contentValues) {

        long insertid = -1;
        String createtime = contentValues.getAsString(Fields.GroupChatFields.CREATE_TIME);
        String roomid = contentValues.getAsString(Fields.GroupChatFields.ROOM_ID);
        String memberid = contentValues.getAsString(Fields.GroupChatFields.MEMBER_ID);
        String avatarUrl = contentValues.getAsString(Fields.AccounFields.MEMBER_AVATARURL);
        String nickName = contentValues.getAsString(Fields.AccounFields.MEMBER_NICKNAME);
        String msgid = contentValues.getAsString(Fields.GroupChatFields.MSG_ID);
        int sendtype = contentValues.getAsInteger(Fields.GroupChatFields.SEND_TYPE);
        contentValues.remove(Fields.AccounFields.MEMBER_AVATARURL);
        contentValues.remove(Fields.AccounFields.MEMBER_NICKNAME);
        if (StringUtil.isNull(memberid)) {
            return -1;
        }

        if (StringUtil.isNull(createtime)) {
            insertid = sqLiteDatabase.insert(Fields.GroupChatFields.TB_NAME, null, contentValues);
            tellToMessageListener(contentValues);
//            insertSubscrition(sqLiteDatabase,contentValues,avatarUrl);
        } else {

            //此消息是否存在
            final Cursor cursor = sqLiteDatabase.rawQuery(String.format(
                    "SELECT * FROM %s WHERE %s = ? AND %s = ? AND %s = ?",
                    Fields.GroupChatFields.TB_NAME, Fields.GroupChatFields.CREATE_TIME, Fields.GroupChatFields.ROOM_ID, Fields.GroupChatFields.MEMBER_ID),
                    new String[]{createtime, roomid, memberid});
            final int count = cursor.getCount();
            cursor.close();
            if (StringUtil.isNull(msgid) && count <= 0) {
                //msgid 为空 并且不存在此行
                insertid = sqLiteDatabase.insert(Fields.GroupChatFields.TB_NAME, null, contentValues);
                tellToMessageListener(contentValues);
//                insertSubscrition(sqLiteDatabase,contentValues,avatarUrl);
            } else if ((msgid != null && !msgid.equals("")) && count <= 0) {
                //不存在此行，但是需要判断此msgid是否已存在
                final Cursor cursorMsg = sqLiteDatabase.rawQuery(String.format(
                        "SELECT * FROM %s WHERE %s = ?", Fields.GroupChatFields.TB_NAME, Fields.GroupChatFields.MSG_ID), new String[]{msgid});
                if (cursorMsg.getCount() == 1) {
                    cursorMsg.moveToFirst();
                    if (MD5.getMD5(msgid).equals(MD5.getMD5(cursorMsg.getString(cursorMsg.getColumnIndex(Fields.GroupChatFields.MSG_ID))))
                            && cursorMsg.getInt(cursorMsg.getColumnIndex(Fields.GroupChatFields.SEND_TYPE)) != Fields.GroupChatFields.SEND_FINISH) {
                        ContentValues c1 = new ContentValues();
                        if(sendtype == Fields.GroupChatFields.SEND_FINISH || sendtype == Fields.GroupChatFields.SEND_UNCONNECT) {
                            c1.put(Fields.GroupChatFields.CREATE_TIME, createtime);
                        }
                        c1.put(Fields.GroupChatFields.SEND_TYPE, sendtype);
                        if (sqLiteDatabase.update(Fields.GroupChatFields.TB_NAME, c1, Fields.GroupChatFields.MSG_ID + " = ?", new String[]{msgid}) > 0) {
                            insertid = -11;
                            tellToCache2(contentValues);
                        }
                    }
                } else {
                    insertid = sqLiteDatabase.insert(Fields.GroupChatFields.TB_NAME, null, contentValues);
                    tellToMessageListener(contentValues);
//                    insertSubscrition(sqLiteDatabase,contentValues,avatarUrl);
                }
                cursorMsg.close();
            }
        }

        //此人员是否存在
        Cursor cursor = sqLiteDatabase.rawQuery(String.format(
                "SELECT * FROM %s WHERE %s = ? ",
                Fields.AccounFields.TB_NAME, Fields.AccounFields.MEMBER_ID),
                new String[]{memberid});
        if (cursor.getCount() <= 0) {
            //不存在新增人员消息
            ContentValues c = new ContentValues();
            c.put(Fields.AccounFields.MEMBER_ID, memberid);
            c.put(Fields.AccounFields.MEMBER_AVATARURL, avatarUrl);
            c.put(Fields.AccounFields.MEMBER_NICKNAME, nickName);
            if ((insertid = sqLiteDatabase.insert(Fields.AccounFields.TB_NAME, null, c))> 0) {
                cursor.close();
//                return insertid;
            }
        } else {
            //存在则判断是否更新
            cursor.moveToFirst();
            String oldavatarUrl = cursor.getString(cursor.getColumnIndex(Fields.AccounFields.MEMBER_AVATARURL));
            String oldnickName = cursor.getString(cursor.getColumnIndex(Fields.AccounFields.MEMBER_NICKNAME));
            if (!MD5.getMD5(memberid + oldnickName + oldavatarUrl).equals(
                    MD5.getMD5(memberid + nickName + avatarUrl))) {
                ContentValues c = new ContentValues();
                c.put(Fields.AccounFields.MEMBER_AVATARURL, avatarUrl);
                c.put(Fields.AccounFields.MEMBER_NICKNAME, nickName);
                if (sqLiteDatabase.update(Fields.AccounFields.TB_NAME, c, String.format(" %s = %s", Fields.GroupChatFields.MEMBER_ID, memberid), null)
                        > 0 && insertid > 0) {
                    cursor.close();
//                    return insertid;
                }

            }
            cursor.close();
        }

        return insertid;

    }

    /**
     * 订阅此群聊
     * @param sqLiteDatabase
     * @param contentValues
     * @param avatarUrl
     * @return
     */
    private long insertSubscrition(SQLiteDatabase sqLiteDatabase,ContentValues contentValues,String avatarUrl){
        Cursor cursor = sqLiteDatabase.query(Fields.SubscriptionFields.TB_NAME,new String[]{Fields.AccounFields.ID},
                String.format("%s = ? AND %s = ?",Fields.SubscriptionFields.SUBSCRIPTION_ID,Fields.SubscriptionFields.MEMBER_ID),
                new String[]{contentValues.getAsString(Fields.GroupChatFields.ROOM_ID),UserBean.getMemberID()},
                null,null,null);
        ContentValues subscritionValues = new ContentValues();
        long id;
        subscritionValues.put(Fields.SubscriptionFields.PICURL,avatarUrl);
        if(cursor != null && cursor.getCount() == 1 && cursor.moveToFirst()){
            id = cursor.getInt(0);
//            sqLiteDatabase.update(Fields.SubscriptionFields.TB_NAME,subscritionValues,Fields.SubscriptionFields.ID+" = "+id,null);
//            log("----- modi subscrition room "+contentValues.getAsString(Fields.GroupChatFields.ROOM_ID)+" -----");
        }else{
            subscritionValues.put(Fields.SubscriptionFields.SUBSCRIPTION_ID,contentValues.getAsString(Fields.GroupChatFields.ROOM_ID));
            subscritionValues.put(Fields.SubscriptionFields.TYPE,InformationBean.ROOM);
            subscritionValues.put(Fields.SubscriptionFields.MEMBER_ID,UserBean.getMemberID());
            subscritionValues.put(Fields.SubscriptionFields.NAME,"群聊 "+contentValues.getAsString(Fields.GroupChatFields.ROOM_ID));
            id = sqLiteDatabase.insert(Fields.SubscriptionFields.TB_NAME,null,subscritionValues);
            log("----- insert subscrition room "+contentValues.getAsString(Fields.GroupChatFields.ROOM_ID)+" -----");
        }
        cursor.close();

        return id;
    }

    private void tellToMessageListener(ContentValues contentValues){
        final String memberid = contentValues.getAsString(Fields.GroupChatFields.MEMBER_ID);
        final String roomid = contentValues.getAsString(Fields.GroupChatFields.ROOM_ID);
        if(UserBean.getMd5MemberID().equals(MD5.getMD5(memberid))){
            return;
        }
        ThreadUtil.getInstances().createThreadStartToCachedThreadPool(new Runnable() {
            @Override
            public void run() {

                final OnSimpleMessageListener onSimpleMessageListener = Proxy.getMessageManager().getOnMessageReceiver();
                if(onSimpleMessageListener != null){
                    onSimpleMessageListener.groupChat(memberid,roomid);
                }
            }
        });
    }


}
