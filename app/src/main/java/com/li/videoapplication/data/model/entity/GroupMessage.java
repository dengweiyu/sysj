package com.li.videoapplication.data.model.entity;

/**
 * 实体类：圈子消息
 */
@SuppressWarnings("serial")
public class GroupMessage extends Message {
	
	private String group_id;
	private String game_id;
	private String group_name;
	private String flag;
	private String new_data_num;
	private String late_data;

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getNew_data_num() {
		return new_data_num;
	}

	public void setNew_data_num(String new_data_num) {
		this.new_data_num = new_data_num;
	}

	public String getLate_data() {
		return late_data;
	}

	public void setLate_data(String late_data) {
		this.late_data = late_data;
	}
}
