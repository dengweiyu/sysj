package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Gift;
import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class PackageInfoEntity extends BaseResponseEntity {
	
	private Gift data;

	public Gift getData() {
		return data;
	}

	public void setData(Gift data) {
		this.data = data;
	}
}
