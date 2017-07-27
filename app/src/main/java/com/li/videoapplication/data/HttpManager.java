package com.li.videoapplication.data;

import android.util.Log;

import com.li.videoapplication.data.model.entity.Currency;
import com.li.videoapplication.data.model.entity.Download;
import com.li.videoapplication.data.model.entity.Match;
import com.li.videoapplication.data.model.entity.PaymentList;
import com.li.videoapplication.data.model.entity.TopUp;
import com.li.videoapplication.data.model.response.BillEntity;
import com.li.videoapplication.data.model.response.ChangeGuessEntity;
import com.li.videoapplication.data.model.response.EventsList214Entity;
import com.li.videoapplication.data.model.response.EventsPKListEntity;
import com.li.videoapplication.data.model.response.GameCateEntity;
import com.li.videoapplication.data.model.response.MatchRecordEntity;
import com.li.videoapplication.data.model.response.MatchRewardBillboardEntity;
import com.li.videoapplication.data.model.response.MemberAttention201Entity;
import com.li.videoapplication.data.model.response.MyMatchListEntity;
import com.li.videoapplication.data.model.response.MyPackageEntity;
import com.li.videoapplication.data.model.response.PaymentEntity;
import com.li.videoapplication.data.model.response.PhotoCollectionEntity;
import com.li.videoapplication.data.model.response.PhotoFlowerEntity;
import com.li.videoapplication.data.model.response.PlayerRankingCurrencyEntity;
import com.li.videoapplication.data.model.response.PlayerRankingEntity;
import com.li.videoapplication.data.model.response.RechargeCoinEntity;
import com.li.videoapplication.data.model.response.ServiceNameEntity;
import com.li.videoapplication.data.model.response.SignScheduleEntity;
import com.li.videoapplication.data.model.response.TopUpOptionEntity;
import com.li.videoapplication.data.model.response.UnfinishedTaskEntity;
import com.li.videoapplication.data.model.response.AdvertisementDto;
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
import com.li.videoapplication.tools.DownloadHelper;
import com.li.videoapplication.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import io.rx_cache.internal.RxCache;
import okhttp3.ResponseBody;
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
    private static final String TAG = HttpManager.class.getSimpleName();
    private static final boolean DEBUG = true;

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
            Log.d(TAG, "ResponseHandler: " + baseHttpResult.getData().toString());
            if (!baseHttpResult.isResult()) {
                throw new ApiException(baseHttpResult);
            }
            Log.d(TAG, "ResponseHandler: " + baseHttpResult.toString());

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
    public void adverImage(int location_id, Observer<AdvertisementDto> observer) {
        Map<String, Object> params = RequestParams.getInstance().adverImage(location_id);
        Observable<AdvertisementDto> observable = service.adverImage("http://ad.17sysj.com/Advertisement/adSysjImage",params);
        setSubscribe(observable, observer);
    }

    public void getDownloadOther(String game_id, Observer<List<Download>> observer) {
        Map<String, Object> params = RequestParams.getInstance().gameTagList(game_id);
        Observable<List<Download>> observable = service.getDownloadOther(params)
                .map(new HttpResultFunc<List<Download>>());
        setSubscribe(observable, observer);
    }


    public void getGameDownloadInfo(String game_id, Observer<List<Download>> observer) {
        Map<String, Object> params = RequestParams.getInstance().gameTagList(game_id);
        Observable<List<Download>> observable = service.getGameDownloadInfo(params)
                .map(new HttpResultFunc<List<Download>>());
        setSubscribe(observable, observer);
    }

    /**
     * @param location_id
     */
    public void adImage208(int location_id, boolean isLoad, Observer<AdvertisementDto> observer) {
        Map<String, Object> params = RequestParams.getInstance().adImage208(location_id);
        Observable<AdvertisementDto> observable = service.adImage208("http://ad.17sysj.com/Advertisement/adSysjImage",params);
        Observable<AdvertisementDto> observableCache = providers.adImage208(observable,
                new DynamicKey("Ad&" + location_id),
                new EvictDynamicKey(isLoad)).map(new HttpResultFuncCache<AdvertisementDto>());
        setSubscribe(observableCache, observer);
    }

    //广告点击统计
    public void adClick(long ad_id, int ad_click_state, String hardwarecode, String imei,Observer<BaseHttpResult> observer) {
        Observable<BaseHttpResult> observable = service.adClick(ad_id, ad_click_state, hardwarecode,imei);
        setSubscribe(observable, observer);
    }

    // TODO: ############### 下载 ###############

    public void downloadFiles(List<String> downloadList, Observer<Boolean> observer) {
        List<Observable<Boolean>> observables = new ArrayList<>();
        //将所有的Observable放到List中
        for (int i = 0; i < downloadList.size(); i++) {
            final String downloadUrl = downloadList.get(i);
            observables.add(service.downloadFile(downloadUrl)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<ResponseBody, Boolean>() {
                        @Override
                        public Boolean call(ResponseBody responseBody) {
                            return DownloadHelper.writeResponseBody2Disk(responseBody,
                                    StringUtil.getFileNameWithExt(downloadUrl));
                        }
                    }).subscribeOn(Schedulers.io()));
        }

        //Observable的merge将所有的Observable合成一个Observable,所有的observable同时发射数据。
        Observable.merge(observables).observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    // TODO: ############### 榜单 ###############

    // 主播榜--粉丝榜
    public void loadRankingFansData(String member_id, int page, Observer<PlayerRankingEntity> observer) {
        Observable<PlayerRankingEntity> observable = service.loadRankingData(member_id, "fans", page)
                .map(new HttpResultFunc<PlayerRankingEntity>());
        setSubscribe(observable, observer);
    }

    // 主播榜--磨豆榜
    public void loadRankingCurrencyData(String member_id, int page, Observer<PlayerRankingCurrencyEntity> observer) {
        Observable<PlayerRankingCurrencyEntity> observable = service.loadRankingCurrencyData(member_id, page);
        setSubscribe(observable, observer);
    }

    // 主播榜--视频榜
    public void loadRankingVideoData(String member_id, int page, Observer<PlayerRankingEntity> observer) {
        Observable<PlayerRankingEntity> observable = service.loadRankingData(member_id, "video", page)
                .map(new HttpResultFunc<PlayerRankingEntity>());
        setSubscribe(observable, observer);
    }

    // 主播榜--关注玩家
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
    public void getEventsList(int page, String format_type, String game_id, Observer<EventsList214Entity> observer) {
        Observable<EventsList214Entity> observable = service.getEventsList(page, format_type, game_id)
                .map(new HttpResultFunc<EventsList214Entity>());
        setSubscribe(observable, observer);
    }

    // 赛事列表 游戏类型筛选
    public void getGameCate(Observer<GameCateEntity> observer) {
        Observable<GameCateEntity> observable = service.getGameCate();
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
    public void signSchedule(String member_id, String schedule_id, String event_id, Observer<SignScheduleEntity> observer) {
        Map<String, Object> params = RequestParams.getInstance().signSchedule214(member_id, schedule_id, event_id);
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

    // 历史战绩
    public void getHistoricalRecord(String memberId, int page, Observer<MatchRecordEntity> observer) {
        Map<String, Object> params = RequestParams.getInstance().getHistoricalRecord(memberId, page);
        Observable<MatchRecordEntity> observable = service.getHistoricalRecord(params)
                .map(new HttpResultFunc<MatchRecordEntity>());
        setSubscribe(observable, observer);
    }

    // 赛事流水点击接口
    public void eventsRecordClick(String event_id, int e_record_status, Observer<BaseHttpResult> observer) {
        Map<String, Object> params = RequestParams.getInstance().eventsRecordClick(event_id, e_record_status);
        Observable<BaseHttpResult> observable = service.eventsRecordClick(params);
        setSubscribe(observable, observer);
    }

    // TODO: ############### 活动 礼包 ###############

    // 我的活动
    public void getMyMatchList(String member_id, int page, Observer<MyMatchListEntity> observer) {
        Map<String, Object> params = RequestParams.getInstance().myMatchList(member_id, page);
        Observable<MyMatchListEntity> observable = service.getMyMatchList(params)
                .map(new HttpResultFunc<MyMatchListEntity>());
        setSubscribe(observable, observer);
    }

    // 活动详情
    public void getMatchInfo(String match_id, Observer<Match> observer) {
        Map<String, Object> params = RequestParams.getInstance().getMatchInfo(match_id);
        Observable<Match> observable = service.getMatchInfo(params).map(new HttpResultFunc<Match>());
        setSubscribe(observable, observer);
    }

    // 活动流水点击
    public void competitionRecordClick(String competition_id, int c_record_status, Observer<BaseHttpResult> observer) {
        Map<String, Object> params = RequestParams.getInstance().competitionRecordClick(competition_id, c_record_status);
        Observable<BaseHttpResult> observable = service.competitionRecordClick(params);
        setSubscribe(observable, observer);
    }

    // 我的礼包
    public void getMyGiftList(String member_id, int page, Observer<MyPackageEntity> observer) {
        Map<String, Object> params = RequestParams.getInstance().myMatchList(member_id, page);
        Observable<MyPackageEntity> observable = service.getMyGiftList(params)
                .map(new HttpResultFunc<MyPackageEntity>());
        setSubscribe(observable, observer);
    }

    // TODO: ############### 商城 ###############
    // 兑换记录
    public void getOrderList(String member_id, Observer<List<Currency>> observer) {
        Map<String, Object> params = RequestParams.getInstance().getOrderList(member_id);
        Observable<List<Currency>> observable = service.getOrderList(params)
                .map(new HttpResultFunc<List<Currency>>());
        setSubscribe(observable, observer);
    }

    // 抽奖记录
    public void getMemberAward(String member_id, Observer<List<Currency>> observer) {
        Map<String, Object> params = RequestParams.getInstance().getOrderList(member_id);
        Observable<List<Currency>> observable = service.getMemberAward(params)
                .map(new HttpResultFunc<List<Currency>>());
        setSubscribe(observable, observer);
    }

    // 兑换记录详情
    public void orderDetail(String member_id, String order_id, Observer<Currency> observer) {
        Map<String, Object> params = RequestParams.getInstance().orderDetail(member_id, order_id);
        Observable<Currency> observable = service.orderDetail(params)
                .map(new HttpResultFunc<Currency>());
        setSubscribe(observable, observer);
    }

    // 抽奖记录详情
    public void getMemberAwardDetail(String member_id, String id, Observer<Currency> observer) {
        Map<String, Object> params = RequestParams.getInstance().getMemberAwardDetail(member_id, id);
        Observable<Currency> observable = service.getMemberAwardDetail(params)
                .map(new HttpResultFunc<Currency>());
        setSubscribe(observable, observer);
    }

    // 充值
    public void getRechargeRule(Observer<TopUpOptionEntity> observer) {
        Observable<TopUpOptionEntity> observable = service.getRechargeRule();
        setSubscribe(observable, observer);
    }

    // 支付
    public void payment(String member_id, String currency_num, int pay_type, int ingress, Observer<PaymentEntity> observer) {
        Map<String, Object> params = RequestParams.getInstance().payment(member_id, currency_num, pay_type, ingress);
        Observable<PaymentEntity> observable = service.payment(params);
        setSubscribe(observable, observer);
    }

    //开通会员
    public void payment(String member_id, int  level, int pay_type, Observer<PaymentEntity> observer) {
        Map<String, Object> params = RequestParams.getInstance().payment(member_id, level, pay_type);
        Observable<PaymentEntity> observable = service.rechargeVip(params);
        setSubscribe(observable, observer);
    }

    //充值魔币
    public void paymentCoin(String member_id, String coin_num, int pay_type, int ingress, Observer<PaymentEntity> observer) {
        Map<String, Object> params = RequestParams.getInstance().paymentCoin(member_id, coin_num, pay_type, ingress);
        Observable<PaymentEntity> observable = service.paymentCoin(params);
        setSubscribe(observable, observer);
    }

    // 充值记录
    public void getTopUpRecordList(String member_id, Observer<List<TopUp>> observer) {
        Map<String, Object> params = RequestParams.getInstance().getOrderList(member_id);
        Observable<List<TopUp>> observable = service.getTopUpRecordList(params)
                .map(new HttpResultFunc<List<TopUp>>());
        setSubscribe(observable, observer);
    }

    public void getPaymentList(String target,Observer<PaymentList> observer){
        Observable<PaymentList> observable = service.getPaymentList(target);
              //  .map(new HttpResultFunc<PaymentList>());
        setSubscribe(observable, observer);
    }

    public void getCoinList(Observer<RechargeCoinEntity> observer){
        Observable<RechargeCoinEntity> observable = service.getCoinList();
        setSubscribe(observable, observer);
    }

    public void getBillList(String memeber_id,int type,Observer<BillEntity> observer){
        Map<String, Object> params = RequestParams.getInstance().getBillList(memeber_id,type);
        Observable<BillEntity> observable = service.getBillList(params);
        setSubscribe(observable, observer);
    }
}
