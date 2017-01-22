package com.li.videoapplication.mvp;

import com.li.videoapplication.data.network.RequestUrl;

/**
 * 常量：
 */
public class Constant {
    //抽奖接口 js接口名
    public static final String JS_SWEEPSTAKE = "sweepstake";
    //抽奖接口
    public static final String API_SWEEPSTAKE = RequestUrl.getInstance().getSweepstake();
    //跳转qq聊天
    public static final String QQURL = "mqqwpa://im/chat?chat_type=wpa&uin=";
    //傻逼陈翔的qq
    public static final String SYSJQQID = "2768484684";
    //我的钱包问号帮助网页链接
    public static final String WEB_WALLET = "http://m.17sysj.com/help/wallet";

}
