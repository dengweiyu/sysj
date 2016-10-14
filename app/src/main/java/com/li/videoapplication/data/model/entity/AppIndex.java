package com.li.videoapplication.data.model.entity;

import java.util.List;

import com.li.videoapplication.framework.BaseEntity;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * 首页专栏
 */
@SuppressWarnings("serial")
public class AppIndex extends BaseEntity {
	
	private String title;
	private String icon_pic;
	private String more_mark;
	private List<VideoImage> list;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon_pic() {
		return icon_pic;
	}

	public void setIcon_pic(String icon_pic) {
		this.icon_pic = icon_pic;
	}

	public String getMore_mark() {
		return more_mark;
	}

	public void setMore_mark(String more_mark) {
		this.more_mark = more_mark;
	}

	public List<VideoImage> getList() {
		return list;
	}

	public void setList(List<VideoImage> list) {
		this.list = list;
	}
}
