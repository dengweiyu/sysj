package com.li.videoapplication.mvp.mall.model;

import android.util.Log;

import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.entity.PaymentList;
import com.li.videoapplication.data.model.entity.TopUp;
import com.li.videoapplication.data.model.response.BillEntity;
import com.li.videoapplication.data.model.response.PaymentEntity;
import com.li.videoapplication.data.model.response.RechargeCoinEntity;
import com.li.videoapplication.data.model.response.TopUpOptionEntity;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.mall.MallContract.IMallModel;

import java.util.List;

import rx.Observer;

/**
 * Model层: 商城
 */
public class MallModel implements IMallModel {
    private static MallModel mallModel;
    public final static int USE_RECHARGE_MONEY = 0;
    public final static int USE_RECHARGE_VIP = 1;
    public final static int USE_RECHARGE_COIN = 2;
    public static synchronized IMallModel getInstance() {
        if (mallModel == null) {
            mallModel = new MallModel();
        }
        return mallModel;
    }

    @Override
    public void getOrderList(String member_id, final OnLoadDataListener<List<Currency>> listener) {
        HttpManager.getInstance().getOrderList(member_id, new Observer<List<Currency>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(List<Currency> entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getMemberAward(String member_id, final OnLoadDataListener<List<Currency>> listener) {
        HttpManager.getInstance().getMemberAward(member_id, new Observer<List<Currency>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(List<Currency> entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void orderDetail(String member_id, String order_id, final OnLoadDataListener<Currency> listener) {
        HttpManager.getInstance().orderDetail(member_id, order_id, new Observer<Currency>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(Currency entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getMemberAwardDetail(String member_id, String id, final OnLoadDataListener<Currency> listener) {
        HttpManager.getInstance().getMemberAwardDetail(member_id, id, new Observer<Currency>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(Currency entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getRechargeRule(final OnLoadDataListener<TopUpOptionEntity> listener) {
        HttpManager.getInstance().getRechargeRule(new Observer<TopUpOptionEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(TopUpOptionEntity entity) {
                if (entity != null && entity.isResult())
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void payment(int use,String member_id,int level,int packageKey, String num, int pay_type, int ingress, final OnLoadDataListener<PaymentEntity> listener) {
        Observer<PaymentEntity> observer =  new Observer<PaymentEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(PaymentEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        };

        switch (use){
            case USE_RECHARGE_MONEY:        //充值魔豆
                HttpManager.getInstance().payment(member_id, num, pay_type, ingress,observer);
                break;
            case USE_RECHARGE_VIP:          //开通VIP
                HttpManager.getInstance().payment(member_id,level, packageKey,pay_type,observer);
                break;
            case USE_RECHARGE_COIN:         //充值魔币
                HttpManager.getInstance().paymentCoin(member_id,num,pay_type,ingress,observer);
                break;
        }
    }

    @Override
    public void getTopUpRecordList(String member_id,final OnLoadDataListener<List<TopUp>> listener) {
        HttpManager.getInstance().getTopUpRecordList(member_id, new Observer<List<TopUp>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(List<TopUp> entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void getPaymentList(String target,final OnLoadDataListener<PaymentList> listener) {
        HttpManager.getInstance().getPaymentList(target, new Observer<PaymentList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                    listener.onFailure(e);
                Log.e("getPaymentList error",e.toString());
            }

            @Override
            public void onNext(PaymentList paymentList) {
                if (paymentList != null){
                    listener.onSuccess(paymentList);
                }
            }
        });
    }

    @Override
    public void getCoinList(final OnLoadDataListener<RechargeCoinEntity> listener) {
        HttpManager.getInstance().getCoinList(new Observer<RechargeCoinEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(RechargeCoinEntity entity) {
                if (entity != null){
                    listener.onSuccess(entity);
                }
            }
        });
    }

    @Override
    public void getBillList(String member_id,int type,final OnLoadDataListener<BillEntity> listener) {
        HttpManager.getInstance().getBillList(member_id, type, new Observer<BillEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(BillEntity entity) {
                if (entity != null){
                    listener.onSuccess(entity);
                }
            }
        });
    }
}
