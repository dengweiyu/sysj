package com.li.videoapplication.mvp.activity_gift.model;

import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.data.model.response.MyPackageEntity;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IGiftModel;

import rx.Observer;

/**
 * Model层: 礼包
 */
public class GiftModel implements IGiftModel {

    private static GiftModel giftModel;

    public static synchronized IGiftModel getInstance() {
        if (giftModel == null) {
            giftModel = new GiftModel();
        }
        return giftModel;
    }

    @Override
    public void getMyGiftList(String member_id, int page,final OnLoadDataListener<MyPackageEntity> listener) {
        HttpManager.getInstance().getMyGiftList(member_id, page, new Observer<MyPackageEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(MyPackageEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }
}
