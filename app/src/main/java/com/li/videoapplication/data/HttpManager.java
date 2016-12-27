package com.li.videoapplication.data;

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
import com.li.videoapplication.data.Api.CacheProviders;
import com.li.videoapplication.data.Api.SYSJService;
import com.li.videoapplication.data.Retrofit.ApiException;
import com.li.videoapplication.data.Retrofit.RetrofitUtils;
import com.li.videoapplication.data.local.SYSJStorageUtil;
import com.li.videoapplication.data.model.response.VideoCollect2Entity;
import com.li.videoapplication.data.model.response.VideoFlower2Entity;
import com.li.videoapplication.data.model.response.VideoRankingEntity;
import com.li.videoapplication.data.network.RequestParams;
import com.li.videoapplication.framework.BaseHttpResult;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import io.rx_cache.internal.RxCache;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 功能：数据请求管理类
 * 所有的请求数据的方法集中地
 * 根据 API接口：SYSJService的定义编写合适的方法
 */
public class HttpManager extends RetrofitUtils {
    private static final String TAG = "HttpManager";
    private static final String A_SYSJ = "a_sysj";
    //缓存目录
    private static File cacheDirectory = SYSJStorageUtil.getSysjCache();
    //缓存提供器
    private static final CacheProviders providers = new RxCache.Builder()
            .persistence(cacheDirectory)
            .using(CacheProviders.class);

    //API接口
    protected static final SYSJService service = getRetrofit().create(SYSJService.class);

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpManager INSTANCE = new HttpManager();
    }

    //获取单例
    public static HttpManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //############################################################

    /**
     * 插入观察者
     */
    private static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<BaseHttpResult<T>, T> {

        @Override
        public T call(BaseHttpResult<T> baseHttpResult) {
            if (!baseHttpResult.isResult()) {
                throw new ApiException(baseHttpResult);
            }
            return baseHttpResult.getData();
        }
    }

    /**
     * 用来统一处理RxCacha的结果
     */
    private class HttpResultFuncCache<T> implements Func1<Reply<T>, T> {

        @Override
        public T call(Reply<T> httpResult) {
            return httpResult.getData();
        }
    }

    //############################################################

    /**
     * @param isload 是否加载新数据 true：加载数据 false：使用缓存不加载数据
     *               Observable 是获取API数据
     *               observableCahce获取缓存数据
     *               new EvictDynamicKey(isload) true：加载数据不使用缓存 false：使用缓存不加载数据
     */

    // TODO: ############### 首页 ###############

    //获取首页信息
    public void getHomeInfo(int page, boolean isload, Observer<HomeDto> observer) {
        Observable<HomeDto> observable = service.getHomeInfo(page).map(new HttpResultFunc<HomeDto>());
        Observable<HomeDto> observableCache = providers.getHomeInfo(observable,
                new DynamicKey("getHomeInfo&" + page),
                new EvictDynamicKey(isload)).map(new HttpResultFuncCache<HomeDto>());
        setSubscribe(observableCache, observer);
    }

    //首页每日任务
    public void unfinishedTask(String member_id, boolean update, Observer<UnfinishedTaskEntity> observer) {
        Observable<UnfinishedTaskEntity> observable = service.unfinishedTask(member_id);
        Observable<UnfinishedTaskEntity> observableCache = providers.unfinishedTask(observable,
                new DynamicKey("unfinishedTask&"),
                new EvictDynamicKey(update));
        setSubscribe(observableCache, observer);
    }

    //首页猜你喜欢（换一换）
    public void changeGuess(String group_ids, Observer<ChangeGuessEntity> observer) {
        Observable<ChangeGuessEntity> observable = service.changeGuess(group_ids);
        setSubscribe(observable, observer);
    }

    //首页猜你喜欢（换一换）详情
    public void changeGuessSecond(String video_ids, Observer<ChangeGuessEntity> observer) {
        Observable<ChangeGuessEntity> observable = service.changeGuessSecond(video_ids);
        setSubscribe(observable, observer);
    }

    // TODO: ############### 广告 ###############

    /**
     * @param localtion_id 不要怀疑自己的英语，后台做接口拼错的，就得这么写。
     */
    public void adImage208(int localtion_id, boolean isLoad, Observer<AdvertisementDto> observer) {
        Observable<AdvertisementDto> observable = service.adImage208(localtion_id);
        Observable<AdvertisementDto> observableCache = providers.adImage208(observable,
                new DynamicKey("Ad&" + localtion_id),
                new EvictDynamicKey(isLoad)).map(new HttpResultFuncCache<AdvertisementDto>());
        setSubscribe(observableCache, observer);
    }

    //广告点击统计
    public void adClick(long ad_id, int ad_click_state, String hardwarecode, Observer<BaseHttpResult> observer) {
        Observable<BaseHttpResult> observable = service.adClick(ad_id, ad_click_state, hardwarecode);
        setSubscribe(observable, observer);
    }

    // TODO: ############### 榜单 ###############

    // 玩家榜--粉丝榜
    public void loadRankingFansData(String member_id, int page, Observer<PlayerRankingEntity> observer) {
        Observable<PlayerRankingEntity> observable = service.loadRankingData(member_id, "fans", page)
                .map(new HttpResultFunc<PlayerRankingEntity>());
        setSubscribe(observable, observer);
    }

    // 玩家榜--磨豆榜
    public void loadRankingCurrencyData(String member_id, int page, Observer<PlayerRankingCurrencyEntity> observer) {
        Observable<PlayerRankingCurrencyEntity> observable = service.loadRankingCurrencyData(member_id, page);
        setSubscribe(observable, observer);
    }

    // 玩家榜--视频榜
    public void loadRankingVideoData(String member_id, int page, Observer<PlayerRankingEntity> observer) {
        Observable<PlayerRankingEntity> observable = service.loadRankingData(member_id, "video", page)
                .map(new HttpResultFunc<PlayerRankingEntity>());
        setSubscribe(observable, observer);
    }

    // 玩家榜--关注玩家
    public void memberAttention(String member_id, String id, Observer<MemberAttention201Entity> observer) {
        Observable<MemberAttention201Entity> observable = service.memberAttention(member_id, id);
        setSubscribe(observable, observer);
    }

    // 视频榜--点赞榜,评论榜,观看榜
    public void loadVideoBillboardRankingData(String member_id, String sort, int page, Observer<VideoRankingEntity> observer) {
        Observable<VideoRankingEntity> observable = service.loadVideoBillboardRankingData(member_id, sort, page)
                .map(new HttpResultFunc<VideoRankingEntity>());
        setSubscribe(observable, observer);
    }

    // 视频榜--视频点赞
    public void videoFlower(String video_id, String member_id, Observer<VideoFlower2Entity> observer) {
        Observable<VideoFlower2Entity> observable = service.videoFlower(video_id, member_id);
        setSubscribe(observable, observer);
    }

    // 视频榜--图文点赞
    public void photoFlower(String pic_id, String member_id, int isflower, Observer<PhotoFlowerEntity> observer) {
        Observable<PhotoFlowerEntity> observable = service.photoFlower(pic_id, member_id, isflower);
        setSubscribe(observable, observer);
    }

    // 视频榜--视频收藏
    public void videoCollect(String video_id, String member_id, Observer<VideoCollect2Entity> observer) {
        Observable<VideoCollect2Entity> observable = service.videoCollect(video_id, member_id);
        setSubscribe(observable, observer);
    }

    // 视频榜--图文收藏
    public void photoCollection(String pic_id, String member_id, int iscoll, Observer<PhotoCollectionEntity> observer) {
        Observable<PhotoCollectionEntity> observable = service.photoCollection(pic_id, member_id, iscoll);
        setSubscribe(observable, observer);
    }

    // 赛事奖金榜
    public void loadMatchRewardBillboardData(Observer<MatchRewardBillboardEntity> observer) {
        Observable<MatchRewardBillboardEntity> observable = service.loadMatchRewardBillboardData();
        setSubscribe(observable, observer);
    }

    // TODO: ############### 赛事 ###############
    // 赛事列表
    public void getEventsList(int page, int format_type, String game_id, Observer<EventsList214Entity> observer) {
        Observable<EventsList214Entity> observable = service.getEventsList(page, format_type, game_id)
                .map(new HttpResultFunc<EventsList214Entity>());
        setSubscribe(observable, observer);
    }

    // 赛事列表 游戏类型筛选
    public void getGameCate(Observer<List<Game>> observer) {
        Observable<List<Game>> observable = service.getGameCate().map(new HttpResultFunc<List<Game>>());
        setSubscribe(observable, observer);
    }

    // 我的赛事列表
    public void getMyEventsList(int page, String member_id, Observer<EventsList214Entity> observer) {
        Observable<EventsList214Entity> observable = service.getMyEventsList(page, member_id)
                .map(new HttpResultFunc<EventsList214Entity>());
        setSubscribe(observable, observer);
    }

    // 赛事结果
    public void getEventResult(String event_id, Observer<MatchRewardBillboardEntity> observer) {
        Observable<MatchRewardBillboardEntity> observable = service.getEventResult(event_id, "app")
                .map(new HttpResultFunc<MatchRewardBillboardEntity>());
        setSubscribe(observable, observer);
    }

    // 圈子赛事列表
    public void getGroupEventsList(int page, String game_id, Observer<EventsList214Entity> observer) {
        Observable<EventsList214Entity> observable = service.getGroupEventsList(page, game_id)
                .map(new HttpResultFunc<EventsList214Entity>());
        setSubscribe(observable, observer);
    }

    // 赛事详情
    public void getEventsInfo(String event_id, String member_id, Observer<Match> observer) {
        Observable<Match> observable = service.getEventsInfo(event_id, member_id)
                .map(new HttpResultFunc<Match>());
        setSubscribe(observable, observer);
    }

    // 赛事签到
    public void signSchedule(String member_id, String schedule_id, Observer<SignScheduleEntity> observer) {
        Map<String, Object> params = RequestParams.getInstance().signSchedule214(member_id, schedule_id);
        Observable<SignScheduleEntity> observable = service.signSchedule(params);
        setSubscribe(observable, observer);
    }

    // 加入群聊
    public void groupJoin(String member_id, String chatroom_group_id, Observer<BaseHttpResult> observer) {
        Map<String, Object> params = RequestParams.getInstance().groupJoin(member_id, chatroom_group_id);
        Observable<BaseHttpResult> observable = service.groupJoin(params);
        setSubscribe(observable, observer);
    }

    // 客服名称
    public void getServiceName(String member_id, Observer<ServiceNameEntity> observer) {
        Observable<ServiceNameEntity> observable = service.getServiceName(member_id)
                .map(new HttpResultFunc<ServiceNameEntity>());
        setSubscribe(observable, observer);
    }

    // 赛事赛程
    public void getEventsSchedule(String event_id, Observer<List<Match>> observer) {
        Observable<List<Match>> observable = service.getEventsSchedule(event_id)
                .map(new HttpResultFunc<List<Match>>());
        setSubscribe(observable, observer);
    }

    // 赛事赛程pk列表
    public void getEventsPKList(String schedule_id, int page, String member_id, Observer<EventsPKListEntity> observer) {
        Map<String, Object> params = RequestParams.getInstance().eventsPKList(schedule_id, page, member_id);
        Observable<EventsPKListEntity> observable = service.getEventsPKList(params)
                .map(new HttpResultFunc<EventsPKListEntity>());
        setSubscribe(observable, observer);
    }
}
