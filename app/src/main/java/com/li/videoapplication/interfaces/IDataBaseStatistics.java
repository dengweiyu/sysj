package com.li.videoapplication.interfaces;

import com.li.videoapplication.data.model.entity.StatisticsModel;

import java.io.File;
import java.util.List;

/**
 * Created by linhui on 2018/4/2.
 */
public interface IDataBaseStatistics {

    String getDbJsonData(int loadCount);

    String getDbAllJsonData();

    boolean exportDbAllDataToFile(File file);

    List<StatisticsModel> getResourceData(int loadCount);

    List<StatisticsModel> getResourceAllData();

}
