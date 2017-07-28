package com.li.videoapplication.data.network;

import android.os.Build;

import com.li.videoapplication.BuildConfig;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;



/**
 * 拦截器中为接口增加 current_version 的请求参数
 */

public class AddVersionParamInterceptor implements Interceptor {
    private static final String VERSION = BuildConfig.VERSION_NAME;
    private static final  String CURRENT_VERSION = "current_version";
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        String url = request.urlString();

        if (url.indexOf("?") > 0){
            url += "&"+CURRENT_VERSION+"="+VERSION;
        }else {
            url += "?"+CURRENT_VERSION+"="+VERSION;
        }

        request = request
                    .newBuilder()
                    .url(url+"")
                    .build();
        return chain.proceed(request);
    }
}
