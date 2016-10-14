package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.framework.BaseResponse2Entity;


@SuppressWarnings("serial")
public class MessageMsgRedEntity extends BaseResponseEntity {

	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data extends BaseResponse2Entity {

		private String vpNum;

		private String groupNum;

		private String sysNum;

		public String getVpNum() {
			return vpNum;
		}

		public void setVpNum(String vpNum) {
			this.vpNum = vpNum;
		}

		public String getGroupNum() {
			return groupNum;
		}

		public void setGroupNum(String groupNum) {
			this.groupNum = groupNum;
		}

		public String getSysNum() {
			return sysNum;
		}

		public void setSysNum(String sysNum) {
			this.sysNum = sysNum;
		}
	}
}
