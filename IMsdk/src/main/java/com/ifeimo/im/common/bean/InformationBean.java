package com.ifeimo.im.common.bean;

import android.content.ContentValues;
import android.database.Cursor;

import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.provider.InformationProvide;

import java.io.Serializable;

/**
 * Created by lpds on 2017/1/24.
 * 消息列表实体类
 */
public class InformationBean implements Serializable,Cloneable{


    private int id;
    private String title;
    private String memberid;
    /**
     * 群聊时，cacheid 为 roomid
     * 单聊时，cacheid 为 memberid + receiverid;
     * 广告时，cacheid 为 广告id
     * 系统推送时 cacheid 为系统推送id
     */
    private String oppositeId;//来者id
    private int unread;
    private String lastContent;
    private String lastCreateTime;
    private String picUrl;
    private int type;
    private int isMeSend;
    private int sendType;
    private String msgId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public int isMeSend() {
        return isMeSend;
    }

    public void setMeSend(int meSend) {
        isMeSend = meSend;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getOppositeId() {
        return oppositeId;
    }

    public void setOppositeId(String oppositeId) {
        this.oppositeId = oppositeId;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public static final int Default = 0x110;
    public static final int ROOM = 0x111; //群聊
    public static final int ROOM1 = 0x112; //群聊临时，
    public static final int CHAT = 0x113; //单聊 receiverid
    public static final int Advertisement = 0x114; //广告
    public static final int SystemPut = 0x115; //系统推送



    public InformationBean() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getLastContent() {
        return lastContent;
    }

    public void setLastContent(String lastContent) {
        this.lastContent = lastContent;
    }

    public String getLastCreateTime() {
        return lastCreateTime;
    }

    public void setLastCreateTime(String lastCreateTime) {
        this.lastCreateTime = lastCreateTime;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public static InformationBean buildInfomationMsgItemByCursor(Cursor cursor) {
        if(cursor == null){
            return null;
        }
        InformationBean infomationBean = new InformationBean();
        infomationBean.id = cursor.getInt(cursor.getColumnIndex(Fields.InformationFields.ID));
        infomationBean.memberid = cursor.getString(cursor.getColumnIndex(Fields.InformationFields.MEMBER_ID));
        infomationBean.oppositeId = cursor.getString(cursor.getColumnIndex(Fields.InformationFields.OPPOSITE_ID));
        infomationBean.title = cursor.getString(cursor.getColumnIndex(Fields.InformationFields.TITLE));
        infomationBean.lastContent = cursor.getString(cursor.getColumnIndex(Fields.InformationFields.LAST_CONTENT));
        infomationBean.lastCreateTime = cursor.getString(cursor.getColumnIndex(Fields.InformationFields.LAST_CREATETIME));
        infomationBean.picUrl = cursor.getString(cursor.getColumnIndex(Fields.InformationFields.PICURL));
        infomationBean.type = cursor.getInt(cursor.getColumnIndex(Fields.InformationFields.TYPE));
        infomationBean.isMeSend = cursor.getInt(cursor.getColumnIndex(Fields.InformationFields.IS_ME_SEND));
        infomationBean.unread = cursor.getInt(cursor.getColumnIndex(Fields.InformationFields.UNREAD_COUNT));
        infomationBean.msgId = cursor.getString(cursor.getColumnIndex(Fields.InformationFields.MSG_ID));
        infomationBean.sendType = cursor.getInt(cursor.getColumnIndex(Fields.InformationFields.SEND_TYPE));
        return infomationBean;
    }

    public static ContentValues buildContentValuesByItem(InformationBean infomationBean){
        if(infomationBean == null){
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(Fields.InformationFields.ID,infomationBean.getId());
        contentValues.put(Fields.InformationFields.MEMBER_ID,infomationBean.getMemberid());
        contentValues.put(Fields.InformationFields.OPPOSITE_ID, infomationBean.getOppositeId());
        contentValues.put(Fields.InformationFields.TITLE, infomationBean.getTitle());
        contentValues.put(Fields.InformationFields.LAST_CONTENT, infomationBean.getLastContent());
        contentValues.put(Fields.InformationFields.LAST_CREATETIME, infomationBean.getLastCreateTime());
        contentValues.put(Fields.InformationFields.PICURL, infomationBean.getPicUrl());
        contentValues.put(Fields.InformationFields.TYPE, infomationBean.getType());
        contentValues.put(Fields.InformationFields.IS_ME_SEND,infomationBean.isMeSend());
        contentValues.put(Fields.InformationFields.UNREAD_COUNT,infomationBean.getUnread());
        return contentValues;
    }

    public static InformationBean clon(InformationBean informationBean){
        try {
            return (InformationBean)informationBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "InformationBean{ hashcode = "+this.hashCode()+" ," +
                "id=" + id +
                ", title='" + title + '\'' +
                ", memberid='" + memberid + '\'' +
                ", oppositeId='" + oppositeId + '\'' +
                ", unread=" + unread +
                ", lastContent='" + lastContent + '\'' +
                ", lastCreateTime='" + lastCreateTime + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", type=" + type +
                ", isMeSend=" + isMeSend +
                ", sendType=" + sendType +
                ", msgId='" + msgId + '\'' +
                '}';
    }
}
