package com.li.videoapplication.mvp.billboard;


import com.li.videoapplication.data.model.entity.HomeDto;
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

/**
 * 接口存放类：榜单（赛事奖金榜，玩家榜，视频榜）
 * 放置 Model View Presenter 的接口
 */
public class BillboardContract {

    /**
     * Model层接口: 榜单
     */
    public interface IBillboardModel {
        //玩家榜--粉丝榜
        void loadRankingFansData(String member_id, int page, final OnLoadDataListener<PlayerRankingEntity> listener);
        //玩家榜--磨豆榜
        void loadRankingCurrencyData(String member_id, int page, final OnLoadDataListener<PlayerRankingCurrencyEntity> listener);
        //玩家榜--视频榜
        void loadRankingVideoData(String member_id, int page, final OnLoadDataListener<PlayerRankingEntity> listener);
        //玩家榜--玩家关注
        void memberAttention(String member_id, String id, final OnLoadDataListener<MemberAttention201Entity> listener);
        //视频榜--点赞榜,评论榜,观看榜
        void loadVideoBillboardRankingData(String member_id, String sort, int page, final OnLoadDataListener<VideoRankingEntity> listener);
        //视频榜--视频点赞
        void videoFlower(String video_id, String member_id, final OnLoadDataListener<VideoFlower2Entity> listener);
        //视频榜--图文点赞
        void photoFlower(String pic_id, String member_id, int isflower, final OnLoadDataListener<PhotoFlowerEntity> listener);
        //视频榜--视频收藏
        void videoCollect(String video_id, String member_id, final OnLoadDataListener<VideoCollect2Entity> listener);
        //视频榜--图文收藏
        void photoCollection(String pic_id, String member_id, int iscoll, final OnLoadDataListener<PhotoCollectionEntity> listener);
        //赛事奖金榜
        void loadMatchRewardBillboardData(final OnLoadDataListener<MatchRewardBillboardEntity> listener);
    }

    /**
     * View层接口: 榜单--玩家榜
     */
    public interface IPlayerBillboardView {
        //关闭加载页
        void hideProgress();

        //数据加载成功
        void refreshRankingFansData(PlayerRankingEntity data);

        void refreshRankingCurrencyData(PlayerRankingCurrencyEntity data);

        void refreshRankingVideoData(PlayerRankingEntity data);

        void refreshPlayerAttention(MemberAttention201Entity data);
    }

    /**
     * View层接口: 榜单--视频榜
     */
    public interface IVideoBillboardView {
        //关闭加载页
        void hideProgress();

        //数据加载成功
        void refreshVideoBillboardRankingData(VideoRankingEntity data);
    }

    /**
     * View层接口: 赛事奖金榜
     */
    public interface IMatchRewardBillboardView {
        //数据加载成功
        void refreshData(MatchRewardBillboardEntity data);
    }

    /**
     * Presenter接口: 榜单
     */
    public interface IBillboardPresenter {
        void setPlayerBillboardView(IPlayerBillboardView playerBillboardView);

        void setVideoBillboardView(IVideoBillboardView videoBillboardView);

        void setMatchRewardBillboardView(IMatchRewardBillboardView matchRewardBillboardView);

        void loadRankingFansData(String member_id, int page);

        void loadRankingCurrencyData(String member_id, int page);

        void loadRankingVideoData(String member_id, int page);

        void memberAttention(String member_id, String id);

        void loadVideoBillboardRankingData(String member_id, String sort, int page);

        void videoFlower(String video_id, String member_id);

        void photoFlower(String pic_id, String member_id, int isflower);

        void videoCollect(String video_id, String member_id);

        void photoCollection(String pic_id, String member_id, int iscoll);

        void loadMatchRewardBillboardData();
    }
}
