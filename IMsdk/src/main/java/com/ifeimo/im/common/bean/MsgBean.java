package com.ifeimo.im.common.bean;

import android.database.Cursor;

import com.ifeimo.im.framwork.database.Fields;
import java.io.Serializable;

/**
 * Created by admin on 2016/12/19.
 *
 * 一条基本聊天信息
 *
 */

public class MsgBean implements Serializable,Cloneable{

    protected int id;
    //发送者
    protected String memberId;
    @Deprecated
    protected String memberNickName;
    @Deprecated
    protected String memberAvatarUrl;
    //接收者
    protected String receiverId;
    @Deprecated
    protected String receiverNickName;
    @Deprecated
    protected String receiverAvatarUrl;

    protected String content;
    protected String createTime;
    protected int sendType;
    protected String msgId;

    protected Runnable runnable;

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public MsgBean(int id, String msgId, String memberId, String memberNickName, String memberAvatarUrl, String receiverId,
                   String receiverNickName, String receiverAvatarUrl, String content, String createTime, int sendType) {
        this.id = id;
        this.msgId = msgId;
        this.memberId = memberId;
        this.memberNickName = memberNickName;
        this.memberAvatarUrl = memberAvatarUrl;
        this.receiverId = receiverId;
        this.receiverNickName = receiverNickName;
        this.receiverAvatarUrl = receiverAvatarUrl;
        this.content = content;
        this.createTime = createTime;
        this.sendType = sendType;
    }

    public MsgBean() {
    }

    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }

    public String getMemberAvatarUrl() {
        return memberAvatarUrl;
    }

    public void setMemberAvatarUrl(String memberAvatarUrl) {
        this.memberAvatarUrl = memberAvatarUrl;
    }

//    public String getReceiverNickName() {
//        return receiverNickName;
//    }
//
//    public void setReceiverNickName(String receiverNickName) {
//        this.receiverNickName = receiverNickName;
//    }
//
//    public String getReceiverAvatarUrl() {
//        return receiverAvatarUrl;
//    }
//
//    public void setReceiverAvatarUrl(String receiverAvatarUrl) {
//        this.receiverAvatarUrl = receiverAvatarUrl;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getReceiverId(){
        return receiverId;
    }

    public void setReceiverId(String receiverId){
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }


    public static MsgBean createByCursor(Cursor cursor){
        MsgBean msgBean = new MsgBean();
        msgBean.setId(cursor.getInt(cursor.getColumnIndex(Fields.ChatFields.ID)));
        msgBean.setReceiverId(cursor.getString(cursor.getColumnIndex(Fields.ChatFields.RECEIVER_ID)));
        msgBean.setSendType(cursor.getInt(cursor.getColumnIndex(Fields.ChatFields.SEND_TYPE)));
        msgBean.setMemberId(cursor.getString(cursor.getColumnIndex(Fields.ChatFields.MEMBER_ID)));
        msgBean.setContent(cursor.getString(cursor.getColumnIndex(Fields.ChatFields.CONTENT)));
        msgBean.setCreateTime(cursor.getString(cursor.getColumnIndex(Fields.ChatFields.CREATE_TIME)));
        msgBean.setMsgId(cursor.getString(cursor.getColumnIndex(Fields.ChatFields.MSG_ID)));
        return msgBean;

    }

    public static MsgBean buildChatBean(org.jivesoftware.smack.packet.Message message) {

        if (message.getBody() != null && !message.getBody().equals("")) {
            MsgBean msgBean = new MsgBean();
            String content = message.getBody();
            String memberID = message.getFrom().split("@")[0];//发送者
            String receiverId = message.getTo().split("@")[0];//接收者

            msgBean.setMemberId(memberID);
            msgBean.setReceiverId(receiverId);
            msgBean.setContent(content);
            msgBean.setSendType(Fields.ChatFields.SEND_FINISH);
            msgBean.setCreateTime(System.currentTimeMillis()+"");
            msgBean.setMsgId(message.getPacketID());
            return msgBean;
        } else {
            return null;
        }
    }


    @Override
    public String toString() {
        return "MsgBean{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", memberNickName='" + memberNickName + '\'' +
                ", memberAvatarUrl='" + memberAvatarUrl + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", receiverNickName='" + receiverNickName + '\'' +
                ", receiverAvatarUrl='" + receiverAvatarUrl + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", sendType=" + sendType +
                ", msgId='" + msgId + '\'' +
                '}';
    }


    public MsgBean clonThis() throws CloneNotSupportedException {
        return (MsgBean) super.clone();
    }




}
