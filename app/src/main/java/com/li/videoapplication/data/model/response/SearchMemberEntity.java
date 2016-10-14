package com.li.videoapplication.data.model.response;

import java.util.List;

import com.li.videoapplication.data.model.entity.Member;
import com.li.videoapplication.framework.BaseEntity;
import com.li.videoapplication.framework.BaseResponseEntity;



@SuppressWarnings("serial")
public class SearchMemberEntity extends BaseResponseEntity {

	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data extends BaseEntity {

		private List<Member> list;

		public List<Member> getList() {
			return list;
		}

		public void setList(List<Member> list) {
			this.list = list;
		}
	}
}
