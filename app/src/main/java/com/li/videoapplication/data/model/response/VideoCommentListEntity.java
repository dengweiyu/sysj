package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.framework.BaseResponse2Entity;

@SuppressWarnings("serial")
public class VideoCommentListEntity extends BaseResponseEntity {
	
	public Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public class Data extends BaseResponse2Entity {
		
		private List<Comment> list;
		
		public List<Comment> getList() {
			return list;
		}
		
		public void setList(List<Comment> list) {
			this.list = list;
		}
	}
}
