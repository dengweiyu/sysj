package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.framework.BaseResponseEntity;



@SuppressWarnings("serial")
public class HotGameEntity extends BaseResponseEntity {
	
	private List<Game> data;

	public List<Game> getData() {
		return data;
	}

	public void setData(List<Game> data) {
		this.data = data;
	}
}
