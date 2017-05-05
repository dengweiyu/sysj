package com.ifeimo.im.common.bean.model;

import com.ifeimo.im.framwork.database.Fields;

import java.util.logging.Level;

import y.com.sqlitesdk.framework.annotation.TBColumn;
import y.com.sqlitesdk.framework.annotation.TBForeign;
import y.com.sqlitesdk.framework.annotation.TBPrimarykey;

/**
 * Created by lpds on 2017/4/20.
 */
public class InformationModel extends Model<InformationModel>{

    public static final String TB_NAME = Fields.InformationFields.TB_NAME;


    public static final int Default = 0x110;
    public static final int ROOM = 0x111; //群聊
    public static final int ROOM1 = 0x112; //群聊临时，
    public static final int CHAT = 0x113; //单聊 receiverid
    public static final int Advertisement = 0x114; //广告
    public static final int SystemPut = 0x115; //系统推送

    @TBPrimarykey
    private long id;

    @TBColumn
    private String memberId;

    /**
     * 对方id，或者订阅信息的id
     */
    @TBColumn
    private String opposite_id;

    @TBColumn
    private String msgId;

    @TBColumn
    private String last_content;

    @TBColumn
    private String last_create_time;

    @TBColumn
    private int send_type;

    @TBColumn
    private int type;

    @TBColumn
    private String name;

    @TBColumn
    private int un_read;

    @TBColumn
    private boolean banned_post;

    @TBColumn
    private int level;

    @TBColumn
    private boolean is_mesend;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private String pic_url;

    private String title;

    public boolean isBanned_post() {
        return banned_post;
    }

    public void setBanned_post(boolean banned_post) {
        this.banned_post = banned_post;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return pic_url;
    }

    public void setPicRrl(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getOppositeId() {
        return opposite_id;
    }

    public void setOppositeId(String opposite_id) {
        this.opposite_id = opposite_id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getLastContent() {
        return last_content;
    }

    public void setLastContent(String last_content) {
        this.last_content = last_content;
    }

    public String getLastCreateTime() {
        return last_create_time;
    }

    public void setLastCreateTime(String last_create_time) {
        this.last_create_time = last_create_time;
    }

    public int getSendType() {
        return send_type;
    }

    public void setSendType(int send_type) {
        this.send_type = send_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnread() {
        return un_read;
    }

    public void setUnread(int un_read) {
        this.un_read = un_read;
    }

    public boolean is_mesend() {
        return is_mesend;
    }

    public void setIs_mesend(boolean is_mesend) {
        this.is_mesend = is_mesend;
    }

    @Override
    public String getTableName() {
        return TB_NAME;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getCreateTime() {
        return null;
    }

    @Override
    public String toString() {
        return "InformationModel{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", opposite_id='" + opposite_id + '\'' +
                ", msgId='" + msgId + '\'' +
                ", last_content='" + last_content + '\'' +
                ", last_create_time='" + last_create_time + '\'' +
                ", send_type=" + send_type +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", un_read=" + un_read +
                ", is_mesend=" + is_mesend +
                ", pic_url='" + pic_url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
