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
public class GroupType2Entity extends BaseResponseEntity {


	private List<GroupType> AData;


	public List<GroupType> getAData() {
		return AData;
	}

	public void setAData(List<GroupType> AData) {
		this.AData = AData;
	}




}
