package com.li.videoapplication.impl;

import android.content.Context;

import com.li.videoapplication.tools.StatisticsCell;
import com.li.videoapplication.data.model.entity.StatisticsModel;
import com.li.videoapplication.interfaces.ICell;
import com.li.videoapplication.interfaces.IDataBaseStatistics;
import com.li.videoapplication.interfaces.IHandlerPost;
import com.li.videoapplication.interfaces.IStatistics;

/**
 * Created by linhui on 2018/4/2.
 */
public class StatisticsImp implements IStatistics {

    private static StatisticsImp STATISTICS_IMP = new StatisticsImp();
    private StatisticsImp(){
        handlerPostImp = new HandlerPostImp();
    }
    public static IStatistics getInstance(){
        return STATISTICS_IMP;
    }

    private IHandlerPost handlerPostImp;
    private IDataBaseStatistics dataBaseStatisticsImp;

    @Override
    public void init(Context context) {
        dataBaseStatisticsImp = new DataBaseStatisticsImp(context);
    }

    @Override
    public ICell createDefaultParameter(StatisticsModel statisticsModel) {
        return StatisticsCell.createDefaultParameter(statisticsModel);
    }

    @Override
    public void insertOne(ICell cell) {
        /**
         * 插入数据库操作
         */
    }

    @Override
    public boolean isHadCache(ICell cell) {
        return false;
    }

    @Override
    public IDataBaseStatistics getDataBaseStatistics() {
        return dataBaseStatisticsImp;
    }

    @Override
    public IHandlerPost getHandlerPost() {
        return handlerPostImp;
    }
}
