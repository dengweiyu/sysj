package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.framework.BaseResponseEntity;



@SuppressWarnings("serial")
public class GroupInfoEntity extends BaseResponseEntity {
	
	private Game data;

	public Game getData() {
		return data;
	}

	public void setData(Game data) {
		this.data = data;
	}
}
