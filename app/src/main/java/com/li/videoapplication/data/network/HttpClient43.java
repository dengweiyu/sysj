package com.li.videoapplication.data.network;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 功能：网络请求客户端
 */
public class HttpClient43 implements AbsRequestClient {

	private static HttpClient43 instance;

	public static HttpClient43 getInstance() {
		if (instance == null) {
			synchronized (LightTask.class) {
				if (instance == null) {
					instance = new  HttpClient43();
				}
			}
		}
		return instance;
	}

	protected final String name = this.getClass().getName();
	protected final String simpleName = this.getClass().getSimpleName();

	private HttpClient httpClient;

	public HttpClient getHttpClient() {
		return httpClient;
	}

	private HttpClient43() {
		super();
		httpClient = getClient();
	}

	private PoolingHttpClientConnectionManager manager;

	private CloseableHttpClient client;

	private CloseableHttpClient getClient() {
		if (manager == null) {
			manager = new PoolingHttpClientConnectionManager();
			manager.setMaxTotal(100);
		}
		if (client == null) {
			client = HttpClients.custom().useSystemProperties().setConnectionManager(manager).build();
		}
		return client;
	}

	@Override
	public Object execute(Object o) throws Exception {
		return (HttpResponse) execute((HttpUriRequest) o);
	}

	@Override
	public HttpResponse execute(HttpUriRequest httpUriRequest) throws ClientProtocolException, IOException {
		if (httpClient != null)
			return httpClient.execute(httpUriRequest);
		return null;
	}

	@Override
	public Response execute(Request request) throws IOException {
		return null;
	}
}