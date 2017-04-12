package com.ifeimo.im.framwork.database.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IntegerRes;
import android.util.Log;

import com.ifeimo.im.common.MD5;
import com.ifeimo.im.common.bean.AccountBean;
import com.ifeimo.im.common.bean.InformationBean;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.common.util.ThreadUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.database.IMDataBaseHelper;
import com.ifeimo.im.framwork.database.access.Access;
import com.ifeimo.im.framwork.database.iduq.OnQureListener;
import com.ifeimo.im.framwork.interface_im.IMWindow;
import com.ifeimo.im.framwork.message.IInformation;
import com.ifeimo.im.provider.BaseProvider;
import com.ifeimo.im.provider.InformationProvide;

/**
 * Created by lpds on 2017/2/14.
 */
public class Business{

    static Business business;
    static {
        business = new Business();
    }

    private final static String TAG = "XMPP_DB_Business";
    private Business(){

    }

    public static Business getInstances(){
        return business;
    }

    /**
     * 查找人员 异步
     *
     * @param memberid
     * @param onQureListener
     */
    @Deprecated
    public void queryByID(final String memberid, final OnQureListener<AccountBean> onQureListener) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Cursor cursor = Access.getInstances().open().query(Fields.AccounFields.TB_NAME, null, Fields.AccounFields.MEMBER_ID + " = ?", new String[]{memberid}, null, null, null);
                    IMWindow imWindow = Proxy.getIMWindowManager().getLastWindow();
                    if (cursor.getCount() == 1 && cursor.moveToFirst()) {
                        final AccountBean accountBean = AccountBean.createAccountBeanByCursor(cursor);
                        if (onQureListener != null) {
                            if (imWindow != null) {

                                imWindow.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onQureListener.quer(accountBean);
                                    }
                                });

                            }
                        }
                    } else {
                        onQureListener.nullQuer();
                    }
                    cursor.close();
                    Access.getInstances().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * 查找人员同步
     *
     * @param memberid
     * @return
     */
    public AccountBean queryByID(final String memberid) {

        return (AccountBean) Access.getInstances().open(new Access.OnExecSQL<AccountBean>() {
            @Override
            public AccountBean onExecSQL(SQLiteDatabase sqLiteDatabase) {
                Cursor cursor = sqLiteDatabase.query(Fields.AccounFields.TB_NAME, null, Fields.AccounFields.MEMBER_ID + " = ?", new String[]{memberid}, null, null, null);
                if(cursor.getCount() == 1){
                    cursor.moveToFirst();
                    return AccountBean.createAccountBeanByCursor(cursor);
                }
                return null;
            }
        });
    }

    /**
     * 新增人员信息
     * @param contentValues
     * @return
     */
    public long insertById(final ContentValues contentValues){
        AccountBean bean = queryByID(contentValues.getAsString(Fields.AccounFields.MEMBER_ID));
        if(bean == null) {
            return (Long) Access.getInstances().open(new Access.SimpleOnExecSQL<Long>() {
                @Override
                public Long onExecSQL(SQLiteDatabase sqLiteDatabase) {
                    return sqLiteDatabase.insert(Fields.AccounFields.TB_NAME,null,contentValues);
                }
            }.setUri(InformationProvide.CONTENT_URI));
        }else if(!StringUtil.isNull(contentValues.getAsString(Fields.AccounFields.MEMBER_AVATARURL))){
            contentValues.put(Fields.AccounFields.ID,bean.getId());
            return update(contentValues);
        }else{return -1;}
    }

    /**
     * 更新人员信息
     * @param contentValues
     * @return
     */
    private Integer update(final ContentValues contentValues){

//        return (Integer) Access.getInstances().open(new Access.OnExecSQL<Integer>() {
//            @Override
//            public Integer onExecSQL(SQLiteDatabase sqLiteDatabase) {
//                int id = contentValues.getAsInteger(Fields.AccounFields.ID);
//                contentValues.remove(Fields.AccounFields.MEMBER_ID);
//                contentValues.remove(Fields.AccounFields.ID);
//                final int update = sqLiteDatabase.update(Fields.AccounFields.TB_NAME,contentValues,Fields.AccounFields.ID+" = "+id,null);
//                if(update > 0){
//                    IMSdk.CONTEXT.getContentResolver().notifyChange(InformationProvide.CONTENT_URI,null);
//                }
//                return update;
//            }
//        });
        return (Integer) Access.getInstances().open(new Access.SimpleOnExecSQL<Integer>() {
            @Override
            public Integer onExecSQL(SQLiteDatabase sqLiteDatabase) {
                int id = contentValues.getAsInteger(Fields.AccounFields.ID);
                contentValues.remove(Fields.AccounFields.MEMBER_ID);
                contentValues.remove(Fields.AccounFields.ID);
                return sqLiteDatabase.update(Fields.AccounFields.TB_NAME,contentValues,Fields.AccounFields.ID+" = "+id,null);
            }
        }.setUri(InformationProvide.CONTENT_URI));

    }

    /**
     * 查找最大行数 某一表格
     * @param tbName
     * @param where
     * @return
     */
    public Integer queryMaxCountByTableName(final String tbName, final String where) {

         return (Integer) Access.getInstances().open(new Access.OnExecSQL<Integer>() {
            @Override
            public Integer onExecSQL(SQLiteDatabase sqLiteDatabase) {
                Cursor cursor = null;
                if (!StringUtil.isNull(where)) {
                    cursor = sqLiteDatabase.rawQuery(String.format("SELECT count(*) FROM %s WHERE " + where, tbName), null);
                } else {
                    cursor = sqLiteDatabase.rawQuery(String.format("SELECT count(*) FROM %s ", tbName), null);
                }

                if(cursor.getCount() > 0){

                    cursor.moveToFirst();
                    final int count = cursor.getInt(0);
                    cursor.close();
                    return count;
                }
                return 0;

            }
        });
    }

    /**
     * 清除消息列表未读消息
     * @param memberid
     * @param opposideid
     * @param type
     */
    public void cancelInformation(String memberid, String opposideid, int type){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Fields.InformationFields.UNREAD_COUNT,0);
        if(IMSdk.CONTEXT != null){
            if(IMSdk.CONTEXT.getContentResolver().update(
                    InformationProvide.update_URI,contentValues,String.format("%s = ? AND %s = ? AND %s = ?",
                            Fields.InformationFields.MEMBER_ID,
                            Fields.InformationFields.OPPOSITE_ID,
                            Fields.InformationFields.TYPE),new String[]{memberid,opposideid,type+""}) > 0){
                log("------ 清除未读信息成功！ opposideid = "+opposideid+"------");
            }else{
                log("------ 清除未读信息失败！ opposideid = "+opposideid+"------");
            }
        }
    }

    public void cancelInfomationById(int id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Fields.InformationFields.ID, id);
        if(IMSdk.CONTEXT != null
                && IMSdk.CONTEXT.getContentResolver().update(InformationProvide.ClearUnreadById_URI, contentValues, null, null) > 0) {
            log("------ 清楚未读信息成功！（id = "+id+"） ------");
        }else{
            log("------ 清楚未读信息失败！ （id = "+id+"）------");
        }
    }

    /**
     * 新增 群聊详情
     * @param memberId
     * @param subscriptionId
     * @param subscriptionName
     * @param subscriptionPic
     * @return
     */
    public Long insertSubscription(final String memberId,final String subscriptionId,
                                   final String subscriptionName,final String subscriptionPic){
        return (Long)Access.getInstances().open(new Access.OnExecSQL<Long> () {
            @Override
            public Long onExecSQL(SQLiteDatabase sqLiteDatabase) {
                long result = -1;
                ContentValues informationValues = new ContentValues();
                informationValues.put(Fields.SubscriptionFields.NAME,subscriptionName);
                informationValues.put(Fields.SubscriptionFields.PICURL,subscriptionPic);
                Cursor cursor = sqLiteDatabase.query(Fields.SubscriptionFields.TB_NAME,null,
                        String.format("%s = ? AND %s = ?",Fields.SubscriptionFields.MEMBER_ID,Fields.SubscriptionFields.SUBSCRIPTION_ID),
                        new String[]{memberId,subscriptionId},null,null,null);
                if(cursor != null && cursor.getCount() == 1 && cursor.moveToFirst()){
                    String n = MD5.getMD5(cursor.getString(cursor.getColumnIndex(Fields.SubscriptionFields.NAME))+
                            cursor.getString(cursor.getColumnIndex(Fields.SubscriptionFields.PICURL)));
                    String o = MD5.getMD5(subscriptionName+subscriptionPic);
                    if(o.equals(n)){
                        result = cursor.getInt(0);
                    }else{
                        result = sqLiteDatabase.update(Fields.SubscriptionFields.TB_NAME,informationValues,String.format("%s = ? AND %s = ?",Fields.SubscriptionFields.MEMBER_ID,Fields.SubscriptionFields.SUBSCRIPTION_ID),
                                new String[]{memberId,subscriptionId});
                    }
                }else{
                    informationValues.put(Fields.SubscriptionFields.SUBSCRIPTION_ID,subscriptionId);
                    informationValues.put(Fields.SubscriptionFields.MEMBER_ID,memberId);
                    informationValues.put(Fields.SubscriptionFields.TYPE, InformationBean.ROOM);
                    result = sqLiteDatabase.insert(Fields.SubscriptionFields.TB_NAME,null,informationValues);
                }


                return result;
            }
        });
    }


    /**
     * 获取最大未读行数
     * @return
     */
    public int getMaxUnReadCount(){
        return (Integer) Access.getInstances().open(new Access.OnExecSQL<Integer>() {
            @Override
            public Integer onExecSQL(SQLiteDatabase sqLiteDatabase) {
                int maxUnReadCount = 0;
                String sql = String.format("SELECT sum(un_read) FROM  %s",Fields.InformationFields.TB_NAME);
                Cursor cursor = sqLiteDatabase.rawQuery(sql,null,null);
                if(cursor!=null && cursor.moveToFirst()){
                    maxUnReadCount = cursor.getInt(0);
                }
                return maxUnReadCount;
            }
        });

    }

//    public int updateWaite(){
//
//        Access.getInstances().open(new Access.OnExecSQL() {
//            @Override
//            public Object onExecSQL(SQLiteDatabase sqLiteDatabase) {
//
//                sqLiteDatabase.execSQL("update %s set");
//
//
//                return null;
//            }
//        });
//
//
//    }


    private void log(String str){
        Log.i(TAG,str);
    }



}
