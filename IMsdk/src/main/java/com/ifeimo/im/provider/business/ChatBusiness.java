package com.ifeimo.im.provider.business;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ifeimo.im.common.bean.model.ChatMsgModel;
import com.ifeimo.im.common.bean.model.InformationModel;
import com.ifeimo.im.common.bean.model.Model;
import com.ifeimo.im.common.util.StringUtil;
import com.ifeimo.im.framwork.IMSdk;
import com.ifeimo.im.framwork.Proxy;
import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.provider.ChatProvider;
import com.ifeimo.im.provider.InformationProvide;

import y.com.sqlitesdk.framework.business.Business;
import y.com.sqlitesdk.framework.db.Access;
import y.com.sqlitesdk.framework.sqliteinterface.Execute;

/**
 * Created by lpds on 2017/4/24.
 */
public class ChatBusiness extends InformationBusiness implements IMsgBusiness<ChatMsgModel> {

    private static final String TAG = "XMPP_ChatBusiness";

    static ChatBusiness chatBusiness;

    static {
        chatBusiness = new ChatBusiness();
    }

    private ChatBusiness() {

    }


    public static final IMsgBusiness getInstances() {
        return chatBusiness;
    }

    public void insert(Model<ChatMsgModel> model) {
        final ChatMsgModel inserChatMsgModel =  model.clone();
        Access.runCustomThread(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {

                /**
                 * 单聊处理
                 */
                {
                    if (!StringUtil.isNull(inserChatMsgModel.getMsgId())) {
                        ChatMsgModel chatMsgModel = Business.getInstances().queryLineByWhere(
                                sqLiteDatabase,
                                ChatMsgModel.class,
                                String.format("((%s = ? AND %s = ?) or (%s = ? AND %s = ?)) AND %s = ?",
                                        Fields.ChatFields.MEMBER_ID, Fields.ChatFields.RECEIVER_ID,
                                        Fields.ChatFields.MEMBER_ID, Fields.ChatFields.RECEIVER_ID, Fields.MsgFields.MSG_ID
                                ),
                                new String[]{
                                        inserChatMsgModel.getMemberId(), inserChatMsgModel.getReceiverId(),
                                        inserChatMsgModel.getReceiverId(), inserChatMsgModel.getMemberId(), inserChatMsgModel.getMsgId()
                                });
                        if (chatMsgModel == null) {
                            if (Business.getInstances().insert(sqLiteDatabase, inserChatMsgModel) > 0) {
                                Log.i(TAG, "onExecute: 插入单聊成功 " + inserChatMsgModel);
                            }
                        } else {
                            if (chatMsgModel.getSendType() == Fields.MsgFields.SEND_FINISH) {
                                inserChatMsgModel.setSendType(Fields.MsgFields.SEND_FINISH);
                            }
                            inserChatMsgModel.setId(chatMsgModel.getId());
                            if (Business.getInstances().modify(sqLiteDatabase, inserChatMsgModel) > 0) {
                                Log.i(TAG, "onExecute: 插入单聊成功 " + inserChatMsgModel);
                            }
                        }
                    } else {
                        if (Business.getInstances().insert(sqLiteDatabase, inserChatMsgModel) > 0) {
                            Log.i(TAG, "onExecute: 插入单聊成功 " + inserChatMsgModel);
                        }
                    }
                }

                /**
                 * 更新information
                 */
                {
                    if (inserChatMsgModel.getReceiverId().equals(Proxy.getAccountManger().getUserMemberId())) {
                        insertInformation(sqLiteDatabase,
                                inserChatMsgModel.getMemberId(),
                                inserChatMsgModel.getMsgId(),
                                inserChatMsgModel.getContent(),
                                inserChatMsgModel.getCreateTime(),
                                inserChatMsgModel.getSendType(),
                                InformationModel.CHAT,
                                null, false);
                    } else {
                        insertInformation(sqLiteDatabase,
                                inserChatMsgModel.getReceiverId(),
                                inserChatMsgModel.getMsgId(),
                                inserChatMsgModel.getContent(),
                                inserChatMsgModel.getCreateTime(),
                                inserChatMsgModel.getSendType(),
                                InformationModel.CHAT,
                                null, true);
                    }
                }

                IMSdk.CONTEXT.getContentResolver().notifyChange(ChatProvider.CONTENT_URI, null);
                IMSdk.CONTEXT.getContentResolver().notifyChange(InformationProvide.CONTENT_URI, null);
            }

            @Override
            public void onExternalError() {

            }
        });
    }

    @Override
    public void deleteInformation(final InformationModel informationModel) {
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {
                if (informationModel.getType() != InformationModel.CHAT) {
                    return;
                }
                Business.getInstances().deleteById(sqLiteDatabase, informationModel);
                IMSdk.CONTEXT.getContentResolver().notifyChange(InformationProvide.CONTENT_URI, null);

            }

            @Override
            public void onExternalError() {

            }
        });
    }

    @Override
    public void modifyErrorSituation() {
        Access.run(new Execute() {
            @Override
            public void onExecute(SQLiteDatabase sqLiteDatabase) throws Exception {

                ChatMsgModel chatMsgModel = new ChatMsgModel();
                chatMsgModel.setSendType(Fields.MsgFields.SEND_UNCONNECT);
                if (Business.getInstances().modify(
                        sqLiteDatabase,
                        chatMsgModel,
                        String.format("%s != ?", Fields.GroupChatFields.SEND_TYPE),
                        new String[]{String.valueOf(Fields.GroupChatFields.SEND_FINISH)}) > 0) {
                    Log.i(TAG, "onExecute: 所有异常菊花发送部分修改");
                    IMSdk.CONTEXT.getContentResolver().notifyChange(ChatProvider.CONTENT_URI, null);
                }

            }

            @Override
            public void onExternalError() {


            }
        });
    }
}
