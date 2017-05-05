package com.ifeimo.im.provider.business;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.bean.chat.GroupChatBean;
import com.ifeimo.im.common.bean.model.GroupChatModel;
import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.common.bean.model.SubscriptionModel;
import com.ifeimo.im.common.util.ConnectUtil;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.provider.GroupChatProvider;
import com.ifeimo.im.provider.InformationProvide;

import org.jivesoftware.smack.SmackException;

import java.util.List;

import y.com.sqlitesdk.framework.business.Business;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/3/1.
 */
public final class GroupChatBusiness extends SubscriptionBusiness implements IMsgBusiness{
    //    private BaseProvider provider;
    private static GroupChatBusiness muccBusiness;
    private final String TAG = "XMPP_MuccBusiness";


    static {
        muccBusiness = new GroupChatBusiness();
    }

    private GroupChatBusiness() {

    }

    public static GroupChatBusiness getInstances() {
        return muccBusiness;
    }

    /**
     * 接收
     *
     * @param modiModel
     */
    public void insert(final GroupChatModel modiModel) {

        Access.runCustomThread(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {

                /**
                 * 基础判断
                 */
                {
                    if (StringUtil.isNull(modiModel.getMemberId())) {
                        return;
                    }

                }

                /**
                 * 用户消息处理
                 */
                {
                    insertAccount(sqLiteDatabase, modiModel.getMemberId(), modiModel.getMemberNickName(), modiModel.getMemberAvatarUrl());
                }


                /**
                 * 群聊消息处理
                 */
                {
                    if (!StringUtil.isNull(modiModel.getMsgId())) {
                        GroupChatModel qureModel = Business.getInstances().queryLineByWhere(sqLiteDatabase,
                                GroupChatModel.class,
                                String.format("%s = ? and %s = ? and %s = ?",
                                        Fields.GroupChatFields.ROOM_ID, Fields.GroupChatFields.MSG_ID, Fields.GroupChatFields.MEMBER_ID),
                                new String[]{modiModel.getRoomid(), modiModel.getMsgId(), modiModel.getMemberId()});
                        if (qureModel != null) {
                            modiModel.setId(qureModel.getId());
                            if (qureModel.getSendType() == Fields.MsgFields.SEND_FINISH) {
                                modiModel.setSendType(Fields.MsgFields.SEND_FINISH);
                                if (modiModel.getContent().equals(qureModel.getContent())
                                        && modiModel.getCreateTime().equals(qureModel.getCreateTime())) {
                                    return;
                                }
                            }
//                            modiModel.setCreateTime(qureModel.getCreateTime());
                            if (Business.getInstances().modify(sqLiteDatabase, modiModel) > 0) {
                                Log.i(TAG, "onExecute: *********** 修改群聊成功 Success Group Modi ********** " + modiModel.toString());
                            }
                        } else if (qureModel == null) {
                            if (Business.getInstances().insert(sqLiteDatabase, modiModel) > 0) {
                                Log.i(TAG, "onExecute: *********** 插入群聊成功 Success Group Insert ********** " + modiModel.toString());
                            }
                        }
                    } else {
                        //没有msgid是 iphone手机发送的情况
                        GroupChatModel groupChatModel  = Business.getInstances().queryLineByWhere(
                                sqLiteDatabase,GroupChatModel.class,
                                String.format("%s = ? AND %s = ? AND %s = ?",
                                        Fields.AccounFields.MEMBER_ID,
                                        Fields.GroupChatFields.ROOM_ID,
                                        Fields.GroupChatFields.CREATE_TIME),
                                new String[]{
                                        modiModel.getMemberId(),
                                        modiModel.getRoomid(),
                                        modiModel.getCreateTime()});
                        if(groupChatModel != null){
                            if(!modiModel.getContent().equals(groupChatModel.getContent())){
                                modiModel.setId(groupChatModel.getId());
                                Business.getInstances().modify(sqLiteDatabase, modiModel);
                            }
                        }else if(Business.getInstances().insert(sqLiteDatabase, modiModel) > 0) {
                            Log.i(TAG, "onExecute: *********** 插入群聊成功 Success Group Insert ********** " + modiModel.toString());
                        }
                    }
                }

                /**
                 * 更新information
                 */
                {
                    boolean flag = false;
                    if(UserBean.getMemberID().equals(modiModel.getMemberId())) {
                        flag = true;
                    }
                    insertInformation(sqLiteDatabase,
                            modiModel.getRoomid(),
                            modiModel.getMsgId(),
                            modiModel.getContent(),
                            modiModel.getCreateTime(),
                            modiModel.getSendType(),
                            InformationModel.ROOM,
                            null,flag);
                }

                IMSdk.CONTEXT.getContentResolver().notifyChange(GroupChatProvider.CONTENT_URI, null);
                IMSdk.CONTEXT.getContentResolver().notifyChange(InformationProvide.CONTENT_URI, null);
            }

            @Override
            public void onExternalError() {

            }
        });


    }

    /**
     * 删除消息列表
     * @param informationModel
     */
    public void deleteInformationByGroupChat(final InformationModel informationModel)  {
        final GroupChatBean groupChatBean = Proxy.getMessageManager().getGroupChatBean(informationModel.getOppositeId());
        if(groupChatBean.getMultiUserChat().isJoined()){
            try {
                groupChatBean.getMultiUserChat().leave();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                if(informationModel.getType() != InformationModel.ROOM){
                    return;
                }
                GroupChatBean groupChatBean = Proxy.getMessageManager().getGroupChatBean(informationModel.getOppositeId());
                Proxy.getMessageManager().removeGroupChat(informationModel.getOppositeId());
                if(groupChatBean != null){
                    if(ConnectUtil.isConnect(IMSdk.CONTEXT) && Proxy.getConnectManager().isConnect()){
                        if(groupChatBean.getMultiUserChat() != null){
                            groupChatBean.getMultiUserChat().leave();
                        }
                    }

                }
                Business.getInstances().deleteById(sqLiteDatabase,informationModel);

                SubscriptionModel subscriptionModel = Business.getInstances().queryLineByWhere(sqLiteDatabase, SubscriptionModel.class,
                        String.format("%s = ?",Fields.SubscriptionFields.SUBSCRIPTION_ID),
                        new String[]{informationModel.getOppositeId()});

                if(subscriptionModel!=null){
                    cancelAccount2Subscription(sqLiteDatabase,subscriptionModel.getId());
                }

                IMSdk.CONTEXT.getContentResolver().notifyChange(InformationProvide.CONTENT_URI,null);
            }

            @Override
            public void onExternalError() {

            }
        });
    }


    public List<InformationModel> getAllSubscriptionByType(SQLiteDatabase sqLiteDatabase,int type) throws IllegalAccessException, NoSuchFieldException, InstantiationException {

        if(StringUtil.isNull(UserBean.getMemberID())){
            return null;
        }

        return Business.getInstances().queryAll(sqLiteDatabase,InformationModel.class,
                String.format("%s = ? AND %s = ?",
                        Fields.AccounFields.MEMBER_ID,
                        Fields.InformationFields.TYPE),new String[]{UserBean.getMemberID(),String.valueOf(type)});

    }


    @Override
    public void modifyErrorSituation() {
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {

                GroupChatModel groupChatModel = new GroupChatModel();
                groupChatModel.setSendType(Fields.MsgFields.SEND_UNCONNECT);
                if(Business.getInstances().modify(
                        sqLiteDatabase,
                        groupChatModel,
                        String.format("%s != ?",Fields.GroupChatFields.SEND_TYPE),
                        new String[]{Fields.GroupChatFields.SEND_FINISH + ""}) > 0){
                    Log.i(TAG, "onExecute: 所有异常菊花发送部分修改");
                    IMSdk.CONTEXT.getContentResolver().notifyChange(GroupChatProvider.CONTENT_URI,null);
                }


            }

            @Override
            public void onExternalError() {



            }
        });

    }
}
