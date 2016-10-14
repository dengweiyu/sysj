package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Banner;
import com.li.videoapplication.framework.BaseResponseEntity;

@SuppressWarnings("serial")
public class HomeHdEntity extends BaseResponseEntity {
	
	private List<Banner> data;

	public List<Banner> getData() {
		return data;
	}

	public void setData(List<Banner> data) {
		this.data = data;
	}
}
