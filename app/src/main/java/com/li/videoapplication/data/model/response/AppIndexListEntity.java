package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.AppIndex;
import com.li.videoapplication.framework.BaseResponseEntity;
/**
 * 首页专栏
 * @author z
 *
 */
@SuppressWarnings("serial")
public class AppIndexListEntity extends BaseResponseEntity {
	
	private List<AppIndex> data;

	public List<AppIndex> getData() {
		return data;
	}

	public void setData(List<AppIndex> data) {
		this.data = data;
	}
}
