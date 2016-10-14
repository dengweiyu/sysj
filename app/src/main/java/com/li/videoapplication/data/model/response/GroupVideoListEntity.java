package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponseEntity;

@SuppressWarnings("serial")
public class GroupVideoListEntity extends BaseResponseEntity {
	
	private List<VideoImage> data;

	public List<VideoImage> getData() {
		return data;
	}

	public void setData(List<VideoImage> data) {
		this.data = data;
	}
}
