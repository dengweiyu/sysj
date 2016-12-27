package com.li.videoapplication.mvp.home.model;

import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.response.UnfinishedTaskEntity;
import com.li.videoapplication.data.model.entity.AdvertisementDto;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.mvp.home.HomeContract.onloadHomeDataListener;
import com.li.videoapplication.mvp.home.HomeContract.IHomeModel;
import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.framework.BaseHttpResult;

import rx.Observer;

/**
 * Model层: 首页
 */
public class HomeModel implements IHomeModel {

    private static final String TAG = HomeModel.class.getSimpleName();
    private static HomeModel homeModel;

    public static synchronized IHomeModel getInstance() {
        if (homeModel == null) {
            homeModel = new HomeModel();
        }
        return homeModel;
    }

    @Override
    public void loadHomeData(int page, boolean isLoad, final onloadHomeDataListener listener) {

        HttpManager.getInstance().getHomeInfo(page, isLoad, new Observer<HomeDto>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(HomeDto homeDto) {
                listener.onLoadHomeSuccess(homeDto);
            }
        });
    }

    @Override
    public void unfinishedTask(String member_id, boolean update, final onloadHomeDataListener listener) {

        HttpManager.getInstance().unfinishedTask(member_id,update, new Observer<UnfinishedTaskEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(UnfinishedTaskEntity unFinishTaskDto) {
                if (unFinishTaskDto != null && unFinishTaskDto.isResult())
                    listener.onLoadUnFinishTaskSuccess(unFinishTaskDto);
            }
        });
    }

    @Override
    public void changeGuess(String group_ids, final onloadHomeDataListener listener) {

        HttpManager.getInstance().changeGuess(group_ids, new Observer<ChangeGuessEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(ChangeGuessEntity changeGuessDto) {
                if (changeGuessDto != null)
                    listener.onLoadChangeGuessSuccess(changeGuessDto);
            }
        });
    }

    @Override
    public void changeGuessSecond(String video_ids, final onloadHomeDataListener listener) {

        HttpManager.getInstance().changeGuessSecond(video_ids, new Observer<ChangeGuessEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(ChangeGuessEntity changeGuessDto) {
                if (changeGuessDto != null)
                    listener.onLoadChangeGuessSuccess(changeGuessDto);
            }
        });
    }

    @Override
    public void adImage208(int localtion_id, boolean isLoad, final onloadHomeDataListener listener) {

        HttpManager.getInstance().adImage208(localtion_id, isLoad, new Observer<AdvertisementDto>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(AdvertisementDto data) {
                if (data != null)
                    listener.onLoadAdvertisementSuccess(data);
            }
        });
    }

    @Override
    public void adClick(long ad_id, int ad_click_state, String hardwarecode, final onloadHomeDataListener listener) {
        HttpManager.getInstance().adClick(ad_id, ad_click_state, hardwarecode, new Observer<BaseHttpResult>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(BaseHttpResult data) {
                if (data != null && data.isResult())
                    listener.onLoadAdClickSuccess(data);
            }
        });
    }
}
