package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class CloudListEntity extends BaseResponseEntity {
	
	public Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public class Data extends BaseResponse2Entity {
		
		private List<VideoImage> list;
		
		public List<VideoImage> getList() {
			return list;
		}
		
		public void setList(List<VideoImage> list) {
			this.list = list;
		}
	}
}
