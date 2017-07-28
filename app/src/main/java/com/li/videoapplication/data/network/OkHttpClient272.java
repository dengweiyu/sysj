package com.li.videoapplication.data.network;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

/**
 * 功能：OKHttp网络请求客户端
 */
public class OkHttpClient272 implements AbsRequestClient {

	private static OkHttpClient272 instance;

	public static OkHttpClient272 getInstance() {
		if (instance == null) {
			synchronized (LightTask.class) {
				if (instance == null) {
					instance = new OkHttpClient272();
				}
			}
		}
		return instance;
	}

	private com.squareup.okhttp.OkHttpClient okHttpClient;

	private OkHttpClient272() {
		super();
		if (okHttpClient == null){
			okHttpClient = new com.squareup.okhttp.OkHttpClient();
			//okHttpClient.interceptors().add(new RefreshTokenInterceptor());
			//默认每个接口带参 current_version
			okHttpClient.interceptors().add(new AddVersionParamInterceptor());
		}


	}

	public com.squareup.okhttp.OkHttpClient getOkHttpClient() {
		return okHttpClient;
	}

	@Override
	public Object execute(Object o) throws Exception {
		return (Response) execute((Request) o);
	}

	@Override
	public HttpResponse execute(HttpUriRequest httpUriRequest) throws IOException {
		return null;
	}

	@Override
	public Response execute(Request request) throws IOException {
		if (okHttpClient != null)
			return okHttpClient.newCall(request).execute();
		return null;
	}
}
