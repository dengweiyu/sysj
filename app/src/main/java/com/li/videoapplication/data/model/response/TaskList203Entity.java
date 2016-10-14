package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Task;
import com.li.videoapplication.data.model.entity.TaskGroup;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class TaskList203Entity extends BaseResponseEntity {

	private List<TaskGroup> data;

	public List<TaskGroup> getData() {
		return data;
	}

	public void setData(List<TaskGroup> data) {
		this.data = data;
	}
}
