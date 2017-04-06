package com.li.videoapplication.payment;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.li.videoapplication.data.model.response.PaymentEntity;

/**
 * Created by liuwei on 2017/4/2.
 */

public interface IPayment {
    boolean isAvailable();
    void executePayment(PaymentEntity entity, @Nullable Callback callback);
    void handleIntent(Intent intent);
     interface Callback{
        void success();
        void fail(String message);
    }
}
