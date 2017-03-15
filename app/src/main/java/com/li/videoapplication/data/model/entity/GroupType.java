package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：圈子 类型
 */
@SuppressWarnings("serial")
public class GroupType extends BaseEntity {

	private String id;
	private String name;
	private String flag;
	private String hotrank;
	private String sort;
	private String group_type_id;
	private String group_type_name;
	private String parent_id;
	private String path;
	private String level;
	private String label_style;
	private String flagPath;
	private boolean selected;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getHotrank() {
		return hotrank;
	}

	public void setHotrank(String hotrank) {
		this.hotrank = hotrank;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getGroup_type_id() {
		return group_type_id;
	}

	public void setGroup_type_id(String group_type_id) {
		this.group_type_id = group_type_id;
	}

	public String getGroup_type_name() {
		return group_type_name;
	}

	public void setGroup_type_name(String group_type_name) {
		this.group_type_name = group_type_name;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLabel_style() {
		return label_style;
	}

	public void setLabel_style(String label_style) {
		this.label_style = label_style;
	}

	public String getFlagPath() {
		return flagPath;
	}

	public void setFlagPath(String flagPath) {
		this.flagPath = flagPath;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
