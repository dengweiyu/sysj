package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * Created by cx on 2018/1/29.
 */

public class MessageReplyComment extends BaseEntity{
    /**
     "id": "5464",
     "content": "QWERTY",
     "group_id": "1876",
     "member_id": "4741865",
     "reply_user": "1473811",
     "parent_id": "5173",
     "add_time": 1517215476,
     "replyMemberName": "yangruiqi1",
     "replyMemberIcon": "http://avatar.17sysj.com/default3.JPG?imageView2/2/h/150/format/jpg",
     "replyedUserName": "玩家1473811s",
     "replyedUserIcon": "http://avatar.17sysj.com/default13.JPG?imageView2/2/h/150/format/jpg"
     */
    private String id;
    private String content;
    private String group_id;
    private String member_id;
    private String reply_user;
    private String parent_id;
    private String add_time;
    private String replyMemberName;
    private String replyMemberIcon;
    private String replyedUserName;
    private String replyedUserIcon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getReply_user() {
        return reply_user;
    }

    public void setReply_user(String reply_user) {
        this.reply_user = reply_user;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getReplyMemberName() {
        return replyMemberName;
    }

    public void setReplyMemberName(String replyMemberName) {
        this.replyMemberName = replyMemberName;
    }

    public String getReplyMemberIcon() {
        return replyMemberIcon;
    }

    public void setReplyMemberIcon(String replyMemberIcon) {
        this.replyMemberIcon = replyMemberIcon;
    }

    public String getReplyedUserName() {
        return replyedUserName;
    }

    public void setReplyedUserName(String replyedUserName) {
        this.replyedUserName = replyedUserName;
    }

    public String getReplyedUserIcon() {
        return replyedUserIcon;
    }

    public void setReplyedUserIcon(String replyedUserIcon) {
        this.replyedUserIcon = replyedUserIcon;
    }
}
