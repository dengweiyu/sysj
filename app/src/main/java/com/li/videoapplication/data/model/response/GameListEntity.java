package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.framework.BaseResponse2Entity;

/**
 * 接口实体类：最新，最热游戏
 * 
 * @author z
 *
 */
@SuppressWarnings("serial")
public class GameListEntity extends BaseResponseEntity {

	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data extends BaseResponse2Entity {

		private List<Game> list;

		public List<Game> getList() {
			return list;
		}

		public void setList(List<Game> list) {
			this.list = list;
		}
	}
}
