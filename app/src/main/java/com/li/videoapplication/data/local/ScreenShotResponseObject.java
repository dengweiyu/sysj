package com.li.videoapplication.data.local;

import com.li.videoapplication.framework.BaseResponseEntity;

import java.util.List;

/**
 * 事件：加载截图
 */
public class ScreenShotResponseObject extends BaseResponseEntity {

    private List<ScreenShotEntity> data;

    public ScreenShotResponseObject(boolean result, String msg, List<ScreenShotEntity> data) {
        this.data = data;
        setResult(result);
        setMsg(msg);
    }

    public List<ScreenShotEntity> getData() {
        return data;
    }

    public void setData(List<ScreenShotEntity> data) {
        this.data = data;
    }
}
