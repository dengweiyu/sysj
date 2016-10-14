package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.GroupMessage;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.framework.BaseResponse2Entity;

@SuppressWarnings("serial")
public class MessageGroupMessageEntity extends BaseResponseEntity {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data extends BaseResponse2Entity {

		private List<GroupMessage> list;

		public List<GroupMessage> getList() {
			return list;
		}

		public void setList(List<GroupMessage> list) {
			this.list = list;
		}
	}

}
