package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;


@SuppressWarnings("serial")
public class MemberCurrencyEntity extends BaseResponseEntity {

	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
