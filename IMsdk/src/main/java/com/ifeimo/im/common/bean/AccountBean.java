package com.ifeimo.im.common.bean;

import android.content.Intent;
import android.database.Cursor;

import com.ifeimo.im.framwork.database.Fields;
import com.ifeimo.im.provider.BaseProvider;

import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.packet.RosterPacket;

import java.io.Serializable;
import java.lang.reflect.Member;

/**
 * Created by lpds on 2017/1/17.
 * <p/>
 * 联系人
 */
public class AccountBean extends MemberBean{

    String remarkName;
    String title;
    String content;
    String time;
    String status;
    RosterPacket.ItemType type;
    Intent intent;


    public AccountBean(int id, String memeberid, String nickName, String remarkName, String avatarUrl, String title, String content, String time, Intent intent) {
        this.id = id;
        this.memeberid = memeberid;
        this.nickName = nickName;
        this.remarkName = remarkName;
        this.avatarUrl = avatarUrl;
        this.title = title;
        this.content = content;
        this.time = time;
        this.intent = intent;
    }


    public AccountBean() {

    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public RosterPacket.ItemType getType() {
        return type;
    }

    public void setType(RosterPacket.ItemType type) {
        this.type = type;
    }

    /**
     * name nickName
     * roster
     * status 在线状态
     * user memberid
     * weakConnect
     */
    public static AccountBean buildeAccountBeanByRoster(RosterEntry rosterEntry) {
        AccountBean accountBean = new AccountBean();
        accountBean.setNickName(rosterEntry.getName());
        accountBean.setType(rosterEntry.getType());


        return accountBean;
    }

    public static AccountBean createAccountBeanByCursor(Cursor cursor) {
        if(cursor == null){
            return null;
        }
        AccountBean accountBean = new AccountBean();
        accountBean.id = cursor.getInt(cursor.getColumnIndex(Fields.AccounFields.ID));
        accountBean.avatarUrl = cursor.getString(cursor.getColumnIndex(Fields.AccounFields.MEMBER_AVATARURL));
        accountBean.memeberid = cursor.getString(cursor.getColumnIndex(Fields.AccounFields.MEMBER_ID));
        accountBean.nickName = cursor.getString(cursor.getColumnIndex(Fields.AccounFields.MEMBER_NICKNAME));
        return accountBean;
    }

}
