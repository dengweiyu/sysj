package com.li.videoapplication.data.network;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.IOException;

/**
 * 功能：网络请求客户端
 */
public class HttpClient41 implements AbsRequestClient {

	private static HttpClient41 instance;

	public static HttpClient41 getInstance() {
		if (instance == null) {
			synchronized (HttpClient41.class) {
				if (instance == null) {
					instance = new  HttpClient41();
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

	private HttpClient41() {
		super();
		httpClient = getClient();
	}
	
	private ThreadSafeClientConnManager manager;
	
	private HttpClient client;

	/**
	 * 线程安全
	 */
	private HttpClient getClient() {
		if (manager == null) {
			// Create and initialize HTTP parameters
			HttpParams params = new BasicHttpParams();
			ConnManagerParams.setMaxTotalConnections(params, 100);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			// Create and initialize scheme registry 
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("httpClientMethod", PlainSocketFactory.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			// Create an HttpClient with the ThreadSafeClientConnManager.
			// This connection windowManager must be used if more than one thread will
			// be using the HttpClient.
			manager = new ThreadSafeClientConnManager(params, schemeRegistry);
		}
		if (client == null) {
			// Create and initialize HTTP parameters
			HttpParams params = new BasicHttpParams();
			ConnManagerParams.setMaxTotalConnections(params, 100);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			client = new DefaultHttpClient(manager, params);
		}
		return client;
	}

	/**
	 * 线程安全
	 */
	@SuppressWarnings("unused")
	private DefaultHttpClient getThreadSafeClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager manager = client.getConnectionManager();
		HttpParams params = client.getParams();
		ThreadSafeClientConnManager saftManager = new ThreadSafeClientConnManager(params, manager.getSchemeRegistry());
		client = new DefaultHttpClient(saftManager, params);
		return client;
	}

	/**
	 * 一般的写法
	 */
	@SuppressWarnings("unused")
	private DefaultHttpClient getSingleHttpClient() {
		BasicHttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 20000);
		HttpConnectionParams.setSoTimeout(params, 20000);
		DefaultHttpClient httpClient = new DefaultHttpClient(params);
		return httpClient;
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