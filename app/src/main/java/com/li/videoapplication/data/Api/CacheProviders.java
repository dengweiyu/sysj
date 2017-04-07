package com.li.videoapplication.data.Api;

import com.li.videoapplication.data.model.response.AdvertisementDto;
import com.li.videoapplication.data.model.entity.HomeDto;
import com.li.videoapplication.data.model.response.UnfinishedTaskEntity;

import java.util.concurrent.TimeUnit;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.LifeCache;
import io.rx_cache.Reply;
import rx.Observable;

/**
 * 缓存API接口
 * <p>
 * LifeCache：设置缓存过期时间. 如果没有设置 @LifeCache, 数据将被永久缓存理除非你使用了 EvictProvider, EvictDynamicKey or EvictDynamicKeyGroup .
 * EvictProvider：可以明确地清理清理所有缓存数据.
 * DynamicKey：驱逐与一个特定的键使用EvictDynamicKey相关的数据。比如分页，排序或筛选要求
 * EvictDynamicKey：可以明确地清理指定的数据 DynamicKey.
 * DynamicKeyGroup：驱逐一组与key关联的数据，使用EvictDynamicKeyGroup。比如分页，排序或筛选要求
 * EvictDynamicKeyGroup：允许明确地清理一组特定的数据. DynamicKeyGroup.
 */
public interface CacheProviders {

    //获取首页数据 不设置过期  在数据更新后更新缓存
      @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<Reply<HomeDto>> getHomeInfo(Observable<HomeDto> oRepos,
                                           DynamicKey userName,
                                           EvictDynamicKey evictDynamicKey);

    //获取广告数据 缓存时间1天
    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<Reply<AdvertisementDto>> adImage208(Observable<AdvertisementDto> oRepos,
                                                   DynamicKey userName,
                                                   EvictDynamicKey evictDynamicKey);

    //获取首页任务 缓存时间1天
    @LifeCache(duration = 1, timeUnit = TimeUnit.DAYS)
    Observable<UnfinishedTaskEntity> unfinishedTask(Observable<UnfinishedTaskEntity> oRepos,
                                                    DynamicKey userName,
                                                    EvictDynamicKey evictDynamicKey);
}
