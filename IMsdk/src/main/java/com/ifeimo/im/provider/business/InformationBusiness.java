package com.ifeimo.im.provider.business;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.framwork.commander.IMWindow;
import com.ifeimo.im.provider.InformationProvide;

import y.com.sqlitesdk.framework.business.Business;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/4/24.
 */
public abstract class InformationBusiness extends BaseSupport {

    private final String TAG = "XMPP_Information";

    /**
     * 新增 浏览消息
     *
     * @param sqLiteDatabase
     * @param opposite_id
     * @param msgId
     * @param last_content
     * @param last_create_time
     * @param send_type
     * @param type
     * @param name
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws InstantiationException
     */
    protected void insertInformation(SQLiteDatabase sqLiteDatabase,
                                     String opposite_id,
                                     String msgId,
                                     String last_content,
                                     String last_create_time,
                                     int send_type,
                                     int type,
                                     String name, boolean isme) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        InformationModel informationModel = new InformationModel();
        informationModel.setMemberId(Proxy.getAccountManger().getUserMemberId());
        informationModel.setOppositeId(opposite_id);
        informationModel.setMsgId(msgId);
        informationModel.setLastContent(last_content);
        informationModel.setLastCreateTime(last_create_time);
        informationModel.setSendType(send_type);
        informationModel.setType(type);
        informationModel.setName(name);
        informationModel.setIs_mesend(isme);
        insertInformation(sqLiteDatabase, informationModel);
    }

    protected void insertInformation(SQLiteDatabase sqLiteDatabase, InformationModel informationModel) throws IllegalAccessException, NoSuchFieldException, InstantiationException {

        /**
         * 是否在这个memberid，这个地想id，这个类型上存在此行
         */
        InformationModel quremodel = Business.getInstances().queryLineByWhere(
                sqLiteDatabase,
                InformationModel.class,
                String.format("%s = ? And %s = ? And %s = ?",
                        Fields.InformationFields.MEMBER_ID,
                        Fields.InformationFields.OPPOSITE_ID,
                        Fields.InformationFields.TYPE),
                new String[]{
                        informationModel.getMemberId(),
                        informationModel.getOppositeId(),
                        informationModel.getType() + ""});
        boolean flag = false;
        synchronized (Proxy.getIMWindowManager()) {
            for (IMWindow i : Proxy.getIMWindowManager().getAllIMWindows()) {
                switch (i.getType()) {
                    case IMWindow.MUCCHAT_TYPE:
                        if (i.getKey().equals(informationModel.getOppositeId())) {
                            flag = true;
                            break;
                        }
                        break;
                    case IMWindow.CHAT_TYPE:
                        if (i.getReceiver().equals(informationModel.getOppositeId())) {
                            flag = true;
                            break;
                        }
                }
            }
        }

        if (!informationModel.is_mesend() && !flag) {
            if(quremodel != null) {
                informationModel.setUnread(quremodel.getUnread() + 1);
            }else{
                informationModel.setUnread(1);
            }
        } else if(informationModel.is_mesend()){
            informationModel.setUnread(0);
        }

        if (quremodel == null) {
            if (Business.getInstances().insert(sqLiteDatabase, informationModel) > 0) {
                Log.i(TAG, "insertInformation: ********** 修改消息列表成功 Success Information Insert ******* " + informationModel);
            }
        } else {
            informationModel.setId(quremodel.getId());
            if(informationModel.getLastCreateTime().compareTo(quremodel.getLastCreateTime()) >= 0) {
                if (Business.getInstances().modify(sqLiteDatabase, informationModel) > 0) {
                    Log.i(TAG, "insertInformation: ********** 修改消息列表成功 Success Information Modify ******* " + informationModel);
                }
            }
        }
    }

    /**
     * 清空红点
     * @param id
     */
    public void clearUnRead(final long id) {

        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {

                InformationModel informationModel = Business.getInstances().queryLineByWhere(sqLiteDatabase, InformationModel.class,
                        String.format("%s = ?", Fields.AccounFields.ID), new String[]{id + ""});
                if (informationModel != null) {
                    informationModel.setUnread(0);
                    Business.getInstances().modify(sqLiteDatabase, informationModel);
                }
                IMSdk.CONTEXT.getContentResolver().notifyChange(InformationProvide.CONTENT_URI, null);
            }

            @Override
            public void onExternalError() {

            }
        });

    }

    /**
     * 获取某列值总和
     * @return
     */
    public int getMaxUnRead(){
        final int[] maxUnReadCount = {0};
        Access.runCustomThread(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                maxUnReadCount[0] = Business.getInstances().
                        getMaxByField(
                                sqLiteDatabase,
                                Fields.InformationFields.UNREAD_COUNT,
                                Fields.InformationFields.TB_NAME,
                                String.format("%s = ?",Fields.InformationFields.MEMBER_ID),
                                new String[]{Proxy.getAccountManger().getUserMemberId()});
            }

            @Override
            public void onExternalError() {

            }
        });
        return maxUnReadCount[0];
    }


}
