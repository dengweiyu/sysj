package com.li.videoapplication.data.model.entity;

/**
 * 实体类：系统消息
 */
@SuppressWarnings("serial")
public class SysMessage extends Message {

	private String relation_id;//关联的ID
	private String member_id;
    private int explain;//当为1时，关联的ID 为推荐位的系统消息
    private String cover;
    private String mark;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
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

    public int getExplain() {
        return explain;
    }

    public void setExplain(int explain) {
        this.explain = explain;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
