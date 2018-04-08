package com.li.videoapplication.interfaces;

import com.li.videoapplication.data.model.entity.StatisticsModel;

/**
 * Created by linhui on 2018/3/31.
 */
public interface ICell {

    int DEFINITION_VALUES = 2;

    long getSumCount();

    /**
     * 时间间隔值
     * @return
     */
    int getDefinitionValues();

    StatisticsModel getData();

    ICellProcess process();

}
