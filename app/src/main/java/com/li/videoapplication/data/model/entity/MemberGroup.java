package com.li.videoapplication.data.model.entity;

import java.util.List;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：玩家分组
 */
@SuppressWarnings("serial")
public class MemberGroup extends BaseEntity {

	private String title;
	private List<Member> list;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Member> getList() {
		return list;
	}

	public void setList(List<Member> list) {
		this.list = list;
	}
}
