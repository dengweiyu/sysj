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
    //支付宝支付
    public static final int ALIPAY = 1;
    //微信支付
    public static final int WXPAY = 2;
    //APP内进入充值页面的入口：1=>我的钱包，2=>申请推荐位，3=>抽奖，4=>个人信息
    public static final int TOPUP_ENTRY_MYWALLEY = 1;
    public static final int TOPUP_ENTRY_RECOMMEND = 2;
    public static final int TOPUP_ENTRY_SWEEP = 3;
    public static final int TOPUP_ENTRY_INFO = 4;
    //商品详情显示：0=>默认，1=>富文本页面底部无按钮，2=>富文本页面底部有按钮
    public static final int PRODUCTSDETAIL_DEFAULT = 0;
    public static final int PRODUCTSDETAIL_RICHTEXT_NOBTN = 1;
    public static final int PRODUCTSDETAIL_RICHTEXT_WITHBTN = 2;
    //点击下载的位置：1=>游戏圈子、2=>视频页、3=>赛事
    public static final int DOWNLOAD_LOCATION_GROUP = 1;
    public static final int DOWNLOAD_LOCATION_VIDEOPLAY = 2;
    public static final int DOWNLOAD_LOCATION_MATCH = 3;
}
