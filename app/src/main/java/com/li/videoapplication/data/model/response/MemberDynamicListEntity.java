package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponseEntity;



@SuppressWarnings("serial")
public class MemberDynamicListEntity extends BaseResponseEntity {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public class Data extends Member {
		
		private String page_count;
		
		private List<VideoImage> list;

		public String getPage_count() {
			return page_count;
		}

		public void setPage_count(String page_count) {
			this.page_count = page_count;
		}

		public List<VideoImage> getList() {
			return list;
		}

		public void setList(List<VideoImage> list) {
			this.list = list;
		}
	}
}
