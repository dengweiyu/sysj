package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.LaunchImage;
import com.li.videoapplication.framework.BaseResponseEntity;

@SuppressWarnings("serial")
public class IndexLaunchImageEntity extends BaseResponseEntity {
	
	private List<LaunchImage> data;
	
	private int changetime;

	public List<LaunchImage> getData() {
		return data;
	}

	public void setData(List<LaunchImage> data) {
		this.data = data;
	}

	public int getChangetime() {
		return changetime;
	}

	public void setChangetime(int changetime) {
		this.changetime = changetime;
	}
}
