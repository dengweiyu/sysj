package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.framework.BaseResponseEntity;



@SuppressWarnings("serial")
public class DaRenListNewVideoEntity extends BaseResponseEntity {
	
	private List<Member> data;

	public List<Member> getData() {
		return data;
	}

	public void setData(List<Member> data) {
		this.data = data;
	}
}
