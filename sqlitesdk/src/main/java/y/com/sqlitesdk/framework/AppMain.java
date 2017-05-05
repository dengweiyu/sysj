package y.com.sqlitesdk.framework;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lpds on 2017/4/14.
 */
public interface AppMain {


    Application getApplication();

    void runOnUiThread(Runnable runnable);

    SQLiteDatabase getSQLiteDatabase();

    SQLiteOpenHelper getSQLiteOpenHelper();

    void stopAllRunnable();


}
