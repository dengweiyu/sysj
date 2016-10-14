package com.li.videoapplication.framework;

import com.li.videoapplication.data.network.Contants;

/**
 * 基本实体:接口对应实体
 */
@SuppressWarnings("serial")
public class BaseResponseEntity extends BaseEntity {
	
	private boolean result = false;
	
	private String msg = Contants.DEFAULT_STRING;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	private int total_page;

	public int getTotal_page() {
		return total_page;
	}

	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}
}
