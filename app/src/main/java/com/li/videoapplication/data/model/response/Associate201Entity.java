package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Associate;
import com.li.videoapplication.framework.BaseResponseEntity;



@SuppressWarnings("serial")
public class Associate201Entity extends BaseResponseEntity {
	
	private List<Associate> data;

	public List<Associate> getData() {
		return data;
	}

	public void setData(List<Associate> data) {
		this.data = data;
	}
}
