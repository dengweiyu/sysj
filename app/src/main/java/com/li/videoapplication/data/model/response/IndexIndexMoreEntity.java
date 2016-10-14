package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.VideoImageGroup;
import com.li.videoapplication.framework.BaseResponseEntity;

@SuppressWarnings("serial")
public class IndexIndexMoreEntity extends BaseResponseEntity {
	
	private VideoImageGroup data;

	public VideoImageGroup getData() {
		return data;
	}

	public void setData(VideoImageGroup data) {
		this.data = data;
	}
}
