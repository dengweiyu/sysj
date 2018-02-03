package com.li.videoapplication.data.model.response;

import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * Created by cx on 2018/1/29.
 */

public class VideoReplyMessageEntity extends BaseResponseEntity {
    private GameGroupReplyMessageEntity.DataBean data;

    public GameGroupReplyMessageEntity.DataBean getData() {
        return data;
    }

    public void setData(GameGroupReplyMessageEntity.DataBean data) {
        this.data = data;
    }
}
