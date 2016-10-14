package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

@SuppressWarnings("serial")
public class AuthorVideoList208Entity extends BaseResponseEntity {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public class Data extends BaseResponse2Entity {
		
		private List<VideoImage> data;

		public List<VideoImage> getData() {
			return data;
		}

		public void setData(List<VideoImage> data) {
			this.data = data;
		}
	}
}
