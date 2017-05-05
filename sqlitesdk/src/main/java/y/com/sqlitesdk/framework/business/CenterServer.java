package y.com.sqlitesdk.framework.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import y.com.sqlitesdk.framework.business.Business;
import y.com.sqlitesdk.framework.business.BusinessUtil;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.db.exception.PrimarykeyException;
import y.com.sqlitesdk.framework.entity.respone.DeleteAllRespone;
import y.com.sqlitesdk.framework.entity.respone.DeleteRespone;
import y.com.sqlitesdk.framework.entity.respone.InsertListRespone;
import y.com.sqlitesdk.framework.entity.respone.InsertRespone;
import y.com.sqlitesdk.framework.entity.respone.ModifyRespone;
import y.com.sqlitesdk.framework.entity.respone.QueryRespone;
import y.com.sqlitesdk.framework.entity.respone.QuerySingleRespone;
import y.com.sqlitesdk.framework.entity.respone.TableCreateRespone;
import y.com.sqlitesdk.framework.entity.respone.TableMaxCountRespone;
import y.com.sqlitesdk.framework.interface_model.IModel;
import y.com.sqlitesdk.framework.server.TableServer;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/4/19.
 */
public class CenterServer implements TableServer {

    private static CenterServer centerServer;

    static {
        centerServer = new CenterServer();
    }

    private CenterServer() {

    }

    public static TableServer getInstances() {
        return centerServer;
    }

    @Override
    public <T extends IModel<T>> void createTable(final Class<T> tClass) throws InstantiationException, IllegalAccessException {
        final String tb_name = BusinessUtil.getTbNmae(tClass);
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                if (Business.getInstances().createTable(sqLiteDatabase, tClass)) {
                    EventBus.getDefault().post(new TableCreateRespone(true, tb_name));
                }
            }

            @Override
            public void onExternalError() {
                EventBus.getDefault().post(new TableCreateRespone(false, tb_name));
            }
        });
    }

    @Override
    public <T extends IModel<T>> void insert(T model) {
        final T m = model.clone();
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
                long index = Business.getInstances().insert(sqLiteDatabase, m);
                if (index < 1) {
                    EventBus.getDefault().post(new InsertRespone(-1, m));
                } else {
                    EventBus.getDefault().post(new InsertRespone(index, m));
                }
            }

            @Override
            public void onExternalError() {
                EventBus.getDefault().post(new InsertRespone(-1, m));
            }

        });
    }

    @Override
    public <T extends IModel<T>> void insert(List<T> models) {
        if (models == null || models.size() == 0) {
            return;
        }
        final List<T> modellist = new ArrayList<>(models);
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
                if(Business.getInstances().insert(sqLiteDatabase, modellist)){
                    EventBus.getDefault().post(new InsertListRespone(true));
                }

            }

            @Override
            public void onExternalError() {
                EventBus.getDefault().post(new InsertListRespone(false));
            }

        });
    }

    @Override
    public <T extends IModel<T>> void deleteById(T model) {
        delete(model, String.format("%s = %s", "id", String.valueOf(model.getId())),null);
    }

    @Override
    public <T extends IModel<T>> void delete(T model, final String where,final String[] args) {
        final T m = model.clone();
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) {
                long id = Business.getInstances().delete(sqLiteDatabase, m, where,args);
                EventBus.getDefault().post(new DeleteRespone(id, m));
            }

            @Override
            public void onExternalError() {
                EventBus.getDefault().post(new DeleteRespone(-1, m));
            }
        });
    }

    @Override
    public void deleteAll(final String tablename) {
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) {
                if(Business.getInstances().deleteAll(sqLiteDatabase, tablename)) {
                    EventBus.getDefault().post(new DeleteAllRespone(true));
                }
            }

            @Override
            public void onExternalError() {
                EventBus.getDefault().post(new DeleteAllRespone(false));
            }
        });
    }

    @Override
    public <T extends IModel<T>> void modify(T model) {
        modify(model, String.format(" %s = %s ", "id", String.valueOf(model.getId())));
    }

    @Override
    public <T extends IModel<T>> void modify(T model, final String where) {
        final T copyModel = model.clone();
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
                long updataid = -1;
                if((updataid = Business.getInstances().modify(sqLiteDatabase, copyModel, where,null)) > 0){
                    EventBus.getDefault().post(new ModifyRespone(updataid, copyModel));
                }
            }

            @Override
            public void onExternalError() {
                EventBus.getDefault().post(new ModifyRespone(-1, copyModel));
            }

        });
    }

    @Override
    public <T extends IModel<T>> void queryById(T model) {
        final T copyModel = model.clone();
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
                T queryModel;
                if((queryModel = Business.getInstances().queryById(sqLiteDatabase, copyModel)) != null){
                    EventBus.getDefault().post(new QuerySingleRespone(queryModel));
                }else{
                    EventBus.getDefault().post(new QuerySingleRespone<T>(null));
                }
            }

            @Override
            public void onExternalError() {
                EventBus.getDefault().post(null);
            }
        });
    }

    @Override
    public <T extends IModel<T>> void queryAll(final Class<T> tClass) {
        final String tableName;
        try {
            tableName = BusinessUtil.getTbNmae(tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
                EventBus.getDefault().post(new QueryRespone<T>(Business.getInstances().queryAll(sqLiteDatabase, tClass,null,null), true, BusinessUtil.getTbNmae(tClass)));
            }

            @Override
            public void onExternalError() {
                EventBus.getDefault().post(new QueryRespone<T>(null, true, tableName));
            }
        });
    }

    @Override
    public <T extends IModel<T>> void executeSQLQuery(final Class<T> tClass, final String sql) {
        final String tbName;
        try {
            tbName = BusinessUtil.getTbNmae(tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
                EventBus.getDefault().post(new QueryRespone(Business.getInstances().executeSQLQuery(sqLiteDatabase, tClass, sql), true, BusinessUtil.getTbNmae(tClass)));
            }

            @Override
            public void onExternalError() {
                EventBus.getDefault().post(new QueryRespone(null, false, tbName));
            }
        });
    }

    @Override
    public <T extends IModel<T>> void getTableMaxCount(final Class<T> tClass, final String where) {
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                long maxCount = Business.getInstances().getTableMaxCount(sqLiteDatabase,tClass,where);
                EventBus.getDefault().post(new TableMaxCountRespone(maxCount,where));
            }

            @Override
            public void onExternalError() {
                EventBus.getDefault().post(new TableMaxCountRespone(-1,where));
            }
        });
    }

    @Override
    public String getTag() {
        return null;
    }
}
