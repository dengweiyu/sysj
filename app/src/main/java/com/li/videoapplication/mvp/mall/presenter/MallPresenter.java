package com.li.videoapplication.mvp.mall.presenter;

import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.mvp.OnLoadDataListener;
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
        mallModel.orderDetail(member_id,order_id, new OnLoadDataListener<Currency>() {
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
        mallModel.getMemberAwardDetail(member_id,id, new OnLoadDataListener<Currency>() {
            @Override
            public void onSuccess(Currency data) {
                exchangeRecordDetailView.refreshRewardDetailData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

}
