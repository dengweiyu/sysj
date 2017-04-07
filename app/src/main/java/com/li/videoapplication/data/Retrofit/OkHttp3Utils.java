package com.li.videoapplication.data.Retrofit;

import android.util.Log;

import com.li.videoapplication.BuildConfig;
import com.li.videoapplication.data.model.entity.Tag;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Okio;

/*
 *  okHttp的配置
 * 缓存已经添加  目前只支持GET请求的缓存  POST的我在找合适的处理方法
 * 也可根据自己的需要进行相关的修改
 */
public class OkHttp3Utils {

    private static OkHttpClient mOkHttpClient;
    private final  static String TAG_URL = "Network_Url";
    private final  static String TAG_RESP = "Network_Resp";
    private final  static String TAG_HEADER = "Network_Header";
    private final  static String TAG_BODY = "Network_Body";
    private final  static String TAG_EXCEPTION = "Network_Exception";
    /**
     * 获取OkHttpClient对象
     */
    public static OkHttpClient getOkHttpClient() {
        LogInterceptor interceptor = null;
        if (BuildConfig.DEBUG){
            interceptor = new LogInterceptor();
        }
        if (null == mOkHttpClient) {

            //同样okhttp3后也使用build设计模式
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
             //       .addNetworkInterceptor(interceptor)          // 这里只要加入 首页的加载就出问题
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Request newRequest = request.newBuilder()
                                    .addHeader("Connection","Keep-Alive")
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .build();

        }

        return mOkHttpClient;
    }

    /**
     * 拦截器 打印日志
     */
    static class  LogInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (request != null){
                Log.e(TAG_URL,request.url().toString());
            }
            RequestBody requestBody = request.body();
            Headers headers = request.headers();
            Set<String> headersSet = headers.names();

            Iterator iterator = headersSet.iterator();
            StringBuffer headerBuffer = new StringBuffer();
            while(iterator.hasNext()){
                headerBuffer.append(iterator.next());
                headerBuffer.append("\n");
            }
            Log.e(TAG_HEADER,headerBuffer.toString());
            if (requestBody != null){
                //Log.e(TAG_BODY,requestBody.);
            }
            Response response = null;
            response = chain.proceed(request);
            if (response != null){
                okhttp3.ResponseBody responseBody = response.body();
                okhttp3.ResponseBody copyBody = response.peekBody(responseBody.contentLength());
                String responseStr = copyBody.string();
                Log.e(TAG_RESP,new String(responseStr.getBytes("UTF-8")));
            }
            return response;
        }
    }
}
