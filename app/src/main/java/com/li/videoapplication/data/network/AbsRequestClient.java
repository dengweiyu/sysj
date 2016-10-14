package com.li.videoapplication.data.network;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

/**
 * Http客户端接口
 */
public interface AbsRequestClient {

    /**
     * 执行网络请求
     */
    Object execute(Object o) throws Exception;

    /**
     * HttpClient执行网络请求
     */
    HttpResponse execute(HttpUriRequest httpUriRequest) throws ClientProtocolException, IOException;

    /**
     * OkHttpClient执行网络请求
     */
    Response execute(Request request) throws IOException;
}
