package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体：文件下载
 */
@SuppressWarnings("serial")
public class Download extends BaseEntity {

	private String title;//下载标题
	private String download_url;//下载地址 xxx.apk
	private String app_name; //广告下载APP名
	private String build;
	private String size_num;


	public String getSize_num() {
		return size_num;
	}

	public void setSize_num(String size_num) {
		this.size_num = size_num;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDownload_url() {
		return download_url;
	}

	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}
}
