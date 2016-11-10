package com.li.videoapplication.data.model.response;

import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;


@SuppressWarnings("serial")
public class GoodsListEntity extends BaseResponseEntity {

	public List<Currency> data;

	public List<Currency> getData() {
		return data;
	}

	public void setData(List<Currency> data) {
		this.data = data;
	}
}
