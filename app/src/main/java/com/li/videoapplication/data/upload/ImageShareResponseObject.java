package com.li.videoapplication.data.upload;

import com.li.videoapplication.framework.BaseEntity;

/**
 * 图片上传请求响应
 */
public class ImageShareResponseObject extends BaseEntity{

    private boolean result;

    private int status;

    private String msg;

    public ImageShareResponseObject(boolean result, int status, String msg) {
        this.result = result;
        this.status = status;
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
