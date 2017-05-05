package y.com.sqlitesdk.framework.entity;

/**
 * Created by lpds on 2017/4/15.
 */
public class BaseIdEntity extends BaseEntity {

    protected long id = -1;

    public BaseIdEntity(long i) {
        this.id = i;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
