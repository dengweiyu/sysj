package com.li.videoapplication.data.network;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

/**
 * 功能：OkApache网络请求客户端
 */
public class OkApacheClient272 implements AbsRequestClient {

	private static OkApacheClient272 instance;

	public static OkApacheClient272 getInstance() {
		if (instance == null) {
			synchronized (LightTask.class) {
				if (instance == null) {
					instance = new OkApacheClient272();
				}
			}
		}
		return instance;
	}

	private com.squareup.okhttp.apache.OkApacheClient okApacheClient;

	private OkApacheClient272() {
		super();
		if (okApacheClient == null)
			okApacheClient = new com.squareup.okhttp.apache.OkApacheClient(OkHttpClient272.getInstance().getOkHttpClient());
	}

	public com.squareup.okhttp.apache.OkApacheClient getOkApacheClient() {
		return okApacheClient;
	}

	@Override
	public Object execute(Object o) throws Exception {
		return (HttpResponse) execute((HttpUriRequest) o);
	}

	@Override
	public HttpResponse execute(HttpUriRequest httpUriRequest) throws ClientProtocolException, IOException {
		if (okApacheClient != null)
			return okApacheClient.execute(httpUriRequest);
		return null;
	}

	@Override
	public Response execute(Request request) throws IOException {
		return null;
	}
}
