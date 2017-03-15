package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.framework.BaseResponse2Entity;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

@SuppressWarnings("serial")
public class MemberMatchPKEntity204 extends BaseResponseEntity {

	private List<Match> data;
	private String currencyNum;

	public String getCurrencyNum() {
		return currencyNum;
	}

	public void setCurrencyNum(String currencyNum) {
		this.currencyNum = currencyNum;
	}

	public List<Match> getData() {
		return data;
	}

	public void setData(List<Match> data) {
		this.data = data;
	}

}
