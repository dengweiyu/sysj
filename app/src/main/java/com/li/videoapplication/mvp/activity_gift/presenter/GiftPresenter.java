package com.li.videoapplication.mvp.activity_gift.presenter;

import com.li.videoapplication.data.model.response.MyPackageEntity;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IGiftModel;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IGiftPresenter;
import com.li.videoapplication.mvp.activity_gift.ActivityGiftContract.IMyGiftView;
import com.li.videoapplication.mvp.activity_gift.model.GiftModel;

/**
 * Presenter实现类: 礼包
 */
public class GiftPresenter implements IGiftPresenter{

    private IGiftModel giftModel;
    private static GiftPresenter giftPresenter;

    private IMyGiftView myGiftView;


    private GiftPresenter() {
        giftModel = GiftModel.getInstance();
    }

    public static synchronized GiftPresenter getInstance() {
        if (giftPresenter == null) {
            giftPresenter = new GiftPresenter();
        }
        return giftPresenter;
    }

    @Override
    public void setMyGiftView(IMyGiftView myGiftView) {
        this.myGiftView = myGiftView;
    }

    @Override
    public void getMyGiftList(String member_id, int page) {
        giftModel.getMyGiftList(member_id, page, new OnLoadDataListener<MyPackageEntity>() {
            @Override
            public void onSuccess(MyPackageEntity data) {
                myGiftView.hideProgress();
                myGiftView.refreshMyGiftListData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }
}
