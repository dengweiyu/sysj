package com.li.videoapplication.data.network;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Token 刷新 拦截器
 */

public class RefreshTokenInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().addHeader("version","233").build();
        Log.e("RefreshTokenInterceptor",request.urlString());
        Response response = chain.proceed(request);

        //权限验证  或者token失效
        if (response.code() == 401 ){

        }

        return response;
    }
}
