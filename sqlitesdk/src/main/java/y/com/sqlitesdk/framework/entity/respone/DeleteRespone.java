package y.com.sqlitesdk.framework.entity.respone;

import y.com.sqlitesdk.framework.interface_model.IModel;

/**
 * Created by lpds on 2017/4/17.
 */
public class DeleteRespone<T extends IModel<T>> {

    private long deleteId;
    private T model;

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public long getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(long deleteId) {
        this.deleteId = deleteId;
    }

    public DeleteRespone(long deleteId, T model) {
        this.deleteId = deleteId;
        this.model = model;
    }
}
