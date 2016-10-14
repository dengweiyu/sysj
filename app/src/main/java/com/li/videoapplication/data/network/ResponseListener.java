package com.li.videoapplication.data.network;


import com.li.videoapplication.framework.BaseEntity;

/**
 * 功能：网络请求响应回调
 */
public interface ResponseListener {
	
	/**
	 * 功能：返回网络访问结果
	 */
	void onResponse(BaseEntity entity);
}
