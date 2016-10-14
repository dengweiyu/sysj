package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体：版本升级
 */
@SuppressWarnings("serial")
public class Update extends BaseEntity {

	private String id;
	private String target;
	private String version_str;
	private String build;
	// N为最新，U为可能，A为不可用
	private String update_flag;
	private String url;
	private String update_url;
	private String change_log;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getVersion_str() {
		return version_str;
	}

	public void setVersion_str(String version_str) {
		this.version_str = version_str;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getUpdate_flag() {
		return update_flag;
	}

	public void setUpdate_flag(String update_flag) {
		this.update_flag = update_flag;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUpdate_url() {
		return update_url;
	}

	public void setUpdate_url(String update_url) {
		this.update_url = update_url;
	}

	public String getChange_log() {
		return change_log;
	}

	public void setChange_log(String change_log) {
		this.change_log = change_log;
	}
}
