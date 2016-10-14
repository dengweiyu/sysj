package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

@SuppressWarnings("serial")
public class EventsPKList204Entity extends BaseResponseEntity {

	public Data data;
	public int is_last;

	public int getIs_last() {
		return is_last;
	}

	public void setIs_last(int is_last) {
		this.is_last = is_last;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data extends BaseResponse2Entity {

		private List<Match> list;

		private int is_last;

		public int getIs_last() {
			return is_last;
		}

		public void setIs_last(int is_last) {
			this.is_last = is_last;
		}

		public List<Match> getList() {
			return list;
		}

		public void setList(List<Match> list) {
			this.list = list;
		}
	}
}
