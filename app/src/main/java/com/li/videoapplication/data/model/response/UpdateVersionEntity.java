package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Update;
import com.li.videoapplication.framework.BaseResponseEntity;
/**
 * 接口实体类：版本升级
 * @author z
 *
 */
@SuppressWarnings("serial")
public class UpdateVersionEntity extends BaseResponseEntity {
	
	private List<Update> data;

	public List<Update> getData() {
		return data;
	}

	public void setData(List<Update> data) {
		this.data = data;
	}
}
