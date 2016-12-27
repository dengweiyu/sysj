package com.li.videoapplication.mvp.billboard.model;

import com.li.videoapplication.data.HttpManager;
import com.li.videoapplication.data.model.response.MatchRewardBillboardEntity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.PhotoCollectionEntity;
import com.li.videoapplication.data.model.response.PhotoFlowerEntity;
import com.li.videoapplication.data.model.response.PlayerRankingCurrencyEntity;
import com.li.videoapplication.data.model.response.PlayerRankingEntity;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.data.model.response.VideoRankingEntity;
import com.li.videoapplication.mvp.OnLoadDataListener;
import com.li.videoapplication.mvp.billboard.BillboardContract.IBillboardModel;

import rx.Observer;

/**
 * Model层: 首页
 */
public class BillboardModel implements IBillboardModel {

    private static BillboardModel billboardModel;

    public static synchronized IBillboardModel getInstance() {
        if (billboardModel == null) {
            billboardModel = new BillboardModel();
        }
        return billboardModel;
    }

    @Override
    public void loadRankingFansData(String member_id, int page, final OnLoadDataListener<PlayerRankingEntity> listener) {
        HttpManager.getInstance().loadRankingFansData(member_id, page, new Observer<PlayerRankingEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(PlayerRankingEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void loadRankingCurrencyData(String member_id, int page, final OnLoadDataListener<PlayerRankingCurrencyEntity> listener) {
        HttpManager.getInstance().loadRankingCurrencyData(member_id, page, new Observer<PlayerRankingCurrencyEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(PlayerRankingCurrencyEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void loadRankingVideoData(String member_id, int page, final OnLoadDataListener<PlayerRankingEntity> listener) {
        HttpManager.getInstance().loadRankingVideoData(member_id, page, new Observer<PlayerRankingEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(PlayerRankingEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void memberAttention(String member_id, String id, final OnLoadDataListener<MemberAttention201Entity> listener) {
        HttpManager.getInstance().memberAttention(member_id, id, new Observer<MemberAttention201Entity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(MemberAttention201Entity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void loadVideoBillboardRankingData(String member_id, String sort, int page, final OnLoadDataListener<VideoRankingEntity> listener) {
        HttpManager.getInstance().loadVideoBillboardRankingData(member_id, sort, page, new Observer<VideoRankingEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(VideoRankingEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void videoFlower(String video_id, String member_id, final OnLoadDataListener<VideoFlower2Entity> listener) {
        HttpManager.getInstance().videoFlower(video_id, member_id, new Observer<VideoFlower2Entity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(VideoFlower2Entity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void photoFlower(String pic_id, String member_id, int isflower, final OnLoadDataListener<PhotoFlowerEntity> listener) {
        HttpManager.getInstance().photoFlower(pic_id, member_id, isflower, new Observer<PhotoFlowerEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(PhotoFlowerEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void videoCollect(String video_id, String member_id, final OnLoadDataListener<VideoCollect2Entity> listener) {
        HttpManager.getInstance().videoCollect(video_id, member_id, new Observer<VideoCollect2Entity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(VideoCollect2Entity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void photoCollection(String pic_id, String member_id, int iscoll, final OnLoadDataListener<PhotoCollectionEntity> listener) {
        HttpManager.getInstance().photoCollection(pic_id, member_id, iscoll, new Observer<PhotoCollectionEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(PhotoCollectionEntity entity) {
                if (entity != null)
                    listener.onSuccess(entity);
            }
        });
    }

    @Override
    public void loadMatchRewardBillboardData(final OnLoadDataListener<MatchRewardBillboardEntity> listener) {
        HttpManager.getInstance().loadMatchRewardBillboardData(new Observer<MatchRewardBillboardEntity>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                listener.onFailure(e);
            }

            @Override
            public void onNext(MatchRewardBillboardEntity entity) {
                if (entity != null && entity.isResult())
                    listener.onSuccess(entity);
            }
        });
    }


}
