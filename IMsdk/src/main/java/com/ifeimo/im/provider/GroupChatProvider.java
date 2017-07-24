package com.ifeimo.im.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.ifeimo.im.common.adapter.BaseChatReCursorAdapter;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.Proxy;

import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/1/11.
 */
public class GroupChatProvider extends BaseProvider {
    public static final String PROVIDER_NAME = "com.ifeimo.im.db.mucc";
    private static UriMatcher matcher;
    private static final int MUCC = 2;
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/mucc");

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(PROVIDER_NAME, "mucc", MUCC);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(final Uri uri, String[] projection, final String selection, final String[] selectionArgs, String sortOrder) {
        switch (matcher.match(uri)) {
            case MUCC:
                final Cursor[] cursor = {null};
                Access.runCustomThread(new Execute() {
                    @Override
                    public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                        String sql1 =
                                String.format(
                                        "SELECT * " +
                                                "FROM (" +
                                                    "SELECT tb_groupchat.* , tb_account.member_nick_name , tb_account.avatarUrl \n" +
                                                    "FROM tb_groupchat " +
                                                    "INNER JOIN tb_account ON tb_groupchat.memberId = tb_account.memberId \n" +
                                                    "WHERE tb_groupchat.roomid = ? AND ((tb_groupchat.memberId != ? AND tb_groupchat.send_type = 2001) or tb_groupchat.memberId = ?)\n" +
                                                    "ORDER BY tb_groupchat.create_time " +
                                                    "DESC " +
                                                    "LIMIT %s) " +
                                                "ORDER BY create_time",
                                        BaseChatReCursorAdapter.MAX_PAGE_COUNT * Integer.parseInt(StringUtil.isNull(selection) ? "5" : selection));
                        cursor[0] = sqLiteDatabase.rawQuery(sql1, new String[]{selectionArgs[0], Proxy.getAccountManger().getUserMemberId(), Proxy.getAccountManger().getUserMemberId()});
                        cursor[0].setNotificationUri(getContext().getContentResolver(), uri);
                    }

                    @Override
                    public void onExternalError() {

                    }
                });

                return cursor[0];
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
