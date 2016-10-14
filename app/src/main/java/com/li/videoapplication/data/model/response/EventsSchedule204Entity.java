package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

@SuppressWarnings("serial")
public class EventsSchedule204Entity extends BaseResponseEntity {
	
	private List<Match> data;

	public List<Match> getData() {
		return data;
	}

	public void setData(List<Match> data) {	
		this.data = data;
	}
}
