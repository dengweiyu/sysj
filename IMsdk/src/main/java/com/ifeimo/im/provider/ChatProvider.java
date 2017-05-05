package com.ifeimo.im.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.ifeimo.im.common.adapter.BaseChatReCursorAdapter;
import com.ifeimo.im.common.util.StringUtil;

import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/1/11.
 */
public class ChatProvider extends BaseProvider {

    public static final String PROVIDER_NAME = "com.ifeimo.im.db.chat";
    private static UriMatcher matcher;
    private static final int CHAT = 1;
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/chat");
//    public static final Uri CONTEXT_BY_ID = Uri.parse("content://" + PROVIDER_NAME + "/deletebyid");



    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(PROVIDER_NAME, "chat", CHAT);

    }

//    @Override
//    public boolean onCreate() {
//       return super.onCreate();
//    }

    @Override
    public Cursor query(final Uri uri, String[] projection, final String selection, final String[] selectionArgs, String sortOrder) {
        switch (matcher.match(uri)) {
            case CHAT:
                final Cursor[] ret = {null};
                Access.runCustomThread(new Execute() {
                    @Override
                    public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                        String sql = String.format(
                                "SELECT * " +
                                        "FROM " +
                                        "(SELECT * \n" +
                                        "FROM tb_chat  \n" +
                                        "WHERE (receiverId = ? AND memberId = ? AND send_type = 2001) OR (receiverId = ? AND memberId = ?) \n" +
                                        "ORDER BY create_time " +
                                        "DESC " +
                                        "LIMIT %s) " +
                                        "ORDER BY create_time ", Integer.parseInt(StringUtil.isNull(selection) ? "5" : selection) * BaseChatReCursorAdapter.MAX_PAGE_COUNT);
                        ret[0] = sqLiteDatabase.rawQuery(sql, new String[]{
                                selectionArgs[0], selectionArgs[1],
                                selectionArgs[1], selectionArgs[0],
                        }, null);
                        ret[0].setNotificationUri(getContext().getContentResolver(), uri);
                    }

                    @Override
                    public void onExternalError() {

                    }
                });
                return ret[0];
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
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


}
