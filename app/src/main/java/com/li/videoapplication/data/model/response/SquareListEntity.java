package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.framework.BaseResponse2Entity;

@SuppressWarnings("serial")
public class SquareListEntity extends BaseResponseEntity {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public class Data extends BaseResponse2Entity {

		private String game_id;

		private List<VideoImage> list;

		public List<VideoImage> getList() {
			return list;
		}

		public void setList(List<VideoImage> list) {
			this.list = list;
		}

		public String getGame_id() {
			return game_id;
		}

		public void setGame_id(String game_id) {
			this.game_id = game_id;
		}
	}
}
