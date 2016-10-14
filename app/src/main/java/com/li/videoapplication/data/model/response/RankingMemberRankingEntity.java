package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.framework.BaseResponse2Entity;



@SuppressWarnings("serial")
public class RankingMemberRankingEntity extends BaseResponseEntity {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public class Data extends BaseResponse2Entity {
		
		private String myRanking;
		
		private List<Member> list;

		public List<Member> getList() {
			return list;
		}

		public void setList(List<Member> list) {
			this.list = list;
		}

		public String getMyRanking() {
			return myRanking;
		}

		public void setMyRanking(String myRanking) {
			this.myRanking = myRanking;
		}
	}

}
