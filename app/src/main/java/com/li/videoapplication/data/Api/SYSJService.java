package com.li.videoapplication.data.Api;

import com.li.videoapplication.data.model.entity.Game;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.response.EventsList214Entity;
import com.li.videoapplication.data.model.response.EventsPKListEntity;
import com.li.videoapplication.data.model.response.MatchRewardBillboardEntity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.PhotoCollectionEntity;
import com.li.videoapplication.data.model.response.PhotoFlowerEntity;
import com.li.videoapplication.data.model.response.PlayerRankingCurrencyEntity;
import com.li.videoapplication.data.model.response.PlayerRankingEntity;
import com.li.videoapplication.data.model.response.ServiceNameEntity;
import com.li.videoapplication.data.model.response.SignScheduleEntity;
import com.li.videoapplication.data.model.response.UnfinishedTaskEntity;
import com.li.videoapplication.data.model.entity.AdvertisementDto;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.data.model.response.VideoRankingEntity;
import com.li.videoapplication.framework.BaseHttpResult;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * API接口
 * 因为使用RxCache作为缓存策略 所以这里不需要写缓存信息
 */
public interface SYSJService {

    //@GET("接口尾址")（域名 http://apps.ifeimo.com 在 RetrofitUtils 中已配置）

    //获取首页详情
    @GET("/sysj204/index/index")
    Observable<BaseHttpResult<HomeDto>> getHomeInfo(@Query("page") int page);

    //获取首页每日任务
    @GET("/Sysj211/Currency/unfinishedTask")
    Observable<UnfinishedTaskEntity> unfinishedTask(@Query("member_id") String member_id);

    //获取首页猜你喜欢（换一换）
    @GET("/sysj201/index/changeGuess")
    Observable<ChangeGuessEntity> changeGuess(@Query("group_ids") String group_ids);

    //获取首页猜你喜欢（换一换）详情
    @GET("/sysj201/index/changeGuessSecond")
    Observable<ChangeGuessEntity> changeGuessSecond(@Query("video_ids") String video_ids);

    //获取广告
    @GET("/sysj208/ad/adImage")
    Observable<AdvertisementDto> adImage208(@Query("localtion_id") int localtion_id);

    //广告点击统计
    @GET("/Sysj204/Advertisement/adClick")
    Observable<BaseHttpResult> adClick(@Query("ad_id") long ad_id,
                                       @Query("ad_click_state") int ad_click_state,
                                       @Query("hardwarecode") String hardwarecode);

    // 玩家榜--粉丝榜, 视频榜
    @GET("/sysj201/ranking/memberRanking")
    Observable<BaseHttpResult<PlayerRankingEntity>> loadRankingData(@Query("member_id") String member_id,
                                                                    @Query("sort") String sort,
                                                                    @Query("page") int page);

    // 玩家榜--磨豆榜
    @GET("/Sysj211/Currency/memberRanking")
    Observable<PlayerRankingCurrencyEntity> loadRankingCurrencyData(@Query("member_id") String member_id,
                                                                    @Query("page") int page);

    // 玩家榜--关注玩家
    @GET("/sysj201/member/attention")
    Observable<MemberAttention201Entity> memberAttention(@Query("member_id") String member_id,
                                                         @Query("id") String id);

    // 视频榜--点赞榜,评论榜,观看榜
    @GET("/sysj201/ranking/videoRanking")
    Observable<BaseHttpResult<VideoRankingEntity>> loadVideoBillboardRankingData(@Query("member_id") String member_id,
                                                                                 @Query("sort") String sort,
                                                                                 @Query("page") int page);

    // 视频榜--视频点赞
    @GET("/sysj201/video/flower")
    Observable<VideoFlower2Entity> videoFlower(@Query("video_id") String video_id,
                                               @Query("member_id") String member_id);

    // 视频榜--图文点赞
    @GET("/sysj201/video/flower")
    Observable<PhotoFlowerEntity> photoFlower(@Query("pic_id") String pic_id,
                                              @Query("member_id") String member_id,
                                              @Query("isflower") int isflower);

    // 视频榜--视频收藏
    @GET("/sysj201/video/collect")
    Observable<VideoCollect2Entity> videoCollect(@Query("video_id") String video_id,
                                                 @Query("member_id") String member_id);

    // 视频榜--图文收藏
    @GET("/Sysj201/Photo/collection")
    Observable<PhotoCollectionEntity> photoCollection(@Query("pic_id") String pic_id,
                                                      @Query("member_id") String member_id,
                                                      @Query("iscoll") int iscoll);

    // 赛事奖金榜
    @GET("/sysj213/Events/rewardRankDetail")
    Observable<MatchRewardBillboardEntity> loadMatchRewardBillboardData();

    //获取赛事列表
//    @GET("/Sysj214/NewEvents/getEventsList")// FIXME: 2016/12/27 test!!!!
    @GET("/Sysj214/NewEvents/getEventsListTest")
    Observable<BaseHttpResult<EventsList214Entity>> getEventsList(@Query("page") int page,
                                                                  @Query("format_type") int format_type,
                                                                  @Query("game_id") String game_id);

    //获取赛事列表 游戏类型筛选
    @GET("/Sysj214/GameCategory/getGameCate")
    Observable<BaseHttpResult<List<Game>>> getGameCate();

    //获取我的赛事列表
    @GET("/Sysj214/NewEvents/getEventsList")
    Observable<BaseHttpResult<EventsList214Entity>> getMyEventsList(@Query("page") int page,
                                                                    @Query("member_id") String member_id);

    // 赛事结果
    @GET("/Sysj214/NewEvents/eventResultDetail")
    Observable<BaseHttpResult<MatchRewardBillboardEntity>> getEventResult(@Query("event_id") String event_id,
                                                                          @Query("terminal") String terminal);

    //获取圈子赛事列表
    @GET("/Sysj214/NewEvents/getGroupEventsList")
    Observable<BaseHttpResult<EventsList214Entity>> getGroupEventsList(@Query("page") int page,
                                                                       @Query("game_id") String game_id);

    //获取赛事详情
    @GET("/Sysj214/NewEvents/getEventsInfo")
    Observable<BaseHttpResult<Match>> getEventsInfo(@Query("event_id") String event_id,
                                                    @Query("member_id") String member_id);

    //赛事签到
    @GET("/Sysj214/NewEvents/signSchedule")
    Observable<SignScheduleEntity> signSchedule(@QueryMap Map<String, Object> options);

    //赛事加入群聊
    @GET("/sysj204/YongYun/groupJoin")
    Observable<BaseHttpResult> groupJoin(@QueryMap Map<String, Object> options);

    //获取客服名称
    @GET("/sysj204/events/serviceName")
    Observable<BaseHttpResult<ServiceNameEntity>> getServiceName(@Query("member_id") String member_id);

    //赛事赛程
    @GET("/Sysj214/NewEvents/eventsSchedule")
    Observable<BaseHttpResult<List<Match>>> getEventsSchedule(@Query("event_id") String event_id);

    //赛事赛程pk列表
    @GET("/Sysj214/NewEvents/eventsPKList")
    Observable<BaseHttpResult<EventsPKListEntity>> getEventsPKList(@QueryMap Map<String, Object> options);
}
