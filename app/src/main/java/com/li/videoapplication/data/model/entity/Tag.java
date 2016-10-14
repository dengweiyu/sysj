package com.li.videoapplication.data.model.entity;


import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：标签
 */
@SuppressWarnings("serial")
public class Tag extends BaseEntity {

	private String game_tag_id;
	private String name;
	private String location;

	public String getGame_tag_id() {
		return game_tag_id;
	}

	public void setGame_tag_id(String game_tag_id) {
		this.game_tag_id = game_tag_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
