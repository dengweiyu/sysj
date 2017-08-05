package com.li.videoapplication.data.network;



import com.li.videoapplication.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp3 所有接口默认添加请求参
 */

public class NewAddVersionParamInterceptor implements Interceptor {

    public static final String VERSION = BuildConfig.VERSION_NAME;
    public static final  String CURRENT_VERSION = "current_version";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String url = request.url().toString();

        if (url.indexOf("?") > 0){
            url += "&"+CURRENT_VERSION+"="+VERSION;
        }else {
            url += "?"+CURRENT_VERSION+"="+VERSION;
        }

        request = request
                .newBuilder()
                .addHeader("Connection","Keep-Alive")
                .url(url+"")
                .build();


        return chain.proceed(request);
    }
}
