package y.com.sqlitesdk.framework.server;


import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import y.com.sqlitesdk.framework.interface_model.IModel;

/**
 * 成功与否，eventbus返回
 * Created by lpds on 2017/4/12.
 */
public interface TableServer extends ITag{
    /**
     * TableCreateRespone
     * @param tClass
     * @param <T>
     */
    <T extends IModel<T>> void createTable(Class<T> tClass) throws InstantiationException, IllegalAccessException;//测试成功

    /**
     * InsertRespone
     * @param model
     * @param <T>
     */
    <T extends IModel<T>> void insert(T model);//测试成功

    /**
     * InsertListRespone
     * @param models
     * @param <T>
     */
    <T extends IModel<T>> void insert(List<T> models);//测试成功

    /**
     * DeleteRespone
     * @param model
     * @param <T>
     */
    <T extends IModel<T>> void deleteById(T model);//测试成功

    /**
     * DeleteRespone
     * @param model
     * @param where
     * @param <T>
     */
    <T extends IModel<T>> void delete(T model,String where,String[] args);//测试成功

    /**
     * DeleteAllRespone
     * @param tablename
     */
    void deleteAll(String tablename);//测试成功

    /**
     * ModifyRespone
     * @param model
     * @param <T>
     */
    <T extends IModel<T>> void modify(T model);//测试成功

    /**
     * ModifyRespone
     * @param model
     * @param where
     * @param <T>
     */
    <T extends IModel<T>> void modify(T model, String where);//测试成功

    /**
     * QuerySingleRespone
     * @param model
     * @param <T>
     */
    <T extends IModel<T>> void queryById(T model);//测试成功

    /**
     * QueryRespone
     * @param tClass
     * @param <T>
     */
    <T extends IModel<T>> void queryAll(Class<T> tClass);//测试成功

    /**
     * QueryRespone
     * @param tClass
     * @param sql
     * @param <T>
     */
    <T extends IModel<T>> void executeSQLQuery(Class<T> tClass , String sql);//测试成功

    <T extends IModel<T>> void getTableMaxCount(Class<T> tClass, String where);

}
