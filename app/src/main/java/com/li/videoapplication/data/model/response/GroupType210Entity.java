package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 接口实体类：圈子类型
 * @author z
 *
 */
@SuppressWarnings("serial")
public class GroupType210Entity extends BaseResponseEntity {
	
	private List<GroupType> data;

	public List<GroupType> getData() {
		return data;
	}

	public void setData(List<GroupType> data) {	
		this.data = data;
	}
}
