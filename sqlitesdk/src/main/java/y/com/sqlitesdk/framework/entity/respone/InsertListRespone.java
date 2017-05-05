package y.com.sqlitesdk.framework.entity.respone;

import java.util.List;

import y.com.sqlitesdk.framework.entity.BaseEntity;

/**
 * Created by lpds on 2017/4/17.
 */
public class InsertListRespone extends BaseEntity {

    public InsertListRespone(boolean isiInsers) {
        this.isiInsers = isiInsers;
    }

    boolean isiInsers;

    public boolean isiInsers() {
        return isiInsers;
    }

    public InsertListRespone setIsiInsers(boolean isiInsers) {
        this.isiInsers = isiInsers;
        return this;
    }
}
