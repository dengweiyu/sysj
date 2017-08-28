package com.li.videoapplication.payment;

import android.content.Context;
import android.util.Log;

/**
 * Created by liuwei on 2017/4/2.
 */

public class PaymentFactory {
    public final static int TYPE_WECHAT = 2;
    public final static int TYPE_TENCENT = 3;
    public final static int TYPE_ALIPAY = 1;

    private PaymentFactory(){}

    public static IPayment createPayment(Context context,int type){
        IPayment payment;
        switch (type){
            case TYPE_ALIPAY:
                payment = new AliPayment(context);      //alipay must be activity
                Log.d("PaymentFactory","create alipay");
                break;
            case TYPE_TENCENT:
                payment = new TencentPayment(context);
                Log.d("PaymentFactory","create TenPayment");
                break;
            default:
                payment = new WechatPayment(context);
                Log.d("PaymentFactory","WechatPayment");
                break;
        }
        return payment;
    }

}
