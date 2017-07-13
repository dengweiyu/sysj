package com.ifeimo.im.common.bean.eventbus;

import com.ifeimo.im.framwork.commander.IMWindow;

/**
 * Created by lpds on 2017/5/12.
 */
public class GroupChatWindowEntity implements BaseChatWindowEntity {

    private String roomId;



    @Override
    public int getType() {
        return IMWindow.MUCCHAT_TYPE;
    }

    @Override
    public String getkey() {
        return roomId;
    }

}
