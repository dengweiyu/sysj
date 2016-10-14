package com.li.videoapplication.data.model.entity;

import com.li.videoapplication.framework.BaseEntity;
/**
 * 实体类：启动图片广告
 */
@SuppressWarnings("serial")
public class LaunchImage extends BaseEntity {

	private String launch_id;
	private String title;
	private String flag;
	private int starttime;
	private int endtime;
	private String alone_id;
	private String go_url;

	public String getLaunch_id() {
		return launch_id;
	}

	public void setLaunch_id(String launch_id) {
		this.launch_id = launch_id;
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

	public int getStarttime() {
		return starttime;
	}

	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public String getAlone_id() {
		return alone_id;
	}

	public void setAlone_id(String alone_id) {
		this.alone_id = alone_id;
	}

	public String getGo_url() {
		return go_url;
	}
}
