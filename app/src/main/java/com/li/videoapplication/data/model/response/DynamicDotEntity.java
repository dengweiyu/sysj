package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class DynamicDotEntity extends BaseResponseEntity {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public class Data extends Member {
		private boolean hasNew;
		private String info;

		public boolean isHasNew() {
			return hasNew;
		}

		public void setHasNew(boolean hasNew) {
			this.hasNew = hasNew;
		}

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}
	}
}
