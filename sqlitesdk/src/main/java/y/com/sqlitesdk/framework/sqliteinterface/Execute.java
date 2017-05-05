package y.com.sqlitesdk.framework.sqliteinterface;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lpds on 2017/4/10.
 */
public interface Execute{

    void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception;

    void onExternalError();

}
