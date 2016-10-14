package com.li.videoapplication.data.model.entity;

import java.util.List;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：主页分组
 */
public class GameGroup extends BaseEntity {

	private String title;
	private List<Game> list;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Game> getList() {
		return list;
	}

	public void setList(List<Game> list) {
		this.list = list;
	}
}
