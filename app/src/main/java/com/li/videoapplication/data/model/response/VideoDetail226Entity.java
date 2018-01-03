package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponseEntity;



@SuppressWarnings("serial")
public class VideoDetail226Entity extends BaseResponseEntity {
	
	private VideoImage AData;

	public VideoImage getData() {
		return AData;
	}

	public void setData(VideoImage data) {
		this.AData = data;
	}
}
