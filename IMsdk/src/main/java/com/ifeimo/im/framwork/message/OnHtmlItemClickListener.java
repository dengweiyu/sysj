package com.ifeimo.im.framwork.message;

import android.view.View;

/**
 * Created by lpds on 2017/4/11.
 *
 * 点击聊天文本，是html的就回调此方法
 *
 */
public interface OnHtmlItemClickListener {
    void onClick(String memberid,String  defaultStr,String[] html,boolean isMe);
}
