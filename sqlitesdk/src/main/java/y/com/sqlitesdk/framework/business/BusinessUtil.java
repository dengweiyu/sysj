package y.com.sqlitesdk.framework.business;


import android.content.ContentValues;
import android.database.Cursor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import y.com.sqlitesdk.framework.annotation.TBColumn;
import y.com.sqlitesdk.framework.annotation.TBForeign;
import y.com.sqlitesdk.framework.annotation.TBPrimarykey;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.db.exception.NoColumnException;
import y.com.sqlitesdk.framework.db.exception.PrimarykeyException;
import y.com.sqlitesdk.framework.interface_model.IModel;
import y.com.sqlitesdk.framework.util.StringDdUtil;

/**
 * Created by lpds on 2017/4/13.
 */
public final class BusinessUtil {


    /**
     * 反射获取一组数据
     * 数据模型的属性必须包含数据集中的属性，且变量名一致
     *
     * @param cursor 数据集
     * @param tClass 数据模型
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     */
    public static <T extends IModel<T>> List<T> reflectCursor(Cursor cursor, Class<T> tClass)
            throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        List<T> list = new ArrayList<>();
        if (cursor.isClosed()) {
            return list;
        }
        String[] column = new String[cursor.getColumnCount()];
        if (cursor.moveToFirst()) {

            int leng = 0;
            for (; leng < column.length; leng++) {
                column[leng] = cursor.getColumnName(leng);
            }
            do {
                leng = 0;
                T t = tClass.newInstance();
                for (; leng < column.length; leng++) {
                    Field field = getDeclaredField(t, column[leng]);
                    field.setAccessible(true);
                    final String type = field.getType().toString();
                    if (type.endsWith("String")) {
                        field.set(t, cursor.getString(cursor.getColumnIndex(column[leng])));
                    } else if (type.endsWith("int") || type.endsWith("Integer") ||
                            type.endsWith("long") || type.endsWith("Long")) {
                        field.setInt(t, cursor.getInt(cursor.getColumnIndex(column[leng])));
                    } else if (type.endsWith("Boolean") || type.endsWith("boolean")) {
                        if (cursor.getInt(cursor.getColumnIndex(column[leng])) == 1) {
                            field.setBoolean(t, true);
                        } else if (cursor.getInt(cursor.getColumnIndex(column[leng])) == 0) {
                            field.setBoolean(t, false);
                        }
                    } else if (type.endsWith("Double") || type.endsWith("double")
                            || type.endsWith("Float") || type.endsWith("float")) {
                        field.setFloat(t, cursor.getFloat(cursor.getColumnIndex(column[leng])));
                    }
                }
                list.add(t);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 寻找此属性，递归到父类
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;

        Class<?> clazz = object.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
                //这里甚么都不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会进入
//                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 创建构建表格数据库sql语句
     *
     * @param tClass
     * @param <T>
     * @return 数组下标 0 为sql语句 ，1为表名
     * @throws Exception
     */
    public static <T extends IModel> String[] getTableStr(String tableName, Class<T> tClass) throws Exception {
        Field[] fields = tClass.getDeclaredFields();
        List<String> sqls = new ArrayList<>();
        //语句拼装的组合
        List<String> sqlList = new ArrayList<>();
        boolean keyFlag = false;
        for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];
            field.setAccessible(true);
            final Annotation[] annotations = fields[i].getAnnotations();
            /**
             * 要添加的语句
             */
            String lineSentence = "";

            for (int j = 0; j < annotations.length; j++) {
                final Annotation annotation = annotations[j];
                if (annotation instanceof TBPrimarykey) {
                    if (!keyFlag) {
                        keyFlag = true;
                        sqlList.add(0, String.format(" %s %s primary key autoincrement,", field.getName(), getColumnStr(field)));
                        break;
                    } else {
                        throw new PrimarykeyException("主键必须在 " + tClass.getName() + " 表格中唯一 :" + field.getName() + " 错误！");
                    }
                } else if (annotation instanceof TBColumn) {
                    lineSentence += String.format(" %s %s  ", field.getName(), getColumnStr(field));
                    TBColumn tbColumn = (TBColumn) annotation;
                    if(tbColumn.notNull()){
                        lineSentence +=" NOT NULL ";
                    }
                    if(tbColumn.unique()){
                        lineSentence +=" UNIQUE ";
                    }
                    if(field.getType().toString().endsWith("Integer")
                            || field.getType().toString().endsWith("Long")) {
                        if (tbColumn.autoincrememt()) {
                            lineSentence += " autoincrement ";
                        }
                    }
                } else if (annotation instanceof TBForeign) {
                    final TBForeign tbForeign = (TBForeign) annotation;
                    final String tbName = tbForeign.referencesTableName();
                    final String tbColunm = tbForeign.referencesTableField();
                    if (!StringDdUtil.isNull(tbName) && !StringDdUtil.isNull(tbColunm)) {
                        //lineSentence += String.format(",FOREIGN KEY(%s) REFERENCES %s(%s)", field.getName(), tbName, tbColunm);
                        String name = "trigger_"+field.getName() + "_" + tableName;
                        if(!Access.getAlltriggers().contains(name)) {
                            sqls.add(String.format(
                                    "\nCREATE TRIGGER " + name + " BEFORE DELETE ON " + tbName + "\n" +
                                            "FOR EACH ROW \n" +
                                            "BEGIN\n" +
                                            "   DELETE FROM " + tableName + " WHERE " + field.getName() + " = old." + tbColunm + " ;\n" +
                                            "END;"));
                        }
                    }
                }
            }
            if (!StringDdUtil.isNull(lineSentence)) {
                lineSentence += " ,";
                sqlList.add(lineSentence);
            }

        }
        sqlList.add(0, String.format("CREATE TABLE IF NOT EXISTS %s (", tableName));
        if (sqlList.size() == 0) {
            throw new NoColumnException();
        }
        String oneSql = "";
        for (int i = 0; i < sqlList.size(); i++) {
            if (i == sqlList.size() - 1) {
                oneSql += sqlList.get(i).substring(0,sqlList.get(i).length()-1)+")";
            } else {
                oneSql += sqlList.get(i);
            }
        }
        sqls.add(0, oneSql);
        return sqls.toArray(new String[sqls.size()]);
    }

    /**
     * 构建表格的field转换成 属性数据类型
     *
     * @param field
     * @return
     */
    private static String getColumnStr(Field field) {
        if (field.getType().toString().endsWith("String")) {
            return " nvarchar(50) ";
        } else if (field.getType().toString().endsWith("int")
                || field.getType().toString().endsWith("Integer")) {
            return " integer ";
        } else if (field.getType().toString().endsWith("long")
                || field.getType().toString().endsWith("Long")) {
            return " integer ";
        } else if (field.getType().toString().endsWith("Boolean")
                || field.getType().toString().endsWith("boolean")) {
            return " bit ";
        } else if (field.getType().toString().endsWith("Double")
                || field.getType().toString().endsWith("double")
                || field.getType().toString().endsWith("Float")
                || field.getType().toString().endsWith("float")) {
            return " float ";
        }
        return "text";
    }

    /**
     * model转换，获取contentvalues
     *
     * @param clone
     * @param <T>
     * @return
     */
    public static <T extends IModel<T>> ContentValues getAllValues(T clone) throws IllegalAccessException {
        Field[] fields = clone.getClass().getDeclaredFields();
        ContentValues contentValues = new ContentValues();
        fields = BusinessUtil.checkHasColunm(fields);
        for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];
            field.setAccessible(true);
            if (field.getType().toString().endsWith("String")) {
                final String str;
                if (!StringDdUtil.isNull(str = (String) field.get(clone))) {
                    contentValues.put(field.getName(), str);
                }
            } else if (field.getType().toString().endsWith("int")
                    || field.getType().toString().endsWith("Integer")) {
                contentValues.put(field.getName(), field.getInt(clone));
            } else if (field.getType().toString().endsWith("long")
                    || field.getType().toString().endsWith("Long")) {
                contentValues.put(field.getName(), field.getLong(clone));
            } else if (field.getType().toString().endsWith("Boolean")
                    || field.getType().toString().endsWith("boolean")) {
                contentValues.put(field.getName(), field.getBoolean(clone));
            } else if (field.getType().toString().endsWith("Double")
                    || field.getType().toString().endsWith("double")
                    || field.getType().toString().endsWith("Float")
                    || field.getType().toString().endsWith("float")) {
                contentValues.put(field.getName(), field.getFloat(clone));
            }
        }
        return contentValues;


    }

    /**
     * 转换获得属性值
     * @param field
     * @param model
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <T extends IModel<T>> String convertField(Field field,T model) throws IllegalAccessException {
        field.setAccessible(true);
        if (field.getType().toString().endsWith("String")) {
            final String str;
            if (!StringDdUtil.isNull(str = (String) field.get(model))) {
                return str;
            }
        } else if (field.getType().toString().endsWith("int")
                || field.getType().toString().endsWith("Integer")) {
            return field.getInt(model)+"";
        } else if (field.getType().toString().endsWith("long")
                || field.getType().toString().endsWith("Long")) {
            return field.getLong(model)+"";
        } else if (field.getType().toString().endsWith("Boolean")
                || field.getType().toString().endsWith("boolean")) {
            return field.getBoolean(model)?1+"":0+"";
        } else if (field.getType().toString().endsWith("Double")
                || field.getType().toString().endsWith("double")
                || field.getType().toString().endsWith("Float")
                || field.getType().toString().endsWith("float")) {
            return field.getFloat(model)+"";
        }
        return null;
    }

    /**
     * 获得表格的属性，过滤不是表格的属性
     *
     * @param fields
     * @return
     */
    private static Field[] checkHasColunm(Field[] fields) {
        if (fields == null) {
            return null;
        }
        List<Field> fieldList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];
            field.setAccessible(true);
            boolean flag = false;
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof TBColumn) {
                    flag = true;
                }
            }
            if (flag) {
                fieldList.add(field);
            }
        }
        return fieldList.toArray(new Field[fieldList.size()]);
    }

    /**
     * 获取表格名
     * @param tclass
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T extends IModel> String getTbNmae(Class<T> tclass) throws IllegalAccessException, InstantiationException {
        return tclass.newInstance().getTableName();
    }

    /**
     * 获取所有唯一字段
     * @param model
     * @param <T>
     * @return
     */
    public static <T extends IModel<T>> Field[] getAllUniqueFields(T model) {
        List<Field> uniqueFields = new ArrayList<>();
        Field[] fields = model.getClass().getDeclaredFields();
        for(Field field : fields){
            Annotation[] annotations = field.getAnnotations();
            for(Annotation annotation : annotations){
                if(annotation instanceof TBColumn){
                    final TBColumn tBColumn  = (TBColumn) annotation;
                    if(tBColumn.unique()){
                        uniqueFields.add(field);
                        break;
                    }
                }
//                if(annotation instanceof TBForeign){
//                    uniqueFields.add(field);
//                    break;
//                }
            }
        }
        return uniqueFields.toArray(new Field[uniqueFields.size()]);
    }


    public static <T extends IModel<T>> T getLineModelByCursor(Class<T> tClass, Cursor cursor){

        String[] column = cursor.getColumnNames();
        int leng = 0;
        try {
            T t = tClass.newInstance();
            for (; leng < column.length; leng++) {
                Field field = getDeclaredField(t, column[leng]);
                field.setAccessible(true);
                final String type = field.getType().toString();
                if (type.endsWith("String")) {
                    field.set(t, cursor.getString(cursor.getColumnIndex(column[leng])));
                } else if (type.endsWith("int") || type.endsWith("Integer") ||
                        type.endsWith("long") || type.endsWith("Long")) {
                    field.setInt(t, cursor.getInt(cursor.getColumnIndex(column[leng])));
                } else if (type.endsWith("Boolean") || type.endsWith("boolean")) {
                    if (cursor.getInt(cursor.getColumnIndex(column[leng])) == 1) {
                        field.setBoolean(t, true);
                    } else if (cursor.getInt(cursor.getColumnIndex(column[leng])) == 0) {
                        field.setBoolean(t, false);
                    }
                } else if (type.endsWith("Double") || type.endsWith("double")
                        || type.endsWith("Float") || type.endsWith("float")) {
                    field.setFloat(t, cursor.getFloat(cursor.getColumnIndex(column[leng])));
                }
            }
            return t;
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;

    }

}
