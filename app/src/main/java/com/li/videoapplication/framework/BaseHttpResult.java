package com.li.videoapplication.framework;

import com.li.videoapplication.data.network.Contants;

/**
 *  基本实体:接口对应实体（与原来的BaseResponseEntity是一样的）
 */
public class BaseHttpResult<T> {

    private boolean result;
    private String msg = Contants.DEFAULT_STRING;
    public T data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
