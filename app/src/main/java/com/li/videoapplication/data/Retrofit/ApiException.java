package com.li.videoapplication.data.Retrofit;


import com.li.videoapplication.framework.BaseHttpResult;

/**
 * 异常类
 * 使用需谨慎
 */
public class ApiException extends RuntimeException {



    public ApiException(BaseHttpResult baseHttpResult) {
        this(getApiExceptionMessage(baseHttpResult));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 对服务器接口传过来的错误信息进行统一处理
     * 免除在Activity的过多的错误判断
     */
    private static String getApiExceptionMessage(BaseHttpResult baseHttpResult){

        return baseHttpResult.getMsg();
    }
}

