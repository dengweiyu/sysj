package com.li.videoapplication.payment;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.li.videoapplication.data.model.response.PaymentEntity;
import com.tencent.mobileqq.openpay.api.IOpenApi;
import com.tencent.mobileqq.openpay.api.IOpenApiListener;
import com.tencent.mobileqq.openpay.api.OpenApiFactory;
import com.tencent.mobileqq.openpay.constants.OpenConstants;
import com.tencent.mobileqq.openpay.data.base.BaseResponse;
import com.tencent.mobileqq.openpay.data.pay.PayApi;
import com.tencent.mobileqq.openpay.data.pay.PayResponse;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * QQ支付
 */

public class TencentPayment implements IPayment {
    private IOpenApi mOpenApi;
    private final static String APP_ID = "1103189341";

    private final static String TAG = "TenPayment";

    private IPayment.Callback mCallback;
    public TencentPayment(Context context) {
            init(context);
    }

    private void init(Context context){
        try {
            mOpenApi = OpenApiFactory.getInstance(context,APP_ID);
        }catch (Exception e){
            Log.e(TAG,"支付失败 openApi init exception");
        }
    }

    @Override
    public boolean isAvailable() {
            return null == mOpenApi?false:mOpenApi.isMobileQQInstalled() && mOpenApi.isMobileQQSupportApi(OpenConstants.API_NAME_PAY);
    }

    @Override
    public void executePayment(PaymentEntity entity, Callback callback) {
        mCallback = callback;
        if (!isAvailable()){
            if (mCallback != null){
                mCallback.fail("请您安装或更新手机QQ");
            }
            return;
        }
        if (entity == null){
            if (mCallback != null){
                mCallback.fail("订单号获取失败");
            }
        }else {

            PaymentEntity.DataBean data = entity.getData();
            PayApi api = new PayApi();
            api.appId = data.getAppId();
            api.tokenId = data.getTokenId();
            api.callbackScheme ="qwallet1103189341";
            api.serialNumber = System.currentTimeMillis()/100+"";
            api.pubAcc = data.getPubAcc();
            api.pubAccHint = "支付完成";
            api.nonce = data.getNonce();
            api.timeStamp = data.getTimestamp()/1000;
            api.bargainorId = data.getBargainorId();
            api.sig = data.getSign();
            api.sigType = "HMAC-SHA1";      //加密方式

            if (api.checkParams()){
                mOpenApi.execApi(api);
            }

        }
    }

    @Override
    public void handleIntent(Intent intent) {
        mOpenApi.handleIntent(intent,mListener);
    }

    final IOpenApiListener mListener = new IOpenApiListener() {
        @Override
        public void onOpenResponse(BaseResponse response) {
            String message = "";
            if (response == null) {
                message = "支付失败";
                Log.e(TAG,"支付失败 response is null.");
            } else {
                if (response instanceof PayResponse) {
                    PayResponse payResponse = (PayResponse) response;
                    if (payResponse.isSuccess()) {
                        if (!payResponse.isPayByWeChat()) {
                            if (mCallback != null){
                                mCallback.success();
                                return;
                            }
                        }

                    }else {
                        message = "支付取消";
                        Log.e(TAG,"支付失败 response isSuccess is false.");
                    }
                } else {
                    message = "支付失败";
                    Log.e(TAG,"支付失败 response is not PayResponse.");
                }
            }
            if (mCallback != null){
                mCallback.fail(message);
            }
        }
    };
}
