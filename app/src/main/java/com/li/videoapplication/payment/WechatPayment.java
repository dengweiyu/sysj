package com.li.videoapplication.payment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.li.videoapplication.data.model.response.PaymentEntity;
import com.li.videoapplication.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付
 */

public class WechatPayment implements IPayment {
    public final static int PAYMENT_RESULT_SUCCESS = 0;
    public final static int PAYMENT_RESULT_FAIL = -1;
    public final static int PAYMENT_RESULT_CANCEL = -2;

    private String TAG = "WechatPayment";

    private IWXAPI mApi;
    private IPayment.Callback mCallback;
    private Context mContext;
    public WechatPayment(Context context){
        mContext = context;
    }

    private void init(Context context,String appId){
        mApi = WXAPIFactory.createWXAPI(context,null);
        mApi.registerApp(appId);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void executePayment(PaymentEntity entity, Callback callback) {
        mCallback = callback;
        if (entity == null){
            if (mCallback != null){
                mCallback.fail("订单号获取失败");
            }
            return;
        }

        init(mContext,entity.getData().getAppId());

        PaymentEntity.DataBean data  = entity.getData();
        PayReq request = new PayReq();
        request.appId = data.getAppId();
        request.partnerId = data.getBargainorId();
        request.prepayId=data.getTokenId();
        request.packageValue=data.getPackageX();
        request.nonceStr=data.getNonce();
        request.timeStamp=data.getTimestamp()+"";
        request.sign=data.getSign();

        signApi(request);
    }

    @Override
    public void handleIntent(Intent intent) {
        if (intent == null){
            return;
        }
        if (intent.hasExtra(WXPayEntryActivity.TYPE) && intent.getStringExtra(WXPayEntryActivity.TYPE).equals(WXPayEntryActivity.WX_PAY)){
            int result = intent.getIntExtra(WXPayEntryActivity.RESULT_NAME,1);
            String message = "";
            switch (result){
                case PAYMENT_RESULT_SUCCESS:
                    if (mCallback != null){
                        mCallback.success();
                    }
                    return;
                case PAYMENT_RESULT_FAIL:
                    message = "支付失败";
                    Log.e(TAG,"APP id error or other problem");
                    break;
                case PAYMENT_RESULT_CANCEL:
                    message = "支付取消";
                    Log.e(TAG,"cancel");
                    break;
                default:
                    message = "支付失败";
                    Log.e(TAG,"intent has not result");
                    break;
            }
            fail(message);
        }
    }

    private void signApi(BaseReq request){
        mApi.sendReq(request);
    }

    private void fail(String message){
        if (mCallback != null){
            mCallback.fail(message);
        }
    }

}
