package y.com.sqlitesdk.framework.db.exception;

/**
 * Created by lpds on 2017/4/15.
 */
public class NoColumnException extends Exception {
    public static final String MESSAGE1 = "没有可构建的表字段！";
    public NoColumnException(){
        super(MESSAGE1);
    }

}
