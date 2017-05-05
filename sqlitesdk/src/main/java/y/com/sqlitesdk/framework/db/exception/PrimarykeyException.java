package y.com.sqlitesdk.framework.db.exception;

import java.lang.reflect.Field;

/**
 * Created by lpds on 2017/4/15.
 */
public class PrimarykeyException extends Exception {
    public PrimarykeyException(String message){
        super(message);
    }
}
