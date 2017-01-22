package com.li.videoapplication.mvp.mall.model;

import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.mall.MallContract.IMallModel;

import java.util.List;

import rx.Observer;

/**
 * Model层: 商城
 */
public class MallModel implements IMallModel {
    private static MallModel mallModel;

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
}
