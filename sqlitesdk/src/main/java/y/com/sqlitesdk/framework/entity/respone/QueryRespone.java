package y.com.sqlitesdk.framework.entity.respone;

import java.util.List;

import y.com.sqlitesdk.framework.entity.BaseEntity;
import y.com.sqlitesdk.framework.interface_model.IModel;

/**
 * Created by lpds on 2017/4/15.
 */
public class QueryRespone<T extends IModel<T>> extends BaseEntity {
    List<T> data;
    boolean isOk;
    String tableName;
    public QueryRespone(List<T> data,boolean isOk,String table) {
        this.data = data;
        this.isOk = isOk;
        this.tableName = table;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
