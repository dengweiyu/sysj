package com.li.videoapplication.data.model.response;


import com.li.videoapplication.framework.BaseResponseEntity;

/**
 * 是否切换聊天为自有IM
 */

public class SwitchChatEntity extends BaseResponseEntity {

    /**
     * privateIM : true
     */

    private boolean privateIM;

    public boolean isPrivateIM() {
        return privateIM;
    }

    public void setPrivateIM(boolean privateIM) {
        this.privateIM = privateIM;
    }
}
