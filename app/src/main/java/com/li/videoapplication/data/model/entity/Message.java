package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;
/**
 * 实体类：消息
 */
@SuppressWarnings("serial")
public class Message extends BaseEntity {
	
	private String msg_id;
	private String content;
	private String time;

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
