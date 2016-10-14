package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.VideoImage;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.tools.ArrayHelper;

@SuppressWarnings("serial")
public class IndexChangeGuessEntity extends BaseResponseEntity {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
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
	
	private String video_ids;

	public String getVideo_ids() {
		return video_ids;
	}

	public void setVideo_ids(String video_ids) {
		this.video_ids = video_ids;
	}

	public List<String> getVideoIds() {
		return ArrayHelper.array2List(getVideo_ids());
	}
}
