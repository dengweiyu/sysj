package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Message;
import com.li.videoapplication.framework.BaseResponseEntity;

@SuppressWarnings("serial")
public class MsgListEntity extends BaseResponseEntity {
	
	private List<Message> data;

	public List<Message> getData() {
		return data;
	}

	public void setData(List<Message> data) {
		this.data = data;
	}
}
