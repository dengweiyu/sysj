package com.ifeimo.im.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ifeimo.im.framwork.Proxy;

import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/1/24.
 */
public class InformationProvide extends BaseProvider {
    public static final String PROVIDER_NAME = "com.ifeimo.im.db.cachemsglist";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/Cache");
    private static UriMatcher matcher;
    private static final int Cache = 0x111;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(PROVIDER_NAME, "Cache", Cache);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(final Uri uri, String[] strings, String s, String[] strings1, String s1) {

        switch (matcher.match(uri)) {
            case Cache:
                final Cursor[] cursor = new Cursor[1];
                Access.runCustomThread(new Execute() {
                    @Override
                    public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                        String memberid = Proxy.getAccountManger().getUserMemberId();
                        String sql = "SELECT * FROM  \n" +
                                        "               (  SELECT tb_information.*,tb_account.member_nick_name as title,tb_account.avatarUrl as pic_url  \n" +
                                        "               FROM tb_information,tb_account  \n" +
                                        "               WHERE tb_information.memberId = ? AND  tb_information.opposite_id = tb_account.memberId   )  \n" +
                                        "               UNION ALL  \n" +
                                        "                     SELECT tb_information.*,tb_subscription.subscription_name as title,tb_subscription.subscription_pic_url as pic_url   \n" +
                                        "                     FROM tb_information,tb_subscription,tb_account2subscription   \n" +
                                        "                     WHERE  tb_account2subscription.memberId = ? \n" +
                                        "                     AND tb_information.memberId = tb_account2subscription.memberId \n" +
                                        "                     AND tb_account2subscription.subscription_id =   tb_subscription.id \n" +
                                        "                     AND tb_information.opposite_id = tb_subscription.subscription_id \n" +
                                "   ORDER BY last_create_time DESC";
                        Log.i(TAG, "onExecute: "+sql);
                        cursor[0] = sqLiteDatabase.rawQuery(sql, new String[]{memberid,memberid});
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
