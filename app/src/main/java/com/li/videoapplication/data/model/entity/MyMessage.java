package com.li.videoapplication.data.model.entity;

/**
 * 实体类：我的消息
 */
@SuppressWarnings("serial")
public class MyMessage extends Message {
	
	private String member_id;
	private String avatar;
	private String nickname;
	private String video_id;
	private String pic_id;
	
	/**
	 * 消息是否已读：
	 *           1未读
	 *           0已读
	 */
	private String mark;
	private boolean videoIsDel;

	public boolean isVideoIsDel() {
		return videoIsDel;
	}

	public void setVideoIsDel(boolean videoIsDel) {
		this.videoIsDel = videoIsDel;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getVideo_id() {
		return video_id;
	}

	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}

	public String getPic_id() {
		return pic_id;
	}

	public void setPic_id(String pic_id) {
		this.pic_id = pic_id;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
}
