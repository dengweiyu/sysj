package com.li.videoapplication.mvp.billboard.presenter;

import android.util.Log;

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
import com.li.videoapplication.mvp.billboard.BillboardContract.IMatchRewardBillboardView;
import com.li.videoapplication.mvp.billboard.BillboardContract.IBillboardModel;
import com.li.videoapplication.mvp.billboard.BillboardContract.IBillboardPresenter;
import com.li.videoapplication.mvp.billboard.BillboardContract.IPlayerBillboardView;
import com.li.videoapplication.mvp.billboard.BillboardContract.IVideoBillboardView;
import com.li.videoapplication.mvp.billboard.model.BillboardModel;

/**
 * Presenter实现类: 榜单
 */
public class BillboardPresenter implements IBillboardPresenter {

    private static final String TAG = BillboardPresenter.class.getSimpleName();

    private IPlayerBillboardView playerBillboardView;
    private IVideoBillboardView videoBillboardView;
    private IMatchRewardBillboardView matchRewardBillboardView;
    private IBillboardModel billboardModel;

    public BillboardPresenter() {
        billboardModel = BillboardModel.getInstance();
    }

    //玩家榜view
    public void setPlayerBillboardView(IPlayerBillboardView playerBillboardView) {
        this.playerBillboardView = playerBillboardView;
    }

    //视频榜view
    public void setVideoBillboardView(IVideoBillboardView videoBillboardView) {
        this.videoBillboardView = videoBillboardView;
    }

    //赛事奖金榜view
    @Override
    public void setMatchRewardBillboardView(IMatchRewardBillboardView matchRewardBillboardView) {
        this.matchRewardBillboardView = matchRewardBillboardView;
    }

    //视频榜view


    //玩家榜--粉丝榜
    @Override
    public void loadRankingFansData(String member_id, int page) {
        billboardModel.loadRankingFansData(member_id, page, new OnLoadDataListener<PlayerRankingEntity>() {
            @Override
            public void onSuccess(PlayerRankingEntity data) {
                playerBillboardView.hideProgress();
                playerBillboardView.refreshRankingFansData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    //玩家榜--财富榜
    @Override
    public void loadRankingCurrencyData(String member_id, int page) {
        billboardModel.loadRankingCurrencyData(member_id, page, new OnLoadDataListener<PlayerRankingCurrencyEntity>() {
            @Override
            public void onSuccess(PlayerRankingCurrencyEntity data) {
                playerBillboardView.hideProgress();
                playerBillboardView.refreshRankingCurrencyData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    //玩家榜--视频榜
    @Override
    public void loadRankingVideoData(String member_id, int page) {
        billboardModel.loadRankingVideoData(member_id, page, new OnLoadDataListener<PlayerRankingEntity>() {
            @Override
            public void onSuccess(PlayerRankingEntity data) {
                playerBillboardView.hideProgress();
                playerBillboardView.refreshRankingVideoData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    //玩家榜--玩家关注
    @Override
    public void memberAttention(String member_id, String id) {
        billboardModel.memberAttention(member_id, id, new OnLoadDataListener<MemberAttention201Entity>() {
            @Override
            public void onSuccess(MemberAttention201Entity data) {
                playerBillboardView.hideProgress();
                playerBillboardView.refreshPlayerAttention(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    //视频榜--点赞榜,评论榜,观看榜
    @Override
    public void loadVideoBillboardRankingData(String member_id, String sort, int page) {
        billboardModel.loadVideoBillboardRankingData(member_id, sort, page, new OnLoadDataListener<VideoRankingEntity>() {
            @Override
            public void onSuccess(VideoRankingEntity data) {
                videoBillboardView.hideProgress();
                videoBillboardView.refreshVideoBillboardRankingData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    // 视频榜--视频点赞
    @Override
    public void videoFlower(String video_id, String member_id) {
        billboardModel.videoFlower(video_id, member_id, new OnLoadDataListener<VideoFlower2Entity>(){

            @Override
            public void onSuccess(VideoFlower2Entity data) {
                Log.d(TAG, "onSuccess: "+data.getMsg());
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    // 视频榜--图文点赞
    @Override
    public void photoFlower(String pic_id, String member_id, int isflower) {
        billboardModel.photoFlower(pic_id, member_id, isflower, new OnLoadDataListener<PhotoFlowerEntity>(){

            @Override
            public void onSuccess(PhotoFlowerEntity data) {
                Log.d(TAG, "onSuccess: "+data.getMsg());
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    // 视频榜--视频收藏
    @Override
    public void videoCollect(String video_id, String member_id) {
        billboardModel.videoCollect(video_id, member_id, new OnLoadDataListener<VideoCollect2Entity>(){

            @Override
            public void onSuccess(VideoCollect2Entity data) {
                Log.d(TAG, "onSuccess: "+data.getMsg());
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    // 视频榜--图文收藏
    @Override
    public void photoCollection(String pic_id, String member_id, int iscoll) {
        billboardModel.photoCollection(pic_id, member_id, iscoll, new OnLoadDataListener<PhotoCollectionEntity>(){

            @Override
            public void onSuccess(PhotoCollectionEntity data) {
                Log.d(TAG, "onSuccess: "+data.getMsg());
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    @Override
    public void loadMatchRewardBillboardData() {
        billboardModel.loadMatchRewardBillboardData(new OnLoadDataListener<MatchRewardBillboardEntity>(){

            @Override
            public void onSuccess(MatchRewardBillboardEntity data) {
                matchRewardBillboardView.refreshData(data);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }
}
