package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体：下载
 */
@SuppressWarnings("serial")
public class Download extends BaseEntity {

	private String title;//下载标题
	private String download_url;//下载地址 xxx.apk

	private String app_name; //广告下载APP名
	private String build;
	private String size_num;
	private long app_id;
	private long type_id;
	private String game_id;
	private String flag;
	private String size_text;
	private String play_num;
	private String play_text;
	private String app_intro;
	private String display;
	private String a_download_url = "";
	private String i_download_url;

	public long getApp_id() {
		return app_id;
	}

	public void setApp_id(long app_id) {
		this.app_id = app_id;
	}

	public long getType_id() {
		return type_id;
	}

	public void setType_id(long type_id) {
		this.type_id = type_id;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSize_text() {
		return size_text;
	}

	public void setSize_text(String size_text) {
		this.size_text = size_text;
	}

	public String getPlay_num() {
		return play_num;
	}

	public void setPlay_num(String play_num) {
		this.play_num = play_num;
	}

	public String getPlay_text() {
		return play_text;
	}

	public void setPlay_text(String play_text) {
		this.play_text = play_text;
	}

	public String getApp_intro() {
		return app_intro;
	}

	public void setApp_intro(String app_intro) {
		this.app_intro = app_intro;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getA_download_url() {
		return a_download_url;
	}

	public void setA_download_url(String a_download_url) {
		this.a_download_url = a_download_url;
	}

	public String getI_download_url() {
		return i_download_url;
	}

	public void setI_download_url(String i_download_url) {
		this.i_download_url = i_download_url;
	}

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
