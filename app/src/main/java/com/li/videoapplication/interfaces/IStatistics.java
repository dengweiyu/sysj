package com.li.videoapplication.interfaces;

import android.content.Context;

import com.li.videoapplication.data.model.entity.StatisticsModel;

/**
 * Created by linhui on 2018/3/31.
 * 统计
 */
public interface IStatistics {

    void init(Context context);

    ICell createDefaultParameter(StatisticsModel statisticsModel);

    void insertOne(ICell cell);

    boolean isHadCache(ICell cell);

    IDataBaseStatistics getDataBaseStatistics();

    IHandlerPost getHandlerPost();

}
