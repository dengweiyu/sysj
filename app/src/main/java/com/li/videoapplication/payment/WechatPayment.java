package com.li.videoapplication.payment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.li.videoapplication.data.model.response.PaymentEntity;
import com.li.videoapplication.mvp.mall.view.PaymentWayActivity;
import com.li.videoapplication.ui.activity.MainActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by liuwei on 2017/4/2.
 */

public class WechatPayment implements IPayment {
    public final static int PAYMENT_RESULT_SUCCESS = 0;
    public final static int PAYMENT_RESULT_FAIL = -1;
    public final static int PAYMENT_RESULT_CANCEL = -2;

    private final static String TYPE = "type";
    public final static String WX_PAY = "wx_pay";                  //intent 携带数据的key 表示是微信支付
    public final static String RESULT_NAME = "wx_pay_result";      //intent 携带数据的key 支付结果

    private final static String APP_ID ="";

    private String TAG = "WechatPayment";

    private IWXAPI mApi;
    private IPayment.Callback mCallback;
    public WechatPayment(Context context){
        init(context);
    }

    private void init(Context context){
        mApi = WXAPIFactory.createWXAPI(context,null);
        mApi.registerApp(APP_ID);
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
        if (intent.hasExtra(TYPE) && intent.getStringExtra(TYPE).equals(WX_PAY)){
            int result = intent.getIntExtra(RESULT_NAME,1);
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

    /**
     *WXPayEntryActivity onResp()中调用
     */
    @SuppressWarnings("unused")
    public static void onResponse(BaseResp response,Context context){
        if (response.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
            Intent intent = new Intent(context, PaymentWayActivity.class);
            intent.putExtra(TYPE,WX_PAY);
            intent.putExtra(RESULT_NAME,response.errCode);
            context.startActivity(intent);
        }
    }
}
