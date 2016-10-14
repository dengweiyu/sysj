package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Comment;
import com.li.videoapplication.framework.BaseResponseEntity;



@SuppressWarnings("serial")
public class PhotoPhotoCommentListEntity extends BaseResponseEntity {
	
	private List<Comment> data;

	public List<Comment> getData() {
		return data;
	}

	public void setData(List<Comment> data) {
		this.data = data;
	}
}
