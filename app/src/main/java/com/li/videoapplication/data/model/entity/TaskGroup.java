package com.li.videoapplication.data.model.entity;

import java.util.List;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：任务分组
 */
@SuppressWarnings("serial")
public class TaskGroup extends BaseEntity {

	public static final String TYPENAME_EVERYDAY = "每日任务";
	public static final String TYPENAME_SYSTEM = "系统任务";

	private String type_id;
	private String type_name;
	private List<Task> list;

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public List<Task> getList() {
		return list;
	}

	public void setList(List<Task> list) {
		this.list = list;
	} 
}
