package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class DetailNewEntity extends BaseResponseEntity {
	
	private Member data;

	public Member getData() {
		return data;
	}

	public void setData(Member data) {
		this.data = data;
	}
}
