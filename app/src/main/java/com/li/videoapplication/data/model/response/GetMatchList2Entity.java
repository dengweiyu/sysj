package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.BaseResponseEntity;

@SuppressWarnings("serial")
public class GetMatchList2Entity extends BaseResponseEntity {
	
	private List<Match> data;

	public List<Match> getData() {
		return data;
	}

	public void setData(List<Match> data) {	
		this.data = data;
	}
}
