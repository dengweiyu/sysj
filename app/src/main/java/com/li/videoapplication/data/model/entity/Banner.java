package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;
/**
 * 实体类：首页广告
 */
@SuppressWarnings("serial")
public class Banner extends BaseEntity {
	
	public static final String TYPE_ACTIVITY = "activity";
	public static final String TYPE_PACKAGE = "package";
	public static final String TYPE_VIDEO = "video";
	public static final String TYPE_URL = "url";
	public static final String TYPE_EVENT = "event";

	/**
	 * 广告类型
	 * activity,package,url
	 */
	private String type;
	private String video_id;
	private String package_id;
	private String activity_id;
	private String flag;
	private String flagPath;
	private String title;
	private String time;
	private String url;
	private String qn_key;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVideo_id() {
		return video_id;
	}

	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}

	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlagPath() {
		return flagPath;
	}

	public void setFlagPath(String flagPath) {
		this.flagPath = flagPath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getQn_key() {
		return qn_key;
	}

	public void setQn_key(String qn_key) {
		this.qn_key = qn_key;
	}
	
	private String type_id;
	private String go_url;
	private String yk_url;

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getGo_url() {
		return go_url;
	}

	public void setGo_url(String go_url) {
		this.go_url = go_url;
	}

	public String getYk_url() {
		return yk_url;
	}

	public void setYk_url(String yk_url) {
		this.yk_url = yk_url;
	}
}
