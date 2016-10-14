package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.framework.BaseResponse2Entity;



@SuppressWarnings("serial")
public class SearchVideoEntity extends BaseResponseEntity {
	
	private Data data;
	
	private Game game;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public class Data extends BaseResponse2Entity {
		
		private List<VideoImage> list;

		public List<VideoImage> getList() {
			return list;
		}

		public void setList(List<VideoImage> list) {
			this.list = list;
		}
	}
	
	public class Game extends BaseResponse2Entity {
		
		private List<com.li.videoapplication.data.model.entity.Game> gameList;

		public List<com.li.videoapplication.data.model.entity.Game> getList() {
			return gameList;
		}

		public void setList(List<com.li.videoapplication.data.model.entity.Game> list) {
			this.gameList = list;
		}
	}
}
