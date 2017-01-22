package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


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
