package com.li.videoapplication.data.network;

/**
 * 功能：网络请求响应回调
 */
public interface RequestListener {
	
	/**
	 * 功能：返回网络访问结果
	 */
	void onResponse(ResponseObject response);
}
