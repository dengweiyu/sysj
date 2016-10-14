package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：游戏圈子
 */
@SuppressWarnings("serial")
public class Comment extends BaseEntity {
	
	private String id;
	private String comment_id;
	private String member_id;
	private String content;
	private String likeNum;
	private String time;
	private String nickname;
	private String avatar;
	private int like_tick;
	private boolean isV;

	public boolean isV() {
		return isV;
	}

	public void setV(boolean v) {
		isV = v;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(String likeNum) {
		this.likeNum = likeNum;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getLike_tick() {
		return like_tick;
	}

	public void setLike_tick(int like_tick) {
		this.like_tick = like_tick;
	}
}

