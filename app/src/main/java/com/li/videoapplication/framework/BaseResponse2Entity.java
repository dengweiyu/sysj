package com.li.videoapplication.framework;

/**
 * 基本实体:接口对应实体
 */
@SuppressWarnings("serial")
public class BaseResponse2Entity extends BaseEntity {
	
	private int itemsCount;
	
	private int page_count;

	public int getItemsCount() {
		return itemsCount;
	}

	public void setItemsCount(int itemsCount) {
		this.itemsCount = itemsCount;
	}

	public int getPage_count() {
		return page_count;
	}

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}
}
