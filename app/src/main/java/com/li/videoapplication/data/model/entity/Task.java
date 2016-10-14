package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;
/**
 * 实体类：任务
 */
public class Task extends BaseEntity {

//	private String type_id;
//
//	private String game_id;
//
//	private String description;
//
//	private String reward;
//
//	private String starttime;
//
//	private String endtime;
//
//	private String addtime;
//
//	private String add_money;
//
//	private String type_name = new String();
//
//	private String task_flag;
//
//	private String is_get;
//
//	private String status_txt;
//
//	private long taskTimeLength;
//
//	private String flagPath;
//
//	private String is_accept;
//
//	private String flaging;

	public static final String STATUS_ACCEPT = "前往完成";
	public static final String STATUS_FINISH = "已完成";

	private String id;
	private String flag;
	private String name;
	private String content;
	private String add_exp;
	private String num;
	private String num_ing;
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAdd_exp() {
		return add_exp;
	}

	public void setAdd_exp(String add_exp) {
		this.add_exp = add_exp;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getNum_ing() {
		return num_ing;
	}

	public void setNum_ing(String num_ing) {
		this.num_ing = num_ing;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
