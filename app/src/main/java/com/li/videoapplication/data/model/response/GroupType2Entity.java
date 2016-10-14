package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.GroupType;
import com.li.videoapplication.framework.BaseResponseEntity;
/**
 * 接口实体类：圈子类型
 * @author z
 *
 */
@SuppressWarnings("serial")
public class GroupType2Entity extends BaseResponseEntity {
	
	private List<GroupType> data;

	public List<GroupType> getData() {
		return data;
	}

	public void setData(List<GroupType> data) {	
		this.data = data;
	}
}
