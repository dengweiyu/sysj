package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 实体类：启动图片广告
 */
@SuppressWarnings("serial")
public class Advertisement extends BaseEntity {

	//下载类：
	public static final int AD_CLICK_STATUS_11 = 11; //点击广告位，展示页面或直接下载
	public static final int AD_CLICK_STATUS_12 = 12; //点击展示页面下载按键并进行下载
	//落地类：
	public static final int AD_CLICK_STATUS_23 = 23; //点击广告位，展示页面

	private long ad_location_id;
	private long ad_location_port;
	private String ad_location_name;
	private String ad_location_desc;
	private int time;
	private String target;

	private long ad_id;
	private int ad_type;
	private String title;
	private String flag;
	private String ad_url_android;
	private String ad_url_ios;
	private int starttime;
	private int endtime;
	private int ad_period_start;
	private int ad_period_end;
	private int changetime;

	private String server_pic_a;
	private String download_android;

	public String getDownload_android() {
		return download_android;
	}

	public void setDownload_android(String download_android) {
		this.download_android = download_android;
	}

	public String getServer_pic_a() {
		return server_pic_a;
	}

	public void setServer_pic_a(String server_pic_a) {
		this.server_pic_a = server_pic_a;
	}

	public long getAd_location_id() {
		return ad_location_id;
	}

	public void setAd_location_id(long ad_location_id) {
		this.ad_location_id = ad_location_id;
	}

	public long getAd_location_port() {
		return ad_location_port;
	}

	public void setAd_location_port(long ad_location_port) {
		this.ad_location_port = ad_location_port;
	}

	public String getAd_location_name() {
		return ad_location_name;
	}

	public void setAd_location_name(String ad_location_name) {
		this.ad_location_name = ad_location_name;
	}

	public String getAd_location_desc() {
		return ad_location_desc;
	}

	public void setAd_location_desc(String ad_location_desc) {
		this.ad_location_desc = ad_location_desc;
	}

	public long getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public long getAd_id() {
		return ad_id;
	}

	public void setAd_id(long ad_id) {
		this.ad_id = ad_id;
	}

	public long getAd_type() {
		return ad_type;
	}

	public void setAd_type(int ad_type) {
		this.ad_type = ad_type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getAd_url_android() {
		return ad_url_android;
	}

	public void setAd_url_android(String ad_url_android) {
		this.ad_url_android = ad_url_android;
	}

	public String getAd_url_ios() {
		return ad_url_ios;
	}

	public void setAd_url_ios(String ad_url_ios) {
		this.ad_url_ios = ad_url_ios;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public long getAd_period_start() {
		return ad_period_start;
	}

	public void setAd_period_start(int ad_period_start) {
		this.ad_period_start = ad_period_start;
	}

	public long getAd_period_end() {
		return ad_period_end;
	}

	public void setAd_period_end(int ad_period_end) {
		this.ad_period_end = ad_period_end;
	}

	public int getChangetime() {
		return changetime;
	}

	public void setChangetime(int changetime) {
		this.changetime = changetime;
	}
}
