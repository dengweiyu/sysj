package com.li.videoapplication.data.network;

import com.squareup.okhttp.OkHttpClient;

import org.apache.http.client.HttpClient;

/**
 * 功能：网络请求客户端
 */
public class RequestClient {

	/**
	 * 功能：OKHttp客户端
	 */
	public static OkHttpClient getOkHttpClient() {
		return OkHttpClient272.getInstance().getOkHttpClient();
	}

	/**
	 * 功能：OKHttp客户端
	 */
	public static com.squareup.okhttp.apache.OkApacheClient getOkApacheClient() {
		return OkApacheClient272.getInstance().getOkApacheClient();
	}

	/**
	 * 功能：HttpClient4.1客户端
	 */
	public static HttpClient getHttpClient41() {
		return HttpClient41.getInstance().getHttpClient();
	}

	/**
	 * 功能：HttpClient4.3客户端
	 */
	public static HttpClient getHttpClient43() {
		return HttpClient43.getInstance().getHttpClient();
	}
}
