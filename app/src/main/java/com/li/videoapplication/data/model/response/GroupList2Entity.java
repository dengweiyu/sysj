package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

@SuppressWarnings("serial")
public class GroupList2Entity extends BaseResponseEntity {

	private ADataBean AData;

	public ADataBean getAData() {
		return AData;
	}

	public void setAData(ADataBean AData) {
		this.AData = AData;
	}


	public class ADataBean extends BaseResponse2Entity {

		private List<Game> list;
		private medalBean medal;

		public List<Game> getList() {
			return list;
		}

		public void setList(List<Game> list) {
			this.list = list;
		}

		public medalBean getMedal() {
			return medal;
		}

		public void setMedal(medalBean medal) {
			this.medal = medal;
		}

		public class medalBean {
			private String first;
			private String second;
			private String three;

			public String getFirst() {
				return first;
			}

			public void setFirst(String first) {
				this.first = first;
			}

			public String getSecond() {
				return second;
			}

			public void setSecond(String second) {
				this.second = second;
			}

			public String getThree() {
				return three;
			}

			public void setThree(String three) {
				this.three = three;
			}
		}

	}
}
