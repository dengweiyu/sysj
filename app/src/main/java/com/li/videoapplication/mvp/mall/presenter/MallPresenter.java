package com.li.videoapplication.mvp.mall.presenter;

import android.util.Log;

import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.entity.PaymentList;
import com.li.videoapplication.data.model.entity.TopUp;
import com.li.videoapplication.data.model.response.PaymentEntity;
import com.li.videoapplication.data.model.response.TopUpOptionEntity;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.mall.MallContract;
import com.li.videoapplication.mvp.mall.MallContract.ITopUpRecordView;
import com.li.videoapplication.mvp.mall.MallContract.ITopUpView;
import com.li.videoapplication.mvp.mall.MallContract.IMallPresenter;
import com.li.videoapplication.mvp.mall.MallContract.IMallModel;
import com.li.videoapplication.mvp.mall.MallContract.IExchangeRecordView;
import com.li.videoapplication.mvp.mall.MallContract.IExchangeRecordDetailView;
import com.li.videoapplication.mvp.mall.model.MallModel;

import java.util.List;

/**
 * Presenter实现类: 商城
 */
public class MallPresenter implements IMallPresenter {
    private static final String TAG = MallPresenter.class.getSimpleName();

    private IMallModel mallModel;
    private IExchangeRecordView exchangeRecordView;
    private IExchangeRecordDetailView exchangeRecordDetailView;
    private ITopUpView topUpView;
    private ITopUpRecordView topUpRecordView;
    private MallContract.IPaymentListView paymentListView;
    public MallPresenter() {
        mallModel = MallModel.getInstance();
    }

    //兑换记录view
    @Override
    public void setExchangeRecordView(IExchangeRecordView exchangeRecordView) {
        this.exchangeRecordView = exchangeRecordView;
    }

    @Override
    public void setExchangeRecordDetailView(IExchangeRecordDetailView exchangeRecordDetailView) {
        this.exchangeRecordDetailView = exchangeRecordDetailView;
    }

    @Override
    public void setTopUpView(ITopUpView topUpView) {
        this.topUpView = topUpView;
    }

    @Override
    public void setTopUpRecordView(ITopUpRecordView topUpRecordView) {
        this.topUpRecordView = topUpRecordView;
    }


    @Override
    public void setPaymentLisView(MallContract.IPaymentListView paymentLisView) {
        this.paymentListView = paymentLisView;
    }

    @Override
    public void getOrderList(String member_id) {
        mallModel.getOrderList(member_id, new OnLoadDataListener<List<Currency>>() {
            @Override
            public void onSuccess(List<Currency> data) {
                exchangeRecordView.refreshOrderListData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getMemberAward(String member_id) {
        mallModel.getMemberAward(member_id, new OnLoadDataListener<List<Currency>>() {
            @Override
            public void onSuccess(List<Currency> data) {
                exchangeRecordView.refreshRewardListData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void orderDetail(String member_id, String order_id) {
        mallModel.orderDetail(member_id, order_id, new OnLoadDataListener<Currency>() {
            @Override
            public void onSuccess(Currency data) {
                exchangeRecordDetailView.refreshOrderDetailData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getMemberAwardDetail(String member_id, String id) {
        mallModel.getMemberAwardDetail(member_id, id, new OnLoadDataListener<Currency>() {
            @Override
            public void onSuccess(Currency data) {
                exchangeRecordDetailView.refreshRewardDetailData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getRechargeRule() {
        mallModel.getRechargeRule(new OnLoadDataListener<TopUpOptionEntity>() {
            @Override
            public void onSuccess(TopUpOptionEntity data) {
                topUpView.refreshTopUpOptionData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void payment(String member_id, String currency_num, int pay_type, int ingress) {
        mallModel.payment(member_id, currency_num,pay_type,ingress, new OnLoadDataListener<PaymentEntity>() {
            @Override
            public void onSuccess(PaymentEntity data) {
                paymentListView.refreshOrderInfoData(data);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e("payment",e.getMessage());
            }
        });
    }

    @Override
    public void getTopUpRecordList(String member_id) {
        mallModel.getTopUpRecordList(member_id, new OnLoadDataListener<List<TopUp>>() {
            @Override
            public void onSuccess(List<TopUp> data) {
                topUpRecordView.refreshTopUpRecordData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void getPaymentList(String target) {
        mallModel.getPaymentList(target, new OnLoadDataListener<PaymentList>() {
            @Override
            public void onSuccess(PaymentList data) {
                   paymentListView.refreshPaymentList(data);
            }

            @Override
            public void onFailure(Throwable e) {
            }
        });
    }
}
