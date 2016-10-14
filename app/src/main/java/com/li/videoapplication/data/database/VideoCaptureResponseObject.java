package com.li.videoapplication.data.database;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 事件：加载本地视频
 */
public class VideoCaptureResponseObject extends BaseResponseEntity {

    public static final int RESULT_CODE_LOADING = 1;
    public static final int RESULT_CODE_CHECKING = 2;
    public static final int RESULT_CODE_IMPORTING = 3;

    private int resultCode;

    private List<VideoCaptureEntity> data;

    public VideoCaptureResponseObject(boolean result, int resultCode, String msg, List<VideoCaptureEntity> data) {
        this.data = data;
        this.resultCode = resultCode;
        setResult(result);
        setMsg(msg);
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public List<VideoCaptureEntity> getData() {
        return data;
    }

    public void setData(List<VideoCaptureEntity> data) {
        this.data = data;
    }
}
