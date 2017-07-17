package com.baidu.push.example.commont;

import android.content.Context;

import com.baidu.push.example.BaiduPush;

/**
 * Created by lpds on 2017/6/14.
 */
public interface IBaiduPushDispense {

    void receiver(String title,String description, String customContentString);

    void onClick(Context context, String title, String description, String customContentString);

}
