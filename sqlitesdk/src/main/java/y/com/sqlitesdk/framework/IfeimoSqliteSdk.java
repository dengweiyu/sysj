package y.com.sqlitesdk.framework;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import y.com.sqlitesdk.framework.db.Access;

/**
 * Created by lpds on 2017/4/14.
 */
public final class IfeimoSqliteSdk {

    public static AppMain IPMAIN;
    public static void init(AppMain main){
        IfeimoSqliteSdk.IPMAIN = main;
        Access.setSqliteDB(main.getSQLiteDatabase());
    }

}
