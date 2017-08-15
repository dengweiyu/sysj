package com.li.videoapplication.data.network;



import com.google.gson.Gson;
import com.li.videoapplication.BuildConfig;
import com.li.videoapplication.data.model.response.Token;
import com.li.videoapplication.data.preferences.PreferencesHepler;
import com.li.videoapplication.framework.AppAccount;
import com.li.videoapplication.framework.BaseResponseEntity;
import com.li.videoapplication.utils.StringUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import okio.Buffer;


/**
 * okhttp2 拦截器中为接口增加 current_version 的请求参数
 */

public class AddVersionParamInterceptor implements Interceptor {
    public static final String VERSION = BuildConfig.VERSION_NAME;
    public static final  String CURRENT_VERSION = "current_version";

    //重入锁 避免出现并发刷新access_token的情况
    private final ReentrantLock mReentrantLock = new ReentrantLock();

    private static boolean mAccessTokenIsOverDue = true;
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
                    .addHeader("Connection","Keep-Alive")
                    .build();
        Response response = chain.proceed(request);

        if(response.code() == 200){
            //.body().string()只能调用一次 ，因为是输入流 所以克隆一个新的response
            Response cloneResponse = response.newBuilder().build();
            ResponseBody cloneBody = cloneResponse.body();
            //获取返回值字符串
            String res = cloneBody.string();

            //还原response
            response = cloneResponse
                    .newBuilder()
                    .body(ResponseBody.create(cloneBody.contentType(),res))
                    .build();

            //同步刷新token
            if (isOverdue(res)){
                mAccessTokenIsOverDue = true;
                //由于并发请求的时候，接口返回结果的时间并不一定 因此会出现 请求是并发 返回结果时不是并发，导致出现多次刷新的情况
                if (mReentrantLock.tryLock()) {
                        try {
                            //
                            Response refreshResponse = RequestClient.getOkHttpClient().newCall(refreshAccessToken()).execute();
                            if (refreshResponse.code() == 200) {
                                //如果token刷新成功 重复请求接口
                                if (isRefreshSuccess(refreshResponse.body().string())) {
                                 //   mLastRefreshTime = System.currentTimeMillis();
                                    mAccessTokenIsOverDue = false;
                                    response = retryRequest(chain, request);
                                    mReentrantLock.newCondition().signalAll();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            mReentrantLock.unlock();
                        }
                }else {
                    try {
                        mReentrantLock.lock();
                        if (mAccessTokenIsOverDue){
                            mReentrantLock.newCondition().await();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        mReentrantLock.unlock();
                    }
                    response =  retryRequest(chain,request);
                }
            }else {
                mAccessTokenIsOverDue = false;

            }
        }
        return response;
    }

    /**
     * access_token  refresh token是否过期
     * @param data
     * @return
     */
    private boolean isOverdue(String data){
        if (!StringUtil.isNull(data)){
            try {
                Gson gson  = new Gson();
                BaseResponseEntity entity = gson.fromJson(data, BaseResponseEntity.class);
                //access_token 失效
                if (entity.getCode() == 30100){
                    return true;
                }else if (entity.getCode() == 30101 || entity.getCode() == 30099){     //refresh_token 失效 或者access_token缺失
                    //退出当前登录
                    AppAccount.logout();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * access_token 是否刷新成功
     * @param data
     * @return
     */
    private boolean isRefreshSuccess(String data){
        if (!StringUtil.isNull(data)){
            try {
                Gson gson  = new Gson();
                Token entity = gson.fromJson(data, Token.class);
                //access_token 失效
                if (entity.isResult()){
                    //保存token
                    AppAccount.saveAccessToken(entity);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 同步请求刷新token
     * @return
     */
    private Request refreshAccessToken(){
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Map<String,Object> params = RequestParams.getInstance().refreshToken(
                                    "E!AHcLR%Pxyp*&d8","refresh_token",
                                    AppAccount.getRefreshToken(),
                                    PreferencesHepler.getInstance().getMember_id());
        Set<String> keys = params.keySet();
        for (String key : keys) {
            if (params.get(key) != null) {
                String value = params.get(key).toString();
                builder.add(key, value);
            }
        }
        Request request = new Request
                        .Builder()
                        .addHeader("Connection","Keep-Alive")
                        .post(builder.build())
                        .url(RequestUrl.getInstance().refreshAccessToken())
                        .build();
        return request;
    }

    /**
     * 构建新的body
     * @param body
     * @return
     */
    private RequestBody createNewRequestBody(RequestBody body){
        String oldBody = bodyToString(body);
        try {
            if (!StringUtil.isNull(oldBody)){
                int start = oldBody.indexOf("access_token=");
                int end = oldBody.indexOf("&",start);
                if (start >= 0){
                    //
                    String oldAccessToken = oldBody.substring(start+"access_token=".length(),end);
                    String newAccessToken  = AppAccount.getAccessToken();
                    oldBody = oldBody.replace(oldAccessToken,newAccessToken);

                    return RequestBody.create(body.contentType(),oldBody);
                 }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * body转String
     * @param body
     * @return
     */
    private String bodyToString(RequestBody body){
        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 重试接口
     * @param chain
     * @param request
     * @return
     * @throws IOException
     */
    private Response retryRequest(Chain chain,Request request) throws IOException{
      RequestBody newBody = createNewRequestBody(request.body());
        if (newBody != null){
            request = request
                    .newBuilder()
                    .post(newBody)
                    .build();
        }
        return chain.proceed(request);
    }
}
