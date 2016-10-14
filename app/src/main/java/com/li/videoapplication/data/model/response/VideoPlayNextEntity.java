package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponseEntity;



@SuppressWarnings("serial")
public class VideoPlayNextEntity extends BaseResponseEntity {
	
	private VideoImage data;

	public VideoImage getData() {
		return data;
	}

	public void setData(VideoImage data) {
		this.data = data;
	}
}
