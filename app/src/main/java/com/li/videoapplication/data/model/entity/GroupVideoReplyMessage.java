package com.li.videoapplication.data.model.entity;

/**
 * 游戏圈回复、视频回复列
 * Created by cx on 2018/1/29.
 */

public class GroupVideoReplyMessage extends Message {

    /**
     "msg_id": "43",
     "original_content": "宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼宾总牛逼",
     "time": "1516950454",
     "relid": "5127",              //消息圈
     "relation_id": "4468437",     //视频
     "member_id": "969106",
     "relevance_member_id": "4741865",
     "mark": "1",
     "conent": "回复了你的消息，快去看看~",
     "member_name": "yangruiqi1",
     "icon": "http://apps.ifeimo.com/Public/Uploads/Member/Avatar/default3.JPG",
     "symbol": "gameGroupReply",
     "msg_type": "8"
     */

    private String relid;              //消息圈
    private String relation_id;        //视频
    private String member_id;
    private String mark;
    private String icon;
    private String relevance_member_id;
    private String original_content;
    private String member_name;
    private String symbol;
    private String msg_type;

    public String getRelid() {
        return relid;
    }

    public void setRelid(String relid) {
        this.relid = relid;
    }

    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRelevance_member_id() {
        return relevance_member_id;
    }

    public void setRelevance_member_id(String relevance_member_id) {
        this.relevance_member_id = relevance_member_id;
    }

    public String getOriginal_content() {
        return original_content;
    }

    public void setOriginal_content(String original_content) {
        this.original_content = original_content;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }
}
