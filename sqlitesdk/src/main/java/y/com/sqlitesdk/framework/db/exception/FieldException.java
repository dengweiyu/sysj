package y.com.sqlitesdk.framework.db.exception;

import java.util.IllegalFormatException;

/**
 * Created by lpds on 2017/4/15.
 */
public class FieldException extends IllegalArgumentException {

    public FieldException() {
        super("字段类型转换错误！");
    }
}
