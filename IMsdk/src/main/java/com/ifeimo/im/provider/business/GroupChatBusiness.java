package com.ifeimo.im.provider.business;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ifeimo.im.common.bean.UserBean;
import com.ifeimo.im.common.bean.chat.GroupChatBean;
import com.ifeimo.im.common.bean.model.GroupChatModel;
import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.common.bean.model.Model;
import com.ifeimo.im.common.bean.model.SubscriptionModel;
import com.ifeimo.im.common.util.ConnectUtil;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.provider.GroupChatProvider;
import com.ifeimo.im.provider.InformationProvide;

import java.util.List;

import y.com.sqlitesdk.framework.business.Business;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/3/1.
 */
public final class GroupChatBusiness extends SubscriptionBusiness implements IMsgBusiness<GroupChatModel>,IInfortmationBusiness {
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
     * @param model
     */
    public void insert(Model<GroupChatModel> model) {
        final GroupChatModel groupModel = model.clone();
        Access.runCustomThread(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {

                /**
                 * 基础判断
                 */
                {
                    if (StringUtil.isNull(groupModel.getMemberId())) {
                        return;
                    }

                }

                /**
                 * 用户消息处理
                 */
                {
                    if(!groupModel.getMemberId().equals(UserBean.getMemberID())) {
                        insertAccount(sqLiteDatabase, groupModel.getMemberId(), groupModel.getMemberNickName(), groupModel.getMemberAvatarUrl());
                    }
                }


                /**
                 * 群聊消息处理
                 */
                {
                    //此消息是 android 端发出
                    if (!StringUtil.isNull(groupModel.getMsgId())) {
                        GroupChatModel qureModel = Business.getInstances().queryLineByWhere(sqLiteDatabase,
                                GroupChatModel.class,
                                String.format("%s = ? and %s = ? and %s = ?",
                                        Fields.GroupChatFields.ROOM_ID, Fields.GroupChatFields.MSG_ID, Fields.GroupChatFields.MEMBER_ID),
                                new String[]{groupModel.getRoomid(), groupModel.getMsgId(), groupModel.getMemberId()});
                        if (qureModel != null) {
                            groupModel.setId(qureModel.getId());
                            if (qureModel.getSendType() == Fields.MsgFields.SEND_FINISH) {
                                groupModel.setSendType(Fields.MsgFields.SEND_FINISH);
                                if (groupModel.getMsgId().equals(qureModel.getMsgId())
                                        && groupModel.getContent().equals(qureModel.getContent())) {
                                    return;
                                }
                            }
                            if(groupModel.getSendType() != Fields.MsgFields.SEND_WAITING){
                                groupModel.setCreateTime(qureModel.getCreateTime());
                            }
                            if (Business.getInstances().modify(sqLiteDatabase, groupModel) > 0) {
                                Log.i(TAG, "onExecute: *********** 修改群聊成功 Success Group Modi ********** " + groupModel.toString());
                            }
                        } else if (qureModel == null) {
                            if (Business.getInstances().insert(sqLiteDatabase, groupModel) > 0) {
                                Log.i(TAG, "onExecute: *********** 插入群聊成功 Success Group Insert ********** " + groupModel.toString());
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
                                        groupModel.getMemberId(),
                                        groupModel.getRoomid(),
                                        groupModel.getCreateTime()});
                        if(groupChatModel != null){
                            if(!groupModel.getContent().equals(groupChatModel.getContent())){
                                groupModel.setId(groupChatModel.getId());
                                Business.getInstances().modify(sqLiteDatabase, groupModel);
                            }
                        }else if(Business.getInstances().insert(sqLiteDatabase, groupModel) > 0) {
                            Log.i(TAG, "onExecute: *********** 插入群聊成功 Success Group Insert ********** " + groupModel.toString());
                        }
                    }
                }

                /**
                 * 更新information
                 */
                {
                    boolean flag = false;
                    if(UserBean.getMemberID().equals(groupModel.getMemberId())) {
                        flag = true;
                    }
                    insertInformation(sqLiteDatabase,
                            groupModel.getRoomid(),
                            groupModel.getMsgId(),
                            groupModel.getContent(),
                            groupModel.getCreateTime(),
                            groupModel.getSendType(),
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
    @Override
    public void deleteInformation(final InformationModel informationModel)  {
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
                        if(groupChatBean.getMultiUserChat() != null && groupChatBean.getMultiUserChat().isJoined()){
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

    @Override
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
