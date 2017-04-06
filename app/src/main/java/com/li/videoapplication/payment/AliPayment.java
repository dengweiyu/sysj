package com.li.videoapplication.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.li.videoapplication.data.model.response.EventsList214Entity;
import com.li.videoapplication.data.model.response.PayResult;
import com.li.videoapplication.data.model.response.PaymentEntity;
import com.li.videoapplication.tools.TimeHelper;

import java.util.Map;

/**
 * Created by liuwei on 2017/4/2.
 */

public class AliPayment implements IPayment {

    private static final int SDK_PAY_FLAG = 1;

    private String TAG = "Alipayment";
    private Activity mContext;
    private IPayment.Callback mCallback;
    public AliPayment(Context context){
        init(context);
    }

    private void init(Context context){
        if(context instanceof Activity){
            mContext = (Activity) context;
        }else {
            throw new RuntimeException("should be use activity,while to run alipay");
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void executePayment(final PaymentEntity entity, Callback callback) {
        mCallback = callback;
        if (entity == null || entity.getData().getAlipay() == null){
            if (mCallback != null){
                mCallback.fail("订单获取失败");
            }
            return;
        }

        Log.e("alipay",entity.getData().getAlipay());
        //异步执行

        Runnable task = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mContext);
                Map<String, String> result = alipay.payV2(entity.getData().getAlipay(), true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        new Thread(task).start();
    }

    @Override
    public void handleIntent(Intent intent) {
        //alipay nothing to do
    }

   Handler mHandler = new Handler() {
       @Override
       public void handleMessage(Message msg) {
           switch (msg.what) {
               case SDK_PAY_FLAG: {
                   try {
                       @SuppressWarnings("unchecked")
                       PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                       /**
                        对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                        */
                       String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                       String resultStatus = payResult.getResultStatus();
                       // 判断resultStatus 为9000则代表支付成功
                       if (TextUtils.equals(resultStatus, "9000")) {
                           // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                           mCallback.success();
                           Log.d(TAG, "支付成功");
                       } else {
                           if (resultStatus.equals("6001")){
                               mCallback.fail("取消支付");
                           }else {
                               mCallback.fail("支付失败");
                           }
                           // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                           Log.d(TAG, "支付失败"+resultStatus);
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                   break;
               }
           }
       }
   };
}
