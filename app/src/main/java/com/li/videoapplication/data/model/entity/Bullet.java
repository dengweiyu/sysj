package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

import java.util.List;

/**
 * 首页专栏
 */
@SuppressWarnings("serial")
public class Bullet extends BaseEntity {

	private String bullet_id;
	private String video_id;
	private String video_node;
	private String content;

	public String getBullet_id() {
		return bullet_id;
	}

	public void setBullet_id(String bullet_id) {
		this.bullet_id = bullet_id;
	}

	public String getVideo_id() {
		return video_id;
	}

	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}

	public String getVideo_node() {
		return video_node;
	}

	public void setVideo_node(String video_node) {
		this.video_node = video_node;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
